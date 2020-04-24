package com.xinshang.rest.modular.asmall.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.xinshang.rest.common.util.R;
import com.xinshang.rest.modular.asmall.dto.AllinPayAsynResponseDTO;
import com.xinshang.rest.modular.asmall.dto.JoininDTO;

import com.xinshang.rest.modular.asmall.dto.OrderDTO;
import com.xinshang.rest.modular.asmall.model.ContractInfo;
import com.xinshang.rest.modular.asmall.model.JoinInner;
import com.xinshang.rest.modular.asmall.model.Joinin;
 import com.xinshang.rest.modular.asmall.vo.JoininVO;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 加盟表 服务类
 * </p>
 *
 * @author zhoushuai
 * @since 2019-10-24
 */
public interface IJoininService extends IService<Joinin> {

     R joinInner(JoinInner joinInner);

    /**
     * 查询加盟信息
     * @param
     * @return
     */
    List<JoininVO> showJoinin( Long memberId,Page<JoininVO> page);

    ContractInfo joinListByProjectId(Long projectId);

    R payGoods(JoininDTO joininDTO);
    R payConfirm(JoininDTO joininDTO);

    //加盟
    void joinin(String bizOrderNo,BigDecimal originalAmount,String buyerBizUserId,String extendInfo);

}
