package com.github.byakkili.bim.demo;

import com.github.byakkili.bim.core.BimSession;
import com.github.byakkili.bim.core.interceptor.CommandInterceptor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Guannian Li
 */
@Slf4j
public class TestInterceptor implements CommandInterceptor {
    @Override
    public boolean preHandle(int command, BimSession session) {
        log.info("会话: {}, preHandle", session.getId());
        return true;
    }

    @Override
    public void postHandle(int command, Object msg, BimSession session) {
        log.info("会话: {}, postHandle", session.getId());
    }

    @Override
    public void afterCompletion(int command, BimSession session, Exception e) {
        log.info("会话: {}, afterCompletion", session.getId());
    }
}
