package com.xinshang.modular.biz.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xinshang.constant.OrderStatusEnum;
import com.xinshang.core.base.controller.BaseController;
import com.xinshang.core.common.constant.factory.PageFactory;
import com.xinshang.core.log.LogObjectHolder;
import com.xinshang.core.shiro.ShiroKit;
import com.xinshang.core.shiro.ShiroUser;
import com.xinshang.core.support.BeanKit;
import com.xinshang.modular.biz.dao.*;
import com.xinshang.modular.biz.model.*;
import com.xinshang.modular.biz.service.*;
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

import java.math.BigDecimal;
import java.util.*;

/**
 * @title:退货申请控制器
 *
 * @author: daijunye
 * @since: 2019-10-23 12:18:09
 */
@Controller
@RequestMapping("/orderBackRecive")
public class OrderBackReciveController extends BaseController {

    private String PREFIX = "/biz/orderBackRecive/";

    @Autowired
    private IOrderBackService orderBackService;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IItemService itemService;
    @Autowired
    private IOrderItemService orderItemService;
    @Autowired
    private OrderBackMapper orderBackMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private  IMemberService iMemberService;
    @Autowired
    private OrderStateRecordMapper orderStateRecordMapper;
    @Autowired
    private ClientLogMoneysMapper clientLogMoneysMapper;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private IOrderService iOrderService;
    @Autowired private IDictService iDictService;
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
        //orderBackService.refundConfirm(orderBack);
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
        wrapper.in("state","12,15,18");
        wrapper.orderBy("operator_time", false);
        page=orderBackService.selectPage(page,wrapper);
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
        ShiroUser user = ShiroKit.getUser();
        orderBack.setAuditUser(user.getName());
        orderBack.setAuditTime(new Date());
//        }
        if(orderBack.getState()==18)
        {
            String a=orderBackService.refundConfirm(orderBack);
            Order ordertest = iOrderService.selectOne(new EntityWrapper<Order>().eq("refund_num", a));
            if(ordertest!=null) {
                OrderBack orderBack1=orderBackMapper.selectById(orderBack);
                Order order = iOrderService.selectOne(new EntityWrapper<Order>().eq("order_id", orderBack1.getOrderId()));
                Member member=memberMapper.selectById(Integer.parseInt(String.valueOf(order.getUserId())));
                OrderItem orderItem=orderItemService.selectOne(new EntityWrapper<OrderItem>().eq("order_id", orderBack1.getOrderId()));

                Item item=itemService.selectOne(new EntityWrapper<Item>().eq("id", orderItem.getItemId()));
                orderBackService.addOpt(order.getOrderId());
                EntityWrapper<Account> accountEntityWrapper= new EntityWrapper<Account>();
                accountEntityWrapper.eq("master_id", member.getId());
                accountEntityWrapper.eq("type",1);
                ClientLogMoneys moneys = new ClientLogMoneys();
                moneys.setCreateTime(new Date());
                moneys.setMoney(order.getPayAmount());
                moneys.setTradeNo(order.getPayNum());
                moneys.setDataSrc(5);
                moneys.setDeleteFlg(1);
                //1：收入 2：支出
                moneys.setMoneyType(1);
                moneys.setRemark("退款");
                moneys.setClientId(order.getUserId());
                moneys.setUserName(order.getBuyerNick());
                clientLogMoneysMapper.insert(moneys);
                //新增订单状态
                OrderStateRecord tsr = new OrderStateRecord();
                //操作人
                tsr.setOperatorUser(order.getBuyerNick());
                //是否当前状态
                tsr.setIsCurrent(1);
                //状态
                tsr.setState(OrderStatusEnum.退货完成.getValue());
                //订单id
                tsr.setOrderId(order.getOrderId());
                orderStateRecordMapper.insert(tsr);

                //Account account=accountService.selectOne(accountEntityWrapper);
                //account.setTotleAmount(account.getTotleAmount().add(order.getPayAmount()));
                //account.setAvailableBalance(account.getAvailableBalance().add(order.getPayAmount()));
                //accountService.updateById(account);
                //orderBack.setState(7);
                orderBackService.updateById(orderBack);
                order.setStatus(7);
                orderMapper.updateById(order);
            }
            else
            { return  a;}
        }
        else
        {

            orderBackService.updateById(orderBack);
        }
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
