package com.xinshang.modular.biz.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.allinpay.yunst.sdk.YunClient;
import com.allinpay.yunst.sdk.bean.YunRequest;
import com.allinpay.yunst.sdk.util.RSAUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
 import com.xinshang.config.properties.AllinPayCodeProperties;
import com.xinshang.config.properties.AllinPayProperties;
import com.xinshang.config.properties.SystemProperties;
import com.xinshang.constant.Constants;

import com.xinshang.core.base.tips.SuccessTip;
import com.xinshang.core.base.tips.Tip;
import com.xinshang.core.enums.BizTypeEnum;
import com.xinshang.core.enums.UserTypeEnum;
import com.xinshang.core.exception.SystemException;
import com.xinshang.core.util.AllinPayUtil;
import com.xinshang.core.util.NoUtil;
import com.xinshang.core.util.RedisUtil;
import com.xinshang.modular.biz.dao.JoininMapper;
import com.xinshang.modular.biz.dao.ProjectMapper;
import com.xinshang.modular.biz.dto.*;
import com.xinshang.modular.biz.model.*;
import com.xinshang.modular.biz.service.*;
import com.xinshang.modular.biz.utils.MoneyFormatTester;
import com.xinshang.modular.biz.vo.JoininVO;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.*;

/**
 * <p>
 * 加盟表 服务实现类
 * </p>
 *
 * @author zhoushuai
 * @since 2019-10-23
 */
@Service
@Slf4j
@AllArgsConstructor
public class JoininServiceImpl extends ServiceImpl<JoininMapper, Joinin> implements IJoininService {

    private final JoininMapper joininMapper;
    private final AllinPayCodeProperties allinPayCodeProperties;
    private final AllinPayProperties allinPayProperties;
    private final ISupplierService supplierService;
    private final IMemberService memberService;
    private final ProjectMapper projectMapper;
    private final SystemProperties systemProperties;
    private final RedisUtil redisUtil;
    private final IRepayPlanService iRepayPlanService;
    private final ISupplierLogMoneysService iSupplierLogMoneysService;
    private final IMemberService iMemberService;
    private final IClientLogMoneysService iClientLogMoneysService;
    private final IAccountService iAccountService;
    private final ISupplierService iSupplierService;

    @Override
    public List<JoininInfo> joinList(JoininDTO param, Page<JoininInfo> page) {
        return joininMapper.joinList(param, page);
    }

    @Override
    public ContractInfo joinListByProjectId(Long projectId) {
        return joininMapper.joinListByProjectId(projectId);
    }

