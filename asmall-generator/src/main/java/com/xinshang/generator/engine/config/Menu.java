package com.xinshang.generator.engine.config;

import com.baomidou.mybatisplus.activerecord.Model;
import lombok.Data;
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
public class Menu extends Model<Menu> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Integer id;
    /**
     * 菜单编号
     */
    private String code;
    /**
     * 菜单父编号
     */
    private Integer parentId;
    /**
     * 搜索码
     */
    private String seq;
    /**
     * 菜单名称
     */
    private String name;
    /**
     * 菜单图标
     */
    private String icon;
    /**
     * url地址
     */
    private String url;
    /**
     * 菜单排序号
     */
    private Integer sortNum;
    /**
     * 菜单层级
     */
    private Integer level;
    /**
     * 是否是菜单（1：是  0：不是）
     */
    private Integer isMenu;
    /**
     * 备注
     */
    private String remark;
    /**
     * 菜单状态 :  1:启用   0:不启用
     */
    private Integer state;
    /**
     * 是否打开:    1:打开   0:不打开
     */
    private Integer isOpen;

    /**
     * 创建人
     */
    private Integer creator;
    /**
     * 创建时间
     */
    private Date gmtCreate;
    /**
     * 修改人
     */
    private Integer modifier;
    /**
     * 修改时间
     */
    private Date gmtModified;


    public Integer getCreator() {
        return creator;
    }

    public void setCreator(Integer creator) {
        this.creator = creator;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
