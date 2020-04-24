package com.xinshang.modular.biz.controller;

import com.xinshang.core.base.controller.BaseController;
import com.xinshang.core.shiro.ShiroKit;
import com.xinshang.core.shiro.ShiroUser;
import com.xinshang.core.support.DateTime;
import com.xinshang.modular.biz.dto.ItemDTO;
import com.xinshang.modular.biz.model.*;
import com.xinshang.modular.biz.service.*;
import com.xinshang.modular.biz.vo.ExamineVO;
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
import java.util.Arrays;

import cn.hutool.core.util.StrUtil;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.xinshang.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @title:商品发布审核控制器
 * @author: sunhao
 * @since: 2019-10-18 10:35:45
 */
@Controller
@RequestMapping("/itemAudit")
public class ItemAuditController extends BaseController {

    private String PREFIX = "/biz/itemAudit/";

    @Autowired
    private IItemAuditService itemAuditService;

    @Autowired
    private IItemService itemService;

    @Autowired
    private IExamineService examineService;

    @Autowired
    private IItemSpecsService iItemSpecsService;

    @Autowired
    private IPostageService postageService;

    /**
     * 商品发布审核(通过)
     */
    @RequestMapping(value = "/access")
    @ResponseBody
    public Object access(ItemAudit itemAudit) {
        //获取当前审批者姓名
        ShiroUser user = ShiroKit.getUser();
        Examine examine = new Examine();
        examine.setProjectId(itemAudit.getId());
        examine.setCreateTime(new DateTime());
        examine.setType(2);
        examine.setState(2);
        examine.setRemarks(itemAudit.getAuditDetail());
        examine.setUserId(Long.valueOf(user.getId()));
        examine.setUserName(user.getName());
        examineService.insert(examine);

        EntityWrapper<Item> itemEntityWrapper = new EntityWrapper<>();
        itemEntityWrapper.eq("id", itemAudit.getId());
        Item item = itemService.selectOne(itemEntityWrapper);
        if (item != null) {
            item.setAuditStatus(examine.getState());
            //商品审核通过自动设置上架状态
            item.setStatus(1);
            itemService.update(item, itemEntityWrapper);
        }
        return SUCCESS_TIP;


    }

    /**
     * 商品发布审核(拒绝)
     */
    @RequestMapping(value = "/refuse")
    @ResponseBody
    public Object refuse(ItemAudit itemAudit) {
        //获取当前审批者姓名
        ShiroUser user = ShiroKit.getUser();
        Examine examine = new Examine();
        examine.setProjectId(itemAudit.getId());
        examine.setCreateTime(new DateTime());
        examine.setType(2);
        examine.setState(3);
        examine.setUserId(Long.valueOf(user.getId()));
        examine.setRemarks(itemAudit.getAuditDetail());
        examine.setUserName(user.getName());
        examineService.insert(examine);
        EntityWrapper<Item> itemEntityWrapper = new EntityWrapper<>();
        itemEntityWrapper.eq("id", itemAudit.getId());
        Item item = itemService.selectOne(itemEntityWrapper);
        if (item != null) {
            item.setAuditStatus(examine.getState());
            item.setStatus(-1);
            itemService.update(item, itemEntityWrapper);
        }
        return SUCCESS_TIP;
    }

    /**
     * 跳转到商品发布审核
     */
    @RequestMapping("/item_audit/{itemId}")
    public String itemAudit(@PathVariable Long itemId, Model model) {
        Item item = itemService.selectById(itemId);
        item.setDetailBannerList(item.getDetailBanner().split(","));
        model.addAttribute("item", item);
        List<ItemSpecs> itemSpecsS = iItemSpecsService.selectList(new EntityWrapper<ItemSpecs>().eq("item_no", item.getItemNumber()));
        List<String> attrNames = new ArrayList<>();
        if(itemSpecsS.size()>0){
            String[] attrInfos = item.getAttrInfo().split("\\|");
            for (int i = 0; i <attrInfos.length ; i++) {
                attrNames.add(attrInfos[i].split(":")[0]);
            }
            for (ItemSpecs specsS : itemSpecsS) {
                specsS.setSpecsValuesArr(specsS.getSpecsValues().split(","));
            }
        }
        List<Postage> postages = postageService.selectList(new EntityWrapper<Postage>().eq("item_number", item.getItemNumber()));
        model.addAttribute("itemSpecs",itemSpecsS);
        model.addAttribute("attrNames",attrNames);
        model.addAttribute("postages",postages);
        LogObjectHolder.me().set(item);
        return PREFIX + "item_audit.html";
    }

    /**
     * 跳转到商品审核记录页面
     * @param itemId
     * @param model
     * @return
     */
    @RequestMapping("/item_list/{itemId}")
    public Object itemAudit(@PathVariable String itemId, Model model) {

        List<ExamineVO> item = examineService.selectListById(itemId);
        for (ExamineVO vo:item
        ) {
            vo.setTypeName("商品发布审核");
        }
        model.addAttribute("item",item);
        return PREFIX + "audit_list.html";
    }

    /**
     * 跳转到商品发布审核首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "itemAudit.html";
    }

    /**
     * 跳转到添加商品发布审核
     */
    @RequestMapping("/itemAudit_add")
    public String itemAuditAdd() {
        return PREFIX + "itemAudit_add.html";
    }

    /**
     * 跳转到修改商品发布审核
     */
    @RequestMapping("/itemAudit_update/{itemAuditId}")
    public String itemAuditUpdate(@PathVariable Integer itemAuditId, Model model) {
        ItemAudit itemAudit = itemAuditService.selectById(itemAuditId);
        model.addAttribute("item", itemAudit);
        LogObjectHolder.me().set(itemAudit);
        return PREFIX + "itemAudit_edit.html";
    }

    /**
     * 获取商品发布审核分页列表
     */
    @RequestMapping(value = "/pageList")
    @ResponseBody
    public Object pageList(ItemDTO item) {
        Page<Item> page = new PageFactory<Item>().defaultPage();
        Map<String, Object> beanMap = BeanKit.beanToMap(item, true);
        EntityWrapper<Item> wrapper = new EntityWrapper<>();
        wrapper.ne("audit_status", 2);
        wrapper.ne("audit_status", 0);
        wrapper.ne("audit_status", 4);
        wrapper.allEq(beanMap);
        return itemService.selectPage(page, wrapper);
    }

    /**
     * 获取商品发布审核列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(ItemAudit itemAudit) {
        Map<String, Object> beanMap = BeanKit.beanToMap(itemAudit, true);
        EntityWrapper<ItemAudit> wrapper = new EntityWrapper<>();
        wrapper.allEq(beanMap);
        return itemAuditService.selectList(wrapper);
    }

    /**
     * 新增商品发布审核
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(ItemAudit itemAudit) {
        itemAuditService.insert(itemAudit);
        return SUCCESS_TIP;
    }

    /**
     * 删除商品发布审核
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam String itemAuditIds) {
        if (StrUtil.isNotBlank(itemAuditIds)) {
            itemAuditService.deleteBatchIds(Arrays.asList(itemAuditIds.split(",")));
        }
        return SUCCESS_TIP;
    }

    /**
     * 修改商品发布审核
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(ItemAudit itemAudit) {
        itemAuditService.updateById(itemAudit);
        return SUCCESS_TIP;
    }

    /**
     * 商品发布审核详情
     */
    @RequestMapping(value = "/detail/{itemAuditId}")
    @ResponseBody
    public Object detail(@PathVariable("itemAuditId") Integer itemAuditId) {
        return itemAuditService.selectById(itemAuditId);
    }
}
