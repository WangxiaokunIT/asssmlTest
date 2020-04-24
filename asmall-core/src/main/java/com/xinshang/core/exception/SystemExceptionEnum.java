package com.xinshang.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * asmall异常枚举
 *
 * @author fengshuonan
 * @date 2017/12/28 下午10:33
 */
@Getter
@AllArgsConstructor
public enum SystemExceptionEnum {

    /**
     * 其他
     */
    INVLIDE_DATE_STRING(400, "输入日期格式不对"),

    /**
     * 其他
     */
    WRITE_ERROR(500, "渲染界面错误"),

    /**
     * 文件上传
     */
    FILE_READING_ERROR(400, "FILE_READING_ERROR!"),
    FILE_NOT_FOUND(400, "FILE_NOT_FOUND!"),

    FILE_UPLOAD_ERROR(500,"文件上传失败"),

    /**
     * 错误的请求
     */
    REQUEST_NULL(400, "请求有错误"),
    SERVER_ERROR(500, "服务器异常");

    private final Integer code;

    private final String message;


}
