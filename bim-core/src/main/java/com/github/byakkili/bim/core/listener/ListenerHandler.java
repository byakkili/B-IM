package com.github.byakkili.bim.core.listener;

import com.github.byakkili.bim.core.BimContext;
import com.github.byakkili.bim.core.BimSession;
import com.github.byakkili.bim.core.util.BimSessionUtils;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 监听处理器
 *
 * @author Guannian Li
 */
@Sharable
public class ListenerHandler extends ChannelDuplexHandler {
    /**
     * B-IM上下文
     */
    private BimContext context;
    /**
     * 会话监听器
     */
    private ISessionListener sessionListener;

    public ListenerHandler(BimContext context) {
        this.context = context;
        this.sessionListener = context.getSessionListener();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        BimSession session = BimSessionUtils.init(ctx.channel(), context);
        // 会话监听器
        if (sessionListener != null) {
            sessionListener.onAfterCreated(session);
        }
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        BimSession session = BimSessionUtils.get(ctx.channel());
        // 会话监听器
        if (sessionListener != null) {
            sessionListener.onBeforeDestroy(session);
        }
        session.unbind();
        session.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        BimSession session = BimSessionUtils.get(ctx.channel());
        if ((evt instanceof IdleStateEvent) && sessionListener != null) {
            switch (((IdleStateEvent) evt).state()) {
                case READER_IDLE:
                    sessionListener.onReaderIdle(session);
                    break;
                case WRITER_IDLE:
                    sessionListener.onWriterIdle(session);
                    break;
                default:
                    break;
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
