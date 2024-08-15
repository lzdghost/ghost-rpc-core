package icu.ghost.rpc.server.tcp;

import icu.ghost.rpc.protocol.ProtocolConstant;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.parsetools.RecordParser;

/**
 * @author Ghost
 * @version 1.0
 * @className TcpBufferHandlerWrapper
 * @date 2024/08/12 18:02
 * @since 1.0
 */

/**
 * 装饰着模式，使用
 */
public class TcpBufferHandlerWrapper implements Handler<Buffer> {

    private final RecordParser recordParser;

    public TcpBufferHandlerWrapper(Handler<Buffer> handler) {
        this.recordParser = initRecordParser(handler);
    }

    @Override
    public void handle(Buffer event) {
        recordParser.handle(event);
    }

    private RecordParser initRecordParser(Handler<Buffer> handler) {
        RecordParser parser = RecordParser.newFixed(ProtocolConstant.MESSAGE_HEADER_LENGTH);

        parser.setOutput(new Handler<Buffer>() {
            // 初始化
            int size = -1;
            Buffer resultBuffer = Buffer.buffer();

            @Override
            public void handle(Buffer buffer) {
                if (size == -1) {
                    // 消息体长度
                    size = buffer.getInt(13);
                    parser.fixedSizeMode(size);
                    // 头消息
                    resultBuffer.appendBuffer(buffer);
                } else {
                    // 体消息
                    resultBuffer.appendBuffer(buffer);
                    // 已经拼接为完整buffer，执行处理
                    handler.handle(resultBuffer);
                    // 重置
                    parser.fixedSizeMode(ProtocolConstant.MESSAGE_HEADER_LENGTH);
                    size = -1;
                    resultBuffer = Buffer.buffer();
                }
            }
        });
        return parser;
    }
}
