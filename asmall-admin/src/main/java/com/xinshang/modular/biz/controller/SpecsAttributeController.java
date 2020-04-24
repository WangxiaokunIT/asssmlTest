package com.xinshang.modular.biz.controller;

import com.xinshang.core.base.controller.BaseController;
import com.xinshang.modular.biz.model.ItemCategory;
import com.xinshang.modular.biz.service.IItemCategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import com.xinshang.core.support.BeanKit;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xinshang.core.common.constant.factory.PageFactory;

import java.util.List;
import java.util.Map;
import java.util.Arrays;
import cn.hutool.core.util.StrUtil;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.xinshang.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.xinshang.modular.biz.model.SpecsAttribute;
import com.xinshang.modular.biz.service.ISpecsAttributeService;

/**
 * @title:规格属性控制器
 *
 * @author: zhangjiajia
 * @since: 2019-11-25 10:24:53
 */
@Controller
@RequestMapping("/specsAttribute")
public class SpecsAttributeController extends BaseController {

    private String PREFIX = "/biz/specsAttribute/";

    @Autowired
    private ISpecsAttributeService specsAttributeService;
    
    @Autowired
    private IItemCategoryService iItemCategoryService;


    /**
     * 跳转到规格属性首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "specsAttribute.html";
    }

    /**
     * 跳转到添加规格属性
     */
    @RequestMapping("/specsAttribute_add")
    public String specsAttributeAdd() {
        return PREFIX + "specsAttribute_add.html";
    }

    /**
     * 跳转到修改规格属性
     */
    @RequestMapping("/specsAttribute_update/{specsAttributeId}")
    public String specsAttributeUpdate(@PathVariable Integer specsAttributeId, Model model) {
        SpecsAttribute specsAttribute = specsAttributeService.selectById(specsAttributeId);
        model.addAttribute("item",specsAttribute);
        LogObjectHolder.me().set(specsAttribute);
        return PREFIX + "specsAttribute_edit.html";
    }

    /**
     * 获取规格属性分页列表
     */
    @RequestMapping(value = "/pageList")
    @ResponseBody
    public Object pageList(SpecsAttribute specsAttribute) {
        Page<SpecsAttribute> page = new PageFactory<SpecsAttribute>().defaultPage();
        Map<String, Object> beanMap = BeanKit.beanToMap(specsAttribute,true);
        EntityWrapper<SpecsAttribute> wrapper = new EntityWrapper<>();
        wrapper.allEq(beanMap);
        return specsAttributeService.selectPage(page,wrapper);
    }

    /**
     * 获取规格属性列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(SpecsAttribute specsAttribute) {
        Map<String, Object> beanMap = BeanKit.beanToMap(specsAttribute,true);
        EntityWrapper<SpecsAttribute> wrapper = new EntityWrapper<>();
        wrapper.allEq(beanMap);
        return specsAttributeService.selectList(wrapper);
    }

    /**
     * 新增规格属性
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(SpecsAttribute specsAttribute) {
        specsAttributeService.insert(specsAttribute);
        return SUCCESS_TIP;
    }

    /**
     * 删除规格属性
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam String specsAttributeIds) {
        if(StrUtil.isNotBlank(specsAttributeIds)) {
            specsAttributeService.deleteBatchIds(Arrays.asList(specsAttributeIds.split(",")));
        }
        return SUCCESS_TIP;
    }

    /**
     * 修改规格属性
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(SpecsAttribute specsAttribute) {
        specsAttributeService.updateById(specsAttribute);
        return SUCCESS_TIP;
    }

    /**
     * 规格属性详情
     */
    @RequestMapping(value = "/detail/{specsAttributeId}")
    @ResponseBody
    public Object detail(@PathVariable("specsAttributeId") Integer specsAttributeId) {
        return specsAttributeService.selectById(specsAttributeId);
    }

    /**
     * 获取规格属性列表及属性值
     */
    @RequestMapping(value = "/listAndValue")
    @ResponseBody
    public Object listAndValue(Integer categoryId) {
        final ItemCategory itemCategory = iItemCategoryService.selectById(categoryId);
        final Integer parentId = itemCategory.getParentId();
        List<SpecsAttribute> specsAttributes = specsAttributeService.selectList(new EntityWrapper<SpecsAttribute>().eq("category_id",categoryId));
        if(parentId != 0){
            List<SpecsAttribute> parents = specsAttributeService.selectList(new EntityWrapper<SpecsAttribute>().eq("category_id",parentId));
            specsAttributes.addAll(parents);
        }
        return specsAttributes;
    }
}
