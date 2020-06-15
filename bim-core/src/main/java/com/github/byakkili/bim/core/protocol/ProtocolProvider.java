package com.github.byakkili.bim.core.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;

/**
 * 协议提供者
 *
 * @author Guannian Li
 */
public interface ProtocolProvider {
    /**
     * 当前协议的名称
     *
     * @return 当前协议名称
     */
    String name();

    /**
     * 判断是否为当前协议
     *
     * @param buffer 请求内容
     * @return 是/否
     */
    boolean isProtocol(ByteBuf buffer);

    /**
     * 当前协议的通道处理器
     *
     * @return 通道处理器数组
     */
    ChannelHandler[] channelHandlers();
}
