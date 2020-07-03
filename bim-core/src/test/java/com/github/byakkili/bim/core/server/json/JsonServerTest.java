package com.github.byakkili.bim.core.server.json;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.net.LocalPortGenerater;
import cn.hutool.core.util.ObjectUtil;
import com.github.byakkili.bim.core.BimConfiguration;
import com.github.byakkili.bim.core.BimNettyServer;
import com.github.byakkili.bim.core.protocol.impl.json.tcp.TcpJsonPacket;
import com.github.byakkili.bim.core.protocol.impl.json.tcp.TcpJsonProtocolProvider;
import com.github.byakkili.bim.core.protocol.impl.json.ws.WsJsonProtocolProvider;
import com.github.byakkili.bim.core.util.JsonUtils;
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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author Guannian Li
 */
@Slf4j
public class JsonServerTest {
    private int port;
    private TestJsonListener testListener;
    private TestJsonInterceptor testInterceptor;
    private BimNettyServer bimNettyServer;

    @Before
    public void init() {
        port = new LocalPortGenerater(10000).generate();
        testListener = new TestJsonListener();
        testInterceptor = new TestJsonInterceptor();

        BimConfiguration config = new BimConfiguration();
        config.setPort(port);
        config.setReaderTimeout(30);
        config.setWriterTimeout(30);
        config.addSessionListener(testListener);
        config.addProtocolProvider(new WsJsonProtocolProvider());
        config.addProtocolProvider(new TcpJsonProtocolProvider());
        config.addCommandHandler(new TestJsonCommandHandler());
        config.addCommandInterceptor(testInterceptor);

        // 启动
        bimNettyServer = new BimNettyServer(config);
        bimNettyServer.start();
    }

    @Test
    public void ws() throws Exception {
        final TestJsonMsg[] sendAndReceive = {new TestJsonMsg(TestJsonCommandHandler.COMMAND, 99L, "Hello, testing!"), null};

        CountDownLatch countDownLatch = new CountDownLatch(1);

        WebSocketClient wsClient = new WebSocketClient(new URI("ws://127.0.0.1:" + port + "/ws"), new Draft_6455()) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                log.info("Ws open");
            }

            @Override
            public void onMessage(String jsonStr) {
                log.info("Receive: {}", jsonStr);
                sendAndReceive[1] = JsonUtils.parse(jsonStr, TestJsonMsg.class);
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

        String jsonStr = JsonUtils.stringify(sendAndReceive[0]);
        log.info("Send: {}", jsonStr);
        wsClient.send(jsonStr);

        countDownLatch.await(5, TimeUnit.SECONDS);

        Assert.assertEquals(TestJsonCommandHandler.COMMAND_ACK, sendAndReceive[1].getCommand());
        Assert.assertEquals(sendAndReceive[0].getSeq(), sendAndReceive[1].getSeq());
        Assert.assertEquals(sendAndReceive[0].getContent(), sendAndReceive[1].getContent());

        testInterceptor.getCountDownLatch().await(5, TimeUnit.SECONDS);
        Assert.assertEquals(testInterceptor.getCount().get(), 3);

        Assert.assertEquals(testListener.getCount().get(), 3);

        wsClient.close();
    }

    @Test
    public void tcp() throws Exception {
        try (
                Socket socket = new Socket("127.0.0.1", port);
                InputStream inputStream = socket.getInputStream();
                OutputStream outputStream = socket.getOutputStream()
        ) {
            TestJsonMsg sendMsg = new TestJsonMsg(TestJsonCommandHandler.COMMAND, 99L, "Hello, testing!");
            TcpJsonPacket sendPacket = new TcpJsonPacket(sendMsg);
            IoUtil.write(outputStream, false, sendPacket.toByteArray());
            log.info("Send: {}", JsonUtils.stringify(sendMsg));

            byte[] receiveBytes = IoUtil.readBytes(inputStream, 200);
            TcpJsonPacket receivePacket = TcpJsonPacket.parse(receiveBytes);
            TestJsonMsg receiveMsg = JsonUtils.deserialize(receivePacket.getData(), TestJsonMsg.class);
            log.info("Receive: {}", JsonUtils.stringify(receiveMsg));

            Assert.assertEquals(TestJsonCommandHandler.COMMAND_ACK, receiveMsg.getCommand());
            Assert.assertEquals(sendMsg.getSeq(), receiveMsg.getSeq());
            Assert.assertEquals(sendMsg.getContent(), receiveMsg.getContent());
        }
        testInterceptor.getCountDownLatch().await(5, TimeUnit.SECONDS);
        Assert.assertEquals(testInterceptor.getCount().get(), 3);

        testListener.getCountDownLatch().await(5, TimeUnit.SECONDS);
        Assert.assertEquals(testListener.getCount().get(), 4);
    }

    @Test
    public void interceptor() throws Exception {
        try (
                Socket socket = new Socket("127.0.0.1", port);
                InputStream inputStream = socket.getInputStream();
                OutputStream outputStream = socket.getOutputStream()
        ) {
            TestJsonMsg sendMsg = new TestJsonMsg(TestJsonInterceptor.NO_ALLOW_COMMAND, 99L, "Hello, testing!");
            TcpJsonPacket sendPacket = new TcpJsonPacket(sendMsg);
            IoUtil.write(outputStream, false, sendPacket.toByteArray());
            log.info("Send: {}", JsonUtils.stringify(sendMsg));

            byte[] receiveBytes = IoUtil.readBytes(inputStream, 200);
            TcpJsonPacket receivePacket = TcpJsonPacket.parse(receiveBytes);
            TestJsonMsg receiveMsg = JsonUtils.deserialize(receivePacket.getData(), TestJsonMsg.class);
            log.info("Receive: {}", JsonUtils.stringify(receiveMsg));

            Assert.assertTrue(ObjectUtil.equal(TestJsonInterceptor.RESP_MSG, receiveMsg));
        }
    }

    @After
    public void close() {
        bimNettyServer.close();
        System.out.println();
    }
}
