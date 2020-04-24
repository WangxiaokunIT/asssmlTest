package com.xinshang.modular.biz.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xinshang.constant.Constants;
import com.xinshang.constant.OrderStatusEnum;
import com.xinshang.core.util.AllinPayUtil;
import com.xinshang.core.util.RedisUtil;
import com.xinshang.modular.biz.dao.*;
import com.xinshang.modular.biz.dto.*;
import com.xinshang.modular.biz.model.*;
import com.xinshang.modular.biz.service.*;
 import com.xinshang.modular.biz.service.IProjectService;
import com.xinshang.modular.biz.service.ISupplierService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author zhangjiajia
 */
@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(value = "allinPayAsynRespNotice")
public class AllinPayAsynRespController {

    private final ISupplierService iSupplierService;
    private final IProjectService projectService;
    private final RedisUtil redisUtil;
    private final IAccountService iAccountService;
    private final ISupplierLogMoneysService supplierLogMoneysService;
    private  final IJoininService iJoininService;
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
    private OrderStateRecordMapper orderStateRecordMapper;
    @Autowired
    private ClientLogMoneysMapper clientLogMoneysMapper;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private IOrderService iOrderService;
    /**
     * 供应商签约通联电子协议异步回调方法
     * @param allinPayAsynResponseDTO
     */
    @PostMapping("supplierAsynSignContract")
    public void supplierAsynSignContract(AllinPayAsynResponseDTO allinPayAsynResponseDTO) {
        log.info("通联电子协议签约异步返回结果:{}",allinPayAsynResponseDTO);
        Optional<AllinPayAsynSignContractDTO> response = AllinPayUtil.response(allinPayAsynResponseDTO, AllinPayAsynSignContractDTO.class);
        AllinPayAsynSignContractDTO apasc = response.get();
        String returnValue = apasc.getReturnValue();
        JSONObject jsonObject = JSON.parseObject(returnValue);
        Supplier supplier = iSupplierService.selectOne(new EntityWrapper<Supplier>().eq("biz_user_id", jsonObject.get("bizUserId").toString()));
        supplier.setContractNo(apasc.getContractNo());
        iSupplierService.updateById(supplier);
    }

    /**
     * 供应商设置支付密码异步回调方法
     * @param allinPayAsynResponseDTO
     */
    @PostMapping("supplierAsynSetPayPwd")
    public void supplierAsynSetPayPwd(AllinPayAsynResponseDTO allinPayAsynResponseDTO) {
        log.info("供应商设置支付密码异步返回结果:{}",allinPayAsynResponseDTO);
        Optional<AllinPaySyncPayPwdDTO> response = AllinPayUtil.response(allinPayAsynResponseDTO, AllinPaySyncPayPwdDTO.class);
        AllinPaySyncPayPwdDTO apasc = response.get();
        String returnValue = apasc.getReturnValue();
        JSONObject jsonObject = JSON.parseObject(returnValue);
        if(Constants.SUCCESS_CODE.equals(jsonObject.get("status"))){
            Supplier supplier = iSupplierService.selectOne(new EntityWrapper<Supplier>().eq("biz_user_id", jsonObject.get("bizUserId").toString()));
            supplier.setIsSetPayPwd(1);
            iSupplierService.updateById(supplier);
        }
    }

    /**
     * 供应商修改支付密码异步回调方法
     * @param allinPayAsynResponseDTO
     */
    @PostMapping("supplierAsynUpdatePayPwd")
    public void supplierAsynUpdatePayPwd(AllinPayAsynResponseDTO allinPayAsynResponseDTO) {
        log.info("供应商修改支付密码异步返回结果:{}",allinPayAsynResponseDTO);
    }

