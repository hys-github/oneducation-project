package com.hys.eduService.entity.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Auth 86191
 * @Date 2020/4/16
 *      存储每一个课程下的章节内容
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChapterVO implements Serializable {

    // 课程下每一章的主键id
    private String id;

    // 课程下的每一章的标题
    private String title;

    // 每一章下面的小节集合
    List<VideoVO> children;

}
