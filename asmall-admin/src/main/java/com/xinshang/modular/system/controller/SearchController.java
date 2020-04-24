package com.xinshang.modular.system.controller;

import com.xinshang.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 搜索信息
 *
 * @author zhangjaijia
 * @date 2018年9月30日 17:50:02
 */
@Controller
@RequestMapping("/search")
public class SearchController extends BaseController {

    /**
     * 跳转到黑板
     */
    @RequestMapping("")
    public String index(Model model,String pw) {
        model.addAttribute("pw", pw);
        return "/secrch_result.html";
    }
}
