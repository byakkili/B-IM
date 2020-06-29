package com.github.byakkili.bim.core;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ConcurrentHashSet;
import com.github.byakkili.bim.core.cluster.ClusterManager;
import com.github.byakkili.bim.core.cmd.CmdHandler;
import com.github.byakkili.bim.core.interceptor.CmdInterceptor;
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
     * 命令拦截器
     */
    private final List<CmdInterceptor> cmdInterceptors = new CopyOnWriteArrayList<>();
    /**
     * 协议提供者
     */
    private final Set<ProtocolProvider> protocolProviders = new ConcurrentHashSet<>();
    /**
     * cmd处理器
     */
    private final Map<Integer, CmdHandler> cmdHandlers = new ConcurrentHashMap<>();

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
     * 添加命令拦截器
     *
     * @param cmdInterceptor 命令拦截器
     */
    public void addCmdInterceptor(CmdInterceptor cmdInterceptor) {
        cmdInterceptors.add(cmdInterceptor);
        LOGGER.info("Cmd interceptor: {}", cmdInterceptor.toString());
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
     * 添加cmd处理器
     *
     * @param cmdHandler cmd处理器
     */
    public void addCmdHandler(CmdHandler cmdHandler) {
        int cmd = cmdHandler.cmd();
        if (cmdHandlers.containsKey(cmd)) {
            throw new IllegalArgumentException("Cmd already exists");
        } else {
            cmdHandlers.put(cmd, cmdHandler);
            LOGGER.info("Cmd Handler: {} -> {}", cmd, cmdHandler.toString());
        }
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
        this.cmdHandlers.putAll(config.getCmdHandlers());
        this.cmdInterceptors.addAll(config.getCmdInterceptors());
        this.sessionListeners.addAll(config.getSessionListeners());
        this.protocolProviders.addAll(config.getProtocolProviders());
    }
}
