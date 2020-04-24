package com.xinshang.modular.biz.dao;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.xinshang.modular.biz.model.Bank;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xinshang.modular.biz.vo.BankVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 银行卡信息 Mapper 接口
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-11-12
 */
public interface BankMapper extends BaseMapper<Bank> {

    /**
     * 查询用户的银行卡信息
     * @param masterId
     * @param type
     * @param page
     * @return
     */
    List<BankVO> selectBankInfo(@Param("masterId") Integer masterId,@Param("type") Integer type,@Param("page") Pagination page);
}
