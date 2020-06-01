package com.github.byakkili.bim.core.protocol.impl.json;

import java.io.Serializable;

/**
 * @author Guannian Li
 */
public interface JsonMsg extends Serializable {
    /**
     * 字段名
     */
    String CMD_FIELD = "cmd";

    /**
     * Command
     *
     * @return CMD
     */
    Integer getCmd();
}
