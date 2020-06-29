package com.github.byakkili.bim.demo.dto;

import com.github.byakkili.bim.core.protocol.impl.json.JsonMsg;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Guannian Li
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AckMsg implements JsonMsg {
    private static final long serialVersionUID = 1L;
    /**
     * CMD
     */
    private Integer cmd;
    /**
     * 序列号
     */
    private Long seq;
    /**
     * Status
     */
    private Integer status;
    /**
     * MSG
     */
    private String msg;
}
