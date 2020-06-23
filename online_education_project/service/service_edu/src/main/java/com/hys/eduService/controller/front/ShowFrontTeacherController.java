package com.hys.eduService.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hys.eduService.entity.PO.EduCourse;
import com.hys.eduService.entity.PO.EduTeacher;
import com.hys.eduService.entity.VO.TeacherPageVO;
import com.hys.eduService.service.EduCourseService;
import com.hys.eduService.service.EduTeacherService;
import com.hys.utils.ResultJsonToHtmlUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auth 86191
 * @Date 2020/4/23
 */
@Api(description = "在前端展示讲师相关的controller")
@CrossOrigin
@RestController
@RequestMapping("/eduService/front/teacher")
@Slf4j
public class ShowFrontTeacherController {

    @Autowired
    EduTeacherService eduTeacherService;

    @Autowired
    EduCourseService eduCourseService;


    /**
     * @param pageNum   第几页
     * @param pageSize  每页最大条数
     * @param teacherPageVO     根据条件查询讲师分页信息，可选
     * @return  成功，返回分页查询到的讲师信息；失败，返回异常信息
     *
     *      分页查询讲师并展示到页面上
     */
    @ApiOperation(value = "分页查询教师展示到页面上")
    @PostMapping("/get/teacher/{pageNum}/{pageSize}")
    public ResultJsonToHtmlUtil<Map<String,Object>> getTeacherListToFrontPage(
            @ApiParam(name="pageNum",value = "第几页",required = true)@PathVariable("pageNum") Integer pageNum,
            @ApiParam(name="pageSize",value = "每页最大条数",required = true)@PathVariable("pageSize") Integer pageSize,
            @ApiParam(name = "teacherPageVO",value = "查询讲师的条件,可选参数")@RequestBody(required = false) TeacherPageVO teacherPageVO){
        try {
            // 将分页信息封装到Page中
            Page<EduTeacher> page = new Page<>(pageNum,pageSize);

            // 根据条件查询分页信息
            QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();

            //TODO,条件查询讲师信息

            // 执行分页查询，返回的值直接就封装到page中了
            eduTeacherService.page(page,wrapper);

            List<EduTeacher> teacherList = page.getRecords();
            long total = page.getTotal();
            long pages = page.getPages();
            long size = page.getSize();
            long current = page.getCurrent();
            // 是否有下一页
            boolean hasNext = page.hasNext();
            // 是否有上一页
            boolean hasPrevious = page.hasPrevious();

            // 将从数据库中查询到的信息封装到map中
            Map<String,Object> map=new HashMap<>();
            map.put("teacherList",teacherList);
            map.put("total",total);
            map.put("pages",pages);
            map.put("size",size);
            map.put("current",current);
            map.put("hasNext",hasNext);
            map.put("hasPrevious",hasPrevious);

            return ResultJsonToHtmlUtil.successWithData(map);
        }catch (Exception e){
            e.printStackTrace();
            return ResultJsonToHtmlUtil.failedWithErrorMessage(e.getMessage());
        }
    }


    /**
     * @param id    讲师的id
     * @return  成功，返回讲师的详细信息和其要讲的课程等信息
     *
     *      得到讲师的详细信息和所讲的课程信息
     */
    @GetMapping("/get/detail/teacher/and/course/{id}")
    @ApiOperation(value = "通过讲师的id得到讲师详情和其所讲的课程信息")
    public ResultJsonToHtmlUtil<Map<String,Object>> getDetailTeacherAndCourse(
            @ApiParam(name="id",value = "讲师的id",required = true)@PathVariable("id") String id){
        try {
            // 通过讲师的id得到讲师的信息
            EduTeacher teacher = eduTeacherService.getById(id);

            // 通过讲师的id得到讲师要讲课程的信息
            QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
            wrapper.eq("teacher_id",id);
            List<EduCourse> courseList = eduCourseService.list(wrapper);

            Map<String,Object> map=new HashMap<>();
            map.put("teacher",teacher);
            map.put("courseList",courseList);

            return ResultJsonToHtmlUtil.successWithData(map);
        }catch (Exception e){
            e.printStackTrace();
            return ResultJsonToHtmlUtil.failedWithErrorMessage(e.getMessage());
        }
    }



}
