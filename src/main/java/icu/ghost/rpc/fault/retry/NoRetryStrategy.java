package icu.ghost.rpc.fault.retry;

import icu.ghost.rpc.model.RpcResponse;

import java.util.concurrent.Callable;

/**
 * @author Ghost
 * @version 1.0
 * @className NoRetryStrategy
 * @date 2024/08/13 10:52
 * @since 1.0
 */
/**
 * 不重试 - 重试策略
 */
public class NoRetryStrategy implements RetryStrategy {
    @Override
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception {
        return callable.call();
    }
}
