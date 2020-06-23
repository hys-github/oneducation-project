package com.hys.eduService.entity.VO;

import lombok.Data;

/**
 * @Auth 86191
 * @Date 2020/4/18
 */
@Data
public class CoursePublishVO {

    // 课程主键
    private String id;

    // 课程的标题
    private String title;

    // 课程的封面
    private String cover;

    // 课程总共多少课时
    private Integer lessonNum;

    // 课程的一级分类
    private String subjectLevelOne;

    // 课程的二级分类
    private String subjectLevelTwo;

    // 上该课程的讲师昵称
    private String teacherName;

    // 该课程的价格
    private String price;//只用于显示

    // 该课程发布状态（课程状态 Draft未发布  Normal已发布）
    private String status;

}
