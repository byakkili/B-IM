package com.github.byakkili.bim.core.cmd;


import com.github.byakkili.bim.core.BimSession;

/**
 * @author Guannian Li
 */
public abstract class CmdHandler<I, O> {
    /**
     * Command
     *
     * @return int
     */
    public abstract int cmd();

    /**
     * 消息处理
     *
     * @param msg     消息
     * @param session 会话
     * @return RESPONSE
     */
    public abstract O handle(I msg, BimSession session);
}
