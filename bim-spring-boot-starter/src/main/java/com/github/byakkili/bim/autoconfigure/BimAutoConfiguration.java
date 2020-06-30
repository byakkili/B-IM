package com.github.byakkili.bim.autoconfigure;

import cn.hutool.core.collection.CollUtil;
import com.github.byakkili.bim.core.BimConfiguration;
import com.github.byakkili.bim.core.BimNettyServer;
import com.github.byakkili.bim.core.cluster.ClusterManager;
import com.github.byakkili.bim.core.cmd.CmdHandler;
import com.github.byakkili.bim.core.interceptor.CmdInterceptor;
import com.github.byakkili.bim.core.listener.SessionListener;
import com.github.byakkili.bim.core.protocol.ProtocolProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author Guannian Li
 */
@Configuration
@EnableConfigurationProperties(BimProperties.class)
public class BimAutoConfiguration {
    @Autowired
    private BimProperties properties;

    @Autowired(required = false)
    private ClusterManager clusterManager;
    @Autowired(required = false)
    private List<CmdInterceptor> cmdInterceptors;
    @Autowired(required = false)
    private List<SessionListener> sessionListeners;

    @Bean
    public BimConfiguration configuration(List<CmdHandler> cmdHandlers, List<ProtocolProvider> protocolProviders) {
        BimConfiguration config = new BimConfiguration();
        config.setPort(properties.getPort());
        config.setReaderTimeout(properties.getReaderTimeout());
        config.setWriterTimeout(properties.getWriterTimeout());
        config.setClusterManager(clusterManager);

        cmdHandlers.forEach(config::addCmdHandler);
        protocolProviders.forEach(config::addProtocolProvider);

        CollUtil.emptyIfNull(cmdInterceptors).forEach(config::addCmdInterceptor);
        CollUtil.emptyIfNull(sessionListeners).forEach(config::addSessionListener);

        return config;
    }

    @Bean(initMethod = "start", destroyMethod = "close")
    public BimNettyServer bimNettyServer(BimConfiguration configuration) {
        return new BimNettyServer(configuration);
    }

    @Bean
    public BimCloseLifecycle bimLifecycle(BimNettyServer nettyServer) {
        BimCloseLifecycle bimCloseLifecycle = new BimCloseLifecycle();
        bimCloseLifecycle.setBimNettyServer(nettyServer);
        return bimCloseLifecycle;
    }
}
