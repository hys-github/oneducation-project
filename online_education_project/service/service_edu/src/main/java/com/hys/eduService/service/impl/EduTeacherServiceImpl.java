package com.hys.eduService.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hys.eduService.entity.PO.EduCourse;
import com.hys.eduService.entity.PO.EduTeacher;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hys.eduService.mapper.EduTeacherMapper;
import com.hys.eduService.service.EduCourseService;
import com.hys.eduService.service.EduTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hys
 * @since 2020-04-09
 */
@Service
public class EduTeacherServiceImpl extends ServiceImpl<EduTeacherMapper, EduTeacher> implements EduTeacherService {

    @Autowired
    EduCourseService eduCourseService;

    /**
     * @return  成功，返回查询的教师和课程的信息集合；失败，控制器抓取异常，并回滚
     */
    @Override
    @Cacheable(value = "courseAndTeacherList",key = "'#root.method.name'")
    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    public Map<String, Object> getHotTeacherAndCourseInfo() {

        // 查询热度老师，通过id的降序查询
        QueryWrapper<EduTeacher> teacherQueryWrapper = new QueryWrapper<>();
        // 通过id的降序查询
        teacherQueryWrapper.orderByDesc("id");
        // 限制查询的讲师数量
        teacherQueryWrapper.last("limit 4");
        // 查询讲师集合
        List<EduTeacher> teacherList = this.list(teacherQueryWrapper);

        // 查询热度课程，通过id的降序查询
        QueryWrapper<EduCourse> courseQueryWrapper = new QueryWrapper<>();
        // 通过id的降序查询
        courseQueryWrapper.orderByDesc("id");
        // 限制查询的课程数量
        courseQueryWrapper.last("limit 8");
        // 查询课程集合
        List<EduCourse> courseList = eduCourseService.list(courseQueryWrapper);

        Map<String,Object> map=new HashMap<>();
        map.put("teacherList",teacherList);
        map.put("courseList",courseList);

        return map;
    }
}
