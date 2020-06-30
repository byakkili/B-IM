package com.github.byakkili.bim.core.server.protobuf;

import com.github.byakkili.bim.core.BimSession;
import com.github.byakkili.bim.core.protocol.CmdMsgFrame;
import com.github.byakkili.bim.core.protocol.impl.protobuf.BaseProtobufCmdHandler;
import com.github.byakkili.bim.protobuf.Packet.Chat;
import com.github.byakkili.bim.protobuf.Packet.Command;
import com.google.protobuf.Message;

/**
 * @author Guannian Li
 */
public class TestProtobufCmdHandler extends BaseProtobufCmdHandler<Chat> {
    @Override
    public int cmd() {
        return Command.CHAT_VALUE;
    }

    @Override
    public CmdMsgFrame<Message> handle(Chat chat, BimSession session) {
        Chat respChat = Chat.newBuilder()
                .setCmd(Command.CHAT_ACK)
                .setSeq(chat.getSeq())
                .setTo(chat.getTo())
                .setContent(chat.getContent() + ": 已收到")
                .setChatType(chat.getChatType())
                .setMsgType(chat.getMsgType())
                .build();
        return new CmdMsgFrame<>(respChat.getCmdValue(), respChat);
    }
}