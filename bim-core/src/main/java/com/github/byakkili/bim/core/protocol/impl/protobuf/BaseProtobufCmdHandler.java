package com.github.byakkili.bim.core.protocol.impl.protobuf;

import cn.hutool.core.util.StrUtil;
import com.github.byakkili.bim.core.cmd.BaseCmdHandler;
import com.google.protobuf.Message;

/**
 * @author Guannian Li
 */
public abstract class BaseProtobufCmdHandler<T extends Message> extends BaseCmdHandler<T, Message> {
    @Override
    public String toString() {
        return StrUtil.format("{}(cmd:{}, protocol:protobuf, reqClass:{})", this.getClass().getName(), cmd(), reqMsgClass().getName());
    }
}
