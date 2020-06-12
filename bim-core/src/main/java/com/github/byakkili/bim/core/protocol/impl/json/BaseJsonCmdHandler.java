package com.github.byakkili.bim.core.protocol.impl.json;

import cn.hutool.core.util.StrUtil;
import com.github.byakkili.bim.core.BimSession;
import com.github.byakkili.bim.core.cmd.BaseCmdHandler;
import com.github.byakkili.bim.core.protocol.CmdMsgFrame;

/**
 * @author Guannian Li
 */
public abstract class BaseJsonCmdHandler<T extends JsonMsg> extends BaseCmdHandler<T, JsonMsg> {
    @Override
    public String toString() {
        return StrUtil.format("{}(cmd:{}, protocol:json, reqClass:{})", this.getClass().getName(), cmd(), reqMsgClass().getName());
    }

    @Override
    protected CmdMsgFrame<JsonMsg> handle(T reqMsg, BimSession session) {
        JsonMsg jsonMsg = process(reqMsg, session);
        return new CmdMsgFrame<>(jsonMsg.getCmd(), jsonMsg);
    }

    protected abstract JsonMsg process(T reqMsg, BimSession session);
}
