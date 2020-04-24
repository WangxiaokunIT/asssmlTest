package com.xinshang.modular.system.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xinshang.core.base.controller.BaseController;
import com.xinshang.core.base.tips.ErrorTip;
import com.xinshang.core.common.annotion.BussinessLog;
import com.xinshang.core.common.annotion.Permission;
import com.xinshang.core.common.constant.dictmap.DeptDict;
import com.xinshang.core.common.constant.factory.ConstantFactory;
import com.xinshang.core.common.constant.state.SystemTreeTableEnum;
import com.xinshang.core.common.exception.BizExceptionEnum;
import com.xinshang.core.exception.SystemException;
import com.xinshang.core.log.LogObjectHolder;
import com.xinshang.core.node.ZTreeNode;
import com.xinshang.core.util.ToolUtil;
import com.xinshang.modular.system.model.Dept;
import com.xinshang.modular.system.model.Position;
import com.xinshang.modular.system.model.User;
import com.xinshang.modular.system.service.IDeptService;
import com.xinshang.modular.system.service.IPositionService;
import com.xinshang.modular.system.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;

/**
 * 部门控制器
 *
 * @author fengshuonan
 * @date 2017年2月17日20:27:22
 */
@Controller
@RequestMapping("/dept")
public class DeptController extends BaseController {

    private String PREFIX = "/system/dept/";

    @Autowired
    private IDeptService deptService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IPositionService positionService;



    /**
     * 跳转到部门管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "dept.html";
    }

    /**
     * 跳转到添加部门
     */
    @RequestMapping("/dept_add")
    public String deptAdd(Dept dept, Model model) {
        model.addAttribute(dept);
        return PREFIX + "dept_add.html";
    }

    /**
     * 跳转到修改部门
     */

    @RequestMapping("/dept_update/{deptId}")
    public String deptUpdate(@PathVariable Integer deptId, Model model) {
        Dept dept = deptService.selectById(deptId);
        model.addAttribute(dept);
        model.addAttribute("parentName", ConstantFactory.me().getDeptName(dept.getParentId()));
        LogObjectHolder.me().set(dept);
        return PREFIX + "dept_edit.html";
    }

    /**
     * 获取部门的tree列表
     */
    @RequestMapping(value = "/tree")
    @ResponseBody
    public List<ZTreeNode> tree() {
        List<ZTreeNode> tree = this.deptService.listTree();
        tree.add(ZTreeNode.createParent());
        return tree;
    }

    /**
     * 新增部门
     */
    @BussinessLog(value = "添加部门", key = "simpleName", dict = DeptDict.class)
    @RequestMapping(value = "/add")

    @ResponseBody
    public Object add(Dept dept) {
        if (ToolUtil.isOneEmpty(dept, dept.getSimpleName())) {
            throw new SystemException(BizExceptionEnum.REQUEST_NULL.getMessage());
        }

        deptService.insert(dept);
        /**
         * 添加seq和level
         */
        ConstantFactory.me().addSeqAndLevelByIdAndParentId(SystemTreeTableEnum.DEPT.getTableName(),dept.getId(),dept.getParentId());
        return SUCCESS_TIP;
    }

    /**
     * 获取所有部门列表
     */
    @RequestMapping(value = "/list")

    @ResponseBody
    public Object list(String condition) {
        List<Dept> list = this.deptService.selectList(new EntityWrapper<Dept>().like(StrUtil.isNotBlank(condition),"simple_name",  condition ).or().like(StrUtil.isNotBlank(condition),"full_name",  condition ));
        return list;
    }

    /**
     * 部门详情
     */
    @RequestMapping(value = "/detail/{deptId}")

    @ResponseBody
    public Object detail(@PathVariable("deptId") Integer deptId) {
        return deptService.selectById(deptId);
    }

    /**
     * 修改部门
     */
    @BussinessLog(value = "修改部门", key = "simpleName", dict = DeptDict.class)
    @RequestMapping(value = "/update")

    @ResponseBody
    public Object update(Dept dept) {
        if (ToolUtil.isEmpty(dept) || dept.getId() == null) {
            throw new SystemException(BizExceptionEnum.REQUEST_NULL.getMessage());
        }
        deptService.updateById(dept);
        /**
         * 修改seq和level
         */
        ConstantFactory.me().updateSeqAndLevelById(SystemTreeTableEnum.DEPT.getTableName(),dept.getId());
        return SUCCESS_TIP;
    }

    /**
     * 删除部门
     */
    @BussinessLog(value = "删除部门", key = "deptId", dict = DeptDict.class)
    @RequestMapping(value = "/delete")

    @ResponseBody
    public Object delete(@RequestParam Integer deptId) {

        //缓存被删除的部门名称
        LogObjectHolder.me().set(ConstantFactory.me().getDeptName(deptId));

        //判断部门下有没有人
        List<User> list = userService.selectDeptUser(deptId);

        if(list.size()>0){
            return new ErrorTip("请先清空部门下面的人员在删除！");
        }

        EntityWrapper entityWrapper = new EntityWrapper<Position>();
        entityWrapper.eq("dept_id",deptId);
        List<Position> positions = positionService.selectList(entityWrapper);
        if(positions.size()>0){
            return new ErrorTip("请先清空部门下面的职位在删除！");
        }

        deptService.deleteDept(deptId);
        return SUCCESS_TIP;
    }

}
