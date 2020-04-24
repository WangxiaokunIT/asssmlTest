package com.xinshang.modular.system.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.xinshang.core.annotation.DictField;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 菜单表
 * </p>
 *
 * @author zhangjiajia
 * @since 2017-07-11
 */

@Data
@TableName("sys_menu")
public class Menu extends Model<Menu> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 菜单编号
     */
    private String code;
    /**
     * 菜单父编号
     */

    @TableField("parent_id")
    private Integer parentId;
    /**
     * 搜索码
     */
    private String seq;
    /**
     * 菜单名称
     */
    @NotNull
    private String name;
    /**
     * 菜单图标
     */
    private String icon;
    /**
     * url地址
     */
    @NotNull
    private String url;
    /**
     * 菜单排序号
     */
    @TableField("sort_num")
    private Integer sortNum;
    /**
     * 菜单层级
     */
    private Integer level;
    /**
     * 是否是菜单（1：是  0：不是）
     */
    @DictField("sys_menu_is_menu")
    @TableField("is_menu")
    private Integer isMenu;
    /**
     * 备注
     */
    private String remark;
    /**
     * 菜单状态 :  1:启用   0:不启用
     */
    @DictField("sys_menu_state")
    private Integer state;
    /**
     * 是否打开:    1:打开   0:不打开
     */
    @TableField("is_open")
    private Integer isOpen;

    /**
     * 创建人
     */
    private Integer creator;
    /**
     * 创建时间
     */
    @TableField("gmt_create")
    private Date gmtCreate;
    /**
     * 修改人
     */
    private Integer modifier;
    /**
     * 修改时间
     */
    @TableField("gmt_modified")
    private Date gmtModified;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
