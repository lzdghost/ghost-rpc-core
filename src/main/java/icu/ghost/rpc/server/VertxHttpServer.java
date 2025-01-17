package icu.ghost.rpc.server;

import io.vertx.core.Vertx;

/**
 * @author Ghost
 * @version 1.0
 * @className VertxHttpServer
 * @date 2024/07/28 16:19
 * @since 1.0
 */
public class VertxHttpServer implements HttpServer{
    public void doStart(int port) {
        // 创建vert.x实例
        Vertx vertx = Vertx.vertx();

        // 创建Http服务器
        io.vertx.core.http.HttpServer server = vertx.createHttpServer();

        // 监听端口并处理请求
        /*server.requestHandler(request -> {
            // 处理HTTP请求
            System.out.println("Received request,request method:" + request.method() + ",request uri" + request.uri());

            // 发送HTTP响应
            request.response()
                    .putHeader("content-type", "text/plain")
                    .end("Hello from Vert.x HTTP server");
        }); */
        server.requestHandler(new HttpServerHandler());

        // 启动HTTP服务器并监听指定端口
        server.listen(port,result -> {
           if (result.succeeded()) {
               System.out.println("Server is now listening on port:" + port);
           } else {
               System.out.println("Failed to start server!" + result.cause());
           }
        });
    }
}
