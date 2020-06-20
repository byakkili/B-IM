package com.github.byakkili.bim.core.util;

import com.github.byakkili.bim.Packet.Chat;
import com.github.byakkili.bim.Packet.ChatType;
import com.github.byakkili.bim.Packet.Command;
import com.github.byakkili.bim.Packet.MsgType;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Guannian Li
 */
public class ProtobufUtilsTests {
    private Chat chat = Chat.newBuilder().setSeq(1).setTo("1").setContent("Hello World!").setChatType(ChatType.PRIVATE).setMsgType(MsgType.TEXT).setCmd(Command.CHAT).build();
    private Chat chat2 = Chat.newBuilder().setSeq(2).setTo("2").setContent("Hello World!").setChatType(ChatType.PRIVATE).setMsgType(MsgType.TEXT).setCmd(Command.CHAT).build();

    @Test
    public void serialize() {
        Assert.assertNotEquals(chat, chat2);

        byte[] chatBytes = chat.toByteArray();
        byte[] serialize = ProtobufUtils.serialize(chat);

        Assert.assertNotNull(chatBytes);
        Assert.assertNotNull(serialize);
        Assert.assertArrayEquals(chatBytes, serialize);
    }

    @Test
    public void deserialize() {
        Assert.assertNotEquals(chat, chat2);

        Chat deserialize = ProtobufUtils.deserialize(chat.toByteArray(), Chat.class);

        Assert.assertNotNull(chat);
        Assert.assertNotNull(deserialize);
        Assert.assertEquals(chat, deserialize);
    }
}
