package com.xinshang.modular.system.controller;

import com.xinshang.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.xinshang.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.xinshang.modular.system.model.MsgTemplate;
import com.xinshang.modular.system.service.IMsgTemplateService;

/**
 * 消息模版控制器
 *
 * @author fengshuonan
 * @date 2018-10-09 16:54:12
 */
@Controller
@RequestMapping("/msgTemplate")
public class MsgTemplateController extends BaseController {

    private String PREFIX = "/system/msgTemplate/";

    @Autowired
    private IMsgTemplateService msgTemplateService;

    /**
     * 跳转到消息模版首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "msgTemplate.html";
    }

    /**
     * 跳转到添加消息模版
     */
    @RequestMapping("/msgTemplate_add")
    public String msgTemplateAdd() {
        return PREFIX + "msgTemplate_add.html";
    }

    /**
     * 跳转到修改消息模版
     */
    @RequestMapping("/msgTemplate_update/{msgTemplateId}")
    public String msgTemplateUpdate(@PathVariable Integer msgTemplateId, Model model) {
        MsgTemplate msgTemplate = msgTemplateService.selectById(msgTemplateId);
        model.addAttribute("item",msgTemplate);
        LogObjectHolder.me().set(msgTemplate);
        return PREFIX + "msgTemplate_edit.html";
    }

    /**
     * 获取消息模版列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return  msgTemplateService.selectList(null);
    }

    /**
     * 新增消息模版
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(MsgTemplate msgTemplate) {
        msgTemplateService.insert(msgTemplate);
        return SUCCESS_TIP;
    }

    /**
     * 删除消息模版
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer msgTemplateId) {
        msgTemplateService.deleteById(msgTemplateId);
        return SUCCESS_TIP;
    }

    /**
     * 修改消息模版
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(MsgTemplate msgTemplate) {
        msgTemplateService.updateById(msgTemplate);
        return SUCCESS_TIP;
    }

    /**
     * 消息模版详情
     */
    @RequestMapping(value = "/detail/{msgTemplateId}")
    @ResponseBody
    public Object detail(@PathVariable("msgTemplateId") Integer msgTemplateId) {
        return msgTemplateService.selectById(msgTemplateId);
    }
}
