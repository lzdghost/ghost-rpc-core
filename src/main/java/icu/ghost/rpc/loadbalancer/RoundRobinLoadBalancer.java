package icu.ghost.rpc.loadbalancer;

import icu.ghost.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Ghost
 * @version 1.0
 * @since 1.0
 * @className RoundRobinLoadBalancer
 * @date 2024/08/12 21:12
 */
public class RoundRobinLoadBalancer implements LoadBalancer {
    private final AtomicInteger currentIndex = new AtomicInteger(0);

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParam, List<ServiceMetaInfo> serviceMetaInfoList) {
        if (serviceMetaInfoList.isEmpty()) {
            return null;
        }
        int size = serviceMetaInfoList.size();
        // 只有一个服务，不需要轮询
        if (size == 1) {
            return serviceMetaInfoList.get(0);
        }
        // 取模轮询
        int index = currentIndex.getAndIncrement() % size;
        return serviceMetaInfoList.get(index);
    }
}
