package com.xinshang.modular.biz.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zhangjiajia
 */
@Data
public class AllinPayAsynSetCompanyInfoDTO implements Serializable {

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
     * 错误代码
     */
    private String errorCode;

    /**
     * 错误信息
     */
    private String message;

}
