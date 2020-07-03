package com.github.byakkili.bim.core.server.json;

import com.github.byakkili.bim.core.BimSession;
import com.github.byakkili.bim.core.listener.SessionListener;
import com.github.byakkili.bim.core.protocol.CommandFrame;
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
public class TestJsonListener implements SessionListener {
    private CountDownLatch countDownLatch = new CountDownLatch(4);
    private AtomicInteger count = new AtomicInteger(0);

    @Override
    public void onRead(CommandFrame frame, BimSession session) {
        log.info("Session({}) read: {}", session.getId(), JsonUtils.stringify(frame.getMsg()));
        countDownLatch.countDown();
        count.incrementAndGet();
    }

    @Override
    public void onWrite(CommandFrame frame, BimSession session) {
        log.info("Session({}) write: {}", session.getId(), JsonUtils.stringify(frame.getMsg()));
        countDownLatch.countDown();
        count.incrementAndGet();
    }

    @Override
    public void onAfterCreated(BimSession session) {
        log.info("Session({}) created.", session.getId());
        countDownLatch.countDown();
        count.incrementAndGet();
    }

    @Override
    public void onBeforeDestroy(BimSession session) {
        log.info("Session({}) destroy.", session.getId());
        countDownLatch.countDown();
        count.incrementAndGet();
    }

    @Override
    public void onReaderIdle(BimSession session) {
        log.info("Session({}) reader idle.", session.getId());
    }

    @Override
    public void onWriterIdle(BimSession session) {
        log.info("Session({}) writer idle.", session.getId());
    }
}