package com.xinshang.modular.biz.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 周帅
 */
@Data
public class APCreateJoininRespDTO implements Serializable {

    private static final long serialVersionUID = 1806786008504124841L;
    /**
     * 支付状态
     */
    private String payStatus;

    /**
     * 支付失败信息
     */
    private  String payFailMessage;

    /**
     *  商户系统用户标识，商户 系统中唯一编号
     */
    private  String bizUserId;

    /**
     *  通商云订单号
     */
    private  String orderNo;

    /**
     * 商户订单号（支付订单
     */
    private  String bizOrderNo;

    /**
     *  交易编号
     */
    private  String tradeNo;

    /**
     * POS支付的付款码
     */
    private  String payCode;

    /**
     * 扩展参数
     */
    private  String extendInfo;

    /**
     * 微信APP支付信息
     */
    private JSONObject weChatAPPInfo;

    /**
     *  扫码支付信息/ JS 支付串 信息/微信原生H5 支付串 信息
     */
    private String payInfo;

    /**
     *  交易验证方式
     */
    private Long validateType;

}
