package icu.ghost.rpc.protocol;

/**
 * @author Ghost
 * @version 1.0
 * @className ProtocolConstant
 * @date 2024/08/12 12:37
 * @since 1.0
 */
public interface ProtocolConstant {
    /**
     * 消息头长度
     */
    int MESSAGE_HEADER_LENGTH = 17;

    /**
     * 协议魔数
     */
    byte PROTOCOL_MAGIC = 0x1;

    /**
     * 协议版本号
     */
    byte PROTOCOL_VERSION = 0x1;
}
