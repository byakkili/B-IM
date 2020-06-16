package com.github.byakkili.bim.core.protocol.impl.json.tcp;

import com.github.byakkili.bim.core.BimSession;
import com.github.byakkili.bim.core.protocol.CmdMsgFrame;
import com.github.byakkili.bim.core.protocol.impl.json.BaseJsonCodec;
import com.github.byakkili.bim.core.protocol.impl.json.JsonMsg;
import com.github.byakkili.bim.core.util.BimSessionUtils;
import com.github.byakkili.bim.core.util.JsonUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author Guannian Li
 */
@Sharable
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TcpJsonCodec extends BaseJsonCodec<ByteBuf> {
    /**
     * 单例
     */
    static final TcpJsonCodec INSTANCE = new TcpJsonCodec();

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) {
        BimSession session = BimSessionUtils.get(ctx.channel());

        byte[] bytes = ByteBufUtil.getBytes(byteBuf);
        Map jsonMap = JsonUtils.deserialize(bytes, Map.class);

        CmdMsgFrame<JsonMsg> frame = decodeToFrame(session, jsonMap);

        if (frame != null) {
            out.add(frame);
        }
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, CmdMsgFrame<JsonMsg> frame, List<Object> out) throws Exception {
        TcpJsonPacket packet = new TcpJsonPacket(frame);
        byte[] bytes = packet.toByteArray();

        ByteBuf byteBuf = ctx.alloc().heapBuffer();
        byteBuf.writeBytes(bytes);
        out.add(byteBuf);
    }
}
