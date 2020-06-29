package com.github.byakkili.bim.core.protocol;

import cn.hutool.core.collection.CollUtil;
import com.github.byakkili.bim.core.BimContext;
import com.github.byakkili.bim.core.BimSession;
import com.github.byakkili.bim.core.cmd.CmdHandler;
import com.github.byakkili.bim.core.interceptor.CmdInterceptor;
import com.github.byakkili.bim.core.util.BimSessionUtils;
import io.netty.channel.ChannelHandler.Sharable;
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
@Sharable
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CmdMsgHandler extends MessageToMessageCodec<CmdMsgFrame, CmdMsgFrame> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CmdMsgHandler.class);
    /**
     * 单例
     */
    public static final CmdMsgHandler INSTANCE = new CmdMsgHandler();

    @Override
    @SuppressWarnings("unchecked")
    protected final void decode(ChannelHandlerContext ctx, CmdMsgFrame frame, List<Object> list) {
        BimSession session = BimSessionUtils.get(ctx.channel());
        BimContext context = session.getContext();
        Map<Integer, CmdHandler> cmdHandlers = context.getCmdHandlers();

        // 会话监听器
        context.getSessionListeners().forEach(listener -> listener.onRead(frame, session));

        RuntimeException tmpEx = null;
        Object respMsg = null;
        Integer cmd = frame.getCmd();
        try {
            if (!applyPreHandle(cmd, session)) {
                return;
            }
            CmdHandler cmdHandler = cmdHandlers.get(cmd);
            if (cmdHandler == null) {
                return;
            }
            Object msg = frame.getMsg();
            respMsg = cmdHandler.handle(msg, session);
            applyPostHandle(cmd, respMsg, session);
        } catch (RuntimeException ex) {
            tmpEx = ex;
            throw ex;
        } finally {
            triggerAfterCompletion(cmd, session, tmpEx);
            if (respMsg != null) {
                session.writeAndFlush(respMsg);
            }
        }
    }

    @Override
    protected final void encode(ChannelHandlerContext ctx, CmdMsgFrame frame, List<Object> list) {
        BimSession session = BimSessionUtils.get(ctx.channel());
        BimContext context = session.getContext();

        // 会话监听器
        context.getSessionListeners().forEach(listener -> listener.onWrite(frame, session));

        list.add(frame);
    }

    /**
     * 前置处理
     *
     * @param cmd     CMD
     * @param session 会话
     * @return 是否往下执行
     */
    private boolean applyPreHandle(int cmd, BimSession session) {
        List<CmdInterceptor> interceptors = session.getContext().getCmdInterceptors();
        if (CollUtil.isNotEmpty(interceptors)) {
            for (CmdInterceptor interceptor : interceptors) {
                if (!interceptor.preHandle(cmd, session)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 后置处理
     *
     * @param cmd     CMD
     * @param msg     消息
     * @param session 会话
     */
    private void applyPostHandle(int cmd, Object msg, BimSession session) {
        List<CmdInterceptor> interceptors = session.getContext().getCmdInterceptors();
        if (CollUtil.isNotEmpty(interceptors)) {
            for (int i = interceptors.size() - 1; i >= 0; i--) {
                try {
                    interceptors.get(i).postHandle(cmd, msg, session);
                } catch (Exception e) {
                    LOGGER.error("CmdInterceptor.postHandle throw exception", e);
                }
            }
        }
    }

    /**
     * 完成后处理
     *
     * @param cmd     CMD
     * @param session 会话
     * @param e       异常
     */
    private void triggerAfterCompletion(int cmd, BimSession session, RuntimeException e) {
        List<CmdInterceptor> interceptors = session.getContext().getCmdInterceptors();
        if (CollUtil.isNotEmpty(interceptors)) {
            for (int i = interceptors.size() - 1; i >= 0; i--) {
                try {
                    interceptors.get(i).afterCompletion(cmd, session, e);
                } catch (RuntimeException re) {
                    LOGGER.error("CmdInterceptor.afterCompletion throw exception", re);
                }
            }
        }
    }
}
