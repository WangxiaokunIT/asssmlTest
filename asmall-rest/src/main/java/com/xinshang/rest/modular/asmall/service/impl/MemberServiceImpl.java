package com.xinshang.rest.modular.asmall.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.allinpay.yunst.sdk.YunClient;
import com.allinpay.yunst.sdk.bean.YunRequest;
import com.allinpay.yunst.sdk.util.RSAUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xinshang.core.enums.BizTypeEnum;
import com.xinshang.core.enums.UserTypeEnum;
import com.xinshang.core.exception.SystemException;
import com.xinshang.core.util.NoUtil;
import com.xinshang.core.util.ToolUtil;
import com.xinshang.rest.common.enums.BizExceptionEnum;
import com.xinshang.rest.common.enums.CommonConstants;
import com.xinshang.rest.common.util.*;
import com.xinshang.rest.config.constant.Constants;
import com.xinshang.rest.config.properties.AllinPayCodeProperties;
import com.xinshang.rest.config.properties.AllinPayProperties;
import com.xinshang.rest.config.properties.RestProperties;
import com.xinshang.rest.modular.asmall.dao.AccountMapper;
import com.xinshang.rest.modular.asmall.dao.BankMapper;
import com.xinshang.rest.modular.asmall.dao.ClientLogMoneysMapper;
import com.xinshang.rest.modular.asmall.dao.MemberMapper;
import com.xinshang.rest.modular.asmall.dto.*;
import com.xinshang.rest.modular.asmall.model.*;
import com.xinshang.rest.modular.asmall.service.*;
import com.xinshang.rest.modular.asmall.util.MoneyFormatTester;
import com.xinshang.rest.modular.asmall.vo.BankVO;
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
 * 用户表 服务实现类
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-10-19
 */
