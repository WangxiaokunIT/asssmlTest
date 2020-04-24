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

/**
 * <p>
 * 收货地址
 * </p>
 *
 * @author wangxiaokun
 * @since 2019-10-27
 */
@TableName("tb_address")
@ApiModel("收货地址")
@Data
public class Address extends Model<Address> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "address_id", type = IdType.AUTO)
    @ApiModelProperty(value = "地址ID",required=true)
    private Long addressId;
    /**
     * 用户id
     */
    @TableField("user_id")
    @ApiModelProperty(value = "用户ID",required=true)
    private Long userId;
    /**
     * 收货人
     */
    @TableField("user_name")
    @ApiModelProperty(value = "收货人")
    private String userName;
    /**
     * 收货人电话
     */
    @ApiModelProperty(value = "收货人电话")
    private String tel;
    /**
     * 详细地址
     */
    @TableField("street_name")
    @ApiModelProperty(value = "详细地址")
    private String streetName;
    /**
     * 是否默认[0:否,1:是]
     */
    @TableField("is_default")
    @ApiModelProperty(value = "是否默认[0:否,1:是]")
    private Integer isDefault;
    /**
     * 省市区
     */
    @TableField("prov_city_area")
    @ApiModelProperty(value = "省市区")
    private String provCityArea;


    @Override
    protected Serializable pkVal() {
        return this.addressId;
    }

    @Override
    public String toString() {
        return "Address{" +
        "addressId=" + addressId +
        ", userId=" + userId +
        ", userName=" + userName +
        ", tel=" + tel +
        ", streetName=" + streetName +
        ", isDefault=" + isDefault +
        ", provCityArea=" + provCityArea +
        "}";
    }
}
