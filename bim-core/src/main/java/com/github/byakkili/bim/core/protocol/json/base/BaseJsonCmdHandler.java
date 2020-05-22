package com.github.byakkili.bim.core.protocol.json.base;

import cn.hutool.core.util.StrUtil;
import com.github.byakkili.bim.core.cmd.BaseCmdHandler;

/**
 * @author Guannian Li
 */
public abstract class BaseJsonCmdHandler<REQUEST extends JsonMsg> extends BaseCmdHandler<REQUEST, JsonMsg> {
    @Override
    public String toString() {
        return StrUtil.format("{}(cmd:{}, protocol:json, reqClass:{})", this.getClass().getName(), cmd(), reqMsgClass().getName());
    }
}
