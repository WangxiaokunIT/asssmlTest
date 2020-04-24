package com.xinshang.modular.im.dto;

import lombok.Data;

/**
 * @author zhangjiajia
 * @since 19-7-5
 */
@Data
public class ReceiveMineInfo {

    /**
     * 头像
     */
    private String avatar;
    /**
     * 消息内容
     */
    private String content;
    /**
     * 用户id
     */
    private Integer id;
    /**
     * 是否我发送的消息
     */
    private Boolean mine;
    /**
     * 昵称
     */
    private String username;

}
