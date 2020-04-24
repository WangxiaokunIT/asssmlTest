package com.xinshang.modular.im.dto;

import lombok.Data;

/**
 * 对方信息
 * @author zhangjiajia
 * @since 19-7-5
 */
@Data
public class ReceiveToInfo {

    /**
     * 头像
     */
    private String avatar;
    /**
     * 用户id
     */
    private Integer id;
    /**
     * 名称
     */
    private String name;
    /**
     * 签名
     */
    private String sign;
    /**
     * 聊天类型，一般分friend和group两种，group即群聊
     */
    private String type;
    /**
     * 对方昵称
     */
    private String username;

    /**
     * 群昵称
     */
    private String groupname;

}
