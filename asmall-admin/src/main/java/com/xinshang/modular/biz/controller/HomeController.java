package com.xinshang.modular.biz.controller;

import com.xinshang.modular.biz.dto.ParameterDTO;
import com.xinshang.modular.biz.service.IHomeService;
import com.xinshang.modular.biz.vo.HomeVO;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页
 * @author lyk
 */
@Controller
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private IHomeService homeService;

    /**
     * 首页统计
     * @return
     */
    @RequestMapping(value = "/welcome")
    @ResponseBody
    public Object welcome() {
        Map<String, Object> map = new HashMap<>();
        ParameterDTO parameterDto = new ParameterDTO();
        // 获取累积用户数量
        List<HomeVO> userList = homeService.showUserNum(parameterDto);

        for (HomeVO user : userList){
            // 非会员
            if(Integer.valueOf(user.getNum2().toString()) == 0) {
                map.put("z_userNum", user.getNum1());
                // 会员
            } else {
                map.put("z_vipNum", user.getNum1());
            }
        }


        // 累积加盟额
        HomeVO joinIn = homeService.showJoinIn(parameterDto);
        map.put("z_joinIn", joinIn == null ? 0 : joinIn.getNum1());

        // 累积待还权益额
        HomeVO equity = homeService.showEquity(parameterDto);
        map.put("z_equity", equity == null ? 0 : equity.getNum1());

        // -------------------获取当月时间-----------------------
        LocalDateTime localDateTime = LocalDateTime.now();
        String time = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM")) + "-01";
        parameterDto.setStartTime(time);

        // 获取当前月份用户数量
        List<HomeVO> yUserList = homeService.showUserNum(parameterDto);
        for (HomeVO user : yUserList){
            // 非会员
            if(Integer.valueOf(user.getNum2().toString()) == 0) {
                map.put("y_userNum", user.getNum1());
                // 会员
            } else {
                map.put("y_vipNum", user.getNum1());
            }
        }

        // 累积加盟额
        HomeVO yJoinIn = homeService.showJoinIn(parameterDto);
        map.put("y_joinIn", yJoinIn == null ? 0 : yJoinIn.getNum1());

        // 累积待还权益额
        HomeVO yEquity = homeService.showEquity(parameterDto);
        map.put("y_equity", yEquity == null ? 0 : yEquity.getNum1());

        // 获取当前时间
        LocalDate today = LocalDate.now();
        // 获取本月第一天
        LocalDate firstDayOfThisMonth = today.with(TemporalAdjusters.firstDayOfMonth());
        // 得到一年前的当月
        LocalDate oldDate = firstDayOfThisMonth.minusYears(1);
        // 设置初始统计时间
        parameterDto.setStartTime(oldDate.toString());
        // 查询首页柱状图数据
        List<HomeVO> sList = homeService.showStatisticsSystem(parameterDto);

        map.put("statistics", sList);



        return map;
    }
}
