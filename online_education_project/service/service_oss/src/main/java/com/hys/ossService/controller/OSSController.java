package com.hys.ossService.controller;

import com.hys.ossService.service.OSSService;
import com.hys.utils.ResultJsonToHtmlUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @Auth 86191
 * @Date 2020/4/13
 */
@Api(description="阿里云OSS文件管理")
@RestController
@RequestMapping("/aliyun/oss/file")
@CrossOrigin
@Slf4j
public class OSSController {

    @Resource
    private OSSService ossService;

    /**
     * @param file : 上传文件的封装类
     * @return  返回上传文件的路径
     *          返回封装上传文件的路径
     */
    @ApiOperation(value = "上传文件controller")
    @PostMapping("/upload/file/to/oss")
    public ResultJsonToHtmlUtil<String> uploadFileToOSS(
            @ApiParam(name = "multipartFile",value = "上传文件的路径",required = true) MultipartFile file){

            log.info("file="+file.getOriginalFilename());
            ResultJsonToHtmlUtil<String> uploadFile=ossService.uploadFileToOSS(file);

            return uploadFile;
    }


}
