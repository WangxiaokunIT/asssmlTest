package com.xinshang.rest.modular.asmall.dto;

import lombok.Data;

/**
 * @Auther: wangxiaokun
 * @Date: 2019/11/3 16:55
 * @Description:
 */
@Data
public class OrderConsumeRespDTO {

    private String payStatus;
    private String payFailMessage;
    private String bizUserId;
    private String orderNo;
    private String bizOrderNo;
    private String tradeNo;
    private String payCode;
    private String extendInfo;
    private String weChatAPPInfo;
    private String payInfo;
    private String validateType;


}
