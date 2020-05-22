package com.github.byakkili.bim.core.protocol;

import cn.hutool.core.collection.CollUtil;
import com.github.byakkili.bim.core.BimContext;
import com.github.byakkili.bim.core.BimSession;
import com.github.byakkili.bim.core.cmd.ICmdHandler;
import com.github.byakkili.bim.core.interceptor.CmdInterceptor;
import com.github.byakkili.bim.core.util.BimSessionUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @author Guannian Li
 */
public abstract class BaseCmdChannelHandler<I, T> extends SimpleChannelInboundHandler<I> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseCmdChannelHandler.class);

    @Override
    protected final void channelRead0(ChannelHandlerContext ctx, I msg) {
        BimSession session = BimSessionUtils.get(ctx.channel());
        BimContext context = session.getContext();
        Map<Integer, ICmdHandler> cmdHandlers = context.getCmdHandlers();

        Integer cmd = getCmd(msg);
        ICmdHandler cmdHandler = cmd == null ? null : cmdHandlers.get(cmd);
        if (cmdHandler == null) {
            //TODO no cmd
            return;
        }
        @SuppressWarnings("unchecked")
        Object reqMsg = convert(msg, cmdHandler.reqMsgClass());
        RuntimeException ex = null;
        try {
            if (!applyPreHandle(cmd, reqMsg, session)) {
                return;
            }
            Object respMsg = cmdHandler.msgHandle(reqMsg, session);
            if (respMsg != null) {
                session.writeAndFlush(respMsg);
            }
            applyPostHandle(cmd, session, respMsg);
        } catch (RuntimeException e) {
            ex = e;
            throw e;
        } finally {
            triggerAfterCompletion(cmd, session, ex);
        }
    }

    /**
     * 获取CMD
     *
     * @param msg 请求消息
     * @return CMD
     */
    protected abstract Integer getCmd(I msg);

    /**
     * 类型转换
     *
     * @param msg         请求消息
     * @param targetClass 目标类型
     * @return 目标对象
     */
    protected abstract T convert(I msg, Class<T> targetClass);

    /**
     * 前置处理
     *
     * @param cmd     CMD
     * @param reqMsg  请求消息
     * @param session 会话
     * @return 是否往下执行
     */
    private boolean applyPreHandle(Integer cmd, Object reqMsg, BimSession session) {
        List<CmdInterceptor> interceptors = session.getContext().getCmdInterceptors();
        if (CollUtil.isNotEmpty(interceptors)) {
            for (CmdInterceptor interceptor : interceptors) {
                if (!interceptor.preHandle(cmd, reqMsg, session)) {
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
     * @param session 会话
     * @param respMsg 响应消息
     */
    private void applyPostHandle(Integer cmd, BimSession session, Object respMsg) {
        List<CmdInterceptor> interceptors = session.getContext().getCmdInterceptors();
        if (CollUtil.isNotEmpty(interceptors)) {
            for (CmdInterceptor interceptor : interceptors) {
                try {
                    interceptor.postHandle(cmd, session, respMsg);
                } catch (RuntimeException e) {
                    LOGGER.error("CmdInterceptor.postHandle threw exception", e);
                }
            }
        }
    }

    /**
     * 完成后处罚
     *
     * @param cmd     命令
     * @param session 会话
     * @param e       异常
     */
    private void triggerAfterCompletion(Integer cmd, BimSession session, Exception e) {
        List<CmdInterceptor> interceptors = session.getContext().getCmdInterceptors();
        if (CollUtil.isNotEmpty(interceptors)) {
            for (CmdInterceptor interceptor : interceptors) {
                try {
                    interceptor.afterCompletion(cmd, session, e);
                } catch (RuntimeException re) {
                    LOGGER.error("CmdInterceptor.afterCompletion threw exception", re);
                }
            }
        }
    }
}
