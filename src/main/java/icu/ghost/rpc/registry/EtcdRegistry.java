package icu.ghost.rpc.registry;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.json.JSONUtil;
import icu.ghost.rpc.config.RegistryConfig;
import icu.ghost.rpc.model.ServiceMetaInfo;
import io.etcd.jetcd.*;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;
import io.etcd.jetcd.watch.WatchEvent;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @author Ghost
 * @version 1.0
 * @className EtcdRegistry
 * @date 2024/07/31 16:19
 * @since 1.0
 */
public class EtcdRegistry implements Registry{

    private Client client;

    private KV kvClient;

    public static final String ETCD_ROOT_PATH = "/rpc/";

    /**
     * 本机注册的节点 key 集合（用于维护续期）
     */
    private final Set<String> localRegisterNodeKeySet = new HashSet<>();

    /**
     * 注册中心服务缓存(单服务)
     */
    private final RegistryServiceCache registryServiceCache =  new RegistryServiceCache();

    /**
     * 注册中心服务缓存（多服务）
     */
    private final RegistryServiceMultiCache registryServiceMultiCache =  new RegistryServiceMultiCache();

    private final Set<String> watchingKeySet = new ConcurrentHashSet<>();

    @Override
    public void init(RegistryConfig registryConfig) {
        client = Client
                .builder()
                .endpoints(registryConfig.getAddress())
                .connectTimeout(Duration.ofMillis(registryConfig.getTimeOut()))
                .build();
        kvClient = client.getKVClient();

        // 开启心跳检测
        heartBeat();
    }

    @Override
    public void registry(ServiceMetaInfo serviceMetaInfo) throws ExecutionException, InterruptedException {
        // 创建客户端
        Lease leaseClient = client.getLeaseClient();
        // 创建一个30秒的租约
        long leaseId = leaseClient.grant(30).get().getID();

        // 设置要存储的键值对
        String registryKey = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        ByteSequence key = ByteSequence.from(registryKey, StandardCharsets.UTF_8);
        ByteSequence value = ByteSequence.from(JSONUtil.toJsonStr(serviceMetaInfo), StandardCharsets.UTF_8);

        // 键值对和租约关联起来，并设置过期时间
        PutOption putOption = PutOption.builder().withLeaseId(leaseId).build();
        kvClient.put(key,value,putOption).get();

        // 添加结点到本地缓存
        localRegisterNodeKeySet.add(registryKey);

    }

    @Override
    public void unRegistry(ServiceMetaInfo serviceMetaInfo) {
        String registryKey = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        ByteSequence key = ByteSequence.from(registryKey, StandardCharsets.UTF_8);
        kvClient.delete(key);

        // 从本地缓存中移除
        localRegisterNodeKeySet.remove(registryKey);
    }

    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String serviceKey) {
        // 先从缓存中获取
        /*List<ServiceMetaInfo> serviceMetaInfoListCache = registryServiceCache.readCache();*/
        List<ServiceMetaInfo> serviceMetaInfoListCache = registryServiceMultiCache.readCache(serviceKey);
        if (serviceMetaInfoListCache != null) {
            return serviceMetaInfoListCache;
        }

        // 没有缓存走下面

        // 前缀搜索
        String searchPrefix = ETCD_ROOT_PATH + serviceKey;
        try {
            GetOption getOption = GetOption.builder().isPrefix(true).build();
            List<KeyValue> keyValueList = kvClient.get(ByteSequence.from(searchPrefix, StandardCharsets.UTF_8), getOption)
                    .get()
                    .getKvs();
            List<ServiceMetaInfo> serviceMetaInfoList = keyValueList.stream().map(keyValue -> {
                // 监听key的变化
                String key = keyValue.getKey().toString(StandardCharsets.UTF_8);
                watch(key);

                String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                return JSONUtil.toBean(value, ServiceMetaInfo.class);
            }).collect(Collectors.toList());

            // 写入缓存
            /*registryServiceCache.writeCache(serviceMetaInfoList);*/
            registryServiceMultiCache.writeCache(serviceKey,serviceMetaInfoList);
            return serviceMetaInfoList;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("获取服务列表失败",e);
        }

    }

    @Override
    public void destroy() {
        System.out.println("当前结点已经下线");
        // 下线结点
        for (String key: localRegisterNodeKeySet) {
            try {
                kvClient.delete(ByteSequence.from(key, StandardCharsets.UTF_8)).get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }

        if (kvClient != null) {
            kvClient.close();
        }
        if (client != null) {
            client.close();
        }
    }

    @Override
    public void heartBeat() {
        // 10秒一续签
        CronUtil.schedule("*/10 * * * * *", new Task() {
            @Override
            public void execute() {
                // 遍历所有的key
                for (String key : localRegisterNodeKeySet) {
                    try {
                        List<KeyValue> keyValueList = kvClient.get(ByteSequence.from(key, StandardCharsets.UTF_8))
                                .get()
                                .getKvs();
                        // 结点已经过期
                        if (CollUtil.isEmpty(keyValueList)) {
                            continue;
                        }

                        // 结点没有过期，重新注册
                        KeyValue keyValue = keyValueList.get(0);
                        String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                        ServiceMetaInfo serviceMetaInfo = JSONUtil.toBean(value, ServiceMetaInfo.class);
                        // 注册
                        registry(serviceMetaInfo);
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException("续期失败",e);
                    }
                }
            }
        });

        // 支持秒级别定时任务
        CronUtil.setMatchSecond(true);
        CronUtil.start();
    }

    @Override
    public void watch(String serviceNodeKey) {
        Watch watchClient = client.getWatchClient();
        // 之前没有被监听，开启监听
        boolean newWatch = watchingKeySet.add(serviceNodeKey);
        if (newWatch) {
            ByteSequence from = ByteSequence.from(serviceNodeKey, StandardCharsets.UTF_8);
            watchClient.watch(from, watchResponse -> {
               for (WatchEvent event: watchResponse.getEvents()) {
                    switch (event.getEventType()) {
                        // key删除时触发
                        case DELETE:
                            registryServiceMultiCache.clearCache(serviceNodeKey);
                            break;
                        case PUT:
                        default:
                            break;
                    }
               }
            });
        }
    }
}
