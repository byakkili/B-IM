package com.github.byakkili.bim.core.protocol;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author Guannian Li
 */
@Getter
@AllArgsConstructor
public class CmdMsgFrame<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * CMD
     */
    private final Integer cmd;
    /**
     * Msg
     */
    private final T msg;
}
