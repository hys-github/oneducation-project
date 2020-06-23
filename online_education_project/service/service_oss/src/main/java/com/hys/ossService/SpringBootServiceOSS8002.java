package com.hys.ossService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Auth 86191
 * @Date 2020/4/13
 *      exclude = {DataSourceAutoConfiguration.class}：由于在其父类继承了mybatis跟数据库相关的类，所以不使用数据库可没能会报错
 */
@EnableDiscoveryClient
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = {"com.hys"})
public class SpringBootServiceOSS8002 {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootServiceOSS8002.class,args);
    }
}
