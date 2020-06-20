package com.github.byakkili.bim.protocol.json;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.github.byakkili.bim.core.BimSession;
import com.github.byakkili.bim.core.interceptor.CmdInterceptor;
import com.github.byakkili.bim.core.util.JsonUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Guannian Li
 */
@Slf4j
@Getter
public class TestJsonInterceptor implements CmdInterceptor {
    static final Integer NO_ALLOW_CMD = 100;
    static final TestJsonMsg RESP_MSG = new TestJsonMsg(101, 403L, "Forbidden");

    private CountDownLatch countDownLatch = new CountDownLatch(3);
    private AtomicInteger count = new AtomicInteger(0);

    @Override
    public boolean preHandle(int cmd, BimSession session) {
        log.info("Session({}) preHandle, cmd: {}", session.getId(), cmd);
        countDownLatch.countDown();
        count.incrementAndGet();
        if (ObjectUtil.equal(NO_ALLOW_CMD, cmd)) {
            session.writeAndFlush(RESP_MSG);
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(int cmd, Object msg, BimSession session) {
        log.info("Session({}) postHandle, cmd: {}, respMsg: {}", session.getId(), cmd, JsonUtils.stringify(msg));
        countDownLatch.countDown();
        count.incrementAndGet();
    }

    @Override
    public void afterCompletion(int cmd, BimSession session, Exception e) {
        log.info("Session({}) afterCompletion, exception: {}", session.getId(), StrUtil.toString(e));
        countDownLatch.countDown();
        count.incrementAndGet();
    }
}