package com.xinshang.rest.modular.asmall.dto;

import com.xinshang.rest.factory.PageFactory;
import lombok.Data;

/**
 * @Auther: wangxiaokun
 * @Date: 2019/12/5:13:58
 * @Description:
 */
@Data
public class OrderListDTO extends PageFactory {

    /**
     * 客户id
     */
    private String userId;

    /**
     * 订单状态
     */
    private Integer status;


}
