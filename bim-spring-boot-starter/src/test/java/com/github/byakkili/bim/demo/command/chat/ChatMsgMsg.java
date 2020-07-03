package com.github.byakkili.bim.demo.command.chat;

import cn.hutool.core.bean.BeanUtil;
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
public class ChatMsgMsg extends ChatReqMsg {
    private static final long serialVersionUID = 1L;
    /**
     * 指令
     */
    private final Integer command = Command.CHAT_MSG;
    /**
     * 来源用户Id
     */
    private String from;

    /**
     * 发送时间戳 (ms)
     */
    private Long createTime;

    public ChatMsgMsg(ChatReqMsg chatReqMsg, Long seq, String from) {
        BeanUtil.copyProperties(chatReqMsg, this);
        setSeq(seq);
        this.from = from;
        this.createTime = System.currentTimeMillis();
    }
}
