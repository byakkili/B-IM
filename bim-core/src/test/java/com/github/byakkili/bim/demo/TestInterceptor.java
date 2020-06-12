package com.github.byakkili.bim.demo;

import com.github.byakkili.bim.core.BimSession;
import com.github.byakkili.bim.core.interceptor.CmdInterceptor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Guannian Li
 */
@Slf4j
public class TestInterceptor implements CmdInterceptor {
    @Override
    public boolean preHandle(Integer cmd, Object reqMsg, BimSession session) {
        log.info("会话: {}, preHandle", session.getChannel().id().asShortText());
        return true;
    }

    @Override
    public void postHandle(Integer cmd, BimSession session, Object respMsg) {
        log.info("会话: {}, postHandle", session.getChannel().id().asShortText());
    }

    @Override
    public void afterCompletion(Integer cmd, BimSession session, Exception e) {
        log.info("会话: {}, afterCompletion", session.getChannel().id().asShortText());
    }
}
