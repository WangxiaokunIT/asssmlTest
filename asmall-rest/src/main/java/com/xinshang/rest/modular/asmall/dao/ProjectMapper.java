package com.xinshang.rest.modular.asmall.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xinshang.rest.modular.asmall.dto.ProjectDTO;
import com.xinshang.rest.modular.asmall.model.Project;
import com.xinshang.rest.modular.asmall.vo.ProjectVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 发布招募信息申请 Mapper 接口
 * </p>
 *
 * @author lvyingkai
 * @since 2019-10-16
 */
public interface ProjectMapper extends BaseMapper<Project> {

    /**
     * 获取项目
     * @param param
     * @param page
     * @return
     */
    List<ProjectVO> showProject(@Param("param") ProjectDTO param, Page<ProjectVO> page);

    /**
     * 根据id获取招募详情信息
     * @param param
     * @return
     */
    ProjectVO showProjectDetail(@Param("param") ProjectDTO param);
}
