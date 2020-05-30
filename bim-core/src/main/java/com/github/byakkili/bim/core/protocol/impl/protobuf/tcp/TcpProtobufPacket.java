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
    private byte head = PROTOCOL_HEAD;
    /**
     * 数据长度
     */
    private int length;
    /**
     * cmd
     */
    private int cmd;
    /**
     * 数据
     */
    private byte[] data;

    public TcpProtobufPacket(byte[] bytes) {
        if (ObjectUtil.equal(ArrayUtil.get(bytes, 0), PROTOCOL_HEAD)) {
            this.length = NumberUtil.toInt(ArrayUtil.sub(bytes, 1, 5));
            this.cmd = NumberUtil.toInt(ArrayUtil.sub(bytes, 5, 9));
            this.data = ArrayUtil.sub(bytes, 9, bytes.length);
        }
    }

    public TcpProtobufPacket(ProtobufFrame frame) {
        this.cmd = frame.getCmd();
        this.data = ProtobufUtils.serialize(frame.getMessage());
        this.length = Integer.BYTES + data.length;
    }

    public byte[] toBytes() {
        return ArrayUtil.addAll(new byte[]{head}, NumberUtil.toBytes(length), NumberUtil.toBytes(cmd), data);
    }
}
