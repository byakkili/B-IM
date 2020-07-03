package com.github.byakkili.bim.demo;

import cn.hutool.core.io.IoUtil;
import com.github.byakkili.bim.core.protocol.impl.protobuf.tcp.TcpProtobufPacket;
import com.github.byakkili.bim.protobuf.Packet.Chat;
import com.github.byakkili.bim.protobuf.Packet.ChatType;
import com.github.byakkili.bim.protobuf.Packet.Command;
import com.github.byakkili.bim.protobuf.Packet.MsgType;
import com.googlecode.protobuf.format.JsonFormat;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author Guannian Li
 */
@Slf4j
public class ClientTcp {
    private static JsonFormat jsonFormat = new JsonFormat();

    public static void main(String[] args) throws IOException {
        try (
                Socket socket = new Socket("127.0.0.1", ServerStarter.PORT);
                InputStream inputStream = socket.getInputStream();
                OutputStream outputStream = socket.getOutputStream()
        ) {
            Chat sendChat = Chat.newBuilder()
                    .setCommand(Command.CHAT)
                    .setSeq(999)
                    .setTo("张三")
                    .setContent("你好")
                    .setChatType(ChatType.GROUP)
                    .setMsgType(MsgType.VIDEO)
                    .build();
            TcpProtobufPacket sendPacket = new TcpProtobufPacket(sendChat.getCommandValue(), sendChat.toByteArray());
            IoUtil.write(outputStream, false, sendPacket.toByteArray());
            log.info("Send: {}", jsonFormat.printToString(sendChat));

            byte[] receiveBytes = IoUtil.readBytes(inputStream, 1000);
            TcpProtobufPacket receivePacket = TcpProtobufPacket.parse(receiveBytes);
            Chat receiveChat = Chat.parseFrom(receivePacket.getData());
            log.info("Receive: {}", jsonFormat.printToString(receiveChat));
        }
    }
}
