package com.xinshang.modular.biz.model;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
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
public class Address extends Model<Address> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "address_id", type = IdType.AUTO)
    private Long addressId;
    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 收货人
     */
    @TableField("user_name")
    private String userName;
    /**
     * 收货人电话
     */
    private String tel;
    /**
     * 详细地址
     */
    @TableField("street_name")
    private String streetName;
    /**
     * 是否默认[0:否,1:是]
     */
    @TableField("is_default")
    private Integer isDefault;
    /**
     * 省市区
     */
    @TableField("prov_city_area")
    private String provCityArea;


    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }

    public String getProvCityArea() {
        return provCityArea;
    }

    public void setProvCityArea(String provCityArea) {
        this.provCityArea = provCityArea;
    }

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
