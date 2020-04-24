package com.xinshang.modular.biz.vo;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 审核记录表
 * </p>
 *
 * @author lvyingkai
 * @since 2019-10-18
 */
@Data
public class ExamineVO  {


    private Long id;
    /**
     * 审核类型 1招募发布
     */
    private Integer type;
    /**
     * 审批的项目id
     */
    @TableField("project_id")
    private Long projectId;
    /**
     * 审核状态 1 申请 2审核通过 3审核不通过 4取消
     */
    private Integer state;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 申请人id/审核人id
     */
    private Long userId;
    /**
     * 申请人/审核人姓名
     */
    private String userName;
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private String typeName;

    private String time;

    private String stateName;
}
