package com.xinshang.modular.biz.controller;

import cn.hutool.core.util.StrUtil;
import com.allinpay.yunst.sdk.bean.YunRequest;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xinshang.config.properties.AllinPayProperties;
import com.xinshang.core.base.controller.BaseController;
import com.xinshang.core.common.constant.factory.PageFactory;
import com.xinshang.core.log.LogObjectHolder;
import com.xinshang.core.support.BeanKit;
import com.xinshang.core.util.AllinPayUtil;
import com.xinshang.core.util.DateUtil;
import com.xinshang.modular.biz.dto.AllinPayQueryInExpRespDetailDTO;
import com.xinshang.modular.biz.dto.AllinPayResponseDTO;
import com.xinshang.modular.biz.model.ReceiptsPayments;
import com.xinshang.modular.biz.service.IReceiptsPaymentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @title:控制器
 *
 * @author: jyz
 * @since: 2019-11-05 09:51:11
 */
@Controller
@RequestMapping("/receiptsPayments")
public class ReceiptsPaymentsController extends BaseController {

    private String PREFIX = "/biz/receiptsPayments/";

    @Autowired
    private IReceiptsPaymentsService receiptsPaymentsService;
    
    @Autowired
    private AllinPayProperties allinPayProperties;

    /**
     * 跳转到首页
     */
    @RequestMapping("/index/{type}")
    public String index(@PathVariable("type") Integer type,Model model) {
        YunRequest request = new YunRequest("MerchantService", "queryMerchantBalance");
        request.put("accountSetNo", "100001");

        Optional<AllinPayResponseDTO<AllinPayQueryInExpRespDetailDTO>> response = AllinPayUtil.request(request, AllinPayQueryInExpRespDetailDTO.class);

        Map map = new HashMap();
        //非空判断
        if(response.get().getSignedValue().getAllAmount() != null) {
            model.addAttribute("totalMoney", response.get().getSignedValue().getAllAmount() / 100.0);
        }else{
            model.addAttribute("totalMoney", 0);
        }

        if(response.get().getSignedValue().getFreezenAmount() != null) {
            model.addAttribute("freezenAmount", response.get().getSignedValue().getFreezenAmount() / 100.0);
        }else{
            model.addAttribute("freezenAmount", 0);
        }
        //如果都不为空 计算可结算余额
        if(response.get().getSignedValue().getAllAmount() != null && response.get().getSignedValue().getFreezenAmount() != null) {
            model.addAttribute("cjsAmount", (response.get().getSignedValue().getAllAmount() / 100.0) - (response.get().getSignedValue().getFreezenAmount() / 100.0));
        } else if(response.get().getSignedValue().getAllAmount() != null && response.get().getSignedValue().getFreezenAmount() == null){
            model.addAttribute("cjsAmount", response.get().getSignedValue().getAllAmount() / 100.0);
        } else {
            model.addAttribute("cjsAmount", 0);
        }
        //初始化日期获取
        //当前日期
        String curDate = DateUtil.getDay(new Date());
        String beforeDate = getPastDate(7);
        model.addAttribute("curDate",curDate);
        model.addAttribute("beforeDate",beforeDate);
        model.addAttribute("queryType",type);
        return PREFIX + "receiptsPayments.html";
    }


    /**
     * 获取余额
     */
    @RequestMapping("/getQueryBalance")
    @ResponseBody
    public Object getQueryBalance(ReceiptsPayments receiptsPayments) {
        if(receiptsPayments.getAccountSetName() ==null){
         return null;
        }
        YunRequest request = new YunRequest("MerchantService", "queryMerchantBalance");
        request.put("accountSetNo", receiptsPayments.getAccountSetName());

        Optional<AllinPayResponseDTO<AllinPayQueryInExpRespDetailDTO>> response = AllinPayUtil.request(request, AllinPayQueryInExpRespDetailDTO.class);

        Map map = new HashMap();
        //非空判断
        if(response.get().getSignedValue().getAllAmount() != null) {
            map.put("totalMoney", response.get().getSignedValue().getAllAmount() / 100.0);
        }else{
            map.put("totalMoney", 0);
        }

        if(response.get().getSignedValue().getFreezenAmount() != null) {
            map.put("freezenAmount", response.get().getSignedValue().getFreezenAmount() / 100.0);
        }else{
            map.put("freezenAmount", 0);
        }
        //如果都不为空 计算可结算余额
        if(response.get().getSignedValue().getAllAmount() != null && response.get().getSignedValue().getFreezenAmount() != null) {
            map.put("cjsAmount", (response.get().getSignedValue().getAllAmount() / 100.0) - (response.get().getSignedValue().getFreezenAmount() / 100.0));
        } else if(response.get().getSignedValue().getAllAmount() != null && response.get().getSignedValue().getFreezenAmount() == null){
            map.put("cjsAmount", response.get().getSignedValue().getAllAmount() / 100.0);
        } else {
            map.put("cjsAmount", 0);
        }
        return map;
    }
    /**
     * 跳转到添加
     */
    @RequestMapping("/receiptsPayments_add")
    public String receiptsPaymentsAdd() {
        return PREFIX + "receiptsPayments_add.html";
    }

