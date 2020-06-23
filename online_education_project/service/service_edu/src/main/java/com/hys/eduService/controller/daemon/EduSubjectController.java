package com.hys.eduService.controller.daemon;


import com.hys.eduService.entity.VO.excel.OneSubjectVO;
import com.hys.eduService.service.EduSubjectService;
import com.hys.utils.ResultJsonToHtmlUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author hys
 * @since 2020-04-15
 */
@Api(description = "课程分类管理")
@RestController
@RequestMapping("/eduService/subject")
@CrossOrigin
@Slf4j
public class EduSubjectController {

    @Autowired
    EduSubjectService eduSubjectService;

    /**
     * @param multipartFile: 上传上来的excel表格文件，封装到类中
     * @return  成功不返回信息，失败返回失败信息
     *          添加课程分类
     */
    @ApiOperation(value = "课程分类添加controller")
    @PostMapping("/add/subject")
    public ResultJsonToHtmlUtil<String> addEduSubject(
            @ApiParam(name = "multipartFile",value = "上传的课程分类excel表格",required = true) MultipartFile multipartFile){

        log.info(multipartFile.getOriginalFilename());
        ResultJsonToHtmlUtil<String> resultJsonToHtmlUtil=eduSubjectService.saveSubject(multipartFile,eduSubjectService);

        return resultJsonToHtmlUtil;
    }

    /**
     * @return：失败  返回异常信息； 成功   返回前端树形结构模式
     *      查询所有的课程名展示到页面上
     */
    @ApiOperation(value = "得到所有课程名，以树形结构")
    @GetMapping("/query/subject")
    public ResultJsonToHtmlUtil<Collection<OneSubjectVO>> querySubjectList(){

        ResultJsonToHtmlUtil<Collection<OneSubjectVO>> resultJsonToHtmlUtil=eduSubjectService.querySubjectList();

        return resultJsonToHtmlUtil;
    }



}

