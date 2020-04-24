package com.xinshang.modular.biz.controller;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xinshang.core.base.controller.BaseController;
import com.xinshang.core.support.DateTimeKit;
import com.xinshang.modular.biz.model.Item;
import com.xinshang.modular.biz.service.impl.ItemServiceImpl;
import com.xinshang.modular.biz.vo.SuggestItem;
import org.apache.commons.lang3.StringUtils;
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
import com.xinshang.modular.biz.model.Banner;
import com.xinshang.modular.biz.service.IBannerService;

/**
 * @title:首页轮播图控制器
 *
 * @author: zhangjiajia
 * @since: 2019-11-29 09:17:53
 */
@Controller
@RequestMapping("/banner")
public class BannerController extends BaseController {

    private String PREFIX = "/biz/banner/";

    @Autowired
    private IBannerService bannerService;

    @Autowired
    private ItemServiceImpl itemService;
    /**
     * 跳转到首页轮播图首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "banner.html";
    }

    /**
     * 跳转到添加首页轮播图
     */
    @RequestMapping("/banner_add")
    public String bannerAdd() {
        return PREFIX + "banner_add.html";
    }

    /**
     * 跳转到修改首页轮播图
     */
    @RequestMapping("/banner_update/{bannerId}")
    public String bannerUpdate(@PathVariable Integer bannerId, Model model) {
        Banner banner = bannerService.selectById(bannerId);
        model.addAttribute("banner",banner);
        Wrapper wrapper=new EntityWrapper();
        Item item= itemService.selectOne(wrapper.eq("item_number",banner.getItemNumber()));
        model.addAttribute("item",item);
        LogObjectHolder.me().set(banner);
        return PREFIX + "banner_edit.html";
    }

    /**
     * 关联商品列表检索
     */
    @RequestMapping(value = "/searchItem")
    @ResponseBody
    public Object searchItem(String tittle) {
        SuggestItem suggest = new SuggestItem();
        suggest.setCode(200);
        suggest.setMessage("成功");
        suggest.setRedirect("");
        if (StringUtils.isEmpty(tittle)) {
            return suggest;
        }
        suggest.setValue(bannerService.searchItem(tittle));
        return suggest;
    }
    /**
     * 获取首页轮播图分页列表
     */
    @RequestMapping(value = "/pageList")
    @ResponseBody
    public Object pageList(Banner banner) {
        Page<Banner> page = new PageFactory<Banner>().defaultPage();
        EntityWrapper<Banner> wrapper = new EntityWrapper<>();
        if (banner.getState()!=null&&banner.getState()!=-1){
            wrapper.eq("state",banner.getState());
        } if (banner.getCreateTime() != null) {
            wrapper.eq("DATE_FORMAT(create_time,'%Y-%m-%d')", DateTimeKit.formatDate(banner.getCreateTime()));
        }
        return bannerService.selectPage(page,wrapper);
    }

    /**
     * 获取首页轮播图列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(Banner banner) {
        Map<String, Object> beanMap = BeanKit.beanToMap(banner,true);
        EntityWrapper<Banner> wrapper = new EntityWrapper<>();
        wrapper.allEq(beanMap);
        return bannerService.selectList(wrapper);
    }

    /**
     * 新增首页轮播图
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Banner banner) {
        bannerService.insert(banner);
        return SUCCESS_TIP;
    }

    /**
     * 删除首页轮播图
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam String bannerIds) {
        if(StrUtil.isNotBlank(bannerIds)) {
            bannerService.deleteBatchIds(Arrays.asList(bannerIds.split(",")));
        }
        return SUCCESS_TIP;
    }

    /**
     * 修改首页轮播图
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Banner banner) {
        Banner temp=bannerService.selectById(banner.getId());
        //如果不上传图片的话，使用上次上传的图片
        if (StringUtils.isBlank(banner.getBannerPath())){
            banner.setBannerPath(temp.getBannerPath());
        }
        bannerService.updateById(banner);
        return SUCCESS_TIP;
    }

    /**
     * 首页轮播图详情
     */
    @RequestMapping(value = "/detail/{bannerId}")
    @ResponseBody
    public Object detail(@PathVariable("bannerId") Integer bannerId) {
        return bannerService.selectById(bannerId);
    }
}
