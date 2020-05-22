package com.github.byakkili.bim.core.cluster;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author Guannian Li
 */
@Setter
@Getter
@ToString
public abstract class BaseClusterPacket implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 当前的服务器ID
     */
    public static final String SERVER_ID = UUID.randomUUID().toString();
    /**
     * 服务器ID
     */
    private String fromServerId = SERVER_ID;
    /**
     * 数据
     */
    private Serializable data;
}
