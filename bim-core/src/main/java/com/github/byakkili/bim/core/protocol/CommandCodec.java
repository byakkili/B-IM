package com.github.byakkili.bim.core.protocol;

import cn.hutool.core.collection.CollUtil;
import com.github.byakkili.bim.core.BimContext;
import com.github.byakkili.bim.core.BimSession;
import com.github.byakkili.bim.core.command.CommandHandler;
import com.github.byakkili.bim.core.interceptor.CommandInterceptor;
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
 * 指令解码器
 *
 * @author Guannian Li
 */
@Sharable
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommandCodec extends MessageToMessageCodec<CommandFrame, CommandFrame> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandCodec.class);
    /**
     * 单例
     */
    public static final CommandCodec INSTANCE = new CommandCodec();

    @Override
    @SuppressWarnings("unchecked")
    protected final void decode(ChannelHandlerContext ctx, CommandFrame frame, List<Object> list) {
        BimSession session = BimSessionUtils.get(ctx.channel());
        BimContext context = session.getContext();
        Map<Integer, CommandHandler> commandHandlers = context.getCommandHandlers();

        // 会话监听器
        context.getSessionListeners().forEach(listener -> listener.onRead(frame, session));

        RuntimeException tmpEx = null;
        Object respMsg = null;
        Integer command = frame.getCommand();
        try {
            if (!applyPreHandle(command, session)) {
                return;
            }
            CommandHandler commandHandler = commandHandlers.get(command);
            if (commandHandler == null) {
                return;
            }
            Object msg = frame.getMsg();
            respMsg = commandHandler.handle(msg, session);
            applyPostHandle(command, respMsg, session);
        } catch (RuntimeException ex) {
            tmpEx = ex;
            throw ex;
        } finally {
            triggerAfterCompletion(command, session, tmpEx);
            if (respMsg != null) {
                session.writeAndFlush(respMsg);
            }
        }
    }

    @Override
    protected final void encode(ChannelHandlerContext ctx, CommandFrame frame, List<Object> list) {
        BimSession session = BimSessionUtils.get(ctx.channel());
        BimContext context = session.getContext();

        // 会话监听器
        context.getSessionListeners().forEach(listener -> listener.onWrite(frame, session));

        list.add(frame);
    }

    /**
     * 前置处理
     *
     * @param command 指令
     * @param session 会话
     * @return 是否往下执行
     */
    private boolean applyPreHandle(int command, BimSession session) {
        List<CommandInterceptor> interceptors = session.getContext().getCommandInterceptors();
        if (CollUtil.isNotEmpty(interceptors)) {
            for (CommandInterceptor interceptor : interceptors) {
                if (!interceptor.preHandle(command, session)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 后置处理
     *
     * @param command 指令
     * @param msg     消息
     * @param session 会话
     */
    private void applyPostHandle(int command, Object msg, BimSession session) {
        List<CommandInterceptor> interceptors = session.getContext().getCommandInterceptors();
        if (CollUtil.isNotEmpty(interceptors)) {
            for (int i = interceptors.size() - 1; i >= 0; i--) {
                try {
                    interceptors.get(i).postHandle(command, msg, session);
                } catch (Exception e) {
                    LOGGER.error("CommandInterceptor.postHandle throw exception", e);
                }
            }
        }
    }

    /**
     * 完成后处理
     *
     * @param command 指令
     * @param session 会话
     * @param e       异常
     */
    private void triggerAfterCompletion(int command, BimSession session, RuntimeException e) {
        List<CommandInterceptor> interceptors = session.getContext().getCommandInterceptors();
        if (CollUtil.isNotEmpty(interceptors)) {
            for (int i = interceptors.size() - 1; i >= 0; i--) {
                try {
                    interceptors.get(i).afterCompletion(command, session, e);
                } catch (RuntimeException re) {
                    LOGGER.error("CommandInterceptor.afterCompletion throw exception", re);
                }
            }
        }
    }
}
