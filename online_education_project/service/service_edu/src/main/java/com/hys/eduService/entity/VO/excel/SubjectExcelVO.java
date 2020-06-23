package com.hys.eduService.entity.VO.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Auth 86191
 * @Date 2020/4/15
 *       课程分类，封装上传上来的excel表格中的数据
 *       面向视图的实体类
 *          其中第二级课程是第一级的子类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "SubjectExcelVO对象",description = "接受课程分类的视图对象")
public class SubjectExcelVO implements Serializable {

    @ApiModelProperty(value = "第一级课程名")
    @ExcelProperty(value = "第一级课程名称",index = 0)
    private String oneSubjectName;

    @ApiModelProperty(value = "第二级课程名")
    @ExcelProperty(value = "第二级课程名",index = 1)
    private String twoSubjectName;


}
