package com.xinshang.modular.biz.controller;

import cn.hutool.core.util.StrUtil;
import com.allinpay.yunst.sdk.bean.YunRequest;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xinshang.core.base.controller.BaseController;
import com.xinshang.core.common.constant.factory.PageFactory;
import com.xinshang.core.log.LogObjectHolder;
import com.xinshang.core.support.BeanKit;
import com.xinshang.core.util.AllinPayUtil;
import com.xinshang.modular.biz.dto.AllinPayQueryInExpRespDetailDTO;
import com.xinshang.modular.biz.dto.AllinPayResponseDTO;
import com.xinshang.modular.biz.model.ReceiptsPayments;
import com.xinshang.modular.biz.service.IReceiptsPaymentsService;
import com.xinshang.modular.biz.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * @title:商户集合对账文件下载控制器
 *
 * @author: jyz
 * @since: 2019-11-18 09:51:11
 */
@Controller
@RequestMapping("/reconciliationDownload")
public class ReconciliationDownloadController extends BaseController {

    private String PREFIX = "/biz/reconciliationDownload/";

    @Autowired
    private IReceiptsPaymentsService receiptsPaymentsService;

    /**
     * 跳转到首页
     */
    @RequestMapping("")
    public String index(Model model) {
        return PREFIX + "reconciliationDownload.html";
    }
    /**
     * 获取余额
     */
    @RequestMapping("/downLoad")
    @ResponseBody
    public void downLoad(ReceiptsPayments receiptsPayments, HttpServletResponse response1) {

        YunRequest request = new YunRequest("MerchantService", "getCheckAccountFile");
        String date = receiptsPayments.getDateStart().replaceAll("-", "");

        request.put("date", date);
        request.put("fileType", receiptsPayments.getAccountSetName());

        Optional<AllinPayResponseDTO<AllinPayQueryInExpRespDetailDTO>> response = AllinPayUtil.request(request, AllinPayQueryInExpRespDetailDTO.class);

        if (response.get().getSignedValue().getUrl() != "") {
            FileUtil.getImageStream(date + ".txt", response.get().getSignedValue().getUrl(), response1);
        }

//        try {
//            downLoadFromUrl(response.get().getSignedValue().getUrl(),date+"对账文件","d:/");
//        } catch (IOException e) {
//            e.printStackTrace();
//            return "fail";
//        }

    }


    /**
     * 从输入流中获取字节数组
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static  byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    /**
     * 从网络Url中下载文件
     * @param urlStr
     * @param fileName
     * @param savePath
     * @throws IOException
     */
    public static void  downLoadFromUrl(String urlStr,String fileName,String savePath) throws IOException{
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(3*1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        //得到输入流
        InputStream inputStream = conn.getInputStream();
        //获取自己数组
        byte[] getData = readInputStream(inputStream);

        //文件保存位置
        File saveDir = new File(savePath);
        if(!saveDir.exists()){
            saveDir.mkdir();
        }
        File file = new File(saveDir+File.separator+fileName+".txt");
        FileOutputStream fos = null;
        try{
           if(!file.exists()) {
               file.getParentFile().mkdir();
               file.createNewFile();
           }
           fos = new FileOutputStream(file);
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
        fos.write(getData);
        if(fos!=null){
            fos.close();
        }
        if(inputStream!=null){
            inputStream.close();
        }

        System.out.println("info:"+url+" download success");

    }
    /**
     * 跳转到添加
     */
    @RequestMapping("/receiptsPayments_add")
    public String receiptsPaymentsAdd() {
        return PREFIX + "reconciliationDownload_add.html";
    }

    /**
     * 跳转到修改
     */
    @RequestMapping("/receiptsPayments_update/{receiptsPaymentsId}")
    public String receiptsPaymentsUpdate(@PathVariable Integer receiptsPaymentsId, Model model) {
        ReceiptsPayments receiptsPayments = receiptsPaymentsService.selectById(receiptsPaymentsId);
        model.addAttribute("item",receiptsPayments);
        LogObjectHolder.me().set(receiptsPayments);
        return PREFIX + "reconciliationDownload_edit.html";
    }

    /**
     * 获取分页列表
     */
    @RequestMapping(value = "/pageList")
    @ResponseBody
    public Object pageList(ReceiptsPayments receiptsPayments) {

        Page<ReceiptsPayments> page = new PageFactory<ReceiptsPayments>().defaultPage();

        YunRequest request = new YunRequest("OrderService", "queryInExpDetail");
        request.put("bizUserId", "#yunBizUserId_B2C#");
        request.put("accountSetNo", receiptsPayments.getAccountSetName());
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
}
