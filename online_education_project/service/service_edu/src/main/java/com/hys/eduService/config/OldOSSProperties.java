package com.hys.eduService.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @author 86191
 *		aliyun上开通的oss创建一个bucket，并开启accessKey
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "aliyun.oss")
public class OldOSSProperties {
	
	// 地域节点
	private String endPoint;

	// 创建的Bucket名
	private String bucketName;

	//	操作aliyun上oss的必须钥匙id
	private String accessKeyId;

	//	钥匙id的密码
	private String accessKeySecret;

	// Bucket 域名
	private String bucketDomain;

}
