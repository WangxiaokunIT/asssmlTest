/*
 *
 *      Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the pig4cloud.com developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: lengleng (wangiegie@gmail.com)
 *
 */

package com.xinshang.rest.common.enums;

/**
 * @author lengleng
 * @date 2017/10/29
 */
public interface CommonConstants {

	/**
	 * 项目名
	 */
	String PROJECT_NAME = "asmall-rest";
	/**
	 * 默认成功code
	 */
	Integer SUCCESS = 200;
	/**
	 * 默认失败code
	 */
	Integer FAIL = 400;


	/**
	 * 通联接口返回成功
	 *
	 */
	 String SUCCESS_CODE="OK";


	/**
	 * 验证码redis key
	 */

	String SMS_CAPTCHA_KEY = PROJECT_NAME+":"+"sms:captcha";

	/**
	 * 验证码每日限制redis key
	 */
	String SMS_CAPTCHA_DAY_COUNT_KEY = PROJECT_NAME+":sms_captcha_day_count";

	/**
	 * 用户启用状态
	 */
	Integer USER_STATUS_ENABLE_CODE = 1;

	/**
	 * 用户禁用状态
	 */
	Integer USER_STATUS_PROHIBIT_CODE = 0;

	String MEMBER_PASSWORD_MD5_KEY="815c273dc82fa32d";
}
