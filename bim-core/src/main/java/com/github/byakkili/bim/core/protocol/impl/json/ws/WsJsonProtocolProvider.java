package com.github.byakkili.bim.core.protocol.impl.json.ws;

import com.github.byakkili.bim.core.protocol.impl.BaseWsProtocolProvider;
import com.github.byakkili.bim.core.protocol.CommandCodec;
import com.github.byakkili.bim.core.protocol.impl.json.JsonMsgEncoder;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.EqualsAndHashCode;

/**
 * @author Guannian Li
 */
@EqualsAndHashCode(callSuper = true)
public class WsJsonProtocolProvider extends BaseWsProtocolProvider {
    @Override
    public ChannelHandler[] channelHandlers() {
        return new ChannelHandler[]{
                new HttpServerCodec(),
                new ChunkedWriteHandler(),
                new HttpObjectAggregator(1024),
                new WebSocketServerProtocolHandler("/ws", true),
                WsJsonCodec.INSTANCE,
                CommandCodec.INSTANCE,
                JsonMsgEncoder.INSTANCE
        };
    }
}

