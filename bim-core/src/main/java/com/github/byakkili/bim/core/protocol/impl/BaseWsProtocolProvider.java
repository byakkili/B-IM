package com.github.byakkili.bim.core.protocol.impl;

import com.github.byakkili.bim.core.protocol.ProtocolProvider;
import io.netty.buffer.ByteBuf;
import lombok.EqualsAndHashCode;

import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

/**
 * @author Guannian Li
 */
@EqualsAndHashCode
public abstract class BaseWsProtocolProvider implements ProtocolProvider {
    private static final Pattern UPGRADE_PATTERN = Pattern.compile("^Upgrade: websocket$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
    private static final Pattern SEC_WEB_SOCKET_VERSION_PATTERN = Pattern.compile("^Sec-WebSocket-Version: \\d+$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

    @Override
    public String name() {
        return "WS";
    }

    @Override
    public boolean isProtocol(ByteBuf buf) {
        String msg = buf.toString(buf.readerIndex(), buf.readableBytes(), StandardCharsets.UTF_8);
        return UPGRADE_PATTERN.matcher(msg).find() && SEC_WEB_SOCKET_VERSION_PATTERN.matcher(msg).find();
    }
}
