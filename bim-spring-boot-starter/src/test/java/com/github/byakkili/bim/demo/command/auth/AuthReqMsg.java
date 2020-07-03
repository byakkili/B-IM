package com.github.byakkili.bim.demo.command.auth;

import com.github.byakkili.bim.core.protocol.impl.json.JsonMsg;
import com.github.byakkili.bim.demo.constant.Command;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Guannian Li
 */
@Setter
@Getter
@ToString
public class AuthReqMsg implements JsonMsg {
    private static final long serialVersionUID = 1L;
    /**
     * 指令
     */
    private final Integer command = Command.AUTH_REQ;
    /**
     * 序列号
     */
    private Long seq;
    /**
     * Token
     */
    private String token;
}
