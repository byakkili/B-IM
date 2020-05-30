package com.github.byakkili.bim.core.protocol.impl.json.tcp;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
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
    private byte head = PROTOCOL_HEAD;
    /**
     * 数据长度
     */
    private int length;
    /**
     * 数据
     */
    private byte[] data;

    public TcpJsonPacket(byte[] bytes) {
        if (ObjectUtil.equal(ArrayUtil.get(bytes, 0), PROTOCOL_HEAD)) {
            this.length = NumberUtil.toInt(ArrayUtil.sub(bytes, 1, 5));
            this.data = ArrayUtil.sub(bytes, 5, bytes.length);
        }
    }

    public TcpJsonPacket(JsonMsg jsonMsg) {
        this.data = JsonUtils.serialize(jsonMsg);
        this.length = this.data.length;
    }

    public byte[] toBytes() {
        return ArrayUtil.addAll(new byte[]{head}, NumberUtil.toBytes(length), data);
    }

    @Override
    public String toString() {
        return StrUtil.format("{}(head={}, length={}, data={})", this.getClass().getSimpleName(), head, length, JsonUtils.deserialize(data));
    }
}
