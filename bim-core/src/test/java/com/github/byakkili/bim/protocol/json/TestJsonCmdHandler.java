package com.github.byakkili.bim.protocol.json;

import com.github.byakkili.bim.core.BimSession;

/**
 * @author Guannian Li
 */
public class TestJsonCmdHandler extends BaseJsonCmdHandler<TestJsonMsg> {
    static final Integer CMD = 1;
    static final Integer CMD_ACK = 2;

    @Override
    public JsonMsg handle(TestJsonMsg reqMsg, BimSession session) {
        reqMsg.setCmd(CMD_ACK);
        return reqMsg;
    }

    @Override
    public int cmd() {
        return CMD;
    }
}