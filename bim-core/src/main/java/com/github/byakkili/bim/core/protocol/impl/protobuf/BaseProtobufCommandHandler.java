package com.github.byakkili.bim.core.protocol.impl.protobuf;

import cn.hutool.core.util.StrUtil;
import com.github.byakkili.bim.core.command.CommandHandler;
import com.github.byakkili.bim.core.protocol.CommandFrame;
import com.google.protobuf.Message;

/**
 * @author Guannian Li
 */
public abstract class BaseProtobufCommandHandler<T extends Message> extends CommandHandler<T, CommandFrame<Message>> {
    @Override
    public String toString() {
        return StrUtil.format("{}(command: {}, protocol: \"protobuf\")", this.getClass().getName(), command());
    }
}
