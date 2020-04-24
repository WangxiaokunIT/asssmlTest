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
import com.xinshang.modular.biz.model.Bank;
import com.xinshang.modular.biz.service.IBankService;

/**
 * @title:银行卡控制器
 *
 * @author: zhangjiajia
 * @since: 2019-11-12 14:40:57
 */
@Controller
@RequestMapping("/bank")
public class BankController extends BaseController {

    private String PREFIX = "/biz/bank/";

    @Autowired
    private IBankService bankService;

    /**
     * 跳转到银行卡首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "bank.html";
    }

    /**
     * 跳转到添加银行卡
     */
    @RequestMapping("/bank_add")
    public String bankAdd() {
        return PREFIX + "bank_add.html";
    }

    /**
     * 跳转到修改银行卡
     */
    @RequestMapping("/bank_update/{bankId}")
    public String bankUpdate(@PathVariable Integer bankId, Model model) {
        Bank bank = bankService.selectById(bankId);
        model.addAttribute("item",bank);
        LogObjectHolder.me().set(bank);
        return PREFIX + "bank_edit.html";
    }

    /**
     * 获取银行卡分页列表
     */
    @RequestMapping(value = "/pageList")
    @ResponseBody
    public Object pageList(Bank bank) {
        Page<Bank> page = new PageFactory<Bank>().defaultPage();
        Map<String, Object> beanMap = BeanKit.beanToMap(bank,true);
        EntityWrapper<Bank> wrapper = new EntityWrapper<>();
        wrapper.allEq(beanMap);
        return bankService.selectPage(page,wrapper);
    }

    /**
     * 获取银行卡列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(Bank bank) {
        Map<String, Object> beanMap = BeanKit.beanToMap(bank,true);
        EntityWrapper<Bank> wrapper = new EntityWrapper<>();
        wrapper.allEq(beanMap);
        return bankService.selectList(wrapper);
    }

    /**
     * 新增银行卡
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Bank bank) {
        bankService.insert(bank);
        return SUCCESS_TIP;
    }

    /**
     * 删除银行卡
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam String bankIds) {
        if(StrUtil.isNotBlank(bankIds)) {
            bankService.deleteBatchIds(Arrays.asList(bankIds.split(",")));
        }
        return SUCCESS_TIP;
    }

    /**
     * 修改银行卡
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Bank bank) {
        bankService.updateById(bank);
        return SUCCESS_TIP;
    }

    /**
     * 银行卡详情
     */
    @RequestMapping(value = "/detail/{bankId}")
    @ResponseBody
    public Object detail(@PathVariable("bankId") Integer bankId) {
        return bankService.selectById(bankId);
    }
}
