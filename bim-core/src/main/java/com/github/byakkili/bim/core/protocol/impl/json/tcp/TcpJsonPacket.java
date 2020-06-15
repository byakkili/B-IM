package com.github.byakkili.bim.core.protocol.impl.json.tcp;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.byakkili.bim.core.protocol.CmdMsgFrame;
import com.github.byakkili.bim.core.protocol.impl.json.JsonMsg;
import com.github.byakkili.bim.core.util.JsonUtils;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author Guannian Li
 */
@Getter
public class TcpJsonPacket implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final byte PROTOCOL_HEAD = 0x01;
    /**
     * 协议头
     */
    private final byte head;
    /**
     * 数据长度
     */
    private final int length;
    /**
     * 数据
     */
    private final byte[] data;

    public TcpJsonPacket(CmdMsgFrame<JsonMsg> frame) {
        this(frame.getMsg());
    }

    public TcpJsonPacket(JsonMsg jsonMsg) {
        this(JsonUtils.serialize(jsonMsg));
    }

    public TcpJsonPacket(byte[] data) {
        this.head = PROTOCOL_HEAD;
        this.data = data;
        this.length = data.length;
    }

    public byte[] toByteArray() {
        return ArrayUtil.addAll(new byte[]{head}, NumberUtil.toBytes(length), data);
    }

    public static TcpJsonPacket parse(byte[] bytes) {
        if (ObjectUtil.notEqual(ArrayUtil.get(bytes, 0), PROTOCOL_HEAD)) {
            return null;
        }
        byte[] data = ArrayUtil.sub(bytes, 5, bytes.length);
        return new TcpJsonPacket(data);
    }
}
