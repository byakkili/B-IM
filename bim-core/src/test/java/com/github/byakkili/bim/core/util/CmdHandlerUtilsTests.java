package com.github.byakkili.bim.core.util;

import com.github.byakkili.bim.core.server.json.TestJsonCmdHandler;
import com.github.byakkili.bim.core.server.json.TestJsonMsg;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Guannian Li
 */
public class CmdHandlerUtilsTests {
    @Test
    public void getMsgClass() {
        TestJsonCmdHandler cmdHandler = new TestJsonCmdHandler();

        Class<Object> msgClass = CmdHandlerUtils.getMsgClass(cmdHandler);
        Class<Object> msgClass1 = CmdHandlerUtils.getMsgClass(cmdHandler);

        Assert.assertEquals(msgClass, TestJsonMsg.class);
        Assert.assertEquals(msgClass1, TestJsonMsg.class);
    }
}
