package com.github.byakkili.bim.core.command;


import com.github.byakkili.bim.core.BimSession;

/**
 * @author Guannian Li
 */
public abstract class CommandHandler<I, O> {
    /**
     * 指令
     *
     * @return 指令
     */
    public abstract int command();

    /**
     * 消息处理
     *
     * @param msg     消息
     * @param session 会话
     * @return 响应消息, 可为null
     */
    public abstract O handle(I msg, BimSession session);
}
