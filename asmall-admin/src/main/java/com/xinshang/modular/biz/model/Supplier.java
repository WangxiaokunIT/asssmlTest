package com.xinshang.modular.biz.model;

import com.baomidou.mybatisplus.enums.IdType;

import java.math.BigDecimal;
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
 * 供应商
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-10-21
 */
@Data
@TableName("tb_supplier")
public class Supplier extends Model<Supplier> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 名称
     */
    private String name;
    /**
     * 类型[0:个人,1:企业]
     */
    @DictField("supplier_type")
    private Integer type;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 证件类型[1:身份证,2:护照,3:军官证,4:回乡证,5:台胞证,6:警官证,7:士兵证,8:其他证件]
     */
    @TableField("identity_type")
    @DictField("identity_type")
    private Integer identityType;
    /**
     * 证件号码
     */
    @TableField("identity_no")
    private String identityNo;
    /**
     * 是否由通商云进行认证[0:未认证,1:已认证]
     */
    @TableField("is_auth")
    @DictField("is_auth")
    private Integer isAuth;

    /**
     * 认证类型[1:三证,2:一证]
     */
    @TableField("auth_type")
    @DictField("auth_type")
    private Integer authType;
    /**
     * 法人姓名
     */
    @TableField("legal_name")
    private String legalName;

    /**
     * 法人对公账户
     */
    @TableField("account_no")
    private String accountNo;
    /**
     * 开户银行名称
     */
    @TableField("parent_bank_mame")
    private String parentBankMame;
    /**
     * 开户行支行名称
     */
    @TableField("bank_name")
    private String bankName;
    /**
     * 支付行号
     */
    private String unionbank;
    /**
     * 贷款总额度
     */
    @TableField("loan_limit")
    private BigDecimal loanLimit;
    /**
     * 已用贷款额度
     */
    @TableField("used_loan_amount")
    private BigDecimal usedLoanAmount;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 修改时间
     */
    @TableField("update_time")
    private Date updateTime;
    /**
     * 商户系统用户标识
     */
    @TableField("biz_user_id")
    private String bizUserId;

    /**
     * 通商云用户唯一标识
     */
    @TableField("allinpay_user_id")
    private String allinpayUserId;

    /**
     * 通商云合同编号
     */
    @TableField("contract_no")
    private String contractNo;

    /**
     * 是否有效[0:正常,1:锁定]
     */
    @DictField("supplier_state")
    private Integer state;

    /**
     * 统一社会信用
     */
    @TableField("uni_credit")
    private String uniCredit;

    /**
     * 营业执照号
     */
    @TableField("business_license")
    private String businessLicense;

    /**
     * 组织机构代码
     */
    @TableField("organization_code")
    private String organizationCode;

    /**
     * 税务登记证
     */
    @TableField("tax_register")
    private String taxRegister;

    /**
     * 是否设置支付密码[0:未设置,1:已设置]
     */
    @DictField("is_set_pay_pwd")
    @TableField("is_set_pay_pwd")
    private Integer isSetPayPwd;

    /**
     * 备注
     */
    private String remark;

    /**
     * 通联审核状态[1:待审核,2:审核成功,3:审核失败]
     * @return
     */
    @TableField("allin_pay_state")
    private Integer allinPayState;

    /**
     * 未通过原因
     */
    @TableField("fail_reason")
    private String failReason;

    /**
     * 公司地址
     */
    @TableField("company_address")
    private String companyAddress;



    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
