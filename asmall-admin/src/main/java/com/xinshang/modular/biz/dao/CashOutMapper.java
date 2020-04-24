package com.xinshang.modular.biz.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.xinshang.modular.biz.dto.CashOutDTO;
import com.xinshang.modular.biz.model.CashOut;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xinshang.modular.biz.vo.CashOutVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 提现审核 Mapper 接口
 * </p>
 *
 * @author lvyingkai
 * @since 2019-10-22
 */
@Repository
public interface CashOutMapper extends BaseMapper<CashOut> {

    /**
     * 根据条件获取审核信息
     * @param param
     * @param page
     * @return
     */
    List<CashOutVO> showCashOut(@Param("param") CashOutDTO param, Page<CashOutVO> page);

}
