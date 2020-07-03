package com.github.byakkili.bim.core.protocol.impl.json.ws;

import com.github.byakkili.bim.core.BimSession;
import com.github.byakkili.bim.core.protocol.CommandFrame;
import com.github.byakkili.bim.core.protocol.impl.json.BaseJsonCodec;
import com.github.byakkili.bim.core.protocol.impl.json.JsonMsg;
import com.github.byakkili.bim.core.util.BimSessionUtils;
import com.github.byakkili.bim.core.util.JsonUtils;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author Guannian Li
 */
@Sharable
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WsJsonCodec extends BaseJsonCodec<TextWebSocketFrame> {
    /**
     * 单例
     */
    static final WsJsonCodec INSTANCE = new WsJsonCodec();

    @Override
    protected void decode(ChannelHandlerContext ctx, TextWebSocketFrame textWsFrame, List<Object> out) {
        BimSession session = BimSessionUtils.get(ctx.channel());

        Map jsonMap = JsonUtils.parse(textWsFrame.text(), Map.class);

        CommandFrame<JsonMsg> frame = decodeToFrame(session, jsonMap);

        if (frame != null) {
            out.add(frame);
        }
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, CommandFrame<JsonMsg> frame, List<Object> out) {
        JsonMsg jsonMsg = frame.getMsg();
        String jsonStr = JsonUtils.stringify(jsonMsg);

        TextWebSocketFrame textWsFrame = new TextWebSocketFrame(jsonStr);
        out.add(textWsFrame);
    }
}
