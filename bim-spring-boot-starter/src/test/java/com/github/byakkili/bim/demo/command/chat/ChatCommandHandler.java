package com.github.byakkili.bim.demo.command.chat;

import cn.hutool.core.util.StrUtil;
import com.github.byakkili.bim.core.BimContext;
import com.github.byakkili.bim.core.BimSession;
import com.github.byakkili.bim.core.protocol.impl.json.BaseJsonCommandHandler;
import com.github.byakkili.bim.core.protocol.impl.json.JsonMsg;
import com.github.byakkili.bim.demo.constant.ChatMsgType;
import com.github.byakkili.bim.demo.cluster.sendtouser.SendToUserClusterPacket;
import com.github.byakkili.bim.demo.constant.ChatType;
import com.github.byakkili.bim.demo.constant.Command;
import com.github.byakkili.bim.demo.dto.AckMsg;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Guannian Li
 */
@Component
public class ChatCommandHandler extends BaseJsonCommandHandler<ChatReqMsg> {
    private AtomicLong chatReq = new AtomicLong(0);

    @Override
    public JsonMsg handle(ChatReqMsg reqMsg, BimSession session) {
        ChatMsgType chatMsgType = ChatMsgType.of(reqMsg.getMsgType());
        ChatType chatType = ChatType.of(reqMsg.getChatType());
        if (chatType == null || chatMsgType == null || StrUtil.isBlank(reqMsg.getTo())) {
            session.writeAndFlush(new AckMsg(Command.CHAT_RESP, reqMsg.getSeq(), -1, "chatType,chatMsgType,to不能为空"));
            return null;
        }

        BimContext context = session.getContext();
        ChatMsgMsg chatMsg = new ChatMsgMsg(reqMsg, chatReq.incrementAndGet(), session.getUserId());

        switch (chatType) {
            case PRIVATE:
                context.sendToUser(chatMsg.getTo(), chatMsg, session.getId());
                if (context.isCluster()) {
                    context.sendToCluster(new SendToUserClusterPacket(chatMsg.getTo(), null, chatMsg));
                }
                break;
            case GROUP:
                context.sendToGroup(chatMsg.getTo(), chatMsg, session.getId());
                if (context.isCluster()) {
                    context.sendToCluster(new SendToUserClusterPacket(null, chatMsg.getTo(), chatMsg));
                }
                break;
            default:
                break;
        }
        return new AckMsg(Command.CHAT_RESP, reqMsg.getSeq(), 0, "发送成功");
    }

    @Override
    public int command() {
        return Command.CHAT_REQ;
    }
}
