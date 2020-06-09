package com.github.byakkili.bim.demo.cmd.auth;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.github.byakkili.bim.core.BimSession;
import com.github.byakkili.bim.core.protocol.impl.json.BaseJsonCmdHandler;
import com.github.byakkili.bim.core.protocol.impl.json.JsonMsg;
import com.github.byakkili.bim.demo.constant.GlobalConst;
import com.github.byakkili.bim.demo.constant.Cmd;
import com.github.byakkili.bim.demo.dto.AckMsg;
import com.github.byakkili.bim.demo.util.NameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author Guannian Li
 */
@Component
public class AuthCmdHandler extends BaseJsonCmdHandler<AuthReqMsg> {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    protected JsonMsg handle(AuthReqMsg reqMsg, BimSession session) {
        // 验证Token不能为空
        if (StrUtil.isBlank(reqMsg.getToken())) {
            session.writeAndFlush(new AckMsg(Cmd.AUTH_RESP, reqMsg.getSeq(), -1, "Token不能为空"));
            return null;
        }

        String userId = IdUtil.objectId();
        String nickname = NameUtils.randomNickname();
        // 保存到redis
        redisTemplate.opsForHash().put(GlobalConst.REDIS_ONLINE_USERS, userId, nickname);
        // 绑定之前确保会话还没绑定
        session.unbind();
        // 设置会话的用户信息
        session.setToken(reqMsg.getToken());
        session.setUserId(userId);
        session.setGroupIds(CollUtil.newHashSet(GlobalConst.ALL_USER_GROUP));
        session.setAttribute("isLogin", true);
        // 绑定会话
        session.bind();
        return new AckMsg(Cmd.AUTH_RESP, reqMsg.getSeq(), 0, "登录成功");
    }

    @Override
    public int cmd() {
        return Cmd.AUTH_REQ;
    }

    @Override
    public Class<AuthReqMsg> reqMsgClass() {
        return AuthReqMsg.class;
    }

}
