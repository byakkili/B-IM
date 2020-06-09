package com.github.byakkili.bim.demo.constant;

/**
 * @author Guannian Li
 */
public class Cmd {
    /** 认证请求 */
    public static final int AUTH_REQ = 1;
    /** 认证响应 */
    public static final int AUTH_RESP = 2;

    /** 在线用户请求 */
    public static final int ONLINE_USERS_REQ = 3;
    /** 在线用户响应 */
    public static final int ONLINE_USERS_RESP = 4;

    /** 聊天请求 */
    public static final int CHAT_REQ = 5;
    /** 聊天响应 */
    public static final int CHAT_RESP = 6;
    /** 聊天内容 */
    public static final int CHAT_MSG = 7;

    /** 心跳 */
    public static final int PING = 8;
    public static final int PONG = 9;
}
