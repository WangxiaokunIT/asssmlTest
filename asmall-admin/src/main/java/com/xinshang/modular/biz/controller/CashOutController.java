package com.xinshang.modular.biz.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xinshang.core.base.controller.BaseController;
import com.xinshang.core.common.constant.factory.PageFactory;
import com.xinshang.core.log.LogObjectHolder;
import com.xinshang.core.support.BeanKit;
import com.xinshang.modular.biz.dto.CashOutDTO;
import com.xinshang.modular.biz.model.CashOut;
import com.xinshang.modular.biz.service.ICashOutService;
import com.xinshang.modular.biz.vo.CashOutVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * @title:提现审核控制器
 *
 * @author: lvyingkai
 * @since: 2019-10-22 15:41:51
 */
@Controller
@RequestMapping("/cashOut")
public class CashOutController extends BaseController {

    private String PREFIX = "/biz/cashOut/";

    @Autowired
    private ICashOutService cashOutService;

    /**
     * 跳转到提现审核首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "cashOut.html";
    }

    /**
     * 跳转到添加提现审核
     */
    @RequestMapping("/cashOut_add")
    public String cashOutAdd() {
        return PREFIX + "cashOut_add.html";
    }

    /**
     * 跳转到修改提现审核
     */
    @RequestMapping("/cashOut_update/{cashOutId}")
    public String cashOutUpdate(@PathVariable Integer cashOutId, Model model) {
        CashOut cashOut = cashOutService.selectById(cashOutId);
        model.addAttribute("item",cashOut);
        LogObjectHolder.me().set(cashOut);
        return PREFIX + "cashOut_edit.html";
    }

    /**
     * 获取提现审核分页列表
     */
    @RequestMapping(value = "/pageList")
    @ResponseBody
    public Object pageList(CashOut cashOut) {
        Page<CashOut> page = new PageFactory<CashOut>().defaultPage();
        Map<String, Object> beanMap = BeanKit.beanToMap(cashOut,true);
        EntityWrapper<CashOut> wrapper = new EntityWrapper<>();
        wrapper.allEq(beanMap);
        return cashOutService.selectPage(page,wrapper);
    }

    /**
     * 获取提现审核分页列表（增强）
     */
    @RequestMapping(value = "/list_up")
    @ResponseBody
    public Object listUp(CashOutDTO cashOut) {
        Page<CashOutVO> page = new PageFactory<CashOutVO>().defaultPage();
        page.setRecords(cashOutService.showCashOut(cashOut, page));
        return page;
    }

    /**
     * 获取提现审核列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(CashOut cashOut) {
        Map<String, Object> beanMap = BeanKit.beanToMap(cashOut,true);
        EntityWrapper<CashOut> wrapper = new EntityWrapper<>();
        wrapper.allEq(beanMap);
        return cashOutService.selectList(wrapper);
    }

    /**
     * 新增提现审核
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(CashOut cashOut) {
        cashOutService.insert(cashOut);
        return SUCCESS_TIP;
    }

    /**
     * 删除提现审核
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam String cashOutIds) {
        if(StrUtil.isNotBlank(cashOutIds)) {
            cashOutService.deleteBatchIds(Arrays.asList(cashOutIds.split(",")));
        }
        return SUCCESS_TIP;
    }

    /**
     * 修改提现审核
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(CashOut cashOut) throws Exception {
        cashOutService.updateCashOut(cashOut);
        return SUCCESS_TIP;
    }

    /**
     * 提现审核详情
     */
    @RequestMapping(value = "/detail/{cashOutId}")
    @ResponseBody
    public Object detail(@PathVariable("cashOutId") Integer cashOutId) {
        return cashOutService.selectById(cashOutId);
    }
}
