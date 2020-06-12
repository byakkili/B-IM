package com.github.byakkili.bim.core.cmd;

import com.github.byakkili.bim.core.BimSession;
import com.github.byakkili.bim.core.protocol.CmdMsgFrame;

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
     * @param frame   消息帧
     * @param session 会话
     * @return 响应消息(null : 不响应)
     */
    CmdMsgFrame<RESPONSE> msgHandle(CmdMsgFrame frame, BimSession session);
}
