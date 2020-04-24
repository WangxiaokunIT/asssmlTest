package com.xinshang.core.util;

import cn.hutool.core.util.StrUtil;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * @author: 王晓坤
 * @date: 2018/8/2 14:44
 * @desc: 执行定时任务
 */
public class ScheduleRunnable implements Runnable {
    private Object target;
    private Method method;
    private String params;

    public ScheduleRunnable(String beanName, String methodName, String params)
            throws NoSuchMethodException, SecurityException {
        this.target = SpringUtils.getBean(beanName);
        this.params = params;
        if (StrUtil.isNotEmpty(params)) {
            this.method = target.getClass().getDeclaredMethod(methodName, String.class);
        } else {
            this.method = target.getClass().getDeclaredMethod(methodName);
        }
    }

    @Override
    public void run() {
        try {
            ReflectionUtils.makeAccessible(method);
            if (StrUtil.isNotEmpty(params)) {
                method.invoke(target, params);
            } else {
                method.invoke(target);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
