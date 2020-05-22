package com.github.byakkili.bim.core.protocol.protobuf.ws;

import com.github.byakkili.bim.core.protocol.protobuf.ProtobufFrame;
import com.github.byakkili.bim.core.util.ProtobufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
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
public class WsProtobufCodec extends MessageToMessageCodec<BinaryWebSocketFrame, ProtobufFrame> {
    /**
     * 单例
     */
    static final WsProtobufCodec INSTANCE = new WsProtobufCodec();

    @Override
    protected void decode(ChannelHandlerContext ctx, BinaryWebSocketFrame frame, List<Object> out) {
        out.add(frame.content().retain());
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ProtobufFrame message, List<Object> out) {
        ByteBuf byteBuf = ctx.alloc().heapBuffer();
        byteBuf.writeInt(message.getCmd());
        byteBuf.writeBytes(ProtobufUtils.serialize(message.getMessage()));

        out.add(new BinaryWebSocketFrame(byteBuf));
    }
}
