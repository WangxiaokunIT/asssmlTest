package com.xinshang.rest.modular.asmall.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhangjiajia
 */
@Data
public class AllinPayQueryAccountDTO implements Serializable {


    private static final long serialVersionUID = 8897105663001569450L;
    /**
     * 商户系统用户标识
     */
    private String bizUserId;

    /**
     * 总额
     */
    private Long allAmount;

    /**
     * 冻结额
     */
    private Long freezenAmount;


}
