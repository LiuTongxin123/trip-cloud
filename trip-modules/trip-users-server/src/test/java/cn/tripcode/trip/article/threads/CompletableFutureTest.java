package cn.tripcode.trip.article.threads;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@SpringBootTest(classes = CompletableFutureTest.class)
public class CompletableFutureTest {
    @Test
    public void test() {
        // 多线程加载图片，哪张图片最快获取到直接获取那个，其他就不要了
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("正在加载图片1");
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "图片1";
        });
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("正在加载图片2");
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "图片2";
        });
        CompletableFuture<Object> future = CompletableFuture.anyOf(future1, future2);
        try {
            Object o = future.get();
            System.out.println("最终结果: " + o);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
