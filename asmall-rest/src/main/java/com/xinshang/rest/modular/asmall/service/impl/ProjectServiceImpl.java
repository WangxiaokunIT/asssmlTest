package com.xinshang.rest.modular.asmall.service.impl;


import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xinshang.rest.modular.asmall.dao.ProjectMapper;
import com.xinshang.rest.modular.asmall.dto.ProjectDTO;
import com.xinshang.rest.modular.asmall.model.Project;
import com.xinshang.rest.modular.asmall.service.IProjectService;
import com.xinshang.rest.modular.asmall.vo.ProjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * <p>
 * 发布招募信息申请 服务实现类
 * </p>
 *
 * @author lvyingkai
 * @since 2019-10-16
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements IProjectService {

    @Autowired
    private ProjectMapper projectMapper;


    @Override
    public List<ProjectVO> showProject(ProjectDTO param, Page<ProjectVO> page) {
        return projectMapper.showProject(param, page);
    }

    @Override
    public ProjectVO showProjectDetail(ProjectDTO param) {
        return projectMapper.showProjectDetail(param);
    }
}
