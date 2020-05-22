package com.github.byakkili.bim.core.cluster;

import com.github.byakkili.bim.core.BimContext;

/**
 * @author Guannian Li
 */
public interface IClusterHandler<T extends BaseClusterPacket> {
    /**
     * 数据包名
     *
     * @return 数据包名
     */
    Class<T> packetClass();

    /**
     * 处理
     *
     * @param context       B-IM上下文
     * @param clusterPacket 数据包
     */
    void handle(BimContext context, T clusterPacket);
}
