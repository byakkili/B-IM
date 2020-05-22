package com.github.byakkili.bim.core.protocol;

import cn.hutool.core.collection.CollUtil;
import com.github.byakkili.bim.core.BimContext;
import com.github.byakkili.bim.core.util.BimSessionUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * 协议解码器
 *
 * @author Guannian Li
 */
@Sharable
public class ProtocolDecoder extends SimpleChannelInboundHandler<ByteBuf> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProtocolDecoder.class);
    /**
     * 协议提供者列表
     */
    private final Set<IProtocolProvider> protocolProviders;

    public ProtocolDecoder(BimContext context) {
        super(false);
        this.protocolProviders = context.getProtocolProviders();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buffer) {
        // 获取协议提供者
        IProtocolProvider protocolProvider = getProtocolProvider(buffer);
        if (protocolProvider == null) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("Unsupported protocol type");
            }
            ctx.pipeline().remove(this);
            ctx.close().syncUninterruptibly();
            return;
        }
        // 设置会话协议
        BimSessionUtils.get(ctx.channel()).setProtocol(protocolProvider.name());
        // 添加协议处理器
        ctx.pipeline().addLast(protocolProvider.channelHandlers());
        // 删除当前处理器
        ctx.pipeline().remove(this);
        // 继续往下执行处理器
        ctx.fireChannelRead(buffer);
    }

    /**
     * 获取协议提供者
     *
     * @param buffer 请求Buffer
     * @return 协议提供者
     */
    private IProtocolProvider getProtocolProvider(ByteBuf buffer) {
        if (CollUtil.isNotEmpty(protocolProviders)) {
            for (IProtocolProvider protocolProvider : protocolProviders) {
                if (protocolProvider.isProtocol(buffer)) {
                    return protocolProvider;
                }
            }
        }
        return null;
    }
}
