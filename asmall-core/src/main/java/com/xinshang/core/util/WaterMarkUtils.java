package com.xinshang.core.util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * @author zhangjiajia
 */
public class WaterMarkUtils {
    /**
     * 图片添加水印-右下角
     *
     * @param inputStream       需要添加水印的图片输入流
     * @param markContentColor 水印文字的颜色
     * @param waterMarkContent 水印的文字
     */
    public static BufferedImage addOneWaterMark(InputStream inputStream, Color markContentColor, String waterMarkContent) throws Exception{
        // 读取原图片信息
        Image srcImg = ImageIO.read(inputStream);
        int srcImgWidth = srcImg.getWidth(null);
        int srcImgHeight = srcImg.getHeight(null);
        // 加水印
        float alpha = 0.5f;
        BufferedImage bufferedImage = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bufferedImage.createGraphics();
        g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
        Font font = new Font("宋体", Font.PLAIN, 15);
        //根据图片的背景设置水印颜色
        g.setColor(markContentColor);
        g.setFont(font);
        //设置水印文字透明度
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
        int x = srcImgWidth - getWatermarkLength(waterMarkContent, g) - 10;
        int y = srcImgHeight - 10;
        g.drawString(waterMarkContent, x, y);
        g.dispose();
        return bufferedImage;
    }

    /**
     * 获取水印文字总长度
     *
     * @param waterMarkContent 水印的文字
     * @param g
     * @return 水印文字总长度
     */
    public static int getWatermarkLength(String waterMarkContent, Graphics2D g) {
        return g.getFontMetrics(g.getFont()).charsWidth(waterMarkContent.toCharArray(), 0, waterMarkContent.length());
    }

    /**
     * 图片添加水印-右全屏斜线
     × @param inputStream  需要添加水印的图片输入流
     * @param markContentColor 水印文字的颜色
     * @param waterMarkContent 水印的文字
     */
    public static BufferedImage addMultipleWaterMark(InputStream inputStream, Color markContentColor, String waterMarkContent) throws Exception{
        //水印字体，大小
        Font font = new Font("宋体", Font.BOLD, 15);
        //设置水印文字的旋转角度
        Integer degree = -45;
        //设置水印透明度 默认为1.0  值越小颜色越浅
        float alpha = 0.5f;

        //文件转化为图片
        Image srcImg = ImageIO.read(inputStream);
        //获取图片的宽
        int srcImgWidth = srcImg.getWidth(null);
        //获取图片的高
        int srcImgHeight = srcImg.getHeight(null);
        // 加水印
        BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
        //得到画笔
        Graphics2D g = bufImg.createGraphics();
        g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
        //设置水印颜色
        g.setColor(markContentColor);
        //设置字体
        g.setFont(font);
        //设置水印文字透明度
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
        if (null != degree) {
            //设置水印旋转
            g.rotate(Math.toRadians(degree),(double)bufImg.getWidth(),(double)bufImg.getHeight());
        }
        JLabel label = new JLabel(waterMarkContent);
        FontMetrics metrics = label.getFontMetrics(font);
        //文字水印的宽
        int width = metrics.stringWidth(label.getText());
        // 图片的高  除以  文字水印的宽  打印的行数(以文字水印的宽为间隔)
        int rowsNumber = srcImgHeight/width+srcImgHeight%width;
        //图片的宽 除以 文字水印的宽  每行打印的列数(以文字水印的宽为间隔)
        int columnsNumber = srcImgWidth/width+srcImgWidth%width;
        //防止图片太小而文字水印太长，所以至少打印一次
        if(rowsNumber < 1){
            rowsNumber = 1;
        }
        if(columnsNumber < 1){
            columnsNumber = 1;
        }
        for(int j=0;j<rowsNumber;j++){
            for(int i=0;i<columnsNumber;i++){
                //画出水印,并设置水印位置
                g.drawString(waterMarkContent, i*width + j*width, -i*width + j*width);
            }
        }
        // 释放资源
        g.dispose();
        return bufImg;
    }
}


