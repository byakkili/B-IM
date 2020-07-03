package com.github.byakkili.bim.core.protocol.impl.protobuf.tcp;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.byakkili.bim.core.protocol.CommandFrame;
import com.github.byakkili.bim.core.util.ProtobufUtils;
import com.google.protobuf.Message;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Guannian Li
 */
@Getter
@ToString
public class TcpProtobufPacket implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final byte PROTOCOL_HEAD = 0x02;
    /**
     * 协议头
     */
    private final byte head;
    /**
     * command
     */
    private final int command;
    /**
     * 数据长度
     */
    private final int length;
    /**
     * 数据
     */
    private final byte[] data;

    public TcpProtobufPacket(CommandFrame<Message> frame) {
        this(frame.getCommand(), ProtobufUtils.serialize(frame.getMsg()));
    }

    public TcpProtobufPacket(int command, byte[] data) {
        this.head = PROTOCOL_HEAD;
        this.command = command;
        this.data = data;
        this.length = Integer.BYTES + data.length;
    }

    public byte[] toByteArray() {
        return ArrayUtil.addAll(new byte[]{head}, NumberUtil.toBytes(length), NumberUtil.toBytes(command), data);
    }

    public static TcpProtobufPacket parse(byte[] bytes) {
        if (ObjectUtil.notEqual(ArrayUtil.get(bytes, 0), PROTOCOL_HEAD)) {
            return null;
        }
        int command = NumberUtil.toInt(ArrayUtil.sub(bytes, 5, 9));
        byte[] data = ArrayUtil.sub(bytes, 9, bytes.length);
        return new TcpProtobufPacket(command, data);
    }
}
