package com.github.byakkili.bim.core.protocol.protobuf.tcp;

import com.github.byakkili.bim.core.protocol.protobuf.ProtobufFrame;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author Guannian Li
 */
@Sharable
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TcpProtobufEncoder extends MessageToByteEncoder<ProtobufFrame> {
    /**
     * 单例
     */
    static final TcpProtobufEncoder INSTANCE = new TcpProtobufEncoder();

    @Override
    protected void encode(ChannelHandlerContext ctx, ProtobufFrame frame, ByteBuf out) {
        out.writeBytes(new TcpProtobufPacket(frame).toBytes());
    }
}
