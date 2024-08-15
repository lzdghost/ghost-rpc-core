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
public class ProviderBootstrap {
    public static void init(List<ServiceRegisterInfo<?>> serviceRegisterInfoList) {

        // 配置初始化
        RpcApplication.init();
        // 全局配置
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();

        for (ServiceRegisterInfo<?> serviceRegisterInfo: serviceRegisterInfoList) {
            String serviceName = serviceRegisterInfo.getServiceName();
            Class<?> serviceImplClass = serviceRegisterInfo.getImplClass();
            // 本地注册
            LocalRegistry.register(serviceName,serviceImplClass);

            // 注册服务到注册中心
            RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
            Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());

            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
            serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
            serviceMetaInfo.setServiceName(serviceName);
            try {
                registry.registry(serviceMetaInfo);
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }

            // 启动TCP服务
            VertxTcpServer server = new VertxTcpServer();
            server.doStart(RpcApplication.getRpcConfig().getServerPort());
        }

    }
}
