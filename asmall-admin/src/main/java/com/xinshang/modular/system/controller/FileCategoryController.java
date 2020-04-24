package com.xinshang.modular.system.controller;

import com.xinshang.core.base.controller.BaseController;
import com.xinshang.core.common.constant.factory.ConstantFactory;
import com.xinshang.core.common.constant.state.SystemTreeTableEnum;
import com.xinshang.core.log.LogObjectHolder;
import com.xinshang.core.node.ZTreeNode;
import com.xinshang.modular.system.model.FileCategory;
import com.xinshang.modular.system.service.IFileCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;

/**
 * 文件类别控制器
 *
 * @author fengshuonan
 * @date 2018-07-03 15:26:33
 */
@Controller
@RequestMapping("/fileCategory")
public class FileCategoryController extends BaseController {

    private String PREFIX = "/system/fileCategory/";

    @Autowired
    private IFileCategoryService fileCategoryService;

    /**
     * 跳转到文件类别首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "fileCategory.html";
    }

    /**
     * 跳转到添加文件类别
     */
    @RequestMapping("/fileCategory_add")
    public String fileCategoryAdd() {
        return PREFIX + "fileCategory_add.html";
    }

    /**
     * 跳转到修改文件类别
     */
    @RequestMapping("/fileCategory_update/{fileCategoryId}")
    public String fileCategoryUpdate(@PathVariable Integer fileCategoryId, Model model) {
        FileCategory fileCategory = fileCategoryService.getById(fileCategoryId);
        model.addAttribute("item",fileCategory);
        LogObjectHolder.me().set(fileCategory);
        return PREFIX + "fileCategory_edit.html";
    }

    /**
     * 获取文件类别列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return fileCategoryService.queryList(condition);
    }

    /**
     * 新增文件类别
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(FileCategory fileCategory) {
        fileCategoryService.insert(fileCategory);
        /**
         * 添加seq和level
         */
        ConstantFactory.me().addSeqAndLevelByIdAndParentId(SystemTreeTableEnum.FILE_CATEGORY.getTableName(),fileCategory.getId(),fileCategory.getParentId());
        return SUCCESS_TIP;
    }

    /**
     * 删除文件类别
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer fileCategoryId) {
        fileCategoryService.deleteById(fileCategoryId);
        return SUCCESS_TIP;
    }

    /**
     * 修改文件类别
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(FileCategory fileCategory) {

        fileCategoryService.updateById(fileCategory);
        /**
         * 修改seq和level
         */
        ConstantFactory.me().updateSeqAndLevelById(SystemTreeTableEnum.FILE_CATEGORY.getTableName(),fileCategory.getId());
        return SUCCESS_TIP;
    }

    /**
     * 文件类别详情
     */
    @RequestMapping(value = "/detail/{fileCategoryId}")
    @ResponseBody
    public Object detail(@PathVariable("fileCategoryId") Integer fileCategoryId) {
        return fileCategoryService.getById(fileCategoryId);
    }

    /**
     * 获取部门的tree列表
     */
    @RequestMapping(value = "/tree")
    @ResponseBody
    public List<ZTreeNode> tree() {
        List<ZTreeNode> tree;
        tree = this.fileCategoryService.tree();
        return tree;
    }
}
