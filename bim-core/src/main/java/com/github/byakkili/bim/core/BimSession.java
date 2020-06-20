package com.github.byakkili.bim.core;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.AttributeKey;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Closeable;
import java.util.Set;

/**
 * @author Guannian Li
 */
@Setter
@Getter
@EqualsAndHashCode
public class BimSession {
    /**
     * 会话ID
     */
    private final String id;
    /**
     * 通道
     */
    private final Channel channel;
    /**
     * B-IM上下文
     */
    private final BimContext context;
    /**
     * 协议
     */
    private String protocol;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 用户组
     */
    private Set<String> groupIds;
    /**
     * Token
     */
    private String token;

    public BimSession(Channel channel, BimContext context) {
        this.id = channel.id().asShortText();
        this.channel = channel;
        this.context = context;
    }

    // ---------------------------------------------------------------------------------------------- Public method start

    /**
     * 绑定到上下文
     */
    public synchronized void bind() {
        context.getUsers().bind(this);
        context.getGroups().bind(this);
    }

    /**
     * 从上下文中解除绑定
     */
    public synchronized void unbind() {
        context.getUsers().unbind(this);
        context.getGroups().unbind(this);
    }

    /**
     * 获取属性
     *
     * @param key 键
     * @return 值
     */
    public Object getAttribute(String key) {
        return channel.attr(AttributeKey.valueOf(key)).get();
    }

    /**
     * 设置属性
     *
     * @param key   键
     * @param value 值
     */
    public void setAttribute(String key, Object value) {
        channel.attr(AttributeKey.valueOf(key)).set(value);
    }

    /**
     * 删除属性
     *
     * @param key 键
     */
    public void removeAttribute(String key) {
        channel.attr(AttributeKey.valueOf(key)).set(null);
    }

    /**
     * 写入且刷新
     *
     * @param object 对象
     * @return Future
     */
    public synchronized ChannelFuture writeAndFlush(Object object) {
        return channel.writeAndFlush(object);
    }

    /**
     * 关闭
     */
    public synchronized void close() {
        if (channel != null && channel.isActive()) {
            channel.close();
        }
    }

    // ---------------------------------------------------------------------------------------------- Public method end
}
