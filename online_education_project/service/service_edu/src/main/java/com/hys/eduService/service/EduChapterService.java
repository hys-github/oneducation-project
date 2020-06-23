package com.hys.eduService.service;

import com.hys.eduService.entity.PO.EduChapter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hys.eduService.entity.VO.ChapterVO;
import com.hys.utils.ResultJsonToHtmlUtil;

import java.util.Collection;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author hys
 * @since 2020-04-16
 */
public interface EduChapterService extends IService<EduChapter> {

    ResultJsonToHtmlUtil<Collection<ChapterVO>> queryChapterAndVideo(String courseId);

    ResultJsonToHtmlUtil<String> deleteChapterByChapterId(String chapterId);
}
