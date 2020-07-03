package com.github.byakkili.bim.core.protocol;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 指令帧
 *
 * @author Guannian Li
 */
@Getter
@AllArgsConstructor
public class CommandFrame<T> {
    /**
     * 指令
     */
    private final Integer command;
    /**
     * Msg
     */
    private final T msg;
}
