package com.xinshang.rest.modular.asmall.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.allinpay.yunst.sdk.YunClient;
import com.allinpay.yunst.sdk.bean.YunRequest;
import com.allinpay.yunst.sdk.util.RSAUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xinshang.core.enums.BizTypeEnum;
import com.xinshang.core.enums.UserTypeEnum;
import com.xinshang.core.exception.SystemException;
import com.xinshang.core.util.NoUtil;
import com.xinshang.rest.common.enums.BizExceptionEnum;
import com.xinshang.rest.common.enums.CommonConstants;
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
import com.xinshang.rest.modular.asmall.util.MoneyFormatTester;
import com.xinshang.rest.modular.asmall.vo.JoininVO;
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
 * @since 2019-10-24
 */
@Service
@Slf4j
@AllArgsConstructor
public class JoininServiceImpl extends ServiceImpl<JoininMapper, Joinin> implements IJoininService {

    private final IMemberService imemberService;
    private final ProjectMapper projectMapper;
    private final ISupplierService isupplierService;
    private final  BankMapper bankMapper;
    private final IEqbContractService eqbContractService;

    private  final IIntegralLogMoneysService iIntegralLogMoneysService;

    private final AllinPayCodeProperties allinPayCodeProperties;
    private final AllinPayProperties allinPayProperties;
    private final RestProperties restProperties;
    private final RedisUtil redisUtil;
    private final JoininMapper joininMapper;

