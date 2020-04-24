package com.xinshang.core.util;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.xinshang.core.exception.SystemException;
import com.xinshang.core.exception.SystemExceptionEnum;
import lombok.Cleanup;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.UriUtils;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author zhangjiajia
 */
@Slf4j
@UtilityClass
public class FileUtil {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

    /**
     * 写文件,只用于小文件
     *
     * @param file
     * @param b
     */
    public void wirte(File file, byte[] b) {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(b);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 读文件,该方法只只用于小文件读取
     *
     * @param file
     * @return
     */
    public byte[] readFileToByteArray(File file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] ret = IoUtil.readBytes(fis);
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 指定位置开始写入文件
     *
     * @param file    输入文件
     * @param outPath 输出文件的路径(路径+文件名)
     * @throws IOException
     */
    public void randomAccessFile(String outPath, File file) {
        File dirFile = new File(outPath);
        //以读写的方式打开目标文件
        try (RandomAccessFile raFile = new RandomAccessFile(dirFile, "rw");
             BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file))) {
            raFile.seek(raFile.length());
            byte[] buf = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buf)) != -1) {
                raFile.write(buf, 0, length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存文件
     *
     * @param in
     * @param path
     * @param filename
     *
     */
    public String saveFileFromInputStream(InputStream in, String path, String filename) {
        File file = new File(path + File.separator + filename);
        return saveFileFromInputStream(in,file);
    }

    /**
     * 保存文件
     *
     * @param in
     * @param file
     *
     */
    public String saveFileFromInputStream(InputStream in, File file) {

        try {

            if(!file.exists()){
                file.createNewFile();
            }

            @Cleanup FileOutputStream out = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
                out.flush();
            }
            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }



    /**
     * 文件重命名
     *
     * @param fileName
     * @return
     */
    public String renameWithTime(String fileName) {
        int pot = fileName.lastIndexOf(".");
        if (pot != -1) {
            return fileName.replace(fileName.substring(pot), "_" + System.currentTimeMillis() + fileName.substring(pot));
        } else {
            return fileName + "_" + System.currentTimeMillis();
        }
    }

    /**
     * 文件重命名
     *
     * @param fileName
     * @return
     */
    public String renameWithUUID(String fileName) {
        int pot = fileName.lastIndexOf(".");
        if (pot != -1) {
            return fileName.replace(fileName.substring(pot), "_" + UUID.randomUUID().toString().replaceAll("-", "") + fileName.substring(pot));
        } else {
            return fileName + "_" + UUID.randomUUID().toString().replaceAll("-", "");
        }
    }

    /**
     * 获取文件后缀
     *
     * @param fileName
     * @return
     */
    public String getFileSuffix(String fileName) {
        int pot = fileName.lastIndexOf(".");

        if (pot != -1) {
            return fileName.substring(pot + 1);
        } else {
            return null;
        }
    }

    /**
     * 浏览器下载本地文件
     *
     * @param path     文件路径
     * @param newName   新名称
     * @param request
     * @param response
     * @return
     */
    public void downloadFromFilePath(String path, String newName, HttpServletRequest request, HttpServletResponse response) {

        // path是指欲下载的文件的路径。
        File file = new File(path);
        if(!file.exists()){
            return;
        }
        try (FileInputStream fis = new FileInputStream(file)){
            downloadFromByteArray(IoUtil.readBytes(fis),StrUtil.isEmpty(newName)?file.getName():newName,request,response);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 浏览器查看本地文件
     *
     * @param path     文件路径
     * @param response
     * @return
     */
    public void viewFromFilePath(String path,String fileName,HttpServletResponse response) {

        // path是指欲下载的文件的路径。
        File file = new File(path);
        if(!file.exists()){
            return;
        }

        if(StrUtil.isBlank(fileName)){
            fileName=file.getName();
        }
        try (FileInputStream fis = new FileInputStream(file)){
            viewFromByteArray(IoUtil.readBytes(fis),fileName,response);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 浏览器查看本地文件
     *
     * @param path     文件路径
     * @param response
     * @return
     */
    public void viewFromFilePathWithWaterMark(String path,String fileName,HttpServletResponse response,String waterMarkText) {

        // path是指欲下载的文件的路径。
        File file = new File(path);
        if(!file.exists()){
            return;
        }
        if(StrUtil.isBlank(fileName)){
            fileName=file.getName();
        }
        try (FileInputStream fis = new FileInputStream(file)){
            viewFromByteArrayWithWaterMark(IoUtil.readBytes(fis),fileName,response,waterMarkText);
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    /**
     * 从byte数组下载
     * @param buffer   文件byte数组
     * @param newName   新名称
     * @param request
     * @param response
     * @return
     */
    public void downloadFromByteArray(byte[] buffer, String newName, HttpServletRequest request, HttpServletResponse response) {
        // 取得文件名。
        String fileName = newName;
        if (StrUtil.isBlank(newName)|| ArrayUtil.isEmpty(buffer)) {
            return;
        }
        try (BufferedOutputStream toClient = new BufferedOutputStream(response.getOutputStream())) {
            //获取浏览器环境
            String userAgent = request.getHeader("User-Agent");
            // 针对IE或者以IE为内核的浏览器：
            fileName = (userAgent.contains("MSIE") || userAgent.contains("Trident")) ? java.net.URLEncoder.encode(fileName, "UTF-8") : new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Content-Length", "" + buffer.length);
            response.setContentType("application/octet-stream");
            response.setHeader("Content-type", "text/html;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            toClient.write(buffer);
            toClient.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 浏览器打开文件
     * @param data 数据
     * @param fileName 文件名
     * @param response
     */
    public void viewFromByteArray(byte[] data,String fileName,HttpServletResponse response) {
        //设置响应的媒体类型，这样浏览器会识别
        response.reset();
        response.setHeader("Content-Type",getContentType(getFileSuffix(fileName)));
        response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
        try(OutputStream os = response.getOutputStream()) {
            os.write(data);
            os.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 浏览器打开文件添加水印
     * @param data 数据
     * @param fileName 文件名
     * @param response 响应
     * @param waterMarkText 水印内容
     */
    public void viewFromByteArrayWithWaterMark(byte[] data,String fileName,HttpServletResponse response,String waterMarkText){
        //设置响应的媒体类型，这样浏览器会识别
        String contentType = getContentType(getFileSuffix(fileName));
        if(!"image/jpeg".equalsIgnoreCase(contentType)){
           log.warn("只有jpg的图片才能添加水印");
           return;
        }

        response.setHeader("Content-Type",contentType);
        response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
        try(ByteArrayInputStream bis = new ByteArrayInputStream(data);
            OutputStream os = response.getOutputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            BufferedImage bi = WaterMarkUtils.addMultipleWaterMark(bis, Color.WHITE, waterMarkText);
            ImageIO.write(bi,getFileSuffix(fileName),baos);
            data = baos.toByteArray();
            os.write(data);
            os.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 下载网络文件到本地
     * @param uri
     * @param savePath
     */
    public void downloadFromNet(String uri,String savePath) {
        // 下载网络文件
        int byteSum = 0;
        try {
            URL url = new URL(uri);
            URLConnection conn = url.openConnection();
            try(InputStream inStream = conn.getInputStream();
                FileOutputStream fs = new FileOutputStream(savePath)) {
                byte[] buffer = new byte[1204];
                int length;
                while ((length = inStream.read(buffer)) != -1) {
                    byteSum += length;
                    System.out.println(byteSum);
                    fs.write(buffer, 0, length);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * NIO way
     */
    public byte[] toByteArray(String filePath) {

        try (FileInputStream fs = new FileInputStream(new File(filePath));
            FileChannel channel = fs.getChannel()){
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
            while ((channel.read(byteBuffer)) > 0) {
                // do nothing
                 System.out.println("reading");
            }
            return byteBuffer.array();
        } catch (IOException e) {
            throw new SystemException(SystemExceptionEnum.FILE_READING_ERROR);
        }
    }

    /**
     * 删除目录
     *
     * @author fengshuonan
     * @date 2017/10/30 下午4:15
     */
    public boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }



    /**
     * 通过文件名判断并获取OSS服务文件上传时文件的contentType
     * @param fileName 文件名
     * @return 文件的contentType
     */
    public String getContentType(String fileName){
        String fileExtension = fileName.substring(fileName.lastIndexOf(".")+1);
        switch (fileExtension.toLowerCase()){
            case "jpg":
                return "image/jpeg";
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "txt":
                return "text/plain";
            case "doc":
                return "application/msword";
            case "docx":
                return "application/msword";
            case "xls":
                return "application/x-xls";
            case "xlsx":
                return "application/x-xls";
            case "ppt":
                return "application/vnd.ms-powerpoint";
            case "pptx":
                return "application/vnd.ms-powerpoint";
            case "xml":
                return "text/xml";
            case "pdf":
                return "application/pdf";
            case "apk":
                return "application/vnd.android.package-archive";
            case "mp4":
                return "video/mpeg4";
            case "gif":
                return "image/gif";
            case "bmp":
                return "image/bmp";
            case "vsd":
                return "application/vnd.visio";
            case "html":
                return "text/html";
            default:
                return null;
        }
    }
    public static String rename(String fileName) {
        int pot=fileName.lastIndexOf(".");

        if(pot!=-1){
            return fileName.replace(fileName.substring(pot), "_"+sdf.format(new Date())+fileName.substring(pot));
        }else {
            return fileName+"_"+sdf.format(new Date());
        }
    }


    public static String getType(String fileName) {
        int pot=fileName.lastIndexOf(".");

        if(pot!=-1){
            return fileName.substring(pot+1);
        }else {
            return null;
        }
    }

    /**
     * 得到文件流
     * @param url
     * @return
     */
    public static byte[] getFileStream(String url){
        try {
            URL httpUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)httpUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            //通过输入流获取图片数据
            InputStream inStream = conn.getInputStream();
            //得到图片的二进制数据
            byte[] btImg = readInputStream(inStream);
            return btImg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从输入流中获取数据
     * @param inStream 输入流
     * @return
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while( (len=inStream.read(buffer)) != -1 ){
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }


    public static HttpServletResponse download(byte[] buffer,String rename,HttpServletRequest request,HttpServletResponse response) throws Exception {

        // path是指欲下载的文件的路径。

        // 取得文件名。
        String filename = rename;
        //获取浏览器环境
        String userAgent = request.getHeader("User-Agent");

        // 针对IE或者以IE为内核的浏览器：
        if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
            filename = java.net.URLEncoder.encode(filename, "UTF-8");
        } else {
            // 非IE浏览器的处理：
            filename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
        }

        // 清空response
        response.reset();
        // 设置response的Header
        response.addHeader("Content-Disposition", "attachment;filename=" + filename);
        response.addHeader("Content-Length", "" + buffer.length);
        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
        response.setContentType("application/octet-stream");
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        toClient.write(buffer);
        toClient.flush();
        toClient.close();
        return response;
    }


/*
    public static void main(String[] args) {
        System.out.println(FileUtil.renameWithTime("ddf.zip"));
        System.out.println(FileUtil.renameWithUUID("ddf.zip"));
        System.out.println(FileUtil.getFileSuffix("ddf.zip"));
    }*/

}
