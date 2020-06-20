package com.github.byakkili.bim.demo.protobuf;

import com.github.byakkili.bim.core.BimSession;
import com.github.byakkili.bim.core.interceptor.CmdInterceptor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Guannian Li
 */
@Slf4j
public class TestInterceptor implements CmdInterceptor {
    @Override
    public boolean preHandle(int cmd, BimSession session) {
        log.info("会话: {}, preHandle", session.getId());
        return true;
    }

    @Override
    public void postHandle(int cmd, Object msg, BimSession session) {
        log.info("会话: {}, postHandle", session.getId());
    }

    @Override
    public void afterCompletion(int cmd, BimSession session, Exception e) {
        log.info("会话: {}, afterCompletion", session.getId());
    }
}
