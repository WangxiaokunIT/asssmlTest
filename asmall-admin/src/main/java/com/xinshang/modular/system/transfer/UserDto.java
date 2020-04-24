package com.xinshang.modular.system.transfer;

import lombok.Data;


import java.util.Date;

/**
 * 用户传输bean
 * 
 * @author zhangjiajia
 * @date 2017/5/5 22:40
 */

@Data
public class UserDto{

	private Integer id;
	private String account;
	private String password;
	private String salt;
	private String name;
	private String fullName;
	private Date birthday;
	private Integer sex;
	private String email;
	private String phone;
	private String roleId;
	private Integer deptId;
	private Integer state;
	private Date gmtCreate;
	private Integer creator;
	private Integer modifier;
	private Date gmtModified;
	private Integer version;
	private String avatar;
	private String pinYinIndex;

}
