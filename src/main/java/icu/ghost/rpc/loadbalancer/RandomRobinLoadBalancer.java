package icu.ghost.rpc.loadbalancer;

import icu.ghost.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author Ghost
 * @version 1.0
 * @className RandomRobinLoadBalancer
 * @date 2024/08/12 21:26
 * @since 1.0
 */
public class RandomRobinLoadBalancer implements LoadBalancer{
    private final Random random = new Random();

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
        // 随机
        int index = random.nextInt(size);
        return serviceMetaInfoList.get(index);
    }
}
