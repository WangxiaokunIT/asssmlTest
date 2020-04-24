package com.xinshang.modular.biz.dto;

import lombok.Data;

/**
 *
 * @author jyz
 */
@Data
public class AllinPayQueryInExpDetailDTO {
    /**
     * 商户系统用户标识
     */
    private String bizUserId;

    /**
     * 账户集编号
     */
    private String accountSetNo;

    /**
     * 开始日期
     */
    private String dateStart ;
    /**
     * 结束日期
     */
    private String dateEnd ;
    /**
     * 起始位置
     */
    private String startPosition;
    /**
     * 查询条数
     */
    private String queryNum;
}
