package com.xinshang.modular.biz.dto;

import lombok.Data;

/**
 * 首页统计使用
 * @author lyk
 */
@Data
public class ParameterDTO {

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;
}
