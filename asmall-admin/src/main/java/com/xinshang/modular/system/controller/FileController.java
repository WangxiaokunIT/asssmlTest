package com.xinshang.modular.system.controller;

import cn.hutool.core.util.StrUtil;
import com.xinshang.core.base.controller.BaseController;
import com.xinshang.core.base.tips.ErrorTip;
import com.xinshang.core.common.annotion.Permission;
import com.xinshang.core.log.LogObjectHolder;
import com.xinshang.modular.system.model.File;
import com.xinshang.modular.system.service.IFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * 上传文件控制器
 *
 * @author fengshuonan
 * @date 2018-07-03 15:25:41
 */
@Controller
@RequestMapping("/file")
public class FileController extends BaseController {

    private String PREFIX = "/system/file/";

    @Autowired
    private IFileService fileService;


    /**
     * 跳转到上传文件首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "file.html";
    }

    /**
     * 跳转到添加上传文件
     */
    @RequestMapping("/file_add/{categoryId}")
    public Object fileAdd(@PathVariable Integer categoryId, Model model) {
        if(categoryId==null){
            return new ErrorTip(400, "文件类别不能为空");
        }
        model.addAttribute("categoryId",categoryId);
        return PREFIX + "file_add.html";
    }


    /**
     * 跳转到修改上传文件
     */
    @RequestMapping("/file_update/{fileId}")
    public String fileUpdate(@PathVariable Integer fileId, Model model) {
        File file = fileService.selectById(fileId);
        model.addAttribute("item",file);
        LogObjectHolder.me().set(file);
        return PREFIX + "file_edit.html";
    }

    /**
     * 获取上传文件列表
     */
    @RequestMapping(value = "/list")

    @ResponseBody
    public Object list(@RequestParam(required = false) String name, @RequestParam(required = false) String beginTime, @RequestParam(required = false) String endTime, @RequestParam(required = false) Integer categoryId) {
        return fileService.selectFile(name, beginTime, endTime, categoryId);
    }

    /**
     * 新增上传文件
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(File file) {
        fileService.insert(file);
        return SUCCESS_TIP;
    }


    /**
     * 文件会一个个上传过来
     *
     *
     */
    @RequestMapping(value = "/upload/{categoryId}")
    @ResponseBody
    public Object upload(@RequestParam(value = "file") MultipartFile file,
                         @PathVariable String categoryId) throws Exception{
        File uploadFile = fileService.upload(file,categoryId);
        SUCCESS_TIP.setMessage(uploadFile.getSavePath());
        return SUCCESS_TIP;
    }


    /**
     * 文件预览
     *
     *
     */
    @RequestMapping(value = "/view/{fileId}")
    public void view(@PathVariable Integer fileId,HttpServletResponse response) throws Exception{
        fileService.view(fileId,response);
    }

    /**
     * 文件下载
     *
     *
     */
    @RequestMapping(value = "/download/{fileId}")
    public void download(@PathVariable Integer fileId, HttpServletRequest request, HttpServletResponse response) throws Exception{
        fileService.download(fileId,request,response);
    }


    /**
     * 删除上传文件
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam String fileIds) {
        if(StrUtil.isNotBlank(fileIds)) {
            fileService.deleteBatchIds(Arrays.asList(fileIds.split(",")));
        }
        return SUCCESS_TIP;
    }

    /**
     * 修改上传文件
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(File file) {
        fileService.updateById(file);
        return SUCCESS_TIP;
    }

    /**
     * 上传文件详情
     */
    @RequestMapping(value = "/detail/{fileId}")
    @ResponseBody
    public Object detail(@PathVariable("fileId") Integer fileId) {
        return fileService.selectById(fileId);
    }


}
