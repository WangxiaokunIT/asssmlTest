package com.xinshang.modular.biz.vo;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.xinshang.core.annotation.DictField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 供应商
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-10-21
 */
@Data
public class SupplierVO extends Model<SupplierVO> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
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
    @DictField("identity_type")
    private Integer identityType;
    /**
     * 证件号码
     */
    private String identityNo;
    /**
     * 是否由通商云进行认证[0:未认证,1:已认证]
     */
    @DictField("is_auth")
    private Integer isAuth;

    /**
     * 认证类型[1:三证,2:一证]
     */
    @DictField("auth_type")
    private Integer authType;
    /**
     * 法人姓名
     */
    private String legalName;

    /**
     * 法人对公账户
     */
    private String accountNo;
    /**
     * 开户银行名称
     */
    private String parentBankMame;
    /**
     * 开户行支行名称
     */
    private String bankName;
    /**
     * 支付行号
     */
    private String unionbank;
    /**
     * 贷款总额度
     */
    private BigDecimal loanLimit;
    /**
     * 已用贷款额度
     */
    private BigDecimal usedLoanAmount;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 商户系统用户标识
     */
    private String bizUserId;

    /**
     * 通商云用户唯一标识
     */
    private String allinpayUserId;

    /**
     * 通商云合同编号
     */
    private String contractNo;

    /**
     * 是否有效[0:正常,1:锁定]
     */
    @DictField("supplier_state")
    private Integer state;

    /**
     * 统一社会信用
     */
    private String uniCredit;

    /**
     * 营业执照号
     */
    private String businessLicense;

    /**
     * 组织机构代码
     */
    private String organizationCode;

    /**
     * 税务登记证
     */
    private String taxRegister;

    /**
     * 银行卡数量
     */
    private Integer bankCount;
    /**
     * 账户总额
     */
    private BigDecimal totleAmount;

    /**
     * 冻结金额
     */
    private BigDecimal freezingAmount;

    /**
     * 可用余额
     */
    private BigDecimal availableBalance;

    /**
     *
     * 是否设置支付密码
     */
    /**
     * 是否设置支付密码[0:未设置,1:已设置]
     */
    @DictField("is_set_pay_pwd")
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
