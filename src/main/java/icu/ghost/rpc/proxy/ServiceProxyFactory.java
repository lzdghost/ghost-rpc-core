package icu.ghost.rpc.proxy;

import icu.ghost.rpc.RpcApplication;

import java.lang.reflect.Proxy;

/**
 * @author Ghost
 * @version 1.0
 * @className ServiceProxyFactory
 * @date 2024/07/28 20:58
 * @since 1.0
 */
public class ServiceProxyFactory {
    /**
     * 根据服务类获取代理对象
     * @param serviceClass 服务类
     * @return 代理对象
     * @param <T> 泛型
     */
    public static <T> T getProxy(Class<T> serviceClass) {
        // 是否开启mock
        if (RpcApplication.getRpcConfig().isMock()) {
            return getMockProxy(serviceClass);
        }

        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new ServiceProxy());
    }


    /**
     * 根据服务类获取Mock代理对象
     * @param serviceClass 服务类
     * @return 代理对象
     * @param <T> 泛型
     */
    public static <T> T getMockProxy(Class<T> serviceClass) {
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new MockServiceProxy());
    }
}
