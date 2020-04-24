package com.xinshang.rest.modular.asmall.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 银行卡信息
 * </p>
 *
 * @author sunhao
 * @since 2019-10-24
 */
@Data
@ApiModel("首页轮播图")
@TableName("tb_banner")
public class Banner extends Model<Banner> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID", example = "1", required = true)
    private Integer id;
    /**
     * 图片路径
     */
    @TableField("banner_path")
    @ApiModelProperty(value = "图片路径", required = true)
    private String bannerPath;
    /**
     * 链接
     */
    private String link;
    /**
     * 排序
     */
    @TableField("sort_num")
    @ApiModelProperty(value = "排序")
    private Integer sortNum;
    /**
     * 是否显示[0:不显示,1:显示]
     */
    @ApiModelProperty(value = "是否显示")
    private Integer state;
    /**
     * 创建时间
     */
    @TableField("create_time")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    /**
     * 修改时间
     */
    @TableField("update_time")
    @ApiModelProperty(value = "修改时间")
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
