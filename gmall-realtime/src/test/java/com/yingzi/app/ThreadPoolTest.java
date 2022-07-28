package com.yingzi.app;

import com.yingzi.gmall.realtime.utils.ThreadPoolUtil;
import lombok.SneakyThrows;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author: yingzi
 * @Date: 2022/7/21 11:22
 * @Version 1.0
 */
public class ThreadPoolTest {

    public static void main(String[] args) {
        ThreadPoolExecutor threadPool = ThreadPoolUtil.getThreadPool();

        for (int i = 0; i < 10; i++) {
            threadPool.submit(new Runnable() {
                @SneakyThrows
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + "yingzi");
                    Thread.sleep(2000);
                }
            });
        }
    }
}
