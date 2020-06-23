package com.hys.bannerService.controller;


import com.hys.bannerService.entity.PO.CmsBanner;
import com.hys.bannerService.service.CmsBannerService;
import com.hys.utils.ResultJsonToHtmlUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *
 * @author hys
 * @since 2020-04-21
 */
@Api(description = "首页显示banner")
@CrossOrigin
@RestController
@RequestMapping("/bannerService/banner")
public class CmsFrontBannerController {

    @Autowired
    CmsBannerService cmsBannerService;


    /**
     * @return  成功，返回得到的banner图片集合；失败，返回异常信息
     *
     *      得到首页显示的banner图片集合
     */
    @ApiOperation(value = "得到显示在首页的banner图片信息")
    @GetMapping("/get/banner/to/first/page")
    public ResultJsonToHtmlUtil<List<CmsBanner>> getBannerToFirstPage(){
        try {

            List<CmsBanner> bannerList = cmsBannerService.getBannerList();

            return ResultJsonToHtmlUtil.successWithData(bannerList);
        }catch (Exception e){
            e.printStackTrace();
            return ResultJsonToHtmlUtil.failedWithErrorMessage(e.getMessage());
        }
    }


}

