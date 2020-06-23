package com.hys.eduService.entity.VO.excel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auth 86191
 * @Date 2020/4/15
 *      面向视图的实体类
 *      课程分类树形结构展示
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OneSubjectVO {

    private String id;

    private String title;

    List<TwoSubjectVO> children=new ArrayList<>();
}
