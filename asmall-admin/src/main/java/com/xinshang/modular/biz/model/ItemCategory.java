package com.xinshang.modular.biz.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xinshang.core.annotation.DictField;
import com.xinshang.core.annotation.JoinField;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 商品分类
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-11-25
 */
@TableName("tb_item_category")
@Data
public class ItemCategory extends Model<ItemCategory> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 上级分类
     */
    @TableField("parent_id")
    @JoinField(tableName="tb_item_category",relationColumn="id",targetColumn="name")
    private Integer parentId;
    /**
     * 名称
     */
    private String name;
    /**
     * 图标
     */
    private String icon;
    /**
     * 描述
     */
    private String remark;
    /**
     * 排序
     */
    @TableField("sort_num")
    private Integer sortNum;
    /**
     * 级别
     */
    private Integer level;
    /**
     * 索引值
     */
    private String seq;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;

    /**
     * 是否启用[0:禁用,1:启用]
     */
    @DictField("is_enabled")
    @TableField("is_enabled")
    private Integer isEnabled;



    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
