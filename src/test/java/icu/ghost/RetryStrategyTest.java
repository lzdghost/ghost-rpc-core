package icu.ghost;

import icu.ghost.rpc.fault.retry.FixedIntervalRetryStrategy;
import icu.ghost.rpc.fault.retry.NoRetryStrategy;
import icu.ghost.rpc.fault.retry.RetryStrategy;
import icu.ghost.rpc.model.RpcResponse;
import org.junit.Test;

import java.util.concurrent.Callable;

/**
 * 重试策略测试
 */
public class RetryStrategyTest {

    RetryStrategy retryStrategy1 = new NoRetryStrategy();
    RetryStrategy retryStrategy2 = new FixedIntervalRetryStrategy();
    @Test
    public void doRetry1() {
        try {
            RpcResponse rpcResponse = retryStrategy1.doRetry(new Callable<RpcResponse>() {
                @Override
                public RpcResponse call() throws Exception {
                    System.out.println("测试重试");
                    throw new RuntimeException("模拟重试失败");
                }
            });
            /*RpcResponse rpcResponse = retryStrategy1.doRetry(() -> {
                System.out.println("测试重试");
                throw new RuntimeException("模拟重试失败");
            });*/
            System.out.println(rpcResponse);
        } catch (Exception e) {
            System.out.println("重试多次失败");
            e.printStackTrace();
        }
    }

    @Test
    public void doRetry2() {
        try {
            RpcResponse rpcResponse = retryStrategy2.doRetry(() -> {
                System.out.println("测试重试");
                throw new RuntimeException("模拟重试失败");
            });
            System.out.println(rpcResponse);
        } catch (Exception e) {
            System.out.println("重试多次失败");
            e.printStackTrace();
        }
    }
}
