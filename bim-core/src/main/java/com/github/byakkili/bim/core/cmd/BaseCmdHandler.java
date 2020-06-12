package com.github.byakkili.bim.core.cmd;

import com.github.byakkili.bim.core.BimSession;
import com.github.byakkili.bim.core.protocol.CmdMsgFrame;
import io.netty.util.internal.TypeParameterMatcher;

/**
 * @author Guannian Li
 */
public abstract class BaseCmdHandler<REQUEST, RESPONSE> implements ICmdHandler<REQUEST, RESPONSE> {

    private final TypeParameterMatcher matcher = TypeParameterMatcher.find(this, BaseCmdHandler.class, "REQUEST");

    @Override
    public CmdMsgFrame<RESPONSE> msgHandle(CmdMsgFrame frame, BimSession session) {
        Object msg = frame.getMsg();
        if (matcher.match(msg)) {
            @SuppressWarnings("unchecked")
            REQUEST requestMsg = (REQUEST) msg;
            return handle(requestMsg, session);
        }
        return null;
    }

    /**
     * 处理请求
     *
     * @param reqMsg  请求内容
     * @param session 会话
     * @return 响应内容(null : 不响应)
     */
    protected abstract CmdMsgFrame<RESPONSE> handle(REQUEST reqMsg, BimSession session);
}
