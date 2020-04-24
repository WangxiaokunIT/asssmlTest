package com.xinshang.modular.biz.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.allinpay.yunst.sdk.YunClient;
import com.allinpay.yunst.sdk.bean.YunRequest;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.SqlHelper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xinshang.config.properties.SystemProperties;
import com.xinshang.core.common.exception.BizExceptionEnum;
import com.xinshang.core.enums.BizTypeEnum;
import com.xinshang.core.enums.UserTypeEnum;
import com.xinshang.core.exception.SystemException;
import com.xinshang.core.util.NoUtil;
import com.xinshang.core.util.eqb.AllinPayUtil1;
import com.xinshang.modular.biz.dao.*;
import com.xinshang.modular.biz.dto.AllinPayResponseDTO1;
import com.xinshang.modular.biz.dto.OrderRefundPayRespDTO;
import com.xinshang.modular.biz.model.*;
import com.xinshang.modular.biz.service.*;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xinshang.modular.biz.utils.MoneyFormatTester;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 订单退货记录表 服务实现类
 * </p>
 *
 * @author daijunye
 * @since 2019-10-23
 */
@Service
@Slf4j
public class OrderBackServiceImpl extends ServiceImpl<OrderBackMapper, OrderBack> implements IOrderBackService {
    @Autowired
    private IItemSpecsService iItemSpecsService;
    @Autowired
    private SystemProperties systemProperties;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private OrderBackMapper orderBackMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private IItemService iItemService;
    @Override
    public String refundConfirm(OrderBack orderDTO) {
        OrderBack orderBack=orderBackMapper.selectById(orderDTO);
        Order order = iOrderService.selectOne(new EntityWrapper<Order>().eq("order_id", orderBack.getOrderId()));
        Member member = memberMapper.selectById(order.getUserId());
        String payNum = order.getPayNum();

        //同步到通联
        YunRequest request = new YunRequest("OrderService", "refund");
        String code = NoUtil.generateCode(BizTypeEnum.COST_NUMBER, UserTypeEnum.CUSTOMER, member.getId());
        //商户订单号（支付订单）
        request.put("bizOrderNo", code);
        //商户原订单号 需要退款的原交易订单号
        request.put("oriBizOrderNo", payNum);

        //商户系统用户标识，商户 系统中唯一编号。 退款收款人。
        request.put("bizUserId", member.getBizUserId());

        //退款金额 单位：分
        DecimalFormat form = new DecimalFormat("#.00");//保留两位小数
        BigDecimal amountzuizhong= order.getPayAmount().subtract(order.getFreight());
        Long amount = MoneyFormatTester.bigDecimal2Long(amountzuizhong);
        Long amountall = MoneyFormatTester.bigDecimal2Long(order.getPayAmount());

        if(!orderBack.getBackRemark().equals("未发货取消购买")) {
//            if (amountzuizhong.compareTo( order.getPayAmount())!=0) {
//                request.put("refundType", "D1");
//            } else {
//                request.put("refundType", "D0");
//            }
            request.put("amount", amount);
        }
        else
        {
            Long amountnoyunfei=MoneyFormatTester.bigDecimal2Long(order.getPayAmount());
            //request.put("refundType", "D0");
            request.put("amount", amountall);
        }
        //request.put("amount", amount);
        request.put("extendInfo", "退款");
        request.put("backUrl", systemProperties.getProjectUrl() + "/allinPayAsynRespNotice/refund");
        Optional<AllinPayResponseDTO1<OrderRefundPayRespDTO>> response = AllinPayUtil1.request(request, OrderRefundPayRespDTO.class);
        //通联同步失败一定要抛异常回滚数据库
        Assert.isTrue(CommonConstants.SUCCESS_CODE.equals(response.get().getStatus()), () -> {
            log.warn("用户（买方）退款申请接口调用失败:{}", response);
            if(response.get().getMessage().equalsIgnoreCase("已退款金额大于原订单支付金额。"))
            {
                order.setRefundNum(code);
                iOrderService.updateById(order);
                return code;
            }
            else {
                return response.get().getMessage();
            }
        });
        String payStatus = response.get().getSignedValue().getPayStatus();
        if (payStatus.equalsIgnoreCase("pending")) {
            log.warn("用户（买方）退款申请接口调用成功返回进行中:{}", response);
            //return "退款处理中";
        }
        if (payStatus.equalsIgnoreCase("fail")) {
            log.warn("用户（买方）退款申请接口调用成功返回失败:{}", response);
            return response.get().getSignedValue().getPayFailMessage();
        }
        order.setRefundNum(code);
        iOrderService.updateById(order);
        return code;
    }
    /**
     * 订单加库存操作
     *
     * @param orderId
     * @return
     */
    public void addOpt(String orderId) {
        log.info(orderId + "订单加库存操作");
        //订单支付成功后减库存
        EntityWrapper<OrderItem> orderItemWrapper = new EntityWrapper<>();
        orderItemWrapper.eq("order_id", orderId);
        List<OrderItem> orderItems = orderItemMapper.selectList(orderItemWrapper);

        //总结：try放在for循环的里面所有的for循环都会执行，当遇到异常时，抛出异常继续执行；放在外面，当遇到异常时，抛出异常，后面的循环就会终止，并不会执行。
        try {
            for (OrderItem orderItem : orderItems) {
                //购买数量
                Integer salesVolume = orderItem.getNum();
                Item item = iItemService.selectById(orderItem.getItemId());

                if (StrUtil.isEmpty(orderItem.getItemSpecsNo())) {
                    //加库存
                    item.setNum(item.getNum() + salesVolume);
                } else {
                    //有规格
                    ItemSpecs itemSpecs = iItemSpecsService.selectOne(new EntityWrapper<ItemSpecs>().eq("specs_no", orderItem.getItemSpecsNo()));
                    //加库存
                    itemSpecs.setStock(itemSpecs.getStock() + salesVolume);
                    iItemSpecsService.updateById(itemSpecs);
                }
                iItemService.updateById(item);

            }
            log.info(orderId + "订单加库存操作完成");
        } catch (Exception e) {
            log.info(orderId + "订单加库存操作出现异常");
            e.printStackTrace();
            //throw new SystemException("库存操作失败");
        }
    }


}
