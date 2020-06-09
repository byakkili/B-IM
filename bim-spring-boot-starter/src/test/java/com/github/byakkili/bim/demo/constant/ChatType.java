package com.github.byakkili.bim.demo.constant;

import cn.hutool.core.util.ObjectUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Guannian Li
 */
@Getter
@AllArgsConstructor
public enum ChatType {
    /**
     * 单聊
     */
    PRIVATE(1),
    /**
     * 群聊
     */
    GROUP(2);

    private int code;

    public static ChatType of(Integer code) {
        for (ChatType type : ChatType.values()) {
            if (ObjectUtil.equal(type.getCode(), code)) {
                return type;
            }
        }
        return null;
    }
}
