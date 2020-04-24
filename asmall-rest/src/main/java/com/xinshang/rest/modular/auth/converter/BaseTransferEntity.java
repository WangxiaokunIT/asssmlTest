package com.xinshang.rest.modular.auth.converter;

import lombok.Data;

/**
 * 基础的传输bean
 *
 * @author fengshuonan
 * @date 2017-08-25 15:52
 */
@Data
public class BaseTransferEntity {

    /**
     *  base64编码的json字符串
     */
    private String object;

    /**
     * 签名
     */
    private String sign;

}
