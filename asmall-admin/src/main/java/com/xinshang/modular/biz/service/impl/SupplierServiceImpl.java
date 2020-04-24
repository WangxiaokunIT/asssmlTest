package com.xinshang.modular.biz.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.allinpay.yunst.sdk.YunClient;
import com.allinpay.yunst.sdk.bean.YunRequest;
import com.allinpay.yunst.sdk.util.RSAUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xinshang.config.properties.AllinPayCodeProperties;
import com.xinshang.config.properties.AllinPayProperties;
import com.xinshang.config.properties.SystemProperties;
import com.xinshang.constant.Constants;
import com.xinshang.core.base.tips.ErrorTip;
import com.xinshang.core.base.tips.SuccessTip;
import com.xinshang.core.base.tips.Tip;
import com.xinshang.core.common.exception.BizExceptionEnum;
import com.xinshang.core.enums.BizTypeEnum;
import com.xinshang.core.enums.UserTypeEnum;
import com.xinshang.core.exception.SystemException;
import com.xinshang.core.util.AllinPayUtil;
import com.xinshang.core.util.NoUtil;
import com.xinshang.core.util.RedisUtil;
import com.xinshang.core.util.ToolUtil;
import com.xinshang.modular.biz.dto.*;
import com.xinshang.modular.biz.model.*;
import com.xinshang.modular.biz.dao.SupplierMapper;
import com.xinshang.modular.biz.service.*;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xinshang.modular.biz.utils.MoneyFormatTester;
import com.xinshang.modular.biz.vo.SupplierVO;
import com.xinshang.modular.system.model.OperationLog;
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
 * 供应商 服务实现类
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-10-21
 */
@Service
@Slf4j
@AllArgsConstructor
public class SupplierServiceImpl extends ServiceImpl<SupplierMapper, Supplier> implements ISupplierService {

    private final SupplierMapper supplierMapper;
    private final AllinPayProperties allinPayProperties;
    private final AllinPayCodeProperties allinPayCodeProperties;
    private final SystemProperties systemProperties;
    private final IBankService iBankService;
    private final IAccountService iAccountService;
    private final RedisUtil redisUtil;


    @Override
    public List<Supplier> searchSupplier(String search) {
        return supplierMapper.searchSupplier(search);
    }

    /**
     * 添加供应商
     * @param supplier
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Tip add(Supplier supplier) {
        supplier.insert();
        //同步到通联
        YunRequest request = new YunRequest("MemberService", "createMember");
        String memberNo = NoUtil.generateCode(BizTypeEnum.MEMBER_NO, UserTypeEnum.SUPPLIER, supplier.getId());
        request.put("bizUserId", memberNo);
        //会员类型:[3:个人会员,2:企业会员]
        request.put("memberType", supplier.getType() == 0 ? 3 : 2);
        //访问终端类型:[1:Mobile,2:PC]
        request.put("source", 2);
        log.info("用户信息同步到通联:{}", request);
        Optional<AllinPayResponseDTO<APCreateMemberRespDTO>> response = AllinPayUtil.request(request, APCreateMemberRespDTO.class);
        //通联同步失败一定要抛异常回滚数据库
        Assert.isTrue(Constants.SUCCESS_CODE.equals(response.get().getStatus()), () -> {
            log.warn("用户信息同步到通联失败:{}", response);
            return BizExceptionEnum.USER_CREATION_FAILED.getMessage();
        });
        supplier.setBizUserId(memberNo);
        supplier.setAllinpayUserId(response.get().getSignedValue().getUserId());
        this.updateById(supplier);
        return new SuccessTip();
    }

    /**
     * 添加供应商
     * @param supplier
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Tip active(Supplier supplier) {
        //同步到通联
        YunRequest request = new YunRequest("MemberService", "createMember");
        String memberNo = NoUtil.generateCode(BizTypeEnum.MEMBER_NO, UserTypeEnum.SUPPLIER, supplier.getId());
        request.put("bizUserId", memberNo);
        //会员类型:[3:个人会员,2:企业会员]
        request.put("memberType",3);
        //访问终端类型:[1:Mobile,2:PC]
        request.put("source", 2);
        log.info("用户信息同步到通联:{}", request);
        Optional<AllinPayResponseDTO<APCreateMemberRespDTO>> response = AllinPayUtil.request(request, APCreateMemberRespDTO.class);
        //通联同步失败一定要抛异常回滚数据库
        Assert.isTrue(Constants.SUCCESS_CODE.equals(response.get().getStatus()), () -> {
            log.warn("用户信息同步到通联失败:{}", response);
            return BizExceptionEnum.USER_CREATION_FAILED.getMessage();
        });
        supplier.setBizUserId(memberNo);
        supplier.setAllinpayUserId(response.get().getSignedValue().getUserId());
        this.updateById(supplier);
        return new SuccessTip();
    }


    /**
     * 通联-发送验证码
     * @param allinPaySendSmsCaptchaDTO
     * @return
     */
    @Override
    public Object sendAllinPaySmsCaptcha(AllinPaySendSmsCaptchaDTO allinPaySendSmsCaptchaDTO) {

        //同步到通联
        YunRequest request = new YunRequest("MemberService", "sendVerificationCode");
        request.put("bizUserId", allinPaySendSmsCaptchaDTO.getBizUserId());
        //手机号
        request.put("phone", allinPaySendSmsCaptchaDTO.getPhone());
        //验证码类型
        request.put("verificationCodeType", allinPaySendSmsCaptchaDTO.getVerificationCodeType());

        log.info("发送验证码到通联:{}",request);
        Optional<AllinPayResponseDTO<AllinPaySendSmsCaptchaDTO>> response = AllinPayUtil.request(request, AllinPaySendSmsCaptchaDTO.class);
        //通联同步失败一定要抛异常回滚数据库
        Assert.isTrue(Constants.SUCCESS_CODE.equals(response.get().getStatus()),()->{
            log.warn("发送验证码到通联失败:{}",response);
            return BizExceptionEnum.SMS_SEND_FAILED.getMessage();
        });
        return new SuccessTip("发送成功");
    }

