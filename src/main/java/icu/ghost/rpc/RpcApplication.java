package icu.ghost.rpc;

import icu.ghost.rpc.config.RegistryConfig;
import icu.ghost.rpc.config.RpcConfig;
import icu.ghost.rpc.constant.RpcConstant;
import icu.ghost.rpc.registry.Registry;
import icu.ghost.rpc.registry.RegistryFactory;
import icu.ghost.rpc.utis.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Ghost
 * @version 1.0
 * @className RpcApplication
 * @date 2024/07/29 22:08
 * @since 1.0
 */
@Slf4j
public class RpcApplication {
    /**
     * volatile修饰，禁止重排序，防止初始化实例的时候，拿到空对象
     */
    private static volatile RpcConfig rpcConfig;

    /**
     * rpc框架配置初始化
     */
    public static void init() {
        RpcConfig newRpcConfig = null;
        // 加载rpc配置
        try {
            newRpcConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        } catch (Exception e) {
            // 加载失败，使用默认配置
            rpcConfig = new RpcConfig();
        }
        init(newRpcConfig);
    }

    /**
     * 配置初始化，支持自定义配置
     * @param newRpcConfig 自定义配置
     */
    public static void init(RpcConfig newRpcConfig) {
        rpcConfig = newRpcConfig;
        log.info("rpc init,config = {}",rpcConfig.toString());

        // 注册中心初始化
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        registry.init(registryConfig);
        log.info("registry init,config = {}",registryConfig);

        // 创建并注册Shutdown Hook,JVM退出时执行操作
        Runtime.getRuntime().addShutdownHook(new Thread(registry::destroy));
    }

    public static RpcConfig getRpcConfig() {
        // rpcConfig为空，进入抢锁阶段
        if (rpcConfig == null) {
            synchronized (RpcConfig.class) {
                if (rpcConfig == null) {
                    init();
                }
            }
        }
        return rpcConfig;
    }
}
