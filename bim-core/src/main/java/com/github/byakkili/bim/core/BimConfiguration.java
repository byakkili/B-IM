package com.github.byakkili.bim.core;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ConcurrentHashSet;
import com.github.byakkili.bim.core.cluster.ClusterManager;
import com.github.byakkili.bim.core.command.CommandHandler;
import com.github.byakkili.bim.core.interceptor.CommandInterceptor;
import com.github.byakkili.bim.core.listener.SessionListener;
import com.github.byakkili.bim.core.protocol.ProtocolProvider;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Guannian Li
 */
@Setter
@Getter
@NoArgsConstructor
public class BimConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(BimConfiguration.class);
    /**
     * 端口
     */
    private int port;
    /**
     * 读超时(秒)
     */
    private int readerTimeout;
    /**
     * 写超时(秒)
     */
    private int writerTimeout;
    /**
     * 集群
     */
    private ClusterManager clusterManager;
    /**
     * 会话监听器
     */
    private final List<SessionListener> sessionListeners = new CopyOnWriteArrayList<>();
    /**
     * 指令拦截器
     */
    private final List<CommandInterceptor> commandInterceptors = new CopyOnWriteArrayList<>();
    /**
     * 协议提供者
     */
    private final Set<ProtocolProvider> protocolProviders = new ConcurrentHashSet<>();
    /**
     * 指令处理器
     */
    private final Map<Integer, CommandHandler> commandHandlers = new ConcurrentHashMap<>();

    /**
     * 添加会话监听器
     *
     * @param sessionListener 会话监听器
     */
    public void addSessionListener(SessionListener sessionListener) {
        sessionListeners.add(sessionListener);
        LOGGER.info("Session listener: {}", sessionListeners.toString());
    }

    /**
     * 添加指令拦截器
     *
     * @param commandInterceptor 指令拦截器
     */
    public void addCommandInterceptor(CommandInterceptor commandInterceptor) {
        commandInterceptors.add(commandInterceptor);
        LOGGER.info("Command interceptor: {}", commandInterceptor.toString());
    }

    /**
     * 添加协议提供者
     *
     * @param protocolProvider 协议提供者
     */
    public void addProtocolProvider(ProtocolProvider protocolProvider) {
        protocolProviders.add(protocolProvider);
        LOGGER.info("Protocol: {}", protocolProvider.toString());
    }

    /**
     * 添加指令处理器
     *
     * @param commandHandler 指令处理器
     */
    public void addCommandHandler(CommandHandler commandHandler) {
        int command = commandHandler.command();
        if (commandHandlers.containsKey(command)) {
            throw new IllegalArgumentException("Command already exists");
        }
        commandHandlers.put(command, commandHandler);
        LOGGER.info("Command Handler: {} -> {}", command, commandHandler.toString());
    }

    /**
     * 是否为集群
     *
     * @return 是否
     */
    public boolean isCluster() {
        return clusterManager != null;
    }

    /**
     * 构造函数
     *
     * @param config 配置
     */
    BimConfiguration(BimConfiguration config) {
        BeanUtil.copyProperties(config, this);
        this.commandHandlers.putAll(config.getCommandHandlers());
        this.commandInterceptors.addAll(config.getCommandInterceptors());
        this.sessionListeners.addAll(config.getSessionListeners());
        this.protocolProviders.addAll(config.getProtocolProviders());
    }
}
