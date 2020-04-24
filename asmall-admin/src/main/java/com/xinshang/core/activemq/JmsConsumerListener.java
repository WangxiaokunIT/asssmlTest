package com.xinshang.core.activemq;

import com.xinshang.constant.MqMessageConstant;
import com.xinshang.constant.OrderStatusEnum;
import com.xinshang.modular.biz.model.Order;
import com.xinshang.modular.biz.model.OrderStateRecord;
import com.xinshang.modular.biz.service.IOrderService;
import com.xinshang.modular.biz.service.IOrderStateRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * 队列消息消费者
 */
@Component
@Slf4j
public class JmsConsumerListener {

    @Autowired
    private IOrderService orderService;
    @Autowired
    private IOrderStateRecordService orderStateRecordService;


    @JmsListener(destination = "${spring.activemq.queue-name}")
    public void receiveQueue(String msg) {
        log.info("消费者接收数据 : " + msg);
        onMessage(msg);
    }


    /**
     * 监听收到消息
     *
     * @param text
     */
    public void onMessage(String text) {

        if (text.indexOf(MqMessageConstant.ORDER_NO_PAY_TIMEOUT_MESSAGE_PREFIX) != -1) {
            //获取订单id
            String orderId = text.split(":")[1];
            Order order = orderService.selectById(orderId);
            if (order == null) {
                return;
            }
            if (!order.getStatus().equals(OrderStatusEnum.待付款.getValue())) {
                return;
            }
            try {
                log.info(orderId + "未支付订单超时改为已取消");
                order.setStatus(OrderStatusEnum.已取消.getValue());
                orderService.updateById(order);
                changeOrderRecode(orderId, OrderStatusEnum.已取消.getValue(), "未支付订单超时改为已取消");
            } catch (Exception e) {
                e.printStackTrace();
                log.warn("redisKey失效监听-" + orderId + "未支付订单超时改为已取消失败");
            }
            //订单超时未收货状态改为已完成和已结束
        } else if (text.indexOf(MqMessageConstant.ORDER_RECEIVING_GOODS_TIMEOUT_MESSAGE_PREFIX) != -1) {
            //获取订单id
            String orderId = text.split(":")[1];
            Order order = orderService.selectById(orderId);

            if (order == null) {
                return;
            }
            if (!order.getStatus().equals(OrderStatusEnum.待收货.getValue())) {
                return;
            }

            try {
                //确认收货完成订单
                log.info(orderId + "订单超时未收货状态改为已完成");
                order.setStatus(OrderStatusEnum.已完成.getValue());
                orderService.updateById(order);
                changeOrderRecode(orderId, OrderStatusEnum.已完成.getValue(), "订单超时未收货状态改为已完成");

            } catch (Exception e) {
                e.printStackTrace();
                log.warn("redisKey失效监听-" + orderId + "订单超时未收货状态改为已结束失败");
            }


            //订单已完成超时改为已结束
        } else if (text.indexOf(MqMessageConstant.ORDER_COMPLATE_TIMEOUT_MESSAGE_PREFIX) != -1) {
            //获取订单id
            String orderId = text.split(":")[1];
            Order order = orderService.selectById(orderId);

            if (order == null) {
                return;
            }
            if (!order.getStatus().equals(OrderStatusEnum.已完成.getValue())) {
                return;
            }

            try {
                log.info(orderId + "订单已完成超时改为已结束");
                order.setStatus(OrderStatusEnum.已结束.getValue());
                orderService.updateById(order);
                changeOrderRecode(orderId, OrderStatusEnum.已结束.getValue(), "订单已完成超时改为已结束");
            } catch (Exception e) {
                e.printStackTrace();
                log.warn("redisKey失效监听-" + orderId + "订单已完成超时改为已结束失败");
            }
        }
    }


    /**
     * 功能描述: 增加订单状态记录表
     *
     * @Param: [orderId, status, msg]
     * @Return: java.lang.Integer
     * @Auther: wangxiaokun
     * @Date: 2019/10/25 10:47
     * @Description:
     * @Modify:
     */
    public boolean changeOrderRecode(String orderId, Integer status, String msg) {

        OrderStateRecord record = new OrderStateRecord();
        record.setOrderId(orderId);
        record.setState(status);
        record.setOperatorUser("system");
        record.setIsCurrent(1);
        record.setRemark(msg);

        boolean insert = orderStateRecordService.insert(record);
        return insert;

    }


}