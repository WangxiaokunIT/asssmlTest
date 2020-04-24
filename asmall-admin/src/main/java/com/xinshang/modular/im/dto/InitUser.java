package com.xinshang.modular.im.dto;

import lombok.Data;

/**
 * @author zhangjiajia
 * @since 19-7-5
 */
@Data
public class InitUser {

    /**
     * 用户名
     */
    private String username;
    /**
     * 用户id
     */
    private Integer id;
    /**
     * 是否在线
     */
    private String status;
    /**
     * 签名
     */
    private String sign;
    /**
     * 头像
     */
    private String avatar;
}
