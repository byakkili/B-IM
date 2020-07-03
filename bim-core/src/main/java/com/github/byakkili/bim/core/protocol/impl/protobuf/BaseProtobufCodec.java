package com.github.byakkili.bim.core.protocol.impl.protobuf;

import com.github.byakkili.bim.core.BimContext;
import com.github.byakkili.bim.core.BimSession;
import com.github.byakkili.bim.core.command.CommandHandler;
import com.github.byakkili.bim.core.protocol.CommandFrame;
import com.github.byakkili.bim.core.util.CommandHandlerUtils;
import com.github.byakkili.bim.core.util.ProtobufUtils;
import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.Map;

/**
 * @author Guannian Li
 */
public abstract class BaseProtobufCodec<T> extends MessageToMessageCodec<T, CommandFrame<Message>> {

    protected CommandFrame<Message> decodeToFrame(BimSession session, ByteBuf byteBuf) {
        BimContext context = session.getContext();
        Map<Integer, CommandHandler> commandHandlers = context.getCommandHandlers();

        int command = byteBuf.readInt();
        CommandHandler commandHandler = commandHandlers.get(command);
        if (commandHandler == null) {
            return new CommandFrame<>(command, null);
        }
        byte[] bytes = ByteBufUtil.getBytes(byteBuf);
        Class<Message> msgClass = CommandHandlerUtils.getMsgClass(commandHandler);
        Message message = ProtobufUtils.deserialize(bytes, msgClass);
        return new CommandFrame<>(command, message);
    }
}
