package icu.ghost.rpc.loadbalancer;

import icu.ghost.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Ghost
 * @version 1.0
 * @className ConsistentHashLoadBalancer
 * @date 2024/08/12 21:44
 * @since 1.0
 */
public class ConsistentHashLoadBalancer implements LoadBalancer{
    /**
     * 一致性hash环，存放虚拟节点
     */
    private TreeMap<Integer,ServiceMetaInfo> virtualNodes = new TreeMap<>();

    /**
     * 虚拟结点个数
     */
    private static final int VIRTUAL_NODE_NUM = 100;

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParam, List<ServiceMetaInfo> serviceMetaInfoList) {
        if (serviceMetaInfoList.isEmpty()) {
            return null;
        }
        // 构建hash环
        for (ServiceMetaInfo serviceMetaInfo : serviceMetaInfoList) {
            for (int i = 0; i < VIRTUAL_NODE_NUM; i++) {
                String key = serviceMetaInfo.getServiceAddress() + "#" + i;
                int hash = getHash(key);
                virtualNodes.put(hash, serviceMetaInfo);
            }
        }

        // 调用请求的hash值
        int hash = getHash(requestParam);
        // 选择最接近并且大于等于 调用请求hash值的虚拟节点
        // 定向搜索: ceilingEntry(), floorEntry(), higherEntry()和 lowerEntry()
        // 等方法可以用于定位大于、小于、大于等于、小于等于给定键的最接近的键值对。
        Map.Entry<Integer, ServiceMetaInfo> entry = virtualNodes.ceilingEntry(hash);
        if (entry == null) {
            // 没有则返回环首部的结点
            entry = virtualNodes.firstEntry();
        }
        return entry.getValue();
    }

    /**
     * 获取hash值
     * 这里可以实现自己的hash算法
     * @param key
     * @return
     */
    private int getHash(Object key) {
        return key.hashCode();
    }
}
