package com.hys.eduService.entity.VO;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * @Auth 86191
 * @Date 2020/4/10
 *      查询教师分页的面向页面的对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherPageVO implements Serializable {

    // 教师的姓名，采用模糊查询
    @ApiParam(name = "name",value = "教师姓名")
    private String name;

    // 教师的级别
    @ApiParam(name = "level",value = "教师级别")
    private Integer level;

    // 教师开始教书时间
    @ApiParam(name = "beginTime",value = "教师教书开始时间")
    private String beginTime;

    // 教师结束时间
    @ApiParam(name = "endTime",value="教师结束时间")
    private String endTime;


}
