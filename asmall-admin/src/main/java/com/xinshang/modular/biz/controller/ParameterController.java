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
import com.xinshang.modular.biz.model.Parameter;
import com.xinshang.modular.biz.service.IParameterService;

/**
 * @title:参数控制器
 *
 * @author: zhangjiajia
 * @since: 2019-11-26 11:37:35
 */
@Controller
@RequestMapping("/parameter")
public class ParameterController extends BaseController {

    private String PREFIX = "/biz/parameter/";

    @Autowired
    private IParameterService parameterService;

    /**
     * 跳转到参数首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "parameter.html";
    }

    /**
     * 跳转到添加参数
     */
    @RequestMapping("/parameter_add")
    public String parameterAdd() {
        return PREFIX + "parameter_add.html";
    }

    /**
     * 跳转到修改参数
     */
    @RequestMapping("/parameter_update/{parameterId}")
    public String parameterUpdate(@PathVariable Integer parameterId, Model model) {
        Parameter parameter = parameterService.selectById(parameterId);
        model.addAttribute("item",parameter);
        LogObjectHolder.me().set(parameter);
        return PREFIX + "parameter_edit.html";
    }

    /**
     * 获取参数分页列表
     */
    @RequestMapping(value = "/pageList")
    @ResponseBody
    public Object pageList(Parameter parameter) {
        Page<Parameter> page = new PageFactory<Parameter>().defaultPage();
        Map<String, Object> beanMap = BeanKit.beanToMap(parameter,true);
        EntityWrapper<Parameter> wrapper = new EntityWrapper<>();
        wrapper.allEq(beanMap);
        return parameterService.selectPage(page,wrapper);
    }

    /**
     * 获取参数列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(Parameter parameter) {
        Map<String, Object> beanMap = BeanKit.beanToMap(parameter,true);
        EntityWrapper<Parameter> wrapper = new EntityWrapper<>();
        wrapper.allEq(beanMap);
        return parameterService.selectList(wrapper);
    }

    /**
     * 新增参数
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Parameter parameter) {
        parameterService.insert(parameter);
        return SUCCESS_TIP;
    }

    /**
     * 删除参数
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam String parameterIds) {
        if(StrUtil.isNotBlank(parameterIds)) {
            parameterService.deleteBatchIds(Arrays.asList(parameterIds.split(",")));
        }
        return SUCCESS_TIP;
    }

    /**
     * 修改参数
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Parameter parameter) {
        parameterService.updateById(parameter);
        return SUCCESS_TIP;
    }

    /**
     * 参数详情
     */
    @RequestMapping(value = "/detail/{parameterId}")
    @ResponseBody
    public Object detail(@PathVariable("parameterId") Integer parameterId) {
        return parameterService.selectById(parameterId);
    }
}
