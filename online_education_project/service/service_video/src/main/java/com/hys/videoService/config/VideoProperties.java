package com.hys.videoService.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Auth 86191
 * @Date 2020/4/19
 */
@Data
@Component
@ConfigurationProperties(prefix = "aliyun.video")
public class VideoProperties {

    //	操作aliyun上oss的必须钥匙id
    private String accessKeyId;

    //	钥匙id的密码
    private String accessKeySecret;
}
