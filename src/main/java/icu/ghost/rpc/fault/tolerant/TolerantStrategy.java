package icu.ghost.rpc.fault.tolerant;

import icu.ghost.rpc.model.RpcRequest;
import icu.ghost.rpc.model.RpcResponse;

import java.util.Map;

/**
 * @author Ghost
 * @version 1.0
 * @className TolerantStrategy
 * @date 2024/08/13 12:03
 * @since 1.0
 */
public interface TolerantStrategy {
    /**
     * 容错
     * @param context
     * @param e
     * @return
     */
    RpcResponse doTolerant(Map<String, Object> context, Exception e);
}
