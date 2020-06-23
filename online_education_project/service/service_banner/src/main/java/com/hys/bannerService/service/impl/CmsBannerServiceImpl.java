package com.hys.bannerService.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hys.bannerService.entity.PO.CmsBanner;
import com.hys.bannerService.mapper.CmsBannerMapper;
import com.hys.bannerService.service.CmsBannerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务实现类
 * </p>
 *
 * @author hys
 * @since 2020-04-21
 */
@Service
@Slf4j
public class CmsBannerServiceImpl extends ServiceImpl<CmsBannerMapper, CmsBanner> implements CmsBannerService {


    /**
     * @return  成功，得到banner集合；失败，抛出异常，并回滚数据
     */
    @Override
    @Cacheable(value = "banner",key = "'bannerList'")
    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    public List<CmsBanner> getBannerList() {

        QueryWrapper<CmsBanner> wrapper = new QueryWrapper<>();
        // 通过id降序排序查询
        wrapper.orderByDesc("id");
        // 限制查询的数量
        wrapper.last("limit 3");
        // 查询
        List<CmsBanner> bannerList = this.list(wrapper);

        return bannerList;
    }


}
