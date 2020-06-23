package com.hys.videoService.service.Impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadFileStreamRequest;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.req.UploadVideoRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyun.vod.upload.resp.UploadVideoResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.DeleteVideoResponse;
import com.hys.videoService.config.InitAliyunVideoClient;
import com.hys.videoService.config.VideoProperties;
import com.hys.videoService.service.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @Auth 86191
 * @Date 2020/4/19
 */
@Service
@Slf4j
public class VideoServiceImpl implements VideoService {

    @Autowired
    VideoProperties videoProperties;

    /**
     * @param file  上传上来的视频文件
     * @return  成功上传，返回上传成功视频的唯一id(在阿里云上)；失败，返回异常信息
     *
     *     上传视频到阿里云上
     */
    @Override
    public String uploadVideoToAliyun(MultipartFile file) {

        String title = UUID.randomUUID().toString().substring(0,6).replaceAll("-","");   //上传之后文件名称
        String fileName = file.getOriginalFilename();  //文件名称
        String accessKeyId = videoProperties.getAccessKeyId();
        String accessKeySecret = videoProperties.getAccessKeySecret();
        InputStream inputStream =null;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*
        上传视频的方法,本地文件流上传
        UploadVideoRequest request = new UploadVideoRequest(videoProperties.getAccessKeyId(),
                                              videoProperties.getAccessKeySecret(), title, fileName);
         可指定分片上传时每个分片的大小，默认为2M字节
        request.setPartSize(2 * 1024 * 1024L);
       可指定分片上传时的并发线程数，默认为1，(注：该配置会占用服务器CPU资源，需根据服务器情况指定）
        request.setTaskNum(1);
        */

        // 使用的是流式上传接口
        UploadStreamRequest request = new UploadStreamRequest(accessKeyId, accessKeySecret, title, fileName, inputStream);

        // 上传视频实现类
        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadStreamResponse response = uploader.uploadStream(request);

        // 请求视频点播服务的请求ID
        String videoId = response.getVideoId();

        if (response.isSuccess()) {
            log.info("video==="+videoId);
        } else {
            // 如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。
            // 其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
           log.info("errorCode==="+response.getCode());
           log.info("message==="+response.getMessage());
        }
        // 上传失败，则返回null
        return videoId;
    }


    /**
     * @param videoId   通过阿里云上的唯一凭证id删除视频
     *
     *      删除视频
     */
    @Override
    public void deleteVideoToAliyunByVideoId(String videoId) throws Exception {

        DeleteVideoRequest request = new DeleteVideoRequest();
        // 参数：删除视频的id，可以传多个，且以逗号隔开
        request.setVideoIds(videoId);

        // 得到上传或者删除的固定客服端
        DefaultAcsClient client = InitAliyunVideoClient.initVodClient(videoProperties.getAccessKeyId(), videoProperties.getAccessKeySecret());

        // 得到删除的回应消息
        DeleteVideoResponse response = client.getAcsResponse(request);

        String requestId = response.getRequestId();

        log.info("requestId==="+requestId);

    }


    /**
     * @param videoIdList   过阿里云上的唯一凭证id批量删除视频
     *
     *            批量删除视频
     */
    @Override
    public void deleteBatchVideoToAliyunByVideoIds(List<String> videoIdList) throws Exception {
        // 得到上传的删除亲求
        DeleteVideoRequest request=new DeleteVideoRequest();

        // org.apache.commons.lang这个包下的StringUtils，将数组以‘,’隔开未一个字符串
        String videoIds = StringUtils.join(videoIdList.toArray(), ",");
        log.info(videoIds);

        // 设置视频唯一凭证,多个视频id，以逗号隔开
        request.setVideoIds(videoIds);

        DefaultAcsClient client = InitAliyunVideoClient.initVodClient(videoProperties.getAccessKeyId(), videoProperties.getAccessKeySecret());

        DeleteVideoResponse response = client.getAcsResponse(request);

        String requestId = response.getRequestId();

        log.info("requestId==="+requestId);
    }


    public static void main(String[] args) {

        List<String> list=new ArrayList<>();

        list.add("lll");
        list.add("222");
        list.add("333");

        String join = StringUtils.join(list.toArray(), ",");
        System.out.println(join);

        log.info(join);

    }

}
