package com.xinshang.modular.system.warpper;

import com.xinshang.core.base.warpper.BaseControllerWarpper;
import com.xinshang.core.common.constant.factory.ConstantFactory;
import com.xinshang.core.util.Contrast;
import com.xinshang.core.util.ToolUtil;
import com.xinshang.modular.system.model.LoginLog;

import java.util.Map;

/**
 * 日志列表的包装类
 *
 * @author fengshuonan
 * @date 2017年4月5日22:56:24
 */
public class LogWarpper extends BaseControllerWarpper {

    public LogWarpper(Object list) {
        super(list);
    }

    @Override
    public void warpTheMap(Map<String, Object> map) {
        String message = (String) map.get("message");

        Integer userId = Integer.valueOf(map.get("userid").toString());
        map.put("userName", ConstantFactory.me().getUserNameById(userId));

        //如果信息过长,则只截取前100位字符串
        if (ToolUtil.isNotEmpty(message) && message.length() >= 100) {
            String subMessage = message.substring(0, 100) + "...";
            map.put("message", subMessage);
        }

        //如果信息中包含分割符号;;;   则分割字符串返给前台
        if (ToolUtil.isNotEmpty(message) && message.indexOf(Contrast.separator) != -1) {
            String[] msgs = message.split(Contrast.separator);
            map.put("regularMessage",msgs);
        }else{
            map.put("regularMessage",message);
        }
    }

}
