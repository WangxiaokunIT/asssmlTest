package com.xinshang.rest.modular.asmall.service.impl;


import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.allinpay.yunst.sdk.bean.YunRequest;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xinshang.rest.common.util.AllinPayUtil;
import com.xinshang.rest.config.constant.Constants;
import com.xinshang.rest.config.properties.AllinPayProperties;
import com.xinshang.rest.modular.asmall.dao.ClientLogMoneysMapper;
import com.xinshang.rest.modular.asmall.dao.LogMapper;
import com.xinshang.rest.modular.asmall.dto.*;
import com.xinshang.rest.modular.asmall.model.Account;
import com.xinshang.rest.modular.asmall.model.ClientLogMoneys;
import com.xinshang.rest.modular.asmall.service.ILogService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;


/**
 * <p>
 * 资金流水服务类
 * </p>
 *
 * @author jyz
 * @since 2019-10-16
 */
@Service
@Slf4j
@AllArgsConstructor
public class LogServiceImpl extends ServiceImpl<LogMapper, ClientLogMoneys> implements ILogService {

    private final AllinPayProperties allinPayProperties;

    @Override
    public Page<ReceiptsPayments> showClientLog(LogDTO param) {

        Page<ReceiptsPayments> page = new Page();
        YunRequest request = new YunRequest("OrderService", "queryInExpDetail");
        request.put("bizUserId", param.getBizUserId());
        request.put("accountSetNo", allinPayProperties.getAccountSetNo());
        Date date = new Date();
        Date newDate = DateUtil.offset(date, DateField.DAY_OF_MONTH,-3);
        String format = DateUtil.format(newDate, "yyyy-MM-dd");
        request.put("dateStart", format);
        request.put("dateEnd", DateUtil.today());
        request.put("startPosition", 1);
        request.put("queryNum", 1000);
        Optional<AllinPayResponseDTO<AllinPayQueryInExpRespDetailDTO>> response = AllinPayUtil.request(request, AllinPayQueryInExpRespDetailDTO.class);
        if(response.get().getSignedValue() == null) {
            page.setRecords(null);
        } else {
            List<ReceiptsPayments> inExpDetail = response.get().getSignedValue().getInExpDetail();
            for (ReceiptsPayments receiptsPayments : inExpDetail) {
                receiptsPayments.setChgAmount(receiptsPayments.getChgAmount().divide(new BigDecimal("100"),2,BigDecimal.ROUND_DOWN));
                receiptsPayments.setCurAmount(receiptsPayments.getCurAmount().divide(new BigDecimal("100"),2,BigDecimal.ROUND_DOWN));
                String extendInfo = receiptsPayments.getExtendInfo();
                if(StrUtil.isNotEmpty(extendInfo) && extendInfo.indexOf("_") != -1){
                    receiptsPayments.setExtendInfo(receiptsPayments.getExtendInfo().split("_")[0]);
                }
            }
            page.setRecords(inExpDetail);
            page.setTotal(response.get().getSignedValue().getTotalNum());
        }
        return page;
    }

}
