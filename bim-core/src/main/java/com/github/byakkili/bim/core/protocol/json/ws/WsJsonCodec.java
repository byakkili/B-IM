package com.github.byakkili.bim.core.protocol.json.ws;

import com.github.byakkili.bim.core.protocol.json.base.JsonMsg;
import com.github.byakkili.bim.core.util.JsonUtils;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Guannian Li
 */
@Sharable
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WsJsonCodec extends MessageToMessageCodec<TextWebSocketFrame, JsonMsg> {
    /**
     * 单例
     */
    static final WsJsonCodec INSTANCE = new WsJsonCodec();

    @Override
    protected void decode(ChannelHandlerContext ctx, TextWebSocketFrame msg, List<Object> out) {
        // 解析请求内容
        out.add(JsonUtils.parseStr(msg.text()));
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, JsonMsg msg, List<Object> out) {
        out.add(new TextWebSocketFrame(JsonUtils.stringify(msg)));
    }
}
