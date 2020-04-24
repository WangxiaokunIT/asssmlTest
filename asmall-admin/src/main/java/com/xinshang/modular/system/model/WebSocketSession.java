package com.xinshang.modular.system.model;

import lombok.Data;

import javax.websocket.Session;

/**
 * @author zhangjiajia
 * @since 19-7-4
 */
@Data
public class WebSocketSession {

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     *  接收userId
     */
    private String userId="";

}
