package com.github.byakkili.bim.core.protocol.impl.json;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import com.github.byakkili.bim.core.BimContext;
import com.github.byakkili.bim.core.BimSession;
import com.github.byakkili.bim.core.cmd.CmdHandler;
import com.github.byakkili.bim.core.protocol.CmdMsgFrame;
import com.github.byakkili.bim.core.util.CmdHandlerUtils;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.Map;

/**
 * @author Guannian Li
 */
public abstract class BaseJsonCodec<T> extends MessageToMessageCodec<T, CmdMsgFrame<JsonMsg>> {

    protected CmdMsgFrame<JsonMsg> decodeToFrame(BimSession session, Map jsonMap) {
        BimContext context = session.getContext();
        Map<Integer, CmdHandler> cmdHandlers = context.getCmdHandlers();

        Integer cmd = MapUtil.getInt(jsonMap, JsonMsg.CMD_FIELD);
        if (cmd == null) {
            return null;
        }
        CmdHandler cmdHandler = cmdHandlers.get(cmd);
        if (cmdHandler == null) {
            return new CmdMsgFrame<>(cmd, null);
        }
        Class<JsonMsg> msgClass = CmdHandlerUtils.getMsgClass(cmdHandler);
        JsonMsg jsonMsg = BeanUtil.mapToBean(jsonMap, msgClass, true);

        return new CmdMsgFrame<>(cmd, jsonMsg);
    }
}