    /**
     * 跳转到修改
     */
    @RequestMapping("/receiptsPayments_update/{receiptsPaymentsId}")
    public String receiptsPaymentsUpdate(@PathVariable Integer receiptsPaymentsId, Model model) {
        ReceiptsPayments receiptsPayments = receiptsPaymentsService.selectById(receiptsPaymentsId);
        model.addAttribute("item",receiptsPayments);
        LogObjectHolder.me().set(receiptsPayments);
        return PREFIX + "receiptsPayments_edit.html";
    }

    /**
     * 获取分页列表
     */
    @RequestMapping(value = "/pageList")
    @ResponseBody
    public Object pageList(ReceiptsPayments receiptsPayments) {

        if(receiptsPayments.getDateStart() == null && receiptsPayments.getDateEnd() == null) {
            String curDate = DateUtil.getDay(new Date());
            String beforeDate = getPastDate(7);
            receiptsPayments.setDateStart(beforeDate);
            receiptsPayments.setDateEnd(curDate);
        }
        Page<ReceiptsPayments> page = new PageFactory<ReceiptsPayments>().defaultPage();

        YunRequest request = new YunRequest("OrderService", "queryInExpDetail");
        if(receiptsPayments.getQueryType()==1){
            request.put("bizUserId", "#yunBizUserId_B2C#");
            request.put("accountSetNo", receiptsPayments.getAccountSetName());
        }else{
            request.put("bizUserId", receiptsPayments.getBizUserId());
            request.put("accountSetNo",allinPayProperties.getAccountSetNo());
        }

        request.put("dateStart", receiptsPayments.getDateStart());
        request.put("dateEnd", receiptsPayments.getDateEnd());
        request.put("startPosition", page.getOffset() + 1);
        request.put("queryNum", page.getLimit());

        Optional<AllinPayResponseDTO<AllinPayQueryInExpRespDetailDTO>> response = AllinPayUtil.request(request, AllinPayQueryInExpRespDetailDTO.class);
        if(response.get().getSignedValue() == null) {
            page.setRecords(null);
        } else {
            List<ReceiptsPayments> inExpDetail = response.get().getSignedValue().getInExpDetail();
            for (ReceiptsPayments item : inExpDetail) {
                item.setChgAmount(item.getChgAmount().divide(new BigDecimal("100"),2,BigDecimal.ROUND_DOWN));
                item.setCurAmount(item.getCurAmount().divide(new BigDecimal("100"),2,BigDecimal.ROUND_DOWN));
                item.setOriAmount(item.getOriAmount().divide(new BigDecimal("100"),2,BigDecimal.ROUND_DOWN));
                item.setCurFreezenAmount(item.getCurFreezenAmount().divide(new BigDecimal("100"),2,BigDecimal.ROUND_DOWN));
            }
            page.setRecords(inExpDetail);
            page.setTotal(response.get().getSignedValue().getTotalNum());
        }
        return page;
    }

    /**
     * 获取列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(ReceiptsPayments receiptsPayments) {
        Map<String, Object> beanMap = BeanKit.beanToMap(receiptsPayments,true);
        EntityWrapper<ReceiptsPayments> wrapper = new EntityWrapper<>();
        wrapper.allEq(beanMap);
        return receiptsPaymentsService.selectList(wrapper);
    }

    /**
     * 新增
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(ReceiptsPayments receiptsPayments) {
        receiptsPaymentsService.insert(receiptsPayments);
        return SUCCESS_TIP;
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam String receiptsPaymentsIds) {
        if(StrUtil.isNotBlank(receiptsPaymentsIds)) {
            receiptsPaymentsService.deleteBatchIds(Arrays.asList(receiptsPaymentsIds.split(",")));
        }
        return SUCCESS_TIP;
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(ReceiptsPayments receiptsPayments) {
        receiptsPaymentsService.updateById(receiptsPayments);
        return SUCCESS_TIP;
    }

    /**
     * 详情
     */
    @RequestMapping(value = "/detail/{receiptsPaymentsId}")
    @ResponseBody
    public Object detail(@PathVariable("receiptsPaymentsId") Integer receiptsPaymentsId) {
        return receiptsPaymentsService.selectById(receiptsPaymentsId);
    }


    public static String getPastDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        return result;
    }
}
