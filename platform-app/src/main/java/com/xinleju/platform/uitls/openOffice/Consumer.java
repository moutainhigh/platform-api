package com.xinleju.platform.uitls.openOffice;

import org.apache.log4j.Logger;
/**
 * Created by ly on 2017/9/19.
 */
public class Consumer implements Runnable {
    private static Consumer consumer;
    private static Logger logger = Logger.getLogger(Consumer.class);
    public static volatile boolean isRunning=true;
    public void run() {
        while(isRunning)
        {
            isRunning = false;
            DocCovertThread docCovertThread = new DocCovertThread();
            docCovertThread.run();
        }

    }
    public static Consumer getInstance(){
        if(consumer==null){
            consumer = new Consumer();
            System.out.println("初始化消费线程");
            logger.info("初始化消费线程");
        }
        return consumer;
    }

}