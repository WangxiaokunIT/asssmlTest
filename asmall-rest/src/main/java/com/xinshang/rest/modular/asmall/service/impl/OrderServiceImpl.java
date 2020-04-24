package com.xinshang.rest.modular.asmall.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.allinpay.yunst.sdk.YunClient;
import com.allinpay.yunst.sdk.bean.YunRequest;
import com.allinpay.yunst.sdk.util.RSAUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.xinshang.core.enums.BizTypeEnum;
import com.xinshang.core.enums.UserTypeEnum;
import com.xinshang.core.exception.SystemException;
import com.xinshang.core.util.NoUtil;
import com.xinshang.rest.common.activemq.ScheduleMessagePostProcessor;
import com.xinshang.rest.common.enums.*;
import com.xinshang.rest.common.util.AllinPayUtil;
import com.xinshang.rest.common.util.R;
import com.xinshang.rest.common.util.RedisUtil;
import com.xinshang.rest.config.constant.Constants;
import com.xinshang.rest.config.properties.AllinPayCodeProperties;
import com.xinshang.rest.config.properties.AllinPayProperties;
import com.xinshang.rest.config.properties.RestProperties;
import com.xinshang.rest.modular.asmall.dao.*;
import com.xinshang.rest.modular.asmall.dto.*;
import com.xinshang.rest.modular.asmall.model.*;
import com.xinshang.rest.modular.asmall.service.*;
import com.xinshang.rest.modular.asmall.util.AllinPayUtil1;
import com.xinshang.rest.modular.asmall.util.MoneyFormatTester;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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
    private IItemSpecsService iItemSpecsService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private OrderShippingMapper orderShippingMapper;

    @Autowired
    private OrderStateRecordMapper orderStateRecordMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private IPostMsgService iPostMsgService;

    @Autowired
    private AllinPayCodeProperties allinPayCodeProperties;

    @Autowired
    private BankMapper bankMapper;

    @Autowired
    private IItemService iItemService;

    @Autowired
    private AllinPayProperties allinPayProperties;

    @Autowired
    private ClientLogMoneysMapper clientLogMoneysMapper;

    @Autowired
    private RestProperties restProperties;

    @Autowired
    private  IIntegralLogMoneysService iIntegralLogMoneysService;

    @Autowired
    private ISpecsAttributeService iSpecsAttributeService;


    private static ReentrantReadWriteLock payRwl = new ReentrantReadWriteLock();

    private static ReentrantReadWriteLock createOrderRwl = new ReentrantReadWriteLock();

    private static String CART_PRE = "CART";


    /**
     * 功能描述: 支付确认 （后台+短信验证码）
     *
     * @Param: [orderDTO]
     * @Return: com.xinshang.rest.common.util.R
     * @Auther: wangxiaokun
     * @Date: 2019/11/18 11:47
     * @Description:
     * @Modify:
     */
    @Override
    public R payConfirm(OrderDTO orderDTO) {

        Member member = memberMapper.selectById(orderDTO.getUserId());
        YunRequest request = new YunRequest("OrderService", "pay");
        request.put("bizUserId", member.getBizUserId());
        request.put("bizOrderNo", orderDTO.getBizOrderNo());

//        request.put("tradeNo", orderDTO.getTradeNo());
        //短信验证码
        request.put("verificationCode", orderDTO.getVerificationCode());
        //用户公网 IP 用于分控校验 注：不能使用“127.0.0.1” “localhost”
        request.put("consumerIp", Constants.CUSTOMER_IP);

        Optional<AllinPayResponseDTO<OrderConsumeRespDTO>> response = AllinPayUtil.request(request, OrderConsumeRespDTO.class);
        //通联同步失败一定要抛异常回滚数据库
        if (!CommonConstants.SUCCESS_CODE.equals(response.get().getStatus())) {
            log.warn("普通消费申请 支付确认失败:{}", response);
            return R.failed(400,response.get().getMessage());
        }

        String payStatus = response.get().getSignedValue().getPayStatus();
        String orderId = orderDTO.getOrderId();
        if (payStatus.equalsIgnoreCase("fail")) {
            Integer type = orderDTO.getConsumeType();
            if (type == 0) {
                //todo 0为消费申请 确认接口  直接关闭订单
                Order order = orderMapper.selectById(orderId);
                order.setStatus(4);
                orderMapper.updateById(order);
            }
            return R.failed(response.get().getSignedValue().getPayFailMessage());
        }
        if (payStatus.equalsIgnoreCase("success")) {
            Order order = orderMapper.selectById(orderId);
            //判断订单状态及减库存操作
            R r = reduceOrder(order, orderDTO);
            if (r.getCode() == 500) {
                return r;
            }
        }
        return R.ok(response.get().getMessage());
    }

    /**
     * 执行判断订单状态和减库存等操作
     * @param order
     * @param orderDTO
     * @return
     */
    @Override
    public R reduceOrder (Order order,OrderDTO orderDTO) {
        log.info("执行判断订单状态和减库存等操作:{},{}",order,orderDTO);
        try {
            payRwl.readLock().lock();
            //不是待付款状态
            if (!order.getStatus().equals(OrderStatusEnum.待付款.getValue())) {
                log.info("不是待付款状态");
                return R.failed("不是待付款状态");
            }
            BigDecimal orderAmount = order.getPayAmount().multiply(new BigDecimal(100));

            BigDecimal payAmount = new BigDecimal(orderDTO.getPayMent());

            log.info("订单金额:支付金额{}:{}",orderAmount,payAmount);
            if (orderAmount.compareTo(payAmount) !=0) {
                log.info("支付金额不一致");
                return R.failed("支付金额不一致");
            }
        } finally {
            //释放读锁
            payRwl.readLock().unlock();
        }
        try {
            log.info("判断订单状态成功");
            payRwl.writeLock().lock();
            //减库存
            stockOpt(order.getOrderId());
            //修改订单状态
            order.setPaymentTime(new Date());
//            order.setPaymentType(orderDTO.getPayType());
            order.setPayment(order.getPayAmount());
            order.setStatus(OrderStatusEnum.待发货.getValue());
            orderMapper.updateById(order);
            ClientLogMoneys moneys = new ClientLogMoneys();
            moneys.setCreateTime(new Date());
            moneys.setMoney(order.getPayAmount());
            moneys.setTradeNo(orderDTO.getTradeNo());
            moneys.setDataSrc(2);
            moneys.setDeleteFlg(1);
            //1：收入 2：支出
            moneys.setMoneyType(2);
            moneys.setRemark("商品消费");
            moneys.setClientId(order.getUserId());
            moneys.setUserName(order.getBuyerNick());
            clientLogMoneysMapper.insert(moneys);
            //新增订单状态
            OrderStateRecord tsr = new OrderStateRecord();
            //操作人
            tsr.setOperatorUser(order.getBuyerNick());
            //是否当前状态
            tsr.setCurrent(true);
            //状态
            tsr.setState(OrderStatusEnum.待发货.getValue());
            //订单id
            tsr.setOrderId(order.getOrderId());
            try{
                log.info("开始执行新增订单状态记录信息");
                orderStateRecordMapper.insert(tsr);
            }catch (Exception e){
                log.info("执行新增订单状态记录信息出现异常");
                e.printStackTrace();
                return R.failed("执行新增订单状态记录信息出现异常");
            }
        } finally {
            payRwl.writeLock().unlock();
        }
        return R.ok();
    }

    @Override
    public BigDecimal getTotleFreight(CreateOrderQueryDTO createOrderQueryDTO) {
        List<OrderBuyGoodsDTO> goodsList = createOrderQueryDTO.getGoodsList();
        BigDecimal totleFreight = new BigDecimal(0);
        for (OrderBuyGoodsDTO orderBuyGoodsDTO : goodsList) {
            log.info("计算运费");
            ItemAndSpecsDTO itemAndSpecsDTO = iItemService.selectByItemNumberOrSpecsNo(orderBuyGoodsDTO.getItemNumber(), orderBuyGoodsDTO.getItemSpecsNo());
            String address = createOrderQueryDTO.getAddress();
            String province = address.split("-")[0];
            BigDecimal freight = orderMapper.getFreight(itemAndSpecsDTO.getItemNumber(), province);
            totleFreight = totleFreight.add(freight);
        }
        return totleFreight;
    }

    /**
     * 功能描述: 退款
     *
     * @Param: [orderDTO]
     * @Return: com.xinshang.rest.common.util.R
     * @Auther: wangxiaokun
     * @Date: 2019/11/18 11:08
     * @Description:
     * @Modify:
     */
    @Override
    public R payRefund(OrderDTO orderDTO) {
        Order order = orderMapper.selectById(orderDTO.getOrderId());
        Member member = memberMapper.selectById(orderDTO.getUserId());
        String payNum = order.getPayNum();
        //同步到通联
        YunRequest request = new YunRequest("OrderService", "refund");

        //商户订单号（支付订单）
        String code = NoUtil.generateCode(BizTypeEnum.COST_NUMBER, UserTypeEnum.CUSTOMER, member.getId());
        request.put("bizOrderNo", code);
        //商户原订单号 需要退款的原交易订单号
        request.put("oriBizOrderNo", payNum);

        //商户系统用户标识，商户 系统中唯一编号。 退款收款人。
        request.put("bizUserId", member.getBizUserId());

        //退款金额 单位：分
        Long amount = MoneyFormatTester.bigDecimal2Long(order.getPayAmount().subtract(order.getFreight()));
        request.put("amount", amount);

        Optional<AllinPayResponseDTO1<OrderRefundPayRespDTO>> response = AllinPayUtil1.request(request, OrderRefundPayRespDTO.class);
        //通联同步失败一定要抛异常回滚数据库
        Assert.isTrue(CommonConstants.SUCCESS_CODE.equals(response.get().getStatus()), () -> {
            log.warn("用户（买方）退款申请接口调用失败:{}", response);
            return response.get().getMessage();
        });
        /**
         * 增加库存
         */
        addOpt(order.getOrderId());
        ClientLogMoneys moneys = new ClientLogMoneys();
        moneys.setCreateTime(new Date());
        moneys.setMoney(order.getPayAmount());
        moneys.setTradeNo(orderDTO.getTradeNo());
        moneys.setDataSrc(4);
        moneys.setDeleteFlg(1);
        //1：收入 2：支出
        moneys.setMoneyType(1);
        moneys.setRemark("商品退款");
        moneys.setClientId(order.getUserId());
        moneys.setUserName(order.getBuyerNick());
        clientLogMoneysMapper.insert(moneys);

        return R.ok("退款申请成功");
    }

    /**
     * 功能描述: 商品支付
     *
     * @Param: [orderDTO]
     * @Return: com.xinshang.rest.common.util.R
     * @Auther: wangxiaokun
     * @Date: 2019/11/18 11:47
     * @Description:
     * @Modify:
     */
    @Override
    public R payGoods(OrderDTO orderDTO) {
        Order order = orderMapper.selectById(orderDTO.getOrderId());
        Member member = memberMapper.selectById(orderDTO.getUserId());
        //加判断
        synchronized (this) {
            if (order == null) {
                return R.failed("该订单不存在");
            }
            if (!order.getStatus().equals(OrderStatusEnum.待付款.getValue())) {
                return R.failed("该订单不是待付款状态");
            }
            //同步验证
            log.info("订单检查完成");
        }

        //同步到通联
        YunRequest request = new YunRequest("OrderService", "consumeApply");
        Map<String, Object> param = request.getParam();
        request.put("payerId", member.getBizUserId());
        request.put("recieverId", "#yunBizUserId_B2C#");
        //商户订单号（支付订单）
        String code = NoUtil.generateCode(BizTypeEnum.COST_NUMBER, UserTypeEnum.CUSTOMER, member.getId());
        //生成支付num
        order.setPayNum(code);
        request.put("bizOrderNo", code);
        request.put("extendInfo", "商品消费");
        //金额 单位：分
        Long amount = MoneyFormatTester.bigDecimal2Long(order.getPayAmount());
        request.put("amount", amount);
        //内扣，如果不存在，则填 0。
        request.put("fee", 0);


        //后台通知地址
        request.put("backUrl", restProperties.getProjectUrl()+"/allinPayAsynRespNotice/payCallback");

        JSONObject payMethod = new JSONObject();
        JSONObject subPayMethod = new JSONObject();
        int payType = orderDTO.getPayType();
        if (payType == 1) {
            //前台通知 网关支付必传参数frontUrl
            request.put("frontUrl", orderDTO.getJumpUrl());
            subPayMethod.put("amount", amount);
            subPayMethod.put("paytype", "B2C");
            payMethod.put("GATEWAY_VSP", subPayMethod);
            request.put("validateType", 1);

        } else if (payType == 2) {
            String encrypt;
            try {
                Bank bank = bankMapper.selectById(orderDTO.getBankId());
                encrypt = RSAUtil.encrypt(bank.getBankCardNo());
                subPayMethod.put("bankCardNo", encrypt);
                subPayMethod.put("amount", amount);
                payMethod.put("QUICKPAY_VSP", subPayMethod);
                request.put("validateType", 1);
            } catch (Exception e) {
                e.printStackTrace();
                throw new SystemException("银行卡加密失败");
            }
        } else if (payType == 3) {
            //微信支付
            subPayMethod.put("limitPay", "");
            subPayMethod.put("subAppid", allinPayProperties.getSubAppId());
            subPayMethod.put("acct", orderDTO.getOpenId());
            subPayMethod.put("amount", amount);
            payMethod.put("WECHAT_PUBLIC", subPayMethod);
            request.put("validateType", 1);
        } else if(payType == 4){
            //账户余额
            JSONArray balanceArray = new JSONArray();
            subPayMethod.put("amount", amount);
            subPayMethod.put("accountSetNo",allinPayProperties.getAccountSetNo());
            balanceArray.add(subPayMethod);
            payMethod.put("BALANCE",balanceArray);
            request.put("validateType", 2);
        }

        request.put("payMethod", payMethod);
        //行业代码
        request.put("industryCode", allinPayCodeProperties.getOtherCode());
        //行业名称
        request.put("industryName", allinPayCodeProperties.getOtherName());
        //终端访问类型 访问终端类型:[1:Mobile,2:PC]
        request.put("source", 1);

        Optional<AllinPayResponseDTO<OrderConsumeRespDTO>> response = AllinPayUtil.request(request, OrderConsumeRespDTO.class);
        //通联同步失败一定要抛异常回滚数据库
        if (!CommonConstants.SUCCESS_CODE.equals(response.get().getStatus())) {
            log.warn("用户（买方）消费申请接口调用失败:{}", response);
            return R.failed(400,response.get().getMessage());
        }
        //网关支付  前台+短信验证码  快捷支付 后台+短信验证码  微信 无确认方式
        Map<String, String> map = new HashMap<>(3);
        if (payType == 1 || payType == 4) {
            String url = this.openConfirmPaymentPage(member.getBizUserId(), code, orderDTO.getJumpUrl());
            map.put("url", url);

        } else if (payType == 2) {
            String tradeNo = NoUtil.generateCode(BizTypeEnum.COST_NUMBER, UserTypeEnum.CUSTOMER, member.getId());
            map.put("bizOrderNo", code);
            map.put("tradeNo", tradeNo);
            map.put("payMent", amount.toString());
            map.put("payType", payType + "");
        }
        order.setPaymentType(payType);
        orderMapper.updateById(order);
        //新增订单状态
        OrderStateRecord tsr = new OrderStateRecord();
        //操作人
        tsr.setOperatorUser(order.getBuyerNick());
        //是否当前状态
        tsr.setCurrent(true);
        //状态
        tsr.setState(OrderStatusEnum.待付款.getValue());
        //订单id
        tsr.setOrderId(order.getOrderId());
        orderStateRecordMapper.insert(tsr);
        return R.ok(map);
    }

    public Order checkOrderById(String orderId, String memberPhone) {
        if (StrUtil.isBlank(orderId)) {
            log.warn("订单号不能为空");
            return null;
        }
        //订单不存在
        Order tbOrder = orderMapper.selectById(orderId);
        if (tbOrder == null) {
            log.warn("订单不存在");
            return null;
        }

        if (StrUtil.isNotEmpty(memberPhone) && !tbOrder.getBuyerNick().equals(memberPhone)) {
            return null;
        }
        return tbOrder;
    }


    /**
     * 跳转支付确认页面
     * @param bizUserId
     * @param bizOrderNo
     * @return
     */
    @SneakyThrows
    public String openConfirmPaymentPage(String bizUserId, String bizOrderNo, String jumpUrl) {
        final YunRequest request = new YunRequest("OrderService", "pay");
        request.put("bizUserId", bizUserId);
        request.put("bizOrderNo", bizOrderNo);
        request.put("jumpUrl", jumpUrl);
        request.put("consumerIp", "58.56.184.202");
        String setRes = YunClient.encodeOnce(request);
        return allinPayProperties.getPayConfirmPasswordUrl() + "?" + setRes;
    }

    /**
     * 功能描述: H5端 创建订单
     *
     * @Param: [orderDTO]
     * @Return: boolean
     * @Auther: wangxiaokun
     * @Date: 2019/10/23 17:29
     * @Description:
     * @Modify:
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R createOrder(CreateOrderQueryDTO createOrderQueryDTO) {
        log.info("开始创建订单:{}", createOrderQueryDTO);

        /**
         * 逻辑1：是否能获取下单用户信息
         * 逻辑2：判断未支付的订单是否超限
         * 逻辑3：判断购买提交商品   情况：购买产品不能为空/您选择的产品不存在或者已下架/
         * 逻辑4：判断收货信息   情况：收货人不能为空/收货地址不能为空/收货人手机号不能为空
         * 逻辑5：判断商品状态是否可以购买   情况：产品不存在/库存不足,请重新选择/是否超过购买限制
         *
         */
        log.info("查询当前登录用户");
        Member member = memberMapper.selectById(Long.valueOf(createOrderQueryDTO.getMemberId()));
        if (member == null) {
            return R.failed("获取下单用户失败");
        }
        log.info("开始执行必要条件检查");
        log.info("检查收货信息");
        //判断收货人收货信息
        if (StrUtil.isBlank(createOrderQueryDTO.getConsignee())) {
            return R.failed("收货人不能为空");
        }
        if (StrUtil.isBlank(createOrderQueryDTO.getAddress())) {
            return R.failed("收货地址不能为空");
        }
        if (StrUtil.isBlank(createOrderQueryDTO.getPhone())) {
            return R.failed("收货人手机号不能为空");
        }

        List<OrderBuyGoodsDTO> goodsList = createOrderQueryDTO.getGoodsList();
        log.info("判断是否选了产品");
        if (goodsList == null || goodsList.size() == 0) {
            return R.failed("购买产品不能为空");
        }
        try {
            createOrderRwl.readLock().lock();

            //判断未支付的订单数量是否超限
            Integer status = orderMapper.selectCount(new EntityWrapper<Order>().eq("status", 1).eq("buyer_nick",member.getPhone()));
            if (status > 2) {
                return R.failed("未支付的订单过多");
            }

            BigDecimal totleFreight = new BigDecimal(0);
            //产品
            List<ItemAndSpecsDTO> ItemAndSpecsDTOs = new ArrayList<>();
            log.info("开始遍历商品");
            for (OrderBuyGoodsDTO orderBuyGoodsDTO : goodsList) {
                log.info("判断是否选了规格");
                if (StrUtil.isEmpty(orderBuyGoodsDTO.getItemSpecsNo())) {
                    log.info("没有规格");
                    orderBuyGoodsDTO.setItemSpecsNo("nothingness");
                }
                log.info("查询该产品信息");
                ItemAndSpecsDTO itemAndSpecsDTO = iItemService.selectByItemNumberOrSpecsNo(orderBuyGoodsDTO.getItemNumber(), orderBuyGoodsDTO.getItemSpecsNo());
                log.info("判断产品是否正常");
                if (itemAndSpecsDTO == null) {
                    log.info("返回:产品不存在或者已下架");
                    return R.failed("产品不存在或者已下架");
                }

                log.info("判断该产品是否有规格");
                if (itemAndSpecsDTO.getItemSpecsList().size() > 0) {
                    log.info("有规格");
                    ItemSpecs itemSpecs = itemAndSpecsDTO.getItemSpecsList().get(0);
                    log.info("判断库存是否足够");
                    if (itemSpecs.getStock() < orderBuyGoodsDTO.getProductNum()) {
                        log.info("返回:产品库存不足");
                        return R.failed("产品库存不足");
                    }

                    log.info("设置vip价格");
                    itemAndSpecsDTO.setVipDiscount(itemSpecs.getVipDiscount());
                    log.info("设置价格");
                    itemAndSpecsDTO.setPrice(itemSpecs.getPrice());
                    log.info("利润");
                    itemAndSpecsDTO.setProfits(itemSpecs.getProfits());
                    log.info("商品图片");
                    itemAndSpecsDTO.setImage(itemSpecs.getImage().split(",")[0]);
                }
                log.info("判断是否超过每次购买限制");
                if (itemAndSpecsDTO.getLimitNum() < orderBuyGoodsDTO.getProductNum()) {
                    log.info("返回:超过每次购买数量限制");
                    return R.failed("超过每次购买数量限制");
                }
                log.info("设置购买数量");
                itemAndSpecsDTO.setBuyNum(orderBuyGoodsDTO.getProductNum());
                log.info("计算运费");
                String address = createOrderQueryDTO.getAddress();
                String province = address.split("-")[0];
                BigDecimal freight = orderMapper.getFreight(itemAndSpecsDTO.getItemNumber(), province);
                totleFreight = totleFreight.add(freight);
                ItemAndSpecsDTOs.add(itemAndSpecsDTO);
            }
            log.info("设置总运费");
            createOrderQueryDTO.setTotleFreight(totleFreight);
            log.info("设置商品信息");
            createOrderQueryDTO.setItems(ItemAndSpecsDTOs);
        } finally {
            createOrderRwl.readLock().unlock();
        }

        R r;

        try {
            createOrderRwl.writeLock().lock();
            log.info("开始调用插入订单方法:{},{}", createOrderQueryDTO, member);
            r = insertTbOrder(createOrderQueryDTO, member);
            if (r.getCode() != 200) {
                return R.failed(r.getMsg());
            }
        } catch (Exception e) {
            log.warn("插入订单信息出现异常");
            e.printStackTrace();
            throw new SystemException(e.getMessage());
        } finally {
            createOrderRwl.writeLock().unlock();
        }
        log.warn("插入订单信息完成");

        return r;
    }

    /**
     * 功能描述: 订单列表
     *
     * @Param: [orderDTO]
     * @Return: com.xinshang.rest.common.util.R
     * @Auther: wangxiaokun
     * @Date: 2019/11/18 11:48
     * @Description:
     * @Modify:
     */
    @Override
    public R orderList(OrderListDTO listDTO) {

        Page<Order> page = listDTO.defaultPage();
        EntityWrapper<Order> orderWrapper = new EntityWrapper<>();

        Integer status = listDTO.getStatus();
        if (status != 0) {
            orderWrapper.eq("status", status);
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
        return R.ok(page.setRecords(orders));
    }

    /**
     * 功能描述: 插入订单对象
     *
     * @Param: [orderDTO, member]
     * @Return: com.xinshang.rest.common.util.R
     * @Auther: wangxiaokun
     * @Date: 2019/10/24 11:17
     * @Description:
     * @Modify:
     */
    private R insertTbOrder(CreateOrderQueryDTO createOrderQueryDTO, Member member) {
        log.info("开始插入订单对象:{},{}", createOrderQueryDTO, member);
        log.info("设置订单信息");
        Order order = new Order();
        //生成订单信息
        String orderId = "";
        //订单用户
        order.setUserId(member.getId());
        //用户昵称
        order.setBuyerNick(member.getUsername());
        //运费
        order.setFreight(createOrderQueryDTO.getTotleFreight());
        //订单类型
        Integer customType = createOrderQueryDTO.getCustomType();
        //订单类型
        order.setCustomType(customType);
        //订单状态记录
        OrderStateRecord record = new OrderStateRecord();
        //普通订单逻辑
        if (customType == 0) {
            log.info("执行普通订单逻辑");
            orderId = NoUtil.generateCode(BizTypeEnum.GENERAL_ORDER, UserTypeEnum.CUSTOMER, member.getId());
            log.info("生成的订单号:" + orderId);
            order.setOrderId(orderId);
            order.setStatus(OrderStatusEnum.待付款.getValue());
            //状态状态改为待付款
            record.setState(OrderStatusEnum.待付款.getValue());
        //兑换订单逻辑
        } else if (customType == 3) {
            log.info("执行兑换订单逻辑");
            orderId = NoUtil.generateCode(BizTypeEnum.INTEGRAL_ORDER, UserTypeEnum.CUSTOMER, member.getId());
            log.info("生成的订单号:" + orderId);
            order.setOrderId(orderId);
            //如果是兑换订单 则判断是否积分足够
            BigDecimal point = BigDecimal.valueOf(member.getPoints());
            //商品积分
            BigDecimal pointBean = new BigDecimal(createOrderQueryDTO.getItems().get(0).getBuyNum()).multiply(createOrderQueryDTO.getItems().get(0).getPrice());
            log.info("判断积分余额是否够本次支付,本次交易积分:{}", pointBean);

            if (point.compareTo(pointBean) == -1) {
                log.info("返回:积分余额不足");
                return R.failed("积分余额不足");
            }
            order.setPaymentTime(new Date());
            order.setPayment(pointBean);
            //积分支付
            order.setPaymentType(4);
            String code = NoUtil.generateCode(BizTypeEnum.COST_NUMBER, UserTypeEnum.CUSTOMER, member.getId());
            order.setPayNum(code);
            //剩余积分
            BigDecimal remaining = point.subtract(pointBean);
            member.setPoints(remaining.intValue());

            log.info("更新用户积分余额");
            memberMapper.updateById(member);

            order.setStatus(OrderStatusEnum.待发货.getValue());
            //状态状态改为待发货
            record.setState(OrderStatusEnum.待发货.getValue());
            log.info("更新用户积分流水");
            IntegralLogMoneys integralLogMoneys=new IntegralLogMoneys();
            //添加积分信息
            integralLogMoneys.setClientId(member.getId().intValue());
            integralLogMoneys.setUserName(member.getUsername());
            integralLogMoneys.setIntegral(pointBean.intValue());
            integralLogMoneys.setCreateTime(new Date());
            integralLogMoneys.setDataSrc(2);
            integralLogMoneys.setDeleteFlg(1);
            integralLogMoneys.setMoneyType(2);
            integralLogMoneys.setRemark("积分消费");
            iIntegralLogMoneysService.insert(integralLogMoneys);

            log.info("记录积分流水");


        }
        log.info("保存订单商品");
        //保存订单商品
        R r = insertOrderItem(createOrderQueryDTO, orderId,member.getVip());
        //判断错误直接返回
        if (r.getCode() != 200) {
            return r;
        }
        //购买/兑换备注
        order.setBuyerMessage(createOrderQueryDTO.getBuyerMessage());
        //计算订单总额
        Map<String, BigDecimal> orderItemSaveResult = (Map<String, BigDecimal>) r.getData();
        BigDecimal payAmount = orderItemSaveResult.get("payAmount");
        //计算订单利润
        BigDecimal profitsGoods = orderItemSaveResult.get("profitsGoods");
        //商品总利润
        order.setProfitsGoods(profitsGoods);
        //订单金额 = 商品金额+运费
        order.setPayAmount(payAmount.add(order.getFreight()));

        //订单保存到数据库中

        log.error("插入订单信息,{}", order);
        orderMapper.insert(order);
        //发送超时消息到队列中，订单30分钟失效
        log.info(orderId + "发送延时" + OrderConstant.ORDINARY_ORDER_CANCEL_TIMEOUT + "毫秒消息到队列中，未支付服务订单超时取消");
        iPostMsgService.sendMessage(MqMessageConstant.ORDER_NO_PAY_TIMEOUT_MESSAGE_PREFIX + orderId,
                new ScheduleMessagePostProcessor(OrderConstant.ORDINARY_ORDER_CANCEL_TIMEOUT));

        //如果是兑换订单则减库存操作
        if (customType == 3) {
            log.info("开始扣减库存");
            //扣减库存
            stockOpt(orderId);
        }

        record.setOrderId(orderId);
        record.setOperatorUser(member.getUsername());
        record.setCurrent(true);

        log.info("生成订单状态记录");
        orderStateRecordMapper.insert(record);


        //物流表
        OrderShipping orderShipping = new OrderShipping();
        orderShipping.setOrderId(orderId);
        orderShipping.setReceiverName(createOrderQueryDTO.getConsignee());
        orderShipping.setReceiverAddress(createOrderQueryDTO.getAddress());
        orderShipping.setReceiverPhone(createOrderQueryDTO.getPhone());
        orderShipping.setCreated(new Date());

        log.info("添加订单收货地址");
        orderShippingMapper.insert(orderShipping);

        log.info("结束插入订单对象");
        return R.ok(order);
    }


    /**
     * 功能描述: 保存订单商品
     *
     * @Param: [orderDTO, orderId]
     * @Return: com.xinshang.rest.common.util.R
     * @Auther: wangxiaokun
     * @Date: 2019/10/24 10:27
     * @Description:
     * @Modify:
     */
    private R insertOrderItem(CreateOrderQueryDTO createOrderQueryDTO, String orderId,Integer isVip) {
        log.info("开始保存订单商品:{}", createOrderQueryDTO);
        Map<String, BigDecimal> resultData = new HashMap(3);
        //保存订单总额
        BigDecimal payAmount = new BigDecimal("0");
        //保存订单总利润
        BigDecimal profitsGoods = new BigDecimal("0");

        log.info("生成订单商品");
        for (ItemAndSpecsDTO item : createOrderQueryDTO.getItems()) {
            //购买数量
            BigDecimal buyNum = BigDecimal.valueOf(item.getBuyNum());
            //本商品单价
            BigDecimal price = item.getPrice();
            if (isVip == 1) {
                log.info("vip购买");
                price = item.getVipDiscount();
            }


            //本商品利润
            BigDecimal profits = item.getProfits();

            OrderItem orderItem = new OrderItem();
            //订单id
            orderItem.setOrderId(orderId);
            //商品id
            orderItem.setItemId(item.getId());
            //商品编号
            orderItem.setItemNumber(item.getItemNumber());
            String specsValues;
            if (item.getItemSpecsList().size() > 0) {
                specsValues ="";
                //规格编号
                orderItem.setItemSpecsNo(item.getItemSpecsList().get(0).getSpecsNo());
                //规格
                String[] split = item.getItemSpecsList().get(0).getSpecsValues().split(",");

                for (String s : split) {
                    if(StrUtil.isNotEmpty(s)){
                        SpecsAttribute specsAttribute = iSpecsAttributeService.selectById(s.split(":")[0]);
                        specsValues += specsAttribute.getAttributeName()+":"+s.split(":")[1]+",";
                    }
                }

                orderItem.setSpecsValues(specsValues);
                //规格id
                orderItem.setSpecsId(item.getItemSpecsList().get(0).getId());
            }

            //购买数量
            orderItem.setNum(item.getBuyNum());
            //单价
            orderItem.setPrice(price);
            //商品名称
            orderItem.setTitle(item.getTitle());
            //商品缩略图
            orderItem.setPicPath(item.getImage());
            //总价=单价×数量
            orderItem.setTotalFee(price.multiply(buyNum));

            orderItemMapper.insert(orderItem);
            //商品利润=商品利润×购买数量
            profitsGoods = profitsGoods.add(profits.multiply(buyNum));
            //订单金额累计+=产品单价×购买数量
            payAmount = payAmount.add(orderItem.getTotalFee());


            try {

                List<Object> listCart = redisUtil.hvals(CART_PRE + ":" + createOrderQueryDTO.getMemberId());
                for (Object json : listCart) {
                    CartProduct cart = new Gson().fromJson((String) json, CartProduct.class);
                    if (cart.getProductNum().equals(item.getItemNumber()) && cart.getItemSpecsNo().equals(item.getItemSpecsList().get(0).getSpecsNo())) {
                        redisUtil.hdel(CART_PRE + ":" + createOrderQueryDTO.getMemberId(), cart.getItemNumber() + "_" + cart.getItemSpecsNo());
                    }
                }
            } catch (Exception e) {
                log.error("删除购物车中含该订单的商品失败" + e);
                e.printStackTrace();
            }
        }

        resultData.put("payAmount", payAmount);
        resultData.put("profitsGoods", profitsGoods);
        log.info("结束保存订单商品");
        return R.ok(resultData);
    }


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
        Integer flag = orderMapper.updateById(order);
        if (flag != 0) {
            return true;
        }
        return false;
    }

    /**
     * 功能描述: 强制取消订单
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
     * 订单减库存操作
     *
     * @param orderId
     * @return
     */
    private void stockOpt(String orderId) {
        log.info(orderId + "订单减库存操作");
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
                //增加销量
                item.setNumAll(item.getNumAll() + salesVolume);

                if (StrUtil.isEmpty(orderItem.getItemSpecsNo())) {
                    //减少库存
                    item.setNum(item.getNum() - salesVolume);
                } else {
                    //有规格
                    ItemSpecs itemSpecs = iItemSpecsService.selectById(orderItem.getSpecsId());
                    //减库存
                    itemSpecs.setStock(itemSpecs.getStock() - salesVolume);
                    iItemSpecsService.updateById(itemSpecs);
                }
                iItemService.updateById(item);

            }
            log.info(orderId + "订单减库存操作完成");
        } catch (Exception e) {
            log.info(orderId + "订单减库存操作出现异常");
            e.printStackTrace();
            throw new SystemException("库存操作失败");
        }
    }

    /**
     * 订单加库存操作
     *
     * @param orderId
     * @return
     */
    private void addOpt(String orderId) {
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
                    ItemSpecs itemSpecs = iItemSpecsService.selectById(orderItem.getSpecsId());
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
            throw new SystemException("库存操作失败");
        }
    }
}
