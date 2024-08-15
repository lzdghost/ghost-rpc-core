package icu.ghost.rpc.fault.retry;

import icu.ghost.rpc.loadbalancer.LoadBalancer;
import icu.ghost.rpc.loadbalancer.RoundRobinLoadBalancer;
import icu.ghost.rpc.spi.SPILoader;


/**
 * @author Ghost
 * @version 1.0
 * @className SerializerFactory
 * @date 2024/07/30 23:03
 * @since 1.0
 */
public class RetryFactory {

    /**
     * 重试策略映射关系（用于实现单例）
     */
    static {
        SPILoader.load(RetryStrategy.class);
    }

    /**
     * 默认重试策略
     */
    private static final RetryStrategy DEFAULT_RETRY_STRATEGY = new NoRetryStrategy();

    // 单例：提供公共的访问方法
    /**
     *
     * @param key
     * @return
     */
    public static RetryStrategy getInstance(String key) {
        return SPILoader.getInstance(RetryStrategy.class, key);
    }
}
