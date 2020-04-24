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
import com.xinshang.modular.biz.model.PlatformLogMoneys;
import com.xinshang.modular.biz.service.IPlatformLogMoneysService;

/**
 * @title:平台资金流水控制器
 *
 * @author: jiangyizheng
 * @since: 2019-10-23 16:00:51
 */
@Controller
@RequestMapping("/platformLogMoneys")
public class PlatformLogMoneysController extends BaseController {

    private String PREFIX = "/biz/platformLogMoneys/";

    @Autowired
    private IPlatformLogMoneysService platformLogMoneysService;

    /**
     * 跳转到平台资金流水首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "platformLogMoneys.html";
    }

    /**
     * 跳转到添加平台资金流水
     */
    @RequestMapping("/platformLogMoneys_add")
    public String platformLogMoneysAdd() {
        return PREFIX + "platformLogMoneys_add.html";
    }

    /**
     * 跳转到修改平台资金流水
     */
    @RequestMapping("/platformLogMoneys_update/{platformLogMoneysId}")
    public String platformLogMoneysUpdate(@PathVariable Integer platformLogMoneysId, Model model) {
        PlatformLogMoneys platformLogMoneys = platformLogMoneysService.selectById(platformLogMoneysId);
        model.addAttribute("item",platformLogMoneys);
        LogObjectHolder.me().set(platformLogMoneys);
        return PREFIX + "platformLogMoneys_edit.html";
    }

    /**
     * 获取平台资金流水分页列表
     */
    @RequestMapping(value = "/pageList")
    @ResponseBody
    public Object pageList(PlatformLogMoneys platformLogMoneys) {
        Page<PlatformLogMoneys> page = new PageFactory<PlatformLogMoneys>().defaultPage();
        Map<String, Object> beanMap = BeanKit.beanToMap(platformLogMoneys,true);
        EntityWrapper<PlatformLogMoneys> wrapper = new EntityWrapper<>();
        wrapper.allEq(beanMap);
        wrapper.orderBy("create_time", false);
        return platformLogMoneysService.selectPage(page,wrapper);
    }

    /**
     * 获取平台资金流水列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(PlatformLogMoneys platformLogMoneys) {
        Map<String, Object> beanMap = BeanKit.beanToMap(platformLogMoneys,true);
        EntityWrapper<PlatformLogMoneys> wrapper = new EntityWrapper<>();
        wrapper.allEq(beanMap);
        return platformLogMoneysService.selectList(wrapper);
    }

    /**
     * 新增平台资金流水
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(PlatformLogMoneys platformLogMoneys) {
        platformLogMoneysService.insert(platformLogMoneys);
        return SUCCESS_TIP;
    }

    /**
     * 删除平台资金流水
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam String platformLogMoneysIds) {
        if(StrUtil.isNotBlank(platformLogMoneysIds)) {
            platformLogMoneysService.deleteBatchIds(Arrays.asList(platformLogMoneysIds.split(",")));
        }
        return SUCCESS_TIP;
    }

    /**
     * 修改平台资金流水
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(PlatformLogMoneys platformLogMoneys) {
        platformLogMoneysService.updateById(platformLogMoneys);
        return SUCCESS_TIP;
    }

    /**
     * 平台资金流水详情
     */
    @RequestMapping(value = "/detail/{platformLogMoneysId}")
    @ResponseBody
    public Object detail(@PathVariable("platformLogMoneysId") Integer platformLogMoneysId) {
        return platformLogMoneysService.selectById(platformLogMoneysId);
    }
}
