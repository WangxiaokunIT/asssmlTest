package com.xinshang.rest.modular.asmall.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhangjiajia
 */
@Data
public class APCreateMemberRespDTO implements Serializable {

    private static final long serialVersionUID = 1806786008504124841L;
    /**
     * 通商云用户唯一标识
     */
    private String userId;

    /**
     * 商户系统用户标识
     */
    private String bizUserId;
}
