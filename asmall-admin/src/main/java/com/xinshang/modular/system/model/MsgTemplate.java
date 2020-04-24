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
 * 消息模版
 * </p>
 *
 * @author zhangjiajia
 * @since 2018-10-09
 */

@Data
@TableName("sys_msg_template")
public class MsgTemplate extends Model<MsgTemplate> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 模版名称
     */
    @TableField("template_name")
    private String templateName;
    /**
     * 渠道
     */
    private String channel;
    /**
     * 模版类型(0 站内信,1 短信 , 2 微信 )
     */
    @DictField("msg_type")
    private Integer type;
    /**
     * 标题
     */
    private String title;
    /**
     * 消息内容
     */
    private String content;
    /**
     * 状态(0无效,1有效)
     */
    @DictField("sys_state")
    private Integer state;
    /**
     * 描述
     */
    private String remark;
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
