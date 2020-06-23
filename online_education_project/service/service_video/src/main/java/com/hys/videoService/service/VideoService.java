package com.hys.videoService.service;

import com.aliyuncs.exceptions.ClientException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Auth 86191
 * @Date 2020/4/19
 */
public interface VideoService {
    String uploadVideoToAliyun(MultipartFile file);

    void deleteVideoToAliyunByVideoId(String videoId) throws Exception;

    void deleteBatchVideoToAliyunByVideoIds(List<String> videoIdList) throws Exception;
}
