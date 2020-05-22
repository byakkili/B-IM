package com.github.byakkili.bim.core;

import com.github.byakkili.bim.core.listener.ListenerHandler;
import com.github.byakkili.bim.core.protocol.ProtocolDecoder;
import io.netty.bootstrap.ServerBootstrap;
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
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Guannian Li
 */
@Setter
@Getter
public class BimServerBootstrap {
    private static final Logger LOGGER = LoggerFactory.getLogger(BimServerBootstrap.class);
    private static final LoggingHandler DEBUG_LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);
    private static final LoggingHandler TRACE_LOGGING_HANDLER = new LoggingHandler(LogLevel.TRACE);
    /**
     * B-IM上下文
     */
    private final BimContext context;
    /**
     * Netty Server Bootstrap
     */
    private ServerBootstrap bootstrap;

    public BimServerBootstrap(BimConfiguration config) {
        this.context = new BimContext(config);
        this.bootstrap = defaultServerBootstrap();
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
                channel.pipeline().addLast(TRACE_LOGGING_HANDLER);
                channel.pipeline().addLast(new IdleStateHandler(readerTimeout, writerTimeout, 0));
                channel.pipeline().addLast(listenerHandler);
                channel.pipeline().addLast(protocolDecoder);
            }
        });

        int port = context.getPort();
        ChannelFuture channelFuture = bootstrap.bind(port).syncUninterruptibly();
        channelFuture.channel().closeFuture().addListener(future -> close());

        LOGGER.info("B-IM started on port(s): {}", port);
        LOGGER.info("B-IM reader timeout: {}", readerTimeout > 0 ? readerTimeout + "s" : "off");
        LOGGER.info("B-IM writer timeout: {}", writerTimeout > 0 ? writerTimeout + "s" : "off");
        LOGGER.info("B-IM cluster: {}", context.isCluster() ? "on -> " + context.getClusterManager().getClass().getName() : "off");
    }

    /**
     * 关闭
     */
    private synchronized void close() {
        EventLoopGroup bossGroup = bootstrap.config().group();
        EventLoopGroup workerGroup = bootstrap.config().childGroup();
        if (bossGroup != null && !bossGroup.isShuttingDown() && !bossGroup.isShutdown()) {
            bossGroup.shutdownGracefully();
        }
        if (workerGroup != null && !workerGroup.isShuttingDown() && !workerGroup.isShutdown()) {
            workerGroup.shutdownGracefully();
        }
    }

    private ServerBootstrap defaultServerBootstrap() {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(0, new DefaultThreadFactory("bim-boss"));
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(0, new DefaultThreadFactory("bim-worker"));
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup);
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.childOption(ChannelOption.TCP_NODELAY, true);
        serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        serverBootstrap.handler(DEBUG_LOGGING_HANDLER);
        return serverBootstrap;
    }
}
