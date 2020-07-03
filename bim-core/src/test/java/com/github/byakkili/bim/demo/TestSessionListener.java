package com.github.byakkili.bim.demo;

import com.github.byakkili.bim.core.BimSession;
import com.github.byakkili.bim.core.listener.SessionListener;
import com.github.byakkili.bim.core.protocol.CommandFrame;
import com.google.protobuf.Message;
import com.googlecode.protobuf.format.JsonFormat;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Guannian Li
 */
@Slf4j
public class TestSessionListener implements SessionListener {
    private JsonFormat jsonFormat = new JsonFormat();

    @Override
    public void onAfterCreated(BimSession session) {
        log.info("会话: {}, 已创建", session.getId());
    }

    @Override
    public void onBeforeDestroy(BimSession session) {
        log.info("会话: {}, 准备销毁", session.getId());
    }

    @Override
    public void onReaderIdle(BimSession session) {
        log.info("会话: {}, 读超时, {}s", session.getId(), session.getContext().getReaderTimeout());
        // 关闭客户端
        // session.getChannel().close();
    }

    @Override
    public void onWriterIdle(BimSession session) {
        log.info("会话: {}, 写超时, {}s", session.getId(), session.getContext().getWriterTimeout());
    }

    @Override
    public void onRead(CommandFrame frame, BimSession session) {
        if (frame.getMsg() instanceof Message) {
            log.info("会话: {}, 请求 -> command: {}, data: {}", session.getId(), frame.getCommand(), jsonFormat.printToString((Message) frame.getMsg()));
        }
    }

    @Override
    public void onWrite(CommandFrame frame, BimSession session) {
        if (frame.getMsg() instanceof Message) {
            log.info("会话: {}, 响应 -> command: {}, data: {}", session.getId(), frame.getCommand(), jsonFormat.printToString((Message) frame.getMsg()));
        }
    }
}
