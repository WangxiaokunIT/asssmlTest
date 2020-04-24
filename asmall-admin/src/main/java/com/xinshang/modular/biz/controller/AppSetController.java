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
import com.xinshang.modular.biz.model.AppSet;
import com.xinshang.modular.biz.service.IAppSetService;

/**
 * @title:协议说明控制器
 *
 * @author: lvyingkai
 * @since: 2019-11-26 11:29:49
 */
@Controller
@RequestMapping("/appSet")
public class AppSetController extends BaseController {

    private String PREFIX = "/biz/appSet/";

    @Autowired
    private IAppSetService appSetService;

    /**
     * 跳转到协议说明首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "appSet.html";
    }

    /**
     * 跳转到添加协议说明
     */
    @RequestMapping("/appSet_add")
    public String appSetAdd() {
        return PREFIX + "appSet_add.html";
    }

    /**
     * 跳转到修改协议说明
     */
    @RequestMapping("/appSet_update/{appSetId}")
    public String appSetUpdate(@PathVariable Integer appSetId, Model model) {
        AppSet appSet = appSetService.selectById(appSetId);
        model.addAttribute("item",appSet);
        LogObjectHolder.me().set(appSet);
        return PREFIX + "appSet_edit.html";
    }

    /**
     * 获取协议说明分页列表
     */
    @RequestMapping(value = "/pageList")
    @ResponseBody
    public Object pageList(AppSet appSet) {
        Page<AppSet> page = new PageFactory<AppSet>().defaultPage();
        Map<String, Object> beanMap = BeanKit.beanToMap(appSet,true);
        EntityWrapper<AppSet> wrapper = new EntityWrapper<>();
        wrapper.allEq(beanMap);
        return appSetService.selectPage(page,wrapper);
    }

    /**
     * 获取协议说明列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(AppSet appSet) {
        Map<String, Object> beanMap = BeanKit.beanToMap(appSet,true);
        EntityWrapper<AppSet> wrapper = new EntityWrapper<>();
        wrapper.allEq(beanMap);
        return appSetService.selectList(wrapper);
    }

    /**
     * 新增协议说明
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(AppSet appSet) {
        appSetService.insert(appSet);
        return SUCCESS_TIP;
    }

    /**
     * 删除协议说明
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam String appSetIds) {
        if(StrUtil.isNotBlank(appSetIds)) {
            appSetService.deleteBatchIds(Arrays.asList(appSetIds.split(",")));
        }
        return SUCCESS_TIP;
    }

    /**
     * 修改协议说明
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(AppSet appSet) {
        appSetService.updateById(appSet);
        return SUCCESS_TIP;
    }

    /**
     * 协议说明详情
     */
    @RequestMapping(value = "/detail/{appSetId}")
    @ResponseBody
    public Object detail(@PathVariable("appSetId") Integer appSetId) {
        return appSetService.selectById(appSetId);
    }
}
