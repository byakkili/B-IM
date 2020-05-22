package com.github.byakkili.bim.core.maintain;

import cn.hutool.core.collection.CollUtil;
import com.github.byakkili.bim.core.BimSession;

import java.util.Set;

/**
 * @author Guannian Li
 */
public class Groups extends BaseObjectBind<BimSession> {
    /**
     * 绑定
     *
     * @param session 会话
     */
    public void bind(BimSession session) {
        Set<String> groupIds = session.getGroupIds();
        if (CollUtil.isNotEmpty(groupIds)) {
            groupIds.forEach(groupId -> bind(groupId, session));
        }
    }

    /**
     * 解绑
     *
     * @param session 会话
     */
    public void unbind(BimSession session) {
        Set<String> groupIds = session.getGroupIds();
        if (CollUtil.isNotEmpty(groupIds)) {
            groupIds.forEach(groupId -> unbind(groupId, session));
        }
    }

    /**
     * 获取群组会话
     *
     * @param groupId 群组ID
     * @return 群组会话
     */
    public Set<BimSession> getSessions(String groupId) {
        return getObjectMap().get(groupId);
    }
}
