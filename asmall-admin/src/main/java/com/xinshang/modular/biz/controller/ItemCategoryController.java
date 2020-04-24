package com.xinshang.modular.biz.controller;

import com.xinshang.core.base.controller.BaseController;
import com.xinshang.core.common.constant.factory.ConstantFactory;
import com.xinshang.core.common.constant.state.SystemTreeTableEnum;
import com.xinshang.core.node.ZTreeNode;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import com.xinshang.core.support.BeanKit;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xinshang.core.common.constant.factory.PageFactory;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.xinshang.core.log.LogObjectHolder;
import com.xinshang.modular.biz.model.ItemCategory;
import com.xinshang.modular.biz.service.IItemCategoryService;

/**
 * @title:商品分类控制器
 *
 * @author: zhangjiajia
 * @since: 2019-11-25 10:25:54
 */
@Controller
@RequestMapping("/itemCategory")
public class ItemCategoryController extends BaseController {

    private String PREFIX = "/biz/itemCategory/";

    @Autowired
    private IItemCategoryService itemCategoryService;

    /**
     * 跳转到商品分类首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "itemCategory.html";
    }

    /**
     * 跳转到添加商品分类
     */
    @RequestMapping("/itemCategory_add")
    public String itemCategoryAdd() {
        return PREFIX + "itemCategory_add.html";
    }

    /**
     * 跳转到修改商品分类
     */
    @RequestMapping("/itemCategory_update/{itemCategoryId}")
    public String itemCategoryUpdate(@PathVariable Integer itemCategoryId, Model model) {
        ItemCategory itemCategory = itemCategoryService.selectById(itemCategoryId);
        model.addAttribute("item",itemCategory);
        LogObjectHolder.me().set(itemCategory);
        return PREFIX + "itemCategory_edit.html";
    }

    /**
     * 获取商品分类分页列表
     */
    @RequestMapping(value = "/pageList")
    @ResponseBody
    public Object pageList(ItemCategory itemCategory) {
        Page<ItemCategory> page = new PageFactory<ItemCategory>().defaultPage();
        Map<String, Object> beanMap = BeanKit.beanToMap(itemCategory,true);
        EntityWrapper<ItemCategory> wrapper = new EntityWrapper<>();
        wrapper.allEq(beanMap);
        return itemCategoryService.selectPage(page,wrapper);
    }

    /**
     * 获取商品分类列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(ItemCategory itemCategory) {
        Map<String, Object> beanMap = BeanKit.beanToMap(itemCategory,true);
        EntityWrapper<ItemCategory> wrapper = new EntityWrapper<>();
        wrapper.allEq(beanMap);
        return itemCategoryService.selectList(wrapper);
    }

    /**
     * 新增商品分类
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(ItemCategory itemCategory) {
        itemCategoryService.insert(itemCategory);
        ConstantFactory.me().addSeqAndLevelByIdAndParentId(SystemTreeTableEnum.ITEM_CATEGORY.getTableName(),itemCategory.getId(),itemCategory.getParentId());
        return SUCCESS_TIP;
    }

    /**
     * 删除商品分类
     */
    @RequestMapping(value = "/delete/{itemCategoryId}")
    @ResponseBody
    public Object delete(@PathVariable Integer itemCategoryId) {

        List<ItemCategory> itemCategories = itemCategoryService.selectList(new EntityWrapper<ItemCategory>().eq("parent_id",itemCategoryId));
        Assert.isTrue(itemCategories.size()==0,"该分类不能删除");
        itemCategoryService.deleteById(itemCategoryId);
        return SUCCESS_TIP;
    }

    /**
     * 修改商品分类
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(ItemCategory itemCategory) {
        itemCategoryService.updateById(itemCategory);
        /**
         * 修改seq和level
         */
        ConstantFactory.me().updateSeqAndLevelById(SystemTreeTableEnum.ITEM_CATEGORY.getTableName(),itemCategory.getId());
        return SUCCESS_TIP;
    }

    /**
     * 商品分类详情
     */
    @RequestMapping(value = "/detail/{itemCategoryId}")
    @ResponseBody
    public Object detail(@PathVariable("itemCategoryId") Integer itemCategoryId) {
        return itemCategoryService.selectById(itemCategoryId);
    }

    /**
     * 获取部门的tree列表
     */
    @RequestMapping(value = "/loadTree")
    @ResponseBody
    public List<ZTreeNode> loadTree() {
        return itemCategoryService.loadTree();
    }
}
