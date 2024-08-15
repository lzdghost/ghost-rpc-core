package icu.ghost.rpc.server.tcp;

import icu.ghost.rpc.server.HttpServer;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;

/**
 * @author Ghost
 * @version 1.0
 * @className VertxTcpServer
 * @date 2024/08/12 13:11
 * @since 1.0
 */
public class VertxTcpServer implements HttpServer {
    @Override
    public void doStart(int port) {
        // 创建Vertx实例
        Vertx vertx = Vertx.vertx();

        // 创建TCP服务器
        NetServer server = vertx.createNetServer();

        // 处理请求
        /*server.connectHandler(socket -> {
            // 处理连接
            socket.handler(buffer -> {
                // 处理接收到字节数组
                byte[] requestData = buffer.getBytes();
                // 自定义字节数组处理逻辑，比如解析请求，调用服务，构造响应
                byte[] responseData = handleRequest(requestData);
                // 发送响应
                socket.write(Buffer.buffer(responseData));
            });
        });*/
        server.connectHandler(new TcpServerHandler());

        // 启动TCP服务器并监听指定端口
        server.listen(port,result -> {
            if (result.succeeded()) {
                System.out.println("Server is now listening on port:" + port);
            } else {
                System.out.println("Failed to start server!" + result.cause());
            }
        });
    }

    /*private byte[] handleRequest(byte[] requestData) {
        // 处理请求的逻辑
        return "Hello client".getBytes();
    }

    public static void main(String[] args) {
        new VertxTcpServer().doStart(8888);
    }*/
}
