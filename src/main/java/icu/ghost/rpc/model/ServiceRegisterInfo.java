package icu.ghost.rpc.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Ghost
 * @version 1.0
 * @className ServiceRegisterInfo
 * @date 2024/08/13 14:18
 * @since 1.0
 */
@Data
@AllArgsConstructor
public class ServiceRegisterInfo<T> {
    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 实现类
     */
    private Class<? extends T> implClass;
}
