package com.xinshang.modular.biz.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author sunha
 * @since 2019/10/1818:02
 */
@Data
public class ItemDTO {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date beginTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    private String auditDetail;
    /**
     * 商品ID
     */
    private Long id;
    /**
     * 商品标题
     */
    private String title;
    /**
     * 商品描述
     */
    private String sellPoint;
    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * 商品利润
     */
    private BigDecimal profits;

    /**
     * 库存数量
     */
    private Integer num;
    /**
     * 售卖数量限制
     */
    private Integer limitNum;
    /**
     * 商品缩略图
     */
    private String image;

    /**
     * 所属分类
     */
    private Integer cid;
    /**
     * 商品状态 -1下架1正常
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date created;
    /**
     * 更新时间
     */
    private Date updated;


    /**
     * 销量
     */
    private Integer numAll;

    /**
     * 详情视频
     */
    private String detailVideo;
    /**
     * 详情页轮播图
     */
    private String detailBanner;
    /**
     * 推荐理由
     */
    private String sellReason;


    /**
     * 详情视频封面
     */
    private String infoVideoImage;
    /**
     * 审核状态[0待审核1已通过-1未通过]
     */
    private Integer auditStatus;
    /**
     * 运费
     */
    private BigDecimal freight;

    /**
     * 销量
     */
    private String numAllCon;
    /**
     * 库存量
     */
    private String numCon;

    private Integer maxNum;

    private Integer minNum;

    private Integer maxNumAll;

    private Integer minNumAll;

    private Integer isWarning;
}