    /**
     * 通联-绑定手机
     * @param allinPayBindingPhoneDTO
     * @return
     */
    @Override
    public Object allinPayBindingPhone(AllinPayBindingPhoneDTO allinPayBindingPhoneDTO) {
        //同步到通联
        YunRequest request = new YunRequest("MemberService", "bindPhone");
        request.put("bizUserId", allinPayBindingPhoneDTO.getBizUserId());
        //手机号
        request.put("phone", allinPayBindingPhoneDTO.getPhone());
        //验证码
        request.put("verificationCode", allinPayBindingPhoneDTO.getVerificationCode());
        log.info("通联绑定手机接口参数:{}",request);
        Optional<AllinPayResponseDTO<AllinPayBindingPhoneDTO>> response = AllinPayUtil.request(request, AllinPayBindingPhoneDTO.class);
        //通联同步失败一定要抛异常回滚数据库
        Assert.isTrue(Constants.SUCCESS_CODE.equals(response.get().getStatus()),()->{
            log.warn("通联绑定手机接口失败:{}",response);
            return BizExceptionEnum.BINDING_PHONE_FAILED.getMessage()+",原因:"+response.get().getMessage();
        });
        Supplier supplier = selectOne(new EntityWrapper<Supplier>().eq("biz_user_id", allinPayBindingPhoneDTO.getBizUserId()));
        supplier.setPhone(response.get().getSignedValue().getPhone());
        this.updateById(supplier);
        return new SuccessTip("绑定成功");
    }

    @Override
    public Map<String, Object> getAllInfoById(Integer id) {
        Map<String,Object> map = new HashMap<>(3);
        Supplier supplier = this.selectById(id);
        Account account = iAccountService.getAccount(supplier.getBizUserId());
        Bank bank = iBankService.selectOne(new EntityWrapper<Bank>().eq("master_id",supplier.getId()).eq("type",2));
        map.put("supplier",supplier);
        map.put("account",account);
        map.put("bank",bank);
        return map;
    }

    /**
     * 实名认证
     * @param realNameAuthDTO
     * @return
     */
    @SneakyThrows
    @Override
    public Object realNameAuthentication(RealNameAuthDTO realNameAuthDTO) {

        //通联实名认证
        YunRequest request = new YunRequest("MemberService", "setRealName");
        request.put("bizUserId", realNameAuthDTO.getBizUserId());
        request.put("name", realNameAuthDTO.getName());
        request.put("identityType", realNameAuthDTO.getIdentityType());
        request.put("identityNo", RSAUtil.encrypt(realNameAuthDTO.getIdentityNo()));
        log.info("发送实名认证请求到通联:{}", request);
        Optional<AllinPayResponseDTO<RealNameAuthDTO>> response = AllinPayUtil.request(request, RealNameAuthDTO.class);
        //通联同步失败一定要抛异常回滚数据库
        Assert.isTrue(Constants.SUCCESS_CODE.equals(response.get().getStatus()), () -> {
            log.warn("发送实名认证请求到通联失败:{}", response);
            return response.get().getMessage();
        });
        log.info("更新供应商");
        Supplier supplier = this.selectOne(new EntityWrapper<Supplier>().eq("biz_user_id", realNameAuthDTO.getBizUserId()));
        supplier.setIdentityNo(realNameAuthDTO.getIdentityNo());
        supplier.setIdentityType(realNameAuthDTO.getIdentityType());
        supplier.setName(realNameAuthDTO.getName());
        this.updateById(supplier);
        return new SuccessTip("认证成功");
    }

