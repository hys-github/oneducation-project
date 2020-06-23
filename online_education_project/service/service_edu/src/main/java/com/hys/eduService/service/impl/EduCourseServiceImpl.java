package com.hys.eduService.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hys.eduService.entity.PO.EduChapter;
import com.hys.eduService.entity.PO.EduCourse;
import com.hys.eduService.entity.PO.EduCourseDescription;
import com.hys.eduService.entity.PO.EduVideo;
import com.hys.eduService.entity.VO.CourseInfoVO;
import com.hys.eduService.entity.VO.CoursePublishVO;
import com.hys.eduService.mapper.EduCourseMapper;
import com.hys.eduService.remoteServer.VideoRemoteService;
import com.hys.eduService.service.EduChapterService;
import com.hys.eduService.service.EduCourseDescriptionService;
import com.hys.eduService.service.EduCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hys.eduService.service.EduVideoService;
import com.hys.utils.ResultJsonToHtmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


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
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    @Autowired
    EduCourseDescriptionService eduCourseDescriptionService;

    @Autowired
    EduChapterService eduChapterService;

    @Autowired
    EduVideoService eduVideoService;

    @Autowired
    VideoRemoteService videoRemoteService;


    /**
     * @param courseInfoVO  将上传过来的课程信息添加到数据库中
     * @return  成功，返回添加课程id；失败，controller抓取异常
     *
     *      当上传上来的课程信息对象CourseInfoVO这个中拥有课程主键id时，
     *              则这个前端执行的操作为修改课程信息的操作
     *      上传上来的课程信息对象CourseInfoVO没有课程主键id时，
     *              则为新添加课程操作
     *      在impl中通过课程主键id判断是否为添加还是修改操作
     */
    @Override
    public String addOrUpdateCourseInfoToMysql(CourseInfoVO courseInfoVO) {

        String courseId=null;

        // 如果上传上来的课程信息没有主键id，即主键id为空，执行添加课程信息操作
        if (courseInfoVO.getId()==null){
            courseId = this.addCourseInfoToMysql(courseInfoVO);
        }
        // 上传上来的有主键id，执行修改课程信息的操作（通过课程主键id修改）
        else{
            courseId=this.updateCourseInfoToMysql(courseInfoVO);
        }

        return courseId;
    }


    /**
     * @param courseId  从前端传过来的课程id
     * @return  成功，返回课程的基本信息；失败，controller抓取异常信息
     *
     *      通过课程id查询课程信息
     */
    @Override
    public CourseInfoVO getReturnDataFromMysql(String courseId) {

        // 通过课程主键id查询得到课程信息
        EduCourse eduCourse = this.getById(courseId);

        // 创建一个面向视图层面的课程信息对象实列
        CourseInfoVO courseInfoVO = new CourseInfoVO();

        // 将EduCourse中的属性复制到CourseInfoVO对应的属性中
        BeanUtils.copyProperties(eduCourse,courseInfoVO);

        // 通过课程主键id得到课程描述的信息（其中，课程描述的表中的主键与课程的主键相关联）
        EduCourseDescription eduCourseDescription = eduCourseDescriptionService.getById(courseId);

        // 判断该课程是否拥有课程描述，有，走if代码；
        if(eduCourseDescription!=null){
            BeanUtils.copyProperties(eduCourseDescription,courseInfoVO);
        }

        // 成功返回课程的基本信息
        return courseInfoVO;
    }


    /**
     * @param courseId  课程的主键id
     * @return  成功，返回课程发布信息内容；失败，返回异常信息
     *
     *      通过课程主键id得到要发布课程的详细信息展示到页面上
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    public CoursePublishVO getCoursePublishVOByCourseId(String courseId) {

        CoursePublishVO coursePublishVO = baseMapper.getCoursePublishVOByCourseId(courseId);

        String status = coursePublishVO.getStatus();

        // 该课程未发布
        if("Draft".equals(status)){
            coursePublishVO.setStatus("未发布");
        }else{
            coursePublishVO.setStatus("已发布");
        }
        return coursePublishVO;
    }


    /**
     * @param courseId  通过课程的主键id删除课程的所有信息
     *
     *        删除课程的所有信息
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    public ResultJsonToHtmlUtil<String> deleteCourseByCourseId(String courseId) {

        // 通过课程id得到每小节下面的视频凭证id
        QueryWrapper<EduVideo> wrapper=new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        wrapper.select("video_source_id");
        List<EduVideo> eduVideoList = eduVideoService.list(wrapper);
        int size = eduVideoList.size();
        List<String> videoSourceIds=new ArrayList<>();
        for(int i=0;i<size;i++){
            EduVideo eduVideo = eduVideoList.get(i);
            if (eduVideo.getVideoSourceId()!=null){
                videoSourceIds.add(eduVideo.getVideoSourceId());
            }
        }
        // 远端服务器调用失败，返回异常信息
        ResultJsonToHtmlUtil<String> resultJsonToHtmlUtil = videoRemoteService.deleteBatchVideoToAliyunByVideoIds(videoSourceIds);
        if(!resultJsonToHtmlUtil.isResult()){
            log.info(resultJsonToHtmlUtil.getErrorMessage());

            return resultJsonToHtmlUtil;
        }

        // 通过课程的id删除课程下的章的信息
        QueryWrapper<EduChapter> eduChapterQueryWrapper = new QueryWrapper<>();
        eduChapterQueryWrapper.eq("course_id",courseId);
        eduChapterService.remove(eduChapterQueryWrapper);

        // 通过课程的id删除课程下章下面的小节
        QueryWrapper<EduVideo> eduVideoQueryWrapper = new QueryWrapper<>();
        eduVideoQueryWrapper.eq("course_id",courseId);
        eduVideoService.remove(eduVideoQueryWrapper);

        // 通过课程id删除课程描述信息
        eduCourseDescriptionService.removeById(courseId);

        // 通过课程id删除课程基本信息
        this.removeById(courseId);
        return resultJsonToHtmlUtil;
    }


    /**
     * @param courseInfoVO  将上传过来的课程信息添加到数据库中
     * @return  成功，返回添加课程id；失败，controller抓取异常
     *
     *       为新添加课程操作
     */
    public String addCourseInfoToMysql(CourseInfoVO courseInfoVO){
        // 将从视图上得到的课程信息复制到EduCourse对象中
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVO,eduCourse);

        // 将课程信息插入到mysql中,插入信息后，自动返回生成的主键
        // 也可以用 int insert = baseMapper.insert(eduCourse);
        boolean flag = this.save(eduCourse);
        log.info(eduCourse.toString());

        // 判断是否插入数据库成功与否。是，返回true；否，返回false
        if(flag){

            EduCourseDescription eduCourseDescription = new EduCourseDescription();
            BeanUtils.copyProperties(courseInfoVO,eduCourseDescription);

            // 将课程自动生成的主键id给课程描述的id，使之关联
            eduCourseDescription.setId(eduCourse.getId());

            // 将课程的详细介绍插入到数据库中
            eduCourseDescriptionService.save(eduCourseDescription);
        }

        return eduCourse.getId();
    }


    /**
     * @param courseInfoVO  将上传过来的课程信息添加到数据库中
     * @return  成功，返回添加课程id；失败，controller抓取异常
     *
     *       为修改课程操作
     */
    public String updateCourseInfoToMysql(CourseInfoVO courseInfoVO){

        // 将从视图上得到的课程信息复制到EduCourse对象中
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVO,eduCourse);

        // 通过课程的主键id将课程的信息修改
        baseMapper.updateById(eduCourse);

        // 将从页面提交上来的课程描述复制到EduCourseDescription这个中
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        BeanUtils.copyProperties(courseInfoVO,eduCourseDescription);

        eduCourseDescriptionService.updateById(eduCourseDescription);


        return courseInfoVO.getId();
    }


}
