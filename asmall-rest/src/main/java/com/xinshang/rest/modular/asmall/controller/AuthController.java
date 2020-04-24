package com.xinshang.rest.modular.asmall.controller;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.xinshang.core.exception.SystemException;
import com.xinshang.rest.common.enums.BizExceptionEnum;
import com.xinshang.rest.common.enums.CommonConstants;
import com.xinshang.rest.common.exception.BizException;
import com.xinshang.rest.common.util.R;
import com.xinshang.rest.common.util.RedisUtil;
import com.xinshang.rest.config.properties.SmsProperties;
import com.xinshang.rest.modular.asmall.dto.AuthCaptchaRequestDTO;
import com.xinshang.rest.modular.asmall.dto.AuthPasswordRequestDTO;
import com.xinshang.rest.modular.asmall.dto.AuthResponseDTO;
import com.xinshang.rest.common.util.JwtTokenUtil;
import com.xinshang.rest.modular.asmall.dto.RegisterRequestDTO;
import com.xinshang.rest.modular.asmall.service.IMemberService;
import com.xinshang.rest.modular.asmall.service.ISendMsgService;
import com.xinshang.rest.modular.asmall.vo.TokenVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

/**
 * 请求验证的
 *
 * @author fengshuonan
 * @Date 2017/8/24 14:22
 */
@Api(value = "登录相关信息",tags = "登录相关接口")
@RestController
@Slf4j
@AllArgsConstructor
public class AuthController {

    private final IMemberService iMemberService;
    private final ISendMsgService iSendMsgService;
    private final JwtTokenUtil jwtTokenUtil;
    private final RedisUtil redisUtil;
    private final SmsProperties smsProperties;

    /**
     * 获取token
     * @param authCaptchaRequestDTO
     * @return
     */
    @ApiOperation(value = "获取token", notes = "获取token")
    @PostMapping(value = "auth")
    public R<AuthResponseDTO> createAuthenticationToken(@RequestBody AuthCaptchaRequestDTO authCaptchaRequestDTO) {

        //验证手机号格式
        Assert.isTrue(ReUtil.isMatch("^1\\d{10}$", authCaptchaRequestDTO.getPhoneNo()),BizExceptionEnum.PHONE_NO_VALIDATION_FAILED.getMessage());
        /**
         * Optional.ofNullable(authCaptchaRequestDTO.getSmsCaptcha()).filter(s -> ReUtil.isMatch("^\\d{6}$",s))
         *      .filter(s -> iSendMsgService.checkSmsCaptcha(authCaptchaRequestDTO.getPhoneNo(),authCaptchaRequestDTO.getSmsCaptcha()))
         *      .orElseThrow(()->new BizException(BizExceptionEnum.INCORRECT_VERIFICATION_CODE));
         *
         */

        //验证验证码格式
        Assert.isTrue(ReUtil.isMatch("^\\d{6}$", authCaptchaRequestDTO.getSmsCaptcha()),BizExceptionEnum.INCORRECT_VERIFICATION_CODE.getMessage());
        //验证验证码是否正确
        //Assert.isTrue(iSendMsgService.checkSmsCaptcha(authCaptchaRequestDTO.getPhoneNo(),authCaptchaRequestDTO.getSmsCaptcha()),BizExceptionEnum.INCORRECT_VERIFICATION_CODE.getMessage());
        //这两行代码等价与上面注释掉的代码
        return iMemberService.memberAuth(authCaptchaRequestDTO);
    }

    /**
     * 根据手机号加验证码获取token
     * @param authPasswordRequestDTO
     * @return
     */
    @ApiOperation(value = "获取token", notes = "根据用户名和密码获取token")
    @PostMapping(value = "getToken")
    public R<AuthResponseDTO> getToken(@RequestBody AuthPasswordRequestDTO authPasswordRequestDTO) {
        //验证手机号格式
        Assert.isTrue(ReUtil.isMatch("^1\\d{10}$", authPasswordRequestDTO.getPhoneNo()),BizExceptionEnum.PHONE_NO_VALIDATION_FAILED.getMessage());
        Assert.isTrue(StrUtil.isNotBlank(authPasswordRequestDTO.getPassword()),BizExceptionEnum.PASSWORD_EMPTY_FAILED.getMessage());
        return iMemberService.memberAuth(authPasswordRequestDTO);
    }

