package com.hys.eduService.controller.daemon;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hys.eduService.entity.PO.EduCourse;
import com.hys.eduService.entity.VO.CourseInfoVO;
import com.hys.eduService.entity.VO.CoursePageVO;
import com.hys.eduService.entity.VO.CoursePublishVO;
import com.hys.eduService.entity.VO.TeacherPageVO;
import com.hys.eduService.service.EduCourseService;
import com.hys.utils.ResultJsonToHtmlUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author hys
 * @since 2020-04-16
 */
@RestController
@RequestMapping("/eduService/course")
@CrossOrigin
@Slf4j
@Api(description = "课程信息的controller")
public class EduCourseController {

    @Autowired
    EduCourseService eduCourseService;

    /**
     * @param courseInfoVO  得到从页面上传过来的添加的课程信息
     * @return  成功，返回封装了课程信息的主键id；失败，返回异常信息
     *
     *         当上传上来的课程信息对象CourseInfoVO这个中拥有课程主键id时，
     *              则这个前端执行的操作为修改课程信息的操作
     *         当上传上来的课程信息对象CourseInfoVO没有课程主键id时，
     *              则为新添加课程操作
     *          在impl中通过课程主键id判断是否为添加还是修改操作
     */
    @ApiOperation(value = "通过课程id是否为空判断是添加还是修改操作")
    @PostMapping("/add/or/update/course/info")
    public ResultJsonToHtmlUtil<String> addOrUpdateCourseInfo(
            @ApiParam(name = "courseInfoVO",value = "添加或者修改课程基本信息",required = true) @RequestBody CourseInfoVO courseInfoVO){

        try {
            String courseId=eduCourseService.addOrUpdateCourseInfoToMysql(courseInfoVO);

            return ResultJsonToHtmlUtil.successWithData(courseId);
        }catch (Exception e){

            e.printStackTrace();

            if(e instanceof DuplicateKeyException){
                return ResultJsonToHtmlUtil.failedWithErrorMessage("课程名已在使用，请重新输入课程名");
            }

           return ResultJsonToHtmlUtil.failedWithErrorMessage(e.getMessage());

        }
    }


    /**
     * @param courseId  当点击上一步时，将课程id发送到后台得到回显的课程信息数据
     * @return  成功，回显课程信息的数据；失败，返回异常信息
     */
    @ApiOperation(value = "通过课程id得到课程信息")
    @GetMapping("get/return/course/info/to/form/{courseId}")
    public ResultJsonToHtmlUtil<CourseInfoVO> getReturnDataToForm(
            @ApiParam(name = "courseId",value = "参数为课程的主键id",required = true) @PathVariable("courseId") String courseId){
        try {

            CourseInfoVO courseInfoVO=eduCourseService.getReturnDataFromMysql(courseId);

            return ResultJsonToHtmlUtil.successWithData(courseInfoVO);
        }catch (Exception e){
            e.printStackTrace();

            return ResultJsonToHtmlUtil.failedWithErrorMessage(e.getMessage());
        }


    }


    @ApiOperation(value = "通过课程主键id的得到课程将要发布的详细信息展示到页面上")
    @GetMapping("/get/course/publish//to/html/{courseId}")
    public ResultJsonToHtmlUtil<CoursePublishVO> getCoursePublishVOToHtml(
           @ApiParam(name = "courseId",value = "课程主键id",required = true) @PathVariable("courseId") String courseId){
        try {

            CoursePublishVO coursePublishVO = eduCourseService.getCoursePublishVOByCourseId(courseId);

            return ResultJsonToHtmlUtil.successWithData(coursePublishVO);
        }catch (Exception e){
            e.printStackTrace();
            return ResultJsonToHtmlUtil.failedWithErrorMessage(e.getMessage());
        }
    }


