package icu.ghost.rpc.registry;

import icu.ghost.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ghost
 * @version 1.0
 * @className RegistryServiceCache
 * @date 2024/08/11 20:15
 * @since 1.0
 */
public class RegistryServiceMultiCache {
    /**
     * 服务缓存
     */
    Map<String,List<ServiceMetaInfo>> serviceCache = new ConcurrentHashMap<>();

    /**
     * 写缓存
     * @param serviceKey
     * @param newServiceCache
     */
    void writeCache(String serviceKey, List<ServiceMetaInfo> newServiceCache) {
        this.serviceCache.put(serviceKey, newServiceCache);
    }

    /**
     * 读缓存
     * @param serviceKey
     * @return
     */
    List<ServiceMetaInfo> readCache(String serviceKey) {
        return this.serviceCache.get(serviceKey);
    }

    /**
     * 清空缓存
     * @param serviceKey
     */
    void clearCache(String serviceKey) {
        this.serviceCache.remove(serviceKey);
    }
}
