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
     * @param reqMsg  请求消息
     * @param session 会话
     * @return 是否往下执行
     */
    default boolean preHandle(Integer cmd, Object reqMsg, BimSession session) {
        return true;
    }

    /**
     * 后置处理 (如果CmdHandler出现异常, 不会执行)
     *
     * @param cmd     CMD
     * @param session 会话
     * @param respMsg 响应消息
     */
    default void postHandle(Integer cmd, BimSession session, Object respMsg) {
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
