package com.github.byakkili.bim.demo.config;

import com.github.byakkili.bim.core.cluster.ClusterHandler;
import com.github.byakkili.bim.core.cluster.ClusterManager;
import com.github.byakkili.bim.core.cluster.impl.redis.RedissonClusterManager;
import com.github.byakkili.bim.core.protocol.impl.json.tcp.TcpJsonProtocolProvider;
import com.github.byakkili.bim.core.protocol.impl.json.ws.WsJsonProtocolProvider;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author Guannian Li
 */
@Configuration
public class BimConfig {
    @Autowired
    private List<ClusterHandler> clusterHandlers;

    /**
     * TCP WebSocket 协议
     */
    @Bean
    public WsJsonProtocolProvider wsJsonProtocolProvider() {
        return new WsJsonProtocolProvider();
    }

    /**
     * TCP JSON 协议
     */
    @Bean
    public TcpJsonProtocolProvider tcpJsonProtocolProvider() {
        return new TcpJsonProtocolProvider();
    }

    /**
     * 集群管理器
     */
    @Bean
    public ClusterManager clusterManager(RedissonClient redissonClient) {
        RTopic rTopic = redissonClient.getTopic("B-IM -> Cluster");
        RedissonClusterManager clusterManager = new RedissonClusterManager(rTopic);
        clusterHandlers.forEach(clusterManager::addClusterHandler);
        return clusterManager;
    }
}
