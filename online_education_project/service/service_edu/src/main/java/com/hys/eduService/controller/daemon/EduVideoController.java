package com.hys.eduService.controller.daemon;


import com.hys.eduService.entity.PO.EduChapter;
import com.hys.eduService.entity.PO.EduVideo;
import com.hys.eduService.entity.VO.VideoInfoVO;
import com.hys.eduService.service.EduVideoService;
import com.hys.utils.ResultJsonToHtmlUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author hys
 * @since 2020-04-16
 */
@Api(description = "课程视频和每章的节信息的controller")
@RestController
@RequestMapping("/eduService/video")
@CrossOrigin
@Slf4j
public class EduVideoController {

    @Autowired
    EduVideoService eduVideoService;


    /**
     * @param eduVideo  每一章下的节信息
     * @return  成功，返回不带数据的json；失败，返回异常信息
     *      添加节信息
     */
    @ApiOperation(value = "添加每一章节下的节信息")
    @PostMapping("/add/video")
    public ResultJsonToHtmlUtil<String> addVideo(
            @ApiParam(name = "eduVideo",value = "添加每一章下的节信息",required = true)@RequestBody EduVideo eduVideo){
        try {
            eduVideoService.save(eduVideo);

            return ResultJsonToHtmlUtil.successWithOutOfData();
        }catch (Exception e){
            e.printStackTrace();

            if (e instanceof DuplicateKeyException){
                return ResultJsonToHtmlUtil.failedWithErrorMessage("节信息已在使用中，请重新输入节信息");
            }

            return ResultJsonToHtmlUtil.failedWithErrorMessage(e.getMessage());
        }
    }


    /**
     * @param videoId 节的主键id
     * @return      成功，返回带节的内容的json数据；失败，返回异常信息；
     *         通过节的主键id得到章的内容
     */
    @ApiOperation(value = "通过节主键id得到节的信息")
    @GetMapping("/get/video/by/video/id/{videoId}")
    public ResultJsonToHtmlUtil<VideoInfoVO> getChapterByChapterId(
            @ApiParam(name="videoId",value = "参数为节的id",required = true) @PathVariable("videoId") String videoId){
        try {

            EduVideo eduVideo = eduVideoService.getById(videoId);
            VideoInfoVO videoInfoVO = new VideoInfoVO();
            BeanUtils.copyProperties(eduVideo,videoInfoVO);

            return ResultJsonToHtmlUtil.successWithData(videoInfoVO);
        }catch (Exception e){
            e.printStackTrace();
            return ResultJsonToHtmlUtil.failedWithErrorMessage(e.getMessage());
        }
    }


    /**
     * @param eduVideo   上传要修改的节的内容
     * @return  成功，返回不带数据的json；失败，返回异常信息
     *      修改节的信息操作
     */
    @ApiOperation(value = "修改每一章下的节的信息")
    @PostMapping("/update/video")
    public ResultJsonToHtmlUtil<String> updateChapter(
            @ApiParam(name="eduVideo",value="要修改的节的内容",required = true)@RequestBody EduVideo eduVideo){
        try {
            eduVideoService.updateById(eduVideo);

            return ResultJsonToHtmlUtil.successWithOutOfData();
        }catch (Exception e){
            e.printStackTrace();

            if(e instanceof DuplicateKeyException){
                return ResultJsonToHtmlUtil.failedWithErrorMessage("节信息已在使用中，请重新输入节信息");
            }

            return ResultJsonToHtmlUtil.failedWithErrorMessage(e.getMessage());
        }
    }


    /**
     * @param videoId 节的主键id
     * @return  成功，返回不带数据的json；失败，返回异常信息
     *      删除节的操作
     */
    @ApiOperation(value = "通过每一章的节的主键id删除节的信息")
    @DeleteMapping("/delete/video/{videoId}")
    public ResultJsonToHtmlUtil<String> deleteChapter(
            @ApiParam(name = "videoId",value = "节的主键id",required = true)@PathVariable("videoId") String videoId){
        try {
            // 删除节的信息
            ResultJsonToHtmlUtil<String> resultJsonToHtmlUtil = eduVideoService.deleteVideoByVideoId(videoId);

            return resultJsonToHtmlUtil;
        }catch (Exception e){
            e.printStackTrace();
            return ResultJsonToHtmlUtil.failedWithErrorMessage(e.getMessage());
        }
    }




}

