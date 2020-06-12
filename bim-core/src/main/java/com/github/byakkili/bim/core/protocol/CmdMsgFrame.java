package com.github.byakkili.bim.core.protocol;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Guannian Li
 */
@Getter
@AllArgsConstructor
public class CmdMsgFrame<T> {
    /**
     * CMD
     */
    private final Integer cmd;
    /**
     * Msg
     */
    private final T msg;
}
