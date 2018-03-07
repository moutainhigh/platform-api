package com.xinleju.platform.uitls.openOffice;

import java.io.File;
import java.io.FileNotFoundException;

import com.xinleju.platform.uitls.openOffice.utils.FileUtils;
import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.support.PropertiesLoaderUtils;

/**
 * Created by lenovo on 2017/11/29.
 */
public class OpenOfficePDFConverter implements PDFConverter{
    private static  OfficeManager officeManager;
    @Value("#{configuration['openOffice.home']}")
    private static String OFFICE_HOME;//"D:\\Program Files\\OpenOffice.org 3";
    @Value("#{configuration['openOffice.port']}")
    private static int port;
    public  void convert2PDF(String inputFile, String pdfFile) {

        if(inputFile.endsWith(".txt")){
            String odtFile = FileUtils.getFilePrefix(inputFile)+".odt";
            if(new File(odtFile).exists()){
                System.out.println("odt文件已存在！");
                inputFile = odtFile;
            }else{
                try {
                    FileUtils.copyFile(inputFile,odtFile);
                    inputFile = odtFile;
                } catch (FileNotFoundException e) {
                    System.out.println("文档不存在！");
                    e.printStackTrace();
                }
            }
        }
        startService();
        System.out.println("进行文档转换:" + inputFile + " --> " + pdfFile);
        OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);
        converter.convert(new File(inputFile),new File(pdfFile));
        stopService();
        System.out.println("文档转换成功");
    }
    public void convert2PDF(String inputFile) {
        String pdfFile = FileUtils.getFilePrefix(inputFile)+".pdf";
        convert2PDF(inputFile,pdfFile);
    }
    public static void startService(){
        DefaultOfficeManagerConfiguration configuration = new DefaultOfficeManagerConfiguration();
        try {
            System.out.println("准备启动服务....");
            configuration.setOfficeHome(OFFICE_HOME);//设置OpenOffice.org安装目录
            configuration.setPortNumbers(port); //设置转换端口，默认为8100
            configuration.setTaskExecutionTimeout(1000 * 60 * 5L);//设置任务执行超时为5分钟
            configuration.setTaskQueueTimeout(1000 * 60 * 60 * 1L);//设置任务队列超时为1小时
            officeManager = configuration.buildOfficeManager();
            officeManager.start();    //启动服务
            System.out.println("office转换服务启动成功!");
        } catch (Exception ce) {
            System.out.println("office转换服务启动失败!详细信息:" + ce);
        }
    }
    public static void stopService(){
        System.out.println("关闭office转换服务....");
        if (officeManager != null) {
            officeManager.stop();
        }
        System.out.println("关闭office转换成功!");
    }
}
