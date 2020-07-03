package com.github.byakkili.bim.demo.command.heartbeat;

import com.github.byakkili.bim.core.BimSession;
import com.github.byakkili.bim.core.protocol.impl.json.BaseJsonCommandHandler;
import com.github.byakkili.bim.core.protocol.impl.json.JsonMsg;
import com.github.byakkili.bim.demo.constant.Command;
import com.github.byakkili.bim.demo.dto.AckMsg;
import com.github.byakkili.bim.demo.dto.SimpleMsg;
import org.springframework.stereotype.Component;

/**
 * @author Guannian Li
 */
@Component
public class HeartBeatCommandHandler extends BaseJsonCommandHandler<SimpleMsg> {
    @Override
    public JsonMsg handle(SimpleMsg reqMsg, BimSession session) {
        return new AckMsg(Command.PONG, reqMsg.getSeq(), 0, "PONG");
    }

    @Override
    public int command() {
        return Command.PING;
    }
}