    /**
     * 设置企业信息
     * @param setCompanyInfoDTO
     * @return
     */
    @Override
    @SneakyThrows
    public Object setCompanyInfo(SetCompanyInfoDTO setCompanyInfoDTO) {

        //通联实名认证
        YunRequest request = new YunRequest("MemberService", "setCompanyInfo");
        //商户系统用户标识
        request.put("bizUserId", setCompanyInfoDTO.getBizUserId());
        //企业会员审核结果通知
        request.put("backUrl", systemProperties.getProjectUrl()+"/allinPayAsynRespNotice/setCompanyInfo");
        //是否进行线上认证true：系统自动审核 false：需人工审核
        request.put("isAuth",true);
        JSONObject jsonArray = new JSONObject();
        //企业名称
        jsonArray.put("companyName", setCompanyInfoDTO.getName());
        //认证类型
        jsonArray.put("authType", setCompanyInfoDTO.getAuthType());
        //统一社会信用（一证）
        jsonArray.put("uniCredit", setCompanyInfoDTO.getUniCredit());
        //营业执照号（三证）
        jsonArray.put("businessLicense", setCompanyInfoDTO.getBusinessLicense());
        //组织机构代码（三证）
        jsonArray.put("organizationCode", setCompanyInfoDTO.getOrganizationCode());
        //税务登记证（三证）
        jsonArray.put("taxRegister", setCompanyInfoDTO.getTaxRegister());
        //法人姓名
        jsonArray.put("legalName", setCompanyInfoDTO.getLegalName());
        //法人证件类型
        jsonArray.put("identityType", setCompanyInfoDTO.getIdentityType());
        //法人证件号码
        jsonArray.put("legalIds", RSAUtil.encrypt(setCompanyInfoDTO.getIdentityNo()));
        //法人手机号
        jsonArray.put("legalPhone", setCompanyInfoDTO.getPhone());
        //企业对公账户
        jsonArray.put("accountNo", RSAUtil.encrypt(setCompanyInfoDTO.getAccountNo()));
        //开户银行名称如："工商银行"
        jsonArray.put("parentBankName",setCompanyInfoDTO.getParentBankMame());
        //开户行支行名称
        jsonArray.put("bankName", setCompanyInfoDTO.getBankName());
        //支付行号
        jsonArray.put("unionBank", setCompanyInfoDTO.getUnionbank());

        request.put("companyBasicInfo",jsonArray);

        log.info("发送设置企业信息请求到通联:{}", request);
        Optional<AllinPayResponseDTO<RealNameAuthDTO>> response = AllinPayUtil.request(request, RealNameAuthDTO.class);
        //通联同步失败一定要抛异常回滚数据库
        Assert.isTrue(Constants.SUCCESS_CODE.equals(response.get().getStatus()), () -> {
            log.warn("发送设置企业信息请求到通联失败:{}", response);
            return response.get().getMessage();
        });
        log.info("更新供应商");
        Supplier supplier = this.selectOne(new EntityWrapper<Supplier>().eq("biz_user_id", setCompanyInfoDTO.getBizUserId()));
        //企业名称
        supplier.setName(setCompanyInfoDTO.getName());
        //认证类型
        supplier.setAuthType(setCompanyInfoDTO.getAuthType());
        //统一社会信用（一证）
        supplier.setUniCredit(setCompanyInfoDTO.getUniCredit());
        //营业执照号（三证）
        supplier.setBusinessLicense(setCompanyInfoDTO.getBusinessLicense());
        //组织机构代码（三证）
        supplier.setOrganizationCode(setCompanyInfoDTO.getOrganizationCode());
        //税务登记证（三证）
        supplier.setTaxRegister(setCompanyInfoDTO.getTaxRegister());
        //法人姓名
        supplier.setLegalName(setCompanyInfoDTO.getLegalName());
        //法人证件类型
        supplier.setIdentityType(setCompanyInfoDTO.getIdentityType());
        //法人证件号码
        supplier.setIdentityNo(setCompanyInfoDTO.getIdentityNo());
        //法人手机号
        supplier.setPhone(setCompanyInfoDTO.getPhone());
        //企业对公账户
        supplier.setAccountNo(setCompanyInfoDTO.getAccountNo());
        //开户银行名称如：“工商银行”
        supplier.setParentBankMame(setCompanyInfoDTO.getParentBankMame());
        //开户行支行名称
        supplier.setBankName(setCompanyInfoDTO.getBankName());
        //支付行号
        supplier.setUnionbank(setCompanyInfoDTO.getUnionbank());
        this.updateById(supplier);
        return new SuccessTip("设置信息完成");
    }



    /**
     * 通联电子协议签约
     * @param bizUserId
     * @return
     */
    @Override
    public Object allinPaySignContract(String bizUserId){

        final YunRequest request = new YunRequest("MemberService", "signContract");
        Map<String, String> urlMap = new HashMap<>(1);
        request.put("bizUserId", bizUserId);
        request.put("jumpUrl", systemProperties.getProjectUrl()+"/supplier/goSignContractPageResult");
        request.put("backUrl", systemProperties.getProjectUrl()+"/allinPayAsynRespNotice/supplierAsynSignContract");
        //终端类型 [1:mobile,2:PC]
        request.put("source", "2");
        try {
            String res = YunClient.encodeOnce(request);
            String signContractUrl = allinPayProperties.getSignContractUrl()+"?"+res;
            urlMap.put("url",signContractUrl);
            return urlMap;
        }catch (Exception e){
            log.warn("编码签约通联电子协议地址异常",e);
        }

        return new ErrorTip("编码签约通联电子协议地址失败");
    }

