package com.hys.eduService.controller.daemon;


import com.hys.eduService.entity.PO.EduChapter;
import com.hys.eduService.entity.VO.ChapterVO;
import com.hys.eduService.service.EduChapterService;
import com.hys.utils.ResultJsonToHtmlUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javafx.scene.CacheHint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author hys
 * @since 2020-04-16
 *      章节控制器
 */
@RestController
@RequestMapping("/eduService/chapter")
@CrossOrigin
@Slf4j
@Api(description = "课程下每一章显示controller")
public class EduChapterController {

    @Autowired
    EduChapterService eduChapterService;

    /**
     * @param courseId  传过来的课程id
     * @return  失败，返回异常信息；成功，返回查询到的课程章节信息
     *      通过课程id得到课程章节的集合
     */
    @ApiOperation(value = "通过课程id得到课程章节的集合")
    @GetMapping("/query/chapter/{courseId}")
    public ResultJsonToHtmlUtil<Collection<ChapterVO>> queryChapterAndVideo(
            @ApiParam(name = "courseId",value = "参数为课程的主键id",required = true) @PathVariable(value = "courseId") String courseId){
        try {
            ResultJsonToHtmlUtil<Collection<ChapterVO>> chapterVOs = eduChapterService.queryChapterAndVideo(courseId);

            return chapterVOs;
        }catch (Exception e){
            e.printStackTrace();
            return ResultJsonToHtmlUtil.failedWithErrorMessage(e.getMessage());
        }
    }


    /**
     * @param eduChapter    上传上来的章内容
     * @return      成功，返回不带数据的json；失败，返回异常信息
     *          添加课程章节信息
     */
    @ApiOperation(value ="添加课程章的信息" )
    @PostMapping("/add/chapter")
    public ResultJsonToHtmlUtil<String> addChapter(
            @ApiParam(name="eduChapter",value="添加章节信息的实体类",required = true)@RequestBody EduChapter eduChapter){
        try {
            // 将前端上传过来的章节信息添加到数据库中
            boolean flag = eduChapterService.save(eduChapter);

            return ResultJsonToHtmlUtil.successWithOutOfData();
        }catch (Exception e){
            e.printStackTrace();

            if(e instanceof DuplicateKeyException){
                return ResultJsonToHtmlUtil.failedWithErrorMessage("章节名已在使用，请重新输入章节名");
            }

            return ResultJsonToHtmlUtil.failedWithErrorMessage(e.getMessage());
        }


    }


    /**
     * @param chapterId 章的主键id
     * @return      成功，返回带章的内容的json数据；失败，返回异常信息；
     *         通过章的主键id得到章的内容
     */
    @ApiOperation(value = "通过章主键id得到章的信息")
    @GetMapping("/get/chapter/by/chapter/id/{chapterId}")
    public ResultJsonToHtmlUtil<EduChapter> getChapterByChapterId(
            @ApiParam(name="chapterId",value = "参数为章节的id",required = true) @PathVariable("chapterId") String chapterId){
        try {

            EduChapter eduChapter = eduChapterService.getById(chapterId);

            return ResultJsonToHtmlUtil.successWithData(eduChapter);
        }catch (Exception e){
            e.printStackTrace();
            return ResultJsonToHtmlUtil.failedWithErrorMessage(e.getMessage());
        }
    }


    /**
     * @param eduChapter   上传要修改的章的内容
     * @return  成功，返回不带数据的json；失败，返回异常信息
     *      修改章的信息操作
     */
    @ApiOperation(value = "修改章的信息")
    @PostMapping("/update/chapter")
    public ResultJsonToHtmlUtil<String> updateChapter(
            @ApiParam(name="eduChapter",value="要修改的章的内容",required = true)@RequestBody EduChapter eduChapter){
        try {
            eduChapterService.updateById(eduChapter);

            return ResultJsonToHtmlUtil.successWithOutOfData();
        }catch (Exception e){
            e.printStackTrace();
            return ResultJsonToHtmlUtil.failedWithErrorMessage(e.getMessage());
        }
    }


    /**
     * @param chapterId 章的主键id
     * @return  成功，返回不带数据的json；失败，返回异常信息
     *      删除章的操作
     */
    @ApiOperation(value = "通过章的主键id删除章的信息")
    @DeleteMapping("/delete/chapter/{chapterId}")
    public ResultJsonToHtmlUtil<String> deleteChapter(
            @ApiParam(name = "chapterId",value = "章的主键id",required = true)@PathVariable("chapterId") String chapterId){
        try {
            // 删除章的信息
            ResultJsonToHtmlUtil<String> resultJsonToHtmlUtil = eduChapterService.deleteChapterByChapterId(chapterId);

            return resultJsonToHtmlUtil;
        }catch (Exception e){
            e.printStackTrace();
            return ResultJsonToHtmlUtil.failedWithErrorMessage(e.getMessage());
        }
    }



}

