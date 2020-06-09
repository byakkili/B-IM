package com.github.byakkili.bim.demo.cluster.sendtouser;

import cn.hutool.core.util.StrUtil;
import com.github.byakkili.bim.core.BimContext;
import com.github.byakkili.bim.core.cluster.IClusterHandler;
import org.springframework.stereotype.Component;

/**
 * 集群处理器, 发送信息给用户
 *
 * @author Guannian Li
 */
@Component
public class SendToUserHandler implements IClusterHandler<SendToUserClusterPacket> {
    @Override
    public Class<SendToUserClusterPacket> packetClass() {
        return SendToUserClusterPacket.class;
    }

    @Override
    public void handle(BimContext context, SendToUserClusterPacket clusterPacket) {
        String userId = clusterPacket.getUserId();
        String groupId = clusterPacket.getGroupId();
        Object data = clusterPacket.getData();
        if (data == null) {
            return;
        }
        if (StrUtil.isNotBlank(userId)) {
            context.sendToUser(userId, data);
        }
        if (StrUtil.isNotBlank(groupId)) {
            context.sendToGroup(groupId, data);
        }
    }
}
