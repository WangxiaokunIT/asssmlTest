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
import com.xinshang.modular.biz.model.ItemSpecs;
import com.xinshang.modular.biz.service.IItemSpecsService;

/**
 * @title:商品规格控制器
 *
 * @author: zhangjiajia
 * @since: 2019-11-25 10:26:00
 */
@Controller
@RequestMapping("/itemSpecs")
public class ItemSpecsController extends BaseController {

    private String PREFIX = "/biz/itemSpecs/";

    @Autowired
    private IItemSpecsService itemSpecsService;

    /**
     * 跳转到商品规格首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "itemSpecs.html";
    }

    /**
     * 跳转到添加商品规格
     */
    @RequestMapping("/itemSpecs_add")
    public String itemSpecsAdd() {
        return PREFIX + "itemSpecs_add.html";
    }

    /**
     * 跳转到修改商品规格
     */
    @RequestMapping("/itemSpecs_update/{itemSpecsId}")
    public String itemSpecsUpdate(@PathVariable Integer itemSpecsId, Model model) {
        ItemSpecs itemSpecs = itemSpecsService.selectById(itemSpecsId);
        model.addAttribute("item",itemSpecs);
        LogObjectHolder.me().set(itemSpecs);
        return PREFIX + "itemSpecs_edit.html";
    }

    /**
     * 获取商品规格分页列表
     */
    @RequestMapping(value = "/pageList")
    @ResponseBody
    public Object pageList(ItemSpecs itemSpecs) {
        Page<ItemSpecs> page = new PageFactory<ItemSpecs>().defaultPage();
        Map<String, Object> beanMap = BeanKit.beanToMap(itemSpecs,true);
        EntityWrapper<ItemSpecs> wrapper = new EntityWrapper<>();
        wrapper.allEq(beanMap);
        return itemSpecsService.selectPage(page,wrapper);
    }

    /**
     * 获取商品规格列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(ItemSpecs itemSpecs) {
        Map<String, Object> beanMap = BeanKit.beanToMap(itemSpecs,true);
        EntityWrapper<ItemSpecs> wrapper = new EntityWrapper<>();
        wrapper.allEq(beanMap);
        return itemSpecsService.selectList(wrapper);
    }

    /**
     * 新增商品规格
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(ItemSpecs itemSpecs) {
        itemSpecsService.insert(itemSpecs);
        return SUCCESS_TIP;
    }

    /**
     * 删除商品规格
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam String itemSpecsIds) {
        if(StrUtil.isNotBlank(itemSpecsIds)) {
            itemSpecsService.deleteBatchIds(Arrays.asList(itemSpecsIds.split(",")));
        }
        return SUCCESS_TIP;
    }

    /**
     * 修改商品规格
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(ItemSpecs itemSpecs) {
        itemSpecsService.updateById(itemSpecs);
        return SUCCESS_TIP;
    }

    /**
     * 商品规格详情
     */
    @RequestMapping(value = "/detail/{itemSpecsId}")
    @ResponseBody
    public Object detail(@PathVariable("itemSpecsId") Integer itemSpecsId) {
        return itemSpecsService.selectById(itemSpecsId);
    }
}
