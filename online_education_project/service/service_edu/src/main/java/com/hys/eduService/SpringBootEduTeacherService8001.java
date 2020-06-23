package com.hys.eduService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Auth 86191
 * @Date 2020/4/9
 */
@EnableDiscoveryClient  // nacos注册
@EnableFeignClients(basePackages = "com.hys")
@SpringBootApplication
@ComponentScan(basePackages = "com.hys")
public class SpringBootEduTeacherService8001 {

    public static void main(String[] args) {

        SpringApplication.run(SpringBootEduTeacherService8001.class,args);

    }

}
