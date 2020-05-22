package com.github.byakkili.bim.spring;

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
    private int port = 9000;
    private int readerTimeout = 60;
    private int writerTimeout = 0;
}
