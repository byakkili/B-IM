package com.github.byakkili.bim.demo.listener;

import cn.hutool.core.util.StrUtil;
import com.github.byakkili.bim.core.BimSession;
import com.github.byakkili.bim.core.listener.ISessionListener;
import com.github.byakkili.bim.core.protocol.CmdMsgFrame;
import com.github.byakkili.bim.core.util.JsonUtils;
import com.github.byakkili.bim.demo.constant.GlobalConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author Guannian Li
 */
@Slf4j
@Component
public class SessionListener implements ISessionListener {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void onAfterCreated(BimSession session) {
        log.info("会话: {}, 已创建", session.getChannel().id().asShortText());
    }

    @Override
    public void onBeforeDestroy(BimSession session) {
        log.info("会话: {}, 准备销毁", session.getChannel().id().asShortText());
        String userId = session.getUserId();
        if (StrUtil.isNotBlank(userId)) {
            redisTemplate.opsForHash().delete(GlobalConst.REDIS_ONLINE_USERS, userId);
        }
    }

    @Override
    public void onReaderIdle(BimSession session) {
        log.info("会话: {}, 读超时, {}s", session.getChannel().id().asShortText(), session.getContext().getReaderTimeout());
        // 关闭客户端
        session.getChannel().close();
    }

    @Override
    public void onWriterIdle(BimSession session) {
        log.info("会话: {}, 写超时, {}s", session.getChannel().id().asShortText(), session.getContext().getWriterTimeout());
    }

    @Override
    public void onRead(CmdMsgFrame frame, BimSession session) {
        log.info("会话: {}, 请求: {}", session.getChannel().id().asShortText(), JsonUtils.stringify(frame.getMsg()));
    }

    @Override
    public void onWrite(CmdMsgFrame frame, BimSession session) {
        log.info("会话: {}, 响应: {}", session.getChannel().id().asShortText(), JsonUtils.stringify(frame.getMsg()));
    }
}
