package com.xinshang.modular.biz.model;

import com.baomidou.mybatisplus.enums.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 首页轮播图
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-11-29
 */
@TableName("tb_banner")
@Data
public class Banner extends Model<Banner> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 图片路径
     */
    @TableField("banner_path")
    private String bannerPath;
    /**
     * 链接
     */
    private String link;
    /**
     * 排序
     */
    @TableField("sort_num")
    private Integer sortNum;
    /**
     * 是否显示[0:不显示,1:显示]
     */
    private Integer state;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 修改时间
     */
    @TableField("update_time")
    private Date updateTime;
    /**
     * 关联商品
     */
    @TableField("item_number")
    private String itemNumber;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
