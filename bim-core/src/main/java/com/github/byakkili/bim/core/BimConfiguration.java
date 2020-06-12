package com.github.byakkili.bim.core;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ConcurrentHashSet;
import com.github.byakkili.bim.core.cluster.IClusterManager;
import com.github.byakkili.bim.core.cmd.ICmdHandler;
import com.github.byakkili.bim.core.exception.BimRuntimeException;
import com.github.byakkili.bim.core.interceptor.CmdInterceptor;
import com.github.byakkili.bim.core.listener.ISessionListener;
import com.github.byakkili.bim.core.protocol.IProtocolProvider;
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
    private IClusterManager clusterManager;
    /**
     * 会话监听器
     */
    private ISessionListener sessionListener;
    /**
     * 命令拦截器
     */
    private final List<CmdInterceptor> cmdInterceptors = new CopyOnWriteArrayList<>();
    /**
     * 协议提供者列表
     */
    private final Set<IProtocolProvider> protocolProviders = new ConcurrentHashSet<>();
    /**
     * cmd处理器
     */
    private final Map<Integer, ICmdHandler> cmdHandlers = new ConcurrentHashMap<>();

    /**
     * 添加命令拦截器
     *
     * @param cmdInterceptor 命令拦截器
     */
    public void addCmdInterceptors(CmdInterceptor cmdInterceptor) {
        cmdInterceptors.add(cmdInterceptor);
        LOGGER.info("Cmd Interceptor: {}", cmdInterceptor.toString());
    }

    /**
     * 添加协议提供者
     *
     * @param protocolProvider 协议提供者
     */
    public void addProtocolProvider(IProtocolProvider protocolProvider) {
        protocolProviders.add(protocolProvider);
        LOGGER.info("Protocol: {}", protocolProvider.toString());
    }

    /**
     * 添加cmd处理器
     *
     * @param cmdHandler cmd处理器
     */
    public void addCmdHandler(ICmdHandler cmdHandler) {
        int cmd = cmdHandler.cmd();
        if (cmdHandlers.containsKey(cmd)) {
            throw new BimRuntimeException("Cmd already exists");
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
        this.protocolProviders.addAll(config.getProtocolProviders());
        this.cmdHandlers.putAll(config.getCmdHandlers());
        this.cmdInterceptors.addAll(config.getCmdInterceptors());
    }
}
