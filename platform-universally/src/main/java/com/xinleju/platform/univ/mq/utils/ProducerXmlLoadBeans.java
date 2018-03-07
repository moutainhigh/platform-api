package com.xinleju.platform.univ.mq.utils;

import com.alibaba.fastjson.JSON;
import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.PageBeanInfo;
import com.xinleju.platform.tools.data.JacksonUtils;
import com.xinleju.platform.univ.mq.dto.TopicDto;
import com.xinleju.platform.univ.mq.dto.service.TopicDtoServiceCustomer;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.*;

/**
 * Created by xubaoyong on 2017/3/30.
 */
public class ProducerXmlLoadBeans {

    private static Logger log = Logger.getLogger(ProducerXmlLoadBeans.class);

    private TopicDtoServiceCustomer topicDtoServiceCustomer;

    private List<TopicDto> createdQueueList = new ArrayList<>();

    private Map<String,String> queueExistMap = new HashMap();

    public void doRegisterBean(Properties properties , ConfigurableApplicationContext applicationContext) throws Exception {
        DefaultListableBeanFactory reg =  (DefaultListableBeanFactory) applicationContext .getBeanFactory();
        /**
         * 获得bean,主题mqTopicService
         */
        this.topicDtoServiceCustomer = (TopicDtoServiceCustomer) applicationContext.getBean(TopicDtoServiceCustomer.class);

        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(reg);

//        BeanFactory beanFactory = (BeanFactory)reg;
        String generateXml = generateXml(reg);
        System.out.println(generateXml);
//        File file = new File("applicationContext-rabbit-dyn.xml");
//        SpringFileWirteUtil.writeFile(file,generateXml);
        //ApplicationContext factory2=new FileSystemXmlApplicationContext(file.getPath());

         reader.loadBeanDefinitions(new MemoryXmlResource(generateXml));
         //Document document = DocumentHelper.parseText(generateXml);
         
         //reader.registerBeanDefinitions(parse(document), new MemoryXmlResource(generateXml));
//        reader.loadBeanDefinitions(new FileSystemResource(file));
        if(log.isDebugEnabled()){
            String[] beanNames = reg.getBeanDefinitionNames();
            if(beanNames.length > 0){
                log.info("--------------------动态创建的bean有---------------------");
                for(String beanName :beanNames){
                    log.info(" {}"+beanName);
                }
                log.info(" 结束");

            }
        }

    }
    
    /**
     * 实现dom4j向org.w3c.dom.Document的转换
     * @param doc
     * @return
     * @throws Exception
     */
    public static org.w3c.dom.Document parse(Document doc) throws Exception {
     if (doc == null) {
      return (null);
     }
     java.io.StringReader reader = new java.io.StringReader(doc.toString());
     org.xml.sax.InputSource source = new org.xml.sax.InputSource(reader);
     javax.xml.parsers.DocumentBuilderFactory documentBuilderFactory = javax.xml.parsers.DocumentBuilderFactory
       .newInstance();
     javax.xml.parsers.DocumentBuilder documentBuilder = documentBuilderFactory
       .newDocumentBuilder();
     return (documentBuilder.parse(source));
    }
    
    
    
