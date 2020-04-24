package com.xinshang.modular.system.controller;

import cn.hutool.core.util.StrUtil;
import com.xinshang.config.properties.SystemProperties;
import com.xinshang.core.base.controller.BaseController;
import com.xinshang.core.log.LogManager;
import com.xinshang.core.log.factory.LogTaskFactory;
import com.xinshang.core.node.MenuNode;
import com.xinshang.core.shiro.ShiroKit;
import com.xinshang.core.shiro.ShiroUser;
import com.xinshang.core.support.HttpKit;
import com.xinshang.core.util.ApiMenuFilter;
import com.xinshang.modular.system.model.Role;
import com.xinshang.modular.system.model.User;
import com.xinshang.modular.system.service.IMenuService;
import com.xinshang.modular.system.service.IUserService;
import lombok.AllArgsConstructor;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


/**
 * 登录控制器
 *
 * @author fengshuonan
 * @date 2017年1月10日 下午8:25:24
 */
@Controller
@AllArgsConstructor
public class LoginController extends BaseController {

    private final IMenuService menuService;
    private final IUserService userService;
    private final SystemProperties systemProperties;

    /**
     * 跳转到主页
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {
        //获取菜单列表
        List<Role> roleList = ShiroKit.getUser().getRoles();
        if (roleList == null || roleList.size() == 0) {
            ShiroKit.getSubject().logout();
            model.addAttribute("tips", "该用户没有角色，无法登陆");
            return "/login.html";
        }
        List<Integer> roleIds = new ArrayList<>();
        roleList.forEach(i-> {
            roleIds.add(i.getId());
        });

        List<MenuNode> menus = menuService.getMenusByRoleIds(roleIds);
        List<MenuNode> titles = MenuNode.buildTitle(menus);
        titles = ApiMenuFilter.build(titles);

        model.addAttribute("titles", titles);

        //获取用户头像
        Integer id = ShiroKit.getUser().getId();
        User user = userService.selectById(id);
        String avatar = user.getAvatar();
        model.addAttribute("avatar", avatar);

        return "/index.html";
    }

    /**
     * 跳转到登录页面
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        if (ShiroKit.isAuthenticated() || ShiroKit.getUser() != null) {
            return REDIRECT + "/";
        } else {
            return "/login.html";
        }
    }

    /**
     * 跳转到登录页面
     */
    @RequestMapping(value = "/enroll", method = RequestMethod.GET)
    public String enroll() {
        return "/enroll.html";
    }


    /**
     * 点击登录执行的动作
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String loginVali() {

        //过期时间2020-2-20 之后无法登陆
        LocalDate beginTime = LocalDate.now();
        String limitDate = systemProperties.getLimitDate();
        if(StrUtil.isNotBlank(limitDate)){
            LocalDate endTime =  LocalDate.parse(limitDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            System.out.println(beginTime.isBefore(endTime));
            Assert.isTrue(beginTime.isBefore(endTime),"该系统使用期限已到期！请联系开发人员！！！");
        }

        String username = super.getPara("username").trim();
        String password = super.getPara("password").trim();
        String remember = super.getPara("remember");

        Subject currentUser = ShiroKit.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password.toCharArray());

        if (com.xinshang.constant.Constants.STATUS_ON.equals(remember)) {
            token.setRememberMe(true);
        } else {
            token.setRememberMe(false);
        }

        currentUser.login(token);

        ShiroUser shiroUser = ShiroKit.getUser();
        super.getSession().setAttribute("shiroUser", shiroUser);
        super.getSession().setAttribute("username", shiroUser.getAccount());

        LogManager.me().executeLog(LogTaskFactory.loginLog(shiroUser.getId(), HttpKit.getIp()));

        ShiroKit.getSession().setAttribute("sessionFlag", true);

        return REDIRECT + "/";
    }

    /**
     * 退出登录
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logOut(RedirectAttributes attr) {
        LogManager.me().executeLog(LogTaskFactory.exitLog(ShiroKit.getUser().getId(), HttpKit.getIp()));
        ShiroKit.getSubject().logout();
        deleteAllCookie();
        return REDIRECT + "/login";
    }
}
