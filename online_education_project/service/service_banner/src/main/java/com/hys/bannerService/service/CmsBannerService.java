package com.hys.bannerService.service;

import com.hys.bannerService.entity.PO.CmsBanner;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务类
 * </p>
 *
 * @author hys
 * @since 2020-04-21
 */
public interface CmsBannerService extends IService<CmsBanner> {

    List<CmsBanner> getBannerList();

}
