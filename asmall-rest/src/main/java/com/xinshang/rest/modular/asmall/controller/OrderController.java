package com.xinshang.rest.modular.asmall.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xinshang.rest.common.activemq.ScheduleMessagePostProcessor;
import com.xinshang.rest.common.enums.MqMessageConstant;
import com.xinshang.rest.common.enums.OrderConstant;
import com.xinshang.rest.common.enums.OrderStatusEnum;
import com.xinshang.rest.common.util.JwtTokenUtil;
import com.xinshang.rest.common.util.R;
import com.xinshang.rest.modular.asmall.dto.*;
import com.xinshang.rest.modular.asmall.model.Member;
import com.xinshang.rest.modular.asmall.model.Order;
import com.xinshang.rest.modular.asmall.model.OrderStateRecord;
import com.xinshang.rest.modular.asmall.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.bouncycastle.cms.PasswordRecipientId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 订单信息
 *
 * @author wangxiaokun
 */
@RestController
@RequestMapping("/order")
@Api(value = "订单信息",tags = "订单相关接口")
@AllArgsConstructor
@Slf4j
public class OrderController {

    private final IOrderService orderService;
    private final JwtTokenUtil jwtTokenUtil;
    private final IOrderStateRecordService orderStateRecordService;
    private final IPostMsgService iPostMsgService;

    /**
     * 创建订单
     * @param createOrderQueryDTO
     * @param request
     * @return
     */
    @ApiOperation(value = "创建订单", notes = "创建订单")
    @PostMapping("/addOrder")
    public R createOrder(@RequestBody CreateOrderQueryDTO createOrderQueryDTO, HttpServletRequest request) {
        Member member = jwtTokenUtil.getMemberFromRequest(request);
        if (member == null) {
            return R.failed("用户不存在");
        }
        createOrderQueryDTO.setMemberId(member.getId().toString());

        return orderService.createOrder(createOrderQueryDTO);
    }

    /**
     * 申请退款
     * @param orderDTO
     * @param request
     * @return
     */
    @ApiOperation(value = "申请退款", notes = "申请退款")
    @PostMapping("/payRefund")
    public R payRefund(@RequestBody OrderDTO orderDTO, HttpServletRequest request) {
        Member member = jwtTokenUtil.getMemberFromRequest(request);
        if (member == null) {
            return R.failed("用户不存在");
        }
        orderDTO.setUserId(member.getId().toString());
        R r = orderService.payRefund(orderDTO);
        return r;
    }

    /**
     *  确认支付接收（后台确认+验证码）
     */
    @ApiOperation(value = "商品确认支付", notes = "商品确认支付【后台确认+验证码】")
    @PostMapping("/payConfirm")
    public R payConfirm(@RequestBody OrderDTO orderDTO, HttpServletRequest request) {
        Member member = jwtTokenUtil.getMemberFromRequest(request);
        if (member == null) {
            return R.failed("用户不存在");
        }
        orderDTO.setUserId(member.getId().toString());
        R r = orderService.payConfirm(orderDTO);
        return r;
    }

    /**
     *  计算运费
     */
    @ApiOperation(value = "计算运费", notes = "计算运费")
    @PostMapping("/totleFreight")
    public R getTotleFreight (@RequestBody CreateOrderQueryDTO createOrderQueryDTO) {
        BigDecimal fee = orderService.getTotleFreight(createOrderQueryDTO);
        return R.ok(fee);
    }


    @ApiOperation(value = "商品订单支付", notes = "商品订单支付")
    @PostMapping("/payGoods")
    public R payGoods(@RequestBody OrderDTO orderDTO, HttpServletRequest request) {
        Member member = jwtTokenUtil.getMemberFromRequest(request);
        if (member == null) {
            return R.failed("用户不存在");
        }
        orderDTO.setUserId(member.getId().toString());
        R r = orderService.payGoods(orderDTO);
        return r;
    }


