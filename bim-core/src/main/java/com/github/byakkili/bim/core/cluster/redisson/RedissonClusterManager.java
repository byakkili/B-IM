package com.github.byakkili.bim.core.cluster.redisson;

import cn.hutool.core.util.ObjectUtil;
import com.github.byakkili.bim.core.cluster.BaseClusterManager;
import com.github.byakkili.bim.core.cluster.BaseClusterPacket;
import org.redisson.api.RTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 集群管理器的Redisson实现
 *
 * @author Guannian Li
 */
public class RedissonClusterManager extends BaseClusterManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedissonClusterManager.class);
    /**
     * rTopic
     */
    private RTopic rTopic;

    public RedissonClusterManager(RTopic rTopic) {
        this.rTopic = rTopic;
        this.rTopic.addListener(BaseClusterPacket.class, (channel, msg) -> {
            if (ObjectUtil.notEqual(BaseClusterPacket.SERVER_ID, msg.getFromServerId())) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("接收集群包, {}", msg);
                }
                receive(msg);
            }
        });
    }

    @Override
    public boolean send(BaseClusterPacket clusterPacket) {
        long clientNumber = rTopic.publish(clusterPacket);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("发送到集群, 客户端接收数[{}]", clientNumber);
        }
        return true;
    }
}
