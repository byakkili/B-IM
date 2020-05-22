package com.github.byakkili.bim.core;

import cn.hutool.core.util.ArrayUtil;
import com.github.byakkili.bim.core.cluster.BaseClusterPacket;
import com.github.byakkili.bim.core.cluster.IClusterManager;
import com.github.byakkili.bim.core.maintain.Groups;
import com.github.byakkili.bim.core.maintain.Users;
import lombok.Getter;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * B-IM上下文
 *
 * @author Guannian Li
 */
@Getter
public class BimContext extends BimConfiguration {
    /**
     * 用户连接信息
     */
    private final Users users = new Users();
    /**
     * 群组连接信息
     */
    private final Groups groups = new Groups();
    /**
     * 属性
     */
    private final Map<String, Object> attributes = new ConcurrentHashMap<>();

    public BimContext(BimConfiguration config) {
        super(config);
    }

    // ---------------------------------------------------------------------------------------------- Public method start

    /**
     * 获取属性
     *
     * @param key 键
     * @return 值
     */
    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    /**
     * 设置属性
     *
     * @param key   键
     * @param value 值
     */
    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    /**
     * 删除属性
     *
     * @param key 键
     */
    public void removeAttribute(String key) {
        attributes.remove(key);
    }

    /**
     * 发送信息给用户
     *
     * @param userId            用户ID
     * @param data              消息
     * @param excludeSessionIds 排除的Session
     * @return 是否发送成功
     */
    public boolean sendToUser(String userId, Object data, String... excludeSessionIds) {
        return sendToSessions(users.getSessions(userId), data, excludeSessionIds);
    }

    /**
     * 发送信息给群组
     *
     * @param groupId           群组ID
     * @param data              消息
     * @param excludeSessionIds 排除的Session
     * @return 是否发送成功
     */
    public boolean sendToGroup(String groupId, Object data, String... excludeSessionIds) {
        return sendToSessions(groups.getSessions(groupId), data, excludeSessionIds);
    }

    /**
     * 发送到集群
     *
     * @param clusterPacket 集群包
     * @return 是否发送成功
     */
    public boolean sendToCluster(BaseClusterPacket clusterPacket) {
        IClusterManager clusterManager = getClusterManager();
        if (clusterManager == null) {
            return false;
        }
        return clusterManager.send(clusterPacket);
    }

    // ---------------------------------------------------------------------------------------------- Public method end

    // ---------------------------------------------------------------------------------------------- Private method start

    /**
     * 发送信息到会话
     *
     * @param sessions          会话
     * @param msg               消息
     * @param excludeSessionIds 排除的Session
     * @return 是否发送成功
     */
    private boolean sendToSessions(Set<BimSession> sessions, Object msg, String... excludeSessionIds) {
        // 发送到Session
        if (sessions == null || sessions.isEmpty()) {
            return false;
        }
        for (BimSession session : sessions) {
            if (!ArrayUtil.contains(excludeSessionIds, session.getId())) {
                session.writeAndFlush(msg);
            }
        }
        return true;
    }

    // ---------------------------------------------------------------------------------------------- Private method end
}
