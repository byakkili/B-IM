package com.github.byakkili.bim.demo.command.chat;

import com.github.byakkili.bim.core.protocol.impl.json.JsonMsg;
import com.github.byakkili.bim.demo.constant.ChatMsgType;
import com.github.byakkili.bim.demo.constant.Command;
import com.github.byakkili.bim.demo.constant.ChatType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Guannian Li
 */
@Setter
@Getter
@ToString
public class ChatReqMsg implements JsonMsg {
    private static final long serialVersionUID = 1L;
    /**
     * 指令
     */
    private final Integer command = Command.CHAT_REQ;
    /**
     * 序列号
     */
    private Long seq;
    /**
     * 目标Id (私聊:用户Id,群聊:群Id)
     */
    private String to;
    /**
     * 聊天类型
     *
     * @see ChatType
     */
    private Integer chatType;
    /**
     * 信息类型
     *
     * @see ChatMsgType
     */
    private Integer msgType;
    /**
     * 内容
     */
    private String content;
}
