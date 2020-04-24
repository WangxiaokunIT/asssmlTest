package com.xinshang.modular.system.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xinshang.constant.Constants;
import com.xinshang.core.base.controller.BaseController;
import com.xinshang.core.common.annotion.BussinessLog;
import com.xinshang.core.common.annotion.Permission;
import com.xinshang.core.common.constant.Const;
import com.xinshang.core.common.constant.dictmap.DictMap;
import com.xinshang.core.common.exception.BizExceptionEnum;
import com.xinshang.core.exception.SystemException;
import com.xinshang.core.log.LogObjectHolder;
import com.xinshang.core.util.RedisUtil;
import com.xinshang.core.util.ToolUtil;
import com.xinshang.modular.system.model.Dict;
import com.xinshang.modular.system.service.IDictService;
import com.xinshang.modular.system.vo.DictVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 字典控制器
 *
 * @author fengshuonan
 * @date 2017年4月26日 12:55:31
 */
@Controller
@RequestMapping("/dict")
public class DictController extends BaseController {

    private String PREFIX = "/system/dict/";

    @Autowired
    private IDictService dictService;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 跳转到字典管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "dict.html";
    }

    /**
     * 跳转到添加字典
     */
    @RequestMapping("/dict_add")
    public String dictAdd() {
        return PREFIX + "dict_add.html";
    }

    /**
     * 跳转到修改字典
     */

    @RequestMapping("/dict_edit/{dictId}")
    public String dictUpdate(@PathVariable Integer dictId, Model model) {
        Dict dict = dictService.selectById(dictId);
        model.addAttribute("dict", dict);
        List<Dict> subDicts = dictService.selectList(new EntityWrapper<Dict>().eq("parent_id", dictId));
        model.addAttribute("subDicts", subDicts);
        LogObjectHolder.me().set(dict);
        return PREFIX + "dict_edit.html";
    }

    /**
     * 新增字典
     *
     * @param dictValues 格式例如   "1:启用;2:禁用;3:冻结"
     */
    @BussinessLog(value = "添加字典记录", key = "dictName,dictValues", dict = DictMap.class)
    @RequestMapping(value = "/add")

    @ResponseBody
    public Object add(String dictCode,String dictRemark,String dictName, String dictValues) {
        if (ToolUtil.isOneEmpty(dictCode,dictName, dictValues)) {
            throw new SystemException(BizExceptionEnum.REQUEST_NULL.getMessage());
        }
        this.dictService.addDict(dictCode,dictName,dictRemark,dictValues);
        redisUtil.del(Constants.asmall_SYSTEM_DICT_KEY);
        return SUCCESS_TIP;
    }

    /**
     * 获取所有字典列表
     */
    @RequestMapping(value = "/list")

    @ResponseBody
    public Object list(String condition) {
        List<Map<String, Object>> list = this.dictService.list(condition);
        return list;
    }

    /**
     * 字典详情
     */
    @RequestMapping(value = "/detail/{dictId}")

    @ResponseBody
    public Object detail(@PathVariable("dictId") Integer dictId) {
        return dictService.selectById(dictId);
    }

    /**
     * 修改字典
     */
    @BussinessLog(value = "修改字典", key = "dictName,dictValues", dict = DictMap.class)
    @RequestMapping(value = "/update")

    @ResponseBody
    public Object update(Integer dictId,String dictCode,String dictName, String dictRemark,String dictValues) {
        if (ToolUtil.isOneEmpty(dictId, dictCode, dictName, dictValues)) {
            throw new SystemException(BizExceptionEnum.REQUEST_NULL.getMessage());
        }
        dictService.editDict(dictId, dictCode,dictName, dictRemark,dictValues);
        redisUtil.del(Constants.asmall_SYSTEM_DICT_KEY);
        return SUCCESS_TIP;
    }

    /**
     * 删除字典记录
     */
    @BussinessLog(value = "删除字典记录", key = "dictId", dict = DictMap.class)
    @RequestMapping(value = "/delete")

    @ResponseBody
    public Object delete(@RequestParam Integer dictId) {
        //缓存被删除的名称
        LogObjectHolder.me().set(dictService.selectById(dictId).getName());
        this.dictService.delteDict(dictId);
        redisUtil.del(Constants.asmall_SYSTEM_DICT_KEY);
        return SUCCESS_TIP;
    }

    /**
     * 根据编号集合获取列表
     */
    @RequestMapping(value = "/selectByCodes")
    @ResponseBody
    public Object selectByNames(String codes) {
        List<DictVo> list = this.dictService.selectByNames(codes);
        return list;
    }

}
