package com.xinshang.rest.modular.asmall.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhangjiajia
 */
@NoArgsConstructor
@Data
public class AliyunSmsResponseDTO {

    /**
     * Message : OK
     * RequestId : 2184201F-BFB3-446B-B1F2-C746B7BF0657
     * BizId : 197703245997295588^0
     * Code : OK
     */

    /**
     * 状态码的描述
     */
    private String Message;
    /**
     * 请求ID。
     */
    private String RequestId;
    /**
     * 发送回执ID，可根据该ID在接口QuerySendDetails中查询具体的发送状态。
     */
    private String BizId;
    /**
     * 请求状态码
     * 返回OK代表请求成功。
     * 其他错误码详见
     */
    private String Code;
}
