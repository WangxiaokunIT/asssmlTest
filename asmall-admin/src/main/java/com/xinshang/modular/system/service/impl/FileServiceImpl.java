package com.xinshang.modular.system.service.impl;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xinshang.config.properties.OssProperties;
import com.xinshang.config.properties.SystemProperties;
import com.xinshang.constant.Constants;
import com.xinshang.core.shiro.ShiroKit;
import com.xinshang.core.util.FileUtil;
import com.xinshang.core.util.OssUtil;
import com.xinshang.modular.system.dao.FileMapper;
import com.xinshang.modular.system.model.File;
import com.xinshang.modular.system.service.IFileService;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 上传文件 服务实现类
 * </p>
 *
 * @author zhangjiajia
 * @since 2018-07-03
 */
@Service
@Slf4j
public class FileServiceImpl extends ServiceImpl<FileMapper, com.xinshang.modular.system.model.File> implements IFileService {


    /**
     * asmall配置
     */
    @Autowired
    private SystemProperties systemProperties;

    /**
     * oss配置
     */
    @Autowired
    private OssProperties ossProperties;

    /**
     * 查询文件
     * @param name
     * @param beginTime
     * @param endTime
     * @param categoryId
     * @return
     */
    @Override
    public List<Map<String, Object>> selectFile(String name, String beginTime, String endTime, Integer categoryId) {
        return this.baseMapper.selectFile(name, beginTime, endTime, categoryId);
    }

    /**
     * 文件上传
     * @param file 上传文件
     * @param categoryId 文件类别
     * @return
     * @throws Exception
     */
    @Override
    @SneakyThrows
    public File upload(MultipartFile file,String categoryId) {

        String originalName = file.getOriginalFilename();
        String newName = FileUtil.rename(originalName);
        String fileSuffix=FileUtil.getFileSuffix(originalName);
        File fileEntity = new File();
        //文件保存路径
        String savePath;
        //上传到OSS
        if(systemProperties.getOssOpen()){
            OssUtil ou = new OssUtil(ossProperties);
            savePath = ou.uploadFile(file,newName);
            ou.destory();
            log.info(originalName+"已成功上传到OSS服务器,大小:"+file.getSize()+",浏览url:"+savePath);
            fileEntity.setStoreType(2);
        }else{//保存到本地
            String path = StrUtil.isNotBlank(systemProperties.getFileUploadPath())?systemProperties.getFileUploadPath():System.getProperty("user.home")+ java.io.File.separator+"upload"+ java.io.File.separator;
            savePath = FileUtil.saveFileFromInputStream(file.getInputStream(),path,newName);
            log.info(originalName+"已成功上传到服务器,大小:"+file.getSize()+",保存路径:"+savePath);
            fileEntity.setStoreType(1);
        }

        fileEntity.setCategoryId(categoryId);
        fileEntity.setCreator(ShiroKit.getUser().getId());
        fileEntity.setDownloadAmt(0);
        fileEntity.setGmtCreate(new Date());
        fileEntity.setName(newName);
        fileEntity.setOriginalName(originalName);
        fileEntity.setSize(new BigDecimal(file.getSize()));
        fileEntity.setSavePath(savePath);
        fileEntity.setType(fileSuffix);
        fileEntity.setState(1);
        fileEntity.setViewAmt(0);
        baseMapper.insert(fileEntity);
        return fileEntity;
    }

    /**
     * 文件预览
     * @param fileId
     * @param response
     */
    @Override
    @SneakyThrows
    public void view(Integer fileId, HttpServletResponse response) {
        File file =  baseMapper.selectById(fileId);
        file.setViewAmt(file.getViewAmt()+1);
        baseMapper.updateById(file);

        //OSS读取
        if(file.getStoreType().equals(Constants.FILE_SAVE_PATH_OSS)){
            OssUtil ou = new OssUtil(ossProperties);
            @Cleanup BufferedInputStream bis = ou.downloadFileWithStream(ossProperties.getBucketName(),ossProperties.getDir(),file.getName());
            FileUtil.viewFromByteArray(IoUtil.readBytes(bis),file.getOriginalName(),response);
            ou.destory();
        }else{//本地读取
            FileUtil.viewFromFilePath(file.getSavePath(),file.getOriginalName(),response);
        }
    }

    /**
     * 文件下载
     * @param fileId
     * @param request
     * @param response
     */
    @Override
    @SneakyThrows
    public void download(Integer fileId,HttpServletRequest request, HttpServletResponse response){
        File file =  baseMapper.selectById(fileId);
        file.setDownloadAmt(file.getDownloadAmt()+1);
        baseMapper.updateById(file);

        //OSS读取
        if(file.getStoreType().equals(Constants.FILE_SAVE_PATH_OSS)){
            OssUtil ou = new OssUtil(ossProperties);
            @Cleanup BufferedInputStream bis = ou.downloadFileWithStream(ossProperties.getBucketName(),ossProperties.getDir(),file.getName());
            FileUtil.downloadFromByteArray(IoUtil.readBytes(bis),file.getOriginalName(),request,response);
            ou.destory();
        }else{//本地读取
            FileUtil.downloadFromFilePath(file.getSavePath(),file.getOriginalName(),request,response);
        }

    }

}
