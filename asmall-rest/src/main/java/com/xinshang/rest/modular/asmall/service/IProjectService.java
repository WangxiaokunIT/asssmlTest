package com.xinshang.rest.modular.asmall.service;


import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.xinshang.rest.modular.asmall.dto.ProjectDTO;
import com.xinshang.rest.modular.asmall.model.Project;
import com.xinshang.rest.modular.asmall.vo.ProjectVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 发布招募信息申请 服务类
 * </p>
 *
 * @author lvyingkai
 * @since 2019-10-16
 */
public interface IProjectService extends IService<Project> {


    /**
     * 获取项目
     * @param param
     * @param page
     * @return
     */
    List<ProjectVO> showProject(ProjectDTO param, Page<ProjectVO> page);

    /**
     * 根据id获取招募详情信息
     * @param param
     * @return
     */
    ProjectVO showProjectDetail(ProjectDTO param);


}
