package com.github.byakkili.bim.core;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.net.LocalPortGenerater;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.github.byakkili.bim.core.interceptor.CmdInterceptor;
import com.github.byakkili.bim.core.listener.ISessionListener;
import com.github.byakkili.bim.core.protocol.CmdMsgFrame;
import com.github.byakkili.bim.core.protocol.impl.json.BaseJsonCmdHandler;
import com.github.byakkili.bim.core.protocol.impl.json.JsonMsg;
import com.github.byakkili.bim.core.protocol.impl.json.tcp.TcpJsonPacket;
import com.github.byakkili.bim.core.protocol.impl.json.tcp.TcpJsonProtocolProvider;
import com.github.byakkili.bim.core.protocol.impl.json.ws.WsJsonProtocolProvider;
import com.github.byakkili.bim.core.util.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
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

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Guannian Li
 */
@Slf4j
public class JsonServerTest {
    private int port;
    private TestListener testListener;
    private TestInterceptor testInterceptor;
    private BimServerBootstrap bootstrap;

    @Before
    public void init() {
        port = new LocalPortGenerater(10000).generate();
        testListener = new TestListener();
        testInterceptor = new TestInterceptor();

        BimConfiguration config = new BimConfiguration();
        config.setPort(port);
        config.setReaderTimeout(30);
        config.setWriterTimeout(30);
        config.setSessionListener(testListener);
        config.addProtocolProvider(new WsJsonProtocolProvider());
        config.addProtocolProvider(new TcpJsonProtocolProvider());
        config.addCmdHandler(new TestJsonCmdHandler());
        config.addCmdInterceptors(testInterceptor);

        // 启动
        bootstrap = new BimServerBootstrap(config);
        bootstrap.start();
    }

    @Test
    public void ws() throws Exception {
        final TestJsonMsg[] sendAndReceive = {new TestJsonMsg(TestJsonCmdHandler.CMD, 99L, "Hello, testing!"), null};

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

        Assert.assertEquals(TestJsonCmdHandler.CMD_ACK, sendAndReceive[1].getCmd());
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
            TestJsonMsg sendMsg = new TestJsonMsg(TestJsonCmdHandler.CMD, 99L, "Hello, testing!");
            TcpJsonPacket sendPacket = new TcpJsonPacket(sendMsg);
            IoUtil.write(outputStream, false, sendPacket.toByteArray());
            log.info("Send: {}", JsonUtils.stringify(sendMsg));

            byte[] receiveBytes = IoUtil.readBytes(inputStream, 200);
            TcpJsonPacket receivePacket = TcpJsonPacket.parse(receiveBytes);
            TestJsonMsg receiveMsg = JsonUtils.deserialize(receivePacket.getData(), TestJsonMsg.class);
            log.info("Receive: {}", JsonUtils.stringify(receiveMsg));

            Assert.assertEquals(TestJsonCmdHandler.CMD_ACK, receiveMsg.getCmd());
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
            TestJsonMsg sendMsg = new TestJsonMsg(TestInterceptor.NO_ALLOW_CMD, 99L, "Hello, testing!");
            TcpJsonPacket sendPacket = new TcpJsonPacket(sendMsg);
            IoUtil.write(outputStream, false, sendPacket.toByteArray());
            log.info("Send: {}", JsonUtils.stringify(sendMsg));

            byte[] receiveBytes = IoUtil.readBytes(inputStream, 200);
            TcpJsonPacket receivePacket = TcpJsonPacket.parse(receiveBytes);
            TestJsonMsg receiveMsg = JsonUtils.deserialize(receivePacket.getData(), TestJsonMsg.class);
            log.info("Receive: {}", JsonUtils.stringify(receiveMsg));

            Assert.assertTrue(ObjectUtil.equal(TestInterceptor.RESP_MSG, receiveMsg));
        }
    }

    @After
    public void close() {
        bootstrap.close();
    }

    @Setter
    @Getter
    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    static class TestJsonMsg implements JsonMsg {
        private Integer cmd;
        private Long seq;
        private String content;
    }

    static class TestJsonCmdHandler extends BaseJsonCmdHandler<TestJsonMsg> {
        static final Integer CMD = 1;
        static final Integer CMD_ACK = 2;

        @Override
        protected JsonMsg process(TestJsonMsg reqMsg, BimSession session) {
            reqMsg.setCmd(CMD_ACK);
            return reqMsg;
        }

        @Override
        public int cmd() {
            return CMD;
        }

        @Override
        public Class<TestJsonMsg> reqMsgClass() {
            return TestJsonMsg.class;
        }
    }

    @Slf4j
    @Getter
    static class TestListener implements ISessionListener {
        private CountDownLatch countDownLatch = new CountDownLatch(4);
        private AtomicInteger count = new AtomicInteger(0);

        @Override
        public void onRead(CmdMsgFrame frame, BimSession session) {
            log.info("Session({}) read: {}", session.getChannel().id().asShortText(), JsonUtils.stringify(frame.getMsg()));
            countDownLatch.countDown();
            count.incrementAndGet();
        }

        @Override
        public void onWrite(CmdMsgFrame frame, BimSession session) {
            log.info("Session({}) write: {}", session.getChannel().id().asShortText(), JsonUtils.stringify(frame.getMsg()));
            countDownLatch.countDown();
            count.incrementAndGet();
        }

        @Override
        public void onAfterCreated(BimSession session) {
            log.info("Session({}) created.", session.getChannel().id().asShortText());
            countDownLatch.countDown();
            count.incrementAndGet();
        }

        @Override
        public void onBeforeDestroy(BimSession session) {
            log.info("Session({}) destroy.", session.getChannel().id().asShortText());
            countDownLatch.countDown();
            count.incrementAndGet();
        }

        @Override
        public void onReaderIdle(BimSession session) {
            log.info("Session({}) reader idle.", session.getChannel().id().asShortText());
        }

        @Override
        public void onWriterIdle(BimSession session) {
            log.info("Session({}) writer idle.", session.getChannel().id().asShortText());
        }
    }

    @Slf4j
    @Getter
    static class TestInterceptor implements CmdInterceptor {
        static final Integer NO_ALLOW_CMD = 100;
        static final TestJsonMsg RESP_MSG = new TestJsonMsg(101, 403L, "Forbidden");

        private CountDownLatch countDownLatch = new CountDownLatch(3);
        private AtomicInteger count = new AtomicInteger(0);

        @Override
        public boolean preHandle(CmdMsgFrame reqFrame, BimSession session) {
            log.info("Session({}) preHandle, reqMsg: {}", session.getChannel().id().asShortText(), JsonUtils.stringify(reqFrame.getMsg()));
            countDownLatch.countDown();
            count.incrementAndGet();
            if (ObjectUtil.equal(NO_ALLOW_CMD, reqFrame.getCmd())) {
                session.writeAndFlush(RESP_MSG);
                return false;
            }
            return true;
        }

        @Override
        public void postHandle(CmdMsgFrame respFrame, BimSession session) {
            log.info("Session({}) postHandle, respMsg: {}", session.getChannel().id().asShortText(), JsonUtils.stringify(respFrame.getMsg()));
            countDownLatch.countDown();
            count.incrementAndGet();
        }

        @Override
        public void afterCompletion(CmdMsgFrame reqFrame, BimSession session, Exception e) {
            log.info("Session({}) afterCompletion, exception: {}", session.getChannel().id().asShortText(), StrUtil.toString(e));
            countDownLatch.countDown();
            count.incrementAndGet();
        }
    }
}
