package com.xinshang.modular.system.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xinshang.core.base.controller.BaseController;
import com.xinshang.core.common.constant.factory.PageFactory;
import com.xinshang.core.support.BeanKit;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.xinshang.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.xinshang.modular.system.model.Msg;
import com.xinshang.modular.system.service.IMsgService;
import java.util.Map;

/**
 * 系统消息控制器
 *
 * @author fengshuonan
 * @date 2018-10-12 16:41:50
 */
@Controller
@RequestMapping("/msg")
public class MsgController extends BaseController {

    private String PREFIX = "/system/msg/";

    @Autowired
    private IMsgService msgService;

    /**
     * 跳转到系统消息首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "msg.html";
    }

    /**
     * 跳转到添加系统消息
     */
    @RequestMapping("/msg_add")
    public String msgAdd() {
        return PREFIX + "msg_add.html";
    }

    /**
     * 跳转到修改系统消息
     */
    @RequestMapping("/msg_update/{msgId}")
    public String msgUpdate(@PathVariable Integer msgId, Model model) {
        Msg msg = msgService.selectById(msgId);
        model.addAttribute("item",msg);
        LogObjectHolder.me().set(msg);
        return PREFIX + "msg_edit.html";
    }

    /**
     * 获取系统消息列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(Msg msg) {
        Page<Msg> page = new PageFactory<Msg>().defaultPage();
        Map<String, Object> beanMap = BeanKit.beanToMap(msg,true);
        EntityWrapper<Msg> wrapper = new EntityWrapper<>();
        wrapper.allEq(beanMap);
        return msgService.selectPage(page,wrapper);
    }

    /**
     * 新增系统消息
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Msg msg) {
        msgService.insert(msg);
        return SUCCESS_TIP;
    }

    /**
     * 删除系统消息
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer msgId) {
        msgService.deleteById(msgId);
        return SUCCESS_TIP;
    }

    /**
     * 修改系统消息
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Msg msg) {
        msgService.updateById(msg);
        return SUCCESS_TIP;
    }

    /**
     * 系统消息详情
     */
    @RequestMapping(value = "/detail/{msgId}")
    @ResponseBody
    public Object detail(@PathVariable("msgId") Integer msgId) {
        return msgService.selectById(msgId);
    }
}
