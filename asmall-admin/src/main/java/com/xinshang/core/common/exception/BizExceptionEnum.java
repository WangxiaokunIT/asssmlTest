package com.xinshang.core.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @desc 所有业务异常的枚举
 * @author fengshuonan
 * @date 2016年11月12日 下午5:04:51
 */
@AllArgsConstructor
@Getter
public enum BizExceptionEnum{

	/**
	 * 字典
	 */
	DICT_EXISTED(400,"字典已经存在"),
	ERROR_CREATE_DICT(500,"创建字典失败"),
	ERROR_WRAPPER_FIELD(500,"包装字典属性失败"),
	ERROR_CODE_EMPTY(500,"字典类型不能为空"),

	/**
	 * 文件上传
	 */
	FILE_READING_ERROR(400,"FILE_READING_ERROR!"),
	FILE_NOT_FOUND(400,"FILE_NOT_FOUND!"),
	UPLOAD_ERROR(500,"上传图片出错"),

	/**
	 * 权限和数据问题
	 */
	DB_RESOURCE_NULL(400,"数据库中没有该资源"),
	NO_PERMITION(405, "权限异常"),
	REQUEST_INVALIDATE(400,"请求数据格式不正确"),
	INVALID_KAPTCHA(400,"验证码不正确"),
	CANT_DELETE_ADMIN(600,"不能删除超级管理员"),
	CANT_FREEZE_ADMIN(600,"不能冻结超级管理员"),
	CANT_CHANGE_ADMIN(600,"不能修改超级管理员角色"),

	/**
	 * 账户问题
	 */
	USER_ALREADY_REG(401,"该用户已经注册"),
	NO_THIS_USER(400,"没有此用户"),
	USER_NOT_EXISTED(400, "没有此用户"),
	ACCOUNT_FREEZED(401, "账号被冻结"),
	OLD_PWD_NOT_RIGHT(402, "原密码不正确"),
	TWO_PWD_NOT_MATCH(405, "两次输入密码不一致"),

	/**
	 * 错误的请求
	 */
	MENU_PCODE_COINCIDENCE(400,"菜单编号和副编号不能一致"),
	EXISTED_THE_MENU(400,"菜单编号重复，不能添加"),
	DICT_MUST_BE_NUMBER(400,"字典的值必须为数字"),
	REQUEST_NULL(400, "请求有错误"),
	SESSION_TIMEOUT(400, "会话超时"),
	SERVER_ERROR(500, "服务器异常"),
	/**
	 * select标签问题
	 */
	SELECT_TAG_TABLE_NAME_NULL(400,"表名字不能为空"),
	SELECT_TAG_TEXT_COLUMN_NULL(400,"显示字段不能为空"),
	SELECT_TAG_VALUE_COLUMN_NULL(400,"值字段不能为空"),


	/**
	 * 用户异常
	 */
	USER_DOES_NOT_EXIST(400,"用户不存在"),
	USER_CREATION_FAILED(400,"用户创建失败"),
	USER_IS_NOT_ALLOWED_TO_LOG_IN(400,"用户已被禁止登录"),
	INCORRECT_VERIFICATION_CODE(400,"验证码不正确"),
	VERIFICATION_FORMAT_ERROR(400,"验证码格式不正确"),
	PHONE_NO_VALIDATION_FAILED(400,"手机号格式不正确"),
	SMS_SEND_TYPE_FAILED(400,"验证码类型不正确"),
	SMS_SEND_FAILED(400,"发送验证码失败"),
	BINDING_PHONE_FAILED(400,"绑定手机失败"),
	USER_SET_REAL_FAILD(400,"实名认证失败"),
	USER_APPLY_BANK_FAILED(400,"发送绑卡请求失败"),
	/**
	 * 加盟异常
	 */
	JOININ_COLLECTION_FAILED(400,"代收失败"),
	USER_BIND_BANK_FAILED(400,"绑卡失败");

	private final Integer code;

	private final String message;

}
