package com.github.byakkili.bim.core.listener;

import com.github.byakkili.bim.core.BimSession;

/**
 * @author Guannian Li
 */
public interface ISessionListener {
    /**
     * 会话创建之后的通知回调
     *
     * @param session 会话
     */
    default void onAfterCreated(BimSession session) {
        // ignore
    }

    /**
     * 会话销毁之前的通知回调
     *
     * @param session 会话
     */
    default void onBeforeDestroy(BimSession session) {
        // ignore
    }

    /**
     * 会话读超时的通知回调
     *
     * @param session 会话
     */
    default void onReaderIdle(BimSession session) {
        // ignore
    }

    /**
     * 会话写超时的通知回调
     *
     * @param session 会话
     */
    default void onWriterIdle(BimSession session) {
        // ignore
    }
}