    @ApiOperation(value = "删除订单", notes = "删除订单")
    @PostMapping("/delOrder")
    public R delOrder(@RequestBody OrderDTO orderDTO,HttpServletRequest request) {

        Member member = jwtTokenUtil.getMemberFromRequest(request);
        if (member == null) {
            return R.failed("用户不存在");
        }
        boolean b = orderService.deleteById(orderDTO.getOrderId());
        if (b) {
            return R.ok();
        }
        return R.failed("删除失败");
    }


    @ApiOperation(value = "获得当前用户订单", notes = "获得当前用户订单")
    @PostMapping("/orderList")
    public R orderList(@RequestBody OrderListDTO listDTO, HttpServletRequest request) {
        log.info("获得当前用户订单参数:{}",listDTO);
        Member member = jwtTokenUtil.getMemberFromRequest(request);
        if (member == null) {
            return R.failed("用户不存在");
        }
        listDTO.setUserId(member.getId().toString());
        R r = orderService.orderList(listDTO);
        return r;
    }


    @ApiOperation(value = "更改订单状态", notes = "已完成/申请退单 状态修改接口")
    @PostMapping("/cancelOrder")
    public R cancelOrder(@RequestBody OrderDTO orderDTO,HttpServletRequest request) {
        log.info("取消订单接口");
        Member member = jwtTokenUtil.getMemberFromRequest(request);
        if (member == null) {
            return R.failed("用户不存在");
        }
        orderDTO.setUserId(member.getId()+"");
        Order order = orderService.selectById(orderDTO.getOrderId());
        Integer status = orderDTO.getStatus();
        if(status == 4) {
            Integer orderStatus = order.getStatus();
            if (orderStatus == 8 || orderStatus == 2 || orderStatus == 3) {
                log.info("取消订单接口，订单原状态为:" + orderStatus +"，执行退款操作");
                //执行退款
                R r = orderService.payRefund(orderDTO);
                log.info("取消订单接口，退款结果：" + r);
                if (r.getCode() != 200) {
                    return R.failed("取消订单失败，已付款订单申请通联退款失败");
                }
            }
        }
        order.setStatus(status);
        //如果是确认收货则 设置endTime时间
        if (status == 3) {
            order.setEndTime(new Date());
            //发送超时消息到队列中，订单7天改成已关闭
            log.info(order.getOrderId() + "发送延时" + OrderConstant.ORDER_CLOSE_TIMEOUT + "毫秒消息到队列中，已完成服务订单超时改为已关闭");
            iPostMsgService.sendMessage(MqMessageConstant.ORDER_COMPLATE_TIMEOUT_MESSAGE_PREFIX + order.getOrderId(),
                    new ScheduleMessagePostProcessor(OrderConstant.ORDER_CLOSE_TIMEOUT));

        }
        boolean b = orderService.updateById(order);
        if (b) {
            //新增订单状态
            OrderStateRecord record = new OrderStateRecord();
            //操作人
            record.setOperatorUser(order.getBuyerNick());
            //是否当前状态
            record.setCurrent(true);
            //状态
            record.setState(OrderStatusEnum.待发货.getValue());
            //订单id
            record.setOrderId(order.getOrderId());
            orderStateRecordService.insert(record);
            return R.ok();
        }
        return R.failed("取消订单失败");
    }

    /**
     * 支付确认-通联同步调用
     * @param allinPayResponseDTO
     * @return
     */
    @GetMapping("/confirmPayment")
    public Object confirmPayment(AllinPayResponseDTO<String> allinPayResponseDTO,Model model) {
        log.info("设置支付确认结果:{}",allinPayResponseDTO);

        JSONObject signedValue = (JSONObject) JSON.parseObject(allinPayResponseDTO.getSignedValue()).get("signedValue");

        System.out.println(signedValue);
        //如果支付成功 则修改订单状态
        System.out.println(allinPayResponseDTO);
//        orderService.payResult(allinPayResponseDTO);

        return "http://58.56.184.202:202/pages/money/paySuccess?issuccess=" + allinPayResponseDTO.getStatus() + "&msg=" + allinPayResponseDTO.getMessage();


    }

}