@Service
@Slf4j
@AllArgsConstructor
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements IMemberService {

    private final MemberMapper memberMapper;
    private final BankMapper bankMapper;
    private final JwtTokenUtil jwtTokenUtil;
    private final AllinPayProperties allinPayProperties;
    private final IOperationLogService iOperationLogService;
    private final RestProperties restProperties;
    private final AccountMapper accountMapper;
    private final AllinPayCodeProperties allinPayCodeProperties;
    private final IAccountService iAccountService;
    private final ClientLogMoneysMapper clientLogMoneysMapper;
    private final RedisUtil redisUtil;


    /**
     * 根据ID查询用户信息
     *
     * @param id
     * @return
     */
    @Override
    public BankVO selectDetailbyId(Long id) {
        BankVO bankVO = memberMapper.selectDetailbyId(id);
        Account account = getAccount(bankVO.getBizUserId());
        bankVO.setAvailableBalance(account.getAvailableBalance());
        return bankVO;
    }

    /**
     * 手机号认证用户
     *
     * @param authCaptchaRequestDTO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<AuthResponseDTO> memberAuth(AuthCaptchaRequestDTO authCaptchaRequestDTO) {
        //根据手机号查询用户
        Member dbMember = selectOne(new EntityWrapper<Member>().eq("username", authCaptchaRequestDTO.getPhoneNo()));
        Member member = Optional.ofNullable(dbMember).orElseGet(() -> {
            Member temp = new Member();
            temp.setUsername(authCaptchaRequestDTO.getPhoneNo());
            //状态:[1:正常,0:禁用]
            temp.setState(1);
            log.info("插入数据库:{}", temp);
            memberMapper.insert(temp);
            //同步到通联
            YunRequest request = new YunRequest("MemberService", "createMember");
            String memberNo = NoUtil.generateCode(BizTypeEnum.MEMBER_NO, UserTypeEnum.CUSTOMER, temp.getId());
            request.put("bizUserId", memberNo);
            //会员类型:[3:个人会员,2:企业会员]
            request.put("memberType", 3);
            //访问终端类型:[1:Mobile,2:PC]
            request.put("source", 1);
            log.info("用户信息同步到通联:{}", request);
            Optional<AllinPayResponseDTO<APCreateMemberRespDTO>> response = AllinPayUtil.request(request, APCreateMemberRespDTO.class);
            //通联同步失败一定要抛异常回滚数据库
            Assert.isTrue(CommonConstants.SUCCESS_CODE.equals(response.get().getStatus()), () -> {
                log.warn("用户信息同步到通联失败:{}", response);
                return BizExceptionEnum.USER_CREATION_FAILED.getMessage();
            });
            temp.setBizUserId(memberNo);
            //默认创建用户实名认证状态为0[未认证]
            temp.setRealNameState(0);
            temp.setAllinpayUserId(response.get().getSignedValue().getUserId());
            memberMapper.updateById(temp);
            return temp;
        });

        //判断用户状态是否被禁用
        Assert.isTrue(CommonConstants.USER_STATUS_ENABLE_CODE.equals(member.getState()), BizExceptionEnum.USER_IS_NOT_ALLOWED_TO_LOG_IN.getMessage());
        String randomKey = jwtTokenUtil.getRandomKey();
        String token = jwtTokenUtil.generateToken(authCaptchaRequestDTO.getPhoneNo(), randomKey);
        return R.ok(new AuthResponseDTO(token, randomKey));
    }


    /**
     * 微信认证用户
     *
     * @param weChatUserInfoDTO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<AuthResponseDTO> memberWeChatAuth(WeChatUserInfoDTO weChatUserInfoDTO) {
        //根据手机号查询用户
        Member dbMember = selectOne(new EntityWrapper<Member>().eq("openid", weChatUserInfoDTO.getOpenid()));
        Member member = Optional.ofNullable(dbMember).orElseGet(() -> {
            Member temp = new Member();
            temp.setNickname(weChatUserInfoDTO.getNickname());
            temp.setOpenid(weChatUserInfoDTO.getOpenid());
            temp.setFile(weChatUserInfoDTO.getHeadimgurl());
            //状态:[1:正常,0:禁用]
            temp.setState(1);
            log.info("插入数据库:{}", temp);
            memberMapper.insert(temp);
            //同步到通联
            YunRequest request = new YunRequest("MemberService", "createMember");
            String memberNo = NoUtil.generateCode(BizTypeEnum.MEMBER_NO, UserTypeEnum.CUSTOMER, temp.getId());
            request.put("bizUserId", memberNo);
            //会员类型:[3:个人会员,2:企业会员]
            request.put("memberType", 3);
            //访问终端类型:[1:Mobile,2:PC]
            request.put("source", 1);
            log.info("用户信息同步到通联:{}", request);
            Optional<AllinPayResponseDTO<APCreateMemberRespDTO>> response = AllinPayUtil.request(request, APCreateMemberRespDTO.class);
            //通联同步失败一定要抛异常回滚数据库
            Assert.isTrue(CommonConstants.SUCCESS_CODE.equals(response.get().getStatus()), () -> {
                log.warn("用户信息同步到通联失败:{}", response);
                return BizExceptionEnum.USER_CREATION_FAILED.getMessage();
            });
            temp.setBizUserId(memberNo);
            //默认创建用户实名认证状态为0[未认证]
            temp.setRealNameState(0);
            temp.setAllinpayUserId(response.get().getSignedValue().getUserId());
            memberMapper.updateById(temp);
            return temp;
        });
        //判断用户状态是否被禁用
        Assert.isTrue(CommonConstants.USER_STATUS_ENABLE_CODE.equals(member.getState()), BizExceptionEnum.USER_IS_NOT_ALLOWED_TO_LOG_IN.getMessage());
        String randomKey = jwtTokenUtil.getRandomKey();
        String token = jwtTokenUtil.generateToken(weChatUserInfoDTO.getOpenid(), randomKey);
        return R.ok(new AuthResponseDTO(token, randomKey));
    }


    /**
     * 实名认证
     *
     * @param member
     * @param dto
     * @return
     * @throws Exception
     */
    @Override
    public R<Object> updateMember(Member member, MemberDTO dto) {
        //判断当前用户实名认证状态，未实名认证进行接口请求认证状态
        if (member.getRealNameState() != 1) {
            //通联实名认证
            YunRequest request = new YunRequest("MemberService", "setRealName");
            request.put("bizUserId", member.getBizUserId());
            request.put("name", dto.getRealName());
            request.put("identityType", dto.getDocumenType());
            try {
                request.put("identityNo", RSAUtil.encrypt(dto.getCardNumber()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            log.info("发送实名认证请求到通联:{}", request);
            Optional<AllinPayResponseDTO<MemberResponseDTO>> response = AllinPayUtil.request(request, MemberResponseDTO.class);
            Assert.isTrue(CommonConstants.SUCCESS_CODE.equals(response.get().getStatus()), () -> {
                log.warn("发送实名认证请求到通联失败:{}", response);
                return BizExceptionEnum.USER_SET_REAL_FAILD.getMessage();
            });
            //接口请求成功，接收dto数据插入数据库
            member.setDocumenType(dto.getDocumenType());
            member.setFile(dto.getFile());
            member.setRealName(dto.getRealName());
            member.setCardNumber(dto.getCardNumber());
            member.setNickname(dto.getNickname());
            //状态设置已实名认证
            member.setRealNameState(1);
            Integer update = memberMapper.update(member, new EntityWrapper().eq("username", member.getUsername()));
            if (update == 1) {
                return R.ok("信息补全成功");
            }
            return R.failed(400, "信息补全失败");
        } else {
            //已实名认证用户只允许修改头像
            member.setFile(dto.getFile());
            member.setNickname(dto.getNickname());
            Integer update = memberMapper.update(member, new EntityWrapper().eq("username", member.getUsername()));
            if (update == 1) {
                return R.ok("个人信息更新成功");
            }
            return R.failed(400, "个人信息更新失败");
        }
    }


    /**
     * 申请绑定银行卡
     *
     * @param member
     * @param dto
     * @return
     */
    @Override
    public R<Object> applyBindBankCard(Member member, BindBankDTO dto) {

        //发送请求到通联
        YunRequest request = new YunRequest("MemberService", "applyBindBankCard");
        try {
            request.put("cardNo", RSAUtil.encrypt(dto.getBankCardNo()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        request.put("bizUserId", member.getBizUserId());
        request.put("phone", dto.getPhone());
        request.put("name", dto.getRealName());
        request.put("identityType", member.getDocumenType());
        /**
         * 收银宝快捷支付签约（有
         * 银行范围） ——支持收银宝快捷支付 ——支持提现
         */
        request.put("cardCheck", 7);
        try {
            request.put("identityNo", RSAUtil.encrypt(member.getCardNumber()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("用户发送绑卡请求到通联:{}", request);
        Optional<AllinPayResponseDTO<BankResponseDTO>> response = AllinPayUtil.request(request, BankResponseDTO.class);
        //通联同步失败一定要抛异常回滚数据库
        Assert.isTrue(CommonConstants.SUCCESS_CODE.equals(response.get().getStatus()), () -> {
            log.warn("用户发送绑卡请求到通联失败:{}", response);
            return response.get().getMessage();
        });

        return R.ok(response.get().getSignedValue());
    }

    /**
     * 确认绑定
     *
     * @param member
     * @param dto
     * @return
     */
    @Override
    public R<Object> bindBankCard(Member member, BindBankDTO dto) {

        //发送请求到通联
        YunRequest request = new YunRequest("MemberService", "bindBankCard");
        request.put("bizUserId", member.getBizUserId());
        request.put("phone", dto.getPhone());
        request.put("tranceNum", dto.getTranceNum());
        request.put("verificationCode", dto.getVerificationCode());
        log.info("用户确认绑卡请求到通联:{}", request);
        Optional<AllinPayResponseDTO<BankResponseDTO>> response = AllinPayUtil.request(request, BankResponseDTO.class);
        //通联同步失败一定要抛异常回滚数据库
        Assert.isTrue(CommonConstants.SUCCESS_CODE.equals(response.get().getStatus()), () -> {
            log.warn("用户发送绑卡请求到通联失败:{}", response);
            return BizExceptionEnum.USER_BIND_BANK_FAILED.getMessage();
        });
        Bank bank = new Bank();
        //接收dto数据
        bank.setMasterId(member.getId().intValue());
        bank.setBankCardNo(dto.getBankCardNo());
        bank.setBankName(dto.getBankName());
        bank.setCardType(1);
        bank.setBankCardPro(0);
        bank.setBankCode(dto.getBankCode());
        bank.setPhone(dto.getPhone());
        bank.setIsDefault(0);
        bank.setCreateTime(new Date());
        bank.setType(1);
        member.setIsSetBankState(1);
        //更新客户绑卡信息
        memberMapper.updateById(member);
        //插入数据库
        log.info("用户绑定银行卡信息", bank);
        Integer insert = bankMapper.insert(bank);
        if (insert != 0) {
            return R.ok();
        } else {
            return R.failed(400, "绑卡失败");
        }
    }

    /**
     * 获取绑定银行卡列表
     *
     * @param member
     * @return
     */
    @Override
    public List<Bank> bindBankCardList(Member member) {
        Wrapper<Bank> wrapper = new EntityWrapper<>();
        wrapper.eq("master_id", member.getId());
        wrapper.eq("type", 1);
        return bankMapper.selectList(wrapper);
    }

    /**
     * 解除绑定银行卡
     *
     * @param member
     * @param bindBankDTO
     * @return
     */
    @Override
    public R<Object> unbindBankCard(Member member, BindBankDTO bindBankDTO) {
        Bank bank = bankMapper.selectById(bindBankDTO.getBankId());
        //发送请求到通联
        YunRequest request = new YunRequest("MemberService", "unbindBankCard");
        request.put("bizUserId", member.getBizUserId());
        try {
            request.put("cardNo", RSAUtil.encrypt(bank.getBankCardNo()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("用户解除绑卡到通联:{}", request);
        Optional<AllinPayResponseDTO<BankResponseDTO>> response = AllinPayUtil.request(request, BankResponseDTO.class);
        //通联同步失败一定要抛异常回滚数据库
        Assert.isTrue(CommonConstants.SUCCESS_CODE.equals(response.get().getStatus()), () -> {
            log.warn("用户发送解绑请求到通联失败:{}", response);
            return BizExceptionEnum.USER_UNBIND_BANK_FAILED.getMessage();
        });
        //删除数据库保存该银行卡信息
        Integer delete = bankMapper.delete(new EntityWrapper().eq("id", bindBankDTO.getBankId()));
        // 同步到数据库,查找该卡是否是多张银行卡之一，
        List<Bank> banks = bindBankCardList(member);
        if (banks.size() <= 0) {
            member.setIsSetBankState(0);
        }
        member.update(new EntityWrapper().eq("id", member.getId()));
        if (delete == 1) {
            return R.ok("解绑成功");
        }
        return R.failed(400, "解除绑定失败");
    }

    /**
     * 设置支付密码
     *
     * @param member
     * @return
     */
    @Override
    public R<Object> setPayPwd(Member member) {
        //判断是否实名认证
        if (member.getRealNameState() == 1) {
            //发送请求到通联
            final YunRequest request = new YunRequest("MemberPwdService", "setPayPwd");
            Map<String, String> urlMap = new HashMap<>(1);
            request.put("bizUserId", member.getBizUserId());
            request.put("phone", member.getPhone());
            request.put("name", member.getRealName());
            request.put("identityType", member.getDocumenType());
            try {
                request.put("identityNo", RSAUtil.encrypt(member.getCardNumber()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            request.put("jumpUrl", member.getJumpUrl());
            request.put("backUrl", restProperties.getProjectUrl() + "/allinPayAsynRespNotice/memberSetPwdContract");
            log.info("用户请求设置支付密码到通联:{}", request);
            try {
                String res = YunClient.encodeOnce(request);
                String setUrl = allinPayProperties.getSetPayPwdUrl() + "?" + res;
                urlMap.put("url", setUrl);
                return R.ok(urlMap);
            } catch (Exception e) {
                log.warn("设置支付密码异常", e);
            }
        }
        return R.failed(400, "请先完成实名认证");
    }

    /**
     * 设置支付密码异步回调方法
     *
     * @param allinPayAsynResponseDTO
     */
    @Override
    public void memberAsynSetPwdContract(AllinPayAsynResponseDTO allinPayAsynResponseDTO) {

        Optional<AllinPayAsynSetPwdContractDTO> response = AllinPayUtil.response(allinPayAsynResponseDTO, AllinPayAsynSetPwdContractDTO.class);
        AllinPayAsynSetPwdContractDTO apasc = response.get();
        String returnValue = apasc.getReturnValue();
        JSONObject jsonObject = JSON.parseObject(returnValue);
        Member member = selectOne(new EntityWrapper<Member>().eq("biz_user_id", jsonObject.get("bizUserId").toString()));
        member.setSetPwdState(1);
        member.update(new EntityWrapper().eq("id", member.getId()));

    }

    /**
     * 修改支付密码
     *
     * @param member
     * @return
     */
    @Override
    public R<Object> updatePayPwd(Member member) {
        //判断是否实名认证
        if (member.getRealNameState() == 1) {
            //发送请求到通联
            final YunRequest request = new YunRequest("MemberPwdService", "updatePayPwd");
            Map<String, String> urlMap = new HashMap<>(1);
            request.put("bizUserId", member.getBizUserId());
            request.put("name", member.getRealName());
            request.put("identityType", member.getDocumenType());
            try {
                request.put("identityNo", RSAUtil.encrypt(member.getCardNumber()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            //跳转返回页面地址
            request.put("jumpUrl", member.getJumpUrl());
            request.put("backUrl", restProperties.getProjectUrl() + "/allinPayAsynRespNotice/memberUpdatePwdContract");
            log.info("用户请求修改支付密码到通联:{}", request);
            try {
                String res = YunClient.encodeOnce(request);
                String callbackUrl = allinPayProperties.getUpdatePayPwdUrl() + "?" + res;
                urlMap.put("url", callbackUrl);
                return R.ok(urlMap);
            } catch (Exception e) {
                log.warn("修改支付密码异常", e);
            }
        }
        return R.failed(400, "请先完成实名认证");
    }

    /**
     * 修改支付密码异步回调方法
     *
     * @param allinPayAsynResponseDTO
     */
    @Override
    public void memberAsynUpdatePwdContract(AllinPayAsynResponseDTO allinPayAsynResponseDTO) {

    }

    /**
     * 重置支付密码
     *
     * @param member
     * @return
     */
    @Override
    public R<Object> resetPayPwd(Member member) {
        //判断是否实名认证
        if (member.getRealNameState() == 1) {
            //发送请求到通联
            final YunRequest request = new YunRequest("MemberPwdService", "resetPayPwd");
            Map<String, String> urlMap = new HashMap<>(1);
            request.put("bizUserId", member.getBizUserId());
            request.put("name", member.getRealName());
            request.put("phone", member.getPhone());
            request.put("identityType", member.getDocumenType());
            try {
                request.put("identityNo", RSAUtil.encrypt(member.getCardNumber()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            //跳转返回页面地址
            request.put("jumpUrl", member.getJumpUrl());
            request.put("backUrl", restProperties.getProjectUrl() + "/allinPayAsynRespNotice/memberResetPwdContract");
            log.info("用户请求重置支付密码到通联:{}", request);
            try {
                String res = YunClient.encodeOnce(request);
                String callbackUrl = allinPayProperties.getResetPayPwdUrl() + "?" + res;
                urlMap.put("url", callbackUrl);
                return R.ok(urlMap);
            } catch (Exception e) {
                log.warn("重置支付密码异常", e);
            }
        }
        return R.failed(400, "请先完成实名认证");
    }

    /**
     * 重置支付密码异步回调方法
     *
     * @param allinPayAsynResponseDTO
     */
    @Override
    public void memberAsynResetPwdContract(AllinPayAsynResponseDTO allinPayAsynResponseDTO) {

    }

    /**
     * 通联电子协议签约
     *
     * @param member
     * @return
     */
    @Override
    public R allinPaySignContract(Member member, String jumpUrl) {

        final YunRequest request = new YunRequest("MemberService", "signContract");
        Map<String, String> urlMap = new HashMap<>(1);
        request.put("bizUserId", member.getBizUserId());
        request.put("jumpUrl", jumpUrl);
        request.put("backUrl", restProperties.getProjectUrl() + "/allinPayAsynRespNotice/memberSignContract");
        request.put("source", "1");
        try {
            String res = YunClient.encodeOnce(request);
            String signContractUrl = allinPayProperties.getSignContractUrl() + "?" + res;
            urlMap.put("url", signContractUrl);
            return R.ok(urlMap);
        } catch (Exception e) {
            log.warn("编码签约通联电子协议地址异常", e);
        }
        return R.failed("编码签约通联电子协议地址失败");
    }

    /**
     * 客户签约通联电子协议异步回调方法
     *
     * @param allinPayAsynResponseDTO
     */
    @Override
    public void memberAsynSignContract(AllinPayAsynResponseDTO allinPayAsynResponseDTO) {
        try {
            Optional<AllinPayAsynSignContractDTO> response = AllinPayUtil.response(allinPayAsynResponseDTO, AllinPayAsynSignContractDTO.class);
            AllinPayAsynSignContractDTO apasc = response.get();
            String returnValue = apasc.getReturnValue();
            JSONObject jsonObject = JSON.parseObject(returnValue);
            Member member = selectOne(new EntityWrapper<Member>().eq("biz_user_id", jsonObject.get("bizUserId").toString()));
            member.setContractNo(apasc.getContractNo());
            memberMapper.updateById(member);
        } catch (Exception e) {
            OperationLog ol = new OperationLog();
            ol.setClassname(this.getClass().getName());
            ol.setCreatetime(new Date());
            ol.setLogname("客户签约通联电子协议异步回调方法执行异常");
            ol.setLogtype("异步回调异常");
            ol.setMessage(ToolUtil.getExceptionMsg(e));
            ol.setSucceed("false");
            ol.setMethod("memberAsynSignContract");
            iOperationLogService.insert(ol);
        }
    }

    /**
     * 用户获取token
     *
     * @param authPasswordRequestDTO
     * @return
     */
    @Override
    public R<AuthResponseDTO> memberAuth(AuthPasswordRequestDTO authPasswordRequestDTO) {
        //根据手机号查询用户
        Member dbMember = selectOne(new EntityWrapper<Member>().eq("username", authPasswordRequestDTO.getPhoneNo()));
        //用户是否存在
        Assert.notNull(dbMember, BizExceptionEnum.USER_DOES_NOT_EXIST.getMessage());
        //判断用户状态是否被禁用
        Assert.isTrue(CommonConstants.USER_STATUS_ENABLE_CODE.equals(dbMember.getState()), BizExceptionEnum.USER_IS_NOT_ALLOWED_TO_LOG_IN.getMessage());
        String randomKey = jwtTokenUtil.getRandomKey();
        String password = DigestUtil.md5Hex(authPasswordRequestDTO.getPassword() + "|" + dbMember.getSalt());
        Assert.isTrue(dbMember.getPassword().equals(password), BizExceptionEnum.PASSWORD_VALIDATION_FAILED.getMessage());
        String token = jwtTokenUtil.generateToken(authPasswordRequestDTO.getPhoneNo(), randomKey);
        return R.ok(new AuthResponseDTO(token, randomKey));
    }

    /**
     * 用户注册
     *
     * @param registerRequestDTO
     * @return
     */
    @Override
    public R<String> register(RegisterRequestDTO registerRequestDTO) {
        //根据手机号查询用户
        Member dbMember = selectOne(new EntityWrapper<Member>().eq("username", registerRequestDTO.getPhoneNo()));
        //用户是否存在
        Assert.isNull(dbMember, BizExceptionEnum.USER_EXIST.getMessage());
        Member temp = new Member();
        temp.setUsername(registerRequestDTO.getPhoneNo());
        //状态:[1:正常,0:禁用]
        temp.setState(1);
        String salt = NoUtil.generateSaltCode(registerRequestDTO.getPhoneNo());
        temp.setSalt(salt);
        String password = DigestUtil.md5Hex(registerRequestDTO.getPassword() + "|" + salt);
        temp.setPassword(password);
        log.info("插入数据库:{}", temp);
        memberMapper.insert(temp);
        //同步到通联
        YunRequest request = new YunRequest("MemberService", "createMember");
        String memberNo = NoUtil.generateCode(BizTypeEnum.MEMBER_NO, UserTypeEnum.CUSTOMER, temp.getId());
        request.put("bizUserId", memberNo);
        //会员类型:[3:个人会员,2:企业会员]
        request.put("memberType", 3);
        //访问终端类型:[1:Mobile,2:PC]
        request.put("source", 1);
        log.info("用户信息同步到通联:{}", request);
        Optional<AllinPayResponseDTO<APCreateMemberRespDTO>> response = AllinPayUtil.request(request, APCreateMemberRespDTO.class);
        //通联同步失败一定要抛异常回滚数据库
        Assert.isTrue(CommonConstants.SUCCESS_CODE.equals(response.get().getStatus()), () -> {
            log.warn("用户信息同步到通联失败:{}", response);
            return BizExceptionEnum.USER_CREATION_FAILED.getMessage();
        });
        temp.setBizUserId(memberNo);
        //默认创建用户实名认证状态为0[未认证]
        temp.setRealNameState(0);
        temp.setAllinpayUserId(response.get().getSignedValue().getUserId());
        memberMapper.updateById(temp);
        //创建账户信息
        Account account = new Account();
        account.setMasterId(temp.getId().intValue());
        account.setType(1);
        accountMapper.insert(account);
        return R.ok("注册成功");
    }

    /**
     * 重置本系统登录密码
     * @param registerRequestDTO
     * @return
     */
    @Override
    public R<String> resetPassword(RegisterRequestDTO registerRequestDTO) {
        //根据手机号查询用户
        Member dbMember = selectOne(new EntityWrapper<Member>().eq("username", registerRequestDTO.getPhoneNo()));
        //修改支付密码
        String salt = NoUtil.generateSaltCode(registerRequestDTO.getPhoneNo());
        dbMember.setSalt(salt);
        String password = DigestUtil.md5Hex(registerRequestDTO.getPassword() + "|" + salt);
        dbMember.setPassword(password);
        log.info("重置支付密码:{}", dbMember);
        memberMapper.updateById(dbMember);
        return R.ok("注册成功");
    }


    /**
     * 充值申请
     *
     * @param cwar
     * @return
     */
    @Override
    @SneakyThrows
    public Map<String, String> depositApply(DepositApplyRequestDTO cwar, Member member) {

        final YunRequest request = new YunRequest("OrderService", "depositApply");
        // 充值订单号
        String orderId = NoUtil.generateCode(BizTypeEnum.DEPOSIT_MONEY, UserTypeEnum.SUPPLIER, cwar.getId());
        request.put("bizOrderNo", orderId);
        request.put("bizUserId", member.getBizUserId());

        //账户集编号
        request.put("accountSetNo", allinPayProperties.getAccountSetNo());
        //订单金额
        Integer amount = MoneyFormatTester.bigDecimal2Long(cwar.getAmount()).intValue();
        request.put("amount", amount);
        //手续费=0
        request.put("fee", 0);
        //交易验证方式 0:不验证,1:短信验证码,2:支付密码
        request.put("validateType", 0);

        //后台通知地址
        request.put("backUrl", restProperties.getProjectUrl() + "/allinPayAsynRespNotice/rechargeCallback");

        JSONObject payMethod = new JSONObject();
        JSONObject subPayMethod = new JSONObject();

        Bank bank = bankMapper.selectById(cwar.getBankId());
        //,1:网关支付,2:快捷支付,3:微信,4:支付宝
        if (cwar.getPayMethod() == 1) {
            subPayMethod.put("amount", amount);
            //
            subPayMethod.put("paytype", cwar.getPayType());
            payMethod.put("GATEWAY_VSP", subPayMethod);
            //后台通知地址
            request.put("frontUrl", cwar.getJumpUrl());
        } else if (cwar.getPayMethod() == 2) {
            subPayMethod.put("bankCardNo", RSAUtil.encrypt(bank.getBankCardNo()));
            subPayMethod.put("amount", amount);
            payMethod.put("QUICKPAY_VSP", subPayMethod);
        } else if (cwar.getPayMethod() == 3) {
            payMethod.put("subAppid", allinPayProperties.getSubAppId());
            payMethod.put("amount", amount);
            payMethod.put("acct", cwar.getWechatOpenId());
            payMethod.put("limitPay", "");
            payMethod.put("cusip", cwar.getVisitorIp());
            payMethod.put("sceneInfo", "{\"h5_info\":\"{\"type\":\"Wap\",\"wap_url\": \"" + restProperties.getProjectUrl() + "\",\"wap_name\": \"爱赛充值\"}}");
            payMethod.put("WECHATPAY_H5_OPEN", subPayMethod);
        }
        //支付方式
        request.put("extendInfo", "充值");
        //支付方式
        request.put("payMethod", payMethod);
        //行业代码
        request.put("industryCode", allinPayCodeProperties.getOtherCode());
        //行业名称
        request.put("industryName", allinPayCodeProperties.getOtherName());
        //访问终端类型 1:手机端,2:PC端
        request.put("source", 1);
        log.info("用户充值申请请求到通联:{}", request);
        Optional<AllinPayResponseDTO<AllinPayDepositApplyResponseDTO>> response = AllinPayUtil.request(request, AllinPayDepositApplyResponseDTO.class);
        //通联同步失败一定要抛异常回滚数据库
        Assert.isTrue(Constants.SUCCESS_CODE.equals(response.get().getStatus()), () -> {
            log.warn("用户充值申请请求到通联失败:{}", response);
            return response.get().getMessage();
        });
        AllinPayDepositApplyResponseDTO signedValue = response.get().getSignedValue();
        if (Constants.STATUS_FAIL_CODE.equals(signedValue.getPayStatus())) {
            log.warn("用户充值申请请求到通联失败:{}", response);
            throw new SystemException(signedValue.getPayFailMessage());
        }

        Map<String, String> map = new HashMap<>(1);
        redisUtil.set(signedValue.getBizOrderNo(), 0,3600);
        //生成确认支付地址
        String url = this.openConfirmPaymentPage(signedValue.getBizUserId(), signedValue.getBizOrderNo(), cwar.getJumpUrl());
        map.put("url", url);
        String tradeNo = NoUtil.generateCode(BizTypeEnum.DEPOSIT_MONEY, UserTypeEnum.CUSTOMER, member.getId());
        map.put("bizOrderNo", signedValue.getBizOrderNo());
        map.put("tradeNo", tradeNo);
        map.put("orderTotal", cwar.getAmount().toString());
        return map;
    }

    /**
     * 跳转支付确认页面
     *
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
        request.put("consumerIp", Constants.CUSTOMER_IP);
        String setRes = YunClient.encodeOnce(request);
        return allinPayProperties.getPayOrderUrl() + "?" + setRes;
    }

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
    @Transactional(rollbackFor = Exception.class)
    public R payConfirm(OrderDTO orderDTO) {
        Member member = memberMapper.selectById(orderDTO.getUserId());
        YunRequest request = new YunRequest("OrderService", "pay");
        request.put("bizUserId", member.getBizUserId());
        request.put("bizOrderNo", orderDTO.getBizOrderNo());
        request.put("tradeNo", orderDTO.getTradeNo());
        //短信验证码
        request.put("verificationCode", orderDTO.getVerificationCode());
        //用户公网 IP 用于分控校验 注：不能使用“127.0.0.1” “localhost”
        request.put("consumerIp", Constants.CUSTOMER_IP);

        Optional<AllinPayResponseDTO<AllinPayCashWithdrawalApplyResponseDTO>> response = AllinPayUtil.request(request, AllinPayCashWithdrawalApplyResponseDTO.class);
        AllinPayCashWithdrawalApplyResponseDTO signedValue = response.get().getSignedValue();
        if (Constants.STATUS_FAIL_CODE.equals(signedValue.getPayStatus())) {
            log.warn("用户（买方）充值确认接口调用失败:{}", response);
            return R.failed(response.get().getSignedValue().getPayFailMessage());
        }
        redisUtil.set(signedValue.getBizOrderNo(), 0,3600);
        if (Constants.STATUS_SUCCESS_CODE.equals(signedValue.getPayStatus())) {
            R.ok("充值成功");
        }
        return R.ok("提现中,请稍后查询");

        /* */

    }


    /**
     * 提现申请
     *
     * @param cwar
     * @param member
     * @return
     */

    @Override
    @SneakyThrows
    public R<Map<String, String>> cashWithdrawalApply(CashWithdrawalApplyRequestDTO cwar, Member member) {
        log.info("客户提现申请方法:{}", cwar);

        EntityWrapper<Account> accountEW = new EntityWrapper();
        accountEW.eq("master_id", member.getId()).eq("type", 1);
        Account account = iAccountService.selectOne(accountEW);
        Bank bank = bankMapper.selectById(cwar.getBankId());
        //通联限额
        BigDecimal allinpayQuota = new BigDecimal(Constants.ALLINPAY_QUOTA);
        //本次提现总金额
        BigDecimal totleAmount = cwar.getAmount();
        //剩余额度
        BigDecimal surplusAmount = cwar.getAmount();
        //本次提现金额
        BigDecimal tempAmount;


        //由于通联提现限额5000，所以多次调用提现接口提现
        while (surplusAmount.compareTo(new BigDecimal(0)) > 0) {

            //剩余提现金额>限额
            if (surplusAmount.compareTo(allinpayQuota) > 0) {
                surplusAmount = totleAmount.subtract(allinpayQuota);
                tempAmount = allinpayQuota;
            } else {//剩余提现金额<限额
                tempAmount = surplusAmount;
                surplusAmount = new BigDecimal(0);
            }

            log.info("通联限额:{},本次提现总金额:{},本次提现金额:{},剩余提现金额:{}", allinpayQuota, totleAmount, tempAmount, surplusAmount);

            final YunRequest request = new YunRequest("OrderService", "withdrawApply");
            // 提现申请号
            String orderId = NoUtil.generateCode(BizTypeEnum.CASH_WITHDRAWAL, UserTypeEnum.CUSTOMER, member.getId());
            request.put("bizOrderNo", orderId);
            request.put("bizUserId", member.getBizUserId());
            //账户集编号
            request.put("accountSetNo", allinPayProperties.getAccountSetNo());
            //订单金额
            Long amount = MoneyFormatTester.bigDecimal2Long(tempAmount);
            request.put("amount", amount.intValue());
            //手续费
            int serviceCharge = new BigDecimal(amount * 0.002).intValue();
            //至少收2元
            if (serviceCharge < Constants.MIN_SERVICE_CHARGE) {
                serviceCharge = Constants.MIN_SERVICE_CHARGE;
            }
            request.put("fee", serviceCharge);
            //交易验证方式 0:不验证,1:短信验证码,2:支付密码
            request.put("validateType", 0);
            //后台通知地址
            request.put("backUrl", restProperties.getProjectUrl() + "/allinPayAsynRespNotice/withdrawal");
            //银行卡号/账号
            request.put("bankCardNo", RSAUtil.encrypt(bank.getBankCardNo()));
            //行业代码
            request.put("industryCode", allinPayCodeProperties.getOtherCode());
            //行业名称
            request.put("industryName", allinPayCodeProperties.getOtherName());
            //访问终端类型 1:手机端,2:PC端
            request.put("source", 2);
            request.put("extendInfo","提现");
            log.info("用户提现申请请求到通联:{}", request);
            Optional<AllinPayResponseDTO<AllinPayCashWithdrawalApplyResponseDTO>> response = AllinPayUtil.request(request, AllinPayCashWithdrawalApplyResponseDTO.class);

            if (!Constants.SUCCESS_CODE.equals(response.get().getStatus())) {
                log.warn("用户提现申请请求到通联失败:{}", response);
                throw new SystemException(response.get().getMessage());
            }

            AllinPayCashWithdrawalApplyResponseDTO signedValue = response.get().getSignedValue();

            if (Constants.STATUS_FAIL_CODE.equals(signedValue.getPayStatus())) {
                log.warn("用户提现申请请求到通联失败:{}", response);
                throw new SystemException(signedValue.getPayFailMessage());
            }

            redisUtil.set(signedValue.getBizOrderNo(), 0,3600);

        }

        return R.ok("提现成功,请稍后查询!");
    }

    /**
     * 获取客户通联账户余额
     * @param bizUserId
     * @return
     */
    @Override
    public Account getAccount(String bizUserId) {
        log.info("获取通联账户余额:{}", bizUserId);
        YunRequest request = new YunRequest("OrderService","queryBalance");
        request.put("bizUserId", bizUserId);
        request.put("accountSetNo", allinPayProperties.getAccountSetNo());

        Optional<AllinPayResponseDTO<AllinPayQueryAccountDTO>> response = AllinPayUtil.request(request, AllinPayQueryAccountDTO.class);
        //通联同步失败一定要抛异常回滚数据库
        Assert.isTrue(Constants.SUCCESS_CODE.equals(response.get().getStatus()), () -> {
            log.warn("查询账户信息通联审核结果失败:{}", response);
            return response.get().getMessage();
        });

        AllinPayQueryAccountDTO signedValue = response.get().getSignedValue();
        Account account = new Account();
        BigDecimal totleAmoun = new BigDecimal((signedValue.getAllAmount() / 100.0) + "");
        BigDecimal freezenAmount = new BigDecimal((signedValue.getFreezenAmount() / 100.0) + "");
        account.setTotleAmount(totleAmoun);
        account.setAvailableBalance(totleAmoun.subtract(freezenAmount));
        account.setFreezingAmount(freezenAmount);
        return account;
    }

}
