package com.xinshang.core.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 封装asmall的异常
 *
 * @author fengshuonan
 * @date 2017/12/28 下午10:32
 */
@Data
@AllArgsConstructor
public class SystemException extends RuntimeException {

    private Integer code;

    private String message;

    public SystemException(SystemExceptionEnum systemExceptionEnum) {
        this.code = systemExceptionEnum.getCode();
        this.message = systemExceptionEnum.getMessage();
    }

    public SystemException(String message) {
        this.code = 400;
        this.message=message;
    }

}
