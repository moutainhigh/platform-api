package com.xinleju.platform.uitls.openOffice;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ly on 2017/9/19.
 */
public class ThreadPool {
    private static ExecutorService threadPool = null;
    public static ExecutorService getThreadPool(){
        if(threadPool==null){
            threadPool = Executors.newSingleThreadExecutor();
        }
        return  threadPool;
    }

}