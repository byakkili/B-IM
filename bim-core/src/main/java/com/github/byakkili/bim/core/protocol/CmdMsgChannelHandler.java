package com.github.byakkili.bim.core.protocol;

import cn.hutool.core.collection.CollUtil;
import com.github.byakkili.bim.core.BimContext;
import com.github.byakkili.bim.core.BimSession;
import com.github.byakkili.bim.core.cmd.ICmdHandler;
import com.github.byakkili.bim.core.interceptor.CmdInterceptor;
import com.github.byakkili.bim.core.listener.ISessionListener;
import com.github.byakkili.bim.core.util.BimSessionUtils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @author Guannian Li
 */
@ChannelHandler.Sharable
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CmdMsgChannelHandler extends MessageToMessageCodec<CmdMsgFrame, CmdMsgFrame> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CmdMsgChannelHandler.class);
    /**
     * 单例
     */
    public static final CmdMsgChannelHandler INSTANCE = new CmdMsgChannelHandler();

    @Override
    protected final void decode(ChannelHandlerContext ctx, CmdMsgFrame frame, List<Object> list) {
        BimSession session = BimSessionUtils.get(ctx.channel());
        BimContext context = session.getContext();
        ISessionListener sessionListener = context.getSessionListener();
        Map<Integer, ICmdHandler> cmdHandlers = context.getCmdHandlers();

        if (sessionListener != null) {
            sessionListener.onRead(frame, session);
        }

        RuntimeException ex = null;
        CmdMsgFrame respFrame = null;
        try {
            if (!applyPreHandle(frame, session)) {
                return;
            }
            Integer cmd = frame.getCmd();
            ICmdHandler cmdHandler = cmd == null ? null : cmdHandlers.get(cmd);
            if (cmdHandler == null) {
                return;
            }
            respFrame = cmdHandler.msgHandle(frame, session);
            applyPostHandle(respFrame, session);
        } catch (RuntimeException e) {
            ex = e;
            throw e;
        } finally {
            triggerAfterCompletion(frame, session, ex);
            if (respFrame != null) {
                session.writeAndFlush(respFrame);
            }
        }
    }

    @Override
    protected final void encode(ChannelHandlerContext ctx, CmdMsgFrame frame, List<Object> list) {
        BimSession session = BimSessionUtils.get(ctx.channel());
        BimContext context = session.getContext();
        ISessionListener sessionListener = context.getSessionListener();

        if (sessionListener != null) {
            sessionListener.onWrite(frame, session);
        }
        list.add(frame);
    }

    /**
     * 前置处理
     *
     * @param reqFrame 请求帧
     * @param session  会话
     * @return 是否往下执行
     */
    private boolean applyPreHandle(CmdMsgFrame reqFrame, BimSession session) {
        List<CmdInterceptor> interceptors = session.getContext().getCmdInterceptors();
        if (CollUtil.isNotEmpty(interceptors)) {
            for (CmdInterceptor interceptor : interceptors) {
                if (!interceptor.preHandle(reqFrame, session)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 后置处理
     *
     * @param respFrame 响应帧
     * @param session   会话
     */
    private void applyPostHandle(CmdMsgFrame respFrame, BimSession session) {
        List<CmdInterceptor> interceptors = session.getContext().getCmdInterceptors();
        if (CollUtil.isNotEmpty(interceptors)) {
            for (CmdInterceptor interceptor : interceptors) {
                try {
                    interceptor.postHandle(respFrame, session);
                } catch (RuntimeException e) {
                    LOGGER.error("CmdInterceptor.postHandle threw exception", e);
                }
            }
        }
    }

    /**
     * 完成后处罚
     *
     * @param reqFrame 请求帧
     * @param session  会话
     * @param e        异常
     */
    private void triggerAfterCompletion(CmdMsgFrame reqFrame, BimSession session, Exception e) {
        List<CmdInterceptor> interceptors = session.getContext().getCmdInterceptors();
        if (CollUtil.isNotEmpty(interceptors)) {
            for (CmdInterceptor interceptor : interceptors) {
                try {
                    interceptor.afterCompletion(reqFrame, session, e);
                } catch (RuntimeException re) {
                    LOGGER.error("CmdInterceptor.afterCompletion threw exception", re);
                }
            }
        }
    }
}
