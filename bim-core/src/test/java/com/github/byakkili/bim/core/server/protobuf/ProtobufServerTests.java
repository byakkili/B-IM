package com.github.byakkili.bim.core.server.protobuf;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.net.LocalPortGenerater;
import com.github.byakkili.bim.core.BimConfiguration;
import com.github.byakkili.bim.core.BimNettyServer;
import com.github.byakkili.bim.core.protocol.impl.protobuf.tcp.TcpProtobufPacket;
import com.github.byakkili.bim.core.protocol.impl.protobuf.tcp.TcpProtobufProtocolProvider;
import com.github.byakkili.bim.core.protocol.impl.protobuf.ws.WsProtobufProtocolProvider;
import com.github.byakkili.bim.protobuf.Packet;
import com.github.byakkili.bim.protobuf.Packet.Command;
import com.googlecode.protobuf.format.JsonFormat;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author Guannian Li
 */
@Slf4j
public class ProtobufServerTests {
    private int port;
    private BimNettyServer bimNettyServer;

    private JsonFormat jsonFormat = new JsonFormat();

    @Before
    public void init() {
        port = new LocalPortGenerater(10000).generate();

        BimConfiguration config = new BimConfiguration();
        config.setPort(port);
        config.setReaderTimeout(30);
        config.setWriterTimeout(30);
        config.addProtocolProvider(new WsProtobufProtocolProvider());
        config.addProtocolProvider(new TcpProtobufProtocolProvider());
        config.addCmdHandler(new TestProtobufCmdHandler());

        // 启动
        bimNettyServer = new BimNettyServer(config);
        bimNettyServer.start();
    }

    @Test
    public void tcp() throws Exception {
        try (
                Socket socket = new Socket("127.0.0.1", port);
                InputStream inputStream = socket.getInputStream();
                OutputStream outputStream = socket.getOutputStream()
        ) {
            Packet.Chat sendChat = Packet.Chat.newBuilder()
                    .setCmd(Command.CHAT)
                    .setSeq(999)
                    .setTo("张三")
                    .setContent("你好")
                    .setChatType(Packet.ChatType.GROUP)
                    .setMsgType(Packet.MsgType.VIDEO)
                    .build();
            TcpProtobufPacket sendPacket = new TcpProtobufPacket(sendChat.getCmdValue(), sendChat.toByteArray());
            IoUtil.write(outputStream, false, sendPacket.toByteArray());
            log.info("Send: {}", jsonFormat.printToString(sendChat));

            byte[] receiveBytes = IoUtil.readBytes(inputStream, 1000);
            TcpProtobufPacket receivePacket = TcpProtobufPacket.parse(receiveBytes);
            Packet.Chat receiveChat = Packet.Chat.parseFrom(receivePacket.getData());
            log.info("Receive: {}", jsonFormat.printToString(receiveChat));

            Assert.assertEquals(Command.CHAT_ACK, receiveChat.getCmd());
            Assert.assertEquals(sendChat.getSeq(), receiveChat.getSeq());
            Assert.assertEquals(sendChat.getTo(), receiveChat.getTo());
            Assert.assertEquals(sendChat.getContent() + ": 已收到", receiveChat.getContent());
            Assert.assertEquals(sendChat.getChatType(), receiveChat.getChatType());
            Assert.assertEquals(sendChat.getMsgType(), receiveChat.getMsgType());
        }
    }

    @After
    public void close() {
        bimNettyServer.close();
        System.out.println();
    }
}
