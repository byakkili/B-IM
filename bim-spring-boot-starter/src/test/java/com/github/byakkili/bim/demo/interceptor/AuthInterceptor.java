package com.github.byakkili.bim.demo.interceptor;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.BooleanUtil;
import com.github.byakkili.bim.core.BimSession;
import com.github.byakkili.bim.core.interceptor.CmdInterceptor;
import com.github.byakkili.bim.core.protocol.CmdMsgFrame;
import com.github.byakkili.bim.core.util.JsonUtils;
import com.github.byakkili.bim.demo.constant.Cmd;
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
public class AuthInterceptor implements CmdInterceptor {
    private Set<Integer> skiptCmds = CollUtil.newHashSet(Cmd.AUTH_REQ, Cmd.PING);

    @Override
    public boolean preHandle(Integer cmd, CmdMsgFrame reqFrame, BimSession session) {
        Boolean login = (Boolean) session.getAttribute("isLogin");

        if (!skiptCmds.contains(cmd) && !BooleanUtil.isTrue(login)) {
            log.info("会话: {}, 拒绝请求, {}", session.getChannel().id().asShortText(), JsonUtils.stringify(reqFrame.getMsg()));

            Object seq = BeanUtil.getFieldValue(reqFrame.getMsg(), "seq");
            session.writeAndFlush(new AckMsg(Cmd.AUTH_RESP, Convert.toLong(seq), -1, "请先登录"));
            return false;
        }
        return true;
    }
}
