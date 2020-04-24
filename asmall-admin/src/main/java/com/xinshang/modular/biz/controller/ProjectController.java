package com.xinshang.modular.biz.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xinshang.core.base.controller.BaseController;
import com.xinshang.core.common.constant.factory.PageFactory;
import com.xinshang.core.shiro.ShiroKit;
import com.xinshang.core.shiro.ShiroUser;
import com.xinshang.core.support.BeanKit;
import com.xinshang.modular.biz.dto.ProjectDTO;
import com.xinshang.modular.biz.model.Examine;
import com.xinshang.modular.biz.model.Project;
import com.xinshang.modular.biz.model.Supplier;
import com.xinshang.modular.biz.service.IExamineService;
import com.xinshang.modular.biz.service.IProjectService;
import com.xinshang.modular.biz.service.ISupplierService;
import com.xinshang.modular.biz.vo.ProjectVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * @title:招募信息申请控制器
 *
 * @author: lvyingkai
 * @since: 2019-10-16 15:48:53
 */
@Controller
@RequestMapping("/project")
@Slf4j
public class ProjectController extends BaseController {

    private String PREFIX = "/biz/project/";

    @Autowired
    private IProjectService projectService;

    @Autowired
    private IExamineService examineService;

    @Autowired
    private ISupplierService supplierService;


    /**
     * 跳转到招募信息申请首页
     */
    @RequestMapping("/index/{type}")
    public String index(@PathVariable Integer type, Model model) {
        model.addAttribute("type",type);
        return PREFIX + "project.html";
    }

    /**
     * 跳转到招募信息申请首页
     */
    @RequestMapping("/wx")
    public String indexWx() {
        return "/biz/wx/wx.html";
    }
    /**
     * 跳转到招募信息审核首页
     */
    @RequestMapping("/examine")
    public String index2() {
        return PREFIX + "project_examine.html";
    }

    /**
     * 跳转到招募完成审核首页
     */
    @RequestMapping("/success")
    public String index3() {
        return PREFIX + "project_success.html";
    }

    /**
     * 跳转到添加招募信息申请
     */
    @RequestMapping("/project_add")
    public String projectAdd() {
        return PREFIX + "project_add.html";
    }

    /**
     * 跳转到添加招募信息申请(Ant版)
     */
    @RequestMapping("/project_add_ant")
    public String projectAddAnt() {
        return PREFIX + "project_add_ant.html";
    }

    /**
     * 跳转到修改招募信息申请
     */
    @RequestMapping("/project_update/{projectId}/{type}")
    public String projectUpdate(@PathVariable Long projectId, @PathVariable Integer type, Model model) {
        model.addAttribute("projectId",projectId);
        model.addAttribute("type",type);
        return PREFIX + "project_edit.html";
    }

    /**
     * 跳转到修改招募信息申请
     */
    @RequestMapping("/project_update_ant/{projectId}/{type}")
    public String projectUpdateAnt(@PathVariable Long projectId, @PathVariable Integer type, Model model) {
        model.addAttribute("projectId",projectId);
        model.addAttribute("type",type);
        return PREFIX + "project_edit_ant.html";
    }

    /**
     * 获取招募信息申请分页列表
     */
    @RequestMapping(value = "/pageList")
    @ResponseBody
    public Object pageList(Project project) {
        Page<Project> page = new PageFactory<Project>().defaultPage();
        Map<String, Object> beanMap = BeanKit.beanToMap(project,true);
        EntityWrapper<Project> wrapper = new EntityWrapper<>();
        wrapper.allEq(beanMap);
        return projectService.selectPage(page,wrapper);
    }

    /**
     * 获取招募信息申请列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(Project project) {
        Map<String, Object> beanMap = BeanKit.beanToMap(project,true);
        EntityWrapper<Project> wrapper = new EntityWrapper<>();
        wrapper.allEq(beanMap);
        return projectService.selectList(wrapper);
    }

    /**
     * 获取招募信息申请列表
     */
    @RequestMapping(value = "/list_up")
    @ResponseBody
    public Object listUp(ProjectDTO project) {
        Page<ProjectVO> page = new PageFactory<ProjectVO>().defaultPage();
        page.setRecords(projectService.listUp(project, page));
        return page;
    }

    /**
     * 新增招募信息申请
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Project project) {
        ShiroUser user = ShiroKit.getUser();
        project.setState(1);
        project.setCreateTime(new Date());
        project.setCreateUserId(Long.valueOf(user.getId()));
        project.setUnit("月");
        projectService.insert(project);
        // 增加申请记录
        Examine examine = new Examine();
        examine.setUserId(Long.valueOf(user.getId()));
        examine.setUserName(user.getName());
        examine.setCreateTime(new Date());
        examine.setProjectId(project.getId());
        examine.setState(1);
        examine.setType(1);
        examineService.insert(examine);
        // 修改供应商的已用余额
        Supplier supplier = supplierService.selectById(project.getSupplierId());
        supplier.setUsedLoanAmount(supplier.getUsedLoanAmount() == null ? project.getMaxMoney()
                : (project.getMaxMoney().add(supplier.getUsedLoanAmount())));
        supplierService.updateById(supplier);

        return SUCCESS_TIP;
    }

    /**
     * 删除招募信息申请
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam String projectIds) {
        if(StrUtil.isNotBlank(projectIds)) {
            projectService.deleteBatchIds(Arrays.asList(projectIds.split(",")));
        }
        return SUCCESS_TIP;
    }

    /**
     * 修改招募信息
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Project project, String content) {
        projectService.updateState(project, content);
        return SUCCESS_TIP;
    }

    /**
     * 修改招募信息不含审核
     */
    @RequestMapping(value = "/updateProject")
    @ResponseBody
    public Object updateProject(Project project) {
        project.setUpdateTime(new Date());
        projectService.updateById(project);
        return SUCCESS_TIP;
    }

    /**
     * 招募信息申请详情
     */
    @RequestMapping(value = "/detail/{projectId}")
    @ResponseBody
    public Object detail(@PathVariable("projectId") Integer projectId) {
        return projectService.selectById(projectId);
    }


    /**
     * 获取项目编号
     * @return
     */
    @RequestMapping(value = "/getMaxNumber")
    @ResponseBody
    public Object getMaxNumber() {
        return projectService.getMaxNumber();
    }

}
