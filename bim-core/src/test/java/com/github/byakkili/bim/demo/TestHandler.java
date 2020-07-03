package com.github.byakkili.bim.demo;

import com.github.byakkili.bim.core.BimSession;
import com.github.byakkili.bim.core.protocol.CommandFrame;
import com.github.byakkili.bim.core.protocol.impl.protobuf.BaseProtobufCommandHandler;
import com.github.byakkili.bim.protobuf.Packet.Chat;
import com.github.byakkili.bim.protobuf.Packet.Command;
import com.google.protobuf.Message;

/**
 * @author Guannian Li
 */
public class TestHandler extends BaseProtobufCommandHandler<Chat> {
    @Override
    public CommandFrame<Message> handle(Chat msg, BimSession session) {
        Chat respMsg = Chat.newBuilder()
                .setCommand(Command.CHAT_ACK)
                .setSeq(msg.getSeq())
                .setTo(msg.getTo())
                .setContent(msg.getContent() + ": 已收到")
                .setChatType(msg.getChatType())
                .setMsgType(msg.getMsgType())
                .build();
        return new CommandFrame<>(Command.CHAT_ACK_VALUE, respMsg);
    }

    @Override
    public int command() {
        return 0;
    }
}
