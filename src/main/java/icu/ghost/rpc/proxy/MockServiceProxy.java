package icu.ghost.rpc.proxy;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author Ghost
 * @version 1.0
 * @className ServiceProxy
 * @date 2024/07/28 20:47
 * @since 1.0
 */
@Slf4j
public class MockServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        // 方法的返回值类型
        Class<?> returnType = method.getReturnType();
        log.info("mock invoke {}",method.getName());

        // 返回指定类型的默认对象
        return getDefaultObject(returnType);
    }

    private Object getDefaultObject(Class<?> type) {
        // 是基本类型
        if (type.isPrimitive()) {
            if(type == boolean.class) {
                return false;
            } else if (type == long.class) {
                return 0L;
            } else if(type == int.class ){
                return 0;
            }
        }
        // 对象类型
        return null;
    }
}
