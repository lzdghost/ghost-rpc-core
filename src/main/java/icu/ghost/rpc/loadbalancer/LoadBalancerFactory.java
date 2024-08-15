package icu.ghost.rpc.loadbalancer;

import icu.ghost.rpc.serializer.JDKSerializer;
import icu.ghost.rpc.serializer.Serializer;
import icu.ghost.rpc.spi.SPILoader;


/**
 * @author Ghost
 * @version 1.0
 * @className SerializerFactory
 * @date 2024/07/30 23:03
 * @since 1.0
 */
public class LoadBalancerFactory {

    /**
     * 负载均衡器映射关系（用于实现单例）
     */
    static {
        SPILoader.load(LoadBalancer.class);
    }

    /**
     * 默认负载均衡器
     */
    private static final LoadBalancer DEFAULT_LOAD_BALANCER = new RoundRobinLoadBalancer();

    // 单例：提供公共的访问方法
    /**
     *
     * @param key
     * @return
     */
    public static LoadBalancer getInstance(String key) {
        return SPILoader.getInstance(LoadBalancer.class, key);
    }
}
