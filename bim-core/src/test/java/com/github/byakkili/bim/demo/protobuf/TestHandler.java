package com.github.byakkili.bim.demo.protobuf;

import com.github.byakkili.bim.core.BimSession;
import com.github.byakkili.bim.core.protocol.CmdMsgFrame;
import com.github.byakkili.bim.protocol.protobuf.BaseProtobufCmdHandler;
import com.github.byakkili.bim.Packet.Chat;
import com.github.byakkili.bim.Packet.Command;
import com.google.protobuf.Message;

/**
 * @author Guannian Li
 */
public class TestHandler extends BaseProtobufCmdHandler<Chat> {
    @Override
    public CmdMsgFrame<Message> handle(Chat msg, BimSession session) {
        Chat respMsg = Chat.newBuilder()
                .setCmd(Command.CHAT_ACK)
                .setSeq(msg.getSeq())
                .setTo(msg.getTo())
                .setContent(msg.getContent() + ": 已收到")
                .setChatType(msg.getChatType())
                .setMsgType(msg.getMsgType())
                .build();
        return new CmdMsgFrame<>(Command.CHAT_ACK_VALUE, respMsg);
    }

    @Override
    public int cmd() {
        return 0;
    }
}
