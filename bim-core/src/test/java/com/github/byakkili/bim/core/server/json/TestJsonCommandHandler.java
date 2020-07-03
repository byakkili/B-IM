package com.github.byakkili.bim.core.server.json;

import com.github.byakkili.bim.core.BimSession;
import com.github.byakkili.bim.core.protocol.impl.json.BaseJsonCommandHandler;
import com.github.byakkili.bim.core.protocol.impl.json.JsonMsg;

/**
 * @author Guannian Li
 */
public class TestJsonCommandHandler extends BaseJsonCommandHandler<TestJsonMsg> {
    static final Integer COMMAND = 1;
    static final Integer COMMAND_ACK = 2;

    @Override
    public JsonMsg handle(TestJsonMsg reqMsg, BimSession session) {
        reqMsg.setCommand(COMMAND_ACK);
        return reqMsg;
    }

    @Override
    public int command() {
        return COMMAND;
    }
}