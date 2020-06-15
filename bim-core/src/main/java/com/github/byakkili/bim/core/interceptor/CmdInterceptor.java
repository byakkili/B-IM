package com.github.byakkili.bim.core.interceptor;

import com.github.byakkili.bim.core.BimSession;

/**
 * CMD拦截器
 *
 * @author Guannian Li
 */
public interface CmdInterceptor {
    /**
     * 前置处理
     *
     * @param cmd     CMD
     * @param session 会话
     * @return 是否往下执行
     */
    default boolean preHandle(int cmd, BimSession session) {
        return true;
    }

    /**
     * 后置处理 (如果CmdHandler出现异常, 不会执行)
     *
     * @param cmd     CMD
     * @param msg     消息
     * @param session 会话
     */
    default void postHandle(int cmd, Object msg, BimSession session) {
        // ignore
    }

    /**
     * 完成后处理
     *
     * @param cmd     CMD
     * @param session 会话
     * @param e       异常
     */
    default void afterCompletion(int cmd, BimSession session, Exception e) {
        // ignore
    }
}
