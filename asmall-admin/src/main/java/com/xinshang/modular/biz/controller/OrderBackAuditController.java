package com.xinshang.modular.biz.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xinshang.core.base.controller.BaseController;
import com.xinshang.core.common.constant.factory.PageFactory;
import com.xinshang.core.log.LogObjectHolder;
import com.xinshang.core.support.BeanKit;
import com.xinshang.modular.biz.model.Member;
import com.xinshang.modular.biz.model.Order;
import com.xinshang.modular.biz.model.OrderBack;
import com.xinshang.modular.biz.model.OrderItem;
import com.xinshang.modular.biz.service.IMemberService;
import com.xinshang.modular.biz.service.IOrderBackService;
import com.xinshang.modular.biz.service.IOrderItemService;
import com.xinshang.modular.biz.service.IOrderService;
import com.xinshang.modular.system.model.Dict;
import com.xinshang.modular.system.service.IDictService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @title:退货申请控制器
 *
 * @author: daijunye
 * @since: 2019-10-23 12:18:09
 */
@Controller
@RequestMapping("/orderBackAudit")
public class OrderBackAuditController extends BaseController {

    private String PREFIX = "/biz/orderBackAudit/";

    @Autowired
    private IOrderBackService orderBackService;
    @Autowired
    private IOrderItemService orderItemService;
    @Autowired
    private IOrderService iOrderService;
    @Autowired
    private IMemberService iMemberService;
    @Autowired
    private IDictService iDictService;
    /**
     * 跳转到退货申请首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "orderBack.html";
    }

    /**
     * 跳转到添加退货申请
     */
    @RequestMapping("/orderBack_add")
    public String orderBackAdd() {
        return PREFIX + "orderBack_add.html";
    }

    /**
     * 跳转到修改退货申请
     */
    @RequestMapping("/orderBack_update/{orderBackId}")
    public String orderBackUpdate(@PathVariable Integer orderBackId, Model model) {
        OrderBack orderBack = orderBackService.selectById(orderBackId);
        model.addAttribute("item",orderBack);
        LogObjectHolder.me().set(orderBack);
        return PREFIX + "orderBack_edit.html";
    }

    /**
     * 获取退货申请分页列表
     */
    @RequestMapping(value = "/pageList")
    @ResponseBody
    public Object pageList(OrderBack orderBack) {
        Page<OrderBack> page = new PageFactory<OrderBack>().defaultPage();
        Map<String, Object> beanMap = BeanKit.beanToMap(orderBack,true);
        EntityWrapper<OrderBack> wrapper = new EntityWrapper<>();
//        wrapper.allEq(beanMap);
        if (StringUtils.isNotBlank(orderBack.getOrderId())) {
            wrapper.like("order_id", orderBack.getOrderId());
        }
        if(orderBack.getState() != null) {
            wrapper.eq("state", orderBack.getState());
        }
        wrapper.in("state","3,9");
        wrapper.orderBy("operator_time", false);
        page= orderBackService.selectPage(page,wrapper);
        List<OrderBack> list;
        list = new ArrayList<>();
        for (OrderBack item : page.getRecords()) {
            OrderItem orderItem=orderItemService.selectOne(new EntityWrapper<OrderItem>().eq("order_id", item.getOrderId()));
            Member member=iMemberService.selectOne(new EntityWrapper<Member>().eq("id", item.getOperatorUser()));
            Dict dict = iDictService.selectOne(new EntityWrapper<Dict>().eq("parent_id", 539).eq("code", item.getBackCompanyCode()));
            Order order=iOrderService.selectOne(new EntityWrapper<Order>().eq("order_id", item.getOrderId()));
            if(order!=null){
                item.setPayAmount(order.getPayAmount());
                item.setFreight(order.getFreight());}
            if(member!=null){
                item.setNickname(member.getNickname());
                item.setUsername(member.getUsername());
                item.setRealName(member.getRealName());
                item.setPhone(member.getPhone());}
            if(dict!=null){
                item.setBackCompanyName(dict.getName());}
            if(orderItem!=null){
                item.setTitle(orderItem.getTitle());}
            list.add(item);
        }
        page.setRecords(list);
        return page;
    }

    /**
     * 获取退货申请列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(OrderBack orderBack) {
        Map<String, Object> beanMap = BeanKit.beanToMap(orderBack,true);
        EntityWrapper<OrderBack> wrapper = new EntityWrapper<>();
        wrapper.allEq(beanMap);
        return orderBackService.selectList(wrapper);
    }

    /**
     * 新增退货申请
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(OrderBack orderBack) {
        orderBackService.insert(orderBack);
        return SUCCESS_TIP;
    }

    /**
     * 删除退货申请
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam String orderBackIds) {
        if(StrUtil.isNotBlank(orderBackIds)) {
            orderBackService.deleteBatchIds(Arrays.asList(orderBackIds.split(",")));
        }
        return SUCCESS_TIP;
    }

    /**
     * 修改退货申请
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(OrderBack orderBack) {
        orderBackService.updateById(orderBack);
        return SUCCESS_TIP;
    }

    /**
     * 退货申请详情
     */
    @RequestMapping(value = "/detail/{orderBackId}")
    @ResponseBody
    public Object detail(@PathVariable("orderBackId") Integer orderBackId) {
        return orderBackService.selectById(orderBackId);
    }
}
