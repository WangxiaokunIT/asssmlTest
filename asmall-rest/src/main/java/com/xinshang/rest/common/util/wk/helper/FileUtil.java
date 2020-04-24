package com.xinshang.rest.common.util.wk.helper;


import com.xinshang.core.util.SpringContextHolder;
import com.xinshang.rest.common.util.OssUtil;
import com.xinshang.rest.config.properties.OssProperties;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
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
    public String uploadFilePath(MultipartFile file, OssProperties ossProperties){
//        OssProperties ossProperties = SpringContextHolder.getBean(OssProperties.class);
        String originalName = file.getOriginalFilename();
        log.info("原文件名:{}",originalName);
        String newName = fileRename(originalName);
        log.info("新文件名:{}",newName);
        String hz = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String path;
        //上传到OSS
        OssUtil ou = new OssUtil(ossProperties);
        path = ou.uploadFile(file,newName);
        ou.destory();
        log.info(originalName+"已成功上传到OSS服务器,大小:"+file.getSize()+",浏览url:"+path);
        return path;
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

    /**
     * 文件重命名方法
     * @param originalFilename 原始文件名
     * @return 新文件名
     */
    public String fileRename(String originalFilename){
        String substring = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        return UUID.randomUUID().toString().replaceAll("-", "") + substring;
    }


    public static void getImageStream(String filename, String url, HttpServletResponse response) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                byte[] buffer = toByteArray(inputStream);
                // 清空response
                response.reset();
                // 设置response的Header
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename,"UTF-8"));
                response.addHeader("Content-Length", "" + connection.getContentLength());
                OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
                response.setContentType("application/octet-stream");
                response.setHeader("Content-type", "ext/plain;charset=UTF-8");
                response.setCharacterEncoding("UTF-8");
                toClient.write(buffer);
                toClient.flush();
                toClient.close();
            }
        } catch (IOException e) {
            System.out.println("获取网络文件出现异常，文件路径为：" + url);
            e.printStackTrace();
        }
    }

    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024*4];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }
}
