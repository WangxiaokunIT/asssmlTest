package com.xinshang.modular.biz.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.xinshang.modular.biz.dto.ProjectDTO;
import com.xinshang.modular.biz.model.Project;
import com.xinshang.modular.biz.vo.ProjectVO;

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
     * 生成项目编号
     * @return
     */
    String getMaxNumber();

    /**
     * 获取项目列表（增强版）
     * @param param
     * @param page
     * @return
     */
    List<ProjectVO> listUp(ProjectDTO param, Page<ProjectVO> page);

    /**
     * 更新状态
     * @param project
     * @param content
     */
    void updateState(Project project, String content);

    /**
     * 解冻
     * @param project
     */
    void thawMoney(Project project);

    /**
     * 修改招募信息
     * @param project
     */
    void updateProject(Project project);

}
