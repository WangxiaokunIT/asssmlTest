package com.xinshang.core.util.eqb;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author jim
 * @date 2017-3-9 生成PDF
 */
@Slf4j
public class PDFUtil {
	public final static String HTML_FTL = "/protocol/ftl/";
	public final static String HTML = "/protocol/html/";
	public final static String DEST = "/protocol/pdf/";
	/**
	 * Creates a PDF with the words "Hello World"
	 *
	 * @param file
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static boolean createPdfTest(String file) throws IOException, DocumentException {
		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
		document.open();
		XMLWorkerHelper.getInstance().parseXHtml(writer, document, new FileInputStream(HTML_FTL), Charset.forName("UTF-8"));
		document.close();
		return false;
	}

	/**
	 * Creates a PDF
	 *
	 * @param destFile
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static boolean createPdf(String sourceFile, String destFile,Map<String,Object> paramMap) throws IOException, DocumentException {
	/*	Document document = new Document(PageSize.A4, 36, 36, 90, 36);
		//检测文件路径
		String fileDir = destFile.substring(0,destFile.indexOf(DEST)).concat(DEST);
		File dir = new File(fileDir);
		if(!dir.exists() && !dir.isDirectory()){
			dir.mkdir();
		}
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(destFile));

		document.open();


		//创建基础字体
//		BaseFont bf = BaseFont.createFont(sourceFile.substring(0,sourceFile.indexOf("/upload")).concat(FONT_PATH),BaseFont.IDENTITY_H,BaseFont.EMBEDDED);
//		Font font = new Font(bf,6);
		//添加页眉
		PdfPTable header = new PdfPTable(1);
		// set defaults
		header.setTotalWidth(527);
		header.setLockedWidth(true);
		header.getDefaultCell().setFixedHeight(20);
		header.getDefaultCell().setBorder(Rectangle.BOTTOM);
		header.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);

		// add image
//			Image logo;
//			logo = Image.getInstance(HeaderFooterPageEvent.class.getResource("/logo.png"));
//			header.addCell(logo);

		// add text
		PdfPCell text = new PdfPCell(new Phrase("NO:"+StringUtil.getString(paramMap.get("ordid")),new Font(Font.FontFamily.HELVETICA,6)));
		text.setPaddingBottom(5);
		text.setPaddingLeft(2);
		text.setBorder(Rectangle.BOTTOM);
		text.setBorderColor(BaseColor.LIGHT_GRAY);
		header.addCell(text);
		header.setHorizontalAlignment(Element.ALIGN_RIGHT);
		// write content
		header.writeSelectedRows(0, -1, 34, 803, writer.getDirectContent());
		// write to document
//		document.add(header);

		XMLWorkerHelper.getInstance().parseXHtml(writer, document, new FileInputStream(sourceFile),
				Charset.forName("UTF-8"));
		document.close();*/
		return false;
	}

	/**
	 * 依据模板生成html文件
	 * @param paramMap
	 * @param filePath
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	public static String configurationg(Map<String,Object> paramMap,String filePath) throws IOException,TemplateException{
		try{
			/* 在整个应用的生命周期中，这个工作你应该只做一次。 */
			/* 创建和调整配置。 */
			Configuration cfg = new Configuration();
			File file = new File((String)paramMap.get("ftlPath"));
			if(!file.exists() && !file.isDirectory()){
				file.mkdir();
			}
			cfg.setDefaultEncoding("UTF-8");
			cfg.setDirectoryForTemplateLoading(file);
			cfg.setObjectWrapper(new DefaultObjectWrapper());

			/* 在整个应用的生命周期中，这个工作你可以执行多次 */
			/* 获取或创建模板 */
			Template temp = cfg.getTemplate((String)paramMap.get("ftlFile"));
			/* 将模板和数据模型合并写到静态文件中 */
			file = new File(filePath);
			if(!file.exists() && !file.isDirectory()){
				file.mkdir();
			}
			String htmlFile = filePath.concat(paramMap.get("filename")+".html");
			Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(htmlFile),"utf-8"));
			temp.process(paramMap, out);
			out.flush();
			out.close();
			return htmlFile;
		}catch(Exception e){
			log.error("生成html失败",e);
			throw e;
		}
	}

	public static void configuration() throws IOException, TemplateException {
		/* 在整个应用的生命周期中，这个工作你应该只做一次。 */
		/* 创建和调整配置。 */
		Configuration cfg = new Configuration();
		cfg.setDirectoryForTemplateLoading(new File("D:/workspace/xinshangProject/src/main/resources/jeecg/pdf/"));
		cfg.setObjectWrapper(new DefaultObjectWrapper());
		/* 在整个应用的生命周期中，这个工作你可以执行多次 */
		/* 获取或创建模板 */
		Template temp = cfg.getTemplate("jkxydy.ftl");
		/* 创建数据模型 */
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("user", "Big Joe");
		Map<String, Object> latest = new HashMap<String, Object>();
		root.put("latestProduct", latest);
		latest.put("url", "products/greenmouse.HTML_FTL");
		latest.put("name", "green mouse");
		/* 将模板和数据模型合并 */
		Writer out = new OutputStreamWriter(System.out);
		temp.process(root, out);
		out.flush();
	}

	/**
	 * Main method
	 */
	public static void main(String[] args) throws IOException, DocumentException {
		File file = new File(DEST);
		file.getParentFile().mkdirs();
		createPdfTest(DEST);
	}
}
