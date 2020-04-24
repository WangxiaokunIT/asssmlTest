package com.xinshang.modular.biz.controller;

import cn.hutool.core.io.IoUtil;
import com.xinshang.modular.biz.utils.FileUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;

/**
 * 富文本编辑
 */
@Controller
@RequestMapping("/editor")
public class CkEditorController {

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public Object uploadPicture(@RequestParam("upload") MultipartFile file, HttpServletRequest request) {

        try {

            return FileUtil.uploadFilePath(file, request, "img");
        } catch (Exception e) {
            System.out.println("上传失败");
            e.printStackTrace();
            return "false";
        }
    }

    @RequestMapping(value = "/view/{type}/{name}")
    public void view(@PathVariable String name, @PathVariable String type, HttpServletRequest request, HttpServletResponse response) throws Exception{

        String nameType = name.substring(name.lastIndexOf(".") + 1);
        //获取的是项目的绝对路径  F:\tomcat_home\webapps\项目名称\
        String realPath = request.getSession().getServletContext().getRealPath("/");
        String savePath = realPath + "../upload/"+type+"/";
        if("mp4".equals(nameType.toLowerCase())) {
            File file =  new File(savePath + name);
            sendVideo(request, response, file, name);
        }else {
            try (FileInputStream fis =  new FileInputStream(savePath + name);
                 BufferedInputStream bis = new BufferedInputStream(fis);
                OutputStream bos = new BufferedOutputStream(response.getOutputStream())){
                bos.write(IoUtil.readBytes(bis));
                bos.flush();
                response.flushBuffer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * ios视频格式处理
     * @param request
     * @param response
     * @param file
     * @param fileName
     * @throws IOException
     */
    private void sendVideo(HttpServletRequest request, HttpServletResponse response, File file, String fileName) throws IOException {
        //只读模式
        RandomAccessFile randomFile = new RandomAccessFile(file, "r");
        long contentLength = randomFile.length();
        String range = request.getHeader("Range");
        int start = 0, end = 0;
        if(range != null && range.startsWith("bytes=")){
            String[] values = range.split("=")[1].split("-");
            start = Integer.parseInt(values[0]);
            if(values.length > 1){
                end = Integer.parseInt(values[1]);
            }
        }
        int requestSize = 0;
        if(end != 0 && end > start){
            requestSize = end - start + 1;
        } else {
            requestSize = Integer.MAX_VALUE;
        }

        byte[] buffer = new byte[4096];
        response.setContentType("video/mp4");
        response.setHeader("Accept-Ranges", "bytes");
        response.setHeader("ETag", fileName);
        response.setHeader("Last-Modified", new Date().toString());
        //第一次请求只返回content length来让客户端请求多次实际数据
        if(range == null){
            response.setHeader("Content-length", contentLength + "");
        }else{
            //以后的多次以断点续传的方式来返回视频数据
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);//206
            long requestStart = 0, requestEnd = 0;
            String[] ranges = range.split("=");
            if(ranges.length > 1){
                String[] rangeDatas = ranges[1].split("-");
                requestStart = Integer.parseInt(rangeDatas[0]);
                if(rangeDatas.length > 1){
                    requestEnd = Integer.parseInt(rangeDatas[1]);
                }
            }
            long length = 0;
            if(requestEnd > 0){
                length = requestEnd - requestStart + 1;
                response.setHeader("Content-length", "" + length);
                response.setHeader("Content-Range", "bytes " + requestStart + "-" + requestEnd + "/" + contentLength);
            }else{
                length = contentLength - requestStart;
                response.setHeader("Content-length", "" + length);
                response.setHeader("Content-Range", "bytes "+ requestStart + "-" + (contentLength - 1) + "/" + contentLength);
            }
        }
        ServletOutputStream out = response.getOutputStream();
        int needSize = requestSize;
        randomFile.seek(start);
        while(needSize > 0){
            int len = randomFile.read(buffer);
            if(needSize < buffer.length){
                out.write(buffer, 0, needSize);
            } else {
                out.write(buffer, 0, len);
                if(len < buffer.length){
                    break;
                }
            }
            needSize -= buffer.length;
        }
        randomFile.close();
        out.close();

    }
}
