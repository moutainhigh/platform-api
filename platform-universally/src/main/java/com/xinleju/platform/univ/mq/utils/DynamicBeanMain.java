package com.xinleju.platform.univ.mq.utils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.*;
import org.springframework.context.event.ContextRefreshedEvent;

import java.io.IOException;
import java.util.Properties;

/**
 *  动态创建bean的主要入口
 *
 */
public class DynamicBeanMain implements ApplicationContextAware,ApplicationListener {

    private static Logger log = Logger.getLogger(DynamicBeanMain.class);

    private ConfigurableApplicationContext context;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = (ConfigurableApplicationContext)applicationContext;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {

//        ApplicationContext factory=new ClassPathXmlApplicationContext("classpath:appcontext.XML");
        // 如果是容器刷新事件OR Start Event
        if (event instanceof ContextRefreshedEvent) {
            try {
//                regDynamicBean();
//            } catch (IOException e) {
//                e.printStackTrace();
//                System.exit(-1);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(-1);
            }
            //System.out.println(event.getClass().getSimpleName()+" 事件已发生！");
        }
    }
    private void regDynamicBean() throws Exception {
        Properties properties=(Properties)context.getBean("configuration");
        //注册消息主题bean,即topic
        ProducerXmlLoadBeans producerXmlLoadBeans =  new ProducerXmlLoadBeans();
//        注册生产者
        producerXmlLoadBeans.doRegisterBean(properties,context);
        //注册消费者
        new ConsumerOriginalLoadBeans().doRegisterBean(properties,context,producerXmlLoadBeans.getCreatedQueueList());
//        String[] beanNames = this.app.getBeanDefinitionNames();
//        if(beanNames.length > 0){
//            log.info("--------------------创建的bean有---------------------");
//            Properties p=(Properties)app.getBean("configuration");
//            System.out.println(p.getProperty("zookeeper"));
//            for(String beanName :beanNames){
//                log.info(" "+beanName);
//            }
//        }
    }
}