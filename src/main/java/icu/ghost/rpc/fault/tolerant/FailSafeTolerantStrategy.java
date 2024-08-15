package icu.ghost.rpc.fault.tolerant;

import icu.ghost.rpc.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author Ghost
 * @version 1.0
 * @className FailSafeTolerantStrategy
 * @date 2024/08/13 12:07
 * @since 1.0
 */
@Slf4j
public class FailSafeTolerantStrategy implements TolerantStrategy{
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        log.info("静默处理异常", e);
        return new RpcResponse();
    }
}
