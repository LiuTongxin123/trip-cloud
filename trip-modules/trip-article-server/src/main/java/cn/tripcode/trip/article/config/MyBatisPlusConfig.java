package cn.tripcode.trip.article.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "cn.tripcode.trip.article.mapper")
public class MyBatisPlusConfig {
    @Bean
    public MybatisPlusInterceptor myBatisPlusInterceptor() {
       MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
       PaginationInnerInterceptor interceptor = new PaginationInnerInterceptor();
       interceptor.setOverflow(true);
       interceptor.setDbType(DbType.MYSQL);
       mybatisPlusInterceptor.addInnerInterceptor(interceptor);
       return mybatisPlusInterceptor;
    }
}
