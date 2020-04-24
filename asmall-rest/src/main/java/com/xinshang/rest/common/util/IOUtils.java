package com.xinshang.rest.common.util;

import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import java.io.IOException;

/**
 * @Auther: wangxiaokun
 * @Date: 2018/9/27
 * 13:50
 * @Description:
 */
public class IOUtils {
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
    public static MultipartFile base64ToMultipart(String base64) {

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
}
