package com.xinshang.modular.biz.vo;



import com.xinshang.core.annotation.DictField;
import lombok.Data;

import java.util.Date;


/**
 * <p>
 * 绑定银行卡信息
 * </p>
 *
 * @author sunhao
 * @since 2019-10-24
 */
@Data
public class BankVO {

    /**
     * id
     */
    private Integer id;
    /**
     * 商户用户id
     */
    private String bizUserId ;
    /**
     * 银行卡号
     */
    private String cardNo;
    /**
     * 银行名称
     */
    private String bankName;
    /**
     * 银行代码
     */
    private String bankCode;
    /**
     *银行卡类型
     */
    @DictField("bank_bank_type")
    private Integer cardType;
    /**
     * 银行预留手机号
     */
    private String phone;
    /**
     * 如果是企业会员，请填写法
     * 人姓名
     */
    private String name;
    /**
     * 证件类型
     */
    private Integer identityType;
    /**
     * 证件号码
     */
    private String identityNo;

    /**
     * 通联绑定银行卡的验证码
     */
    private String verificationCode;

    /**
     * 是否默认
     */
    @DictField("bank_is_default")
    private Integer isDefault;

    /**
     * 银行卡属性
     */

    private Integer bankCardPro;

    /**
     * 流水号
     */
    private String tranceNum;

    /**
     * 创建时间
     */
    private Date createTime;
}
