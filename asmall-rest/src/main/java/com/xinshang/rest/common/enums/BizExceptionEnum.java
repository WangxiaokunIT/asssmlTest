package com.xinshang.rest.common.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 所有业务异常的枚举
 *
 * @author fengshuonan
 * @date 2016年11月12日 下午5:04:51
 */
@Getter
@AllArgsConstructor
public enum BizExceptionEnum {

    /**
     * 字典
     */
    DICT_EXISTED(400,"字典已经存在"),
    ERROR_CREATE_DICT(500,"创建字典失败"),
    ERROR_WRAPPER_FIELD(500,"包装字典属性失败"),
    ERROR_CODE_EMPTY(500,"字典类型不能为空"),

    /**
     * token异常
     */
    TOKEN_EXPIRED(700, "token过期"),
    TOKEN_ERROR(700, "token验证失败"),

    /**
     * 签名异常
     */
    SIGN_ERROR(700, "签名验证失败"),

    /**
     * 其他
     */
    AUTH_REQUEST_ERROR(400, "账号密码错误"),

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
     * 用户异常
     */
    USER_DOES_NOT_EXIST(400,"用户不存在"),
    USER_EXIST(400,"用户已存在"),
    USER_CREATION_FAILED(400,"用户创建失败"),
    USER_IS_NOT_ALLOWED_TO_LOG_IN(400,"用户已被禁止登录"),
    INCORRECT_VERIFICATION_CODE(400,"验证码不正确"),
    VERIFICATION_FORMAT_ERROR(400,"验证码格式不正确"),
    PHONE_NO_VALIDATION_FAILED(400,"手机号格式不正确"),
    PASSWORD_EMPTY_FAILED(400,"密码不能为空"),
    CONFIRM_PASSWORD_EMPTY_FAILED(400,"确认密码不能为空"),
    TWO_PASSWORDS_ARE_INCONSISTENT_FAILED(400,"两次密码不一致"),
    PASSWORD_VALIDATION_FAILED(400,"密码不正确"),
    SMS_SEND_TYPE_FAILED(400,"验证码类型不正确"),
    SMS_SEND_FAILED(400,"发送验证码失败"),
    BINDING_PHONE_FAILED(400,"绑定手机失败"),
    USER_SET_REAL_FAILD(400,"实名认证失败"),
    USER_APPLY_BANK_FAILED(400,"发送绑卡请求失败"),
    USER_BIND_BANK_FAILED(400,"绑卡失败"),
    USER_UNBIND_BANK_FAILED(400,"解绑失败"),
    USER_SET_PAYPWD_FAILED(400,"设置支付密码失败"),
    USER_PAY_GOODS_FAILED(400,"消费申请失败"),
    USER_CONFIRM_GOODS_FAILED(400,"支付确认失败"),
    /**
     * 加盟异常
     */
    JOININ_COLLECTION_FAILED(400,"代收失败");


    private final Integer code;

    private final String message;



}
