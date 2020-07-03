package com.github.byakkili.bim.core.protocol.impl.json;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import com.github.byakkili.bim.core.BimContext;
import com.github.byakkili.bim.core.BimSession;
import com.github.byakkili.bim.core.command.CommandHandler;
import com.github.byakkili.bim.core.protocol.CommandFrame;
import com.github.byakkili.bim.core.util.CommandHandlerUtils;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.Map;

/**
 * @author Guannian Li
 */
public abstract class BaseJsonCodec<T> extends MessageToMessageCodec<T, CommandFrame<JsonMsg>> {

    protected CommandFrame<JsonMsg> decodeToFrame(BimSession session, Map jsonMap) {
        BimContext context = session.getContext();
        Map<Integer, CommandHandler> commandHandlers = context.getCommandHandlers();

        Integer command = MapUtil.getInt(jsonMap, JsonMsg.COMMAND_FIELD);
        if (command == null) {
            return null;
        }
        CommandHandler commandHandler = commandHandlers.get(command);
        if (commandHandler == null) {
            return new CommandFrame<>(command, null);
        }
        Class<JsonMsg> msgClass = CommandHandlerUtils.getMsgClass(commandHandler);
        JsonMsg jsonMsg = BeanUtil.mapToBean(jsonMap, msgClass, true);

        return new CommandFrame<>(command, jsonMsg);
    }
}
