package com.hys.bannerService.service;

import com.hys.bannerService.entity.PO.EduUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hys.bannerService.entity.VO.LoginVO;
import com.hys.bannerService.entity.VO.RegisterVO;
import com.hys.utils.ResultJsonToHtmlUtil;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author hys
 * @since 2020-04-22
 */
public interface EduUserService extends IService<EduUser> {

    ResultJsonToHtmlUtil<String> judgeUserLogin(LoginVO loginVO);

    ResultJsonToHtmlUtil<String> userRegister(RegisterVO registerVO);
}
