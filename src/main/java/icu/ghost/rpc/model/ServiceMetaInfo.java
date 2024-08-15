package icu.ghost.rpc.model;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

/**
 * @author Ghost
 * @version 1.0
 * @className ServiceMetaInfo
 * @date 2024/08/01 22:29
 * @since 1.0
 */
@Data
public class ServiceMetaInfo {
    /**
     * 服务名
     */
    private String serviceName;

    /**
     * 服务版本号
     */
    private String serviceVersion = "1.0";

    /**
     * 服务主机
     */
    private String serviceHost;

    /**
     * 服务端口
     */
    private Integer servicePort;

    /**
     * 获取服务键名
     * @return 服务键名
     */
    public String getServiceKey() {
        return String.format("%s:%s", serviceName, serviceVersion);
    }

    /**
     * 获取服务注册结点键名
     * @return 服务注册结点键名
     */
    public String getServiceNodeKey() {
        return String.format("%s:%s:%s", getServiceKey(),serviceHost, servicePort);
    }

    /**
     * 获取完整服务地址
     * @return 服务地址
     */
    public String getServiceAddress() {
        if (!StrUtil.contains(serviceHost, "http")) {
            return String.format("http://%s:%s", serviceHost, servicePort);
        }
        return String.format("%s:%s", serviceHost, servicePort);
    }

}
