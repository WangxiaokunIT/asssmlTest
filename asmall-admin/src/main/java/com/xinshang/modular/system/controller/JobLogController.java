package com.xinshang.modular.system.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xinshang.core.base.controller.BaseController;
import com.xinshang.core.common.constant.factory.PageFactory;
import com.xinshang.core.log.LogObjectHolder;
import com.xinshang.core.support.BeanKit;
import com.xinshang.modular.system.model.JobLog;
import com.xinshang.modular.system.model.Msg;
import com.xinshang.modular.system.service.IJobLogService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 定时任务运行日志控制器
 *
 * @author fengshuonan
 * @date 2018-08-06 09:37:23
 */
@Controller
@RequestMapping("/jobLog")
public class JobLogController extends BaseController {

    private String PREFIX = "/system/jobLog/";

    @Autowired
    private IJobLogService jobLogService;

    /**
     * 跳转到定时任务运行日志首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "jobLog.html";
    }

    /**
     * 跳转到添加定时任务运行日志
     */
    @RequestMapping("/jobLog_add")
    public String jobLogAdd() {
        return PREFIX + "jobLog_add.html";
    }

    /**
     * 跳转到修改定时任务运行日志
     */
    @RequestMapping("/jobLog_update/{id}")
    public String jobLogUpdate(@PathVariable Integer id, Model model) {
        JobLog jobLog = jobLogService.selectById(id);
        model.addAttribute("item",jobLog);
        LogObjectHolder.me().set(jobLog);
        return PREFIX + "jobLog_edit.html";
    }

    /**
     * 获取定时任务运行日志列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition,String state) {
        Page<JobLog> page = new PageFactory<JobLog>().defaultPage();
        EntityWrapper<JobLog> wrapper = new EntityWrapper();

        wrapper.eq(StrUtil.isNotBlank(state),"state",state);
        if(StrUtil.isNotBlank(condition)){
            wrapper.andNew().like("job_name",condition).or().like("job_message",condition).or().like("exception_info",condition);
        }
        wrapper.orderBy("id",false);
        return jobLogService.selectPage(page,wrapper);
    }

    /**
     * 新增定时任务运行日志
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(JobLog jobLog) {
        jobLogService.insert(jobLog);
        return SUCCESS_TIP;
    }

    /**
     * 删除定时任务运行日志
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer id) {
        jobLogService.deleteById(id);
        return SUCCESS_TIP;
    }

    /**
     * 修改定时任务运行日志
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(JobLog jobLog) {
        jobLogService.updateById(jobLog);
        return SUCCESS_TIP;
    }

    /**
     * 定时任务运行日志详情
     */
    @RequestMapping(value = "/detail/{id}")
    @ResponseBody
    public Object detail(@PathVariable("id") Integer id) {
        return jobLogService.selectById(id);
    }
}
