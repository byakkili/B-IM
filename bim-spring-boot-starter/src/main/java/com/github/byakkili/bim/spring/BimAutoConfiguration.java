package com.github.byakkili.bim.spring;

import cn.hutool.core.collection.CollUtil;
import com.github.byakkili.bim.core.BimConfiguration;
import com.github.byakkili.bim.core.BimServerBootstrap;
import com.github.byakkili.bim.core.cluster.IClusterManager;
import com.github.byakkili.bim.core.cmd.ICmdHandler;
import com.github.byakkili.bim.core.interceptor.CmdInterceptor;
import com.github.byakkili.bim.core.listener.ISessionListener;
import com.github.byakkili.bim.core.protocol.IProtocolProvider;
import io.netty.bootstrap.ServerBootstrap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

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
    private ServerBootstrap serverBootstrap;
    @Autowired(required = false)
    private IClusterManager clusterManager;
    @Autowired(required = false)
    private ISessionListener sessionListener;
    @Autowired(required = false)
    private List<ICmdHandler> cmdHandlers;
    @Autowired(required = false)
    private List<CmdInterceptor> cmdInterceptors;
    @Autowired(required = false)
    private List<IProtocolProvider> protocolProviders;

    @Bean
    public BimConfiguration configuration() {
        Assert.notEmpty(cmdHandlers, "Cmd handler not found.");
        Assert.notEmpty(protocolProviders, "Protocol provider not found.");

        BimConfiguration config = new BimConfiguration();
        config.setPort(properties.getPort());
        config.setReaderTimeout(properties.getReaderTimeout());
        config.setWriterTimeout(properties.getWriterTimeout());
        config.setClusterManager(clusterManager);
        config.setSessionListener(sessionListener);
        config.getCmdInterceptors().addAll(CollUtil.emptyIfNull(cmdInterceptors));
        cmdHandlers.forEach(config::addCmdHandler);
        protocolProviders.forEach(config::addProtocolProvider);
        return config;
    }

    @Bean(initMethod = "start", destroyMethod = "close")
    public BimServerBootstrap serverBootstrap() {
        BimServerBootstrap bimServerBootstrap = new BimServerBootstrap(configuration());
        if (serverBootstrap != null) {
            bimServerBootstrap.setBootstrap(serverBootstrap);
        }
        return bimServerBootstrap;
    }
}
