package com.github.byakkili.bim.core.protocol.impl.json.tcp;

import cn.hutool.core.util.ObjectUtil;
import com.github.byakkili.bim.core.protocol.CommandCodec;
import com.github.byakkili.bim.core.protocol.ProtocolProvider;
import com.github.byakkili.bim.core.protocol.impl.json.JsonMsgEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.EqualsAndHashCode;

/**
 * +-------------+--------+--------+
 * | 协议开始标志 |  长度  |  JSON  |
 * +-------------+--------+--------+
 * |    1 byte   | 4 byte | n byte |
 * +-------------+--------+--------+
 *
 * @author Guannian Li
 */
@EqualsAndHashCode
public class TcpJsonProtocolProvider implements ProtocolProvider {
    @Override
    public String name() {
        return "TCP";
    }

    @Override
    public boolean isProtocol(ByteBuf buffer) {
        buffer.markReaderIndex();
        try {
            return ObjectUtil.equal(buffer.readByte(), TcpJsonPacket.PROTOCOL_HEAD);
        } finally {
            buffer.resetReaderIndex();
        }
    }

    @Override
    public ChannelHandler[] channelHandlers() {
        return new ChannelHandler[]{
                // 协议1位, 长度4位, Json bytes
                new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 1, 4, 0, 5),
                TcpJsonCodec.INSTANCE,
                CommandCodec.INSTANCE,
                JsonMsgEncoder.INSTANCE
        };
    }
}
