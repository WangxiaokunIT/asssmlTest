package com.xinshang.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * @author zhangjiajia
 */
@EnableScheduling
@Configuration
@ConditionalOnProperty(prefix = "asmall", name = "scheduler-open", havingValue = "true")
@Slf4j
public class SchedulerConfig implements SchedulerFactoryBeanCustomizer{

    @Override
    public void customize(SchedulerFactoryBean schedulerFactoryBean) {
        log.info("开始执行定时任务初始化");
        schedulerFactoryBean.setStartupDelay(2);
        schedulerFactoryBean.setAutoStartup(true);
        schedulerFactoryBean.setOverwriteExistingJobs(true);
    }

}
