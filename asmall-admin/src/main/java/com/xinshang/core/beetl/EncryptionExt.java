package com.xinshang.core.beetl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.DES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;

import java.nio.charset.Charset;

/**
 * @author zhangjiajia
 * @since 19-3-20
 */
public class EncryptionExt {

    public static byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.DES.getValue()).getEncoded();

    /**
     * 加密字符串
     */
    public static String encryp(String value) {
        if(StrUtil.isBlank(value)){
            return value;
        }
        DES des = SecureUtil.des(key);
        String encryptHex = des.encryptHex(value);
        return encryptHex;
    }

    /**
     * 解密字符串
     * @param value
     * @return
     */
    public static String decrypt(String value) {
        if(StrUtil.isBlank(value)){
            return value;
        }
        DES des = SecureUtil.des(key);
        String str = des.decryptStr(value, Charset.defaultCharset());
        System.out.println(value+"|"+str);
        return str;
    }

    public static void main(String[] args) {
        String tableName = EncryptionExt.encryp("sys_region");
        System.out.println(tableName);
        String tableName1 = EncryptionExt.decrypt(tableName);
        System.out.println(tableName1);
    }
}
