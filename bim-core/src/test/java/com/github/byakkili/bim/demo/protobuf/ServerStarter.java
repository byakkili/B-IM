package com.github.byakkili.bim.demo.protobuf;

import com.github.byakkili.bim.core.BimConfiguration;
import com.github.byakkili.bim.core.BimNettyServer;
import com.github.byakkili.bim.protocol.protobuf.tcp.TcpProtobufProtocolProvider;
import com.github.byakkili.bim.protocol.protobuf.ws.WsProtobufProtocolProvider;

import java.util.Scanner;

/**
 * @author Guannian Li
 */
public class ServerStarter {
    static final int PORT = 9000;

    public static void main(String[] args) {
        // 配置
        BimConfiguration config = new BimConfiguration();
        config.setPort(PORT);
        config.setReaderTimeout(30);
        config.setWriterTimeout(30);
        config.setSessionListener(new TestSessionListener());

        config.addCmdInterceptors(new TestInterceptor());
        config.addProtocolProvider(new WsProtobufProtocolProvider());
        config.addProtocolProvider(new TcpProtobufProtocolProvider());
        config.addCmdHandler(new TestHandler());

        // 启动
        BimNettyServer bimNettyServer = new BimNettyServer(config);
        bimNettyServer.start();

        new Scanner(System.in).next();
        // 关闭
        bimNettyServer.close();
    }
}
