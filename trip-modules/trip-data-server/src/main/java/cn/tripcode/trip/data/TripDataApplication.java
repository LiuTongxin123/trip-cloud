package cn.tripcode.trip.data;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
//启用spring任务调度

@EnableScheduling
@MapperScan(basePackages = "cn.tripcode.trip.data.mapper")
@SpringBootApplication
public class TripDataApplication {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(TripDataApplication.class, args);
    }
}
