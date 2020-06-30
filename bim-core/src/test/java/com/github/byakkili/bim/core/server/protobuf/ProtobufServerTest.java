package com.github.byakkili.bim.core.server.protobuf;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.net.LocalPortGenerater;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.NumberUtil;
import com.github.byakkili.bim.core.BimConfiguration;
import com.github.byakkili.bim.core.BimNettyServer;
import com.github.byakkili.bim.core.protocol.impl.protobuf.tcp.TcpProtobufPacket;
import com.github.byakkili.bim.core.protocol.impl.protobuf.tcp.TcpProtobufProtocolProvider;
import com.github.byakkili.bim.core.protocol.impl.protobuf.ws.WsProtobufProtocolProvider;
import com.github.byakkili.bim.protobuf.Packet.Chat;
import com.github.byakkili.bim.protobuf.Packet.ChatType;
import com.github.byakkili.bim.protobuf.Packet.Command;
import com.github.byakkili.bim.protobuf.Packet.MsgType;
import com.googlecode.protobuf.format.JsonFormat;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static cn.hutool.core.io.BufferUtil.readBytes;

/**
 * @author Guannian Li
 */
@Slf4j
public class ProtobufServerTest {
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
    public void ws() throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        final Chat[] sendAndReceive = {Chat.newBuilder()
                .setCmd(Command.CHAT)
                .setSeq(999)
                .setTo("张三")
                .setContent("你好")
                .setChatType(ChatType.GROUP)
                .setMsgType(MsgType.VIDEO)
                .build(), null};

        WebSocketClient wsClient = new WebSocketClient(new URI("ws://127.0.0.1:" + port + "/ws"), new Draft_6455()) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                log.info("Ws open");
            }

            @Override
            public void onMessage(String jsonStr) {
                log.info("Receive: {}", jsonStr);
            }

            @Override
            @SneakyThrows
            public void onMessage(ByteBuffer bytes) {
                int cmd = NumberUtil.toInt(readBytes(bytes, 4));
                byte[] chatBytes = readBytes(bytes);
                sendAndReceive[1] = Chat.parseFrom(chatBytes);
                log.info("Receive: {}", jsonFormat.printToString(sendAndReceive[1]));
                countDownLatch.countDown();
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                log.info("Ws close");
            }

            @Override
            public void onError(Exception e) {
                log.error("Ws error", e);
            }
        };
        wsClient.connectBlocking(5, TimeUnit.SECONDS);

        wsClient.send(ArrayUtil.addAll(NumberUtil.toBytes(sendAndReceive[0].getCmdValue()), sendAndReceive[0].toByteArray()));
        log.info("Send: {}", jsonFormat.printToString(sendAndReceive[0]));

        countDownLatch.await(5, TimeUnit.SECONDS);

        Assert.assertEquals(Command.CHAT_ACK, sendAndReceive[1].getCmd());
        Assert.assertEquals(sendAndReceive[0].getSeq(), sendAndReceive[1].getSeq());
        Assert.assertEquals(sendAndReceive[0].getTo(), sendAndReceive[1].getTo());
        Assert.assertEquals(sendAndReceive[0].getContent() + ": 已收到", sendAndReceive[1].getContent());
        Assert.assertEquals(sendAndReceive[0].getChatType(), sendAndReceive[1].getChatType());
        Assert.assertEquals(sendAndReceive[0].getMsgType(), sendAndReceive[1].getMsgType());

        wsClient.close();
    }

    @Test
    public void tcp() throws Exception {
        try (
                Socket socket = new Socket("127.0.0.1", port);
                InputStream inputStream = socket.getInputStream();
                OutputStream outputStream = socket.getOutputStream()
        ) {
            Chat sendChat = Chat.newBuilder()
                    .setCmd(Command.CHAT)
                    .setSeq(999)
                    .setTo("张三")
                    .setContent("你好")
                    .setChatType(ChatType.GROUP)
                    .setMsgType(MsgType.VIDEO)
                    .build();
            TcpProtobufPacket sendPacket = new TcpProtobufPacket(sendChat.getCmdValue(), sendChat.toByteArray());
            IoUtil.write(outputStream, false, sendPacket.toByteArray());
            log.info("Send: {}", jsonFormat.printToString(sendChat));

            byte[] receiveBytes = IoUtil.readBytes(inputStream, 1000);
            TcpProtobufPacket receivePacket = TcpProtobufPacket.parse(receiveBytes);
            Chat receiveChat = Chat.parseFrom(receivePacket.getData());
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
