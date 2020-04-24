package com.xinshang.core.util.eqb;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.allinpay.yunst.sdk.YunClient;
import com.allinpay.yunst.sdk.bean.YunRequest;
import com.allinpay.yunst.sdk.util.RSAUtil;
import com.xinshang.core.exception.SystemException;
import com.xinshang.modular.biz.dao.CommonConstants;
import com.xinshang.modular.biz.dto.AllinPayAsynResponseDTO;
import com.xinshang.modular.biz.dto.AllinPayResponseDTO1;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;

/**
 * @author zhangjiajia
 */
@Slf4j
@UtilityClass
public class AllinPayUtil1 {

    /**
     * 请求通联接口
     * @param yunRequest 请求参数
     * @param clazz 返回签名对象类型
     * @param <T>
     * @return
     */
    public <T> Optional<AllinPayResponseDTO1<T>> request (YunRequest yunRequest, Class<T> clazz){
        log.info("开始请求通联接口:{}",yunRequest);
        AllinPayResponseDTO1<T> apr = new AllinPayResponseDTO1<>();
        try {
            String res = YunClient.request(yunRequest);
            log.info("完成请求通联接口,返回结果内容:{}",res);
            Map<String, String> respMap = (Map)JSON.parseObject(res, Map.class);
            String signedValueStr = respMap.get("signedValue");
            respMap.put("signedValue",null);

            BeanUtil.fillBeanWithMap(respMap,apr,true);
            if(CommonConstants.SUCCESS_CODE.equals(apr.getStatus())){
                T t = JSON.parseObject(signedValueStr,clazz);
                apr.setSignedValue(t);
            }
            return Optional.of(apr);
        } catch (Exception e) {
            log.error("请求通联接口异常:{}",e);
        }

        apr.setStatus("error");
        apr.setErrorCode("-1");
        apr.setMessage("请求通联接口异常");
        return Optional.of(apr);
    }

    /**
     * 通联接口异步返回操作
     * @param allinPayAsynResponseDTO
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> Optional<T> response (AllinPayAsynResponseDTO allinPayAsynResponseDTO, Class<T> clazz){
        log.info("通联接口异步返回:{}",allinPayAsynResponseDTO);
        try{

            if (!RSAUtil.verify(allinPayAsynResponseDTO.getSysid()+allinPayAsynResponseDTO.getRps()+allinPayAsynResponseDTO.getTimestamp(), allinPayAsynResponseDTO.getSign())) {
                throw new SystemException("签名验证失败");
            }
            String rps = allinPayAsynResponseDTO.getRps();

            Map<String,String> map = JSON.parseObject(rps,Map.class);
            if (!CommonConstants.SUCCESS_CODE.equals(map.get("status"))) {
                throw new SystemException("调用失败");
            }

            T t = JSON.parseObject(rps,clazz);
            return Optional.of(t);

        }catch (Exception e){
            log.warn("签名验证失败",e);
            throw new SystemException("签名验证失败");
        }

    }


}
