package com.xinshang.modular.system.controller;

import com.xinshang.core.base.controller.BaseController;
import com.xinshang.core.base.tips.ErrorTip;
import com.xinshang.core.log.LogObjectHolder;
import com.xinshang.modular.system.model.Job;
import com.xinshang.modular.system.service.IJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: 王晓坤
 * @date: 2018/8/3 10:02
 * @desc: 定时任务控制器
 */
@Controller
@RequestMapping("/job")
public class JobController extends BaseController {

    private String PREFIX = "/system/job/";

    @Autowired
    private IJobService jobService;


    /**
     * 功能描述:
     *
     * @Param: [job]
     * @Return: java.lang.Object
     * @author: 王晓坤
     * @date: 2018/8/3 9:20
     * @desc: 启动或关闭定时任务
     * @Modify:
     */
    @PostMapping("/changeJobStatus")
    @ResponseBody
    public Object changeJobStatus(Job job) {

        if (jobService.changeJobStatus(job)) {
            return SUCCESS_TIP;
        }
        return new ErrorTip(201, "状态改变失败");
    }


    /**
     * 功能描述:
     *
     * @Param: [job]
     * @Return: java.lang.Object
     * @author: 王晓坤
     * @date: 2018/8/2 15:19
     * @desc: 任务调度立即执行一次
     * @Modify:
     */
    @PostMapping("/runOnce")
    @ResponseBody
    public Object runOnce(Job job) {

        if (jobService.runOnce(job)) {
            return SUCCESS_TIP;
        }
        return new ErrorTip(201, "执行失败");
    }


    /**
     * 跳转到定时任务首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "job.html";
    }

    /**
     * 跳转到添加定时任务
     */
    @RequestMapping("/job_add")
    public String jobAdd() {
        return PREFIX + "job_add.html";
    }

    /**
     * 跳转到修改定时任务
     */
    @RequestMapping("/job_update")
    public String jobUpdate(Job jobParm,Model model) {
        Job job = jobService.getJobByJobClassNameAndJobGroup(jobParm.getJobClassName(),jobParm.getJobGroup());
        model.addAttribute("item", job);
        LogObjectHolder.me().set(job);
        return PREFIX + "job_edit.html";
    }

    /**
     * 获取定时任务列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        List<Job> jobs = jobService.listByCondition(condition);
        return jobs;
    }

    /**
     * 新增定时任务
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Job job) {
        if(jobService.insertJob(job)){
            return SUCCESS_TIP;
        }

        return new ErrorTip(201, "任务添加失败");
    }

    /**
     * 删除定时任务
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(Job job) {
        if(jobService.deleteJob(job)) {
            return SUCCESS_TIP;
        }
        return new ErrorTip(201, "任务删除失败");
    }

    /**
     * 修改定时任务
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Job job) {
        if(jobService.updateJob(job)){
            return SUCCESS_TIP;
        }
        return new ErrorTip(201, "任务修改失败");
    }


}
