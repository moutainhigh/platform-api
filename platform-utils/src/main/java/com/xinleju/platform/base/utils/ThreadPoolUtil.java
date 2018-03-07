package com.xinleju.platform.base.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by luoro on 2017/12/21.
 */
public class ThreadPoolUtil {
    private static class FixedThreadPool {
        private static final ExecutorService INSTANCE = Executors.newFixedThreadPool(10);
    }
    private ThreadPoolUtil(){}
    public static final ExecutorService getInstance() {
        return FixedThreadPool.INSTANCE;
    }
}
