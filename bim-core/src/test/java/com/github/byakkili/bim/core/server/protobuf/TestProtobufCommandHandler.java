package com.github.byakkili.bim.core.server.protobuf;

import com.github.byakkili.bim.core.BimSession;
import com.github.byakkili.bim.core.protocol.CommandFrame;
import com.github.byakkili.bim.core.protocol.impl.protobuf.BaseProtobufCommandHandler;
import com.github.byakkili.bim.protobuf.Packet.Chat;
import com.github.byakkili.bim.protobuf.Packet.Command;
import com.google.protobuf.Message;

/**
 * @author Guannian Li
 */
public class TestProtobufCommandHandler extends BaseProtobufCommandHandler<Chat> {
    @Override
    public int command() {
        return Command.CHAT_VALUE;
    }

    @Override
    public CommandFrame<Message> handle(Chat chat, BimSession session) {
        Chat respChat = Chat.newBuilder()
                .setCommand(Command.CHAT_ACK)
                .setSeq(chat.getSeq())
                .setTo(chat.getTo())
                .setContent(chat.getContent() + ": 已收到")
                .setChatType(chat.getChatType())
                .setMsgType(chat.getMsgType())
                .build();
        return new CommandFrame<>(respChat.getCommandValue(), respChat);
    }
}