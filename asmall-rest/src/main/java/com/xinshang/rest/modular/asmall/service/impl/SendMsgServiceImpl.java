package com.xinshang.rest.modular.asmall.service.impl;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.allinpay.yunst.sdk.bean.YunRequest;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xinshang.rest.common.enums.BizExceptionEnum;
import com.xinshang.rest.common.enums.CommonConstants;
import com.xinshang.rest.common.util.AllinPayUtil;
import com.xinshang.rest.common.util.R;
import com.xinshang.rest.common.util.RedisUtil;
import com.xinshang.rest.config.properties.SmsProperties;
import com.xinshang.rest.modular.asmall.dao.MemberMapper;
import com.xinshang.rest.modular.asmall.dto.AliyunSmsResponseDTO;
import com.xinshang.rest.modular.asmall.dto.AllinPayBindingPhoneDTO;
import com.xinshang.rest.modular.asmall.dto.AllinPayResponseDTO;
import com.xinshang.rest.modular.asmall.dto.AllinPaySendSmsCaptchaDTO;
import com.xinshang.rest.modular.asmall.model.Member;
import com.xinshang.rest.modular.asmall.service.IMemberService;
import com.xinshang.rest.modular.asmall.service.ISendMsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import java.util.Optional;

/**
 * <p>
 * 发送验证码服务
 * </p>
 *
 * @author sunhao
 * @since 2019-10-17
 */
