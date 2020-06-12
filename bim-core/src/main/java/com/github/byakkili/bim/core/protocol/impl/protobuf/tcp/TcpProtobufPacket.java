package com.github.byakkili.bim.core.protocol.impl.protobuf.tcp;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.byakkili.bim.core.protocol.impl.protobuf.ProtobufFrame;
import com.github.byakkili.bim.core.util.ProtobufUtils;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Guannian Li
 */
@Getter
@ToString
public class TcpProtobufPacket {
    public static final byte PROTOCOL_HEAD = 0x02;
    /**
     * 协议头
     */
    private final byte head = PROTOCOL_HEAD;
    /**
     * cmd
     */
    private final int cmd;
    /**
     * 数据长度
     */
    private final int length;
    /**
     * 数据
     */
    private final byte[] data;

    public TcpProtobufPacket(ProtobufFrame frame) {
        this(frame.getCmd(), ProtobufUtils.serialize(frame.getMessage()));
    }

    public TcpProtobufPacket(int cmd, byte[] data) {
        this.cmd = cmd;
        this.data = data;
        this.length = Integer.BYTES + data.length;
    }

    public byte[] toByteArray() {
        return ArrayUtil.addAll(new byte[]{head}, NumberUtil.toBytes(length), NumberUtil.toBytes(cmd), data);
    }

    public static TcpProtobufPacket parse(byte[] bytes) {
        if (ObjectUtil.equal(ArrayUtil.get(bytes, 0), PROTOCOL_HEAD)) {
            int cmd = NumberUtil.toInt(ArrayUtil.sub(bytes, 5, 9));
            byte[] data = ArrayUtil.sub(bytes, 9, bytes.length);
            return new TcpProtobufPacket(cmd, data);
        }
        return null;
    }
}
