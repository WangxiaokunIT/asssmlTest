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
import com.xinshang.modular.biz.model.SupplierLogMoneys;
import com.xinshang.modular.biz.service.ISupplierLogMoneysService;

/**
 * @title:供应商资金流水控制器
 *
 * @author: jiangyizheng
 * @since: 2019-10-23 15:03:37
 */
@Controller
@RequestMapping("/supplierLogMoneys")
public class SupplierLogMoneysController extends BaseController {

    private String PREFIX = "/biz/supplierLogMoneys/";

    @Autowired
    private ISupplierLogMoneysService supplierLogMoneysService;

    /**
     * 跳转到供应商资金流水首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "supplierLogMoneys.html";
    }

    /**
     * 跳转到添加供应商资金流水
     */
    @RequestMapping("/supplierLogMoneys_add")
    public String supplierLogMoneysAdd() {
        return PREFIX + "supplierLogMoneys_add.html";
    }

    /**
     * 跳转到修改供应商资金流水
     */
    @RequestMapping("/supplierLogMoneys_update/{supplierLogMoneysId}")
    public String supplierLogMoneysUpdate(@PathVariable Integer supplierLogMoneysId, Model model) {
        SupplierLogMoneys supplierLogMoneys = supplierLogMoneysService.selectById(supplierLogMoneysId);
        model.addAttribute("item",supplierLogMoneys);
        LogObjectHolder.me().set(supplierLogMoneys);
        return PREFIX + "supplierLogMoneys_edit.html";
    }

    /**
     * 获取供应商资金流水分页列表
     */
    @RequestMapping(value = "/pageList")
    @ResponseBody
    public Object pageList(SupplierLogMoneys supplierLogMoneys) {
        Page<SupplierLogMoneys> page = new PageFactory<SupplierLogMoneys>().defaultPage();
        Map<String, Object> beanMap = BeanKit.beanToMap(supplierLogMoneys,true);
        EntityWrapper<SupplierLogMoneys> wrapper = new EntityWrapper<>();
        wrapper.allEq(beanMap);
        wrapper.orderBy("create_time", false);
        return supplierLogMoneysService.selectPage(page,wrapper);
    }

    /**
     * 获取供应商资金流水列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(SupplierLogMoneys supplierLogMoneys) {
        Map<String, Object> beanMap = BeanKit.beanToMap(supplierLogMoneys,true);
        EntityWrapper<SupplierLogMoneys> wrapper = new EntityWrapper<>();
        wrapper.allEq(beanMap);
        return supplierLogMoneysService.selectList(wrapper);
    }

    /**
     * 新增供应商资金流水
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(SupplierLogMoneys supplierLogMoneys) {
        supplierLogMoneysService.insert(supplierLogMoneys);
        return SUCCESS_TIP;
    }

    /**
     * 删除供应商资金流水
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam String supplierLogMoneysIds) {
        if(StrUtil.isNotBlank(supplierLogMoneysIds)) {
            supplierLogMoneysService.deleteBatchIds(Arrays.asList(supplierLogMoneysIds.split(",")));
        }
        return SUCCESS_TIP;
    }

    /**
     * 修改供应商资金流水
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(SupplierLogMoneys supplierLogMoneys) {
        supplierLogMoneysService.updateById(supplierLogMoneys);
        return SUCCESS_TIP;
    }

    /**
     * 供应商资金流水详情
     */
    @RequestMapping(value = "/detail/{supplierLogMoneysId}")
    @ResponseBody
    public Object detail(@PathVariable("supplierLogMoneysId") Integer supplierLogMoneysId) {
        return supplierLogMoneysService.selectById(supplierLogMoneysId);
    }
}
