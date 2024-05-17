package cn.tripcode.trip.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@MapperScan("cn.tripcode.trip.user.mapper")
@SpringBootApplication
public class TripUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(TripUserApplication.class, args);
    }
}
