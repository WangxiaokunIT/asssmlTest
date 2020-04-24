package com.xinshang.modular.biz.dto;

import lombok.Data;

/**
 *
 * @author zhangjiajia
 */
@Data
public class AllinPayBindingPhoneDTO {

    /**
     * 手机号
     */
    private String phone;

    /**
     * 类型 9-绑定手机
     *     6-解绑手机
     */
    private String verificationCode ;

    /**
     * 商户系统用户标识
     */
    private String bizUserId;
}
