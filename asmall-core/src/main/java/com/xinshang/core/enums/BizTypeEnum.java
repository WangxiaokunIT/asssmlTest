package com.xinshang.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 所有业务编码类型
 * @author zhangjiajia
 */
@AllArgsConstructor
@Getter
public enum BizTypeEnum {

	/**
	 * 手机号码
	 */
	PHONE_NO("PN","手机号码"),

	/**
	 * 会员编码
	 */
	MEMBER_NO("MN","会员编码"),

	/**
	 * 商品编码
	 */
	GOODS_NO("GN","商品编码"),

	/**
	 * 商品规格编码
	 */
	SPECS_NO("SN","商品规格编码"),

	/**
	 * 普通订单
	 */
	GENERAL_ORDER("GO","普通订单"),
	/**
	 * 积分兑换订单
	 */
	INTEGRAL_ORDER("IO","兑换订单"),
	/**
	 * 普通客户
	 */
	GENERAL_CUSTOMER("GC","普通客户"),
	/**
	 * VIP客户-投资方
	 */
	VIP_CUSTOMER("VC","VIP客户"),
	/**
	 * 供应商客户-借款方
	 */
	SUPPLIER_CUSTOMER("SU","供应商客户"),
	/**
	 * 加盟
	 */
	JOIN_IN("JI","加盟"),
	/**
	 * 充值
	 */
	DEPOSIT_MONEY("DM","充值"),
	/**
	 * 退款
	 */
	REFUND_MONEY("RM","退款"),
	/**
	 * 提现
	 */
	CASH_WITHDRAWAL("CW","提现"),
	/**
	 * 代收
	 */
	AGENT_COLLECTION("AC","代收"),
	/**
	 * 代付
	 */
	AGENCY_PAYMENT("AP","代付"),
	/**
	 * 批次号
	 */
	BATCH_NUMBER("BN","批量代付"),
	/**
	 * 消费
	 */
	COST_NUMBER("CS","消费");

	private final String value;

	private final String name;

}
