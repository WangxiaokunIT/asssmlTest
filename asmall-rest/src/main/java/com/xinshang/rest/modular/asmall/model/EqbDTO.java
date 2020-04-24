package com.xinshang.rest.modular.asmall.model;

import lombok.Data;

@Data
public class EqbDTO {
    private String action;//标记该通知的业务类型，该通知固定为：SIGN_FLOW_UPDATE
    private String flowId;//流程id
    private String accountId;//签署人的accountId
    private String authorizedAccountId;//签约主体的账号id（个人/企业）；如签署人本签署，则返回签署人账号id；如签署人代机构签署，则返回机构账号id ,当存在多个签约主体时，返回多个，并逗号隔开
    private String thirdOrderNo;//本次签署任务对应指定的第三方业务流水号id，当存在多个第三方业务流水号id时，返回多个，并逗号隔开
    private String order;//签署人的签署顺序
    private String signTime;//签署时间或拒签时间 格式yyyy-MM-dd HH:MM:SS
    private Integer signResult;//签署结果 2:签署完成
    private String resultDescription;//拒签或失败时，附加的原因描述
    private String timestamp;
}
