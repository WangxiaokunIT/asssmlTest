package com.xinshang.rest.modular.asmall.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xinshang.rest.modular.asmall.dto.LogDTO;
import com.xinshang.rest.modular.asmall.model.ClientLogMoneys;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 加盟客户记录 Mapper 接口
 * </p>
 *
 * @author zhoushuai
 * @since 2019-10-24
 */
@Repository
public interface ClientLogMoneysMapper extends BaseMapper<ClientLogMoneys> {
    /**
     * 获取项目
     * @param param
     * @param page
     * @return
     */
    List<ClientLogMoneys> showClientLog(@Param("param") LogDTO param, Page<ClientLogMoneys> page);
}
