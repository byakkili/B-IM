package com.github.byakkili.bim.core.protocol.protobuf.base;

import cn.hutool.core.util.StrUtil;
import com.github.byakkili.bim.core.cmd.BaseCmdHandler;
import com.github.byakkili.bim.core.protocol.protobuf.ProtobufFrame;
import com.google.protobuf.Message;

/**
 * @author Guannian Li
 */
public abstract class BaseProtobufCmdHandler<REQUEST extends Message> extends BaseCmdHandler<REQUEST, ProtobufFrame> {
    @Override
    public String toString() {
        return StrUtil.format("{}(cmd:{}, protocol:protobuf, reqClass:{})", this.getClass().getName(), cmd(), reqMsgClass().getName());
    }
}
