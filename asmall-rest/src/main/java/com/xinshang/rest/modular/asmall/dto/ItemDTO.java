package com.xinshang.rest.modular.asmall.dto;

import com.xinshang.rest.factory.PageFactory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.math.BigDecimal;

/**
 * @author sunha
 * @since 2019/10/1818:02
 */
@Data
@ApiModel("商品信息")
public class ItemDTO extends PageFactory {

    /**
     * 商品ID
     */
    @ApiModelProperty(value = "商品ID",required=true)
    private Long id;
    /**
     * 商品分类
     */
    @ApiModelProperty(value = "商品分类")
    private Integer categoryId;

    /**
     * 商品标题
     */
    @ApiModelProperty(value = "商品标题")
    private String title;
    /**
     * 商品描述
     */
    @ApiModelProperty(value = "商品描述")
    private String sellPoint;
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
     * 商品类型
     */
    @ApiModelProperty(value = "商品类型")
    private Integer cid;
    /**
     * 售卖数量限制
     */
    @ApiModelProperty(value = "售卖数量限制")
    private Integer limitNum;
    /**
     * 首页轮播图
     */
    @ApiModelProperty(value = "首页轮播图")
    private String banner;
    /**
     * 商品状态 -1下架1正常
     */
    @ApiModelProperty(value = "商品状态")
    private Integer status;
    /**
     * 销量
     */
    @ApiModelProperty(value = "销量")
    private Integer numAll;
    /**
     * 详情视频
     */
    @ApiModelProperty(value = "详情视频")
    private String detailVideo;
    /**
     * 详情页轮播图
     */
    @ApiModelProperty(value = "详情页轮播图")
    private String detailBanner;

    /**
     * 运费
     */
    @ApiModelProperty(value = "运费")
    private BigDecimal freight;

    /**
     * 商品详情
     */
    @ApiModelProperty(value = "商品详情")
    private String detail;
}
