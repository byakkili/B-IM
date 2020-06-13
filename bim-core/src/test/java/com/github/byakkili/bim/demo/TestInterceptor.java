package com.github.byakkili.bim.demo;

import com.github.byakkili.bim.core.BimSession;
import com.github.byakkili.bim.core.interceptor.CmdInterceptor;
import com.github.byakkili.bim.core.protocol.CmdMsgFrame;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Guannian Li
 */
@Slf4j
public class TestInterceptor implements CmdInterceptor {
    @Override
    public boolean preHandle(CmdMsgFrame reqFrame, BimSession session) {
        log.info("会话: {}, preHandle", session.getChannel().id().asShortText());
        return true;
    }

    @Override
    public void postHandle(CmdMsgFrame respFrame, BimSession session) {
        log.info("会话: {}, postHandle", session.getChannel().id().asShortText());
    }

    @Override
    public void afterCompletion(CmdMsgFrame reqFrame, BimSession session, Exception e) {
        log.info("会话: {}, afterCompletion", session.getChannel().id().asShortText());
    }
}
