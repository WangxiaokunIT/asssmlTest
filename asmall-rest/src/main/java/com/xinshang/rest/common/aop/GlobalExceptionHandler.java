package com.xinshang.rest.common.aop;

import com.xinshang.core.exception.SystemException;
import com.xinshang.core.exception.SystemExceptionEnum;
import com.xinshang.rest.common.enums.BizExceptionEnum;
import com.xinshang.rest.common.exception.BizException;
import com.xinshang.rest.common.util.R;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 全局的的异常拦截器（拦截所有的控制器）（带有@RequestMapping注解的方法上都会拦截）
 *
 * @author fengshuonan
 * @date 2016年11月12日 下午3:19:56
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler{

    /**
     * 拦截jwt相关异常
     */
    @ExceptionHandler(JwtException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public R jwtException(JwtException e) {
        return R.failed(BizExceptionEnum.TOKEN_ERROR.getCode(), BizExceptionEnum.TOKEN_ERROR.getMessage());
    }

    /**
     * 拦截系统异常
     */
    @ExceptionHandler(SystemException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public R notFount(SystemException e) {
        log.error("系统异常:", e);
        return R.failed(e.getCode(), e.getMessage());
    }

    /**
     * 拦截业务异常
     */
    @ExceptionHandler(BizException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public R notFount(BizException e) {
        log.error("业务异常:", e);
        return R.failed(e.getCode(), e.getMessage());
    }

    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public R notFount(RuntimeException e) {
        if(e instanceof IllegalArgumentException){
            return R.failed(SystemExceptionEnum.REQUEST_NULL.getCode(), e.getMessage());
        }
        log.error("运行时异常:", e);
        return R.failed(SystemExceptionEnum.SERVER_ERROR.getCode(), SystemExceptionEnum.SERVER_ERROR.getMessage());
    }
}
