package com.github.byakkili.bim.autoconfigure;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Guannian Li
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "bim")
public class BimProperties {
    /**
     * 端口
     */
    private int port = 9000;
    /**
     * 读超时(秒)
     */
    private int readerTimeout = 60;
    /**
     * 写超时(秒)
     */
    private int writerTimeout = 0;
}
