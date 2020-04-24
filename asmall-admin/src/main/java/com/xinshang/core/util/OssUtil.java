package com.xinshang.core.util;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.RandomUtil;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.xinshang.config.properties.OssProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.codec.binary.Base64;
import java.io.*;
import java.net.URL;
import java.util.Date;

/**
 * @author zhangjiajia
 * @date 2018年9月27日 14:14:57
 *  阿里云OSS操作工具类
 */
@Slf4j
public class OssUtil {


    /**
     * oss配置
     */
    private OssProperties ossProperties;

    /**
     * oss客户端
     */
    private OSSClient ossClient;

    /**
     * 获取阿里云OSS客户端对象
     * */
    public OssUtil(OssProperties ossProperties){
        this.ossProperties=ossProperties;
        ossClient = new OSSClient(ossProperties.getEndpoint(),ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret());
    }


    /**
     * 新建Bucket  --Bucket权限:私有
     * @param bucketName bucket名称
     * @return true 新建Bucket成功
     * */
    public boolean createBucket(String bucketName){
        Bucket bucket = ossClient.createBucket(bucketName);
        return bucketName.equals(bucket.getName());
    }


    /**
     * 删除Bucket
     * @param bucketName bucket名称
     * */
    public void deleteBucket(String bucketName){
        ossClient.deleteBucket(bucketName);
    }


    /**
     * 向阿里云的OSS存储中存储文件InputStream
     * @param inputStream 文件流
     * @param fileName 上传文件名字
     * @return String 唯一MD5数字签名
     * */
    public String uploadFile(InputStream inputStream, String fileName) {
        String url = null;
        try {
            //创建上传Object的Metadata
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(inputStream.available());
            metadata.setContentEncoding("utf-8");
            metadata.setContentType(FileUtil.getContentType(fileName));
            metadata.setContentDisposition("inline;filename=" + fileName);
            //上传文件
            ossClient.putObject(ossProperties.getBucketName(),ossProperties.getDir()+fileName, inputStream, metadata);
            //获取下载链接有效期无限制
            url= getUrl(fileName,-1L);
        } catch (Exception e) {
            log.error("上传阿里云OSS服务器异常." + e.getMessage(), e);
        }finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return url;
    }


    /**
     * 向阿里云的OSS存储中存储文件  --file也可以用
     * @param file 上传文件
     * @return String 唯一MD5数字签名
     * */
    public String uploadFile(MultipartFile file , String newName) throws Exception{
        InputStream inputStream = file.getInputStream();
        return uploadFile(inputStream, newName);
    }


    /**
     * 上传图片
     *
     * @param url
     */
    public void uploadImg(String url) throws Exception {
        File fileOnServer = new File(url);
        FileInputStream fin;
        try {
            fin = new FileInputStream(fileOnServer);
            String[] split = url.split("/");
            this.uploadFile(fin, split[split.length - 1]);
        } catch (FileNotFoundException e) {
            throw new Exception("图片上传失败");
        }
    }


    /**
     * 向阿里云的OSS存储中存储文件  --file也可以用InputStream替代
     *
     * @param base64Str 图片base64编码
     * @param fileName  上传文件名字
     * @return url 下载地址
     */
    public String uploadBase64Img(String base64Str, String fileName) throws Exception {
        MultipartFile mf = base64ToMultipart(base64Str);
        return uploadFile(mf.getInputStream(),fileName);
    }


    /**
     * 根据key获取OSS服务器上的文件输入流
     * @param bucketName bucket名称
     * @param filePath 文件路径
     * @param key 文件名称
     * @param key Bucket下的文件的路径名+文件名
     */
    public byte[] downloadFile(String bucketName, String filePath, String key){
        OSSObject ossObj = ossClient.getObject(bucketName, filePath + key);
        BufferedInputStream in=new BufferedInputStream(ossObj.getObjectContent());
        byte[] bytes = IoUtil.readBytes(in);
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    /**
     * 下载文件到流
     * @param bucketName
     * @param filePath
     * @param key
     * @return
     */
    public BufferedInputStream downloadFileWithStream(String bucketName, String filePath, String key){
        OSSObject ossObj = ossClient.getObject(bucketName, filePath + key);
        return new BufferedInputStream(ossObj.getObjectContent());
    }

    /**
     * 根据key删除OSS服务器上的文件
     * @param bucketName bucket名称
     * @param filePath 文件路径
     * @param key 文件名称
     */
    public void deleteFile(String bucketName, String filePath, String key){
        ossClient.deleteObject(bucketName, filePath+ key);
        log.info("删除" + bucketName + "下的文件" + filePath + key + "成功");
    }


    /**
     * 文件重命名方法
     * @param originalFilename 原始文件名
     * @return 新文件名
     */
    public String fileRename(String originalFilename){
        String substring = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        return RandomUtil.randomUUID() + substring;
    }


    /**
     * 获得url链接
     *
     * @param key
     * @return
     */
    public String getUrl(String key,Long expiration) {
        if(expiration<=0L){
            return "https://"+ossProperties.getBucketName()+"."+ossProperties.getEndpoint()+"/"+ossProperties.getDir()+key;
        }
        // 设置URL过期时间为1天
        Date date = new Date(System.currentTimeMillis() + expiration);
        // 生成URL
        URL url = ossClient.generatePresignedUrl(ossProperties.getBucketName(), key, date);
        if (url != null) {
            return url.toString();
        }
        return null;
    }


    /**
     * 销毁
     */
    public void destory() {
        ossClient.shutdown();
    }

    /**
     * 功能描述:
     *
     * @Param: [base64]
     * @Return: org.springframework.web.multipart.MultipartFile
     * @author: wangxiaokun
     * @date: 2018/9/27 13:46
     * @desc: base64转Multipart
     * @Modify:
     */
    public MultipartFile base64ToMultipart(String base64) {

        try {
            String[] baseStrs = base64.split(",");

            byte[] b =Base64.decodeBase64(baseStrs[1]);
            for(int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }

            return new BASE64DecodedMultipartFile(b, baseStrs[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}