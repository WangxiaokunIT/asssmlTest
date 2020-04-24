package com.xinshang.rest.task;

import com.xinshang.rest.common.enums.CommonConstants;
import com.xinshang.rest.common.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author zhangjiajia
 */
@Component
@EnableScheduling
@Slf4j
public class BizTask {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 默认是fixedDelay 上一次执行完毕时间后执行下一轮
     */
    @Async
    @Scheduled(cron = "0 0 0 * * *")
    public void expireDayPhoneCount() {

        log.info("开始清除所有每日验证码发送限制");
        redisUtil.del(CommonConstants.SMS_CAPTCHA_DAY_COUNT_KEY);
        log.info("清除所有每日验证码发送限制完毕");
    }
}