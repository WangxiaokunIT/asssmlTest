package com.xinshang.rest.modular.asmall.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
     * 商品编号
     */
    @TableField("item_number")
    private String itemNumber;
    /**
     * 所属分类
     */
    private Integer cid;

    /**
     * 所属分类
     */
    @TableField("category_id")
    private Integer categoryId;

    /**
     * 商品标题
     */
    private String title;
    /**
     * 商品描述
     */
    @TableField("sell_point")
    private String sellPoint;
    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * VIP价格
     */
    @TableField("vip_discount")
    private BigDecimal vipDiscount;


    /**
     * 库存数量
     */
    private Integer num;
    /**
     * 售卖数量限制
     */
    @TableField("limit_num")
    private Integer limitNum;

    /**
     * 商品利润
     */
    private BigDecimal profits;

    /**
     * 商品缩略图
     */
    private String image;

    /**
     * 商品状态 -1下架1正常
     */
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

    /**
     * 运费
     */

    private BigDecimal freight;

    /**
     * 详情视频封面
     */
    @TableField("info_video_image")
    private String infoVideoImage;

    /**
     * 商品详情
     */
    @TableField("detail")
    private String detail;

    @TableField(exist = false)
    private List<String> bannerDetailList;

    /**
     * 规格属性
     */
    @TableField("attr_info")
    private String attrInfo;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
