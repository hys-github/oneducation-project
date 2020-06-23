package com.hys.eduService.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hys.eduService.config.SubjectExcelListener;
import com.hys.eduService.entity.PO.EduSubject;
import com.hys.eduService.entity.VO.excel.OneSubjectVO;
import com.hys.eduService.entity.VO.excel.SubjectExcelVO;
import com.hys.eduService.entity.VO.excel.TwoSubjectVO;
import com.hys.eduService.mapper.EduSubjectMapper;
import com.hys.eduService.service.EduSubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hys.utils.ResultJsonToHtmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author hys
 * @since 2020-04-15
 */
@Service
@Slf4j
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    @Override
    /**
     *      multipartFile：上传上来的课程分类的excel表格
     */
    public ResultJsonToHtmlUtil<String> saveSubject(MultipartFile multipartFile,EduSubjectService eduSubjectService) {

        try{
            // 得到上传文件的读操作流
            InputStream inputStream = multipartFile.getInputStream();

            // 使用读流的方式将数据一行一行读出给excel监听器SubjectExcelListener去执行存储数据库的操作
            EasyExcel.read(inputStream, SubjectExcelVO.class,new SubjectExcelListener(eduSubjectService)).sheet().doRead();

            return ResultJsonToHtmlUtil.successWithOutOfData();
        }catch (Exception e){
            e.printStackTrace();

            return ResultJsonToHtmlUtil.failedWithErrorMessage(e.getMessage());
        }
    }

    @Override
    /**
     *  查询所有的课程名展示到页面上
     *      封装成以树形结构模式
     */
    public ResultJsonToHtmlUtil<Collection<OneSubjectVO>> querySubjectList() {
        try {

            /*
                查看源码serviceImpl可知：
                        @Autowired
                        protected M baseMapper;
                    mybatis-plus自动注入了mapper，所以在使用mapper时可直接使用，不用再次注入，也可以使用this，调用本类
             */
            // 根据数据库中表中字段的特性：使用parent_id查询到第一级课程信息
            // select*from edu_subject where parent_id='0';
            QueryWrapper<EduSubject> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("parent_id","0");
            List<EduSubject> oneSubjects = baseMapper.selectList(queryWrapper);

            // 根据数据库中表中字段的特性：使用parent_id查询到第二级课程信息
            // select*from edu_subject where parent_id!='0';
            QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
            wrapper.ne("parent_id","0");
            List<EduSubject> twoSubjects = this.list(wrapper);


            Map<String,OneSubjectVO> map=new HashMap<>();
            // 遍历第一级课程信息
            for (EduSubject eduSubject:oneSubjects){

                OneSubjectVO oneSubjectVO = new OneSubjectVO();
                // 将第一级的课程信息名和id复制到OneSubjectVO这个对象中
                BeanUtils.copyProperties(eduSubject,oneSubjectVO);
                // 将第一级的课程信息对象oneSubjectVO根据其id封装到map中
                map.put(oneSubjectVO.getId(),oneSubjectVO);
            }

            // 遍历第二级课程信息
            for (EduSubject subject:twoSubjects){

                // 将第二级的课程信息名和id复制到TwoSubjectVO这个对象中
                TwoSubjectVO twoSubjectVO=new TwoSubjectVO();
                BeanUtils.copyProperties(subject,twoSubjectVO);

                // 通过EduSubject的parent_id得到与其父节点关联的id的对象
                OneSubjectVO oneSubjectVO = map.get(subject.getParentId());

                // 将第二级的对象添加到第一级内部，实现树形结构
                oneSubjectVO.getChildren().add(twoSubjectVO);
            }

            log.info("map="+map);

            // 得到map的value集合
            Collection<OneSubjectVO> values =  map.values();

            return ResultJsonToHtmlUtil.successWithData(values);
        }catch (Exception e){
            e.printStackTrace();

            return ResultJsonToHtmlUtil.failedWithErrorMessage(e.getMessage());
        }

    }



}
