package com.github.byakkili.bim.demo.cmd.onlineuser;

import cn.hutool.core.util.ObjectUtil;
import com.github.byakkili.bim.core.BimSession;
import com.github.byakkili.bim.core.protocol.impl.json.BaseJsonCmdHandler;
import com.github.byakkili.bim.core.protocol.impl.json.JsonMsg;
import com.github.byakkili.bim.demo.constant.Cmd;
import com.github.byakkili.bim.demo.constant.GlobalConst;
import com.github.byakkili.bim.demo.dto.SimpleMsg;
import com.github.byakkili.bim.demo.dto.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Guannian Li
 */
@Component
public class OnlineUsersHandler extends BaseJsonCmdHandler<SimpleMsg> {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public JsonMsg handle(SimpleMsg reqMsg, BimSession session) {
        String userId = session.getUserId();
        List<UserInfo> users = redisTemplate.opsForHash().entries(GlobalConst.REDIS_ONLINE_USERS).entrySet()
                .stream()
                .filter(e -> ObjectUtil.notEqual(e.getKey(), userId))
                .map(entry -> {
                    UserInfo userInfo = new UserInfo();
                    userInfo.setId(entry.getKey().toString());
                    userInfo.setNickname(entry.getValue().toString());
                    return userInfo;
                }).collect(Collectors.toList());
        return new OnlineUsersRespMsg(reqMsg.getSeq(), users, 0, "获取成功");
    }

    @Override
    public int cmd() {
        return Cmd.ONLINE_USERS_REQ;
    }
}
