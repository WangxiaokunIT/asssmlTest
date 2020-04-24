package com.xinshang.rest.common.util.wk.helper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/***
 * @Description: 场景式存证_常用配置信息类
 * @Team: 公有云技术支持小组
 * @Author: 天云小生
 * @Date: 2018年01月11日
 */
@Component
@Slf4j
public class SceneConfig {

	// 编码格式
	public static  String ENCODING = "UTF-8";

	// 哈希算法
	public static  String ALGORITHM = "HmacSHA256";

    // 项目ID(公共应用ID)-模拟环境,正式环境下贵司将拥有独立的应用ID
    public static  String PROJECT_ID;

    // 项目Secret(公共应用Secret)-模拟环境,正式环境下贵司将拥有独立的应用Secret
    public static  String PROJECT_SECRET ;

    public static  String NOTICE_DEVELOPER_URL ;

    public static  String API_HOST ;

    @Value("${eqb.api-host}")
    public  void setApiHost(String apiHost) {
        log.info("初始化api地址:{}",apiHost);
        API_HOST = apiHost;
    }

    @Value("${eqb.project-id}")
    public  void setProjectId(String projectId) {
        log.info("初始化项目编号:{}",projectId);
        PROJECT_ID = projectId;
    }

    @Value("${eqb.project-secret}")
    public  void setProjectSecret(String projectSecret) {
        PROJECT_SECRET = projectSecret;
    }

    @Value("${eqb.notice-developer-url}")
    public  void setNoticeDeveloperUrl(String noticeDeveloperUrl) {
        NOTICE_DEVELOPER_URL = noticeDeveloperUrl;
    }
}
