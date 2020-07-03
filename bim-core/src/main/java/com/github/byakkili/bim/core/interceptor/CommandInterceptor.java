package com.github.byakkili.bim.core.interceptor;

import com.github.byakkili.bim.core.BimSession;

/**
 * 指令拦截器
 *
 * @author Guannian Li
 */
public interface CommandInterceptor {
    /**
     * 前置处理
     *
     * @param command 指令
     * @param session 会话
     * @return 是否往下执行
     */
    default boolean preHandle(int command, BimSession session) {
        return true;
    }

    /**
     * 后置处理 (如果CommandHandler出现异常, 不会执行)
     *
     * @param command 指令
     * @param msg     消息
     * @param session 会话
     */
    default void postHandle(int command, Object msg, BimSession session) {
        // ignore
    }

    /**
     * 完成后处理
     *
     * @param command 指令
     * @param session 会话
     * @param e       异常
     */
    default void afterCompletion(int command, BimSession session, Exception e) {
        // ignore
    }
}
