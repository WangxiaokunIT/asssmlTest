package com.xinshang.core.bootstrap;

import cn.hutool.core.bean.BeanUtil;
import com.allinpay.yunst.sdk.YunClient;
import com.allinpay.yunst.sdk.bean.YunConfig;
import com.xinshang.config.properties.AllinPayProperties;
import com.xinshang.core.common.constant.factory.ConstantFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;



/**
 * @author zhangjiajia
 * @since 19-3-15
 */
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
        ConstantFactory.me().cacheDict();
        log.info("缓存字典到redis完毕！");


    }
}