    /**
     * 供应商重置支付密码异步回调方法
     * @param allinPayAsynResponseDTO
     */
    @PostMapping("supplierAsynResetPayPwd")
    public void supplierAsynResetPayPwd(AllinPayAsynResponseDTO allinPayAsynResponseDTO) {
        log.info("供应商重置支付密码异步返回结果:{}",allinPayAsynResponseDTO);
        Optional<AllinPaySyncPayPwdDTO> response = AllinPayUtil.response(allinPayAsynResponseDTO, AllinPaySyncPayPwdDTO.class);
        AllinPaySyncPayPwdDTO apasc = response.get();
        String returnValue = apasc.getReturnValue();
        JSONObject jsonObject = JSON.parseObject(returnValue);
        if(Constants.SUCCESS_CODE.equals(jsonObject.get("status"))){
            Supplier supplier = iSupplierService.selectOne(new EntityWrapper<Supplier>().eq("biz_user_id", jsonObject.get("bizUserId").toString()));
            supplier.setIsSetPayPwd(0);
            iSupplierService.updateById(supplier);
        }
    }

    /**
     * 企业会员审核结果通知方法
     * @param allinPayAsynResponseDTO
     */
    @PostMapping("setCompanyInfo")
    public void setCompanyInfo(AllinPayAsynResponseDTO allinPayAsynResponseDTO) {
        log.info("企业会员审核结果通知:{}",allinPayAsynResponseDTO);
        Optional<AllinPayAsynSetCompanyInfoDTO> response = AllinPayUtil.response(allinPayAsynResponseDTO, AllinPayAsynSetCompanyInfoDTO.class);
        AllinPayAsynSetCompanyInfoDTO apasc = response.get();
        JSONObject returnValue = apasc.getReturnValue();
        Supplier supplier = iSupplierService.selectOne(new EntityWrapper<Supplier>().eq("biz_user_id", returnValue.get("bizUserId").toString()));
        if((Integer) returnValue.get("result")==2){
            supplier.setIsAuth(0);
        }else{
            supplier.setIsAuth(1);
        }
        iSupplierService.updateById(supplier);
    }


