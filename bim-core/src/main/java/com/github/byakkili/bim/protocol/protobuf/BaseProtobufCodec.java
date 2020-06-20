package com.github.byakkili.bim.protocol.protobuf;

import com.github.byakkili.bim.core.BimContext;
import com.github.byakkili.bim.core.BimSession;
import com.github.byakkili.bim.core.cmd.CmdHandler;
import com.github.byakkili.bim.core.protocol.CmdMsgFrame;
import com.github.byakkili.bim.core.util.CmdHandlerUtils;
import com.github.byakkili.bim.core.util.ProtobufUtils;
import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.Map;

/**
 * @author Guannian Li
 */
public abstract class BaseProtobufCodec<T> extends MessageToMessageCodec<T, CmdMsgFrame<Message>> {

    protected CmdMsgFrame<Message> decodeToFrame(BimSession session, ByteBuf byteBuf) {
        BimContext context = session.getContext();
        Map<Integer, CmdHandler> cmdHandlers = context.getCmdHandlers();

        int cmd = byteBuf.readInt();
        CmdHandler cmdHandler = cmdHandlers.get(cmd);
        if (cmdHandler == null) {
            return new CmdMsgFrame<>(cmd, null);
        }
        byte[] bytes = ByteBufUtil.getBytes(byteBuf);
        Class<Message> msgClass = CmdHandlerUtils.getMsgClass(cmdHandler);
        Message message = ProtobufUtils.deserialize(bytes, msgClass);
        return new CmdMsgFrame<>(cmd, message);
    }
}
