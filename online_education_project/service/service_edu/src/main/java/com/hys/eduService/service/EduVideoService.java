package com.hys.eduService.service;

import com.hys.eduService.entity.PO.EduVideo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hys.utils.ResultJsonToHtmlUtil;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author hys
 * @since 2020-04-16
 */
public interface EduVideoService extends IService<EduVideo> {

    ResultJsonToHtmlUtil<String> deleteVideoByVideoId(String videoId);
}
