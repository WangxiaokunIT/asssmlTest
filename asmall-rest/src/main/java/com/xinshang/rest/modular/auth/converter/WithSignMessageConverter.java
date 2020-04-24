package com.xinshang.rest.modular.auth.converter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.xinshang.core.exception.SystemException;
import com.xinshang.core.support.HttpKit;
import com.xinshang.core.util.MD5Util;
import com.xinshang.rest.common.enums.BizExceptionEnum;
import com.xinshang.rest.config.properties.JwtProperties;
import com.xinshang.rest.modular.auth.security.DataSecurityAction;
import com.xinshang.rest.common.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * 带签名的http信息转化器
 *
 * @author fengshuonan
 * @date 2017-08-25 15:42
 */
@Slf4j
public class WithSignMessageConverter extends FastJsonHttpMessageConverter {

    @Autowired
    private  JwtProperties jwtProperties;

    @Autowired
    private  JwtTokenUtil jwtTokenUtil;

    @Autowired
    private  DataSecurityAction dataSecurityAction;

    @Override
    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        //是否在排除列表中
        if (matchPath(jwtProperties.getNoPath(), HttpKit.getRequest().getServletPath())) {
            return super.read(type,contextClass,inputMessage);
        }
        InputStream in = inputMessage.getBody();
        Object o = JSON.parseObject(in, super.getFastJsonConfig().getCharset(), BaseTransferEntity.class, super.getFastJsonConfig().getFeatures());

        //先转化成原始的对象
        BaseTransferEntity baseTransferEntity = (BaseTransferEntity) o;

        //获取请求参数
        String object = baseTransferEntity.getObject();
        //base64解码
        String json = dataSecurityAction.unlock(object);

        //校验签名
        String token = HttpKit.getRequest().getHeader(jwtProperties.getHeader()).substring(7);
        //获取随机码
        String md5KeyFromToken = jwtTokenUtil.getMd5KeyFromToken(token);
        String encrypt = MD5Util.encrypt(object + md5KeyFromToken);

        if (encrypt.equals(baseTransferEntity.getSign())) {
            log.info("签名校验成功!");
        } else {
            log.warn("签名校验失败,数据被改动过!");
            throw new SystemException(BizExceptionEnum.SIGN_ERROR.getCode(),BizExceptionEnum.SIGN_ERROR.getMessage());
        }
        //校验签名后再转化成应该的对象
        return JSON.parseObject(json, type);
    }


    private boolean matchPath(String[] paths,String realPath){
        for (String path : paths) {
            if(path.equals(realPath)){
                return true;
            }
            int index = path.indexOf("*");
            if(index!=-1){
                path = path.substring(0,index);
                if(realPath.startsWith(path)){
                    return true;
                }
            }

        }
        return false;
    }
}
