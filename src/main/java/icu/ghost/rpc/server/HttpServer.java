package icu.ghost.rpc.server;

/**
 * @author Ghost
 * @version 1.0
 * @className HttpServer
 * @date 2024/07/28 16:17
 * @since 1.0
 */
public interface HttpServer {
    /**
     * 启动服务器
     * @param port 端口
     */
    void doStart(int port);
}
