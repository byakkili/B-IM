package com.github.byakkili.bim.core.protocol.impl.json;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import com.github.byakkili.bim.core.protocol.BaseCmdChannelHandler;
import io.netty.channel.ChannelHandler.Sharable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * CMD处理器
 *
 * @author Guannian Li
 */
@Sharable
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonCmdChannelHandler extends BaseCmdChannelHandler<Map, JsonMsg, JsonMsg> {
    /**
     * 单例
     */
    public static final JsonCmdChannelHandler INSTANCE = new JsonCmdChannelHandler();

    @Override
    protected Integer getCmd(Map msg) {
        return MapUtil.getInt(msg, JsonMsg.CMD_FIELD);
    }

    @Override
    protected JsonMsg convert(Map msg, Class<JsonMsg> targetClass) {
        return BeanUtil.mapToBean(msg, targetClass, false);
    }
}