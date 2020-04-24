package com.xinshang;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SpringBoot方式启动类
 *
 * @author zhangjiajia
 * @date 2017/5/21 12:06
 */
@Slf4j
@SpringBootApplication
public class AdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
        log.info("\033[31mAdminApplication has started, laomao said good good study day day up, come on baby, let's do it!");
    }
}
