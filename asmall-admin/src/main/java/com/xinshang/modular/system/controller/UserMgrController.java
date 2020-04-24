package com.xinshang.modular.system.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xinshang.config.properties.OssProperties;
import com.xinshang.constant.Constants;
import com.xinshang.core.base.controller.BaseController;
import com.xinshang.core.base.tips.Tip;
import com.xinshang.core.common.annotion.BussinessLog;
import com.xinshang.core.common.annotion.Permission;
import com.xinshang.core.common.constant.Const;
import com.xinshang.core.common.constant.dictmap.UserDict;
import com.xinshang.core.common.constant.factory.ConstantFactory;
import com.xinshang.core.common.constant.state.ManagerState;
import com.xinshang.core.common.constant.state.UserTreeType;
import com.xinshang.core.common.exception.BizExceptionEnum;
import com.xinshang.core.datascope.DataScope;
import com.xinshang.core.db.Db;
import com.xinshang.core.exception.SystemException;
import com.xinshang.core.log.LogObjectHolder;
import com.xinshang.core.node.ZTreeNode;
import com.xinshang.core.shiro.ShiroKit;
import com.xinshang.core.shiro.ShiroUser;
import com.xinshang.core.util.*;
import com.xinshang.modular.im.model.ImUser;
import com.xinshang.modular.im.service.IImUserService;
import com.xinshang.modular.system.dao.UserMapper;
import com.xinshang.modular.system.factory.UserFactory;
import com.xinshang.modular.system.model.Dept;
import com.xinshang.modular.system.model.RoleUser;
import com.xinshang.modular.system.model.User;
import com.xinshang.modular.system.service.*;
import com.xinshang.modular.system.transfer.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.naming.NoPermissionException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * 系统管理员控制器
 *
 * @author fengshuonan
 * @date 2017年1月11日 下午1:08:17
 */
@Controller
@RequestMapping("/mgr")
public class UserMgrController extends BaseController {

    private static String PREFIX = "/system/user/";

    @Autowired
    private IUserService userService;

    @Autowired
    private IDeptService deptService;

    @Autowired
    private IPositionService positionService;

    @Autowired
    private IRoleUserService roleUserService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private IImUserService iImUserService;

    @Autowired
    private OssProperties ossProperties;


    /**
     * 跳转到查看管理员列表的页面
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "user.html";
    }

    /**
     * 跳转到查看管理员新增的页面
     */
    @RequestMapping("/user_add")
    public String addUser(Integer deptId,Model model) {
        model.addAttribute("deptId",deptId);
        return PREFIX + "user_add.html";
    }

    /**
     * 跳转到角色分配页面
     * //@RequiresPermissions("/mgr/role_assign")  //利用shiro自带的权限检查
     */

    @RequestMapping("/role_assign/{userId}")
    public String roleAssign(@PathVariable Integer userId, Model model) {
        if (ToolUtil.isEmpty(userId)) {
            throw new SystemException(BizExceptionEnum.REQUEST_NULL.getMessage());
        }
        User user = Db.create(UserMapper.class).selectOneByCon("id", userId);
        model.addAttribute("userId", userId);
        model.addAttribute("userAccount", user.getAccount());
        return PREFIX + "user_roleassign.html";
    }

    /**
     * 跳转到编辑管理员页面
     */

    @RequestMapping("/user_edit/{userId}")
    public String userEdit(@PathVariable Integer userId, Model model) {
        if (ToolUtil.isEmpty(userId)) {
            throw new SystemException(BizExceptionEnum.REQUEST_NULL.getMessage());
        }
        assertAuth(userId);
        User user = this.userService.selectById(userId);
        model.addAttribute(user);
        model.addAttribute("roleName", ConstantFactory.me().getUserRoleName(user.getId()));
        model.addAttribute("deptName", ConstantFactory.me().getDeptName(user.getDeptId()));
        LogObjectHolder.me().set(user);
        return PREFIX + "user_edit.html";
    }

