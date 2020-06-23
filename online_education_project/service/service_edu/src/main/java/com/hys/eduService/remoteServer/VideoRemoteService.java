package com.hys.eduService.remoteServer;

import com.hys.utils.ResultJsonToHtmlUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Auth 86191
 * @Date 2020/4/20
 */
@FeignClient(name = "service-video-8003",fallback = VideoRemoteFallback.class)
@Component
public interface VideoRemoteService {

    /**
     * @param videoId   通过阿里云上的唯一凭证id删除视频
     * @return  成功，返回不带数据的json；失败，返回异常信息
     *
     *      删除视频
     */
    @ApiOperation(value = "通过阿里云上的唯一凭证id删除视频")
    @DeleteMapping("/video/crud/delete/video/to/aliyun/by/videoId/{videoId}")
    public ResultJsonToHtmlUtil<String> deleteVideoToAliyunByVideoId(
            @ApiParam(name = "videoId",value = "删除视频的唯一凭证id",required = true)@PathVariable("videoId") String videoId);

    /**
     * @param videoIdList   上传上来的阿里云上的多个视频的唯一凭证id
     * @return
     */
    @ApiOperation(value = "通过阿里云上的唯一凭证id批量删除视频")
    @DeleteMapping("/video/crud/delete/batch/video")
    public ResultJsonToHtmlUtil<String> deleteBatchVideoToAliyunByVideoIds(
            @ApiParam(name = "videoIdList",value = "删除视频的唯一凭证id",required = true)@RequestParam("videoIdList") List<String> videoIdList);
}
