package com.xinshang.modular.biz.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhangjiajia
 */
@Data
public class AllinPayAsynResponseDTO implements Serializable {

    private static final long serialVersionUID = 8839532292150473185L;
    /**
     * 分配的系统编号
     */
    private String sysid;

    /**
     * 签名
     */
    private String sign;

    /**
     * 服务请求的 JSON 对象，参与签名，非必填参数在报文可出现，也可不出现
     */
    private String rps;

    /**
     * 请求时间戳
     */
    private String timestamp;

    /**
     * 接口版本(现为 2.0)
     */
    private String v;

}
