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
import com.xinshang.modular.biz.model.ClientLogMoneys;
import com.xinshang.modular.biz.service.IClientLogMoneysService;

/**
 * @title:客户资金流水控制器
 *
 * @author: jiangyizheng
 * @since: 2019-10-23 15:45:03
 */
@Controller
@RequestMapping("/clientLogMoneys")
public class ClientLogMoneysController extends BaseController {

    private String PREFIX = "/biz/clientLogMoneys/";

    @Autowired
    private IClientLogMoneysService clientLogMoneysService;

    /**
     * 跳转到客户资金流水首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "clientLogMoneys.html";
    }

    /**
     * 跳转到添加客户资金流水
     */
    @RequestMapping("/clientLogMoneys_add")
    public String clientLogMoneysAdd() {
        return PREFIX + "clientLogMoneys_add.html";
    }

    /**
     * 跳转到修改客户资金流水
     */
    @RequestMapping("/clientLogMoneys_update/{clientLogMoneysId}")
    public String clientLogMoneysUpdate(@PathVariable Integer clientLogMoneysId, Model model) {
        ClientLogMoneys clientLogMoneys = clientLogMoneysService.selectById(clientLogMoneysId);
        model.addAttribute("item",clientLogMoneys);
        LogObjectHolder.me().set(clientLogMoneys);
        return PREFIX + "clientLogMoneys_edit.html";
    }

    /**
     * 获取客户资金流水分页列表
     */
    @RequestMapping(value = "/pageList")
    @ResponseBody
    public Object pageList(ClientLogMoneys clientLogMoneys) {
        Page<ClientLogMoneys> page = new PageFactory<ClientLogMoneys>().defaultPage();
        Map<String, Object> beanMap = BeanKit.beanToMap(clientLogMoneys,true);
        EntityWrapper<ClientLogMoneys> wrapper = new EntityWrapper<>();
        wrapper.allEq(beanMap);
        wrapper.orderBy("create_time", false);
        return clientLogMoneysService.selectPage(page,wrapper);
    }

    /**
     * 获取客户资金流水列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(ClientLogMoneys clientLogMoneys) {
        Map<String, Object> beanMap = BeanKit.beanToMap(clientLogMoneys,true);
        EntityWrapper<ClientLogMoneys> wrapper = new EntityWrapper<>();
        wrapper.allEq(beanMap);
        return clientLogMoneysService.selectList(wrapper);
    }

    /**
     * 新增客户资金流水
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(ClientLogMoneys clientLogMoneys) {
        clientLogMoneysService.insert(clientLogMoneys);
        return SUCCESS_TIP;
    }

    /**
     * 删除客户资金流水
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam String clientLogMoneysIds) {
        if(StrUtil.isNotBlank(clientLogMoneysIds)) {
            clientLogMoneysService.deleteBatchIds(Arrays.asList(clientLogMoneysIds.split(",")));
        }
        return SUCCESS_TIP;
    }

    /**
     * 修改客户资金流水
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(ClientLogMoneys clientLogMoneys) {
        clientLogMoneysService.updateById(clientLogMoneys);
        return SUCCESS_TIP;
    }

    /**
     * 客户资金流水详情
     */
    @RequestMapping(value = "/detail/{clientLogMoneysId}")
    @ResponseBody
    public Object detail(@PathVariable("clientLogMoneysId") Integer clientLogMoneysId) {
        return clientLogMoneysService.selectById(clientLogMoneysId);
    }
}
