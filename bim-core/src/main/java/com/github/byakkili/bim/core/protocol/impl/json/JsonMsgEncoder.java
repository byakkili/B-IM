package com.github.byakkili.bim.core.protocol.impl.json;

import com.github.byakkili.bim.core.protocol.CommandFrame;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * @author Guannian Li
 */
@Sharable
public class JsonMsgEncoder extends MessageToMessageEncoder<JsonMsg> {
    /**
     * 单例
     */
    public static final JsonMsgEncoder INSTANCE = new JsonMsgEncoder();

    @Override
    protected void encode(ChannelHandlerContext ctx, JsonMsg msg, List<Object> out) throws Exception {
        if (msg.getCommand() != null) {
            out.add(new CommandFrame<>(msg.getCommand(), msg));
        }
    }
}
