package com.github.byakkili.bim.core.cluster;

import cn.hutool.core.collection.CollUtil;
import com.github.byakkili.bim.core.BimContext;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Guannian Li
 */
@Setter
@Getter
public abstract class BaseClusterManager implements ClusterManager {
    /**
     * B-IM 上下文
     */
    private BimContext context;
    /**
     * 集群处理器
     */
    private final Map<String, ClusterHandler> clusterHandlers = new ConcurrentHashMap<>();

    /**
     * 添加集群处理器
     *
     * @param clusterHandler 集群处理器
     */
    public void addClusterHandler(ClusterHandler clusterHandler) {
        if (clusterHandler != null) {
            getClusterHandlers().put(clusterHandler.packetClass().getName(), clusterHandler);
        }
    }

    /**
     * 接收集群数据包
     *
     * @param packet 数据包
     */
    @SuppressWarnings("unchecked")
    protected void receive(BaseClusterPacket packet) {
        if (CollUtil.isEmpty(clusterHandlers) || packet == null) {
            return;
        }
        ClusterHandler handler = clusterHandlers.get(packet.getClass().getName());
        if (handler != null) {
            handler.handle(packet, context);
        }
    }
}
