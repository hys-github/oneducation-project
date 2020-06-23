package com.hys.eduService.service;

import com.hys.eduService.entity.PO.EduCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hys.eduService.entity.VO.CourseInfoVO;
import com.hys.eduService.entity.VO.CoursePublishVO;
import com.hys.utils.ResultJsonToHtmlUtil;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author hys
 * @since 2020-04-16
 */
public interface EduCourseService extends IService<EduCourse> {

    String addOrUpdateCourseInfoToMysql(CourseInfoVO courseInfoVO);

    CourseInfoVO getReturnDataFromMysql(String courseId);

    CoursePublishVO getCoursePublishVOByCourseId(String courseId);

    ResultJsonToHtmlUtil<String> deleteCourseByCourseId(String courseId);
}
