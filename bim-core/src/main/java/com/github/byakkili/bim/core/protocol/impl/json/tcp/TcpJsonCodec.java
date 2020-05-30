package com.github.byakkili.bim.core.protocol.impl.json.tcp;

import com.github.byakkili.bim.core.protocol.impl.json.JsonMsg;
import com.github.byakkili.bim.core.util.JsonUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Guannian Li
 */
@Sharable
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TcpJsonCodec extends MessageToMessageCodec<ByteBuf, JsonMsg> {
    /**
     * 单例
     */
    static final TcpJsonCodec INSTANCE = new TcpJsonCodec();

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) {
        out.add(JsonUtils.deserialize(ByteBufUtil.getBytes(buf)));
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, JsonMsg msg, List<Object> out) {
        TcpJsonPacket packet = new TcpJsonPacket(msg);

        ByteBuf byteBuf = ctx.alloc().heapBuffer();
        byteBuf.writeBytes(packet.toBytes());
        out.add(byteBuf);
    }
}
