package com.hys.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.comm.ResponseMessage;
import com.aliyun.oss.model.PutObjectResult;

public class AliyunOSSUtil {
	
	/**
	 * @author 86191 上传，下载文件到阿里云上的OSS服务器上 专门负责上传文件到 OSS 服务器的工具方法 
	 *
	 * @param endpoint OSS 参数
	 * @param accessKeyId OSS 参数 
	 * @param accessKeySecret OSS 参数
	 * @param inputStream 要上传的文件的输入流，自己创建输入流，且文件地址自己定义
	 * @param bucketName OSS 参数
	 * @parambucketDomain OSS 参数 
	 * @param originalName 要上传的文件的原始文件名
	 */
	public static ResultJsonToHtmlUtil<String> uploadFileToAliyunOSS(
					String endpoint, 
					String accessKeyId,
					String accessKeySecret,
					String bucketName,
					String bucketDomain, 
					
					// 要上传的文件的原始文件名
					String originalName,
					
					// 定义输入上传的文件
					InputStream inputStream){
		
		// 创建 OSSClient 实例。 
		OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
		
		// 生成上传文件的目录
		String folderName = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		
		// 生成上传文件在 OSS 服务器上保存时的文件名 
		// 如上传之前本地原始文件名：beautfulgirl.jpg 
		// 生成文件名：wer234234efwer235346457dfswet346235.jpg 
		// 使用 UUID 生成文件主体名称,replace("-", "")将字符串中“-”代替为""（空）
		String uploadFileName = UUID.randomUUID().toString().substring(0,10).replace("-", "");
		
		// 从原始文件名中获取文件扩展名
		String extensionName = originalName.substring(originalName.lastIndexOf("."));
		
		try {
			// 使用目录、文件主体名称、文件扩展名称拼接得到对象名称
			String objectName = folderName + "/" + uploadFileName + extensionName;
			
			// 调用 OSS 客户端对象的方法上传文件并获取响应结果数据
			PutObjectResult putObjectResult = ossClient.putObject(bucketName, objectName, inputStream);
			
			// 从响应结果中获取具体响应消息
			ResponseMessage responseMessage = putObjectResult.getResponse();
			
			// 根据响应状态码判断请求是否成功
			if(responseMessage == null) {
				
				// 拼接访问刚刚上传的文件的路径
				String ossFileAccessPath = bucketDomain + "/" + objectName;
				
				// 当前方法返回成功
				return ResultJsonToHtmlUtil.successWithData(ossFileAccessPath);
			}else {
				
				// 获取响应状态码
				int statusCode = responseMessage.getStatusCode();
				
				// 如果请求没有成功，获取错误消息
				String errorMessage = responseMessage.getErrorResponseAsString(); 
				
				// 当前方法返回失败
				return ResultJsonToHtmlUtil.failedWithErrorMessage("当前响应码为：【"+statusCode+"】  错误消息为：【"+errorMessage+"】");
			}
		} catch (Exception e) {
			e.printStackTrace();
			
			return ResultJsonToHtmlUtil.failedWithErrorMessage(e.getMessage());
		
		} finally {
			
			if (ossClient!=null) {
				
				// 关闭 OSSClient。
				ossClient.shutdown();
				
			}
		}
	}
	
	
	public static void main(String[] args) throws Exception {
		
		String picture="rose.jpg";
		
		InputStream inputStream=new FileInputStream("picture/"+picture);
		
		ResultJsonToHtmlUtil<String> uploadFile = AliyunOSSUtil.uploadFileToAliyunOSS("oss-cn-shenzhen.aliyuncs.com", "LTAI4Fu7nahAzqKghdFhtJZk",
												"OrL1bLz1vXlieNr2oZPvrBthRopBx8", "com-hys", "com-hys.oss-cn-shenzhen.aliyuncs.com",
												picture, inputStream);
		
		Logger logger=LoggerFactory.getLogger(AliyunOSSUtil.class);
		
		logger.info("uploadFile="+uploadFile);
	}
	
	
}
