package com.xinshang.rest.modular.asmall.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xinshang.rest.common.util.AllinPayUtil;
import com.xinshang.rest.common.util.RedisUtil;
import com.xinshang.rest.config.constant.Constants;
import com.xinshang.rest.modular.asmall.dao.ClientLogMoneysMapper;
import com.xinshang.rest.modular.asmall.dao.IntergraLogMoneysMapper;
import com.xinshang.rest.modular.asmall.dao.SupplierLogMoneysMapper;
import com.xinshang.rest.modular.asmall.dto.*;
import com.xinshang.rest.modular.asmall.model.*;
import com.xinshang.rest.modular.asmall.service.*;
import com.xinshang.rest.modular.asmall.service.impl.JoininServiceImpl;
import com.xinshang.rest.modular.asmall.service.impl.ProjectServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

/**
 * 通联接口异步回调处理类
 * @author zhangjiajia
 */
@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(value = "allinPayAsynRespNotice")
public class AllinPayAsynRespController {

    private final IMemberService iMemberService;
    private final IAccountService iAccountService;
    private final IClientLogMoneysService iClientLogMoneysService;
    private final IOrderService iOrderService;
    private final IJoininService iJoininService;
    private final RedisUtil redisUtil;


    /**
     * 客户签约通联电子协议异步回调方法
     * @param allinPayAsynResponseDTO
     */
    @PostMapping("memberSignContract")
    public void memberAsynSignContract(AllinPayAsynResponseDTO allinPayAsynResponseDTO) {
        log.info("通联电子协议签约异步返回结果:{}",allinPayAsynResponseDTO);
        iMemberService.memberAsynSignContract(allinPayAsynResponseDTO);
        log.info("通联电子协议签约异步执行完毕");
    }

    /**
     * 设置支付密码异步回调方法
     * @param allinPayAsynResponseDTO
     */
    @RequestMapping("memberSetPwdContract")
    public void memberAsynSetPwdContract(AllinPayAsynResponseDTO allinPayAsynResponseDTO) {
        log.info("客户设置支付密码异步返回结果:{}",allinPayAsynResponseDTO);
        log.info("客户设置支付密码异步执行完毕");
        iMemberService.memberAsynSetPwdContract(allinPayAsynResponseDTO);
    }

    /**
     * 修改支付密码异步回调方法
     * @param allinPayAsynResponseDTO
     */
    @RequestMapping("memberUpdatePwdContract")
    public void memberAsynUpdatePwdContract(AllinPayAsynResponseDTO allinPayAsynResponseDTO) {
        log.info("客户修改支付密码异步返回结果:{}",allinPayAsynResponseDTO);
        log.info("客户修改支付密码异步执行完毕");
        iMemberService.memberAsynUpdatePwdContract(allinPayAsynResponseDTO);
    }

    /**
     * 重置支付密码异步回调方法
     * @param allinPayAsynResponseDTO
     */
    @RequestMapping("memberResetPwdContract")
    public void memberAsynResetPwdContract(AllinPayAsynResponseDTO allinPayAsynResponseDTO) {
        log.info("客户重置支付密码异步返回结果:{}",allinPayAsynResponseDTO);
        log.info("客户重置支付密码异步执行完毕");
        iMemberService.memberAsynResetPwdContract(allinPayAsynResponseDTO);
    }

    /**
     * 提现异步回调方法
     * @param allinPayAsynResponseDTO
     * @author lyk
     */
    @RequestMapping("/withdrawal")
    public void withdrawal(AllinPayAsynResponseDTO allinPayAsynResponseDTO) {
        log.info("通联提现异步返回结果:{}", allinPayAsynResponseDTO);
        Optional<AllinPayAsynWithdrawalDTO> response = AllinPayUtil.response(allinPayAsynResponseDTO, AllinPayAsynWithdrawalDTO.class);
        AllinPayAsynWithdrawalDTO apasc = response.get();
        if(apasc.getStatus().equals(Constants.SUCCESS_CODE)){
            JSONObject returnValue = apasc.getReturnValue();

            String bizOrderNo = returnValue.get("bizOrderNo").toString();
           if(redisUtil.get(bizOrderNo) == null || (int)redisUtil.get(bizOrderNo)>0){
                return;
            }
            redisUtil.incr(bizOrderNo,1);

            Member member = iMemberService.selectOne(new EntityWrapper<Member>().eq("biz_user_id", returnValue.get("buyerBizUserId").toString()));
            BigDecimal amount1 = new BigDecimal(returnValue.get("amount").toString()).divide(new BigDecimal(100));
            log.info("提现成功:{}",amount1);
            EntityWrapper<Account> accountEW = new EntityWrapper();
            accountEW.eq("master_id", member.getId()).eq("type", 1);
            Account account = iAccountService.selectOne(accountEW);
            BigDecimal balance = account.getAvailableBalance().subtract(amount1);
            account.setAvailableBalance(balance);
            account.setTotleAmount(account.getTotleAmount().subtract(amount1));
            iAccountService.updateById(account);
            ClientLogMoneys slm = new ClientLogMoneys();
            slm.setCreateTime(new Date());
            //1：充值 2：消费 3：提现
            slm.setDataSrc(3);
            //1：有效 0：删除
            slm.setDeleteFlg(1);
            //金额
            slm.setMoney(amount1);
            //：1：收入 2：支出
            slm.setMoneyType(2);
            slm.setClientId(member.getId());
            slm.setUserName(member.getUsername());
            slm.setBalance(balance);
            slm.setTradeNo(returnValue.get("bizOrderNo").toString());
            slm.setRemark("客户提现");
            iClientLogMoneysService.insert(slm);
            log.info("客户提现完成，修改账户成功!");
        }
    }


