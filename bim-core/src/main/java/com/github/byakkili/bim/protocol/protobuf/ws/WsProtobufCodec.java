package com.github.byakkili.bim.protocol.protobuf.ws;

import com.github.byakkili.bim.core.BimSession;
import com.github.byakkili.bim.core.protocol.CmdMsgFrame;
import com.github.byakkili.bim.protocol.protobuf.BaseProtobufCodec;
import com.github.byakkili.bim.core.util.BimSessionUtils;
import com.github.byakkili.bim.core.util.ProtobufUtils;
import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * +--------+----------+
 * |  CMD   | protobuf |
 * +--------+----------+
 * | 4 byte |  n byte  |
 * +--------+----------+
 *
 * @author Guannian Li
 */
@Sharable
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WsProtobufCodec extends BaseProtobufCodec<BinaryWebSocketFrame> {
    /**
     * 单例
     */
    static final WsProtobufCodec INSTANCE = new WsProtobufCodec();

    @Override
    protected void decode(ChannelHandlerContext ctx, BinaryWebSocketFrame binaryWsFrame, List<Object> out) {
        BimSession session = BimSessionUtils.get(ctx.channel());

        CmdMsgFrame<Message> cmdMsgFrame = decodeToFrame(session, binaryWsFrame.content());

        if (cmdMsgFrame != null) {
            out.add(cmdMsgFrame);
        }
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, CmdMsgFrame<Message> frame, List<Object> out) {
        Integer cmd = frame.getCmd();
        Message msg = frame.getMsg();
        byte[] data = ProtobufUtils.serialize(msg);

        ByteBuf byteBuf = ctx.alloc().heapBuffer();
        byteBuf.writeInt(cmd);
        byteBuf.writeBytes(data);

        BinaryWebSocketFrame binaryWsFrame = new BinaryWebSocketFrame(byteBuf);
        out.add(binaryWsFrame);
    }
}
