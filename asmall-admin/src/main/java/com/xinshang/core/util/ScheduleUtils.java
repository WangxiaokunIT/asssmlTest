package com.xinshang.core.util;

import com.xinshang.modular.system.model.Job;
import com.xinshang.schedule.BaseJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: 王晓坤
 * @date: 2018/8/2 15:06
 * @desc: 定时任务工具类
 */
@Slf4j
public class ScheduleUtils {

    /**
     * 创建定时任务
     */
    public static Boolean createScheduleJob(Scheduler scheduler, Job job) {
        try {
            String jobClassName =job.getJobClassName();
            String jobGroupName = job.getJobGroup();
            String cronExpression = job.getCronExpression();
            String description = job.getDescription();
            scheduler.start();

            // 构建job信息
            JobDetail jobDetail =JobBuilder.newJob(getClass(job.getJobClassName()).getClass()).withIdentity(jobClassName, jobGroupName).withDescription(description).build();

            // 表达式调度构建器(即任务执行的时间)
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

            // 按新的cronExpression表达式构建一个新的trigger
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobClassName, jobGroupName).withSchedule(scheduleBuilder).build();

            scheduler.scheduleJob(jobDetail, trigger);
           log.error("创建定时任务成功");
            return Boolean.TRUE;
        } catch (SchedulerException e) {
            log.error("创建定时任务失败" + e);
            return Boolean.FALSE;
        } catch (Exception e1){
            log.error("创建定时任务失败" + e1);
            return Boolean.FALSE;
        }

    }

    /**
     * 更新定时任务
     */
    public static Boolean updateScheduleJob(Scheduler scheduler, Job job) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobClassName(), job.getJobGroup());
            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());

            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

            // 按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

            // 按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);
            return Boolean.TRUE;
        } catch (SchedulerException e) {
            log.error("更新定时任务失败" + e);
            return Boolean.FALSE;
        }
    }

    /**
     * 立即执行任务
     */
    public static Boolean runOnce(Scheduler scheduler, Job job) {

        try {
            scheduler.triggerJob(JobKey.jobKey(job.getJobClassName(), job.getJobGroup()));
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error(e.getMessage());
            return Boolean.FALSE;
        }
    }

    /**
     * 暂停任务
     */
    public static Boolean pauseJob(Scheduler scheduler, Job job) {
        try {
            scheduler.pauseJob(JobKey.jobKey(job.getJobClassName(), job.getJobGroup()));
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error(e.getMessage());
            return Boolean.FALSE;
        }
    }

    /**
     * 恢复任务
     */
    public static Boolean resumeJob(Scheduler scheduler, Job job) {
        try {
            scheduler.resumeJob(JobKey.jobKey(job.getJobClassName(), job.getJobGroup()));
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error(e.getMessage());
            return Boolean.FALSE;
        }
    }

    /**
     * 删除定时任务
     */
    public static Boolean deleteScheduleJob(Scheduler scheduler, Job job) {
        try {

            scheduler.pauseTrigger(TriggerKey.triggerKey(job.getJobClassName(), job.getJobGroup()));
            scheduler.unscheduleJob(TriggerKey.triggerKey(job.getJobClassName(), job.getJobGroup()));
            scheduler.deleteJob(JobKey.jobKey(job.getJobClassName(), job.getJobGroup()));
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error(e.getMessage());
            return Boolean.FALSE;
        }
    }

    /**
     * 根据名称获取class
     * @param className
     * @return
     * @throws Exception
     */
    public static BaseJob getClass(String className) throws Exception {
        Class<?> class1 = Class.forName(className);
        return (BaseJob) class1.newInstance();
    }
}
