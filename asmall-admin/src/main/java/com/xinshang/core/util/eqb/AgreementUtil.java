package com.xinshang.core.util.eqb;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.lowagie.text.pdf.BaseFont;
import com.xinshang.config.properties.EqbProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.charset.Charset;

@Slf4j
@Component
public class AgreementUtil {

    @Autowired
    private  EqbProperties eqbProperties;

    private static EqbProperties staticeqb;


    public static void main(String[] args) {
        //XMLWorkerHelper
        htmlToPDF(readToString("E:\\File\\protocol\\html\\9101803190000024.html"),"E:\\3.pdf");
    }

    @PostConstruct
    public void init(){
        this.staticeqb = eqbProperties;
    }

    public static void htmlToPDF(String htmlString,String pdfPath) {
        try {
            Document document = new Document(PageSize.A4);
            PdfWriter pdfWriter = PdfWriter.getInstance(document,
                    new FileOutputStream(pdfPath));
            document.open();
            document.addAuthor("pdf作者");
            document.addCreator("pdf创建者");
            document.addSubject("pdf主题");
            document.addCreationDate();
            document.addTitle("pdf标题,可在html中指定title");
            XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
            InputStream inputStream=null;
            worker.parseXHtml(pdfWriter, document, new ByteArrayInputStream(htmlString.getBytes("UTF-8")),inputStream,Charset.forName("UTF-8"),new AsianFontProvider());
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String readToString(String fileName) {
        String encoding = "UTF-8";
        File file = new File(fileName);
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(filecontent, encoding);
        } catch (UnsupportedEncodingException e) {
            System.err.println("The OS does not support " + encoding);
            e.printStackTrace();
            return null;
        }
    }

    public static void  toPdf(String inputFile,String outFile){
        try {
            OutputStream os = new FileOutputStream(outFile);

            ITextRenderer renderer = new ITextRenderer();

            ITextFontResolver fontResolver = renderer.getFontResolver();
            //启动中文支持
            log.info("字体路径"+staticeqb.getFontPath());
            fontResolver.addFont(staticeqb.getFontPath(), BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

            String url = new File(inputFile).toURI().toURL().toString();
            renderer.setDocument(url);
            renderer.layout();
            renderer.createPDF(os);
            os.close();
        }catch (Exception ex){
            log.info("生成PDF错误："+ex.getMessage());
        }

    }
}
