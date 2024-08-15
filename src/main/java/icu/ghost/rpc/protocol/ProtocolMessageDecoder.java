package icu.ghost.rpc.protocol;

import icu.ghost.rpc.model.RpcRequest;
import icu.ghost.rpc.model.RpcResponse;
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
public class ProtocolMessageDecoder {
    public static ProtocolMessage<?> decode(Buffer buffer) throws IOException {
        // header赋值
        ProtocolMessage.Header header = new ProtocolMessage.Header();
        byte magic = buffer.getByte(0);
        // 校验魔数
        if (magic != ProtocolConstant.PROTOCOL_MAGIC) {
            throw new RuntimeException("消息magic非法");
        }
        header.setMagic(magic);
        header.setVersion(buffer.getByte(1));
        header.setSerializer(buffer.getByte(2));
        header.setType(buffer.getByte(3));
        header.setStatus(buffer.getByte(4));
        header.setRequestId(buffer.getLong(5));
        header.setBodyLength(buffer.getInt(13));
        // 解决粘包问题，只读指定长度的数据
        byte[] bodyBytes = buffer.getBytes(17, 17 + header.getBodyLength());

        // 序列化器
        ProtocolMessageSerializerEnum messageSerializerEnum = ProtocolMessageSerializerEnum.getByKey(header.getSerializer());
        if (messageSerializerEnum == null) {
            throw new RuntimeException("消息协议序列化器不存在");
        }
        Serializer serializer = SerializerFactory.getInstance(messageSerializerEnum.getValue());
        // 解析消息体
        ProtocolMessageTypeEnum messageTypeEnum = ProtocolMessageTypeEnum.getByKey(header.getType());
        if (messageTypeEnum == null) {
            throw new RuntimeException("消息协议类型不存在");
        }
        switch (messageTypeEnum) {
            case REQUEST:
                RpcRequest rpcRequest = serializer.deserialize(bodyBytes, RpcRequest.class);
                return new ProtocolMessage<>(header,rpcRequest);
            case RESPONSE:
                RpcResponse rpcResponse = serializer.deserialize(bodyBytes, RpcResponse.class);
                return new ProtocolMessage<>(header,rpcResponse);
            case HEART_BEAT:
            case OTHERS:
            default:
                throw new RuntimeException("暂不支持该消息类型");
        }
    }
}