    /**
     * 功能描述: 加盟
     *integralLogMoneys
     * @Param: [orderDTO]
     * @Return: com.xinshang.rest.common.util.R
     * @Auther: zhoushuai
     * @Date: 2019/11/18 11:47
     * @Description:
     * @Modify:
     */
    @Override
    public R payGoods(JoininDTO joininDTO) {
        Member member = imemberService.selectById(joininDTO.getCustomId());
        Project project =projectMapper.selectById(joininDTO.getProjectId());
        Supplier supplier=isupplierService.selectById(project.getSupplierId());

        String bizOrderNo=NoUtil.generateCode(BizTypeEnum.COST_NUMBER, UserTypeEnum.CUSTOMER,joininDTO.getCustomId());
        //同步到通联
        YunRequest request = new YunRequest("OrderService", "consumeApply");
         request.put("payerId", member.getBizUserId());

        request.put("recieverId", supplier.getBizUserId());
        //商户订单号（支付订单）
        request.put("bizOrderNo", bizOrderNo);
        //金额 单位：分
        Long amount = MoneyFormatTester.bigDecimal2Long(joininDTO.getInvestmentAmount());
        request.put("amount", amount);
        //内扣，如果不存在，则填 0。
        request.put("fee", 0);

        //后台通知地址
        request.put("backUrl", restProperties.getProjectUrl()+"/allinPayAsynRespNotice/joininCallback");
        JSONObject payMethod = new JSONObject();
        JSONObject subPayMethod = new JSONObject();
        int payType = joininDTO.getPayType();
         if (payType == 2) {
            //收银宝快捷支付(bankCardNo银行卡号，RSA 加密,amount支付金额，单位：分)
            String encrypt;
            try {
                Bank bank=bankMapper.selectById(joininDTO.getBankId());
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
            subPayMethod.put("limitPay", "");
//            subPayMethod.put("subAppid", allinPayProperties.getSubAppId());
            subPayMethod.put("amount", amount);
            subPayMethod.put("acct", joininDTO.getOpenId());
            payMethod.put("WECHAT_PUBLIC",subPayMethod);
             request.put("validateType", 1);
         }else if(payType== 4){
            //账户余额
             JSONArray balanceArray = new JSONArray();
             subPayMethod.put("amount", amount);
             subPayMethod.put("accountSetNo",allinPayProperties.getAccountSetNo());
             balanceArray.add(subPayMethod);
             payMethod.put("BALANCE",balanceArray);
             request.put("validateType", 2);
        }

        request.put("payMethod", payMethod);

        String json = member.getId().toString()+"|"+project.getId()+"|"+payType+"|"+joininDTO.getInvestmentAmount().toString()+"|"+joininDTO.getSignId();
        request.put("extendInfo","代理_"+json);
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
        redisUtil.set(bizOrderNo,0,3600);
        Map<String,String> map = new HashMap<>(3);
        if (payType == 1 || payType == 4) {
            String url = this.openConfirmPaymentPage(member.getBizUserId(), bizOrderNo, joininDTO.getJumpUrl());
            map.put("url", url);
        }
        map.put("bizOrderNo",bizOrderNo);
        map.put("payMent",joininDTO.getInvestmentAmount().toString());
        map.put("payType",payType + "");
         return R.ok(map);
    }

    /**
     * 功能描述: 加盟支付确认 （后台+短信验证码）
     *
     * @Param: [orderDTO]
     * @Return: com.xinshang.rest.common.util.R
     * @Auther: wangxiaokun
     * @Date: 2019/11/18 11:47
     * @Description:
     * @Modify:
     */
    @Override
    public R payConfirm(JoininDTO joininDTO) {
        log.info("加盟支付确认:{}",joininDTO);
        //获取客户信息
        Member member = imemberService.selectById(joininDTO.getCustomId());
        //获取招募信息
        YunRequest request = new YunRequest("OrderService", "pay");
        request.put("bizUserId",member.getBizUserId());
        request.put("bizOrderNo",joininDTO.getBizOrderNo());
        //短信验证码
        request.put("verificationCode",joininDTO.getVerificationCode());
        //用户公网 IP 用于分控校验 注：不能使用“127.0.0.1” “localhost”
        request.put("consumerIp",Constants.CUSTOMER_IP);

        Optional<AllinPayResponseDTO<AllinPayCashWithdrawalApplyResponseDTO>> response = AllinPayUtil.request(request, AllinPayCashWithdrawalApplyResponseDTO.class);
        AllinPayCashWithdrawalApplyResponseDTO signedValue = response.get().getSignedValue();
//        if(Constants.STATUS_FAIL_CODE.equals(signedValue.getPayStatus())) {
//            log.info("加盟失败信息!");
//            return  R.failed(response.get().getSignedValue().getPayFailMessage());
//        }
        if (!CommonConstants.SUCCESS_CODE.equals(response.get().getStatus())) {
            log.warn("加盟失败 支付确认接口失败:{}", response);
            return R.failed(400,response.get().getMessage());
        }
        redisUtil.set(signedValue.getBizOrderNo(),0,3600);

        return R.ok("加盟成功,请稍后查询");
    }

    /**
     * 加盟支付异步操作
     * @param bizOrderNo
     * @param originalAmount
     * @param buyerBizUserId
     * @param extendInfo
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void joinin(String bizOrderNo, BigDecimal originalAmount, String buyerBizUserId,String extendInfo) {

        log.info("开始插入加盟对象{},{},{},{}",bizOrderNo,originalAmount,buyerBizUserId,extendInfo);

        try {
            String jsonStr = extendInfo.split("_")[1];
            log.info("jsonStr:{}",jsonStr);
            String[] info = jsonStr.split("\\|");
            //添加加盟信息
            Joinin joinin = new Joinin();
            joinin.setProjectId(Long.valueOf(info[1]));
            joinin.setPaymentMethod(Integer.valueOf(info[2]));
            joinin.setCustomId(Long.valueOf(info[0]));
            joinin.setInvestmentAmount(new BigDecimal(info[3]));
            joinin.setStatus(1);
            joinin.setBizOrderNo(bizOrderNo);
            joinin.setCreateTime(new Date());
            joinin.setInvestmentTime(new Date());
            joininMapper.insert(joinin);
            log.info("结束插入加盟对象");
            Project project = projectMapper.selectById(joinin.getProjectId());

            log.info("开始判断是否满标");
            List<Joinin> joininList=this.selectList(new EntityWrapper<Joinin>().eq("project_id",joinin.getProjectId()));
            BigDecimal investmentAmountSum= new BigDecimal("0.00");
            for(int i=0;i<joininList.size();i++){
                BigDecimal investmentAmount=joininList.get(i).getInvestmentAmount();
                investmentAmountSum=investmentAmountSum.add(investmentAmount);
            }
            log.info("investmentAmountSum:{}",investmentAmountSum);

            if(investmentAmountSum.compareTo(project.getMaxMoney()) == 0){
                log.info("满标修改项目状态");
               project.setState(4);
               projectMapper.updateById(project);
            }


            log.info("开始修改合同");
            EqbContract eqbContract = eqbContractService.selectOne(new EntityWrapper<EqbContract>().eq("sign_id", info[4]));
            eqbContract.setJoinId(joinin.getJoinId());
            eqbContract.setStatus(1);
            eqbContractService.updateById(eqbContract);
            Member member = imemberService.selectOne(new EntityWrapper<Member>().eq("biz_user_id", buyerBizUserId));
            //加盟积分表
            IntegralLogMoneys integralLogMoneys = new IntegralLogMoneys();
            log.info("joinin:{}", joinin);


            log.info("结束插入加盟客户交易流水");
            //添加积分信息
            integralLogMoneys.setClientId(member.getId().intValue());
            integralLogMoneys.setUserName(member.getUsername());
            //上线之前修改
            BigDecimal point=originalAmount.multiply(new BigDecimal(project.getRecruitmentCycle())).multiply(new BigDecimal(0.001));
            log.info("开始插入积分明细");
           // BigDecimal point = originalAmount.multiply(new BigDecimal(project.getRecruitmentCycle())).multiply(new BigDecimal(100));

            integralLogMoneys.setIntegral(point.intValue());
            integralLogMoneys.setCreateTime(new Date());
            integralLogMoneys.setDataSrc(1);
            integralLogMoneys.setDeleteFlg(1);
            integralLogMoneys.setMoneyType(1);
            integralLogMoneys.setRemark(project.getName());
            iIntegralLogMoneysService.insert(integralLogMoneys);
            log.info("结束插入积分明细");

            log.info("开始修改客户积分");
            member.setPoints(member.getPoints() + point.intValue());
            imemberService.updateById(member);
            log.info("结束修改客户积分");

        }catch (Exception e){
            e.printStackTrace();
        }
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
     *查询加盟信息
     * @param page
     * @return
     */
    @Override
    public List<JoininVO> showJoinin(Long memberId, Page<JoininVO> page) {
        return joininMapper.showJoinin( memberId,page);
    }

    @Override
    public R joinInner(JoinInner joinInner){
        if(joinInner.getRepaymentMethod()==1) //一次性还本付息
             {
            BigDecimal a=new BigDecimal(joinInner.getInvestmentAmount()).multiply(new BigDecimal(joinInner.getRecruitmentCycle())).divide(new BigDecimal(1000));
            joinInner.setPoints(a.intValue());
            joinInner.setInner(new BigDecimal(joinInner.getInvestmentAmount()).multiply(new BigDecimal(joinInner.getRecruitmentCycle())).multiply(joinInner.getEquityRate()).divide(new BigDecimal(12),2, BigDecimal.ROUND_HALF_UP));
             }
        else//按月付息到期还本
        {
            BigDecimal a=new BigDecimal(joinInner.getInvestmentAmount()).multiply(new BigDecimal(joinInner.getRecruitmentCycle())).divide(new BigDecimal(1000));
            joinInner.setPoints(a.intValue());
            joinInner.setInner(new BigDecimal(joinInner.getInvestmentAmount()).multiply(new BigDecimal(joinInner.getRecruitmentCycle())).multiply(joinInner.getEquityRate()).divide(new BigDecimal(12),2, BigDecimal.ROUND_HALF_UP));
        }
        return  R.ok(joinInner);
    }

    @Override
    public ContractInfo joinListByProjectId(Long projectId) {
        return joininMapper.joinListByProjectId(projectId);
    }


}
