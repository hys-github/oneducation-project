package com.hys.ossService.service.Impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.comm.ResponseMessage;
import com.aliyun.oss.model.PutObjectResult;
import com.hys.ossService.config.OSSProperties;
import com.hys.ossService.service.OSSService;
import com.hys.utils.ResultJsonToHtmlUtil;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

/**
 * @Auth 86191
 * @Date 2020/4/13
 */
@Service
public class OSSServiceImpl implements OSSService {


    /**
     * @param multipartFile : 将上传的文件封装的文件类
     * @return  成功，返回上传文件的路径；失败，返回异常信息
     */
    @Override
    public ResultJsonToHtmlUtil<String> uploadFileToOSS(MultipartFile multipartFile) {
        // 得到地域节点
        String endPoint= OSSProperties.END_POIND;

        // 创建的Bucket名
        String bucketName=OSSProperties.BUCKET_NAME;

        // Bucket 域名
        String bucketDomain=OSSProperties.BUCKET_DOMAIN;

        // 操作aliyun上oss的必须钥匙id
        String accessKeyId=OSSProperties.ACCESS_KEY_ID;

        // 钥匙id的密码
        String accessKeySecret=OSSProperties.ACCESS_KEY_SECRET;

        // 创建OSSClient实列
        OSS ossClient=new OSSClientBuilder().build(endPoint,accessKeyId,accessKeySecret);

        try{

            // 创建文件夹，在oss上存储的文件夹：年/月/日/xxx文件
            // 利用joda-time这个jar包获取日期时间
            String folderName= new DateTime().toString("yyyy/MM/dd");

            // 获取唯一文件名，将生成的uuid中的字符串中的“-”替换成空格
            String fileName= UUID.randomUUID().toString().substring(0,8).replaceAll("-","");

            // 获取上传上来的文件名
            String originalFilename = multipartFile.getOriginalFilename();

            // 拼接上传到oss上的文件名
            String ossFileName=folderName+"/"+fileName+"_"+originalFilename;

            // 得到文件上传流
            InputStream inputStream = multipartFile.getInputStream();

            // 使用ossClient上传到oss上,调用 OSS 客户端对象的方法上传文件并获取响应结果数据
            PutObjectResult putObjectResult = ossClient.putObject(bucketName, ossFileName, inputStream);

            // 从响应结果中获取具体响应消息
            ResponseMessage responseMessage = putObjectResult.getResponse();

            // 成功返回null
                           if(responseMessage==null){

                // 成功返回，拼接上传到oss上的文件访问路径
                String ossFileAccessPath=bucketDomain+"/"+ossFileName;

                // 将路径封装到工具类中
                return ResultJsonToHtmlUtil.successWithData(ossFileAccessPath);
            }else{

                int statusCode = responseMessage.getStatusCode();

                // 如果请求没有成功，获取错误消息
                String errorMessage = responseMessage.getErrorResponseAsString();

                // 当前方法返回失败
                return ResultJsonToHtmlUtil.failedWithErrorMessage("当前响应码为：【"+statusCode+"】  错误消息为：【"+errorMessage+"】");
            }

        }catch (Exception e){

            e.printStackTrace();

            return ResultJsonToHtmlUtil.failedWithErrorMessage(e.getMessage());
        }finally {
            if (ossClient!=null){
                ossClient.shutdown();
            }

        }
    }



}
