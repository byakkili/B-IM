package com.github.byakkili.bim.demo;

import com.github.byakkili.bim.core.BimSession;
import com.github.byakkili.bim.core.protocol.impl.protobuf.BaseProtobufCmdHandler;
import com.github.byakkili.bim.core.protocol.impl.protobuf.ProtobufFrame;
import com.github.byakkili.bim.protobuf.Packet.Chat;

/**
 * @author Guannian Li
 */
public class TestHandler extends BaseProtobufCmdHandler<Chat> {
    @Override
    protected ProtobufFrame handle(Chat msg, BimSession session) {
        Chat respMsg = Chat.newBuilder()
                .setSeq(msg.getSeq())
                .setTo(msg.getTo())
                .setContent(msg.getContent() + ": 已收到")
                .build();
        return new ProtobufFrame(2, respMsg);
    }

    @Override
    public int cmd() {
        return 0;
    }

    @Override
    public Class<Chat> reqMsgClass() {
        return Chat.class;
    }
}