@Service
@Slf4j
public class SendMsgServiceImpl implements ISendMsgService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private IMemberService iMemberService;

    @Autowired
    private SmsProperties smsProperties;

    @Autowired
    private MemberMapper memberMapper;

    private IAcsClient iAcsClient;
    /**
     * 发送短信验证码
     * @param phoneNo 手机号
     */
    @Async
    @Override
    public void sendSmsCaptcha(String phoneNo) {

        IAcsClient client = Optional.ofNullable(iAcsClient).orElseGet(() -> {
            iAcsClient = new DefaultAcsClient(DefaultProfile.getProfile(smsProperties.getRegionId(), smsProperties.getAccessKeyId(), smsProperties.getAccessKeySecret()));
            return iAcsClient;
        });

        //生成验证码
        String captcha = ((Math.random() * 9 + 1) * 100000 + "").substring(0, 6);

        CommonRequest cr = new CommonRequest();
        cr.setMethod(MethodType.POST);
        cr.setDomain(smsProperties.getDomain());
        cr.setVersion("2017-05-25");
        cr.setAction("SendSms");
        cr.putQueryParameter("TemplateCode", smsProperties.getTemplateCode());
        cr.putQueryParameter("SignName", "爱赛商城");
        cr.putQueryParameter("PhoneNumbers", phoneNo);
        cr.putQueryParameter("TemplateParam", "{\"code\":\"" + captcha + "\"}");

        CommonResponse response;
        try {
            log.info("短信接口请求参数:{}",cr);
            response = client.getCommonResponse(cr);
            String data = response.getData();
            AliyunSmsResponseDTO aliyunSmsResponseDTO = JSON.parseObject(data, AliyunSmsResponseDTO.class);
            log.info("短信接口返回结果:{}",aliyunSmsResponseDTO);
            Assert.isTrue(CommonConstants.SUCCESS_CODE.equals(aliyunSmsResponseDTO.getCode()),()->{
                log.warn(phoneNo + "短信发送失败:" + aliyunSmsResponseDTO.getMessage());
                return "短信发送失败,请稍后再试";
            });

        } catch (com.aliyuncs.exceptions.ServerException e) {
            log.warn("阿里云短信服务器错误",e);
        } catch (com.aliyuncs.exceptions.ClientException e) {
            log.warn("阿里云短信客户端错误",e);
        }

        //同步存入redis中 保持10分钟
        redisUtil.hset(CommonConstants.SMS_CAPTCHA_KEY,phoneNo,captcha,600);
    }

    /**
     * 验证短信验证码
     * @param phoneNo
     * @param captcha
     * @return
     */
    @Override
    public Boolean checkSmsCaptcha(String phoneNo,String captcha) {
        Object obj = redisUtil.hget(CommonConstants.SMS_CAPTCHA_KEY,phoneNo);
        return Optional.ofNullable(obj).map(o -> o.equals(captcha)).orElse(Boolean.FALSE);
    }

    /**
     * 通联-发送验证码
     * @param allinPaySendSmsCaptchaDTO
     * @return
     */
    @Override
    public R sendAllinPaySmsCaptcha(AllinPaySendSmsCaptchaDTO allinPaySendSmsCaptchaDTO) {

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
        Assert.isTrue(CommonConstants.SUCCESS_CODE.equals(response.get().getStatus()),()->{
            log.warn("发送验证码到通联失败:{}",response);
            return BizExceptionEnum.SMS_SEND_FAILED.getMessage();
        });
        return R.ok("发送成功");
    }

    /**
     * 通联-绑定手机
     * @param allinPayBindingPhoneDTO
     * @return
     */
    @Override
    public R<String> allinPayBindingPhone(AllinPayBindingPhoneDTO allinPayBindingPhoneDTO) {
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
        Assert.isTrue(CommonConstants.SUCCESS_CODE.equals(response.get().getStatus()),()->{
            log.warn("通联绑定手机接口失败:{}",response);
            return BizExceptionEnum.BINDING_PHONE_FAILED.getMessage()+",原因:"+response.get().getMessage();
        });
        Member member = iMemberService.selectOne(new EntityWrapper<Member>().eq("biz_user_id", allinPayBindingPhoneDTO.getBizUserId()));
        member.setPhone(response.get().getSignedValue().getPhone());
        memberMapper.updateById(member);
        return R.ok("绑定成功");
    }
    /**
     * 通联-解除绑定手机
     * @param allinPayBindingPhoneDTO
     * @return
     */
    @Override
    public R<String> allinPayUnBindingPhone(AllinPayBindingPhoneDTO allinPayBindingPhoneDTO) {
        //同步到通联
        YunRequest request = new YunRequest("MemberService", "unbindPhone");
        request.put("bizUserId", allinPayBindingPhoneDTO.getBizUserId());
        //手机号
        request.put("phone", allinPayBindingPhoneDTO.getPhone());
        //验证码
        request.put("verificationCode", allinPayBindingPhoneDTO.getVerificationCode());
        log.info("通联解除绑定手机接口参数:{}",request);
        Optional<AllinPayResponseDTO<AllinPayBindingPhoneDTO>> response = AllinPayUtil.request(request, AllinPayBindingPhoneDTO.class);
        //通联同步失败一定要抛异常回滚数据库
        Assert.isTrue(CommonConstants.SUCCESS_CODE.equals(response.get().getStatus()),()->{
            log.warn("通联解除绑定手机接口失败:{}",response);
            if("手机未绑定".equals(response.get().getMessage()))
            {

                Member member = iMemberService.selectOne(new EntityWrapper<Member>().eq("biz_user_id", allinPayBindingPhoneDTO.getBizUserId()));
                //解绑手机后，数据库phone设置为空
                member.setPhone("");
                memberMapper.updateById(member);
                //return restResult(null, CommonConstants.SUCCESS, "解除绑定成功");
                return "解除绑定成功";
                //R<String> apiResult = new R<String>();
                //apiResult.setCode(CommonConstants.SUCCESS);
                //apiResult.setData(null);
                //apiResult.setMsg("解除绑定成功");
                //return apiResult;
            }
            else
            {
            return BizExceptionEnum.BINDING_PHONE_FAILED.getMessage()+",原因:"+response.get().getMessage();
            }
        });
        Member member = iMemberService.selectOne(new EntityWrapper<Member>().eq("biz_user_id", allinPayBindingPhoneDTO.getBizUserId()));
        //解绑手机后，数据库phone设置为空
        member.setPhone("");
        memberMapper.updateById(member);
        return R.ok("解除绑定成功");
    }
}