    /**
     * 根据手机号加验证码获取token
     * @param registerRequestDTO
     * @return
     */
    @ApiOperation(value = "用户注册", notes = "用户注册")
    @PostMapping(value = "register")
    public R<String> register(@RequestBody RegisterRequestDTO registerRequestDTO) {
        //验证手机号格式
        Assert.isTrue(ReUtil.isMatch("^1\\d{10}$", registerRequestDTO.getPhoneNo()),BizExceptionEnum.PHONE_NO_VALIDATION_FAILED.getMessage());
        Assert.isTrue(StrUtil.isNotBlank(registerRequestDTO.getPassword()),BizExceptionEnum.PASSWORD_EMPTY_FAILED.getMessage());
        Assert.isTrue(StrUtil.isNotBlank(registerRequestDTO.getConfirmPassword()),BizExceptionEnum.CONFIRM_PASSWORD_EMPTY_FAILED.getMessage());
        Assert.isTrue(registerRequestDTO.getPassword().equals(registerRequestDTO.getConfirmPassword()),BizExceptionEnum.TWO_PASSWORDS_ARE_INCONSISTENT_FAILED.getMessage());

          Optional.ofNullable(registerRequestDTO.getSmsCaptcha()).filter(s -> ReUtil.isMatch("^\\d{6}$",s))
               .filter(s -> iSendMsgService.checkSmsCaptcha(registerRequestDTO.getPhoneNo(),registerRequestDTO.getSmsCaptcha()))
              .orElseThrow(()->new BizException(BizExceptionEnum.INCORRECT_VERIFICATION_CODE));

        return iMemberService.register(registerRequestDTO);
    }

    /**
     * 根据手机号加验证码获取token
     * @param registerRequestDTO
     * @return
     */
    @ApiOperation(value = "用户重置密码", notes = "用户重置密码")
    @PostMapping(value = "resetPassword")
    public R<String> resetPassword(@RequestBody RegisterRequestDTO registerRequestDTO) {
        //验证手机号格式
        Assert.isTrue(ReUtil.isMatch("^1\\d{10}$", registerRequestDTO.getPhoneNo()),BizExceptionEnum.PHONE_NO_VALIDATION_FAILED.getMessage());
        Assert.isTrue(StrUtil.isNotBlank(registerRequestDTO.getPassword()),BizExceptionEnum.PASSWORD_EMPTY_FAILED.getMessage());
        Assert.isTrue(StrUtil.isNotBlank(registerRequestDTO.getConfirmPassword()),BizExceptionEnum.CONFIRM_PASSWORD_EMPTY_FAILED.getMessage());
        Assert.isTrue(registerRequestDTO.getPassword().equals(registerRequestDTO.getConfirmPassword()),BizExceptionEnum.TWO_PASSWORDS_ARE_INCONSISTENT_FAILED.getMessage());

        Optional.ofNullable(registerRequestDTO.getSmsCaptcha()).filter(s -> ReUtil.isMatch("^\\d{6}$",s))
                .filter(s -> iSendMsgService.checkSmsCaptcha(registerRequestDTO.getPhoneNo(),registerRequestDTO.getSmsCaptcha()))
                .orElseThrow(()->new BizException(BizExceptionEnum.INCORRECT_VERIFICATION_CODE));

        return iMemberService.resetPassword(registerRequestDTO);
    }


    /**
     * 验证token是否过期
     * @param token
     * @return true 过期  false 未过期
     */
    @ApiOperation(value = "校验token", notes = "校验token")
    @PostMapping("/isTokenExpired")
    public R<TokenVO> isTokenExpired(String token) {
        Boolean bo = jwtTokenUtil.isTokenExpired(token);
        TokenVO tokenVO = new TokenVO();
        tokenVO.setToken(token);
        tokenVO.setState(bo);
        return R.ok(tokenVO);
    }

    /**
     * 发送短信验证码
     * @param phoneNo
     * @return
     */
    @ApiOperation(value = "发送验证码", notes = "发送验证码")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "phoneNo", value = "手机号码", required = true, dataType = "String")
    })
    @GetMapping("/smsCaptcha/{phoneNo}")
    public R<String> SendSMSCaptcha(@PathVariable String phoneNo) {

        //验证手机号格式
        Assert.isTrue(ReUtil.isMatch("^1\\d{10}$", phoneNo),BizExceptionEnum.PHONE_NO_VALIDATION_FAILED.getMessage());

        //发送次数+1
        redisUtil.hincr(CommonConstants.SMS_CAPTCHA_DAY_COUNT_KEY,phoneNo,1L);
        //查询该手机号已的验证码的次数
        Object hget = redisUtil.hget(CommonConstants.SMS_CAPTCHA_DAY_COUNT_KEY, phoneNo);
        Optional.ofNullable(hget).map(o -> Integer.valueOf(o.toString()))
                .filter(d->{log.info("{}手机号今日已发送短信{}次",phoneNo,d);return d > smsProperties.getDayLimit();})
                .ifPresent(t-> {throw new SystemException("今日短信使用次数已达到上限");});

        iSendMsgService.sendSmsCaptcha(phoneNo);
        return R.ok("验证码已发送");
    }



}
