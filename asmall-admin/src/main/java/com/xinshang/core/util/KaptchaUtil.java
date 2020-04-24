package com.xinshang.core.util;


import com.xinshang.config.properties.SystemProperties;

/**
 * @author zhangjiajia
 * @date 2018年11月29日 16:06:25
 * 验证码工具类
 */
public class KaptchaUtil {

    /**
     * 获取验证码开关
     */
    public static Boolean getKaptchaOnOff() {
        return SpringContextHolder.getBean(SystemProperties.class).getKaptchaOpen();
    }
}