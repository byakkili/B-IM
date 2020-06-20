package com.github.byakkili.bim.protocol.json;

import com.github.byakkili.bim.core.protocol.CmdMsgFrame;
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
        if (msg.getCmd() != null) {
            out.add(new CmdMsgFrame<>(msg.getCmd(), msg));
        }
    }
}
