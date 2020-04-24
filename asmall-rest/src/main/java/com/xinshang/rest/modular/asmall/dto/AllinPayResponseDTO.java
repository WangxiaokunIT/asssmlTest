package com.xinshang.rest.modular.asmall.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhangjiajia
 */
@Data
public class AllinPayResponseDTO<T> implements Serializable {


    private static final long serialVersionUID = -5345298616378494151L;
    /**
     * 服务调用是否成功，“OK”表示成功，“error”表示失败。
     */
    private String status;

    /**
     * 返回内容，同时也是签名内容。
     * 服务调用成功后的返回结果，是一个嵌套的 String 类型的 JSON 对象，仅当
     * status=OK 时有效
     */
    private T signedValue;

    /**
     * 签名
     */
    private String sign;

    /**
     * 当请求失败时返回的错误代码，仅当 status=error 时有效
     */
    private String errorCode;

    /**
     * 当请求失败时返回的错误信息，仅当 status=error 时有效
     */
    private String message;
}
