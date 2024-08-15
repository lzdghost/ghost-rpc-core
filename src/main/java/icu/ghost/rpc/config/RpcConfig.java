package icu.ghost.rpc.config;

import icu.ghost.rpc.fault.retry.RetryKeys;
import icu.ghost.rpc.fault.tolerant.TolerantKeys;
import icu.ghost.rpc.loadbalancer.LoadBalancerKeys;
import icu.ghost.rpc.serializer.SerializerKeys;
import lombok.Data;
import lombok.ToString;

/**
 * @author Ghost
 * @version 1.0
 * @className RpcConfig
 * @date 2024/07/29 21:20
 * @since 1.0
 */
@Data
@ToString
public class RpcConfig {

    /**
     * 名称
     */
    private String name = "ghost-rpc";

    /**
     * 版本号
     */
    private String version = "1.0";

    /**
     * 服务器主机号
     */
    private String serverHost = "localhost";

    /**
     * 服务器端口
     */
    private int serverPort = 8080;

    /**
     * 是否开启mock
     */
    private boolean mock = false;

    /**
     * 默认序列化器
     */
    private String serializerKey = SerializerKeys.JDK;

    /**
     * 注册中心配置
     */
    private RegistryConfig registryConfig = new RegistryConfig();

    /**
     * 负载均衡器
     */
    private String loadBalancer = LoadBalancerKeys.ROUND;

    /**
     * 重试策略
     */
    private String retryStrategy = RetryKeys.NO;

    /**
     * 容错机制
     */
    private String tolerantStrategy = TolerantKeys.FAIL_FAST;
}
