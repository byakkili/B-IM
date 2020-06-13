package com.github.byakkili.bim.core.protocol.impl.protobuf;

import com.github.byakkili.bim.core.BimContext;
import com.github.byakkili.bim.core.BimSession;
import com.github.byakkili.bim.core.cmd.ICmdHandler;
import com.github.byakkili.bim.core.protocol.CmdMsgFrame;
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
        Map<Integer, ICmdHandler> cmdHandlers = context.getCmdHandlers();

        int cmd = byteBuf.readInt();
        ICmdHandler cmdHandler = cmdHandlers.get(cmd);
        if (cmdHandler == null) {
            return new CmdMsgFrame<>(cmd, null);
        }
        byte[] bytes = ByteBufUtil.getBytes(byteBuf);
        @SuppressWarnings("unchecked")
        Class<Message> reqMsgClass = cmdHandler.reqMsgClass();
        Message message = ProtobufUtils.deserialize(bytes, reqMsgClass);
        return new CmdMsgFrame<>(cmd, message);
    }
}
