package com.xinshang.modular.biz.dto;

import lombok.Data;

/**
 * @author zhangjiajia
 */
@Data
public class RealNameAuthDTO {

    /**
     * 商户系统用户标识
     */
    private String bizUserId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 证件类型
     */
    private Integer identityType;

    /**
     * 证件号码
     */
    private String identityNo;

}
