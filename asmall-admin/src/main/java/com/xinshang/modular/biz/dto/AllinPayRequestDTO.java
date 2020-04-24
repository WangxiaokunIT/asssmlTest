package com.xinshang.modular.biz.dto;

import lombok.Data;

/**
 * @author zhangjiajia
 */
@Data
public class AllinPayRequestDTO {

    /**
     * 请求调用的服务对象
     */
    private String service;

    /**
     * 请求调用的方法
     */
    private String method;

    /**
     * 请求参数，也是一个嵌套的 JSON 对象，key 为参数名称，value 为参数值
     */
    private Object param;
}
