package com.xinshang.rest.common.exception;

import com.xinshang.rest.common.enums.BizExceptionEnum;
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
public class BizException extends RuntimeException {

    private Integer code;

    private String message;

    public BizException(BizExceptionEnum bizExceptionEnum) {
        this.code = bizExceptionEnum.getCode();
        this.message = bizExceptionEnum.getMessage();
    }

    public BizException(String message) {
        this.code = 400;
        this.message=message;
    }

}
