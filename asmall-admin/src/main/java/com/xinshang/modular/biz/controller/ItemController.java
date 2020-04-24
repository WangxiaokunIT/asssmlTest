package com.xinshang.modular.biz.controller;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xinshang.core.base.controller.BaseController;
import com.xinshang.core.shiro.ShiroKit;
import com.xinshang.core.shiro.ShiroUser;
import com.xinshang.core.support.DateTime;
import com.xinshang.core.util.NoUtil;
import com.xinshang.modular.biz.dto.ItemDTO;
import com.xinshang.modular.biz.model.*;
import com.xinshang.modular.biz.service.*;
import com.xinshang.modular.biz.utils.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import com.xinshang.core.support.BeanKit;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xinshang.core.common.constant.factory.PageFactory;
import java.util.*;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import com.xinshang.core.log.LogObjectHolder;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;

/**
 * @title:商品管理控制器
 * @author: sunhao
 * @since: 2019-10-16 15:11:38
 */
@Controller
@RequestMapping("/item")
public class ItemController extends BaseController {

    private String PREFIX = "/biz/item/";

    @Autowired
    private IItemService itemService;

    @Autowired
    private IExamineService examineService;

    @Autowired
    private IItemSpecsService iItemSpecsService;

    @Autowired
    private ISpecsAttributeService specsAttributeService;

    @Autowired
    private IPostageService postageService;

    /**
     * 跳转到商品管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "item.html";
    }

    /**
     * 跳转到添加商品管理
     */
    @RequestMapping("/item_add")
    public String itemAdd() {
        return PREFIX + "item_add.html";
    }



