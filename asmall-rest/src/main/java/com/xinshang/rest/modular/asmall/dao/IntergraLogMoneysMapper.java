package com.xinshang.rest.modular.asmall.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xinshang.rest.modular.asmall.dto.LogDTO;
import com.xinshang.rest.modular.asmall.model.ClientLogMoneys;
import com.xinshang.rest.modular.asmall.model.IntegralLogMoneys;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 加盟积分记录 Mapper 接口
 * </p>
 *
 * @author zhoushuai
 * @since 2019-10-24
 */
@Repository
public interface IntergraLogMoneysMapper extends BaseMapper<IntegralLogMoneys> {
    /**
     * 获取项目
     * @param param
     * @param page
     * @return
     */
    List<IntegralLogMoneys> showIntergraLog(@Param("param") LogDTO param, Page<IntegralLogMoneys> page);
}
