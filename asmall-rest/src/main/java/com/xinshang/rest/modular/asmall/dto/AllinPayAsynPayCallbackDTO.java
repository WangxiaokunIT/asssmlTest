package com.xinshang.rest.modular.asmall.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @Auther: wangxiaokun
 * @Date: 2019/12/19:15:26
 * @Description:
 */
@Data
public class AllinPayAsynPayCallbackDTO {


    private static final long serialVersionUID = -4553789337126137921L;

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
    private JSONObject returnValue;

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
