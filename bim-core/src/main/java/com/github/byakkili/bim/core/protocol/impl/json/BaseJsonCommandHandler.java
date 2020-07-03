package com.github.byakkili.bim.core.protocol.impl.json;

import cn.hutool.core.util.StrUtil;
import com.github.byakkili.bim.core.command.CommandHandler;

/**
 * @author Guannian Li
 */
public abstract class BaseJsonCommandHandler<T extends JsonMsg> extends CommandHandler<T, JsonMsg> {
    @Override
    public String toString() {
        return StrUtil.format("{}(command:{}, protocol:json)", this.getClass().getName(), command());
    }
}
