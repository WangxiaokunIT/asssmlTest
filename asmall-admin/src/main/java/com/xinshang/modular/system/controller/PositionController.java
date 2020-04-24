package com.xinshang.modular.system.controller;

import com.xinshang.core.base.controller.BaseController;
import com.xinshang.core.common.constant.factory.ConstantFactory;
import com.xinshang.core.common.constant.state.SystemTreeTableEnum;
import com.xinshang.core.node.ZTreeNode;
import com.xinshang.core.util.ToolUtil;
import com.xinshang.modular.system.model.Dept;
import com.xinshang.modular.system.model.User;
import com.xinshang.modular.system.service.IDeptService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import com.xinshang.core.support.BeanKit;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.xinshang.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.xinshang.modular.system.model.Position;
import com.xinshang.modular.system.service.IPositionService;

/**
 * 职位管理控制器
 *
 * @author fengshuonan
 * @date 2018-11-06 11:01:21
 */
@Controller
@RequestMapping("/position")
public class PositionController extends BaseController {

    private String PREFIX = "/system/position/";

    @Autowired
    private IPositionService positionService;

    @Autowired
    private IDeptService deptService;

    /**
     * 跳转到职位管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "position.html";
    }

    /**
     * 跳转到添加职位管理
     */
    @RequestMapping("/position_add")
    public String positionAdd(Position position,Model model) {
        if(position.getDeptId()==null&&position.getParentId()!=null){
            Position parentPosition = positionService.selectById(position.getParentId());
            position.setDeptId(parentPosition.getDeptId());
        }
        if(ToolUtil.isEmpty(position.getParentId())){
            position.setParentId(0);
        }
        model.addAttribute(position);
        return PREFIX + "position_add.html";
    }

    /**
     * 跳转到修改职位管理
     */
    @RequestMapping("/position_update/{positionId}")
    public String positionUpdate(@PathVariable Integer positionId, Model model) {
        Position position = positionService.selectById(positionId);
        model.addAttribute("item",position);
        LogObjectHolder.me().set(position);
        return PREFIX + "position_edit.html";
    }


    /**
     * 跳转到管理职位成员页面
     */
    @RequestMapping("/position_user_edit/{positionId}")
    public String position_user_edit(@PathVariable Integer positionId, Model model) {

        Position position = positionService.selectById(positionId);
        List<User> positionUsers = positionService.getUserByPositonId(positionId);
        List<User> deptUsers = positionService.getUserByDeptIdExceptByPositionId(positionId);
        Dept dept = deptService.selectById(position.getDeptId());

        model.addAttribute("dept",dept);
        model.addAttribute("position",position);
        model.addAttribute("userList",deptUsers);
        model.addAttribute("addedUserList",positionUsers);
        LogObjectHolder.me().set(position);
        return PREFIX + "position_user_edit.html";
    }


    /**
     * 获取职位管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(Position position) {
        Map<String, Object> beanMap = BeanKit.beanToMap(position,true);
        EntityWrapper<Position> wrapper = new EntityWrapper<>();
        wrapper.allEq(beanMap);
        return positionService.selectList(wrapper);
    }

    /**
     * 新增职位管理
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Position position) {
        positionService.insert (position);
        /**
         * 添加seq和level
         */
        ConstantFactory.me().addSeqAndLevelByIdAndParentId(SystemTreeTableEnum.POSITION.getTableName(),position.getId(),position.getParentId());
        return SUCCESS_TIP;
    }

    /**
     * 删除职位管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer positionId) {
        positionService.deleteById(positionId);
        return SUCCESS_TIP;
    }

    /**
     * 修改职位管理
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Position position) {
        positionService.updateById (position);
        /**
         * 修改seq和level
         */
        ConstantFactory.me().updateSeqAndLevelById(SystemTreeTableEnum.POSITION.getTableName(),position.getId());
        return SUCCESS_TIP;
    }

    /**
     * 职位管理详情
     */
    @RequestMapping(value = "/detail/{positionId}")
    @ResponseBody
    public Object detail(@PathVariable("positionId") Integer positionId) {
        return positionService.selectById(positionId);
    }

    /**
     * 获取职位的tree列表
     */
    @RequestMapping(value = "/tree/{deptId}")
    @ResponseBody
    public List<ZTreeNode> tree(@PathVariable("deptId") Integer deptId) {
        List<ZTreeNode> positionNodes = this.positionService.listTree(deptId);
        positionNodes.add(ZTreeNode.createParent());
        return positionNodes;
    }


}
