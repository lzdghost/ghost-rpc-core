package icu.ghost.rpc.fault.retry;

import icu.ghost.rpc.model.RpcRequest;
import icu.ghost.rpc.model.RpcResponse;

import java.util.concurrent.Callable;

/**
 * @author Ghost
 * @version 1.0
 * @className RetryStrategy
 * @date 2024/08/13 10:37
 * @since 1.0
 */
public interface RetryStrategy {
    /**
     * 重试
     * @param callable
     * @return
     * @throws Exception
     */
    RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception;
}
