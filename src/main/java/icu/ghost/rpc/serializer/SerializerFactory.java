package icu.ghost.rpc.serializer;

import icu.ghost.rpc.spi.SPILoader;


/**
 * @author Ghost
 * @version 1.0
 * @className SerializerFactory
 * @date 2024/07/30 23:03
 * @since 1.0
 */
public class SerializerFactory {

    /**
     * 序列化器映射关系（用于实现单例）
     */
    /*private static final Map<String, Serializer> KEY_SERIALIZER_MAP = new HashMap<String, Serializer>(){{
        put(SerializerKeys.JDK, new JDKSerializer());
        put(SerializerKeys.JSON, new JsonSerializer());
        put(SerializerKeys.KRYO, new KryoSerializer());
        put(SerializerKeys.Hessian, new HessianSerializer());
    }};*/
    static {
        SPILoader.load(Serializer.class);
    }

    /**
     * 默认序列化器
     */
    private static final Serializer DEFAULT_SERIALIZER = new JDKSerializer();

    // 单例：提供公共的访问方法
    /**
     *
     * @param serializerKey 序列化器名字
     * @return 序列化器
     */
    public static Serializer getInstance(String serializerKey) {
        // return KEY_SERIALIZER_MAP.get(serializerKey);
        return SPILoader.getInstance(Serializer.class, serializerKey);
    }
}
