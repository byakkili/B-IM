package com.github.byakkili.bim.core.cluster;

import com.github.byakkili.bim.core.BimContext;

/**
 * @author Guannian Li
 */
public interface ClusterManager {
    /**
     * 设置B-IM上下文
     *
     * @param context B-IM上下文
     */
    void setContext(BimContext context);

    /**
     * 发送包到集群
     *
     * @param clusterPacket 数据包
     * @return 是否成功
     */
    boolean send(BaseClusterPacket clusterPacket);
}
