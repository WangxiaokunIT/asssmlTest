package com.xinshang.modular.biz.service.impl;

import com.xinshang.modular.biz.dao.HomeMapper;
import com.xinshang.modular.biz.dto.ParameterDTO;
import com.xinshang.modular.biz.service.IHomeService;
import com.xinshang.modular.biz.vo.HomeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 首页
 * @author lyk
 */
@Service
public class HomeServiceImpl implements IHomeService {

    @Autowired
    private HomeMapper homeMapper;

    @Override
    public List<HomeVO> showUserNum(ParameterDTO parameterDto) {
        return homeMapper.showUserNum(parameterDto);
    }

    @Override
    public List<HomeVO> showStatisticsSystem(ParameterDTO parameterDto) {
        return homeMapper.showStatisticsSystem(parameterDto);
    }

    @Override
    public HomeVO showJoinIn(ParameterDTO parameterDto) {
        return homeMapper.showJoinIn(parameterDto);
    }

    @Override
    public HomeVO showEquity(ParameterDTO parameterDto) {
        return homeMapper.showEquity(parameterDto);
    }
}
