package icu.ghost.rpc.fault.tolerant;

import icu.ghost.rpc.fault.retry.NoRetryStrategy;
import icu.ghost.rpc.fault.retry.RetryStrategy;
import icu.ghost.rpc.spi.SPILoader;


/**
 * @author Ghost
 * @version 1.0
 * @className SerializerFactory
 * @date 2024/07/30 23:03
 * @since 1.0
 */
public class TolerantFactory {

    /**
     * 容错策略映射关系（用于实现单例）
     */
    static {
        SPILoader.load(TolerantStrategy.class);
    }

    /**
     * 默认容错策略
     */
    private static final TolerantStrategy DEFAULT_RETRY_STRATEGY = new FailFastTolerantStrategy();

    // 单例：提供公共的访问方法
    /**
     *
     * @param key
     * @return
     */
    public static TolerantStrategy getInstance(String key) {
        return SPILoader.getInstance(TolerantStrategy.class, key);
    }
}
