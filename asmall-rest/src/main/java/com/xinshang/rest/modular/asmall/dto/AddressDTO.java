package com.xinshang.rest.modular.asmall.dto;

import lombok.Data;

@Data
public class AddressDTO {

    private Long addressId;

    private Long userId;

    private String userName;

    private String tel;

    private String streetName;

    private Boolean isDefault;

    private String provCityArea;

}
