package com.hys.eduService.entity.VO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Auth 86191
 * @Date 2020/4/18
 *
 *      封装上传上来的课程查询条件的视图类
 */
@Data
public class CoursePageVO implements Serializable {

    @ApiModelProperty(value = "课程标题")
    private String title;

    @ApiModelProperty(value = "课程状态 Draft未发布  Normal已发布")
    private String status;
}
