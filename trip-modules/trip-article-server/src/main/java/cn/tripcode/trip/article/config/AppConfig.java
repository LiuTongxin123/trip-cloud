package cn.tripcode.trip.article.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class AppConfig {
    @Bean
    public  ThreadPoolExecutor businessThreadPoolExecutor() {
        //创建线程池的方式：
        //1. Executors 创建 =》不推荐
        // 默认创建的工作队列使用的是LinkedBlockingQueue，容量是Integer.MAX_VALUE
        // 工作队列容量过大，会导致核心线程工作过载，队列中任务数过多，导致内存溢出,非核心线程无法参与处理
        // new FixThreadPool
        //2. ThreadPoolExecutor 创建 =》推荐
        // 可以自定义线程池的各种参数
        return new ThreadPoolExecutor(10,50,10, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(100));
    }
}
