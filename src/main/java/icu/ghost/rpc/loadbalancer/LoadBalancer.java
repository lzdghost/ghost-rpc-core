package icu.ghost.rpc.loadbalancer;

import icu.ghost.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;

/**
 * @author Ghost
 * @version 1.0
 * @className LoadBalancer
 * @date 2024/08/12 21:04
 * @since 1.0
 */
public interface LoadBalancer {
    /**
     * 选择服务调用
     * @param requestParam 请求参数
     * @param serviceMetaInfoList 可用服务列表
     * @return
     */
    ServiceMetaInfo select(Map<String, Object> requestParam, List<ServiceMetaInfo> serviceMetaInfoList);
}
