package com.xinshang.modular.system.controller;

import cn.hutool.core.util.StrUtil;
import com.xinshang.core.base.controller.BaseController;
import com.xinshang.core.base.tips.ErrorTip;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import com.xinshang.core.support.BeanKit;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xinshang.core.common.constant.factory.PageFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.xinshang.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.xinshang.modular.system.model.PositionUser;
import com.xinshang.modular.system.service.IPositionUserService;

/**
 * 职位用户控制器
 *
 * @author fengshuonan
 * @date 2018-11-12 14:56:21
 */
@Controller
@RequestMapping("/positionUser")
public class PositionUserController extends BaseController {

    private String PREFIX = "/system/positionUser/";

    @Autowired
    private IPositionUserService positionUserService;

    /**
     * 跳转到职位用户首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "positionUser.html";
    }

    /**
     * 跳转到添加职位用户
     */
    @RequestMapping("/positionUser_add")
    public String positionUserAdd() {
        return PREFIX + "positionUser_add.html";
    }

    /**
     * 跳转到修改职位用户
     */
    @RequestMapping("/positionUser_update/{positionUserId}")
    public String positionUserUpdate(@PathVariable Integer positionUserId, Model model) {
        PositionUser positionUser = positionUserService.selectById(positionUserId);
        model.addAttribute("item",positionUser);
        LogObjectHolder.me().set(positionUser);
        return PREFIX + "positionUser_edit.html";
    }

    /**
     * 获取职位用户列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(PositionUser positionUser) {
        Page<PositionUser> page = new PageFactory<PositionUser>().defaultPage();
        Map<String, Object> beanMap = BeanKit.beanToMap(positionUser,true);
        EntityWrapper<PositionUser> wrapper = new EntityWrapper<>();
        wrapper.allEq(beanMap);
        return positionUserService.selectPage(page,wrapper);
    }

    /**
     * 新增职位用户
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(PositionUser positionUser) {
        positionUserService.insert(positionUser);
        return SUCCESS_TIP;
    }

    /**
     * 删除职位用户
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer positionUserId) {
        positionUserService.deleteById(positionUserId);
        return SUCCESS_TIP;
    }

    /**
     * 删除职位用户
     */
    @RequestMapping(value = "/deleteByPositionAndUser")
    @ResponseBody
    public Object deleteByPositionAndUser(PositionUser positionUser) {
        EntityWrapper<PositionUser> wrapper = new EntityWrapper<>();
        wrapper.and("position_id",positionUser.getPositionId()).and("user_id",positionUser.getUserId());
        positionUserService.delete(wrapper);
        return SUCCESS_TIP;
    }

    /**
     * 修改职位用户
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(PositionUser positionUser) {
        deleteByPositionAndUser(positionUser);
        positionUserService.insert(positionUser);
        return SUCCESS_TIP;
    }

    /**
     * 修改职位用户
     */
    @RequestMapping(value = "/savePositionUser")
    @ResponseBody
    public Object savePositionUser(Integer positionId,String userIds) {
        if(positionId==null){
           return new ErrorTip("职位编码不能为空");
        }
        //删除该职位下的所有用户
        EntityWrapper<PositionUser> wrapper = new EntityWrapper<>();
        wrapper.eq("position_id",positionId);
        positionUserService.delete(wrapper);
        String[] userid = null;
        if(StrUtil.isNotBlank(userIds)){
            userid = userIds.split(",");
            List<PositionUser> list = new ArrayList();
            for (int i = 0; i < userid.length; i++) {
                PositionUser pu =  new PositionUser();
                pu.setPositionId(positionId);
                pu.setUserId(Integer.valueOf(userid[i]));
                list.add(pu);
            }
            positionUserService.insertBatch(list);
        }
        return SUCCESS_TIP;
    }

    /**
     * 职位用户详情
     */
    @RequestMapping(value = "/detail/{positionUserId}")
    @ResponseBody
    public Object detail(@PathVariable("positionUserId") Integer positionUserId) {
        return positionUserService.selectById(positionUserId);
    }
}
