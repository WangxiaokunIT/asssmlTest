package com.xinshang.modular.system.service.impl;

import com.allinpay.yunst.sdk.bean.YunRequest;
import com.xinshang.constant.Constants;
import com.xinshang.core.util.AllinPayUtil;
import com.xinshang.modular.biz.dto.AllinPayResponseDTO;
import com.xinshang.modular.biz.dto.AllinPaySyncOrderStateDTO;
import com.xinshang.modular.system.service.IAllinPayOrderStateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import java.util.Optional;

/**
 * @author zhangjiajia
 */
@Service
@Slf4j
public class AllinPayOrderStateServiceImpl implements IAllinPayOrderStateService {

    @Override
    public AllinPaySyncOrderStateDTO getOrderDetail(String bizOrderNo) {
        //发送请求到通联
        YunRequest request = new YunRequest("OrderService", "getOrderDetail");
        request.put("bizOrderNo", bizOrderNo);
        log.info("查询订单状态信息请求到通联:{}", request);
        Optional<AllinPayResponseDTO<AllinPaySyncOrderStateDTO>> response = AllinPayUtil.request(request, AllinPaySyncOrderStateDTO.class);
        //通联同步失败一定要抛异常回滚数据库
        Assert.isTrue(Constants.SUCCESS_CODE.equals(response.get().getStatus()), () -> {
            log.warn("查询订单状态信息请求到通联失败:{}", response);
            return response.get().getMessage();
        });
        return response.get().getSignedValue();
    }

}