    @Override
    public List<JoininVO> showJoinin(Long projectId) {
        return joininMapper.showJoinin(projectId);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateState(String extendInfo,String bizOrderNo) {
        log.info("更新加盟表状态");
        extendInfo=extendInfo.substring(extendInfo.lastIndexOf("_")+1);
        List<Joinin> joininList=joininMapper.selectList(new EntityWrapper<Joinin>().in("join_id", extendInfo));
        for(Joinin joinin:joininList){
            joinin.setStatus(2);
            joininMapper.updateById(joinin);
            Member member=iMemberService.selectById(joinin.getCustomId());
            Account account=iAccountService.selectOne(new EntityWrapper<Account>().eq("master_id", joinin.getCustomId()).eq("type",1));

            //更新还款计划实还本金
            RepayPlan repayPlanOne=iRepayPlanService.repayPlanByJoinId(joinin.getJoinId());
            repayPlanOne.setHaveTim(joinin.getInvestmentAmount());
            repayPlanOne.setHaveTimDate(new Date());
            iRepayPlanService.updateById(repayPlanOne);


            log.info("更新账户表供用商可用余额");
            //更新账户表供用商余额
            Project project=projectMapper.selectById(joinin.getProjectId());

            int joinNum=this.selectCount(new EntityWrapper<Joinin>().eq("Status", 1).eq("project_id",joinin.getProjectId()));
            if(joinNum==0){
                //修改招募表状态
                project.setState(8);
                projectMapper.updateById(project);
            }
         /*   iAccountService.updateAmount(project.getSupplierId(), joinin.getInvestmentAmount());
            log.info("更新供用商可用余额");
            Supplier supplier = iSupplierService.selectById(project.getSupplierId());
            supplier.setUsedLoanAmount(supplier.getUsedLoanAmount().subtract(joinin.getInvestmentAmount()));
            iSupplierService.updateById(supplier);

            log.info("开始插入供用商对象");
            SupplierLogMoneys supplierLogMoneys=new SupplierLogMoneys();
            supplierLogMoneys.setSupplierId(project.getSupplierId().intValue());
            supplierLogMoneys.setMoney(joinin.getInvestmentAmount());
            supplierLogMoneys.setPhone(supplier.getPhone());
            supplierLogMoneys.setDataSrc(5);
            supplierLogMoneys.setMoneyType(2);
            supplierLogMoneys.setTradeNo(bizOrderNo);
            supplierLogMoneys.setDeleteFlg(1);
            supplierLogMoneys.setCreateTime(new Date());
            supplierLogMoneys.setRemark("供应商还款");
            iSupplierLogMoneysService.insert(supplierLogMoneys);
            log.info("结束插入供用商对象");

            log.info("更改账户表客户余额");
            //更改账户表客户余额
            iAccountService.updateMemberAmount(joinin.getCustomId(), joinin.getInvestmentAmount());
            log.info("结束更改账户表客户余额");

            log.info("开始插入加盟客户交易流水");
            ClientLogMoneys clientLogMoneys=new ClientLogMoneys();
            clientLogMoneys.setClientId(member.getId());
            clientLogMoneys.setUserName(member.getPhone());
            clientLogMoneys.setMoney(joinin.getInvestmentAmount());
            clientLogMoneys.setDataSrc(4);
            clientLogMoneys.setMoneyType(1);
            clientLogMoneys.setTradeNo(bizOrderNo);
            clientLogMoneys.setDeleteFlg(1);
            clientLogMoneys.setBalance(account.getAvailableBalance());
            clientLogMoneys.setCreateTime(new Date());
            clientLogMoneys.setRemark("客户收款");
            iClientLogMoneysService.insert(clientLogMoneys);
            log.info("结束插入加盟客户交易流水");*/
            log.info("加盟成功");
        }
    }


    /**
     * 还款逻辑
     * @param joininIds
     * @param projectId
     * @param decimalA
     * @return
     */
    @Override
    public  Map<String, String> payGoods(String joininIds,Long projectId,BigDecimal decimalA) {
        log.info("开始还款逻辑{},{},{}",joininIds,projectId,decimalA);
        String[] idsItems = joininIds.split(",");

        if(idsItems.length>10){
            throw  new SystemException("还款人数不能超过10人");
        }

        Map<String,String> map = new HashMap(3);
        Project project =projectMapper.selectById(projectId);
        log.info("获取招募信息:{}",project);
        Supplier supplier=supplierService.selectById(project.getSupplierId());
        log.info("获取供应商信息:{}",supplier);
        String bizOrderNo=NoUtil.generateCode(BizTypeEnum.COST_NUMBER, UserTypeEnum.SUPPLIER,projectId);

        //同步到通联
        YunRequest request = new YunRequest("OrderService", "consumeApply");
        request.put("payerId",supplier.getBizUserId());
        request.put("bizUserId",supplier.getBizUserId());
        request.put("recieverId",allinPayProperties.getBizUserId());

        //商户订单号（支付订单）
        request.put("bizOrderNo", bizOrderNo);
        //金额 单位：分
         Long amount = MoneyFormatTester.bigDecimal2Long(decimalA);
        request.put("amount", amount);
        //内扣，如果不存在，则填 0。
        request.put("fee", 0);
        request.put("validateType", 2);
        //后台通知地址
        request.put("backUrl", systemProperties.getProjectUrl()+"/allinPayAsynRespNotice/repaymentCallback");
        JSONObject payMethod = new JSONObject();
        JSONObject subPayMethod = new JSONObject();
        //账户余额
        JSONArray balanceArray = new JSONArray();
        JSONArray splitRuleArray = new JSONArray();
        subPayMethod.put("amount", amount);
        subPayMethod.put("accountSetNo",allinPayProperties.getAccountSetNo());
        balanceArray.add(subPayMethod);
        payMethod.put("BALANCE",balanceArray);
        request.put("payMethod", payMethod);
        //行业代码
        request.put("industryCode", allinPayCodeProperties.getOtherCode());
        //行业名称
        request.put("industryName", allinPayCodeProperties.getOtherName());
        //终端访问类型 访问终端类型:[1:Mobile,2:PC]
        request.put("source", 2);
        request.put("extendInfo","归还_"+joininIds);

        for(int i=0;i<idsItems.length;i++){

            String joinId =idsItems[i];
            log.info("joinId:{}",joinId);
            Joinin myJoinin = joininMapper.selectById(Integer.parseInt(joinId));
            log.info("myJoinin:{}",myJoinin);
            Member my = memberService.selectById(myJoinin.getCustomId());
            log.info("my:{}",my);
            JSONObject splitRule = new JSONObject();
            splitRule.put("bizUserId",my.getBizUserId());
            splitRule.put("amount",MoneyFormatTester.bigDecimal2Long(myJoinin.getInvestmentAmount()));
            splitRule.put("fee",0L);
            splitRuleArray.add(splitRule);

        }

        request.put("splitRule",splitRuleArray);
        Optional<AllinPayResponseDTO<AllinPayDepositApplyResponseDTO>> response = AllinPayUtil.request(request, AllinPayDepositApplyResponseDTO.class);
        //通联同步失败一定要抛异常回滚数据库
        if(!Constants.SUCCESS_CODE.equals(response.get().getStatus())){
            log.warn("用户还款申请请求到通联失败:{}", response);
            map.put("message",response.get().getMessage());
            map.put("state","0");
            throw  new SystemException(response.get().getMessage());
        }
        AllinPayDepositApplyResponseDTO signedValue = response.get().getSignedValue();
        if(Constants.STATUS_FAIL_CODE.equals(signedValue.getPayStatus())){
            log.warn("用户还款申请请求到通联失败:{}", response);
            map.put("message",signedValue.getPayFailMessage());
            map.put("state","0");
            throw  new SystemException(signedValue.getPayFailMessage());
        }
        redisUtil.set(signedValue.getBizOrderNo(),0,3600);



        String url = this.openConfirmPaymentPage(supplier.getBizUserId(), bizOrderNo,systemProperties.getProjectUrl()+"/joinin/goPayPwdResult");
        map.put("url", url);
        map.put("tradeNo", bizOrderNo);
        map.put("bizUserId", supplier.getBizUserId());
        map.put("bizOrderNo", signedValue.getBizOrderNo());
        map.put("state","1");
        return map;
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
        String url =  allinPayProperties.getPayConfirmPasswordUrl() + "?" + setRes;
        return url;

    }

    @Override
    public Tip payConfirm(JoininDTO joininDTO) {
        YunRequest request = new YunRequest("OrderService", "pay");
        request.put("bizUserId",joininDTO.getBizUserId());
        request.put("bizOrderNo",joininDTO.getBizOrderNo());
        //todo 交易编号 收银宝快捷支付必传 其他支付则非必传
        //短信验证码
        request.put("verificationCode",joininDTO.getVerificationCode());
        //用户公网 IP 用于分控校验 注：不能使用“127.0.0.1” “localhost”
        request.put("consumerIp",Constants.CUSTOMER_IP);
        Optional<AllinPayResponseDTO<AllinPayCashWithdrawalApplyResponseDTO>> response = AllinPayUtil.request(request, AllinPayCashWithdrawalApplyResponseDTO.class);
        AllinPayCashWithdrawalApplyResponseDTO signedValue = response.get().getSignedValue();
        if(Constants.STATUS_FAIL_CODE.equals(signedValue.getPayStatus())) {
            log.warn("还款确认请求到通联失败:{}", response);
            throw  new SystemException(response.get().getMessage());
        }
        SuccessTip successTip = new SuccessTip();
        successTip.setMessage("还款成功,请稍后查询");
        return successTip;
    }
}
