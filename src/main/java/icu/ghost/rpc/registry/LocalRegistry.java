package icu.ghost.rpc.registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ghost
 * @version 1.0
 * @className LocalRegistry
 * @date 2024/07/28 17:44
 * @since 1.0
 */
// 本地注册信息
public class LocalRegistry {

    private static final Map<String, Class<?>> map = new ConcurrentHashMap<>();

    /**
     * 注册服务
     *
     * @param serviceName 服务名
     * @param implClass   服务实现类
     */
    public static void register(String serviceName, Class<?> implClass) {
        map.put(serviceName, implClass);
    }

    /**
     * 获取服务
     * @param serviceName 服务名
     * @return 服务实现类
     */
    public static Class<?> get(String serviceName) {
        return map.get(serviceName);
    }

    /**
     * 移除服务
     * @param serviceName 服务名
     */
    public static void remove(String serviceName) {
        map.remove(serviceName);
    }
}
