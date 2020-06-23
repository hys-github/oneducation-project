package com.hys.ossService.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Auth 86191
 * @Date 2020/4/13
 *      当项目已启动，spring接口，spring加载之后，执行接口一个方法
 *      项目启动，自动注入，此方式直接就注入值，不需要手动注入(@Autowired)
 */
@Component
public class OSSProperties implements InitializingBean {
    // 地域节点
    @Value("${aliyun.oss.endPoint}")
    private String endPoint;

    // 创建的Bucket名
    @Value("${aliyun.oss.bucketName}")
    private String bucketName;

    //	c
    @Value("${aliyun.oss.accessKeyId}")
    private String accessKeyId;

    //	钥匙id的密码
    @Value("${aliyun.oss.accessKeySecret}")
    private String accessKeySecret;

    // Bucket 域名
    @Value("${aliyun.oss.bucketDomain}")
    private String bucketDomain;

    //定义公开静态常量
    public static String END_POIND;
    public static String ACCESS_KEY_ID;
    public static String ACCESS_KEY_SECRET;
    public static String BUCKET_NAME;
    public static  String BUCKET_DOMAIN;

    @Override
    // 这个方法在项目启动就执行，将properties中的值自动注入到此类中
    public void afterPropertiesSet() throws Exception {
        END_POIND = endPoint;
        ACCESS_KEY_ID = accessKeyId;
        ACCESS_KEY_SECRET = accessKeySecret;
        BUCKET_NAME = bucketName;
        BUCKET_DOMAIN=bucketDomain;
    }
}
