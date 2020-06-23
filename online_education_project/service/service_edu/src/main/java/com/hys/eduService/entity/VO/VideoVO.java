package com.hys.eduService.entity.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Auth 86191
 * @Date 2020/4/16
 *      每一章下的小节内容
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class VideoVO implements Serializable {

    // 每一章下的小节主键id
    private String id;

    // 每一章下的小节标题
    private String title;

}
