package com.github.byakkili.bim.core.json;

import com.github.byakkili.bim.core.BimSession;
import com.github.byakkili.bim.core.protocol.impl.json.BaseJsonCmdHandler;

/**
 * @author Guannian Li
 */
public class TestJsonCmdHandler extends BaseJsonCmdHandler<TestJsonMsg> {
    static final Integer CMD = 1;
    static final Integer CMD_ACK = 2;

    @Override
    public com.github.byakkili.bim.core.protocol.impl.json.JsonMsg handle(TestJsonMsg reqMsg, BimSession session) {
        reqMsg.setCmd(CMD_ACK);
        return reqMsg;
    }

    @Override
    public int cmd() {
        return CMD;
    }
}