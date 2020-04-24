package com.xinshang.modular.biz.controller;

import com.xinshang.core.base.controller.BaseController;
import com.xinshang.modular.biz.model.Joinin;
import com.xinshang.modular.biz.service.IJoininService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import com.xinshang.core.support.BeanKit;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xinshang.core.common.constant.factory.PageFactory;

import java.util.Date;
import java.util.Map;
import java.util.Arrays;
import cn.hutool.core.util.StrUtil;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.xinshang.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.xinshang.modular.biz.model.RepayPlan;
import com.xinshang.modular.biz.service.IRepayPlanService;

/**
 * @title:还款记录控制器
 *
 * @author: zhangjiajia
 * @since: 2019-10-25 13:31:22
 */
@Controller
@RequestMapping("/repayPlan")
public class RepayPlanController extends BaseController {

    private String PREFIX = "/biz/repayPlan/";

    @Autowired
    private IRepayPlanService repayPlanService;
    @Autowired
    private IJoininService joininService;
    /**
     * 跳转到还款记录首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "repayPlan.html";
    }

    /**
     * 跳转到添加还款记录
     */
    @RequestMapping("/repayPlan_add")
    public String repayPlanAdd() {
        return PREFIX + "repayPlan_add.html";
    }

    /**
     * 跳转到修改还款记录
     */
    @RequestMapping("/repayPlan_update/{months}/{id}/{statecode}/{projectId}/{paidInter}")
    public String repayPlanUpdate(@PathVariable Integer months,@PathVariable Integer id,@PathVariable Integer statecode,@PathVariable Integer projectId,@PathVariable String paidInter, Model model) {
        model.addAttribute("months",months);
        model.addAttribute("id",id);
        model.addAttribute("paidInter",paidInter);
        model.addAttribute("statecode",statecode);
        model.addAttribute("projectId",projectId);
         return  "biz/joinin/haveInter.html";
    }

    /**
     * 修改还款记录
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(RepayPlan repayPlan) {
        repayPlan.setHaveInterDate(new Date());
         repayPlan.setStatecode(2);
        //修改还款计划状态
        repayPlanService.updateById(repayPlan);
        return SUCCESS_TIP;
    }


    /**
     * 获取还款记录分页列表
     */
    @RequestMapping(value = "/pageList")
    @ResponseBody
    public Object pageList(RepayPlan repayPlan) {
        Page<RepayPlan> page = new PageFactory<RepayPlan>().defaultPage();
        Map<String, Object> beanMap = BeanKit.beanToMap(repayPlan,true);
        EntityWrapper<RepayPlan> wrapper = new EntityWrapper<>();
        wrapper.allEq(beanMap);
        return repayPlanService.selectPage(page,wrapper);
    }

    /**
     * 获取还款记录列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(RepayPlan repayPlan) {
        Map<String, Object> beanMap = BeanKit.beanToMap(repayPlan,true);
        EntityWrapper<RepayPlan> wrapper = new EntityWrapper<>();
        wrapper.allEq(beanMap);
        return repayPlanService.selectList(wrapper);
    }

    /**
     * 新增还款记录
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(RepayPlan repayPlan) {
        repayPlanService.insert(repayPlan);
        return SUCCESS_TIP;
    }

    /**
     * 删除还款记录
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam String repayPlanIds) {
        if(StrUtil.isNotBlank(repayPlanIds)) {
            repayPlanService.deleteBatchIds(Arrays.asList(repayPlanIds.split(",")));
        }
        return SUCCESS_TIP;
    }



    /**
     * 还款记录详情
     */
    @RequestMapping(value = "/detail/{repayPlanId}")
    @ResponseBody
    public Object detail(@PathVariable("repayPlanId") Integer repayPlanId) {
        return repayPlanService.selectById(repayPlanId);
    }


    /**
     * 获取指定项目审核记录
     */
    @RequestMapping(value = "/repayPlanByProjectId/{projectId}/{id}")
    @ResponseBody
    public Object repayPlanByProjectId(@PathVariable("projectId") Long projectId, @PathVariable Long id) {
        EntityWrapper<RepayPlan> wrapper = new EntityWrapper<>();

        wrapper.eq("project_id",projectId);
        wrapper.eq("joinin_id",id);
        wrapper.eq("is_delete",0);
        return repayPlanService.selectList(wrapper);
    }
}
