package com.hys.eduService.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hys.eduService.entity.PO.EduTeacher;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hys
 * @since 2020-04-09
 */
public interface EduTeacherService extends IService<EduTeacher> {

    Map<String, Object> getHotTeacherAndCourseInfo();
}
