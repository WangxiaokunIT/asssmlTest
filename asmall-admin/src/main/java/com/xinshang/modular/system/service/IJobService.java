package com.xinshang.modular.system.service;

import com.xinshang.modular.system.model.Job;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * @author: 王晓坤
 * @date: 2018/8/3 16:09
 * @desc: 定时任务调度表 服务类
 */
public interface IJobService extends IService<Job> {


    /**
     * 根据jobClass和jobGroup获取任务
     * @param jobClassName
     * @param jobGroup
     * @return
     */
    Job getJobByJobClassNameAndJobGroup(String jobClassName,String jobGroup) ;

    /**
     * 根据条件查询任务
     * @return
     */
    List<Job> listByCondition(String condition);

    /**
     * 立即运行任务 一次
     *
     * @param job 调度信息
     * @author wangxiaokun
     * @return 结果
     */
    Boolean runOnce(Job job);

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
    Boolean insertJob(Job job);

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
    Boolean deleteJob(Job job);

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
    Boolean changeJobStatus(Job job);

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
    Boolean updateJob(Job job);
}