    /**
     * 客户支付异步回调方法
     * @param allinPayAsynResponseDTO
     */
    @PostMapping("payCallback")
    public void payCallback(AllinPayAsynResponseDTO allinPayAsynResponseDTO) {
        log.info("客户支付异步返回结果:{}",allinPayAsynResponseDTO);

        Optional<AllinPayAsynPayCallbackDTO> response = AllinPayUtil.response(allinPayAsynResponseDTO, AllinPayAsynPayCallbackDTO.class);
        AllinPayAsynPayCallbackDTO apasc = response.get();

        if(apasc.getStatus().equals(Constants.SUCCESS_CODE)){
            JSONObject returnValue = apasc.getReturnValue();

            BigDecimal amount1 = new BigDecimal(returnValue.get("amount").toString());
            Object payNum = returnValue.get("bizOrderNo");
            Order order = iOrderService.selectOne(new EntityWrapper<Order>().eq("pay_num", payNum));

            if (order.getStatus() == 8) {
                log.info("用户账单支付状态已更改,订单号：" + order.getOrderId());
            } else {
                log.info("用户确认支付状态未更改，订单号：" + order.getOrderId());
                OrderDTO dto = new OrderDTO();
                dto.setPayMent(amount1.toString());
                dto.setPayType((Integer)returnValue.get("payType"));
                dto.setTradeNo((String)returnValue.get("tradeNo"));
                iOrderService.reduceOrder(order,dto);
            }
        }
        log.info("客户支付异步执行完毕");
    }

    /**
     * 加盟异步回调方法
     * @param allinPayAsynResponseDTO
     * @author lyk
     */
    @RequestMapping("/joininCallback")
    public void joininCallback(AllinPayAsynResponseDTO allinPayAsynResponseDTO) {
        log.info("通联加盟异步返回结果:{}", allinPayAsynResponseDTO);
        Optional<AllinPayAsynWithdrawalDTO> response = AllinPayUtil.response(allinPayAsynResponseDTO, AllinPayAsynWithdrawalDTO.class);
        AllinPayAsynWithdrawalDTO apasc = response.get();

        if(apasc.getStatus().equals(Constants.SUCCESS_CODE)){
            JSONObject returnValue = apasc.getReturnValue();

            String bizOrderNo = returnValue.get("bizOrderNo").toString();
            log.info("本次加盟订单号:{}",bizOrderNo);
            Object redisBizOrderNo = redisUtil.get(bizOrderNo);
            log.info("redisBizOrderNo:{}",redisBizOrderNo);
            if(redisBizOrderNo == null || (int)redisBizOrderNo > 0){
               log.info("未找到该订单号:{}",bizOrderNo);
                return;
            }
            redisUtil.incr(bizOrderNo,1);


            String extendInfo = returnValue.get("extendInfo").toString() ;
            BigDecimal originalAmount = new BigDecimal(returnValue.get("amount").toString()).divide(new BigDecimal(100));
            String buyerBizUserId=returnValue.get("buyerBizUserId").toString();
            log.info("加盟成功:{}",originalAmount);
            iJoininService.joinin(bizOrderNo,originalAmount,buyerBizUserId,extendInfo);
        }
    }


    /**
     * 充值异步回调方法
     * @param allinPayAsynResponseDTO
     */
    @RequestMapping("rechargeCallback")
    public void rechargeCallback(AllinPayAsynResponseDTO allinPayAsynResponseDTO) {
        log.info("充值异步返回结果:{}",allinPayAsynResponseDTO);

        Optional<AllinPayAsynPayCallbackDTO> response = AllinPayUtil.response(allinPayAsynResponseDTO, AllinPayAsynPayCallbackDTO.class);
        AllinPayAsynPayCallbackDTO apasc = response.get();

        if(apasc.getStatus().equals(Constants.SUCCESS_CODE)){
            JSONObject returnValue = apasc.getReturnValue();
            String bizOrderNo = returnValue.get("bizOrderNo").toString();
           if(redisUtil.get(bizOrderNo) == null || (int)redisUtil.get(bizOrderNo)>0){
                return;
            }
            redisUtil.incr(bizOrderNo,1);
             Member member = iMemberService.selectOne(new EntityWrapper<Member>().eq("biz_user_id", returnValue.get("buyerBizUserId").toString()));
            BigDecimal amount1 = new BigDecimal(returnValue.get("amount").toString()).divide(new BigDecimal(100));

            EntityWrapper<Account> wrapper = new EntityWrapper<>();
            wrapper.eq("master_id",member.getId());
            wrapper.eq("type",1);
            Account account=iAccountService.selectOne(wrapper);
            account.setTotleAmount(account.getTotleAmount().add(amount1));
            account.setAvailableBalance(account.getAvailableBalance().add(amount1));
            iAccountService.updateById(account);
            log.info("开始插入客户交易流水");
            ClientLogMoneys clientLogMoneys=new ClientLogMoneys();
            clientLogMoneys.setClientId(member.getId());
            clientLogMoneys.setUserName(member.getUsername());
            clientLogMoneys.setMoney(amount1);
            clientLogMoneys.setBalance(account.getAvailableBalance());
            clientLogMoneys.setDataSrc(1);
            clientLogMoneys.setMoneyType(1);
            clientLogMoneys.setTradeNo(bizOrderNo);
            clientLogMoneys.setDeleteFlg(1);
            clientLogMoneys.setCreateTime(new Date());
            clientLogMoneys.setRemark("客户充值");
            iClientLogMoneysService.insert(clientLogMoneys);
            log.info("结束插入客户交易流水");

        }
        log.info("充值异步执行完毕");
    }

}
