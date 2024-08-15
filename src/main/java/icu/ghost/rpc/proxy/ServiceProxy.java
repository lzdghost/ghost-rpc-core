package icu.ghost.rpc.proxy;

import cn.hutool.core.collection.CollUtil;
import icu.ghost.rpc.RpcApplication;
import icu.ghost.rpc.config.RegistryConfig;
import icu.ghost.rpc.config.RpcConfig;
import icu.ghost.rpc.constant.RpcConstant;
import icu.ghost.rpc.fault.retry.RetryFactory;
import icu.ghost.rpc.fault.retry.RetryStrategy;
import icu.ghost.rpc.fault.tolerant.TolerantFactory;
import icu.ghost.rpc.fault.tolerant.TolerantStrategy;
import icu.ghost.rpc.loadbalancer.LoadBalancer;
import icu.ghost.rpc.loadbalancer.LoadBalancerFactory;
import icu.ghost.rpc.model.RpcRequest;
import icu.ghost.rpc.model.RpcResponse;
import icu.ghost.rpc.model.ServiceMetaInfo;
import icu.ghost.rpc.registry.Registry;
import icu.ghost.rpc.registry.RegistryFactory;
import icu.ghost.rpc.serializer.Serializer;
import icu.ghost.rpc.serializer.SerializerFactory;
import icu.ghost.rpc.server.tcp.VertxTcpClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author Ghost
 * @version 1.0
 * @className ServiceProxy
 * @date 2024/07/28 20:47
 * @since 1.0
 */
public class ServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 序列化器
        // JDKSerializer serializer = new JDKSerializer();
        String serializerKey = RpcApplication.getRpcConfig().getSerializerKey();
        Serializer serializer = SerializerFactory.getInstance(serializerKey);

        // 构造请求
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();

        // 序列化
        byte[] serialized = serializer.serialize(rpcRequest);

        // 发送请求
        // 这里地址被硬编码了（需要注册中心和服务发现）
        // 从注册中心获取服务提供者请求地址
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());

        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(method.getDeclaringClass().getName());
        serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
        List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
        if (CollUtil.isEmpty(serviceMetaInfoList)) {
            throw new RuntimeException("暂无服务地址");
        }
        // 暂时先取第一个
        // ServiceMetaInfo selectedServiceMetaInfo = serviceMetaInfoList.get(0);
        // 负载均衡器
        String loadBalancerKey = rpcConfig.getLoadBalancer();
        LoadBalancer loadBalancer = LoadBalancerFactory.getInstance(loadBalancerKey);
        // 调用方法名作为负载均衡参数
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("methodName",rpcRequest.getMethodName());
        ServiceMetaInfo selectedServiceMetaInfo = loadBalancer.select(requestParams, serviceMetaInfoList);

        // HttpResponse httpResponse = HttpRequest.post("http://localhost:8080")
        /*HttpResponse httpResponse = HttpRequest.post(selectedServiceMetaInfo.getServiceAddress())
                .body(serialized)
                .execute();
        byte[] result = httpResponse.bodyBytes();

        // 反序列化
        RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);*/

        // 修改为，发送TCP请求
        // 发送 TCP 请求
        // RpcResponse rpcResponse = VertxTcpClient.doRequest(rpcRequest, selectedServiceMetaInfo);

        // 重试机制
        /*String retryStrategyKey = rpcConfig.getRetryStrategy();
        RetryStrategy retryStrategy = RetryFactory.getInstance(retryStrategyKey);
        RpcResponse rpcResponse = retryStrategy.doRetry(new Callable<RpcResponse>() {
            @Override
            public RpcResponse call() throws Exception {
                return VertxTcpClient.doRequest(rpcRequest, selectedServiceMetaInfo);
            }
        });*/
        RpcResponse rpcResponse = null;
        try {
            // 重试机制
            String retryStrategyKey = rpcConfig.getRetryStrategy();
            RetryStrategy retryStrategy = RetryFactory.getInstance(retryStrategyKey);
            rpcResponse = retryStrategy.doRetry(new Callable<RpcResponse>() {
                @Override
                public RpcResponse call() throws Exception {
                    return VertxTcpClient.doRequest(rpcRequest, selectedServiceMetaInfo);
                }
            });
        } catch (Exception e) {
            // 容错机制
            String tolerantStrategyKey = rpcConfig.getTolerantStrategy();
            TolerantStrategy tolerantStrategy = TolerantFactory.getInstance(tolerantStrategyKey);
            tolerantStrategy.doTolerant(null,e);
        }

        return rpcResponse.getData();

    }
}
