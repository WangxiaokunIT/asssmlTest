package com.xinshang.modular.biz.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.SqlHelper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xinshang.constant.Constants;
import com.xinshang.constant.MqMessageConstant;
import com.xinshang.core.activemq.ScheduleMessagePostProcessor;
import com.xinshang.modular.biz.model.Order;
import com.xinshang.modular.biz.dao.OrderMapper;
import com.xinshang.modular.biz.model.OrderBack;
import com.xinshang.modular.biz.service.IOrderBackService;
import com.xinshang.modular.biz.service.IOrderService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xinshang.modular.system.dao.DictMapper;
import com.xinshang.modular.system.model.Dict;
import com.xinshang.modular.system.service.IPostMsgService;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author wangxiaokun
 * @since 2019-10-17
 */
@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private DictMapper dictMapper;
    @Autowired
    private IPostMsgService iPostMsgService;
    @Autowired
    private IOrderBackService orderBackService;

    /**
     * 功能描述: 完善订单发货信息
     *
     * @Param: [order]
     * @Return: boolean
     * @Auther: wangxiaokun
     * @Date: 2019/10/18 10:12
     * @Description:
     * @Modify:
     */
    @Override
    public boolean addDeliveryInfo(Order order) {

        //状态 0全部 1待付款 2待收货 3已完成 4已取消 5已结束 6退货中 7退货完成 8待发货
        order.setStatus(2);
        String shippingId = order.getShippingId();
        EntityWrapper<Dict> dictWrapper = new EntityWrapper<>();
        dictWrapper.eq("code",shippingId);
        List<Dict> dicts = dictMapper.selectList(dictWrapper);

        order.setShippingName(dicts.get(0).getName());
        order.setConsignTime(new Date());
        Integer flag = orderMapper.updateById(order);
        if (flag != 0) {
            //发送超时消息到队列中，订单超时10天未收货状态改为已结束
            log.info(order.getOrderId() + "发送延时" + Constants.ORDER_COMPLATE_TIMEOUT + "毫秒消息到队列中，订单超时10天未收货状态改为已完成");
            iPostMsgService.sendMessage(MqMessageConstant.ORDER_RECEIVING_GOODS_TIMEOUT_MESSAGE_PREFIX + order.getOrderId(),
                    new ScheduleMessagePostProcessor(Constants.ORDER_COMPLATE_TIMEOUT));

            //发送超时消息到队列中，订单超时17天已完成状态改为已结束
            log.info(order.getOrderId() + "发送延时" + 1468800000L + "毫秒消息到队列中，订单超时10天已完成状态改为已结束");
            iPostMsgService.sendMessage(MqMessageConstant.ORDER_COMPLATE_TIMEOUT_MESSAGE_PREFIX + order.getOrderId(),
                    new ScheduleMessagePostProcessor(1468800000L));

            return true;
        }
        return false;
    }

    /**
     * 功能描述: 强制取消订单【不退款】
     *
     * @Param: [orderId]
     * @Return: boolean
     * @Auther: wangxiaokun
     * @Date: 2019/10/18 10:33
     * @Description:
     * @Modify:
     */
    @Override
    public boolean cancel(String orderId) {

        Order order = orderMapper.selectById(orderId);
        order.setStatus(4);
        Integer flag = orderMapper.updateById(order);
        if (flag != 0) {
            return true;
        }
        return false;
    }

    /**
     * 功能描述: 强制取消订单【退款】
     *
     * @Param: [orderId]
     * @Return: boolean
     * @Auther: wangxiaokun
     * @Date: 2019/10/18 10:33
     * @Description:
     * @Modify:
     */
    @Override
    public boolean cancelFreight(String orderId) {

        Order order = orderMapper.selectById(orderId);
        OrderBack orderDTO = new OrderBack();
        orderDTO.setOrderId(orderId);
        Integer status = order.getStatus();
        if (status == 8 || status == 2 || status == 3) {
            //退款
            orderBackService.refundConfirm(orderDTO);
        }
        order.setStatus(4);
        Integer flag = orderMapper.updateById(order);
        if (flag != 0) {
            return true;
        }
        return false;
    }

}
