package com.xinshang.rest.modular.asmall.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xinshang.rest.modular.asmall.dto.LogDTO;
import com.xinshang.rest.modular.asmall.dto.ProjectDTO;
import com.xinshang.rest.modular.asmall.model.ClientLogMoneys;
import com.xinshang.rest.modular.asmall.model.Project;
import com.xinshang.rest.modular.asmall.vo.ProjectVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 资金流水 Mapper 接口
 * </p>
 *
 * @author jyz
 * @since 2019-10-25
 */
public interface LogMapper extends BaseMapper<ClientLogMoneys> {

    /**
     * 获取项目
     * @param param
     * @param page
     * @return
     */
    List<ClientLogMoneys> showClientLog(@Param("param") LogDTO param, Page<ClientLogMoneys> page);

}
