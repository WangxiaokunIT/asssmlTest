package com.xinshang.modular.biz.controller;

import com.xinshang.core.base.controller.BaseController;
import com.xinshang.core.shiro.ShiroKit;
import com.xinshang.core.shiro.ShiroUser;
import com.xinshang.core.support.DateTime;
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
import com.xinshang.modular.biz.model.Postage;
import com.xinshang.modular.biz.service.IPostageService;

/**
 * @title:运费模块控制器
 *
 * @author: wangxiaokun
 * @since: 2019-12-09 17:46:08
 */
@Controller
@RequestMapping("/postage")
public class PostageController extends BaseController {

    private String PREFIX = "/biz/postage/";

    @Autowired
    private IPostageService postageService;

    /**
     * 跳转到运费模块首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "postage.html";
    }

    /**
     * 跳转到添加运费模块
     */
    @RequestMapping("/postage_add")
    public String postageAdd() {
        return PREFIX + "postage_add.html";
    }

    /**
     * 跳转到修改运费模块
     */
    @RequestMapping("/postage_update/{postageId}")
    public String postageUpdate(@PathVariable Integer postageId, Model model) {
        Postage postage = postageService.selectById(postageId);
        model.addAttribute("item",postage);
        LogObjectHolder.me().set(postage);
        return PREFIX + "postage_edit.html";
    }

    /**
     * 获取运费模块分页列表
     */
    @RequestMapping(value = "/pageList")
    @ResponseBody
    public Object pageList(Postage postage) {
        Page<Postage> page = new PageFactory<Postage>().defaultPage();
        Map<String, Object> beanMap = BeanKit.beanToMap(postage,true);
        EntityWrapper<Postage> wrapper = new EntityWrapper<>();
        wrapper.allEq(beanMap);
        return postageService.selectPage(page,wrapper);
    }

    /**
     * 获取运费模块列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(Postage postage) {
        Map<String, Object> beanMap = BeanKit.beanToMap(postage,true);
        EntityWrapper<Postage> wrapper = new EntityWrapper<>();
        wrapper.allEq(beanMap);
        return postageService.selectList(wrapper);
    }

    /**
     * 新增运费模块
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Postage postage) {
        //新增前先判断，如果有同一省份同一商品的记录，则添加不成功
        EntityWrapper<Postage> wrapper = new EntityWrapper<>();
        wrapper.eq("item_number",postage.getItemNumber());
        wrapper.eq("area",postage.getArea());

        Postage one = postageService.selectOne(wrapper);
        if (one != null) {
            return new Error("该地区已存在费用数据");
        }
        ShiroUser user = ShiroKit.getUser();
        postage.setCreated(new DateTime());
        postage.setUserid(user.id);
        postageService.insert(postage);
        return SUCCESS_TIP;
    }

    /**
     * 删除运费模块
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(Postage postage) {
        /*if(StrUtil.isNotBlank(postageIds)) {
            postageService.deleteBatchIds(Arrays.asList(postageIds.split(",")));
        }*/
        EntityWrapper<Postage> wrapper = new EntityWrapper<>();
        wrapper.eq("item_number",postage.getItemNumber());
        wrapper.eq("freight",postage.getFreight());
        wrapper.eq("area",postage.getArea());
        postageService.delete(wrapper);
        return SUCCESS_TIP;
    }

    /**
     * 修改运费模块
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Postage postage) {
        postageService.updateById(postage);
        return SUCCESS_TIP;
    }

    /**
     * 运费模块详情
     */
    @RequestMapping(value = "/detail/{postageId}")
    @ResponseBody
    public Object detail(@PathVariable("postageId") Integer postageId) {
        return postageService.selectById(postageId);
    }
}
