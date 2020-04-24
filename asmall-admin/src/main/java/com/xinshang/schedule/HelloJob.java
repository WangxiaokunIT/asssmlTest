package com.xinshang.schedule;

import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSON;
import com.xinshang.constant.Constants;
import com.xinshang.core.util.SpringUtils;
import com.xinshang.modular.system.model.JobLog;
import com.xinshang.modular.system.service.IJobLogService;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.Future;

/**
 * @author zhangjiajia
 * @date 2019年2月16日 16:56:18
 */
@Component
public class HelloJob implements BaseJob {  

    @Autowired
    private IJobLogService jobLogService;

    private static Logger logger = LoggerFactory.getLogger(HelloJob.class);

    @Override
    public void execute(JobExecutionContext context) {
        JobLog jobLog = new JobLog();
        jobLog.setJobName(context.getJobDetail().getJobClass().getName());
        jobLog.setDescription(context.getJobDetail().getDescription());
        jobLog.setJobClassName(context.getJobDetail().getJobClass().getName());
        jobLog.setCreateTime(new Date());
        long startTime = System.currentTimeMillis();
        StringBuffer sb = new StringBuffer("任务开始时间:"+DateTime.now().toString());
        long times=0L;
        try {
            // 任务执行开始

            logger.info("任务正在执行。。");

            //任务执行结束
            times= System.currentTimeMillis() - startTime;
            jobLog.setState(Constants.SUCCESS);
            sb.append(",任务结束时间:"+DateTime.now().toString()+",任务执行成功,总共耗时：" + times + "毫秒");
            jobLog.setJobMessage(sb.toString());
            logger.info(JSON.toJSONString(jobLog));
        } catch (Exception e) {

            times = System.currentTimeMillis() - startTime;
            sb.append(",任务结束时间:"+DateTime.now().toString()+",任务执行失败,总共耗时：" + times + "毫秒");
            jobLog.setJobMessage(sb.toString());
            jobLog.setState(Constants.FAIL);
            jobLog.setExceptionInfo(e.toString());
            logger.error(JSON.toJSONString(jobLog));
        } finally {
            jobLogService.insert(jobLog);

        }

    }
}  
