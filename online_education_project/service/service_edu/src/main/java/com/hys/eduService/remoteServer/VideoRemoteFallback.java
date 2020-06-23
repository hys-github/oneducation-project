package com.hys.eduService.remoteServer;

import com.hys.utils.ResultJsonToHtmlUtil;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Auth 86191
 * @Date 2020/4/20
 */
@Component
public class VideoRemoteFallback implements VideoRemoteService {
    @Override
    public ResultJsonToHtmlUtil<String> deleteVideoToAliyunByVideoId(String videoId) {
        return ResultJsonToHtmlUtil.failedWithErrorMessage("服务器宕机或者超时，删除视频失败");
    }

    @Override
    public ResultJsonToHtmlUtil<String> deleteBatchVideoToAliyunByVideoIds(List<String> videoIdList) {
        return ResultJsonToHtmlUtil.failedWithErrorMessage("服务器宕机或者超时，批量删除视频失败");
    }
}