    /**
     * @param courseId  课程的主键id
     * @return  成功，返回不带数据的json；失败，返回异常信息
     *      发布课程时，后台执行的方法
     */
    @ApiOperation(value = "发布课程")
    @GetMapping("/publish/course/{courseId}")
    public ResultJsonToHtmlUtil<String> PublishCourseToHtml(
            @ApiParam(name = "courseId",value = "通过课程id将该课程的状态改为发布")@PathVariable("courseId") String courseId){
        try {

            EduCourse eduCourse = new EduCourse();
            eduCourse.setStatus("Normal");
            eduCourse.setId(courseId);
            eduCourseService.updateById(eduCourse);

            return ResultJsonToHtmlUtil.successWithOutOfData();
        }catch (Exception e){
            e.printStackTrace();
            return ResultJsonToHtmlUtil.failedWithErrorMessage(e.getMessage());
        }
    }


    /**
     * @param pageNum   查询课程第几页
     * @param pageSize  查询课程每页的最大数量
     * @param coursePageVO  上传上来查询课程的条件
     * @return  成功，返回课程分页的课程信息集合；失败，返回异常信息
     *
     *         课程分页操作
     */
    @ApiOperation(value ="查询课程列表分页" )
    @PostMapping("/query/course/page/{pageNum}/{pageSize}")
    public ResultJsonToHtmlUtil<Map<String,Object>> getCoursePage(
            @ApiParam(name = "pageNum",value = "查询第几页",required = true)@PathVariable("pageNum")Integer pageNum,
            @ApiParam(name="pageSize",value = "每页的条数",required = true)@PathVariable("pageSize") Integer pageSize,
           @ApiParam(name = "coursePageVO",value = "上传上来的查询条件封装类")@RequestBody(required = false) CoursePageVO coursePageVO){
        try {

            // 分页信息封装
            Page<EduCourse> eduCoursePage = new Page<>(pageNum, pageSize);

            // 封装查询条件
            QueryWrapper<EduCourse> eduCourseQueryWrapper = new QueryWrapper<>();

            // 页面上的查询条件
            String title = coursePageVO.getTitle();
            String status = coursePageVO.getStatus();

            // 判断是否有上传的查询条件
            if(!StringUtils.isEmpty(title)){
                eduCourseQueryWrapper.like("title",title);
            }
            if(!StringUtils.isEmpty(status)){

                if("未发布".equals(status)){
                    status="Draft";
                }else{
                    status="Normal";
                }
                eduCourseQueryWrapper.eq("status",status);
            }

            // 使课程根据创造时间排列
            eduCourseQueryWrapper.orderByDesc("create_time");

            IPage<EduCourse> courseIPage = eduCourseService.page(eduCoursePage, eduCourseQueryWrapper);

            Map<String,Object> map=new HashMap<>();

            List<EduCourse> eduCourseList = courseIPage.getRecords();
            for (EduCourse eduCourse:eduCourseList){
                String status1 = eduCourse.getStatus();
                if("Draft".equals(status1)){
                    eduCourse.setStatus("未发布");
                }else {
                    eduCourse.setStatus("已发布");
                }
            }
            // 将课程的中条数发送到页面上
            map.put("totalNum",courseIPage.getTotal());
            // 得到查询到的课程信息集合
            map.put("eduCourseList",eduCourseList);

            return ResultJsonToHtmlUtil.successWithData(map);
        }catch (Exception e){
            e.printStackTrace();
            return ResultJsonToHtmlUtil.failedWithErrorMessage(e.getMessage());
        }
    }


    /**
     * @param courseId  课程主键id
     * @return  成功，返回不带数据的json；失败，返回异常信息
     *
     *         通过课程的主键id删除该课程的所有信息，包括里面的章节和视频等
     */
    @ApiOperation(value = "通过课程主键id删除课程中的所有信息")
    @DeleteMapping("/delete/course/by/courseId/{courseId}")
    public ResultJsonToHtmlUtil<String> deleteCourseByCourseId(
            @ApiParam(name = "courseId",value = "课程主键id",required = true)@PathVariable("courseId") String courseId){
        try {
            ResultJsonToHtmlUtil<String> resultJsonToHtmlUtil = eduCourseService.deleteCourseByCourseId(courseId);

            return resultJsonToHtmlUtil;
        }catch (Exception e){
            e.printStackTrace();
            return ResultJsonToHtmlUtil.failedWithErrorMessage(e.getMessage());
        }
    }



}

