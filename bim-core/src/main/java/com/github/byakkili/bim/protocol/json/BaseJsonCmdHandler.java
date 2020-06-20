package com.github.byakkili.bim.protocol.json;

import cn.hutool.core.util.StrUtil;
import com.github.byakkili.bim.core.cmd.CmdHandler;

/**
 * @author Guannian Li
 */
public abstract class BaseJsonCmdHandler<T extends JsonMsg> extends CmdHandler<T, JsonMsg> {
    @Override
    public String toString() {
        return StrUtil.format("{}(cmd:{}, protocol:json)", this.getClass().getName(), cmd());
    }
}
