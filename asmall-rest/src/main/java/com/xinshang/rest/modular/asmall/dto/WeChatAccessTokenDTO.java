package com.xinshang.rest.modular.asmall.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class WeChatAccessTokenDTO implements Serializable {
    private static final long serialVersionUID = -3892851298834694581L;
    /**
     * 网页授权接口调用凭证
     */
    private String access_token;
    /**
     * 用户唯一标识，请注意，在未关注公众号时，用户访问公众号的网页，也会产生一个用户和公众号唯一的OpenID
     */
    private String openid;

    /**
     * 用户授权的作用域，使用逗号（,）分隔
     */
    private String scope;

    /**
     * 用户刷新access_token
     */
    private String refresh_token;

    /**
     * access_token接口调用凭证超时时间，单位（秒）
     */
    private String expires_in;

    /**
     * 错误码
     */
    private String errcode;

    /**
     * 错误信息
     */
    private String errmsg;
}
