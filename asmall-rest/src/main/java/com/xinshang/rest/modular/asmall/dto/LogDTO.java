package com.xinshang.rest.modular.asmall.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 资金流水查询对象
 * @author jyz
 */
@Data
public class LogDTO {

    private Integer id;
    /**
     * 客户id
     */
    private Long clientId;
    /**
     * 金额（元）
     */
    private BigDecimal money;
    /**
     * 流水来源：1：充值 2：消费 3：提现
     */
    private Integer dataSrc;
    /**
     * 流水标志：1：收入 2：支出
     */
    private Integer moneyType;
    /**
     * 通联流水号
     */
    private String tradeNo;
    /**
     * 有效状态 ：1：有效 0：删除
     */
    private Integer deleteFlg;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 流水备注
     */
    private String remark;
    /**
     * 客户用户名
     */
    private String userName;

    private String bizUserId;

    /**
     * 一次查询多少条数据
     */
    private Integer limit;

    private Integer offset;

}
