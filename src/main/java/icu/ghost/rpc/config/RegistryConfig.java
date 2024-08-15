package icu.ghost.rpc.config;

import lombok.Data;

/**
 * @author Ghost
 * @version 1.0
 * @className RegistryConfig
 * @date 2024/08/01 22:53
 * @since 1.0
 */
@Data
public class RegistryConfig {
    /**
     * 注册中心类别
     */
    private String registry = "etcd";

    /**
     * 注册中心地址
     */
    private String address = "http://localhost:2380";

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 超时时间（毫秒）
     */
    private Long timeOut = 10000L;
}
