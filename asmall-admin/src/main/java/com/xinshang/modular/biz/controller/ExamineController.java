package com.xinshang.modular.biz.controller;

import com.xinshang.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import com.xinshang.core.support.BeanKit;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xinshang.core.common.constant.factory.PageFactory;
import java.util.Map;
import java.util.Arrays;
import cn.hutool.core.util.StrUtil;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.xinshang.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.xinshang.modular.biz.model.Examine;
import com.xinshang.modular.biz.service.IExamineService;

/**
 * @title:审核记录控制器
 *
 * @author: lvyingkai
 * @since: 2019-10-18 15:08:46
 */
@Controller
@RequestMapping("/examine")
public class ExamineController extends BaseController {

    private String PREFIX = "/biz/examine/";

    @Autowired
    private IExamineService examineService;

    /**
     * 跳转到审核记录首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "examine.html";
    }

    /**
     * 跳转到添加审核记录
     */
    @RequestMapping("/examine_add")
    public String examineAdd() {
        return PREFIX + "examine_add.html";
    }

    /**
     * 跳转到修改审核记录
     */
    @RequestMapping("/examine_update/{examineId}")
    public String examineUpdate(@PathVariable Integer examineId, Model model) {
        Examine examine = examineService.selectById(examineId);
        model.addAttribute("item",examine);
        LogObjectHolder.me().set(examine);
        return PREFIX + "examine_edit.html";
    }

    /**
     * 获取审核记录分页列表
     */
    @RequestMapping(value = "/pageList")
    @ResponseBody
    public Object pageList(Examine examine) {
        Page<Examine> page = new PageFactory<Examine>().defaultPage();
        Map<String, Object> beanMap = BeanKit.beanToMap(examine,true);
        EntityWrapper<Examine> wrapper = new EntityWrapper<>();
        wrapper.allEq(beanMap);
        return examineService.selectPage(page,wrapper);
    }

    /**
     * 获取审核记录列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(Examine examine) {
        Map<String, Object> beanMap = BeanKit.beanToMap(examine,true);
        EntityWrapper<Examine> wrapper = new EntityWrapper<>();
        wrapper.allEq(beanMap);
        return examineService.selectList(wrapper);
    }

    /**
     * 新增审核记录
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Examine examine) {
        examineService.insert(examine);
        return SUCCESS_TIP;
    }

    /**
     * 删除审核记录
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam String examineIds) {
        if(StrUtil.isNotBlank(examineIds)) {
            examineService.deleteBatchIds(Arrays.asList(examineIds.split(",")));
        }
        return SUCCESS_TIP;
    }

    /**
     * 修改审核记录
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Examine examine) {
        examineService.updateById(examine);
        return SUCCESS_TIP;
    }

    /**
     * 审核记录详情
     */
    @RequestMapping(value = "/detail/{examineId}")
    @ResponseBody
    public Object detail(@PathVariable("examineId") Integer examineId) {
        return examineService.selectById(examineId);
    }

    /**
     * 获取指定项目审核记录
     */
    @RequestMapping(value = "/showExamineByProjectId/{projectId}")
    @ResponseBody
    public Object showExamineByProjectId(@PathVariable("projectId") Long projectId) {
        EntityWrapper<Examine> wrapper = new EntityWrapper<>();
        wrapper.eq("project_id", projectId);
        return examineService.selectList(wrapper);
    }



}