    /**
     * 客户签约通联电子协议异步回调方法
     * @param allinPayAsynResponseDTO
     */
    @Override
    public void supplierAsynSignContract(AllinPayAsynResponseDTO allinPayAsynResponseDTO) {
        try {
            Optional<AllinPayAsynSignContractDTO> response = AllinPayUtil.response(allinPayAsynResponseDTO, AllinPayAsynSignContractDTO.class);
            AllinPayAsynSignContractDTO apasc = response.get();
            String returnValue = apasc.getReturnValue();
            JSONObject jsonObject = JSON.parseObject(returnValue);
            Supplier supplier = selectOne(new EntityWrapper<Supplier>().eq("biz_user_id", jsonObject.get("bizUserId").toString()));
            supplier.setContractNo(apasc.getContractNo());
            this.updateById(supplier);
        }catch (Exception e){
            OperationLog ol = new OperationLog();
            ol.setClassname(this.getClass().getName());
            ol.setCreatetime(new Date());
            ol.setLogname("客户签约通联电子协议异步回调方法执行异常");
            ol.setLogtype("异步回调异常");
            ol.setMessage(ToolUtil.getExceptionMsg(e));
            ol.setSucceed("fasel");
            ol.setMethod("memberAsynSignContract");
            ol.insert();
        }
    }

    /**
     * 申请绑定银行卡
     * @param bindBankDTO
     * @return
     */
    @Override
    @SneakyThrows
    public Object applyBindBankCard(BindBankDTO bindBankDTO) {

        //发送请求到通联
        YunRequest request = new YunRequest("MemberService", "applyBindBankCard");

        request.put("cardNo", RSAUtil.encrypt(bindBankDTO.getCardNo()));
        request.put("bizUserId", bindBankDTO.getBizUserId());
        request.put("phone", bindBankDTO.getPhone());
        request.put("name", bindBankDTO.getName());
        request.put("identityType", bindBankDTO.getIdentityType());
        request.put("identityNo", RSAUtil.encrypt(bindBankDTO.getIdentityNo()));
        /**
         * 收银宝快捷支付签约（有
         * 银行范围） ——支持收银宝快捷支付 ——支持提现
         */
        request.put("cardCheck", 7);

        log.info("用户发送绑卡请求到通联:{}", request);
        Optional<AllinPayResponseDTO<BankResponseDTO>> response = AllinPayUtil.request(request, BankResponseDTO.class);
        //通联同步失败一定要抛异常回滚数据库
        Assert.isTrue(Constants.SUCCESS_CODE.equals(response.get().getStatus()), () -> {
            log.warn("用户发送绑卡请求到通联失败:{}", response);
            return response.get().getMessage();
        });
        BankResponseDTO bankResponseDTO = response.get().getSignedValue();
        bankResponseDTO.setPhone(bindBankDTO.getPhone());
        bankResponseDTO.setCardNo(bindBankDTO.getCardNo());
        bankResponseDTO.setIsDefault(bindBankDTO.getIsDefault());
        return bankResponseDTO;
    }

    /**
     * 确认绑定银行卡
     * @param bindBankDTO
     * @return
     */
    @Override
    public Object bindBankCard(BindBankDTO bindBankDTO) {

        //发送请求到通联
        YunRequest request = new YunRequest("MemberService", "bindBankCard");
        request.put("bizUserId", bindBankDTO.getBizUserId());
        request.put("phone", bindBankDTO.getPhone());
        request.put("verificationCode", bindBankDTO.getVerificationCode());
        request.put("tranceNum", bindBankDTO.getTranceNum());
        log.info("用户确认绑卡请求到通联:{}", request);
        Optional<AllinPayResponseDTO<BankResponseDTO>> response = AllinPayUtil.request(request, BankResponseDTO.class);
        //通联同步失败一定要抛异常回滚数据库
        Assert.isTrue(Constants.SUCCESS_CODE.equals(response.get().getStatus()), () -> {
            log.warn("用户发送绑卡请求到通联失败:{}", response);
            return BizExceptionEnum.USER_BIND_BANK_FAILED.getMessage()+response.get().getMessage();
        });
        Supplier supplier = selectOne(new EntityWrapper<Supplier>().eq("biz_user_id", bindBankDTO.getBizUserId()));
        Bank bank = new Bank();
        //接收dto数据
        bank.setMasterId(supplier.getId());
        bank.setBankCardPro(0);
        bank.setBankCardNo(bindBankDTO.getCardNo());
        bank.setBankName(bindBankDTO.getBankName());
        bank.setCardType(bindBankDTO.getCardType());
        bank.setBankCode(bindBankDTO.getBankCode());
        bank.setType(2);
        bank.setPhone(bindBankDTO.getPhone());
        bank.setIsDefault(bindBankDTO.getIsDefault());
        bank.setBizUserId(bindBankDTO.getBizUserId());
        if(bindBankDTO.getIsDefault()==1){
            log.info("去掉默认银行卡");
            Bank updateData = new Bank();
            updateData.setIsDefault(0);
            iBankService.update(updateData,new EntityWrapper<Bank>().eq("master_id",supplier.getId()).eq("type",2));
        }
        log.info("用户绑定银行卡信息插入数据库", bank);
        bank.insert();
        return new SuccessTip("绑卡成功");
    }

