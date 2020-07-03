package com.github.byakkili.bim.core.protocol.impl.protobuf.ws;

import com.github.byakkili.bim.core.BimSession;
import com.github.byakkili.bim.core.protocol.CommandFrame;
import com.github.byakkili.bim.core.protocol.impl.protobuf.BaseProtobufCodec;
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
 * |  指令  | protobuf |
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

        CommandFrame<Message> commandFrame = decodeToFrame(session, binaryWsFrame.content());

        if (commandFrame != null) {
            out.add(commandFrame);
        }
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, CommandFrame<Message> frame, List<Object> out) throws Exception {
        Integer command = frame.getCommand();
        Message msg = frame.getMsg();
        byte[] data = ProtobufUtils.serialize(msg);

        ByteBuf byteBuf = ctx.alloc().heapBuffer();
        byteBuf.writeInt(command);
        byteBuf.writeBytes(data);

        BinaryWebSocketFrame binaryWsFrame = new BinaryWebSocketFrame(byteBuf);
        out.add(binaryWsFrame);
    }
}
