package com.xinshang.core.base.tips;

/**
 * 返回给前台的错误提示
 *
 * @author fengshuonan
 * @date 2016年11月12日 下午5:05:22
 */
public class ErrorTip extends Tip {

    public ErrorTip(String message) {
        super();
        this.code = 400;
        this.message = message;
    }

    public ErrorTip(Integer code,String message) {
        super();
        this.code = code;
        this.message = message;
    }
}
