package cn.tripcode.trip.article;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableFeignClients
@SpringBootApplication
public class TripArticleApplication {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(TripArticleApplication.class, args);
        System.out.println("TripArticleApplication start success");
    }
}
