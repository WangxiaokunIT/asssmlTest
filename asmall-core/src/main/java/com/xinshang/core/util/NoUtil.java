package com.xinshang.core.util;


import com.xinshang.core.enums.BizTypeEnum;
import com.xinshang.core.enums.UserTypeEnum;
import java.util.function.Function;


/**
 * @author zhangjiajia
 */
public class NoUtil {

    /**
     * 字母偏移量
     */
    private static Integer LETTER_DRIFT_VOLUME = 6;

    /**
     * 数字偏移量
     */
    private static Integer NUMBER_DRIFT_VOLUME = 4;


    /**
     * 生成业务编码
     * @param bizTypeEnum 业务类型
     * @param userTypeEnum 用户类型
     * @param id 用户id
     * @return
     */
    public static String generateCode(BizTypeEnum bizTypeEnum,UserTypeEnum userTypeEnum, Number id){

        String no = bizTypeEnum.getValue() + "|" + userTypeEnum.getValue() + "|" +id+ "|" +System.currentTimeMillis()/1000;
        System.out.println("原始str:"+no);
        return NoUtil.encrypt.apply(no);
    }

    /**
     * 生成商品编码
     * @param categroyId 产品分类id
     * @param id id
     * @return
     */
    public static String generateGoodsCode(Number categroyId, Number id){
        String no = BizTypeEnum.GOODS_NO.getValue() + "|" + categroyId + "|" +id+ "|" +System.currentTimeMillis()/1000;
        System.out.println("原始str:"+no);
        return NoUtil.encrypt.apply(no);
    }


    /**
     * 生成密码盐
     * @param phoneNo 手机号
     * @return
     */
    public static String generateSaltCode(String phoneNo){
        String no = BizTypeEnum.PHONE_NO.getValue()+"|"+System.currentTimeMillis()/1000;
        System.out.println("原始str:"+no);
        return NoUtil.encrypt.apply(no);
    }


    /**
     * 生成规格编码
     * @param goodsNumber 商品id
     * @param id id
     * @return
     */
    public static String generateSpecsCode(Number goodsNumber, Number id){
        String no = BizTypeEnum.SPECS_NO.getValue() + "|" + goodsNumber + "|" +id+ "|" +System.currentTimeMillis()/1000;
        System.out.println("原始str:"+no);
        return NoUtil.encrypt.apply(no);
    }

    /**
     * 解码编号
     * @param code
     * @return
     */
    public static String decryptCode(String code){
       return NoUtil.decrypt.apply(code);
    }


    /**
     * 编码
     * 0~9数字对应十进制48－57
     * A~Z字母对应的十进制65－90
     * a~z字母对应的十进制97－122
     * 偏移规则:数字+偏移量
     */
    private static Function<String,String> encrypt =(source)->{
        // 源字符串
        char[] sourceChars = source.toCharArray();
        char[] targetChars = new char[sourceChars.length];
        for (int i = 0; i < sourceChars.length; i++) {
            int ascii = (int)sourceChars[i];
            int targetAscii;
            if(ascii>=48&&ascii<=57){
                int temp = ascii+NUMBER_DRIFT_VOLUME;
                if(temp>57){
                    temp=47+(temp-57);
                }
                targetAscii=temp;
            }else if(ascii>=65&&ascii<=90){
                int temp = ascii + LETTER_DRIFT_VOLUME;
                if(temp>90){
                    temp=64+temp-90;
                }
                targetAscii=temp;
            }else if(ascii>=97&&ascii<=121){
                int temp = ascii + LETTER_DRIFT_VOLUME;
                if(temp>121){
                    temp=96+temp-121;
                }
                targetAscii=temp;
            }else{
                targetAscii=122;
            }
            //System.out.println("原始:"+ascii+"_结果:"+targetAscii);
            targetChars[i]=(char)targetAscii;
        }
        return new String(targetChars);
    };

    /**
     * 解码
     */
    private static Function<String,String> decrypt =(target)->{
        // 目标字符串
        char[] targetChars = target.toCharArray();
        // 源字符串
        char[] sourceChars = new char[targetChars.length];
        for (int i = 0; i < targetChars.length; i++) {
            int ascii = (int)targetChars[i];
            int sourceAscii;
            if(ascii>=48&&ascii<=57){
                int temp = ascii-NUMBER_DRIFT_VOLUME;
                if(temp<48){
                    temp=57-(47-temp);
                }
                sourceAscii=temp;
            }else if(ascii>=65&&ascii<=90){
                int temp = ascii - LETTER_DRIFT_VOLUME;
                if(temp<65){
                    temp=90-(64-temp);
                }
                sourceAscii=temp;
            }else if(ascii>=97&&ascii<=121){
                int temp = ascii - LETTER_DRIFT_VOLUME;
                if(temp<97){
                    temp=121-(96-temp);
                }
                sourceAscii=temp;
            }else if(ascii==122){
                sourceAscii=124;
            }else{
                sourceAscii=ascii;
            }
            //System.out.println("结果:"+ascii+"_原始:"+sourceAscii);
            sourceChars[i]=(char)sourceAscii;
        }
        return new String(sourceChars);
    };


    public static void main(String[] args) {
        //生成订单信息
        String orderId = NoUtil.generateCode(BizTypeEnum.GENERAL_ORDER,UserTypeEnum.CUSTOMER,456);
        String apply = NoUtil.decryptCode(orderId);
        System.out.println(orderId+"_"+apply);

    }
}

