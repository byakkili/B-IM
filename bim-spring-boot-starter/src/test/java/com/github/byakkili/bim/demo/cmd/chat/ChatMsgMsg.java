package com.github.byakkili.bim.demo.cmd.chat;

import cn.hutool.core.bean.BeanUtil;
import com.github.byakkili.bim.demo.constant.Cmd;
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
     * CMD
     */
    private final Integer cmd = Cmd.CHAT_MSG;
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
