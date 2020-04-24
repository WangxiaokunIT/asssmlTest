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
import com.xinshang.modular.biz.model.IntegralLogMoneys;
import com.xinshang.modular.biz.service.IIntegralLogMoneysService;

/**
 * @title:客户积分流水控制器
 *
 * @author: jiangyizheng
 * @since: 2019-12-24 09:07:06
 */
@Controller
@RequestMapping("/integralLogMoneys")
public class IntegralLogMoneysController extends BaseController {

    private String PREFIX = "/biz/integralLogMoneys/";

    @Autowired
    private IIntegralLogMoneysService integralLogMoneysService;

    /**
     * 跳转到客户积分流水首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "integralLogMoneys.html";
    }

    /**
     * 跳转到添加客户积分流水
     */
    @RequestMapping("/integralLogMoneys_add")
    public String integralLogMoneysAdd() {
        return PREFIX + "integralLogMoneys_add.html";
    }

    /**
     * 跳转到修改客户积分流水
     */
    @RequestMapping("/integralLogMoneys_update/{integralLogMoneysId}")
    public String integralLogMoneysUpdate(@PathVariable Integer integralLogMoneysId, Model model) {
        IntegralLogMoneys integralLogMoneys = integralLogMoneysService.selectById(integralLogMoneysId);
        model.addAttribute("item",integralLogMoneys);
        LogObjectHolder.me().set(integralLogMoneys);
        return PREFIX + "integralLogMoneys_edit.html";
    }

    /**
     * 获取客户积分流水分页列表
     */
    @RequestMapping(value = "/pageList")
    @ResponseBody
    public Object pageList(IntegralLogMoneys integralLogMoneys) {
        Page<IntegralLogMoneys> page = new PageFactory<IntegralLogMoneys>().defaultPage();
        Map<String, Object> beanMap = BeanKit.beanToMap(integralLogMoneys,true);
        EntityWrapper<IntegralLogMoneys> wrapper = new EntityWrapper<>();
        wrapper.allEq(beanMap);
        wrapper.orderBy("create_time", false);
        return integralLogMoneysService.selectPage(page,wrapper);
    }

    /**
     * 获取客户积分流水列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(IntegralLogMoneys integralLogMoneys) {
        Map<String, Object> beanMap = BeanKit.beanToMap(integralLogMoneys,true);
        EntityWrapper<IntegralLogMoneys> wrapper = new EntityWrapper<>();
        wrapper.allEq(beanMap);
        return integralLogMoneysService.selectList(wrapper);
    }

    /**
     * 新增客户积分流水
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(IntegralLogMoneys integralLogMoneys) {
        integralLogMoneysService.insert(integralLogMoneys);
        return SUCCESS_TIP;
    }

    /**
     * 删除客户积分流水
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam String integralLogMoneysIds) {
        if(StrUtil.isNotBlank(integralLogMoneysIds)) {
            integralLogMoneysService.deleteBatchIds(Arrays.asList(integralLogMoneysIds.split(",")));
        }
        return SUCCESS_TIP;
    }

    /**
     * 修改客户积分流水
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(IntegralLogMoneys integralLogMoneys) {
        integralLogMoneysService.updateById(integralLogMoneys);
        return SUCCESS_TIP;
    }

    /**
     * 客户积分流水详情
     */
    @RequestMapping(value = "/detail/{integralLogMoneysId}")
    @ResponseBody
    public Object detail(@PathVariable("integralLogMoneysId") Integer integralLogMoneysId) {
        return integralLogMoneysService.selectById(integralLogMoneysId);
    }
}
