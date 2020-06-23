package com.hys.eduService.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hys.eduService.entity.PO.EduChapter;
import com.hys.eduService.entity.PO.EduVideo;
import com.hys.eduService.entity.VO.ChapterVO;
import com.hys.eduService.entity.VO.VideoVO;
import com.hys.eduService.mapper.EduChapterMapper;
import com.hys.eduService.service.EduChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hys.eduService.service.EduVideoService;
import com.hys.utils.ResultJsonToHtmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.runtime.directive.contrib.For;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author hys
 * @since 2020-04-16
 */
@Service
@Slf4j
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    EduVideoService eduVideoService;

    /**
     * @param courseId  传过来的课程id
     * @return  失败，返回异常信息；成功，返回查询到的课程章节信息
     *      通过课程id得到课程章节的集合
     */
    @Override
    public ResultJsonToHtmlUtil<Collection<ChapterVO>> queryChapterAndVideo(String courseId) {

        // 通过课程id查询得到该课程有多少章内容
        QueryWrapper<EduChapter> chapterQueryWrapper = new QueryWrapper<>();
        chapterQueryWrapper.eq("course_id",courseId);
        chapterQueryWrapper.orderByAsc("create_time");
        List<EduChapter> eduChapterList = this.list(chapterQueryWrapper);

        // 通过ChapterVO的id存储ChapterVO
        Map<String,ChapterVO> chapterVOMap=new HashMap<>();

        // 将查询到的课程的章的id和title传递给ChapterVO中
        for(EduChapter eduChapter:eduChapterList){
            // 创造ChapterVO实列
            ChapterVO chapterVO = new ChapterVO();
            // 复制属性
            BeanUtils.copyProperties(eduChapter,chapterVO);
            // 将ChapterVO存储到map中
            chapterVOMap.put(eduChapter.getId(),chapterVO);
            // 存储ChapterVO的list集合
        }

        // 通过课程的id查询课程的所有小节(也可以通过章来查询，但是要麻烦点，还要遍历)
        QueryWrapper<EduVideo> eduVideoQueryWrapper = new QueryWrapper<>();
        eduVideoQueryWrapper.eq("course_id",courseId);
        List<EduVideo> eduVideoList = eduVideoService.list(eduVideoQueryWrapper);

        log.info(eduVideoList.toString());

        for (EduVideo eduVideo:eduVideoList){
            // 创造VideoVO的实列
            VideoVO videoVO = new VideoVO();
            BeanUtils.copyProperties(eduVideo,videoVO);

            // 通过查询到的EduVideo(里面封装了每一节的标题还有关联了章的id)
            ChapterVO chapterVO = chapterVOMap.get(eduVideo.getChapterId());

            // 判断每一章中是否已经添加了小节信息
            List<VideoVO> videoVOS = chapterVO.getChildren();
            // 如果没有添加，则new一个集合添加；如果已经有小节信息，则不用实列化对象
            if(videoVOS==null||videoVOS.size()==0){
                videoVOS=new ArrayList<>();
                // videoVOS.add(videoVO);
                // 必须要set回去
                chapterVO.setChildren(videoVOS);
            }
            videoVOS.add(videoVO);
        }

        log.info(chapterVOMap.toString());

        Collection<ChapterVO> values = chapterVOMap.values();

        return ResultJsonToHtmlUtil.successWithData(values);
    }


    /**
     * @param chapterId 通过课程的章节主键id删除章节信息
     * @return  成功，返回不带数据的json；失败，返回异常信息
     *      删除的前提是：章节下面不能有任何小节内容，或者返回失败
     */
    @Override
    public ResultJsonToHtmlUtil<String> deleteChapterByChapterId(String chapterId) {

        // 通过章节的主键id查询是否其下面有小节
        QueryWrapper<EduVideo> eduVideoQueryWrapper = new QueryWrapper<>();
        eduVideoQueryWrapper.eq("chapter_id",chapterId);
        // 同过章节主键id查询video的数量
        int count = eduVideoService.count(eduVideoQueryWrapper);

        // 判断count,如果count大于0，则该章节下有小节，不能删除
        if(count>0){
            return ResultJsonToHtmlUtil.failedWithErrorMessage("只有章节下没有小节时，才能删除");
        }else{
            this.removeById(chapterId);

            return ResultJsonToHtmlUtil.successWithOutOfData();
        }
    }
}
