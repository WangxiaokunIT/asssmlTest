package com.xinshang.modular.im.dto;

import lombok.*;

/**
 * 发送消息实体
 * @author zhangjiajia
 * @since 19-7-5
 */
@Data
public class SendMsgDTO {

    /**
     * 用户名
     */
    private String username;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 用户id
     */

    private Integer id;
    /**
     * 聊天窗口来源类型
     */
    private String type;
    /**
     * 消息内容
     */
    private String content;
    /**
     * 是否我发送的消息，如果为true，则会显示在右方
     */
    private Boolean mine;
    /**
     * 消息的发送者id
     */
    private Integer fromid;
    /**
     * 服务端时间戳毫秒数
     */
    private Long timestamp;
}
