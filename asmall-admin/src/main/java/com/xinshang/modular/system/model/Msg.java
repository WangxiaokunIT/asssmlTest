package com.xinshang.modular.system.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xinshang.core.annotation.DictField;
import lombok.Data;
import java.io.Serializable;

/**
 * <p>
 * 系统消息
 * </p>
 *
 * @author zhangjiajia
 * @since 2018-10-12
 */

@Data
@TableName("sys_msg")
public class Msg extends Model<Msg> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 标题
     */
    @TableField("user_id")
    private Integer userId;
    /**
     * 类型
     */
    @DictField("sys_msg_type")
    private Integer type;

    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 是否已读
     */
    @DictField("sys_msg_is_read")
    @TableField("is_read")
    private Integer isRead;
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
