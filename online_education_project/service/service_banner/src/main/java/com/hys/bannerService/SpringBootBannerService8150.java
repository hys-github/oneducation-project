package com.hys.bannerService;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Auth 86191
 * @Date 2020/4/21
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.hys"})
@MapperScan("com.hys.bannerService.mapper")
public class SpringBootBannerService8150 {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootBannerService8150.class,args);
    }
}
