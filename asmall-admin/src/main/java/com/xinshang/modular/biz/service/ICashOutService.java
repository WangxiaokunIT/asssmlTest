package com.xinshang.modular.biz.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.xinshang.modular.biz.dto.CashOutDTO;
import com.xinshang.modular.biz.dto.CashOutDTO;
import com.xinshang.modular.biz.model.CashOut;
import com.baomidou.mybatisplus.service.IService;
import com.xinshang.modular.biz.vo.CashOutVO;
import com.xinshang.modular.biz.vo.CashOutVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 提现审核 服务类
 * </p>
 *
 * @author lvyingkai
 * @since 2019-10-22
 */
public interface ICashOutService extends IService<CashOut> {

    /**
     * 根据查询条件获取审核信息
     * @param param
     * @param page
     * @return
     */
    List<CashOutVO> showCashOut(CashOutDTO param, Page<CashOutVO> page);


    /**
     * 提现申请
     * @param cashOut
     */
    void updateCashOut(CashOut cashOut) throws Exception;

    /**
     * 提现异步回调修改
     * @param bizOrderNo
     */
    void updateCashOut(String bizOrderNo, Integer state);

    /**
     * 提现调用通联接口
     * @param cashOut
     * @throws Exception
     */
    void cashOut(CashOut cashOut) throws Exception ;

}
