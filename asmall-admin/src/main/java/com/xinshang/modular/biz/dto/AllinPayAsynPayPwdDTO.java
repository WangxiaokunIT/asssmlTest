package com.xinshang.modular.biz.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhangjiajia
 */
@Data
public class AllinPayAsynPayPwdDTO implements Serializable {

    private static final long serialVersionUID = 6927801608677024880L;

    /**
     * 服务对象
     */
    private String service;

    /**
     * 调用方法
     */
    private String method;

    /**
     * 请求参数，嵌套的 JSON 对象，key 为参数名称，value 为参数值
     */
    private String signedValue;

    /**
     * 是否签订成功
     */
    private String status;

    /**
     * 错误代码
     */
    private String errorCode;

    /**
     * 错误信息
     */
    private String message;

}
