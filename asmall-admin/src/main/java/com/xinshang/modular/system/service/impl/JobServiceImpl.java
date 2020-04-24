package com.xinshang.modular.system.service.impl;

import com.xinshang.constant.ScheduleOperationEnum;
import com.xinshang.modular.system.dao.JobMapper;
import com.xinshang.modular.system.model.Job;
import com.xinshang.modular.system.service.IJobService;
import com.xinshang.core.util.ScheduleUtils;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
 * @author: 王晓坤
 * @date: 2018/8/3 16:11
 * @desc: 定时任务调度表 服务实现类
 */
@Service
public class JobServiceImpl extends ServiceImpl<JobMapper, Job> implements IJobService {

    @Autowired
    private Scheduler scheduler;

    @Resource
    private JobMapper jobMapper;


    /**
     * 根据className和group获取任务
     * @param jobClassName
     * @param jobGroupName
     * @return
     */
    @Override
    public Job getJobByJobClassNameAndJobGroup(String jobClassName,String jobGroupName) {
        return jobMapper.getJobByJobClassNameAndJobGroup(jobClassName,jobGroupName);
    }

    /**
     * 根据任务查询任务
     * @return
     */
    @Override
    public List<Job> listByCondition(String condition) {
        return jobMapper.listByCondition(condition);
    }


    /**
     * 立即运行任务 一次
     *
     * @param job 调度信息
     * @return 结果
     * @author 王晓坤
     */
    @Override
    public Boolean runOnce(Job job) {
       return ScheduleUtils.runOnce(scheduler, job);
    }

    /**
     * 功能描述:
     *
     * @Param: [job]
     * @Return: boolean
     * @author: 王晓坤
     * @date: 2018/8/3 10:23
     * @desc: 重写新增定时任务job方法
     * @Modify:
     */
    @Override
    public Boolean insertJob(Job job) {
        return ScheduleUtils.createScheduleJob(scheduler,job);
    }


    /**
     * 功能描述:
     *
     * @Param: [job]
     * @Return: java.lang.Integer
     * @author: 王晓坤
     * @date: 2018/8/3 16:11
     * @desc: 开启或关闭定时任务，修改状态
     * @Modify:
     */
    @Override
    public Boolean changeJobStatus(Job job) {

        String status =job.getTriggerState();
        ScheduleOperationEnum sse = ScheduleOperationEnum.fromValue(status);
        Boolean isSuccess=true;
        switch (sse){
            case RESUME:
                isSuccess = ScheduleUtils.resumeJob(scheduler, job);
                break;
            case PAUSE:
                isSuccess = ScheduleUtils.pauseJob(scheduler, job);
                break;
            default:
                break;
        }
        return isSuccess;
    }


    /**
     * 功能描述:
     *
     * @Param: [job]
     * @Return: java.lang.Integer
     * @author: 王晓坤
     * @date: 2018/8/7 10:17
     * @desc: 修改定时任务
     * @Modify:
     */
    @Override
    public Boolean updateJob(Job job) {
        return ScheduleUtils.updateScheduleJob(scheduler,job);
    }


    /**
     * 功能描述:
     *
     * @Param: [jobId]
     * @Return: boolean
     * @author: 王晓坤
     * @date: 2018/8/3 10:48
     * @desc: 重写删除定时任务job方法
     * @Modify:
     */
    @Override
    public Boolean deleteJob(Job job) {
        return ScheduleUtils.deleteScheduleJob(scheduler, job);
    }
}
