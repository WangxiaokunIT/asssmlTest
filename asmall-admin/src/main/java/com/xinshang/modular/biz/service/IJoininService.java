package com.xinshang.modular.biz.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.xinshang.core.base.tips.Tip;

import com.xinshang.modular.biz.dto.JoininDTO;
import com.xinshang.modular.biz.model.ContractInfo;
import com.xinshang.modular.biz.model.Joinin;
import com.baomidou.mybatisplus.service.IService;
import com.xinshang.modular.biz.model.JoininInfo;
import com.xinshang.modular.biz.model.RepayPlan;
import com.xinshang.modular.biz.vo.JoininVO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 加盟表 服务类
 * </p>
 *
 * @author zhoushuai
 * @since 2019-10-23
 */
public interface IJoininService extends IService<Joinin> {

    /**
     * 加盟列表
     */

    List<JoininInfo> joinList(JoininDTO param, Page<JoininInfo> page);
    /**
     *     通过项目ID查询信息
     */
    ContractInfo joinListByProjectId(Long projectId);

    /**
     * 根据招募id获取加盟信息
     * @param projectId
     * @return
     */
    List<JoininVO> showJoinin(Long projectId);


    /**
     * 还款
     */
    void updateState(String joininIds,String bizOrderNo);
    /**
     * 消费申请
     * @param
     * @return
     */
    Map<String, String> payGoods(String joininIds,Long projectId,BigDecimal decimalA);

    /**
     * 消费确认
     * @param joininDTO
     * @return
     */
    Tip payConfirm(JoininDTO joininDTO);

}
