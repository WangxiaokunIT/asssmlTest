package com.xinshang.rest.common.util;

import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import java.io.File;
import java.io.FileOutputStream;

public class SealUtil {

    public static void main(String[] args) {
        try {
            imagePdf("/home/xgl/setting/as/protocol/1.png","/home/xgl/setting/as/protocol/2.png", "/home/xgl/setting/as/protocol/pdf/40.pdf","/home/xgl/setting/as/protocol/pdf/1112.pdf");
        }catch (Exception ex){
            System.out.println("转换出错");
        }
    }
    /**
     * 功能描述:
     * @Description:
     * @Author: Mr.Jie
     */
    public static void imagePdf(String aiSaiSeal,String legalSeal, String urlPdf,String toPDFUrl) throws Exception {
        //要加水印的原pdf文件路径
        PdfReader reader = new PdfReader(urlPdf, "PDF".getBytes());
        //加了水印后要输出的路径
        PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(toPDFUrl));
        // 插入水印
        Image img = Image.getInstance(aiSaiSeal);
        Image img2 = Image.getInstance(legalSeal);

        //原pdf文件的总页数
        int pageSize = reader.getNumberOfPages();
        //印章位置
        img.setAbsolutePosition(180, 260);
        img2.setAbsolutePosition(400, 317);

        //印章大小
/*
        img.scalePercent(30);
        img2.scalePercent(30);
*/
/*
        img.scaleToFit(420, 420);//自定义大小
        img2.scaleToFit(200, 200);//自定义大小
*/
        img.scaleAbsolute(mmTopx(42), mmTopx(42));// 直接设定显示尺寸
        img2.scaleAbsolute(mmTopx(20), mmTopx(20));// 直接设定显示尺寸
        //背景被覆盖
        PdfContentByte under = stamp.getUnderContent(4);
        PdfContentByte under2 = stamp.getUnderContent(4);

        //文字被覆盖
        //PdfContentByte under = stamp.getOverContent(4);
        //添加电子印章
        under.addImage(img);
        under2.addImage(img2);

        // 关闭
        stamp.close();
        //关闭
        reader.close();
        //删除临时文件
        File file = new File(urlPdf);
        file.delete();

    }

    public static float mmTopx(float mm){
        mm = (float) (mm *3.33) ;
        return mm ;
    }
}