    @SneakyThrows
    @Override
    public Object relieveBindingBankCard(Integer id) {
        //发送请求到通联
        YunRequest request = new YunRequest("MemberService", "unbindBankCard");
        Bank bank = iBankService.selectById(id);
        request.put("cardNo", RSAUtil.encrypt(bank.getBankCardNo()));
        request.put("bizUserId", bank.getBizUserId());
        log.info("用户解除绑定银行卡请求到通联:{}", request);
        Optional<AllinPayResponseDTO<BankResponseDTO>> response = AllinPayUtil.request(request, BankResponseDTO.class);
        //通联同步失败一定要抛异常回滚数据库
        Assert.isTrue(Constants.SUCCESS_CODE.equals(response.get().getStatus()), () -> {
            log.warn("用户解除绑定银行卡请求到通联失败:{}", response);
            return response.get().getMessage();
        });
        iBankService.deleteById(id);
        return new SuccessTip("解除成功");
    }

    /**
     * 分页查询供应商
     * @param page
     * @param supplier
     * @return
     */
    @Override
    public Page<SupplierVO> selectPageInfo(Page<SupplierVO> page, Supplier supplier) {

        List<SupplierVO> list = supplierMapper.selectPageInfo(page,supplier);
        page.setRecords(list);
        return page;
    }

    /**
     * 锁定/解锁会员
     * @param id
     * @return
     */
    @Override
    public Object lockOrUnlockMember(Integer id) {

        //发送请求到通联
        Supplier supplier = this.selectById(id);
        Integer state = supplier.getState();
        YunRequest request = new YunRequest("MemberService", state==0?"lockMember":"unlockMember");
        request.put("bizUserId", supplier.getBizUserId());
        log.info("用户"+(state==0?"锁定":"解锁")+"会员请求到通联:{}", request);
        Optional<AllinPayResponseDTO<BankResponseDTO>> response = AllinPayUtil.request(request, BankResponseDTO.class);
        //通联同步失败一定要抛异常回滚数据库
        Assert.isTrue(Constants.SUCCESS_CODE.equals(response.get().getStatus()), () -> {
            log.warn("用户"+(state==0?"锁定":"解锁")+"会员请求到通联失败:{}", response);
            return response.get().getMessage();
        });
        supplier.setState(state==0?1:0);
        this.updateById(supplier);
        return new SuccessTip((state==0?"锁定":"解锁")+"成功");
    }

    /**
     * 生成支付密码操作url
     * @param id
     * @return
     */
    @Override
    @SneakyThrows
    public Map<String, String> generatePayPwdOptUrl(Integer id) {
        Supplier supplier = this.selectById(id);
        final YunRequest setRequest = new YunRequest("MemberPwdService", "setPayPwd");
        final YunRequest updateRequest = new YunRequest("MemberPwdService", "updatePayPwd");
        final YunRequest resetRequest = new YunRequest("MemberPwdService", "resetPayPwd");
        Map<String, String> urlMap = new HashMap<>(3);

        setRequest.put("bizUserId", supplier.getBizUserId());
        updateRequest.put("bizUserId", supplier.getBizUserId());
        resetRequest.put("bizUserId", supplier.getBizUserId());

        setRequest.put("phone", supplier.getPhone());
        updateRequest.put("phone", supplier.getPhone());
        resetRequest.put("phone", supplier.getPhone());

        setRequest.put("name", supplier.getName());
        updateRequest.put("name", supplier.getName());
        resetRequest.put("name", supplier.getName());

        setRequest.put("identityType", supplier.getIdentityType());
        updateRequest.put("identityType", supplier.getIdentityType());
        resetRequest.put("identityType", supplier.getIdentityType());

        setRequest.put("identityNo", RSAUtil.encrypt(supplier.getIdentityNo()));
        updateRequest.put("identityNo", RSAUtil.encrypt(supplier.getIdentityNo()));
        resetRequest.put("identityNo", RSAUtil.encrypt(supplier.getIdentityNo()));

        setRequest.put("jumpUrl", systemProperties.getProjectUrl()+"/supplier/goSetPayPwdResult");
        updateRequest.put("jumpUrl", systemProperties.getProjectUrl()+"/supplier/goUpdatePayPwdResult");
        resetRequest.put("jumpUrl", systemProperties.getProjectUrl()+"/supplier/goResetPayPwdResult");

        setRequest.put("backUrl", systemProperties.getProjectUrl()+"/allinPayAsynRespNotice/supplierAsynSetPayPwd");
        updateRequest.put("backUrl", systemProperties.getProjectUrl()+"/allinPayAsynRespNotice/supplierAsynUpdatePayPwd");
        resetRequest.put("backUrl", systemProperties.getProjectUrl()+"/allinPayAsynRespNotice/supplierAsynResetPayPwd");

        try {
            String setRes = YunClient.encodeOnce(setRequest);
            String updateRes = YunClient.encodeOnce(updateRequest);
            String resetRes = YunClient.encodeOnce(resetRequest);

            String setUrl = allinPayProperties.getSetPayPwdUrl()+"?"+setRes;
            String updateUrl = allinPayProperties.getUpdatePayPwdUrl()+"?"+updateRes;
            String resetUrl = allinPayProperties.getResetPayPwdUrl()+"?"+resetRes;

            urlMap.put("setUrl",setUrl);
            urlMap.put("updateUrl",updateUrl);
            urlMap.put("resetUrl",resetUrl);
            return urlMap;
        }catch (Exception e){
            log.warn("编码支付密码地址异常",e);
            return urlMap;
        }
    }

