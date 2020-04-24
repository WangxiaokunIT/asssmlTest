package com.xinshang.rest.config;

import cn.hutool.core.bean.BeanUtil;
import com.allinpay.yunst.sdk.YunClient;
import com.allinpay.yunst.sdk.bean.YunConfig;
import com.xinshang.rest.config.properties.AllinPayProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author lnj
 * createTime 2018-11-07 22:37
 **/
@Component
@Slf4j
public class ApplicationRunnerImpl implements ApplicationRunner {

    @Autowired
    private AllinPayProperties allinPayProperties;


    @Override
    public void run(ApplicationArguments args){

        YunConfig yunConfig = new YunConfig();
        BeanUtil.copyProperties(allinPayProperties,yunConfig);
        YunClient.configure(yunConfig);
        log.info("配置YunClient完成");
    }
}