    private String generateXml(DefaultListableBeanFactory beanFactory ) throws Exception {
        String  content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<beans xmlns=\"http://www.springframework.org/schema/beans\"\n" +
                "\t  xmlns:dubbo=\"http://code.alibabatech.com/schema/dubbo\"\n"+
                "\t   xmlns:util=\"http://www.springframework.org/schema/util\"\n" +
                "\t   xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:rabbit=\"http://www.springframework.org/schema/rabbit\"\n" +
                "\t   xsi:schemaLocation=\"http://www.springframework.org/schema/beans\n" +
                "     http://www.springframework.org/schema/beans/spring-beans-3.0.xsd\n" +
                "     http://www.springframework.org/schema/beans\n" +
                "     http://www.springframework.org/schema/beans/spring-beans-3.0.xsd\n" +
                "     http://www.springframework.org/schema/rabbit\n" +
                "     http://www.springframework.org/schema/rabbit/spring-rabbit-1.0.xsd\n" +
                "        http://www.springframework.org/schema/util\n" +
                "        http://www.springframework.org/schema/util/spring-util-4.3.xsd\n" +
                "   http://code.alibabatech.com/schema/dubbo          \n" +
                "        http://code.alibabatech.com/schema/dubbo/dubbo.xsd\">";


        StringBuffer stringBuffer = new StringBuffer(content);
        doRecursionTopic( beanFactory, 0, stringBuffer);
        //创建Exchange
        createExchangeString ( beanFactory, stringBuffer);
        //创建mqTempleatea
        this.createAmqpTemplate(beanFactory, stringBuffer);

        //this.createQueueListener(beanFactory, stringBuffer);
//        stringBuffer.append("<rabbit:listener-container  id=\"listener-container\" connection-factory=\"rabitConnectionFactory\" acknowledge=\"manual\">\n" +
//                "<rabbit:listener queues=\"Queue__mytopic\" ref=\"commonConsumer\"/>\n" +
//
//                "</rabbit:listener-container>");

        stringBuffer.append("\n</beans>");
        content  = stringBuffer.toString();
        if(log.isInfoEnabled()){
            log.info("自定义bean中消费者对应的bean为"+content);
        }
        return content;
    }
    
    
    /*private String generateXml(DefaultListableBeanFactory beanFactory ) throws Exception {
        String  content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<beans xmlns=\"http://www.springframework.org/schema/beans\"\n" +
                "\t  xmlns:dubbo=\"http://code.alibabatech.com/schema/dubbo\"\n"+
                "\t   xmlns:util=\"http://www.springframework.org/schema/util\"\n" +
                "\t   xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:rabbit=\"http://www.springframework.org/schema/rabbit\"\n" +
                "\t   xsi:schemaLocation=\"http://www.springframework.org/schema/beans\n" +
                "     http://www.springframework.org/schema/beans/spring-beans-3.0.xsd\n" +
                "     http://www.springframework.org/schema/beans\n" +
                "     http://www.springframework.org/schema/beans/spring-beans-3.0.xsd\n" +
                "     http://www.springframework.org/schema/rabbit\n" +
                "     http://www.springframework.org/schema/rabbit/spring-rabbit-1.0.xsd\n" +
                "        http://www.springframework.org/schema/util\n" +
                "        http://www.springframework.org/schema/util/spring-util-4.3.xsd\n" +
                "   http://code.alibabatech.com/schema/dubbo          \n" +
                "        http://code.alibabatech.com/schema/dubbo/dubbo.xsd\">";


        StringBuffer stringBuffer = new StringBuffer();
        doRecursionTopic( beanFactory, 0, stringBuffer);
        //创建Exchange
        createExchangeString ( beanFactory, stringBuffer);
        //创建mqTempleatea
        this.createAmqpTemplate(beanFactory, stringBuffer);

        this.createQueueListener(beanFactory, stringBuffer);
//        stringBuffer.append("<rabbit:listener-container  id=\"listener-container\" connection-factory=\"rabitConnectionFactory\" acknowledge=\"manual\">\n" +
//                "<rabbit:listener queues=\"Queue__mytopic\" ref=\"commonConsumer\"/>\n" +
//
//                "</rabbit:listener-container>");

        //stringBuffer.append("\n</beans>");
        String content  = stringBuffer.toString();
        if(log.isInfoEnabled()){
            log.info("自定义bean中消费者对应的bean为"+content);
        }
        ClassPathResource resource = new ClassPathResource("applicationContext-rabbit.xml");
        String last = writeFileContent(resource.getFile(),content);
        return last;
    }*/
    
    
    
    
    /**
     * 向文件中写入内容
     * @param filepath 文件路径与名称
     * @param newstr  写入的内容
     * @return
     * @throws IOException
     */
    public String writeFileContent(File file,String newstr) throws IOException{
        Boolean bool = false;
        String filein = newstr+"\r\n";//新写入的行，换行
        String temp  = "";
        
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        FileOutputStream fos  = null;
        PrintWriter pw = null;
        File fileNew = null;
        StringBuffer buffer = new StringBuffer();
        try {
            
            //将文件读入输入流
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            
            
            //文件原有内容
            for(int i=0;(temp =br.readLine())!=null;i++){
            	if(temp.equals("</beans>")){
            		buffer.append(newstr);
            		// 行与行之间的分隔符 相当于“\n”
                    buffer = buffer.append(System.getProperty("line.separator"));
                    buffer.append(temp);
            	}else{
            		buffer.append(temp);
            		// 行与行之间的分隔符 相当于“\n”
                    buffer = buffer.append(System.getProperty("line.separator"));
            	}
            }
            //buffer.append(filein);
            
            fos = new FileOutputStream(file);
            pw = new PrintWriter(fos);
            pw.write(buffer.toString().toCharArray());
            pw.flush();
            bool = true;
            
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }finally {
            //不要忘记关闭
            if (pw != null) {
                pw.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
        return buffer.toString();
    }
    
    
    
    
    
    //递归调用topic
    private void  doRecursionTopic(DefaultListableBeanFactory beanFactory,int pageNo,StringBuffer stringBuffer) throws Exception {
        int pageSize = 10000;
        PageBeanInfo page =  getAllTopic(pageNo,pageSize);
        List<LinkedHashMap> pageData = page.getList();
        List<TopicDto> topicList = new ArrayList<>();
        for(LinkedHashMap data :pageData){
            String mqMessage= JacksonUtils.toJson(data);
            TopicDto mqTopicDto = JacksonUtils.fromJson(mqMessage,TopicDto.class);
            topicList.add(mqTopicDto);
        }
        /**
         * 动态创建queue
         *
         */
        this.createQueueBeanByTopicList(beanFactory,topicList,stringBuffer);


        int total = page.getTotal();
        int countPage = total <= 0 ? 0:(int) Math.ceil((double) total / pageSize);
        if(pageNo < countPage){
            pageNo ++;
            doRecursionTopic( beanFactory, pageNo,stringBuffer);//执行下一页
        }
    }
    /**
     * 从数据库中加载所有可以使用的topic
     * @return
     */
    public PageBeanInfo getAllTopic(int pageNum,int pageSize ) throws Exception {
        String userInfo = "";
        Map<String, Object> params = new HashMap();
        params.put("delflag",false);
        params.put("start",pageNum*pageSize);
        params.put("limit",pageSize);
        params.put("start",pageNum);

        PageBeanInfo pageInfo= null;
        String pageResultString  = topicDtoServiceCustomer.getPage(null, JSON.toJSONString(params));
        DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(pageResultString, DubboServiceResultInfo.class);
        if(dubboServiceResultInfo.isSucess()){
            String resultInfo= dubboServiceResultInfo.getResult();
            pageInfo=JacksonUtils.fromJson(resultInfo, PageBeanInfo.class);

        }
        return pageInfo;
    }
    /**
     * 创建topicBean
     */
    public void createQueueBeanByTopicList(DefaultListableBeanFactory beanFactory,List<TopicDto> topicList,StringBuffer stringBuffer){
        if(topicList == null || topicList.size() == 0){
            log.warn("not get mqtopic from db");
            return;
        }else {
            String templetate = " \n\t<rabbit:queue id=\"#topicName#\" name=\"#topicName#\" durable=\"true\" auto-delete=\"false\" exclusive=\"false\" />";
            for (TopicDto tempTopic : topicList) {
                String beanName = String.format("Queue_%s_%s",tempTopic.getTendId()==null?"":tempTopic.getTendId(),tempTopic.getTopic());
                if(beanFactory.containsBean(beanName) || queueExistMap.containsKey(beanName)){
                    log.warn(String.format("%s have duplicate",beanName));
                }else {
                   String queueTempletate = templetate.replaceAll("#topicName#",beanName);

                    createdQueueList.add(tempTopic);
                    stringBuffer.append( queueTempletate);
                    queueExistMap.put(beanName,beanName);
                }

            }
        }
    }

    private void createExchangeString (DefaultListableBeanFactory beanFactory,StringBuffer stringBuffer){
        if(beanFactory.containsBean(ConfigConstant.EXCHANGE_NAME)){
            return;
        }
        if(createdQueueList == null || createdQueueList.size() ==0){
            return;
        }
//<rabbit:direct-exchange name="exchangeTest" durable="true" auto-delete="false">
//        <rabbit:bindings>
//            <rabbit:binding queue="queueTest" key="queueTestKey"></rabbit:binding>
//
//            <rabbit:binding queue="queueOrder" key="queueOrderKey"></rabbit:binding>
//        </rabbit:bindings>
//    </rabbit:direct-exchange>
        stringBuffer.append("\n\t<rabbit:direct-exchange  id= \"" + ConfigConstant.EXCHANGE_NAME + "\" name=\""+ConfigConstant.EXCHANGE_NAME+"\" durable=\"true\" auto-delete=\"false\">");
        stringBuffer.append("\n\t\t<rabbit:bindings>");
        //  List<Topic > createdQueueList = new ArrayList<>();
        for(TopicDto tempTopic :createdQueueList){
            String queueName = String.format("Queue_%s_%s",tempTopic.getTendId()==null?"":tempTopic.getTendId(),tempTopic.getTopic());
            stringBuffer.append(" \n\t\t\t<rabbit:binding queue=\""+queueName+"\" key=\""+queueName+"_KEY\"></rabbit:binding>");
        }
        stringBuffer.append("\n\t\t</rabbit:bindings>");
        stringBuffer.append("\n\t</rabbit:direct-exchange>");

    }

    private void  createAmqpTemplate(DefaultListableBeanFactory beanFactory,StringBuffer stringBuffer){
        if(beanFactory.containsBean(ConfigConstant.RABBIT_TEMPLATEID)){
            return;
        }
//        <rabbit:template id="amqpTemplate"  connection-factory="rabitConnectionFactory"  exchange="exchangeTest"  encoding="UTF-8" message-converter="jsonMessageConverter" />
        String templetate = "\n\t<rabbit:template id=\""+ConfigConstant.RABBIT_TEMPLATEID+"\"  connection-factory=\""+ConfigConstant.RABBITIT_CONNECTIONFACTORY+"\"  exchange=\""+ConfigConstant.EXCHANGE_NAME+"\"  encoding=\"UTF-8\" message-converter=\""+ConfigConstant.JSONMESSAGECONVERTER+"\" />";
        stringBuffer.append(templetate);
    }
    
    
    private void  createQueueListener(DefaultListableBeanFactory beanFactory,StringBuffer stringBuffer){
        if(beanFactory.containsBean(ConfigConstant.RABBIT_LISTENER)){
            return;
        }
        
        String listenterId = "queueListenter";
        stringBuffer.append("\n\t<bean id=\""+listenterId+"\" class=\"com.xinleju.platform.univ.mq.listenter.QueueListenter \"/>");
        
//        <rabbit:template id="amqpTemplate"  connection-factory="rabitConnectionFactory"  exchange="exchangeTest"  encoding="UTF-8" message-converter="jsonMessageConverter" />
        String templetate = "\n\t<rabbit:listener-container connection-factory=\""+ConfigConstant.RABBITIT_CONNECTIONFACTORY+"\"  acknowledge=\"auto\">";
        stringBuffer.append(templetate);
        for(TopicDto tempTopic :createdQueueList){
            String queueName = String.format("Queue_%s_%s",tempTopic.getTendId()==null?"":tempTopic.getTendId(),tempTopic.getTopic());
            stringBuffer.append("\n\t<rabbit:listener queues=\""+queueName+"\" ref=\""+listenterId+"\"/>");
        }
        stringBuffer.append("\n\t</rabbit:listener-container>");
        
    }

    public List<TopicDto> getCreatedQueueList() {
        return createdQueueList;
    }
}