    /**
     * 充值异步回调方法
     * @param allinPayAsynResponseDTO
     * @author lyk
     */
    @RequestMapping("/deposit")
    public void deposit(AllinPayAsynResponseDTO allinPayAsynResponseDTO) {
        log.info("通联充值异步返回结果:{}", allinPayAsynResponseDTO);
        Optional<AllinPayAsynWithdrawalDTO> response = AllinPayUtil.response(allinPayAsynResponseDTO, AllinPayAsynWithdrawalDTO.class);
        AllinPayAsynWithdrawalDTO apasc = response.get();
        if(apasc.getStatus().equals(Constants.SUCCESS_CODE)){

                JSONObject returnValue = apasc.getReturnValue();
            String bizOrderNo = returnValue.get("bizOrderNo").toString();
           if(redisUtil.get(bizOrderNo) == null || (int)redisUtil.get(bizOrderNo)>0){
                return;
            }
            redisUtil.incr(bizOrderNo,1);

            Supplier supplier = iSupplierService.selectOne(new EntityWrapper<Supplier>().eq("biz_user_id", returnValue.get("buyerBizUserId").toString()));
            EntityWrapper<Account> accountEW = new EntityWrapper();
            accountEW.eq("master_id",supplier.getId()).eq("type",2);
            Account account = iAccountService.selectOne(accountEW);
            BigDecimal amount = new BigDecimal(returnValue.get("amount").toString()).divide(new BigDecimal(100));
            account.setAvailableBalance(account.getAvailableBalance().add(amount));
            account.setTotleAmount(account.getTotleAmount().add(amount));
            iAccountService.updateById(account);
            SupplierLogMoneys slm = new SupplierLogMoneys();
            slm.setCreateTime(new Date());
            //充值
            slm.setDataSrc(1);
            slm.setDeleteFlg(1);
            slm.setMoney(amount);
            slm.setMoneyType(1);
            slm.setSupplierId(supplier.getId());
            slm.setPhone(supplier.getPhone());
            slm.setTradeNo(returnValue.get("bizOrderNo").toString());
            slm.setRemark("供应商充值");
            supplierLogMoneysService.insert(slm);
        }
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

            Supplier supplier = iSupplierService.selectOne(new EntityWrapper<Supplier>().eq("biz_user_id", returnValue.get("buyerBizUserId").toString()));
            EntityWrapper<Account> accountEW = new EntityWrapper();
            accountEW.eq("master_id",supplier.getId()).eq("type",2);
            Account account = iAccountService.selectOne(accountEW);
            BigDecimal amount = new BigDecimal(returnValue.get("amount").toString()).divide(new BigDecimal(100));
            account.setAvailableBalance(account.getAvailableBalance().subtract(amount));
            account.setTotleAmount(account.getTotleAmount().subtract(amount));
            iAccountService.updateById(account);
            SupplierLogMoneys slm = new SupplierLogMoneys();
            slm.setCreateTime(new Date());
            //提现记录
            slm.setDataSrc(3);
            slm.setDeleteFlg(1);
            slm.setMoney(amount);
            slm.setMoneyType(2);
            slm.setSupplierId(supplier.getId());
            slm.setPhone(supplier.getPhone());
            slm.setTradeNo(returnValue.get("bizOrderNo").toString());
            slm.setRemark("供应商提现");
            supplierLogMoneysService.insert(slm);
            log.info("提现异步接口修改供应商账号余额成功");
        }
    }


    /**
     * 批量代付异步回调方法
     * @param allinPayAsynResponseDTO
     * @author lyk
     */
    @RequestMapping("/payment")
    public void payment(AllinPayAsynResponseDTO allinPayAsynResponseDTO) {
        log.info("通联批量代付异步返回结果:{}", allinPayAsynResponseDTO);
        String bizBatchNo = "";
        Object id = redisUtil.get("OrderService_batchAgentPay_" + bizBatchNo);
        if(id != null) {
            Project project = projectService.selectById(Long.valueOf(id.toString()));
            if (project.getState() == 7) {
                return;
            }
            project.setBizBatchNo(bizBatchNo);
            project.setState(7);
            projectService.updateProject(project);
        }

//        Project project = projectService.selectOne(new EntityWrapper<Project>().eq("biz_batch_no", ""));
//        project.setState(7);
//        projectService.updateProject(project);

    }


    /**
     * 还款异步回调方法
     * @param allinPayAsynResponseDTO
     * @author lyk
     */
    @RequestMapping("/repaymentCallback")
     public void joininCallback( AllinPayAsynResponseDTO allinPayAsynResponseDTO) {
        log.info("通联还款异步返回结果:{}", allinPayAsynResponseDTO);
        Optional<AllinPayAsynWithdrawalDTO> response = AllinPayUtil.response(allinPayAsynResponseDTO, AllinPayAsynWithdrawalDTO.class);
        AllinPayAsynWithdrawalDTO apasc = response.get();
        if(apasc.getStatus().equals(Constants.SUCCESS_CODE)){
            JSONObject returnValue = apasc.getReturnValue();
            String bizOrderNo = returnValue.get("bizOrderNo").toString();
            String extendInfo = returnValue.get("extendInfo").toString();
            if(redisUtil.get(bizOrderNo) == null || (int)redisUtil.get(bizOrderNo)>0){
                return;
            }
            redisUtil.incr(bizOrderNo,1);

           iJoininService.updateState(extendInfo,bizOrderNo);

        }
    }

    /**
     * 退货异步回调方法
     * @param allinPayAsynResponseDTO
     * @author djy
     */
    @RequestMapping("/refund")
    public void refund( AllinPayAsynResponseDTO allinPayAsynResponseDTO) {
        log.info("通联退款异步返回结果:{}", allinPayAsynResponseDTO);
        Optional<AllinPayAsynWithdrawalDTO> response = AllinPayUtil.response(allinPayAsynResponseDTO, AllinPayAsynWithdrawalDTO.class);
        AllinPayAsynWithdrawalDTO apasc = response.get();
        if(apasc.getStatus().equals(Constants.SUCCESS_CODE)){
            JSONObject returnValue = apasc.getReturnValue();
            String bizOrderNo = returnValue.get("bizOrderNo").toString();
            Order order = iOrderService.selectOne(new EntityWrapper<Order>().eq("pay_num", bizOrderNo));
            OrderBack orderBack1=orderBackService.selectOne(new EntityWrapper<OrderBack>().in("state","1,12").eq("order_id", order.getOrderId()));
if(orderBack1.getState()==12)
{

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
    orderBack1.setState(18);
    orderBackService.updateById(orderBack1);
    order.setStatus(7);
    orderMapper.updateById(order);

}



        }
    }
}
