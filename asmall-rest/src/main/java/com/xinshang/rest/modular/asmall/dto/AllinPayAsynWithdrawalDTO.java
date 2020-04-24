package com.xinshang.rest.modular.asmall.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

/**
 * 提现异步接收
 * @author lyk
 */
@Data
public class AllinPayAsynWithdrawalDTO implements Serializable {

    private static final long serialVersionUID = 1013834454044014930L;
    /**
     * 服务调用是否成功 String “OK”：表示成功。
     */
    private String status;

    /**
     * 服务调用成功后的返回结果，是一个嵌套的 String 类型的 JSON 对象，仅当 status=OK 时有效。
     */
    private JSONObject returnValue;

    /**
     * sign 签名 String
     */
    private String sign;

    /**
     * 服务
     */
    private String service;

    /**
     * 方法
     */
    private String method;



}
