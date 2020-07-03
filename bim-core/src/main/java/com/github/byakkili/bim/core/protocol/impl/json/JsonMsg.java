package com.github.byakkili.bim.core.protocol.impl.json;

import java.io.Serializable;

/**
 * @author Guannian Li
 */
public interface JsonMsg extends Serializable {
    /**
     * 字段名
     */
    String COMMAND_FIELD = "command";

    /**
     * 指令
     *
     * @return 指令
     */
    Integer getCommand();
}
