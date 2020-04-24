package com.xinshang.rest.modular.asmall.service;


import com.xinshang.rest.common.util.R;
import com.xinshang.rest.modular.asmall.dto.AllinPayBindingPhoneDTO;
import com.xinshang.rest.modular.asmall.dto.AllinPaySendSmsCaptchaDTO;

/**
 * <p>
 * 短信验证码服务类
 * </p>
 *
 * @author sunhao
 * @since 2019-10-24
 */
public interface ISendMsgService {

    /**
     * 发送短信验证码
     * @param phoneNo
     */
    void sendSmsCaptcha (String phoneNo);

    /**
     * 校验短信验证码
     * @param phoneNo
     * @param captcha
     * @return
     */
    Boolean checkSmsCaptcha (String phoneNo,String captcha);

    /**
     * 通联短信验证码
     * @param allinPaySendSmsCaptchaDTO
     * @return
     */
    R sendAllinPaySmsCaptcha(AllinPaySendSmsCaptchaDTO allinPaySendSmsCaptchaDTO);

    /**
     * 通联绑定手机
     * @param allinPayBindingPhoneDTO
     * @return
     */
    R<String> allinPayBindingPhone(AllinPayBindingPhoneDTO allinPayBindingPhoneDTO);
    /**
     * 通联解除绑定手机
     * @param allinPayBindingPhoneDTO
     * @return
     */
    R<String> allinPayUnBindingPhone(AllinPayBindingPhoneDTO allinPayBindingPhoneDTO);
}
