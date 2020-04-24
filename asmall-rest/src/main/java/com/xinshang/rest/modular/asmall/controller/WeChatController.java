package com.xinshang.rest.modular.asmall.controller;

import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.xinshang.rest.common.util.R;
import com.xinshang.rest.config.properties.RestProperties;
import com.xinshang.rest.modular.asmall.dto.AuthResponseDTO;
import com.xinshang.rest.modular.asmall.dto.WeChatAccessTokenDTO;
import com.xinshang.rest.modular.asmall.dto.WeChatUserInfoDTO;
import com.xinshang.rest.modular.asmall.model.OperationLog;
import com.xinshang.rest.modular.asmall.service.IMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.Date;


/**
 * 微信相关接口管理
 *
 * @author fengshuonan
 * @Date 2017/8/24 14:22
 */
@Api(value = "微信相关信息",tags = "微信相关接口")
@Controller
@Slf4j
@AllArgsConstructor
@RequestMapping("/weChat")
public class WeChatController {

    private final IMemberService iMemberService;
    private final RestProperties restProperties;

    /**
     * 获取可以得到微信用户code的url
     * @return
     */
    @ApiOperation(value = "获取可以得到微信用户code的url", notes = "获取可以得到微信用户code的url")
    @GetMapping("/code")
    public String getWeChatCode() {
        String url ="https://open.weixin.qq.com/connect/oauth2/authorize?appid="+restProperties.getWeChatPublicAddressAppId()+"&redirect_uri="+URLUtil.encode("http://www.iseemall.com.cn/asmall-rest/weChat/callback")+"&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
        return "redirect:"+url;
    }

    /**
     * 获取token
     * @param openid
     * @return
     */
    @ApiOperation(value = "获取token", notes = "获取token")
    @GetMapping(value = "/auth/{openid}")
    @ResponseBody
    public R<AuthResponseDTO> createAuthenticationToken(@PathVariable String openid) {
        WeChatUserInfoDTO wci = new WeChatUserInfoDTO();
        wci.setOpenid(openid);
        return iMemberService.memberWeChatAuth(wci);
    }

    /**
     * 微信回调地址
     * @return
     */

    @GetMapping("/callback")
    public  String getCallback(String code) {
        log.info("微信回调地址code:{}",code);
        //通过code换取网页授权access_token
        String  url="https://api.weixin.qq.com/sns/oauth2/access_token?appid="+restProperties.getWeChatPublicAddressAppId()
                + "&secret="+restProperties.getWeChatPublicAddressAppSecret()
                + "&code="+code
                + "&grant_type=authorization_code";
        final WeChatAccessTokenDTO accessToken = JSON.parseObject(HttpUtil.get(url), WeChatAccessTokenDTO.class);
        log.info("获取accessToken结果:{}",accessToken);
        if(accessToken.getErrcode()!=null){
            OperationLog ol = new OperationLog();
            ol.setClassname(this.getClass().getName());
            ol.setCreatetime(new Date());
            ol.setLogname("获取accessToken失败");
            ol.setLogtype("微信异步回调失败");
            ol.setMessage(accessToken.toString());
            ol.setSucceed("false");
            ol.setMethod("callback");
            ol.insert();
            return "redirect:"+restProperties.getWeChatFrontCallbackUrl()+"?token=&randomKey=";
        }
        //4 第四步：拉取用户信息(需scope为 snsapi_userinfo)
        String userInfoUrl="https://api.weixin.qq.com/sns/userinfo?access_token="+accessToken.getAccess_token()
                + "&openid="+accessToken.getOpenid()
                + "&lang=zh_CN";
        final WeChatUserInfoDTO userInfo = JSON.parseObject(HttpUtil.get(userInfoUrl), WeChatUserInfoDTO.class);
        log.info("获取微信用户信息结果:{}",userInfo);
        if(userInfo.getErrcode()!=null){
            OperationLog ol = new OperationLog();
            ol.setClassname(this.getClass().getName());
            ol.setCreatetime(new Date());
            ol.setLogname("获取微信用户信息失败");
            ol.setLogtype("微信异步回调失败");
            ol.setMessage(userInfo.toString());
            ol.setSucceed("false");
            ol.setMethod("callback");
            ol.insert();
            return "redirect:"+restProperties.getWeChatFrontCallbackUrl()+"?token=&randomKey=";
        }

        R<AuthResponseDTO> authResponseDTOR = iMemberService.memberWeChatAuth(userInfo);
        return "redirect:"+restProperties.getWeChatFrontCallbackUrl()+"?token="+authResponseDTOR.getData().getToken()+"&randomKey="+authResponseDTOR.getData().getRandomKey();
    }
}
