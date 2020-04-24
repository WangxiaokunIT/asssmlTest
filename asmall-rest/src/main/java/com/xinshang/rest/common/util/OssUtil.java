package com.xinshang.rest.common.util;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.text.UnicodeUtil;
import cn.hutool.core.util.RandomUtil;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.xinshang.rest.config.properties.OssProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

/**
 * 阿里云OSS操作工具类
 * 2018年9月27日 14:14:57
 * @author zhangjiajia
 */
@Slf4j
public class OssUtil {

    private OssProperties ossProperties;

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
            metadata.setContentType(getContentType(fileName));
            metadata.setContentDisposition("inline;filename=" + fileName);
            //上传文件
            PutObjectResult putResult = ossClient.putObject(ossProperties.getBucketName(),ossProperties.getDir()+fileName, inputStream, metadata);

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
        return   IoUtil.readBytes(in);
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
     * 通过文件名判断并获取OSS服务文件上传时文件的contentType
     * @param fileName 文件名
     * @return 文件的contentType
     */
    public final String getContentType(String fileName){
        String fileExtension = fileName.substring(fileName.lastIndexOf(".")+1);
        if("bmp".equalsIgnoreCase(fileExtension)) return "image/bmp";
        if("gif".equalsIgnoreCase(fileExtension)) return "image/gif";
        if("jpeg".equalsIgnoreCase(fileExtension) || "jpg".equalsIgnoreCase(fileExtension)  ) return "image/jpeg";
        if("png".equalsIgnoreCase(fileExtension)) return "image/png";
        if("html".equalsIgnoreCase(fileExtension)) return "text/html";
        if("txt".equalsIgnoreCase(fileExtension)) return "text/plain";
        if("vsd".equalsIgnoreCase(fileExtension)) return "application/vnd.visio";
        if("ppt".equalsIgnoreCase(fileExtension) || "pptx".equalsIgnoreCase(fileExtension)) return "application/vnd.ms-powerpoint";
        if("doc".equalsIgnoreCase(fileExtension) || "docx".equalsIgnoreCase(fileExtension)) return "application/msword";
        if("xml".equalsIgnoreCase(fileExtension)) return "text/xml";
        if("pdf".equalsIgnoreCase(fileExtension)) return "application/pdf";
        if("xls".equalsIgnoreCase(fileExtension)||"xlsx".equalsIgnoreCase(fileExtension)) return "application/x-xls";
        if("apk".equalsIgnoreCase(fileExtension)) return "application/vnd.android.package-archive";
        if("mp4".equalsIgnoreCase(fileExtension)) return "video/mpeg4";
        return "text/html";
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
     * @Auther: wangxiaokun
     * @Date: 2018/9/27 13:46
     * @Description: base64转Multipart
     * @Modify:
     */
    public MultipartFile base64ToMultipart(String base64) {

        try {
            String[] baseStrs = base64.split(",");

            BASE64Decoder decoder = new BASE64Decoder();
            byte[] b = new byte[0];
            b = decoder.decodeBuffer(baseStrs[1]);

            for(int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }

            return new BASE64DecodedMultipartFile(b, baseStrs[0]);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public BufferedInputStream downloadFileWithStream(String bucketName, String filePath, String key){
        OSSObject ossObj = ossClient.getObject(bucketName, filePath + key);
        return new BufferedInputStream(ossObj.getObjectContent());
    }
    /**
     * 下载文件到本地(支持https)
     *
     * @param fileUrl 远程地址
     * @throws Exception
     */

    public static InputStream getNetUrlHttps(String fileUrl) {
        //对本地文件进行命名
        String file_name = reloadFile(fileUrl);
        File file = null;

        InputStream in = null;
        DataOutputStream out = null;
        try {
            file = File.createTempFile("net_url", file_name);

            SSLContext sslcontext = SSLContext.getInstance("SSL", "SunJSSE");
            sslcontext.init(null, new TrustManager[]{new X509TrustUtiil()}, new java.security.SecureRandom());
            URL url = new URL(fileUrl);
            HostnameVerifier ignoreHostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslsession) {
                    log.warn("WARNING: Hostname is not matched for cert.");
                    return true;
                }
            };
            HttpsURLConnection.setDefaultHostnameVerifier(ignoreHostnameVerifier);
            HttpsURLConnection.setDefaultSSLSocketFactory(sslcontext.getSocketFactory());
            HttpsURLConnection urlCon = (HttpsURLConnection) url.openConnection();
            urlCon.setConnectTimeout(6000);
            urlCon.setReadTimeout(6000);
            int code = urlCon.getResponseCode();
            if (code != HttpURLConnection.HTTP_OK) {
                throw new Exception("文件读取失败");
            }

            in = urlCon.getInputStream();

            return in;
        } catch (Exception e) {
            log.error("远程图片获取错误："+fileUrl);
            e.printStackTrace();
        }

        return in;
    }

    public static String reloadFile(String oleFileName) {
        oleFileName = getFileName(oleFileName);
        if (StringUtils.isEmpty(oleFileName)) {
            return oleFileName;
        }
        //得到后缀
        if (oleFileName.indexOf(".") == -1) {
            //对于没有后缀的文件，直接返回重命名
            return UUID.randomUUID().toString();
        }
        String[] arr = oleFileName.split("\\.");
        // 根据uuid重命名图片
        String fileName = UUID.randomUUID().toString() + "." + arr[arr.length - 1];

        return fileName;
    }

    /**
     * 把带路径的文件地址解析为真实文件名 /25h/upload/hc/1448089199416_06cc07bf-7606-4a81-9844-87d847f8740f.mp4 解析为 1448089199416_06cc07bf-7606-4a81-9844-87d847f8740f.mp4
     *
     * @param url
     */
    public static String getFileName(final String url) {
        if (StringUtils.isEmpty(url)) {
            return url;
        }
        String newUrl = url;
        newUrl = newUrl.split("[?]")[0];
        String[] bb = newUrl.split("/");
        String fileName = bb[bb.length - 1];
        return fileName;
    }
}