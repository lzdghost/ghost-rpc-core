package icu.ghost.rpc.protocol;

import icu.ghost.rpc.serializer.Serializer;
import icu.ghost.rpc.serializer.SerializerFactory;
import io.vertx.core.buffer.Buffer;

import java.io.IOException;

/**
 * @author Ghost
 * @version 1.0
 * @className ProtocolMessageEncoder
 * @date 2024/08/12 13:44
 * @since 1.0
 */
public class ProtocolMessageEncoder {
    public static Buffer encode(ProtocolMessage<?> protocolMessage) throws IOException {
        if (protocolMessage == null || protocolMessage.getHeader() == null) {
            return Buffer.buffer();
        }
        ProtocolMessage.Header header = protocolMessage.getHeader();
        // 依次向缓冲区写入字节
        Buffer buffer = Buffer.buffer();
        buffer.appendByte(header.getMagic());
        buffer.appendByte(header.getVersion());
        buffer.appendByte(header.getSerializer());
        buffer.appendByte(header.getType());
        buffer.appendByte(header.getStatus());
        buffer.appendLong(header.getRequestId());

        // 获取序列化器
        ProtocolMessageSerializerEnum messageSerializerEnum = ProtocolMessageSerializerEnum.getByKey(header.getSerializer());
        if (messageSerializerEnum == null) {
            throw new RuntimeException("序列化协议不存在");
        }
        Serializer serializer = SerializerFactory.getInstance(messageSerializerEnum.getValue());
        byte[] serializedBody = serializer.serialize(protocolMessage.getBody());
        // 写入长度和数据
        buffer.appendInt(serializedBody.length);
        buffer.appendBytes(serializedBody);
        return buffer;
    }
}
