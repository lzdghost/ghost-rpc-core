package icu.ghost.rpc.bootstrap;

import icu.ghost.rpc.RpcApplication;
import icu.ghost.rpc.config.RegistryConfig;
import icu.ghost.rpc.config.RpcConfig;
import icu.ghost.rpc.model.ServiceMetaInfo;
import icu.ghost.rpc.model.ServiceRegisterInfo;
import icu.ghost.rpc.registry.LocalRegistry;
import icu.ghost.rpc.registry.Registry;
import icu.ghost.rpc.registry.RegistryFactory;
import icu.ghost.rpc.server.tcp.VertxTcpServer;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author Ghost
 * @version 1.0
 * @className ProviderBootstrap
 * @date 2024/08/13 14:20
 * @since 1.0
 */
public class ConsumerBootstrap {
    public static void init() {

        // RPC框架配置初始化
        RpcApplication.init();
    }
}
