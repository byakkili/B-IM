package com.github.byakkili.bim.demo.cmd.heartbeat;

import com.github.byakkili.bim.core.BimSession;
import com.github.byakkili.bim.core.protocol.impl.json.BaseJsonCmdHandler;
import com.github.byakkili.bim.core.protocol.impl.json.JsonMsg;
import com.github.byakkili.bim.demo.constant.Cmd;
import com.github.byakkili.bim.demo.dto.AckMsg;
import com.github.byakkili.bim.demo.dto.SimpleMsg;
import org.springframework.stereotype.Component;

/**
 * @author Guannian Li
 */
@Component
public class HeartBeatCmdHandler extends BaseJsonCmdHandler<SimpleMsg> {
    @Override
    protected JsonMsg process(SimpleMsg reqMsg, BimSession session) {
        return new AckMsg(Cmd.PONG, reqMsg.getSeq(), 0, "PONG");
    }

    @Override
    public int cmd() {
        return Cmd.PING;
    }

    @Override
    public Class<SimpleMsg> reqMsgClass() {
        return SimpleMsg.class;
    }
}
