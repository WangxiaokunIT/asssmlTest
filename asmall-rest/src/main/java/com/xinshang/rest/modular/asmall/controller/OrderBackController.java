package com.xinshang.rest.modular.asmall.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xinshang.rest.common.util.JwtTokenUtil;
import com.xinshang.rest.common.util.R;
import com.xinshang.rest.modular.asmall.dto.OrderBackDTO;
import com.xinshang.rest.modular.asmall.dto.OrderBackDTO;
import com.xinshang.rest.modular.asmall.dto.OrderListDTO;
import com.xinshang.rest.modular.asmall.model.Member;
import com.xinshang.rest.modular.asmall.model.OrderBack;
import com.xinshang.rest.modular.asmall.service.IOrderBackService;
import com.xinshang.rest.modular.asmall.service.IOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 订单信息
 * @author daijunye
 */
@RestController
@RequestMapping("/orderback")
@Api(value = "退货订单信息",tags = "退货订单相关接口")
@AllArgsConstructor
@Slf4j
public class OrderBackController {

    @Autowired
    private IOrderBackService orderService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @ApiOperation(value = "创建订单", notes = "创建订单", nickname = "liukx")
    @PostMapping("/addOrderBack")
    public R createOrder(@RequestBody OrderBackDTO orderbackDTO,HttpServletRequest request) {
        OrderBack orderback1=orderService.selectOne(new EntityWrapper<OrderBack>().in("state","1").eq("order_id", orderbackDTO.getOrderId()).orderBy("id",false));
        if(orderback1!=null)
        {
            return R.failed("请勿重复提交");
        }
        Member member = jwtTokenUtil.getMemberFromRequest(request);
        if (member == null) {
            return R.failed("用户不存在");
        }
        orderbackDTO.setUserName(member.getId().toString());
        R r = orderService.createOrder(orderbackDTO);
        return r;
    }

    @ApiOperation(value = "退货订单", notes = "退货订单", nickname = "liukx")
    @PostMapping("/orderBackOrderId")
    public R orderBackOrderId(@RequestBody OrderBackDTO orderbackDTO,HttpServletRequest request) {
        R r = orderService.orderBackOrderId(orderbackDTO);
        return r;
    }


    @ApiOperation(value = "获得当前用户订单", notes = "获得当前用户订单", nickname = "liukx")
    @PostMapping("/orderBackList")
    public R orderBackList(@RequestBody OrderListDTO listDTO, HttpServletRequest request) {
        log.info("获得当前用户订单参数:{}",listDTO);
        Member member = jwtTokenUtil.getMemberFromRequest(request);
        if (member == null) {
            return R.failed("用户不存在");
        }
        listDTO.setUserId(member.getId().toString());
        R r = orderService.orderList(listDTO);
        return r;
    }

    @ApiOperation(value = "通过id获取订单", notes = "通过id获取订单", nickname = "liukx")
    @PostMapping("/orderBackDetail")
    public R orderDetail(@RequestBody OrderBackDTO orderbackDTO,HttpServletRequest request) {
        OrderBack order = orderService.selectById(orderbackDTO.getOrderId());
        return R.ok(order);
    }

    @ApiOperation(value = "客户取消订单", notes = "客户取消订单", nickname = "liukx")
    @PostMapping("/cancelOrderBack")
    public R cancelOrder(@RequestBody OrderBackDTO orderbackDTO,HttpServletRequest request) {
        OrderBack order = orderService.selectById(orderbackDTO.getOrderId());
        order.setState(6);
        boolean b = orderService.updateById(order);
        if (b) {
            return R.ok();
        }
        return R.failed("取消失败");
    }



}
