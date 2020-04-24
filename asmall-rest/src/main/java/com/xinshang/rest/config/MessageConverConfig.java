package com.xinshang.rest.config;


import com.xinshang.rest.config.properties.RestProperties;
import com.xinshang.rest.modular.auth.converter.WithSignMessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 签名校验messageConverter
 *
 * @author fengshuonan
 * @date 2017-08-25 16:04
 */
@Configuration
public class MessageConverConfig {

    @Bean
    @ConditionalOnProperty(prefix = RestProperties.PREFIX, name = "sign-open", havingValue = "true", matchIfMissing = true)
    public WithSignMessageConverter withSignMessageConverter() {
        WithSignMessageConverter withSignMessageConverter = new WithSignMessageConverter();
        DefaultFastjsonConfig fastJsonConfiguration = new DefaultFastjsonConfig();
        withSignMessageConverter.setFastJsonConfig(fastJsonConfiguration.getFastJsonConfig());
        withSignMessageConverter.setSupportedMediaTypes(fastJsonConfiguration.getSupportedMediaType());
        return withSignMessageConverter;
    }
}
