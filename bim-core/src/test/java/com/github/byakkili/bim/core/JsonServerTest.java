package com.github.byakkili.bim.core;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.BufferUtil;
import cn.hutool.core.net.LocalPortGenerater;
import cn.hutool.socket.nio.NioClient;
import com.github.byakkili.bim.core.protocol.impl.json.BaseJsonCmdHandler;
import com.github.byakkili.bim.core.protocol.impl.json.JsonMsg;
import com.github.byakkili.bim.core.protocol.impl.json.tcp.TcpJsonPacket;
import com.github.byakkili.bim.core.protocol.impl.json.tcp.TcpJsonProtocolProvider;
import com.github.byakkili.bim.core.protocol.impl.json.ws.WsJsonProtocolProvider;
import com.github.byakkili.bim.core.util.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author Guannian Li
 */
@Slf4j
public class JsonServerTest {
    private int port;
    private BimServerBootstrap bootstrap;

    @Before
    public void init() {
        port = new LocalPortGenerater(10000).generate();

        BimConfiguration config = new BimConfiguration();
        config.setPort(port);
        config.setReaderTimeout(30);
        config.setWriterTimeout(30);
        config.addProtocolProvider(new WsJsonProtocolProvider());
        config.addProtocolProvider(new TcpJsonProtocolProvider());
        config.addCmdHandler(new TestJsonCmdHandler());

        // 启动
        bootstrap = new BimServerBootstrap(config);
        bootstrap.start();
    }

    @Test
    public void tcp() {
        ByteBuffer allocate = ByteBuffer.allocate(1024);
        NioClient nioClient = new NioClient("127.0.0.1", port);

        TestJsonMsg sendMsg = new TestJsonMsg(1, 99, "Hello, testing!");
        TcpJsonPacket sendPacket = new TcpJsonPacket(sendMsg);

        allocate.put(sendPacket.toByteArray()).flip();
        nioClient.write(allocate);
        log.info("Send: {}", JsonUtils.stringify(sendMsg));

        allocate.clear();
        nioClient.read(allocate);
        allocate.flip();

        byte[] receiveBytes = BufferUtil.readBytes(allocate);
        TcpJsonPacket receivePacket = TcpJsonPacket.parse(receiveBytes);
        TestJsonMsg receiveMsg = JsonUtils.deserialize(receivePacket.getData(), TestJsonMsg.class);
        log.info("Receive: {}", JsonUtils.stringify(receiveMsg));

        Assert.assertTrue(sendMsg.getCmd() != null && sendMsg.getCmd() + 1 == receiveMsg.getCmd());
        Assert.assertEquals(sendMsg.getSeq(), receiveMsg.getSeq());
        Assert.assertEquals(sendMsg.getContent(), receiveMsg.getContent());

        nioClient.close();
    }

    @Test
    public void ws() throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        TestJsonMsg sendMsg = new TestJsonMsg(1, 99, "Hello, testing!");
        String jsonStr = JsonUtils.stringify(sendMsg);

        WebSocketClient wsClient = new WebSocketClient(new URI("ws://127.0.0.1:" + port + "/ws"), new Draft_6455()) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                log.info("Ws连接成功");
            }

            @Override
            public void onMessage(String jsonStr) {
                log.info("Receive: {}", jsonStr);
                TestJsonMsg receiveMsg = JsonUtils.parse(jsonStr, TestJsonMsg.class);

                Assert.assertTrue(sendMsg.getCmd() != null && sendMsg.getCmd() + 1 == receiveMsg.getCmd());
                Assert.assertEquals(sendMsg.getSeq(), receiveMsg.getSeq());
                Assert.assertEquals(sendMsg.getContent(), receiveMsg.getContent());

                countDownLatch.countDown();
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                log.info("Ws关闭");
            }

            @Override
            public void onError(Exception e) {
                log.error("Ws出现异常", e);
            }
        };
        wsClient.connectBlocking(5, TimeUnit.SECONDS);
        log.info("Send: {}", jsonStr);
        wsClient.send(jsonStr);

        countDownLatch.await(5, TimeUnit.SECONDS);
        wsClient.close();
    }

    @After
    public void close() {
        bootstrap.close();
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TestJsonMsg implements JsonMsg {
        private Integer cmd;
        private long seq;
        private String content;
    }

    public static class TestJsonCmdHandler extends BaseJsonCmdHandler<TestJsonMsg> {
        @Override
        protected JsonMsg process(TestJsonMsg reqMsg, BimSession session) {
            TestJsonMsg respMsg = new TestJsonMsg();
            BeanUtil.copyProperties(reqMsg, respMsg);
            respMsg.setCmd(reqMsg.getCmd() + 1);
            return respMsg;
        }

        @Override
        public int cmd() {
            return 1;
        }

        @Override
        public Class<TestJsonMsg> reqMsgClass() {
            return TestJsonMsg.class;
        }
    }
}
