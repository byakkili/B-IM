package com.github.byakkili.bim.demo.protobuf;

import cn.hutool.core.io.BufferUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.NumberUtil;
import com.github.byakkili.bim.Packet.Chat;
import com.github.byakkili.bim.Packet.ChatType;
import com.github.byakkili.bim.Packet.Command;
import com.github.byakkili.bim.Packet.MsgType;
import com.googlecode.protobuf.format.JsonFormat;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author Guannian Li
 */
@Slf4j
public class ClientWs {
    private static JsonFormat jsonFormat = new JsonFormat();

    public static void main(String[] args) throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        WebSocketClient wsClient = new WebSocketClient(new URI("ws://127.0.0.1:" + ServerStarter.PORT + "/ws"), new Draft_6455()) {
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
                int cmd = NumberUtil.toInt(BufferUtil.readBytes(bytes, 4));
                byte[] chatBytes = BufferUtil.readBytes(bytes);
                Chat receiveChat = Chat.parseFrom(chatBytes);
                log.info("Receive: {}", jsonFormat.printToString(receiveChat));
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

        Chat sendChat = Chat.newBuilder()
                .setCmd(Command.CHAT)
                .setSeq(999)
                .setTo("张三")
                .setContent("你好")
                .setChatType(ChatType.GROUP)
                .setMsgType(MsgType.VIDEO)
                .build();
        wsClient.send(ArrayUtil.addAll(NumberUtil.toBytes(sendChat.getCmdValue()), sendChat.toByteArray()));
        log.info("Send: {}", jsonFormat.printToString(sendChat));

        countDownLatch.await(5, TimeUnit.SECONDS);

        wsClient.close();
    }
}
