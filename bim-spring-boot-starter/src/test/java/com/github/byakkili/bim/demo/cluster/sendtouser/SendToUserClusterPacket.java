package com.github.byakkili.bim.demo.cluster.sendtouser;

import com.github.byakkili.bim.core.cluster.BaseClusterPacket;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Guannian Li
 */
@Setter
@Getter
@ToString(callSuper = true)
@NoArgsConstructor
public class SendToUserClusterPacket extends BaseClusterPacket {
    private static final long serialVersionUID = 1L;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 用户组
     */
    private String groupId;

    public SendToUserClusterPacket(String userId, String groupId, Serializable data) {
        this.userId = userId;
        this.groupId = groupId;
        setData(data);
    }
}
