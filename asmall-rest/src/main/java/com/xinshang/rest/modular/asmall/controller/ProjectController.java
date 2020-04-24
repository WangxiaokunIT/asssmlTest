package com.xinshang.rest.modular.asmall.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.xinshang.rest.common.util.R;
import com.xinshang.rest.modular.asmall.dto.ProjectDTO;
import com.xinshang.rest.modular.asmall.model.Project;
import com.xinshang.rest.modular.asmall.service.IProjectService;
import com.xinshang.rest.modular.asmall.vo.ProjectVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 招募信息
 * @author lyk
 */
@RestController
@RequestMapping("/project")
@Api(value = "招募信息管理",tags = "招募相关接口")
public class ProjectController {

    @Autowired
    private IProjectService projectService;

    @ApiOperation(value = "获取招募列表", notes = "获取所有招募列表", nickname = "liukx")
    @PostMapping("showProject")
    public R showProject(@RequestBody ProjectDTO projectDTO) {
        Page<ProjectVO> page = projectDTO.defaultPage();
        page.setRecords(projectService.showProject(projectDTO, page));
        return R.ok(page);
    }


    @ApiOperation(value = "获取招募详情")
    @PostMapping("showProjectDetail")
    public R showProjectDetail(@Valid @RequestBody ProjectDTO projectDTO) {
        Project project = projectService.showProjectDetail(projectDTO);
        return R.ok(project);
    }

}