    /**
     * 充值申请
     * @param cwar
     * @return
     */
    @Override
    @SneakyThrows
    public Map<String,String> depositApply(DepositApplyRequestDTO cwar) {

        Supplier supplier = this.selectById(cwar.getId());
        final YunRequest request = new YunRequest("OrderService", "depositApply");
        // 充值订单号
        String orderId = NoUtil.generateCode(BizTypeEnum.DEPOSIT_MONEY, UserTypeEnum.SUPPLIER, cwar.getId());
        request.put("bizOrderNo",orderId);
        request.put("bizUserId",supplier.getBizUserId());
        //账户集编号
        request.put("accountSetNo",allinPayProperties.getAccountSetNo());
        //订单金额
        Integer amount = MoneyFormatTester.bigDecimal2Long(cwar.getAmount()).intValue();
        request.put("amount", amount);
        //手续费=0
        request.put("fee",0);
        //交易验证方式 0:不验证,1:短信验证码,2:支付密码
        request.put("validateType",0);
        //前台跳转地址
        request.put("frontUrl",systemProperties+"/supplier/goDepositResultPage");
        //后台通知地址
        request.put("backUrl",systemProperties.getProjectUrl()+"/allinPayAsynRespNotice/deposit");

        JSONObject payMethod = new JSONObject();
        JSONObject subPayMethod = new JSONObject();
        //1:网关支付,2:快捷支付,3:支付宝,4:微信
        if(cwar.getPayMethod()==1){
            subPayMethod.put("amount",amount);
            //“B2C”,“B2B”,“B2C,B2B
            subPayMethod.put("paytype",cwar.getPayType());
            request.put("frontUrl", systemProperties.getProjectUrl()+"/supplier/goDepositResultPage");
            payMethod.put("GATEWAY_VSP",subPayMethod);
            request.put("extendInfo","网关充值");

        }else if(cwar.getPayMethod()==2){
            subPayMethod.put("bankCardNo",RSAUtil.encrypt(cwar.getBankCardNo()));
            subPayMethod.put("amount",amount);
            payMethod.put("QUICKPAY_VSP",subPayMethod);
            request.put("extendInfo","快捷充值");
        }else if(cwar.getPayMethod()==3){
//            payMethod.put("subAppid", allinPayProperties.getSubAppId());
//            payMethod.put("amount", amount);
//            payMethod.put("acct", cwar.getWechatOpenId());
//            payMethod.put("limitPay", "");
//            payMethod.put("cusip",cwar.getVisitorIp());
//            payMethod.put("sceneInfo","{\"h5_info\":\"{\"type\":\"Wap\",\"wap_url\": \""+systemProperties.getProjectUrl()+"\",\"wap_name\": \"爱赛充值\"}}");
//            payMethod.put("WECHATPAY_H5_OPEN",subPayMethod);
            subPayMethod.put("limitPay", "");
            //todo subAppid非必传 wxk
//            subPayMethod.put("subAppid", allinPayProperties.getSubAppId());
            subPayMethod.put("amount", amount);
            subPayMethod.put("acct", cwar.getWechatOpenId());
            payMethod.put("WECHAT_PUBLIC",subPayMethod);
            request.put("extendInfo","微信充值");
        }

        //支付方式
        request.put("payMethod", payMethod);
        //行业代码
        request.put("industryCode",allinPayCodeProperties.getOtherCode());
        //行业名称
        request.put("industryName",allinPayCodeProperties.getOtherName());
        //访问终端类型 1:手机端,2:PC端
        request.put("source",2);
        log.info("用户充值申请请求到通联:{}", request);
        Optional<AllinPayResponseDTO<AllinPayDepositApplyResponseDTO>> response = AllinPayUtil.request(request, AllinPayDepositApplyResponseDTO.class);
        //通联同步失败一定要抛异常回滚数据库

        if(!Constants.SUCCESS_CODE.equals(response.get().getStatus())){
            log.warn("用户充值申请请求到通联失败:{}", response);
            throw  new SystemException(response.get().getMessage());
        }

        AllinPayDepositApplyResponseDTO signedValue = response.get().getSignedValue();

       if(Constants.STATUS_FAIL_CODE.equals(signedValue.getPayStatus())){
            log.warn("用户充值申请请求到通联失败:{}", response);
           throw  new SystemException(signedValue.getPayFailMessage());
       }
        redisUtil.set(signedValue.getBizOrderNo(),0,3600);

        Map<String,String> map = new HashMap(3);

       //网关支付
       if(cwar.getPayMethod()==1){
           String url = openConfirmPaymentPage(supplier.getBizUserId(),signedValue.getBizOrderNo(),cwar.getJumpUrl());
           map.put("url",url);
           //
       }else if(cwar.getPayMethod()==2) {
           map.put("bizUserId", signedValue.getBizUserId());
           map.put("bizOrderNo", signedValue.getBizOrderNo());
           map.put("amount", cwar.getAmount().toString());
           map.put("supplierId", supplier.getId().toString());
       }
        map.put("payMethod", cwar.getPayMethod().toString());
        return map;
    }


