package com.github.byakkili.bim.core.util;

import com.github.byakkili.bim.core.json.TestJsonCmdHandler;
import com.github.byakkili.bim.core.json.TestJsonMsg;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Guannian Li
 */
public class CmdHandlerUtilsTest {
    @Test
    public void getMsgClass() {
        TestJsonCmdHandler cmdHandler = new TestJsonCmdHandler();

        Class<Object> msgClass = CmdHandlerUtils.getMsgClass(cmdHandler);
        Class<Object> msgClass1 = CmdHandlerUtils.getMsgClass(cmdHandler);

        Assert.assertEquals(msgClass, TestJsonMsg.class);
        Assert.assertEquals(msgClass1, TestJsonMsg.class);
    }
}
