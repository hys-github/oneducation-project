package com.hys.eduService.mapper;

import com.hys.eduService.entity.PO.EduCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hys.eduService.entity.VO.CoursePublishVO;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author hys
 * @since 2020-04-16
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {

    // 查询课程的发布信息的封装类
    CoursePublishVO getCoursePublishVOByCourseId(String courseId);
}
