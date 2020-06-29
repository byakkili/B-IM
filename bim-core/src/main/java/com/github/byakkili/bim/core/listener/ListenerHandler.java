package com.github.byakkili.bim.core.listener;

import cn.hutool.core.collection.CollUtil;
import com.github.byakkili.bim.core.BimContext;
import com.github.byakkili.bim.core.BimSession;
import com.github.byakkili.bim.core.util.BimSessionUtils;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.List;

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
    private List<SessionListener> sessionListeners;

    public ListenerHandler(BimContext context) {
        this.context = context;
        this.sessionListeners = context.getSessionListeners();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        BimSession session = BimSessionUtils.init(ctx.channel(), context);
        // 会话监听器
        sessionListeners.forEach(listener -> listener.onAfterCreated(session));
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        BimSession session = BimSessionUtils.get(ctx.channel());
        // 会话监听器
        sessionListeners.forEach(listener -> listener.onBeforeDestroy(session));
        session.unbind();
        BimSessionUtils.destroy(session);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        BimSession session = BimSessionUtils.get(ctx.channel());
        // 会话监听器
        if ((evt instanceof IdleStateEvent) && CollUtil.isNotEmpty(sessionListeners)) {
            switch (((IdleStateEvent) evt).state()) {
                case READER_IDLE:
                    sessionListeners.forEach(listener -> listener.onReaderIdle(session));
                    break;
                case WRITER_IDLE:
                    sessionListeners.forEach(listener -> listener.onWriterIdle(session));
                    break;
                default:
                    break;
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
