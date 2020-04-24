package com.xinshang.rest.modular.asmall.dto;

import com.xinshang.rest.factory.PageFactory;
import com.xinshang.rest.modular.asmall.model.CartProduct;
import com.xinshang.rest.modular.asmall.model.Item;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author zhangjiajia
 */
@Data
public class CreateOrderQueryDTO extends PageFactory {

    /**
     * 客户id
     */
    private String memberId;

    /**
     *  应付金额
     */
    private BigDecimal orderTotal;

    /**
     * [0: 普通购买,1:参加拼团,2:会员服务,3:兑换商品]
     */
    private Integer customType;

    /**
     *  收货地址
     */
    private String address;

    /**
     *  收货人姓名
     */
    private String consignee;

    /**
     *  收货人手机号
     */
    private String phone;

    /**
     * 用户要购买的商品
     */
    private List<OrderBuyGoodsDTO> goodsList;


    /**买家留言**/
    private String buyerMessage;


    /**
     * 经过校验的商品，保存再次准备插入数据库
     */
    private List<ItemAndSpecsDTO> items;

    /**
     * 总运费
     */
    private BigDecimal totleFreight;


}
