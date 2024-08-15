package icu.ghost.rpc.fault.tolerant;

import icu.ghost.rpc.model.RpcResponse;

import java.util.Map;

/**
 * @author Ghost
 * @version 1.0
 * @className FailFastTolerantStrategy
 * @date 2024/08/13 12:06
 * @since 1.0
 */
public class FailFastTolerantStrategy implements TolerantStrategy{
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        throw new RuntimeException("服务错误", e);
    }
}
