package com.github.byakkili.bim.demo.constant;

import cn.hutool.core.util.ObjectUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Guannian Li
 */
@Getter
@AllArgsConstructor
public enum ChatMsgType {
    /**
     * 文本
     */
    TEXT(1),
    /**
     * 图片
     */
    IMAGE(2),
    /**
     * 语音
     */
    VOICE(3),
    /**
     * 视频
     */
    VIDEO(4);

    private int code;

    public static ChatMsgType of(Integer code) {
        for (ChatMsgType type : ChatMsgType.values()) {
            if (ObjectUtil.equal(type.getCode(), code)) {
                return type;
            }
        }
        return null;
    }
}