    /**
     * 跳转支付确认页面
     * @param bizUserId
     * @param bizOrderNo
     * @return
     */
    @SneakyThrows
    public String openConfirmPaymentPage(String bizUserId,String bizOrderNo,String jumpUrl) {
        final YunRequest request = new YunRequest("OrderService", "pay");
        request.put("bizUserId",bizUserId);
        request.put("bizOrderNo",bizOrderNo);
        request.put("jumpUrl",jumpUrl);
        request.put("consumerIp",Constants.CUSTOMER_IP);
        String setRes = YunClient.encodeOnce(request);
        return allinPayProperties.getPayOrderUrl()+"?"+setRes;
    }

    /**
     * 提现申请
     * @param cwar
     * @return
     */
    @Override
    @SneakyThrows
    public Tip cashWithdrawalApply(CashWithdrawalApplyRequestDTO cwar) {
        log.info("供应商提现申请参数:{}",cwar);
        Supplier supplier = this.selectById(cwar.getId());
        EntityWrapper<Account> accountEW = new EntityWrapper();
        accountEW.eq("master_id",supplier.getId()).eq("type",2);

        //通联限额
        BigDecimal allinpayQuota = new BigDecimal(Constants.ALLINPAY_QUOTA);
        //本次提现总金额
        BigDecimal totleAmount = cwar.getAmount();
        //剩余额度
        BigDecimal surplusAmount = cwar.getAmount();
        //本次提现金额
        BigDecimal tempAmount;

        //由于通联提现限额5000，所以多次调用提现接口提现
        while (surplusAmount.compareTo(new BigDecimal(0)) >0){

            //剩余提现金额>限额
            if(surplusAmount.compareTo(allinpayQuota) >0){
                surplusAmount = totleAmount.subtract(allinpayQuota);
                tempAmount = allinpayQuota;
            }else{//剩余提现金额<限额
                tempAmount = surplusAmount;
                surplusAmount = new BigDecimal(0);
            }

            log.info("通联限额:{},本次提现总金额:{},本次提现金额:{},剩余提现金额:{}",allinpayQuota,totleAmount,tempAmount,surplusAmount);

            final YunRequest request = new YunRequest("OrderService", "withdrawApply");
            // 提现申请号
            String orderId = NoUtil.generateCode(BizTypeEnum.CASH_WITHDRAWAL, UserTypeEnum.SUPPLIER, cwar.getId());
            request.put("bizOrderNo",orderId);
            request.put("bizUserId",supplier.getBizUserId());
            //账户集编号
            request.put("accountSetNo",allinPayProperties.getAccountSetNo());
            //订单金额
            Long amount = MoneyFormatTester.bigDecimal2Long(tempAmount);
            request.put("amount", amount.intValue());
            //手续费
            int serviceCharge = new BigDecimal(amount * 0.002).intValue();
            //至少收2元
            if(serviceCharge < Constants.MIN_SERVICE_CHARGE){
                serviceCharge = Constants.MIN_SERVICE_CHARGE;
            }
            request.put("fee",serviceCharge);
            //交易验证方式 0:不验证,1:短信验证码,2:支付密码
            request.put("validateType",0);
            //后台通知地址
            request.put("backUrl",systemProperties.getProjectUrl()+"/allinPayAsynRespNotice/withdrawal");
            //银行卡号/账号
            request.put("bankCardNo",RSAUtil.encrypt(cwar.getBankCardNo()));
            //行业代码
            request.put("industryCode",allinPayCodeProperties.getOtherCode());
            //行业名称
            request.put("industryName",allinPayCodeProperties.getOtherName());
            //访问终端类型 1:手机端,2:PC端
            request.put("source",2);
            request.put("extendInfo","到账");
            log.info("用户提现申请请求到通联:{}", request);
            Optional<AllinPayResponseDTO<AllinPayCashWithdrawalApplyResponseDTO>> response = AllinPayUtil.request(request, AllinPayCashWithdrawalApplyResponseDTO.class);

            if(!Constants.SUCCESS_CODE.equals(response.get().getStatus())){
                log.warn("用户提现申请请求到通联失败:{}", response);
                throw  new SystemException(response.get().getMessage());
            }

            AllinPayCashWithdrawalApplyResponseDTO signedValue = response.get().getSignedValue();

            if(Constants.STATUS_FAIL_CODE.equals(signedValue.getPayStatus())){
                log.warn("用户提现申请请求到通联失败:{}", response);
                throw  new SystemException(signedValue.getPayFailMessage());
            }

            redisUtil.incr(signedValue.getBizOrderNo(),0);
        }
        SuccessTip successTip = new SuccessTip();
        successTip.setMessage("提现成功,请稍后查询");
        return successTip;
    }


