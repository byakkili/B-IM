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
     * @param reqFrame 请求帧
     * @param session  会话
     * @return 是否往下执行
     */
    default boolean preHandle(CmdMsgFrame reqFrame, BimSession session) {
        return true;
    }

    /**
     * 后置处理 (如果CmdHandler出现异常, 不会执行)
     *
     * @param respFrame 响应帧
     * @param session   会话
     */
    default void postHandle(CmdMsgFrame respFrame, BimSession session) {
        // ignore
    }

    /**
     * 完成后处理
     *
     * @param reqFrame 请求帧
     * @param session  会话
     * @param e        异常
     */
    default void afterCompletion(CmdMsgFrame reqFrame, BimSession session, Exception e) {
        // ignore
    }
}
