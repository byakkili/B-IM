package com.github.byakkili.bim.demo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Guannian Li
 */
@Setter
@Getter
@ToString
public class UserInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * Id
     */
    private String id;
    /**
     * 名称
     */
    private String nickname;
}
