package cn.tripcode.trip.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableFeignClients
@ComponentScan(basePackages = {"cn.tripcode.trip.core", "cn.tripcode.trip.search"})
@SpringBootApplication
public class TripSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(TripSearchApplication.class, args);
    }
}