    /**
     * 跳转到修改商品管理
     */
    @RequestMapping("/item_update/{itemId}")
    public String itemUpdate(@PathVariable String itemId, Model model) {

        Item item = itemService.selectById(itemId);
        item.setDetailBannerList(item.getDetailBanner().split(","));
        model.addAttribute("item", item);
        List<ItemSpecs> itemSpecsS = iItemSpecsService.selectList(new EntityWrapper<ItemSpecs>().eq("item_no", item.getItemNumber()));
        List<String> attrNames = new ArrayList<>();
        if(itemSpecsS.size()>0){
            String[] attrInfos = item.getAttrInfo().split("\\|");
            for (int i = 0; i <attrInfos.length ; i++) {
                attrNames.add(specsAttributeService.selectOne(new EntityWrapper<SpecsAttribute>().eq("id",attrInfos[i].split(":")[0])).getAttributeName());
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
        return PREFIX + "item_edit.html";
    }

    /**
     * 跳转到商品详情
     */
    @RequestMapping("/openDetail/{itemId}")
    public String itemDetail(@PathVariable String itemId, Model model) {

        Item item = itemService.selectById(itemId);
        item.setDetailBannerList(item.getDetailBanner().split(","));
        model.addAttribute("item", item);
        List<ItemSpecs> itemSpecsS = iItemSpecsService.selectList(new EntityWrapper<ItemSpecs>().eq("item_no", item.getItemNumber()));
        List<String> attrNames = new ArrayList<>();
        if (itemSpecsS.size() > 0) {
            String[] attrInfos = item.getAttrInfo().split("\\|");
            for (int i = 0; i < attrInfos.length; i++) {
                attrNames.add(specsAttributeService.selectOne(new EntityWrapper<SpecsAttribute>().eq("id",attrInfos[i].split(":")[0])).getAttributeName());
            }
            for (ItemSpecs specsS : itemSpecsS) {
                specsS.setSpecsValuesArr(specsS.getSpecsValues().split(","));
            }
        }
        List<Postage> postages = postageService.selectList(new EntityWrapper<Postage>().eq("item_number", item.getItemNumber()));
        model.addAttribute("itemSpecs", itemSpecsS);
        model.addAttribute("attrNames", attrNames);
        model.addAttribute("postages",postages);
        LogObjectHolder.me().set(item);
        return PREFIX + "item_detail.html";
    }

    /**
     * 获取商品管理分页列表
     */
    @RequestMapping(value = "/pageList")
    @ResponseBody
    public Object pageList(ItemDTO item) {
        Page<Item> page = new PageFactory<Item>().defaultPage();
        //库存量
        final String numCon = item.getNumCon();
        if (StringUtils.isNotBlank(numCon)) {
            if (numCon.indexOf("-") != -1) {
                final String[] numCons = numCon.split("-");
                item.setMinNum(Integer.valueOf(numCons[0]));
                item.setMaxNum(Integer.valueOf(numCons[1]));
            } else {
                item.setMinNum(Integer.valueOf(numCon));

            }
        }

        //销量
        final String numAllCon = item.getNumAllCon();
        if (StringUtils.isNotBlank(numAllCon)) {
            if (numAllCon.indexOf("-") != -1) {
                final String[] numAllCons = numAllCon.split("-");
                item.setMinNumAll(Integer.valueOf(numAllCons[0]));
                item.setMaxNumAll(Integer.valueOf(numAllCons[1]));
            } else {
                item.setMinNumAll(Integer.valueOf(numAllCon));
            }
        }

        Page<Item> itemPage = itemService.selectItemPage(page, item);

        return itemPage;
    }

    /**
     * 获取商品管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(Item item) {
        Map<String, Object> beanMap = BeanKit.beanToMap(item, true);
        EntityWrapper<Item> wrapper = new EntityWrapper<>();
        wrapper.allEq(beanMap);
        return itemService.selectList(wrapper);
    }


    /**
     * 获取商品编号
     */
    @RequestMapping(value = "/getItemNumber")
    @ResponseBody
    public Object getItemNumber(Integer categoryId) {
        int count = itemService.selectCount(new EntityWrapper<Item>().eq("category_id", categoryId));
        String code = NoUtil.generateGoodsCode(categoryId, count);
        SUCCESS_TIP.setMessage(code);
        return SUCCESS_TIP;
    }


    /**
     * 商品申请审核
     */
    @RequestMapping(value = "/apply/{itemId}")
    @ResponseBody
    public Object apply(@PathVariable Long itemId) {
        Examine examine = new Examine();
        ShiroUser user = ShiroKit.getUser();
        examine.setProjectId(itemId);
        examine.setCreateTime(new DateTime());
        examine.setType(2);
        examine.setState(1);
        examine.setUserId(Long.valueOf(user.getId()));
        examine.setUserName(user.getName());
        examineService.insert(examine);
        EntityWrapper<Item> itemEntityWrapper = new EntityWrapper<>();
        itemEntityWrapper.eq("id", itemId);
        Item item = itemService.selectOne(itemEntityWrapper);
        if (item != null) {
            item.setAuditStatus(1);
            itemService.update(item, itemEntityWrapper);
        }
        return SUCCESS_TIP;
    }

    /**
     * 新增商品管理
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Item item) {
        item.setCreated(new Date());
        itemService.add(item);
        return SUCCESS_TIP;
    }

    /**
     * 获取商品规格属性
     */
    @RequestMapping(value = "/getType")
    @ResponseBody
    public Object getType(@RequestParam String categoryId) {
        Wrapper wrapper = new EntityWrapper();
        List list = specsAttributeService.selectList(wrapper.eq("category_id", categoryId));
        return JSON.toJSON(list);
    }

    /**
     * 删除商品管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam String itemIds) {
        if (StrUtil.isNotBlank(itemIds)) {
            itemService.deleteBatchIds(Arrays.asList(itemIds.split(",")));
        }
        return SUCCESS_TIP;
    }

    /**
     * 修改商品管理
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Item item) {
        itemService.update(item);
        return SUCCESS_TIP;
    }

    /**
     * 商品管理详情
     */
    @RequestMapping(value = "/detail/{itemId}")
    @ResponseBody
    public Object detail(@PathVariable("itemId") Integer itemId) {
        return itemService.selectById(itemId);
    }

    /**
     * 下架商品
     *
     * @param itemId
     * @return
     */
    @RequestMapping(value = "/stop/{itemId}")
    @ResponseBody
    public Object stopItem(@PathVariable("itemId") Long itemId) {
        itemService.alertItemState(itemId, -1,0);
        return SUCCESS_TIP;
    }

    /**
     * 发布商品
     *
     * @param itemId
     * @return
     */
    @RequestMapping(value = "/start/{itemId}")
    @ResponseBody
    public Object startItem(@PathVariable("itemId") Long itemId) {

        itemService.alertItemState(itemId, 1,1);
        return SUCCESS_TIP;
    }

    /**
     * 图片上传
     *
     * @param file
     * @param request
     * @return
     */
    @RequestMapping(value = "/upload")
    @ResponseBody
    public Object uploadPicture(MultipartFile file, HttpServletRequest request) {

        try {
            return FileUtil.uploadFilePath(file, request, "img");
        } catch (Exception e) {
            System.out.println("上传失败");
            e.printStackTrace();
            return "false";
        }
    }

    /**
     * 获取商品出售的数量
     *
     * @return
     */
    @RequestMapping(value = "/showItemNum")
    @ResponseBody
    public Object showItemNum() {
        return itemService.showItemNum();
    }

    /**
     * 跳转到商品详情
     */
    @RequestMapping("/openItemBannerDetail/{itemNumber}")
    public String openItemBannerDetail(@PathVariable("itemNumber") String itemNumber, Model model) {
        Item item = itemService.selectOne( new EntityWrapper<Item>().eq("item_number", itemNumber));
        Assert.isTrue(ObjectUtil.isNotNull(item),"该商品不存在");
        item.setDetailBannerList(item.getDetailBanner().split(","));
        model.addAttribute("item", item);
        List<ItemSpecs> itemSpecsS = iItemSpecsService.selectList(new EntityWrapper<ItemSpecs>().eq("item_no", item.getItemNumber()));
        List<String> attrNames = new ArrayList<>();
        if (itemSpecsS.size() > 0) {
            String[] attrInfos = item.getAttrInfo().split("\\|");
            for (int i = 0; i < attrInfos.length; i++) {
                attrNames.add(attrInfos[i].split(":")[0]);
            }
            for (ItemSpecs specsS : itemSpecsS) {
                specsS.setSpecsValuesArr(specsS.getSpecsValues().split(","));
            }
        }
        List<Postage> postages = postageService.selectList(new EntityWrapper<Postage>().eq("item_number", item.getItemNumber()));
        model.addAttribute("itemSpecs", itemSpecsS);
        model.addAttribute("attrNames", attrNames);
        model.addAttribute("postages", postages);
        LogObjectHolder.me().set(item);
        return PREFIX + "item_detail.html";
    }
}
