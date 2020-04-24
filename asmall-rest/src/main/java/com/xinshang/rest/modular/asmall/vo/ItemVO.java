package com.xinshang.rest.modular.asmall.vo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.xinshang.rest.modular.asmall.model.Item;
import com.xinshang.rest.modular.asmall.model.SpecsAttribute;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * @author sunha
 * @since 2019/10/1818:02
 */
@Data

public class ItemVO implements Serializable {


    private static final long serialVersionUID = -1802154921777859489L;

    /**
     * 商品标题
     */
    @ApiModelProperty(value = "商品标题")
    private String title;
    /**
     * 商品标题
     */
    @ApiModelProperty(value = "商品编号")
    private String itemNumber;

    /**
     * 商品描述
     */
    @TableField("sell_point")
    @ApiModelProperty(value = "商品简述")
    private String sellPoint;

    /**
     * 商品价格
     */
    @ApiModelProperty(value = "商品价格")
    private BigDecimal price;

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
     * 商品图
     */
    @ApiModelProperty(value = "商品图")
    private String image;

    /**
     * 类型
     */
    @ApiModelProperty(value = "类型")
    private Integer cid;

    /**
     * 所属分类
     */
    @TableField("category_id")
    @ApiModelProperty(value = "产品分类")
    private Integer categoryId;

    /**
     * 销量
     */
    @TableField("num_all")
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
     * 原价
     */
    @ApiModelProperty(value = "原价")
    private BigDecimal sourcePrice;

    /**
     * 运费
     */
    @ApiModelProperty(value = "运费")
    private BigDecimal freight;

    /**
     * 详情视频封面
     */
    @ApiModelProperty(value = "详情视频封面")
    private String infoVideoImage;

    /**
     * 商品详情
     */
    @ApiModelProperty(value = "商品详情")
    private String detail;

    @ApiModelProperty(value = "详情页轮播图")
    private List<String> bannerDetailList;

    /**
     * VIP价格
     */
    @TableField(exist=false)
    private BigDecimal vipDiscount;

    @ApiModelProperty(value = "规格属性")
    private List<SpecsAttributeVO> attributeList;

    @ApiModelProperty(value = "规格属性值")
    private List<SpecsAttributeValueVO> attributeValueList;

}
