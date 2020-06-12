package com.github.byakkili.bim.core.interceptor;

import com.github.byakkili.bim.core.BimSession;
import com.github.byakkili.bim.core.protocol.CmdMsgFrame;

/**
 * CMD拦截器
 *
 * @author Guannian Li
 */
public interface CmdInterceptor {
    /**
     * 前置处理
     *
     * @param cmd      CMD
     * @param reqFrame 请求消息
     * @param session  会话
     * @return 是否往下执行
     */
    default boolean preHandle(Integer cmd, CmdMsgFrame reqFrame, BimSession session) {
        return true;
    }

    /**
     * 后置处理 (如果CmdHandler出现异常, 不会执行)
     *
     * @param cmd       CMD
     * @param session   会话
     * @param respFrame 响应消息
     */
    default void postHandle(Integer cmd, BimSession session, CmdMsgFrame respFrame) {
        // ignore
    }

    /**
     * 完成后处理
     *
     * @param cmd     CMD
     * @param session 会话
     * @param e       异常
     */
    default void afterCompletion(Integer cmd, BimSession session, Exception e) {
        // ignore
    }
}
