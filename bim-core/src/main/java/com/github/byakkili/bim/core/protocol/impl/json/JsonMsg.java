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
    String SEQ_FIELD = "seq";

    /**
     * Command
     *
     * @return CMD
     */
    Integer getCmd();

    /**
     * 消息序列, 区分每一次的请求
     *
     * @return Long
     */
    Long getSeq();
}
