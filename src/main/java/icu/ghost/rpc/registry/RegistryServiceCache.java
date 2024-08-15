package icu.ghost.rpc.registry;

import icu.ghost.rpc.model.ServiceMetaInfo;

import java.util.List;

/**
 * @author Ghost
 * @version 1.0
 * @className RegistryServiceCache
 * @date 2024/08/11 20:15
 * @since 1.0
 */
public class RegistryServiceCache {
    /**
     * 服务缓存
     */
    List<ServiceMetaInfo> serviceCache;

    /**
     * 写缓存
     * @param newServiceCache
     */
    void writeCache(List<ServiceMetaInfo> newServiceCache) {
        this.serviceCache = newServiceCache;
    }

    /**
     * 读缓存
     * @return
     */
    List<ServiceMetaInfo> readCache() {
        return this.serviceCache;
    }

    /**
     * 清空缓存
     */
    void clearCache() {
        this.serviceCache = null;
    }
}
