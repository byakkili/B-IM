package com.github.byakkili.bim.core.util;

import com.github.byakkili.bim.core.server.json.TestJsonCommandHandler;
import com.github.byakkili.bim.core.server.json.TestJsonMsg;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Guannian Li
 */
public class CommandHandlerUtilsTest {
    @Test
    public void getMsgClass() {
        TestJsonCommandHandler commandHandler = new TestJsonCommandHandler();

        Class<Object> msgClass = CommandHandlerUtils.getMsgClass(commandHandler);
        Class<Object> msgClass1 = CommandHandlerUtils.getMsgClass(commandHandler);

        Assert.assertEquals(msgClass, TestJsonMsg.class);
        Assert.assertEquals(msgClass1, TestJsonMsg.class);
    }
}
