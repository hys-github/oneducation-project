package com.hys.eduService.service;

import com.hys.eduService.entity.PO.EduSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hys.eduService.entity.VO.excel.OneSubjectVO;
import com.hys.utils.ResultJsonToHtmlUtil;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author hys
 * @since 2020-04-15
 */
public interface EduSubjectService extends IService<EduSubject> {

    ResultJsonToHtmlUtil<String> saveSubject(MultipartFile multipartFile,EduSubjectService eduSubjectService);

    ResultJsonToHtmlUtil<Collection<OneSubjectVO>> querySubjectList();
}
