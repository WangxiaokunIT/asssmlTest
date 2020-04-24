package com.xinshang.modular.system.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 上传文件
 * </p>
 *
 * @author zhangjiajia123
 * @since 2018-07-03
 */

@Data
@TableName("sys_file")
public class File extends Model<File> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Integer id;
    /**
     * 文件名
     */
    private String name;
    /**
     * 类别id
     */
    @TableField("category_id")
    private String categoryId;
    /**
     * 原始名称
     */
    @TableField("original_name")
    private String originalName;
    /**
     * 文件类型
     */
    private String type;
    /**
     * 查看次数
     */
    @TableField("view_amt")
    private Integer viewAmt;
    /**
     * 下载次数
     */
    @TableField("download_amt")
    private Integer downloadAmt;
    /**
     * 文件大小
     */
    private BigDecimal size;
    /**
     * 状态(0无效,1有效)
     */
    private Integer state;
    /**
     * md5值
     */
    @TableField("md5_val")
    private String md5Val;
    /**
     * 保存路径
     */
    @TableField("save_path")
    private String savePath;
    /**
     * 描述
     */
    private String remark;
    /**
     * 创建时间
     */
    @TableField("gmt_create")
    private Date gmtCreate;
    /**
     * 更新时间（审批时间）
     */
    @TableField("gmt_modified")
    private Date gmtModified;
    /**
     * 创建人
     */
    private Integer creator;
    /**
     * 修改人
     */
    private Integer modifier;

    /**
     * 存储类型1 本地,2 OSS
     */
    @TableField("store_type")
    private Integer storeType;
    /**
     * 创建人姓名
     * @return
     */
    @TableField(exist = false)
    private String creatorName;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
