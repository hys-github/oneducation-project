package com.hys.videoService.controller;

import com.hys.utils.ResultJsonToHtmlUtil;
import com.hys.videoService.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Auth 86191
 * @Date 2020/4/19
 */
@Api(description = "视频上传下载删除的controller")
@RestController
@RequestMapping("/video/crud")
@CrossOrigin
public class VideoController {

    @Autowired
    VideoService videoService;


    /**
     * @param file  上传上来的视频文件
     * @return  成功上传，返回上传成功视频的唯一id(在阿里云上)；失败，返回异常信息
     *
     *     上传视频到阿里云上
     */
    @ApiOperation(value = "上传视频到阿里云上")
    @PostMapping("/upload/video")
    public ResultJsonToHtmlUtil<String> uploadVideoToAliyun(
            @ApiParam(name="file",value = "上传的视频",required = true) MultipartFile file){
        try {
            String videoId = videoService.uploadVideoToAliyun(file);

            if (videoId==null){
                return ResultJsonToHtmlUtil.failedWithErrorMessage("上传失败");
            }

            return ResultJsonToHtmlUtil.successWithData(videoId);
        }catch (Exception e){
            e.printStackTrace();
            return ResultJsonToHtmlUtil.failedWithErrorMessage(e.getMessage());
        }
    }


    /**
     * @param videoId   通过阿里云上的唯一凭证id删除视频
     * @return  成功，返回不带数据的json；失败，返回异常信息
     *
     *      删除视频(一个一个删除）
     */
    @ApiOperation(value = "通过阿里云上的唯一凭证id删除视频")
    @DeleteMapping("/delete/video/to/aliyun/by/videoId/{videoId}")
    public ResultJsonToHtmlUtil<String> deleteVideoToAliyunByVideoId(
            @ApiParam(name = "videoId",value = "删除视频的唯一凭证id",required = true)@PathVariable("videoId") String videoId){
        try {

            videoService.deleteVideoToAliyunByVideoId(videoId);

            return ResultJsonToHtmlUtil.successWithOutOfData();
        }catch (Exception e){
            e.printStackTrace();
            return ResultJsonToHtmlUtil.failedWithErrorMessage(e.getMessage());
        }
    }


    /**
     * @param videoIdList   上传上来的阿里云上的多个视频的唯一凭证id
     * @return
     */
    @ApiOperation(value = "通过阿里云上的唯一凭证id批量删除视频")
    @DeleteMapping("/delete/batch/video")
    public ResultJsonToHtmlUtil<String> deleteBatchVideoToAliyunByVideoIds(
            @ApiParam(name = "videoIdList",value = "删除视频的唯一凭证id",required = true)@RequestParam("videoIdList") List<String> videoIdList){
        try {

            videoService.deleteBatchVideoToAliyunByVideoIds(videoIdList);

            return ResultJsonToHtmlUtil.successWithOutOfData();
        }catch (Exception e){
            e.printStackTrace();
            return ResultJsonToHtmlUtil.failedWithErrorMessage(e.getMessage());
        }
    }

}
