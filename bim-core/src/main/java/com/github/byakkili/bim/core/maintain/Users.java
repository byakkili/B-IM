package com.github.byakkili.bim.core.maintain;

import com.github.byakkili.bim.core.BimSession;

import java.util.Set;

/**
 * @author Guannian Li
 */
public class Users extends BaseObjectBind<BimSession> {
    /**
     * 绑定
     *
     * @param session 会话
     */
    public void bind(BimSession session) {
        bind(session.getUserId(), session);
    }

    /**
     * 解绑
     *
     * @param session 会话
     */
    public void unbind(BimSession session) {
        unbind(session.getUserId(), session);
    }

    /**
     * 获取用户会话
     *
     * @param userId 用户ID
     * @return 用户会话
     */
    public Set<BimSession> getSessions(String userId) {
        return getStrSetMap().get(userId);
    }
}
