package com.xinshang.rest.common.util;

import com.xinshang.rest.config.properties.OssProperties;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@UtilityClass
public class FileUtil {

    private static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmssSSS");


    /**
     * 文件上传工具类服务方法
     * @param file
     * @param filePath
     * @param fileName
     * @throws Exception
     */
    @SneakyThrows
    public void uploadFile(byte[] file, String filePath, String fileName){

        File targetFile = new File(filePath);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
        try(FileOutputStream out = new FileOutputStream(filePath+fileName)){
            out.write(file);
            out.flush();
        }
    }

    @SneakyThrows
    public Map<String, Object> uploadFilePath(MultipartFile file, OssProperties ossProperties){
        Map<String, Object> map = new HashMap<>(5);
        String originalName = file.getOriginalFilename();
        String newName = renameUUID(originalName);
        String hz = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String path;
        //上传到OSS
        OssUtil ou = new OssUtil(ossProperties);
        path = ou.uploadFile(file,newName);
        ou.destory();
        log.info(originalName+"已成功上传到OSS服务器,大小:"+file.getSize()+",浏览url:"+path);
        map.put("uploaded", true);
        map.put("url", path);
        map.put("message", "上传成功");
        map.put("fileName", file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf(".")));
        map.put("fileType", hz);

        return map;
    }
    @SneakyThrows
    public Map<String, Object> uploadFileServer(MultipartFile file, HttpServletRequest request, String type){
        //图片文件类型 image/jpeg
        String hz = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String uuid = UUID.randomUUID().toString().replace("-","");
        //图片名字   xxxx.jpg
        String fileName = uuid + hz;
        //获取的是项目的相对路径  ”/项目名称”
        String contextPath = request.getContextPath();
        //获取的是项目的绝对路径  F:\tomcat_home\webapps\项目名称\
        String realPath = request.getSession().getServletContext().getRealPath("/");
        // 获取的是服务的访问地址 http://localhost:8080/项目名称/
        String basePath = request.getScheme()+"://"+request.getServerName() + ":" + request.getServerPort()+contextPath+"/";

        String savePath = realPath + "../upload/"+type+"/";
        //设置返回的图片url
        String url = basePath + "editor/view/" +type+"/"+fileName;
        uploadFile(file.getBytes(), savePath, fileName);
        Map<String, Object> map = new HashMap<>();
        map.put("uploaded", true);
        map.put("url", url);
        map.put("message", "上传成功");
        map.put("fileName", file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf(".")));
        map.put("fileType", hz);
        return map;
    }

    public static String renameUUID(String fileName) {
        int pot=fileName.lastIndexOf(".");

        if(pot!=-1){
            return fileName.replace(fileName.substring(pot), "_"+sdf2.format(new Date())+ "_"+UUID.randomUUID().toString().replaceAll("-", "") +fileName.substring(pot));
        }else {
            return fileName+"_"+sdf2.format(new Date()) + "_"+UUID.randomUUID().toString().replaceAll("-", "");
        }
    }
}