    /**
     * 充值支付确认
     * @param depositConfirmRequestDTO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Tip depositConfirmPayment(DepositConfirmRequestDTO depositConfirmRequestDTO) {
        log.info("充值支付确认方法:{}",depositConfirmRequestDTO);

        final YunRequest request = new YunRequest("OrderService", "pay");

        // 提现申请号
        String orderId = NoUtil.generateCode(BizTypeEnum.DEPOSIT_MONEY, UserTypeEnum.SUPPLIER, depositConfirmRequestDTO.getSupplierId());
        //商户系统用户标识，
        request.put("bizUserId",depositConfirmRequestDTO.getBizUserId());
        //订单申请的商户订单号
        request.put("bizOrderNo",depositConfirmRequestDTO.getBizOrderNo());
        //交易编号
        request.put("tradeNo",orderId);

        request.put("consumerIp",Constants.CUSTOMER_IP);
        //短信验证码
        request.put("verificationCode",depositConfirmRequestDTO.getVerificationCode());

        log.info("用户充值支付确认请求到通联:{}", request);
        Optional<AllinPayResponseDTO<AllinPayDepositConfirmApplyResponseDTO>> response = AllinPayUtil.request(request, AllinPayDepositConfirmApplyResponseDTO.class);

        if(!Constants.SUCCESS_CODE.equals(response.get().getStatus())){
            log.warn("用户充值确认请求到通联失败:{}", response);
            throw  new SystemException(response.get().getMessage());
        }

        AllinPayDepositConfirmApplyResponseDTO signedValue = response.get().getSignedValue();
        if(Constants.STATUS_FAIL_CODE.equals(signedValue.getPayStatus())){
            log.warn("用户充值申请请求到通联失败:{}", response);
            throw  new SystemException(signedValue.getPayFailMessage());
        }

        SuccessTip successTip = new SuccessTip();
        successTip.setMessage("充值成功,请稍后查询");
        return successTip;
    }




    /**
     * 查询企业信息通联审核结果
     * @param bizUserId
     * @return
     */
    @Override
    public Object companyVerifyResult(String bizUserId) {
        YunRequest request = new YunRequest("MemberService","getMemberInfo");
        request.put("bizUserId", bizUserId);
        log.info("查询企业信息通联审核结果:{}", request);
        Optional<AllinPayResponseDTO<AllinPayQueryCompanyInfoDTO>> response = AllinPayUtil.request(request, AllinPayQueryCompanyInfoDTO.class);
        //通联同步失败一定要抛异常回滚数据库
        Assert.isTrue(Constants.SUCCESS_CODE.equals(response.get().getStatus()), () -> {
            log.warn("查询企业信息通联审核结果失败:{}", response);
            return response.get().getMessage();
        });
        AllinPayQueryCompanyInfoDTO signedValue = response.get().getSignedValue();
        JSONObject jsonObject = JSON.parseObject(signedValue.getMemberInfo());
        Integer status = Integer.valueOf(jsonObject.get("status").toString());
        String failReason = jsonObject.get("failReason")==null?"无":jsonObject.get("failReason").toString();
        Supplier supplier = this.selectOne(new EntityWrapper<Supplier>().eq("biz_user_id", bizUserId));
        supplier.setAllinPayState(status);
        String result ;
        if(status==1){
            result="待审核";
        }else if(status==2){
            result="审核成功";
        }else{
            result = "审核失败:"+failReason;
            supplier.setFailReason(failReason);
        }
        this.updateById(supplier);
        return new SuccessTip(result);
    }


}