    /**
     * 跳转到查看用户详情页面
     */
    @RequestMapping("/user_info")
    public String userInfo(Model model) {
        User user = this.userService.selectById(ShiroKit.getUser().getId());
        model.addAttribute(user);
        model.addAttribute("roleName", ConstantFactory.me().getUserRoleName(user.getId()));
        model.addAttribute("deptName", ConstantFactory.me().getDeptName(user.getDeptId()));
        model.addAttribute("positionName", ConstantFactory.me().getPositionNameByUserId(user.getId()));
        LogObjectHolder.me().set(user);
        return PREFIX + "user_view.html";
    }

    /**
     * 跳转到修改密码界面
     */
    @RequestMapping("/user_chpwd")
    public String chPwd() {
        return PREFIX + "user_chpwd.html";
    }

    /**
     * 修改当前用户的密码
     */
    @RequestMapping("/changePwd")
    @ResponseBody
    public Object changePwd(@RequestParam String oldPwd, @RequestParam String newPwd, @RequestParam String rePwd) {
        if (!newPwd.equals(rePwd)) {
            throw new SystemException(BizExceptionEnum.TWO_PWD_NOT_MATCH.getMessage());
        }
        Integer userId = ShiroKit.getUser().getId();
        User user = userService.selectById(userId);
        String oldMd5 = ShiroKit.md5(oldPwd, user.getSalt());
        if (user.getPassword().equals(oldMd5)) {
            String newMd5 = ShiroKit.md5(newPwd, user.getSalt());
            user.setPassword(newMd5);
            userService.updateById(user);
            return SUCCESS_TIP;
        } else {
            throw new SystemException(BizExceptionEnum.OLD_PWD_NOT_RIGHT.getMessage());
        }
    }

    /**
     * 查询管理员列表
     */
    @RequestMapping("/list")

    @ResponseBody
    public Object list(@RequestParam(required = false) String name, @RequestParam(required = false) String beginTime, @RequestParam(required = false) String endTime, @RequestParam(required = false) Integer deptId ,@RequestParam(required = false) Integer positionId) {
        if (ShiroKit.isAdmin()) {
            List<User> users = userService.selectUsers(null, name, beginTime, endTime, deptId,positionId);
            return users;
        } else {
            DataScope dataScope = new DataScope(ShiroKit.getDeptDataScope());
            List<User> users = userService.selectUsers(dataScope, name, beginTime, endTime, deptId,positionId);
            return users;
        }
    }

    /**
     * 获取用户列表左边的部门及职位的tree
     */
    @RequestMapping(value = "/syncDeptAndPositionAndUserTree")
    @ResponseBody
    public List<ZTreeNode> syncDeptAndPositionAndUserTree(Integer parentId,Integer type) {
        List<ZTreeNode> tree=null;
        if(parentId==null&&type==null){
            tree = new ArrayList<>();
            tree.add(ZTreeNode.createParent());
        }else{
            if(type==null||type.intValue()== UserTreeType.DEPT.getVal().intValue()){
                //查询下级部门及职位
                tree = deptService.syncDeptAndPositionTree(parentId);
            }else if (type.intValue()==UserTreeType.POSITION.getVal().intValue()){
                //查询下级职位
                tree = positionService.syncPositionTree(parentId);
            }
        }
        return tree;
    }

    /**
     * 添加管理员
     */
    @RequestMapping("/add")
    @BussinessLog(value = "添加管理员", key = "account", dict = UserDict.class)

    @ResponseBody
    public Tip add(@Valid UserDto user, BindingResult result) {
        if (result.hasErrors()) {
            throw new SystemException(BizExceptionEnum.REQUEST_NULL.getMessage());
        }

        // 判断账号是否重复
        User theUser = userService.getByAccount(user.getAccount());
        if (theUser != null) {
            throw new SystemException(BizExceptionEnum.USER_ALREADY_REG.getMessage());
        }

        // 完善账号信息
        user.setSalt(ShiroKit.getRandomSalt(5));
        user.setPassword(ShiroKit.md5(user.getPassword(), user.getSalt()));
        user.setState(ManagerState.OK.getCode());
        user.setGmtCreate(new Date());
        user.setCreator(ShiroKit.getUser().id);
        user.setPinYinIndex(PingYinUtil.getPYIndexStr(user.getName(),false));
        User u = UserFactory.createUser(user);
        this.userService.insert(u);
        redisUtil.del(Constants.asmall_SYSTEM_USER_KEY);
        Dept dept = deptService.selectById(user.getDeptId());
        ImUser iu = new ImUser();
        iu.setId(u.getId());
        iu.setUsername(dept==null?user.getName():dept.getSimpleName()+"-"+u.getName());
        iu.setNickName(u.getName());
        iu.setAvatar("static/img/qq.jpg");
        iImUserService.addUser(iu);
        return SUCCESS_TIP;
    }

