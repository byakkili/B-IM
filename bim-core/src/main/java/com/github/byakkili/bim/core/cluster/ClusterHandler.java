package com.github.byakkili.bim.core.cluster;

import com.github.byakkili.bim.core.BimContext;

/**
 * @author Guannian Li
 */
public interface ClusterHandler<T extends BaseClusterPacket> {
    /**
     * 数据包名
     *
     * @return 数据包名
     */
    Class<T> packetClass();

    /**
     * 处理
     *
     * @param clusterPacket 数据包
     * @param context       B-IM上下文
     */
    void handle(T clusterPacket, BimContext context);
}
