package icu.ghost.rpc.serializer;

import java.io.IOException;

/**
 * @author Ghost
 * @version 1.0
 * @className Serializer
 * @date 2024/07/28 17:57
 * @since 1.0
 */
public interface Serializer {
    /**
     * 序列化
     * @param object 要序列化的对象
     * @return 字节流
     * @param <T> 泛型
     */
    <T> byte[] serialize(T object) throws IOException;

    /**
     * 反序列化
     * @param bytes 字节流
     * @param type 反序列化类型
     * @return 反序列化后的对象
     * @param <T> 泛型
     */
    <T> T deserialize(byte[] bytes, Class<T> type) throws IOException;
}
