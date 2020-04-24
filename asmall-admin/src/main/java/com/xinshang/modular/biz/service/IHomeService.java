package com.xinshang.modular.biz.service;

import com.xinshang.modular.biz.dto.ParameterDTO;
import com.xinshang.modular.biz.vo.HomeVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 首页
 * @author lyk
 */
public interface IHomeService {

    /**
     * 根据条件获取对应区域代理的用户数量和vip数量
     * @param parameterDto
     * @return
     */
    List<HomeVO> showUserNum(ParameterDTO parameterDto);

    /**
     * 获取管理员每月的时间，金额，利润，佣金
     * @param parameterDto
     * @return
     */
    List<HomeVO> showStatisticsSystem(ParameterDTO parameterDto);

    /**
     * 加盟额
     * @param parameterDto
     * @return
     */
    HomeVO showJoinIn(ParameterDTO parameterDto);

    /**
     * 待还权益额
     * @param parameterDto
     * @return
     */
    HomeVO showEquity(ParameterDTO parameterDto);
}
