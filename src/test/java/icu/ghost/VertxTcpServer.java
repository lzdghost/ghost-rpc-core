package icu.ghost;

import icu.ghost.rpc.server.HttpServer;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import io.vertx.core.parsetools.RecordParser;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VertxTcpServer implements HttpServer {

    @Override
    public void doStart(int port) {
        // 创建 Vert.x 实例
        Vertx vertx = Vertx.vertx();

        // 创建 TCP 服务器
        NetServer server = vertx.createNetServer();

        // 处理请求
//        server.connectHandler(new TcpServerHandler());
        server.connectHandler(socket -> {
            socket.handler(buffer -> {
                // 构造parser
                RecordParser parser = RecordParser.newFixed(8);
                parser.setOutput(new Handler<Buffer>() {
                    // 初始化
                    int size = -1;
                    Buffer resultBuffer = Buffer.buffer();

                    @Override
                    public void handle(Buffer buffer) {
                        if (size == -1) {
                            // 消息体长度
                            size = buffer.getInt(4);
                            parser.fixedSizeMode(size);
                            // 头消息
                            resultBuffer.appendBuffer(buffer);
                        } else {
                            // 体消息
                            resultBuffer.appendBuffer(buffer);
                            System.out.println(resultBuffer.toString());
                            // 重置
                            parser.fixedSizeMode(8);
                            size = -1;
                            resultBuffer = Buffer.buffer();
                        }
                    }
                });
                socket.handler(parser);
            });
        });

        // 启动 TCP 服务器并监听指定端口
        server.listen(port, result -> {
            if (result.succeeded()) {
                log.info("TCP server started on port " + port);
            } else {
                log.info("Failed to start TCP server: " + result.cause());
            }
        });
    }

    public static void main(String[] args) {
        new VertxTcpServer().doStart(8888);
    }
}