    /**
     * 修改管理员
     *
     * @throws NoPermissionException
     */
    @RequestMapping("/edit")
    @BussinessLog(value = "修改管理员", key = "account", dict = UserDict.class)
    @ResponseBody
    public Tip edit(@Valid UserDto user, BindingResult result) {
        if (result.hasErrors()) {
            throw new SystemException(BizExceptionEnum.REQUEST_NULL.getMessage());
        }

        User oldUser = userService.selectById(user.getId());
        if(user.getAvatar()!=null){
            ImUser iu = iImUserService.selectById(user.getId());
            if(iu!=null){
                Dept dept = deptService.selectById(user.getDeptId());
                iu.setAvatar("/mgr/avatar/"+user.getId());
                iu.setUsername(dept==null?user.getName():dept.getSimpleName()+"-"+user.getName());
                iImUserService.updateById(iu);
            }
        }

        if (ShiroKit.hasRole(Const.ADMIN_NAME)) {
            this.userService.updateById(UserFactory.editUser(user, oldUser));
            return SUCCESS_TIP;
        } else {
            assertAuth(user.getId());
            ShiroUser shiroUser = ShiroKit.getUser();
            if (shiroUser.getId().equals(user.getId())) {
                this.userService.updateById(UserFactory.editUser(user, oldUser));
                redisUtil.del(Constants.asmall_SYSTEM_USER_KEY);
                return SUCCESS_TIP;
            } else {
                throw new SystemException(BizExceptionEnum.NO_PERMITION.getMessage());
            }
        }
    }

    /**
     * 删除管理员（逻辑删除）
     */
    @RequestMapping("/delete")
    @BussinessLog(value = "删除管理员", key = "userId", dict = UserDict.class)

    @ResponseBody
    public Tip delete(@RequestParam Integer userId) {
        if (ToolUtil.isEmpty(userId)) {
            throw new SystemException(BizExceptionEnum.REQUEST_NULL.getMessage());
        }
        //不能删除超级管理员
        if (userId.equals(Const.ADMIN_ID)) {
            throw new SystemException(BizExceptionEnum.CANT_DELETE_ADMIN.getMessage());
        }
        assertAuth(userId);
        this.userService.setState(userId, ManagerState.DELETED.getCode());
        redisUtil.del(Constants.asmall_SYSTEM_USER_KEY);
        return SUCCESS_TIP;
    }

    /**
     * 查看管理员详情
     */
    @RequestMapping("/view/{userId}")
    @ResponseBody
    public User view(@PathVariable Integer userId) {
        if (ToolUtil.isEmpty(userId)) {
            throw new SystemException(BizExceptionEnum.REQUEST_NULL.getMessage());
        }
        assertAuth(userId);
        return this.userService.selectById(userId);
    }

    /**
     * 重置管理员的密码
     */
    @RequestMapping("/reset")
    @BussinessLog(value = "重置管理员密码", key = "userId", dict = UserDict.class)

    @ResponseBody
    public Tip reset(@RequestParam Integer userId) {
        if (ToolUtil.isEmpty(userId)) {
            throw new SystemException(BizExceptionEnum.REQUEST_NULL.getMessage());
        }
        assertAuth(userId);
        User user = this.userService.selectById(userId);
        user.setSalt(ShiroKit.getRandomSalt(5));
        user.setPassword(ShiroKit.md5(Const.DEFAULT_PWD, user.getSalt()));
        this.userService.updateById(user);
        return SUCCESS_TIP;
    }

    /**
     * 冻结用户
     */
    @RequestMapping("/freeze")
    @BussinessLog(value = "冻结用户", key = "userId", dict = UserDict.class)

