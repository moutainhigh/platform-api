package com.xinleju.cloud.oa.iwebOffice.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.core.FileURIResolver;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.w3c.dom.Document;
import org.docx4j.Docx4J;  
import org.docx4j.Docx4jProperties;  
import org.docx4j.convert.out.FOSettings;  
import org.docx4j.convert.out.HTMLSettings;  
import org.docx4j.fonts.IdentityPlusMapper;  
import org.docx4j.fonts.Mapper;  
import org.docx4j.fonts.PhysicalFonts;  
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
/**
 * word 转换成html
 */
public class WordToHtml {
    
    /**
     * 2007版本word转换成html
     * @throws IOException
     */
    public String Word2007ToHtml(String filepath,String fileName,String htmlName) throws IOException {
    	File imgPath = new File(filepath);
        if(!imgPath.exists()){//目录不存在则创建
            imgPath.mkdirs();
        }
        final String file = fileName;
        File f = new File(file);  
        if (!f.exists()) {  
            System.out.println("Sorry File does not Exists!"); 
            return null;
        } else {  
            if (f.getName().endsWith(".docx") || f.getName().endsWith(".DOCX")) {  
                  
                // 1) 加载word文档生成 XWPFDocument对象  
                InputStream in = new FileInputStream(f);  
                XWPFDocument document = new XWPFDocument(in);  
  
                // 2) 解析 XHTML配置 (这里设置IURIResolver来设置图片存放的目录)  
                final String imagepath = filepath+"image/";
                File imageFolderFile = new File(imagepath);  
                XHTMLOptions options = XHTMLOptions.create().URIResolver(new FileURIResolver(imageFolderFile));  
                options.setExtractor(new FileImageExtractor(imageFolderFile));  
                options.setIgnoreStylesIfUnused(false);  
                options.setFragment(true);  
                  
                // 3) 将 XWPFDocument转换成XHTML  
                OutputStream out = new FileOutputStream(new File(filepath + htmlName));  
                XHTMLConverter.getInstance().convert(document, out, options);  
                
                //也可以使用字符数组流获取解析的内容
//                ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
//                XHTMLConverter.getInstance().convert(document, baos, options);  
//                String content = baos.toString();
//                System.out.println(content);
//                 baos.close();
            } else {  
                System.out.println("Enter only MS Office 2007+ files");  
                return null;
            } 
        }  
        return htmlName;
    }  
    
    /**
     * /**
     * 2003版本word转换成html
     * @throws IOException
     * @throws TransformerException
     * @throws ParserConfigurationException
     */
    public String Word2003ToHtml(String filepath,String fileName,String htmlName) throws IOException, TransformerException, ParserConfigurationException {
    	int dotIndex = htmlName.lastIndexOf('.');
    	String exWord = htmlName.substring(dotIndex + 1);
    	
    	File imgPath = new File(filepath);
        if(!imgPath.exists()){//目录不存在则创建
            imgPath.mkdirs();
        }
        final String imagepath = filepath+"image/";
        final String imgName = htmlName.substring(0,dotIndex);
        final String file = fileName;
        InputStream input = new FileInputStream(new File(file));
        HWPFDocument wordDocument = new HWPFDocument(input);
        WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
        //设置图片存放的位置
        wordToHtmlConverter.setPicturesManager(new PicturesManager() {
            public String savePicture(byte[] content, PictureType pictureType, String suggestedName, float widthInches, float heightInches) {
                File imgPath = new File(imagepath);
                if(!imgPath.exists()){//图片目录不存在则创建
                    imgPath.mkdirs();
                }
                File file = new File(imagepath + imgName+suggestedName);
                try {
                    OutputStream os = new FileOutputStream(file);
                    os.write(content);
                    os.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "../../officeFiles/image/"+imgName+suggestedName;
            }
        });
        
        //解析word文档
        wordToHtmlConverter.processDocument(wordDocument);
        Document htmlDocument = wordToHtmlConverter.getDocument();
        
        File htmlFile = new File(filepath + htmlName);
        OutputStream outStream = new FileOutputStream(htmlFile);
        
        //也可以使用字符数组流获取解析的内容
//        ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
//        OutputStream outStream = new BufferedOutputStream(baos);

        DOMSource domSource = new DOMSource(htmlDocument);
        StreamResult streamResult = new StreamResult(outStream);

        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer serializer = factory.newTransformer();
        serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        serializer.setOutputProperty(OutputKeys.METHOD, "html");
        
        serializer.transform(domSource, streamResult);

        //也可以使用字符数组流获取解析的内容
//        String content = baos.toString();
//        System.out.println(content);
//        baos.close();
        outStream.close();
        return htmlName;
    }
    
//    /** 
//     * 把docx转成html 
//     * @param docxFilePath 
//     * @param htmlPath 
//     * @throws Exception  
//     */  
//    public static void convertDocxToHtml(String docxFilePath,String htmlPath) throws Exception{  
//    	ParseDocxToTemplateUtils.parseDocxToTemplateMap("D:\\officeFiles\\ChEDyFlKKyiAfUybAADRhpi4VJg652.docx", "D:\\officeFiles\\", "image/", "", "啊啊啊啊啊啊啊啊");
//    }  
    public static void docxToHtml(String filepath, String outpath) throws Docx4JException, FileNotFoundException{
    	WordprocessingMLPackage wmp = WordprocessingMLPackage.load(new File(filepath));
    	Docx4J.toHTML(wmp, "html/resources", "resources", new FileOutputStream(new File(outpath)));
    }
    /** 
     * 把docx转成html 
     * @param docxFilePath 
     * @param htmlPath 
     * @throws Exception  
     */  
    public static void convertDocxToHtml2(String docxFilePath,String htmlPath) throws Exception{  
          
    WordprocessingMLPackage wordMLPackage= Docx4J.load(new java.io.File(docxFilePath));  
  
        HTMLSettings htmlSettings = Docx4J.createHTMLSettings();  
        String imageFilePath=htmlPath.substring(0,htmlPath.lastIndexOf("\\")+1)+"/images";  
        htmlSettings.setImageDirPath(imageFilePath);  
        htmlSettings.setImageTargetUri( "images");  
        htmlSettings.setWmlPackage(wordMLPackage);  
  
        String userCSS = "html, body, div, span, h1, h2, h3, h4, h5, h6, p, a, img,  ol, ul, li, table, caption, tbody, tfoot, thead, tr, th, td " +  
                "{ margin: 0; padding: 0; border: 0;}" +  
                "body {line-height: 1;} ";  
          
        htmlSettings.setUserCSS(userCSS);  
  
        OutputStream os;  
          
        os = new FileOutputStream(htmlPath);  
  
        Docx4jProperties.setProperty("docx4j.Convert.Out.HTML.OutputMethodXML", true);  
  
        Docx4J.toHTML(htmlSettings, os, Docx4J.FLAG_EXPORT_PREFER_XSL);  
         
    }  
    public static void main(String[] args) throws Exception{
    	convertDocxToHtml2("D:\\officeFiles\\ChEDyFlKKyiAfUybAADRhpi4VJg652.docx", "D:\\officeFiles\\333.html");
    }
}