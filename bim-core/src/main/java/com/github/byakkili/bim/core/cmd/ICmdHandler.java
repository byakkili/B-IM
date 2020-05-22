package com.github.byakkili.bim.core.cmd;

import com.github.byakkili.bim.core.BimSession;

/**
 * @author Guannian Li
 */
public interface ICmdHandler<REQUEST, RESPONSE> {
    /**
     * Command
     *
     * @return 整型cmd
     */
    int cmd();

    /**
     * 消息类型
     *
     * @return 类型
     */
    Class<REQUEST> reqMsgClass();

    /**
     * 消息处理
     *
     * @param msg     消息内容
     * @param session 会话
     * @return 响应消息(null : 不响应)
     */
    RESPONSE msgHandle(Object msg, BimSession session);
}
