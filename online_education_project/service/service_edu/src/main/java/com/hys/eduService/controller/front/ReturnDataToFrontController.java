package com.hys.eduService.controller.front;

import com.hys.eduService.entity.PO.EduCourse;
import com.hys.eduService.entity.PO.EduTeacher;
import com.hys.eduService.service.EduCourseService;
import com.hys.eduService.service.EduTeacherService;
import com.hys.utils.ResultJsonToHtmlUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Auth 86191
 * @Date 2020/4/21
 *      前台用户页面
 */
@Api(description = "显示数据到前台用户页面")
@RestController
@RequestMapping("/eduService/front/page")
@CrossOrigin
@Slf4j
public class ReturnDataToFrontController {

    @Autowired
    EduTeacherService eduTeacherService;

    @Autowired
    EduCourseService eduCourseService;


    /**
     * @return  成功，得到讲师和课程的信息集合；失败，返回异常信息
     */
    @ApiOperation(value = "通过id降序查询部分讲师和课程的信息集合")
    @GetMapping("/get/hot/teacher/and/course/info")
    public ResultJsonToHtmlUtil<Map<String,Object>> getHotTeacherAndCourseInfo(){
        try {

            Map<String,Object> teacherAndCourseInfo = eduTeacherService.getHotTeacherAndCourseInfo();

            return ResultJsonToHtmlUtil.successWithData(teacherAndCourseInfo);
        }catch (Exception e){
            e.printStackTrace();
            return ResultJsonToHtmlUtil.failedWithErrorMessage(e.getMessage());
        }
    }

}
