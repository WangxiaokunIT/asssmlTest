package com.xinshang.modular.system.controller;

import com.xinshang.core.base.controller.BaseController;
import com.xinshang.core.common.constant.factory.ConstantFactory;
import com.xinshang.modular.system.model.GeneralTable;
import com.xinshang.modular.system.transfer.UserPosition;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import java.util.List;



/**
 * system控制器
 *
 * @author zhangjiajia
 * @date 2018年11月29日 17:01:54
 */
@RestController
@RequestMapping("/system")
public class SystemController extends BaseController {

    /**
     * 根据职位id查询该职位的人员及子职位下面的人员
     * @param positionId
     * @return
     */
    @RequestMapping(value = "/getPositionUserByPositionId/{positionId}",method = {RequestMethod.GET})
    public List<UserPosition> getPositionUserByPositionId(@PathVariable("positionId") Integer positionId) {
        return ConstantFactory.me().getPositionUserByPositionId(positionId);
    }


  /**
     * 根据用户id查询该用户所在职位及子职位下面的人员
     * @param userId   用户id
     * @return
     */
    @RequestMapping(value = "/getPositionUserByUserId/{userId}",method = {RequestMethod.GET})
    public List<UserPosition> getPositionUserByUserId(@PathVariable("userId")Integer userId) {
        return ConstantFactory.me().getPositionUserByUserId(userId);
    }

    /**
     * 判断该用户是否属于该职位
     * @param userId   用户id
     * @param positionCode  职位编码
     * @return
     */

    @RequestMapping(value = "/judgeUserHasPositionByUserIdAndPositionCode",method = {RequestMethod.POST})
    public Integer judgeUserHasPositionByUserIdAndPositionCode(Integer userId,String positionCode) {
        return ConstantFactory.me().judgeUserHasPositionByUserIdAndPositionCode(userId,positionCode);
    }

    /**
     * 查询与该职位相同的人员
     * @param positionCode  职位编码
     * @return
     */
    @RequestMapping(value = "/getSamePositionUserByPositionCode",method = {RequestMethod.POST})
    public List<UserPosition> getSamePositionUserByPositionCode(String positionCode) {
        return ConstantFactory.me().getSamePositionUserByPositionCode(positionCode);
    }

    /**
     * 根据多个code获取对应职务的人
     * @param positionCodes
     * @return
     */
    @RequestMapping(value = "/getSamePositionUserByPositionCodes")
    public Object getSamePositionUserByPositionCodes(String positionCodes) {
        StringBuffer sb = new StringBuffer();
        if(!StringUtils.isEmpty(positionCodes)) {
            String[] str = positionCodes.split(",");

            for (int k = 0; k < str.length; k++) {
                sb.append("'").append(str[k]).append("'");
                if(k != str.length - 1) {
                    sb.append(",");
                }
            }
        }
        return ConstantFactory.me().getSamePositionUserByPositionCodes(sb.toString());
    }

    /**
     * 获取通用字段信息
     * @param generalTable
     * @return
     */
    @RequestMapping(value = "/getCommonFieldsList")
    public Object getCommonFieldsList(GeneralTable generalTable) {

        List<GeneralTable> generalTableList = ConstantFactory.me().findInTable(generalTable);
        return generalTableList;
    }

}
