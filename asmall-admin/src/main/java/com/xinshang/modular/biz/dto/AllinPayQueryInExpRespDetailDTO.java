package com.xinshang.modular.biz.dto;

import com.xinshang.modular.biz.model.ReceiptsPayments;
import lombok.Data;

import java.util.List;

/**
 *
 * @author jyz
 */
@Data
public class AllinPayQueryInExpRespDetailDTO {
    /**
     * 商户系统用户标识
     */
    private String bizUserId;

    /**
     * 该账户收支明细总条数
     */
    private Long totalNum;

    /**
     * 扩展参数
     */
    private String extendInfo ;
    /**
     * 收支明细
     */
    private List<ReceiptsPayments> inExpDetail ;

    private Long allAmount;

    private Long freezenAmount;

    private String url;


}
