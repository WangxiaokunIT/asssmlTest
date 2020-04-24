package com.xinshang.modular.biz.controller;

import com.xinshang.core.base.controller.BaseController;
import com.xinshang.core.base.tips.ErrorTip;
import com.xinshang.modular.biz.model.*;
import com.xinshang.modular.biz.service.IMemberService;
import com.xinshang.modular.biz.service.IOrderItemService;
import com.xinshang.modular.biz.service.IOrderShippingService;
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
import java.util.stream.Collectors;

import cn.hutool.core.util.StrUtil;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.xinshang.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.xinshang.modular.biz.service.IOrderService;

/**
 * @title:订单管理控制器
 * @author: wangxiaokun
 * @since: 2019-10-17 14:50:07
 */
@Controller
@RequestMapping("/order")
public class OrderController extends BaseController {

    private String PREFIX = "/biz/order/";

    @Autowired
    private IOrderService orderService;
    @Autowired
    private IOrderItemService orderItemService;
    @Autowired
    private IOrderShippingService orderShippingService;
    @Autowired
    private IMemberService iMemberService;

    /**
     * 跳转到订单详情
     */
    @RequestMapping("/order_info/{orderId}")
    public String orderInfo(@PathVariable String orderId, Model model) {
        //根据订单id获取订单地址信息和商品信息
        OrderShipping orderShipping = orderShippingService.selectById(orderId);

        EntityWrapper<OrderItem> wrapper = new EntityWrapper<>();
        wrapper.eq("order_id", orderId);
        List<OrderItem> items = orderItemService.selectList(wrapper);
        Order order = orderService.selectById(orderId);

        model.addAttribute("orderShipping", orderShipping);
        model.addAttribute("items", items);
        model.addAttribute("order", order);
        return PREFIX + "order_Info.html";
    }


    /**
     * 强制取消订单 不退款
     */
    @RequestMapping(value = "/cancel")
    @ResponseBody
    public Object cancel(@RequestParam String orderId) {
        boolean b = orderService.cancel(orderId);
        if (b) {
            return SUCCESS_TIP;
        }
        return new ErrorTip("服务器错误");
    }

    /**
     * 强制取消订单 退款
     */
    @RequestMapping(value = "/cancelFreight")
    @ResponseBody
    public Object cancelFreight(@RequestParam String orderId) {
        boolean b = orderService.cancelFreight(orderId);
        if (b) {
            return SUCCESS_TIP;
        }
        return new ErrorTip("服务器错误");
    }

    /**
     * 新增订单发货信息
     */
    @RequestMapping(value = "/addDeliveryInfo")
    @ResponseBody
    public Object addDeliveryInfo(Order order) {
        boolean b = orderService.addDeliveryInfo(order);
        if (b) {
            return SUCCESS_TIP;
        }
        return new ErrorTip("服务器错误");
    }


    /**
     * 跳转到发货
     */
    @RequestMapping("/order_delivery/{orderId}")
    public String orderDelivery(@PathVariable String orderId, Model model) {
        //根据订单id获取订单地址信息和商品信息
        OrderShipping orderShipping = orderShippingService.selectById(orderId);

        EntityWrapper<OrderItem> wrapper = new EntityWrapper<>();
        wrapper.eq("order_id", orderId);
        List<OrderItem> items = orderItemService.selectList(wrapper);
        for (OrderItem item : items) {
            String sv = "";
            if(item.getSpecsValues()!=null && item.getSpecsValues().length()>0){
                String[] split = item.getSpecsValues().split(",");
                for (String v : split) {
                    if(StrUtil.isNotEmpty(v)){
                        sv+=v.split(":")[1]+",";
                    }
                }
            }
            item.setSpecsValues(sv);
        }

        model.addAttribute("orderShipping", orderShipping);
        model.addAttribute("items", items);
        model.addAttribute("orderId", orderId);
        return PREFIX + "order_delivery.html";
    }

    /**
     * 跳转到订单管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "order.html";
    }

    /**
     * 跳转到添加订单管理
     */
    @RequestMapping("/order_add")
    public String orderAdd() {
        return PREFIX + "order_add.html";
    }

    /**
     * 跳转到修改订单管理
     */
    @RequestMapping("/order_update/{orderId}")
    public String orderUpdate(@PathVariable Integer orderId, Model model) {
        Order order = orderService.selectById(orderId);
        model.addAttribute("item", order);
        LogObjectHolder.me().set(order);
        return PREFIX + "order_edit.html";
    }

    /**
     * 获取订单管理分页列表
     */
    @RequestMapping(value = "/pageList")
    @ResponseBody
    public Object pageList(Order order) {
        Page<Order> page = new PageFactory<Order>().defaultPage();

        EntityWrapper<Order> wrapper = new EntityWrapper<>();
        wrapper.eq(order.getStatus()!=null,"status",order.getStatus());
        wrapper.eq(order.getCustomType()!=null,"custom_type",order.getCustomType());
        wrapper.like(StrUtil.isNotEmpty(order.getOrderId()),"order_id",order.getOrderId());
        wrapper.like(StrUtil.isNotEmpty(order.getPayNum()),"pay_num",order.getPayNum());
        wrapper.like(StrUtil.isNotEmpty(order.getBuyerNick()),"buyer_nick",order.getBuyerNick());

        if(order.getRealName()!=null) {
            List<Member> memberlist = iMemberService.selectList(new EntityWrapper<Member>().like("real_name", order.getRealName()));
            //获取查询用户的手机号集合
            List<String> collect = memberlist.stream().map(m -> m.getUsername()).collect(Collectors.toList());
            if(memberlist.size()==0) {
                page.setRecords(null);
                return page;
            }
                wrapper.in("buyer_nick",collect);
        }
        wrapper.orderBy("create_time", false);
        page=  orderService.selectPage(page, wrapper);


        List<Order> list;
        list = new ArrayList<>();
        for (Order item : page.getRecords()) {
            Member member=iMemberService.selectOne(new EntityWrapper<Member>().eq("id", item.getUserId()));
            if(member!=null){
                item.setNickname(member.getNickname());
                item.setUsername(member.getUsername());
                item.setRealName(member.getRealName());
                item.setPhone(member.getPhone());}
            list.add(item);
        }
        page.setRecords(list);
        return page;
    }

    /**
     * 获取订单管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(Order order) {
        Map<String, Object> beanMap = BeanKit.beanToMap(order, true);
        EntityWrapper<Order> wrapper = new EntityWrapper<>();
        wrapper.allEq(beanMap);
        return orderService.selectList(wrapper);
    }

    /**
     * 新增订单管理
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Order order) {
        orderService.insert(order);
        return SUCCESS_TIP;
    }

    /**
     * 删除订单管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam String orderIds) {
        if (StrUtil.isNotBlank(orderIds)) {
            orderService.deleteBatchIds(Arrays.asList(orderIds.split(",")));
        }
        return SUCCESS_TIP;
    }

    /**
     * 修改订单管理
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Order order) {
        orderService.updateById(order);
        return SUCCESS_TIP;
    }

    /**
     * 订单管理详情
     */
    @RequestMapping(value = "/detail/{orderId}")
    @ResponseBody
    public Object detail(@PathVariable("orderId") Integer orderId) {
        Order order = orderService.selectById(orderId);
        return order;
    }
}
