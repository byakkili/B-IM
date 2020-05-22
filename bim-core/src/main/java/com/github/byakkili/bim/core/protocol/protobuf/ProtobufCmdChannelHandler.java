package com.github.byakkili.bim.core.protocol.protobuf;

import com.github.byakkili.bim.core.protocol.BaseCmdChannelHandler;
import com.github.byakkili.bim.core.util.ProtobufUtils;
import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandler.Sharable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * CMD处理器
 *
 * @author Guannian Li
 */
@Sharable
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProtobufCmdChannelHandler extends BaseCmdChannelHandler<ByteBuf, Message> {
    /**
     * 单例
     */
    public static final ProtobufCmdChannelHandler INSTANCE = new ProtobufCmdChannelHandler();

    @Override
    protected Integer getCmd(ByteBuf byteBuf) {
        return byteBuf.readInt();
    }

    @Override
    protected Message convert(ByteBuf byteBuf, Class<Message> targetClass) {
        return ProtobufUtils.deserialize(ByteBufUtil.getBytes(byteBuf), targetClass);
    }
}