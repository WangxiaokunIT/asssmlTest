package com.xinshang.modular.biz.model;

import com.baomidou.mybatisplus.enums.IdType;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xinshang.core.annotation.DictField;
import com.xinshang.core.annotation.JoinField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * <p>
 * 商品表
 * </p>
 *
 * @author sunhao
 * @since 2019-10-17
 */
@TableName("tb_item")
@Data
public class Item extends Model<Item> {

    private static final long serialVersionUID = 1L;

    /**
     * 商品ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商品编码
     */
    @TableField("item_number")
    private String itemNumber;

    /**
     * 商品分类ID
     */
    @TableField("category_id")
    @JoinField(tableName = "tb_item_category",relationColumn = "id",targetColumn = "name")
    private Integer categoryId;


    /**
     * 所属分类
     */
    @DictField("goods_type")
    private Integer cid;

    /**
     * 商品标题
     */
    private String title;
    /**
     * 商品简介
     */
    @TableField("sell_point")
    private String sellPoint;
    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * vip价格
     */
    @TableField("vip_discount")
    private BigDecimal vipDiscount;

    /**
     * 商品利润
     */
    private BigDecimal profits;

    /**
     * 库存数量
     */
    private Integer num;

    /**
     * 库存预警数量
     */
    @TableField("stock_warning")
    private Integer stockWarning;

    /**
     * 售卖数量限制
     */
    @TableField("limit_num")
    private Integer limitNum;
    /**
     * 商品图片
     */
    private String image;

    /**
     * 商品状态 -1下架1正常
     */
    @DictField("item_status")
    private Integer status;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date created;
    /**
     * 更新时间
     */
    private Date updated;

    /**
     * 销量
     */
    @TableField("num_all")
    private Integer numAll;

    /**
     * 详情视频
     */
    @TableField("detail_video")
    private String detailVideo;

    /**
     * 详情页轮播图
     */
    @TableField("detail_banner")
    private String detailBanner;

    @TableField(exist = false)
    private String[] detailBannerList;

    /**
     * 详情视频封面
     */
    @TableField("info_video_image")
    private String infoVideoImage;

    /**
     * 审核状态[审批状态0未申请1 申请中 2审核通过 3审核不通过 4取消]
     */
    @TableField("audit_status")
    private Integer auditStatus;

    /**
     * 运费
     */
    @TableField("freight")
    private BigDecimal freight;

    /**
     * 商品描述[富文本]
     */
    @TableField("detail")
    private String detail;

    /**
     * 规格属性信息
     */
    @TableField("attr_info")
    private String attrInfo;

    /**
     * 规格
     */
    @TableField(exist = false)
    private String specsValue;

    /**
     * 规格库存预警数量
     */
    @TableField(exist = false)
    private Integer specsStockWarningNum;

    /**
     * 快递费用区域
     */
    @TableField(exist = false)
    private String postageArea;

    /**
     * 快递费用
     */
    @TableField(exist = false)
    private String postageFee;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }


}
