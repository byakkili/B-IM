package com.github.byakkili.bim.demo.command.onlineuser;

import com.github.byakkili.bim.core.protocol.impl.json.JsonMsg;
import com.github.byakkili.bim.demo.dto.UserInfo;
import com.github.byakkili.bim.demo.constant.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author Guannian Li
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
public class OnlineUsersRespMsg implements JsonMsg {
    private static final long serialVersionUID = 1L;
    /**
     * command
     */
    private final Integer command = Command.ONLINE_USERS_RESP;
    /**
     * 序列号
     */
    private Long seq;
    /**
     * 在线用户列表
     */
    private List<UserInfo> users;
    /**
     * Status
     */
    private Integer status;
    /**
     * MSG
     */
    private String msg;
}
