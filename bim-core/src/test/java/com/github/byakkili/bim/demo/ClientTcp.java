package com.github.byakkili.bim.demo;

import cn.hutool.core.io.BufferUtil;
import cn.hutool.socket.nio.NioClient;
import com.github.byakkili.bim.core.protocol.impl.protobuf.tcp.TcpProtobufPacket;
import com.github.byakkili.bim.protobuf.Packet.Chat;
import com.github.byakkili.bim.protobuf.Packet.ChatType;
import com.github.byakkili.bim.protobuf.Packet.Command;
import com.github.byakkili.bim.protobuf.Packet.MsgType;
import com.googlecode.protobuf.format.JsonFormat;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @author Guannian Li
 */
@Slf4j
public class ClientTcp {
    private static JsonFormat jsonFormat = new JsonFormat();

    public static void main(String[] args) throws IOException {
        Chat sendChat = Chat.newBuilder()
                .setCmd(Command.CHAT)
                .setSeq(999)
                .setTo("张三")
                .setContent("你好")
                .setChatType(ChatType.PRIVATE)
                .setMsgType(MsgType.TEXT)
                .build();

        NioClient nioClient = new NioClient("127.0.0.1", ServerStarter.PORT);

        ByteBuffer allocate = ByteBuffer.allocate(1024);

        TcpProtobufPacket sendPacket = new TcpProtobufPacket(sendChat.getCmdValue(), sendChat.toByteArray());
        allocate.put(sendPacket.toByteArray()).flip();
        nioClient.write(allocate);
        log.info("Send: {}", jsonFormat.printToString(sendChat));

        allocate.clear();
        nioClient.read(allocate);
        allocate.flip();
        byte[] receiveBytes = BufferUtil.readBytes(allocate);

        TcpProtobufPacket receivePacket = TcpProtobufPacket.parse(receiveBytes);
        Chat receiveChat = Chat.parseFrom(receivePacket.getData());
        log.info("Receive: {}", jsonFormat.printToString(receiveChat));

        nioClient.close();
    }
}
