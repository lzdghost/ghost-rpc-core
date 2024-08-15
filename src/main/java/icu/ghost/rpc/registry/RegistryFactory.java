package icu.ghost.rpc.registry;

import icu.ghost.rpc.spi.SPILoader;


/**
 * @author Ghost
 * @version 1.0
 * @className SerializerFactory
 * @date 2024/07/30 23:03
 * @since 1.0
 */
public class RegistryFactory {

    static {
        SPILoader.load(Registry.class);
    }

    /**
     * 默认注册中心
     */
    private static final Registry DEFAULT_REGISTRY = new EtcdRegistry();

    /**
     * 单例：提供公共的访问方法
     */
    public static Registry getInstance(String key) {
        return SPILoader.getInstance(Registry.class, key);
    }
}
