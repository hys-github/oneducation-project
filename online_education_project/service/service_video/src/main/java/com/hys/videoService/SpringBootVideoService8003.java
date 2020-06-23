package com.hys.videoService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Auth 86191
 * @Date 2020/4/19
 */
@EnableDiscoveryClient  // nacos注册注解
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = {"com.hys"})
public class SpringBootVideoService8003 {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootVideoService8003.class,args);
    }
}
