package com.xinshang.modular.system.service;

import com.baomidou.mybatisplus.service.IService;
import com.xinshang.modular.system.model.File;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 上传文件 服务类
 * </p>
 *
 * @author zhangjiajia
 * @since 2018-07-03
 */
public interface IFileService extends IService<File> {


    /**
     * 根据条件查询上传文件列表
     */
    List<Map<String, Object>> selectFile(String name,String beginTime,String endTime,Integer categoryId);

    /**
     * 文件上传
     * @param file
     * @param categoryId
     * @return
     * @throws Exception
     */
    File upload(MultipartFile file,String categoryId);

    /**
     * 图片查看
     * @param fileId
     * @param response
     * @throws Exception
     */
    void view(Integer fileId, HttpServletResponse response);

    /**
     * 文件下载
     * @param fileId
     * @param request
     * @param response
     * @throws Exception
     */
    void download(Integer fileId, HttpServletRequest request, HttpServletResponse response);

}
