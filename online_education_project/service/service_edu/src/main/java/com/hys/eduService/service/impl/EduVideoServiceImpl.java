package com.hys.eduService.service.impl;

import com.hys.eduService.entity.PO.EduVideo;
import com.hys.eduService.mapper.EduVideoMapper;
import com.hys.eduService.remoteServer.VideoRemoteService;
import com.hys.eduService.service.EduVideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hys.utils.ResultJsonToHtmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author hys
 * @since 2020-04-16
 */
@Service
@Slf4j
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {

    @Autowired
    VideoRemoteService videoRemoteService;

    /**
     * @param videoId 节的主键id
     *
     *      删除节的操作,并通过节的id得到该节下的上传到阿里云上的唯一视频id
     *                通过视频凭证id调用远端服务器删除阿里云上的视频
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    public ResultJsonToHtmlUtil<String> deleteVideoByVideoId(String videoId) {

        EduVideo eduVideo = this.getById(videoId);

        // 得到上传到阿里云上的唯一视频凭证id
        String videoSourceId = eduVideo.getVideoSourceId();

        // 调用远端服务器删除视频
        if(videoSourceId!=null){
            // 调用远端服务器接口
            ResultJsonToHtmlUtil<String> resultUtil = videoRemoteService.deleteVideoToAliyunByVideoId(videoSourceId);
            if(!resultUtil.isResult()){
                log.info(resultUtil.getErrorMessage());

                return resultUtil;
            }

        }
        // 删除节信息
        this.removeById(videoId);

        return ResultJsonToHtmlUtil.successWithOutOfData();
    }
}
