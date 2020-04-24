package com.xinshang.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @desc 所有业务异常的枚举
 * @author fengshuonan
 * @date 2016年11月12日 下午5:04:51
 */
@AllArgsConstructor
@Getter
public enum UserTypeEnum {
	/**
	 * 普通客户-购买商品
	 */
	CUSTOMER("1","客户"),
	/**
     * 供应商客户-借款方
	 */
	SUPPLIER("2","供应商"),

	/**
	 * 平台
	 */
	PLATFORM("3","平台");

	private final String value;

	private final String name;

}
