package com.github.byakkili.bim.protocol.protobuf.tcp;

import com.github.byakkili.bim.core.BimSession;
import com.github.byakkili.bim.core.protocol.CmdMsgFrame;
import com.github.byakkili.bim.protocol.protobuf.BaseProtobufCodec;
import com.github.byakkili.bim.core.util.BimSessionUtils;
import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Guannian Li
 */
@Sharable
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TcpProtobufCodec extends BaseProtobufCodec<ByteBuf> {
    /**
     * 单例
     */
    static final TcpProtobufCodec INSTANCE = new TcpProtobufCodec();

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
        BimSession session = BimSessionUtils.get(ctx.channel());

        CmdMsgFrame<Message> cmdMsgFrame = decodeToFrame(session, byteBuf);

        if (cmdMsgFrame != null) {
            out.add(cmdMsgFrame);
        }
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, CmdMsgFrame<Message> frame, List<Object> out) throws Exception {
        TcpProtobufPacket packet = new TcpProtobufPacket(frame);
        byte[] bytes = packet.toByteArray();

        ByteBuf byteBuf = ctx.alloc().heapBuffer();
        byteBuf.writeBytes(bytes);
        out.add(byteBuf);
    }
}
