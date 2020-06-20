package com.github.byakkili.bim.core;

import cn.hutool.core.collection.CollUtil;
import com.github.byakkili.bim.core.listener.ListenerHandler;
import com.github.byakkili.bim.core.protocol.ProtocolDecoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author Guannian Li
 */
public class BimServerBootstrap {
    private static final Logger LOGGER = LoggerFactory.getLogger(BimServerBootstrap.class);
    private static final LoggingHandler DEBUG_LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);
    /**
     * B-IM上下文
     */
    private final @Getter BimContext context;
    /**
     * Netty server bootstrap
     */
    private ServerBootstrap bootstrap;

    private Channel channel;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    public BimServerBootstrap(BimConfiguration config) {
        this(config, null);
    }

    public BimServerBootstrap(BimConfiguration config, ServerBootstrap bootstrap) {
        this.context = new BimContext(config);
        this.bootstrap = bootstrap != null ? bootstrap : defaultServerBootstrap();
        this.bossGroup = this.bootstrap.config().group();
        this.workerGroup = this.bootstrap.config().childGroup();
        if (this.context.getClusterManager() != null) {
            this.context.getClusterManager().setContext(this.context);
        }
    }

    /**
     * 启动
     */
    public synchronized void start() {
        int readerTimeout = context.getReaderTimeout();
        int writerTimeout = context.getWriterTimeout();

        ListenerHandler listenerHandler = new ListenerHandler(context);
        ProtocolDecoder protocolDecoder = new ProtocolDecoder(context);

        bootstrap.childHandler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel channel) {
                channel.pipeline()
                        .addLast(DEBUG_LOGGING_HANDLER)
                        .addLast(new IdleStateHandler(readerTimeout, writerTimeout, 0, TimeUnit.SECONDS))
                        .addLast(listenerHandler)
                        .addLast(protocolDecoder);
            }
        });

        int port = context.getPort();
        ChannelFuture channelFuture = bootstrap.bind(port);
        channelFuture.syncUninterruptibly();
        channel = channelFuture.channel();

        LOGGER.info("B-IM started on port(s): {}", port);
        LOGGER.info("B-IM reader timeout: {}", readerTimeout > 0 ? readerTimeout + "s" : "off");
        LOGGER.info("B-IM writer timeout: {}", writerTimeout > 0 ? writerTimeout + "s" : "off");
        LOGGER.info("B-IM cluster: {}", context.isCluster() ? "on -> " + context.getClusterManager().getClass().getName() : "off");
    }

    /**
     * 关闭
     */
    public synchronized void close() {
        try {
            channel.close();
        } catch (Exception e) {
            LOGGER.warn(e.getMessage(), e);
        }
        try {
            context.getUsers().getStrSetMap().values().forEach(sessions -> {
                if (CollUtil.isNotEmpty(sessions)) {
                    sessions.forEach(BimSession::close);
                }
            });
            context.getGroups().getStrSetMap().values().forEach(sessions -> {
                if (CollUtil.isNotEmpty(sessions)) {
                    sessions.forEach(BimSession::close);
                }
            });
        } catch (Exception e) {
            LOGGER.warn(e.getMessage(), e);
        }
        try {
            bossGroup.shutdownGracefully().syncUninterruptibly();
            workerGroup.shutdownGracefully().syncUninterruptibly();
        } catch (Exception e) {
            LOGGER.warn(e.getMessage(), e);
        }
    }

    private ServerBootstrap defaultServerBootstrap() {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(0, new DefaultThreadFactory("bim-boss"));
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(0, new DefaultThreadFactory("bim-worker"));

        return new ServerBootstrap()
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .handler(DEBUG_LOGGING_HANDLER);
    }
}
