package com.xinshang.modular.biz.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhangjiajia
 */
@Data
public class AllinPayQueryCompanyInfoDTO implements Serializable {

    private static final long serialVersionUID = -2406778110754439945L;
    /**
     * 商户系统用户标识
     */
    private String bizUserId;

    /**
     * 会员信息
     */
    private String memberInfo;

    /**
     * 会员类型
     */
    private String memberType;

}
