package com.github.byakkili.bim.demo.interceptor;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import com.github.byakkili.bim.core.BimSession;
import com.github.byakkili.bim.core.interceptor.CommandInterceptor;
import com.github.byakkili.bim.demo.constant.Command;
import com.github.byakkili.bim.demo.dto.AckMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author Guannian Li
 */
@Slf4j
@Order(2)
@Component
public class AuthInterceptor implements CommandInterceptor {
    private Set<Integer> skipCommands = CollUtil.newHashSet(Command.AUTH_REQ, Command.PING);

    @Override
    public boolean preHandle(int command, BimSession session) {
        Boolean login = (Boolean) session.getAttribute("isLogin");

        if (!skipCommands.contains(command) && !BooleanUtil.isTrue(login)) {
            log.info("会话: {}, 拒绝请求", session.getId());

            session.writeAndFlush(new AckMsg(Command.AUTH_RESP, null, -1, "请先登录"));
            return false;
        }
        return true;
    }
}
