package com.hys.ossService.service;

import com.hys.utils.ResultJsonToHtmlUtil;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Auth 86191
 * @Date 2020/4/13
 */
public interface OSSService {
    ResultJsonToHtmlUtil<String> uploadFileToOSS(MultipartFile multipartFile);
}
