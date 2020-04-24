package com.xinshang.rest.modular.asmall.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xinshang.core.exception.SystemException;
import com.xinshang.rest.common.enums.OrderConstant;
import com.xinshang.rest.common.enums.OrderStatusEnum;
import com.xinshang.rest.common.enums.OrderTypeEnum;
import com.xinshang.rest.common.util.R;
import com.xinshang.rest.common.util.RedisUtil;
import com.xinshang.rest.modular.asmall.dao.*;
import com.xinshang.rest.modular.asmall.dto.OrderBackDTO;
import com.xinshang.rest.modular.asmall.dto.OrderDTO;
import com.xinshang.rest.modular.asmall.dto.OrderListDTO;
import com.xinshang.rest.modular.asmall.model.*;
import com.xinshang.rest.modular.asmall.service.IOrderBackService;
import com.xinshang.rest.modular.asmall.service.IPostMsgService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author daijunye
 * @since 2019-10-17
 */
@Service
@Slf4j
public class OrderBackServiceImpl extends ServiceImpl<OrderBackMapper, OrderBack> implements IOrderBackService {


    @Autowired
    private OrderBackMapper orderbackMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private OrderShippingMapper orderShippingMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IPostMsgService iPostMsgService;

    private static String CART_PRE = "CART";


    /**
     * 功能描述: H5端 创建订单
     *
     * @Param: [orderDTO]
     * @Return: boolean
     * @Auther: daijunye
     * @Date: 2019/10/23 17:29
     * @Description:
     * @Modify:
     */
    @Override
    public R createOrder(OrderBackDTO orderbackDTO) {

        //订单保存到数据库中
        try {
            log.error("插入退货订单信息");
            Order order =new Order();
            order.setOrderId(orderbackDTO.getOrderId());
            order=orderMapper.selectOne(order);
            order.setStatus(6);
            orderMapper.updateById(order);
            OrderBack orderback=new OrderBack();
            orderback.setOrderId(orderbackDTO.getOrderId());
            orderback.setOperatorUser(orderbackDTO.getUserName());
            orderback.setOperatorTime(new Date());
            orderback.setBackRemark(orderbackDTO.getBackRemark());
            orderback.setBackMoney(order.getPayAmount().subtract(order.getFreight()));
            orderback.setState(1);
            orderbackMapper.insert(orderback);

        } catch (Exception e) {
            log.error("插入退货订单信息异常");
            R.failed(e.getMessage());
            e.printStackTrace();
            throw new SystemException("生成退货订单失败");
        }
        log.warn("插入退货订单信息完成");

        return R.ok(orderbackDTO);
    }
@Override
public R orderBackOrderId(OrderBackDTO orderbackDTO) {

    //订单保存到数据库中
    try {
        log.error("退货订单号信息");
        OrderBack orderBack=selectOne(new EntityWrapper<OrderBack>().in("state","3").eq("order_id", orderbackDTO.getOrderId()));
        orderBack.setBackOrderId(orderbackDTO.getBackOrderId());
        orderBack.setBackCompanyCode(orderbackDTO.getBackCompanyCode());
        orderBack.setState(9);
        orderbackMapper.updateById(orderBack);

    } catch (Exception e) {
        log.error("退货订单号信息异常");
        R.failed(e.getMessage());
        e.printStackTrace();
        throw new SystemException("退货订单号失败");
    }
    log.warn("退货订单号信息完成");

    return R.ok(orderbackDTO);
}
    @Override
    public R orderList(OrderListDTO listDTO) {




        Page<Order> page = listDTO.defaultPage();
        EntityWrapper<Order> orderWrapper = new EntityWrapper<>();

        Integer status = listDTO.getStatus();
        if (status != 0) {
            //orderWrapper.in("status", "6,7");
        }

        /*Integer customtype = orderDTO.getCustomType();
        if (customtype == 3) {
            orderWrapper.eq("custom_type", 3);
        }*/
        orderWrapper.eq("user_id", listDTO.getUserId());
        orderWrapper.orderBy("create_time", false);
        List<Order> orders = orderMapper.selectPage(page, orderWrapper);
        //获取goodlist
        ArrayList<CartProduct> list;
        CartProduct cartProduct;
        Long currentTime = System.currentTimeMillis();
        for (Order order : orders) {
            EntityWrapper<OrderItem> wrapper = new EntityWrapper<>();
            wrapper.eq("order_id", order.getOrderId());
            List<OrderItem> items = orderItemMapper.selectList(wrapper);


            list = new ArrayList<>();
            //设置订单内goodList
            for (OrderItem item : items) {
                cartProduct = new CartProduct();
                cartProduct.setPrice(item.getPrice());
                cartProduct.setProductNum(item.getNum());
                cartProduct.setProductName(item.getTitle());
                cartProduct.setProductImg(item.getPicPath());
                cartProduct.setItemSpecsValues(item.getSpecsValues());
                list.add(cartProduct);
            }
            order.setGoodsList(list);

            //设置订单地址
            OrderShipping shipping = orderShippingMapper.selectById(order.getOrderId());
            order.setAddressInfo(shipping);

            if (order.getStatus().equals(OrderStatusEnum.待付款.getValue())) {
                Long endTime = 0L;
                if (OrderTypeEnum.普通订单.getValue().equals(order.getCustomType())) {
                    endTime = order.getCreateTime().getTime() + OrderConstant.ORDINARY_ORDER_CANCEL_TIMEOUT;
                }
                //已超时
                if (currentTime > endTime) {
                    order.setOrderRemainingMinutes(0);
                    order.setOrderRemainingSeconds(0);
                    //未超时
                } else {
                    Long SecondDifference = (endTime - currentTime) / 1000;
                    //分钟
                    Long mm = (SecondDifference % 3600) / 60;
                    //秒
                    Long ss = SecondDifference % 60;

                    order.setOrderRemainingMinutes(mm.intValue());
                    order.setOrderRemainingSeconds(ss.intValue());
                }

            } else {
                order.setOrderRemainingMinutes(0);
                order.setOrderRemainingSeconds(0);
            }

        }
        List<Order> newOrderlist= new ArrayList<>();
        Order newnewOrder;
        //获取退货状态
        for (Order order : orders) {

            List<OrderBack> orderBacklist = selectList(new EntityWrapper<OrderBack>().eq("order_id", order.getOrderId()).orderBy("id", false));
            for(OrderBack orderBack:orderBacklist) {
                newnewOrder=new Order();
                newnewOrder=orderMapper.selectById(order);
                EntityWrapper<OrderItem> wrapper = new EntityWrapper<>();
                wrapper.eq("order_id", order.getOrderId());
                List<OrderItem> items = orderItemMapper.selectList(wrapper);


                list = new ArrayList<>();
                //设置订单内goodList
                for (OrderItem item : items) {
                    cartProduct = new CartProduct();
                    cartProduct.setPrice(item.getPrice());
                    cartProduct.setProductNum(item.getNum());
                    cartProduct.setProductName(item.getTitle());
                    cartProduct.setProductImg(item.getPicPath());
                    cartProduct.setItemSpecsValues(item.getSpecsValues());
                    list.add(cartProduct);
                }
                newnewOrder.setGoodsList(list);

                //设置订单地址
                OrderShipping shipping = orderShippingMapper.selectById(order.getOrderId());
                newnewOrder.setAddressInfo(shipping);

                if (order.getStatus().equals(OrderStatusEnum.待付款.getValue())) {
                    Long endTime = 0L;
                    if (OrderTypeEnum.普通订单.getValue().equals(order.getCustomType())) {
                        endTime = order.getCreateTime().getTime() + OrderConstant.ORDINARY_ORDER_CANCEL_TIMEOUT;
                    }
                    //已超时
                    if (currentTime > endTime) {
                        newnewOrder.setOrderRemainingMinutes(0);
                        newnewOrder.setOrderRemainingSeconds(0);
                        //未超时
                    } else {
                        Long SecondDifference = (endTime - currentTime) / 1000;
                        //分钟
                        Long mm = (SecondDifference % 3600) / 60;
                        //秒
                        Long ss = SecondDifference % 60;

                        newnewOrder.setOrderRemainingMinutes(mm.intValue());
                        newnewOrder.setOrderRemainingSeconds(ss.intValue());
                    }

                } else {
                    newnewOrder.setOrderRemainingMinutes(0);
                    newnewOrder.setOrderRemainingSeconds(0);
                }





                OrderBack newOrderBack=new OrderBack();
                newOrderBack=orderBack;

                newnewOrder.setOrderBack(newOrderBack);
                newOrderlist.add(newnewOrder);
            }
        }
        return R.ok(page.setRecords(newOrderlist));
    }



    /**
     * 功能描述: 强制取消订单
     *
     * @Param: [orderId]
     * @Return: boolean
     * @Auther: daijunye
     * @Date: 2019/10/18 10:33
     * @Description:
     * @Modify:
     */
    @Override
    public boolean cancel(String orderId) {
OrderBack orderBack =new OrderBack();
orderBack.setOrderId(orderId);
         orderBack = orderbackMapper.selectOne(orderBack);
        orderBack.setState(6);
        Integer flag = orderbackMapper.updateById(orderBack);
        if (flag != 0) {
            return true;
        }
        return false;
    }
}
