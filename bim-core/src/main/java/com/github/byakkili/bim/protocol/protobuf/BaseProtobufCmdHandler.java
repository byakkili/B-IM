package com.github.byakkili.bim.protocol.protobuf;

import cn.hutool.core.util.StrUtil;
import com.github.byakkili.bim.core.cmd.CmdHandler;
import com.github.byakkili.bim.core.protocol.CmdMsgFrame;
import com.google.protobuf.Message;

/**
 * @author Guannian Li
 */
public abstract class BaseProtobufCmdHandler<T extends Message> extends CmdHandler<T, CmdMsgFrame<Message>> {
    @Override
    public String toString() {
        return StrUtil.format("{}(cmd: {}, protocol: \"protobuf\")", this.getClass().getName(), cmd());
    }
}
