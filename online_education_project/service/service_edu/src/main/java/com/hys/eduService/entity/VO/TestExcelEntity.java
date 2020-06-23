package com.hys.eduService.entity.VO;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Auth 86191
 * @Date 2020/4/15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestExcelEntity implements Serializable {

    // value：给每个属性在生成的excel中加上表头信息，属于那一列
    // index：为声明哪一行，从0开始，为第一行
    @ExcelProperty(value = "学生编号",index = 0)
    private Integer id;

    // value：给每个属性在生成的excel中加上表头信息，属于那一列
    // index：为声明哪一行，1，为第二行
    @ExcelProperty(value = "学生姓名",index = 1)
    private String name;

    @ExcelProperty(value = "学生年龄",index = 2)
    private Integer age;

}
