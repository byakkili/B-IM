package com.github.byakkili.bim.core.util;

import com.github.byakkili.bim.core.BimContext;
import com.github.byakkili.bim.core.BimSession;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author Guannian Li
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BimSessionUtils {
    /**
     * Session Key
     */
    private static final AttributeKey<Object> SESSION_ATTR = AttributeKey.valueOf("bim_session_key");

    /**
     * 获取会话
     *
     * @param channel 通道
     * @return 会话
     */
    public static BimSession get(Channel channel) {
        return (BimSession) channel.attr(SESSION_ATTR).get();
    }

    /**
     * 初始化会话
     *
     * @param channel 通道
     * @param context 上下文
     * @return 会话
     */
    public static BimSession init(Channel channel, BimContext context) {
        BimSession session = get(channel);
        if (session == null) {
            session = new BimSession();
            session.setId(channel.id().asShortText());
            session.setChannel(channel);
            session.setContext(context);

            Object prevObj = channel.attr(SESSION_ATTR).setIfAbsent(session);
            if (prevObj != null) {
                return (BimSession) prevObj;
            }
        }
        return session;
    }
}
