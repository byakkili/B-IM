package com.github.byakkili.bim.core.protocol.impl.json;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import com.github.byakkili.bim.core.BimContext;
import com.github.byakkili.bim.core.BimSession;
import com.github.byakkili.bim.core.cmd.ICmdHandler;
import com.github.byakkili.bim.core.protocol.CmdMsgFrame;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.Map;

/**
 * @author Guannian Li
 */
public abstract class BaseJsonCodec<T> extends MessageToMessageCodec<T, CmdMsgFrame<JsonMsg>> {

    protected CmdMsgFrame<JsonMsg> decodeToFrame(BimSession session, Map jsonMap) {
        BimContext context = session.getContext();
        Map<Integer, ICmdHandler> cmdHandlers = context.getCmdHandlers();

        Integer cmd = MapUtil.getInt(jsonMap, JsonMsg.CMD_FIELD);
        if (cmd == null) {
            return null;
        }
        ICmdHandler cmdHandler = cmdHandlers.get(cmd);
        if (cmdHandler == null) {
            return new CmdMsgFrame<>(cmd, null);
        }
        @SuppressWarnings("unchecked")
        Class<JsonMsg> reqMsgClass = cmdHandler.reqMsgClass();
        JsonMsg jsonMsg = BeanUtil.mapToBean(jsonMap, reqMsgClass, true);

        return new CmdMsgFrame<>(cmd, jsonMsg);
    }
}
