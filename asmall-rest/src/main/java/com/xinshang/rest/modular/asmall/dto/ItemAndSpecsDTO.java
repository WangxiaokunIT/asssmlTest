package com.xinshang.rest.modular.asmall.dto;


import com.xinshang.rest.factory.PageFactory;
import com.xinshang.rest.modular.asmall.model.ItemSpecs;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author zhangjiajia
 * @since 2019年11月30日09:49:21
 */
@Data
@ApiModel("商品信息")
public class ItemAndSpecsDTO extends PageFactory {



    /**
     * 商品ID
     */
    private Long id;

    /**
     * 商品编号
     */
    @ApiModelProperty(value = "商品编号")
    private String itemNumber;

    /**
     * 商品类型
     */
    @ApiModelProperty(value = "商品类型")
    private Integer cid;

    /**
     * 商品分类ID
     */
    @ApiModelProperty(value = "商品分类")
    private Integer categoryId;

    /**
     * 商品标题
     */
    @ApiModelProperty(value = "商品标题")
    private String title;

    /**
     * 商品价格
     */
    @ApiModelProperty(value = "商品价格")
    private BigDecimal price;

    /**
     * vip价格
     */
    @ApiModelProperty(value = "vip价格")
    private BigDecimal vipDiscount;

    /**
     * 库存数量
     */
    @ApiModelProperty(value = "库存数量")
    private Integer num;

    /**
     * 售卖数量限制
     */
    @ApiModelProperty(value = "售卖数量限制")
    private Integer limitNum;

    /**
     * 运费
     */
    @ApiModelProperty(value = "运费")
    private BigDecimal freight;

    /**
     * 利润
     */
    private BigDecimal profits;

    /**
     * 商品图片
     */
    private String image;
    /**
     * 商品规格
     */
    private List<ItemSpecs> itemSpecsList;

    /**
     * 要购买数量
     */
    private Integer buyNum;
}