    @ResponseBody
    public Tip freeze(@RequestParam Integer userId) {
        if (ToolUtil.isEmpty(userId)) {
            throw new SystemException(BizExceptionEnum.REQUEST_NULL.getMessage());
        }
        //不能冻结超级管理员
        if (userId.equals(Const.ADMIN_ID)) {
            throw new SystemException(BizExceptionEnum.CANT_FREEZE_ADMIN.getMessage());
        }
        assertAuth(userId);
        this.userService.setState(userId, ManagerState.FREEZED.getCode());
        return SUCCESS_TIP;
    }

    /**
     * 解除冻结用户
     */
    @RequestMapping("/unfreeze")
    @BussinessLog(value = "解除冻结用户", key = "userId", dict = UserDict.class)

    @ResponseBody
    public Tip unfreeze(@RequestParam Integer userId) {
        if (ToolUtil.isEmpty(userId)) {
            throw new SystemException(BizExceptionEnum.REQUEST_NULL.getMessage());
        }
        assertAuth(userId);
        this.userService.setState(userId, ManagerState.OK.getCode());
        return SUCCESS_TIP;
    }

    /**
     * 分配角色
     */
    @RequestMapping("/setRole")
    @BussinessLog(value = "分配角色", key = "userId,roleIds", dict = UserDict.class)

    @ResponseBody
    public Tip setRole(@RequestParam("userId") Integer userId, @RequestParam("roleIds") String roleIds) {
        if (ToolUtil.isOneEmpty(userId, roleIds)) {
            throw new SystemException(BizExceptionEnum.REQUEST_NULL.getMessage());
        }
        //不能修改超级管理员
        if (userId.equals(Const.ADMIN_ID)) {
            throw new SystemException(BizExceptionEnum.CANT_CHANGE_ADMIN.getMessage());
        }
        assertAuth(userId);
        //1.删除原来旧的角色用户
        roleUserService.delete(new EntityWrapper<RoleUser>().eq("user_id",userId));
        //2.添加新的角色用户
        String[] roleIdsArr = roleIds.split(",");
        List<RoleUser> list = new ArrayList();
        for (int i = 0; i < roleIdsArr.length; i++) {
            RoleUser ru = new RoleUser();
            ru.setRoleId(Integer.parseInt(roleIdsArr[i]));
            ru.setUserId(userId);
            list.add(ru);
        }
        roleUserService.insertBatch(list);
        return SUCCESS_TIP;
    }

    /**
     * 上传图片
     * @param picture
     */
    @RequestMapping(method = RequestMethod.POST, path = "/upload")
    @ResponseBody
    public String upload(@RequestPart("file") MultipartFile picture){
        String pictureName = UUID.randomUUID().toString() + "." + ToolUtil.getFileSuffix(picture.getOriginalFilename());
        OssUtil ossUtil = new OssUtil(ossProperties);
        final String saveUrl;
        try {
            saveUrl = ossUtil.uploadFile(picture, pictureName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SystemException(BizExceptionEnum.UPLOAD_ERROR.getMessage());
        }
        return saveUrl;
    }

    /**
     * 获取头像
     * @param userId
     * @param response
     */
    @GetMapping("avatar/{userId}")
    public void avatar(@PathVariable("userId") Integer userId,HttpServletResponse response) throws IOException{
        User user = userService.selectById(userId);
        byte[] fileStream = FileUtil.getFileStream(user.getAvatar());
        OutputStream toClient=response.getOutputStream();
        toClient.write(fileStream);
        toClient.flush();
        toClient.close();
    }
        /**
     * 判断当前登录的用户是否有操作这个用户的权限
     */
    private void assertAuth(Integer userId) {
        if (ShiroKit.isAdmin()) {
            return;
        }
        List<Integer> deptDataScope = ShiroKit.getDeptDataScope();
        User user = this.userService.selectById(userId);
        Integer deptId = user.getDeptId();
        if (deptDataScope.contains(deptId)) {
            return;
        } else {
            throw new SystemException(BizExceptionEnum.NO_PERMITION.getMessage());
        }

    }


}
