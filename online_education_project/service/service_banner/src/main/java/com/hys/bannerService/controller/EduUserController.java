package com.hys.bannerService.controller;


import com.hys.bannerService.entity.PO.EduUser;
import com.hys.bannerService.entity.VO.LoginVO;
import com.hys.bannerService.entity.VO.RegisterVO;
import com.hys.bannerService.service.EduUserService;
import com.hys.utils.JwtUtils;
import com.hys.utils.ResultJsonToHtmlUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *com.hys.bannerService.entity
 * @author hys
 * @since 2020-04-22
 */
@Api(description = "前台用户登录后一系列操作的controller")
@RestController
@RequestMapping("/bannerService/user")
@CrossOrigin
@Slf4j
public class EduUserController {

    @Autowired
    EduUserService eduUserService;


    /**
     * @param loginVO   用户登录的信息封装实体类
     * @return      成功登录，返回token；失败，返回异常信息
     *
     *          用户登录判断
     */
    @ApiOperation(value = "用户登录判断，并返回token")
    @PostMapping("/login")
    public ResultJsonToHtmlUtil<String> userLogin(
            @ApiParam(name = "loginVO",value = "用户登录的账号密码",required = true) @RequestBody LoginVO loginVO){
        try {

            ResultJsonToHtmlUtil<String> resultJsonToHtmlUtil = eduUserService.judgeUserLogin(loginVO);

            return resultJsonToHtmlUtil;
        }catch (Exception e){
            e.printStackTrace();
            return ResultJsonToHtmlUtil.failedWithErrorMessage(e.getMessage());
        }
    }


    /**
     * @param registerVO    用户注册上传上来的信息
     * @return  成功，返回不带数据的json；失败，返回异常信息
     *
     *      用户注册代码
     */
    @ApiOperation(value = "用户注册")
    @PostMapping("/register")
    public ResultJsonToHtmlUtil<String> userRegister(
            @ApiParam(name = "registerVO",value = "用户注册的信息",required = true)@RequestBody RegisterVO registerVO){
        try {

            ResultJsonToHtmlUtil<String> resultJsonToHtmlUtil = eduUserService.userRegister(registerVO);

            return resultJsonToHtmlUtil;
        }catch (Exception e){
            e.printStackTrace();

            if(e instanceof DuplicateKeyException){
                return ResultJsonToHtmlUtil.failedWithErrorMessage("对不起，该手机号已被注册!!!");
            }

            return ResultJsonToHtmlUtil.failedWithErrorMessage(e.getMessage());
        }
    }


    /**
     * @param request   前端页面将token信息存储到request域中的header中
     *                      调用JwtUtils这个工具类，通过jwt默认生成的token规则得到用户的id
     * @return  成功，返回登录用户的信息；失败返回异常信息
     *
     *      得到登录用户信息
     */
    @ApiParam(value = "通过上传上来的cookie头中的token得到用户信息")
    @GetMapping("/get/user/info")
    public ResultJsonToHtmlUtil<EduUser> getUserInfoByToken(HttpServletRequest request){
        try {
            // 通过上传上来的cookie头信息得到登录用户的id
            String userId = JwtUtils.getMemberIdByJwtToken(request);

            // 通过用户的id得到用户信息
            EduUser user = eduUserService.getById(userId);

            return ResultJsonToHtmlUtil.successWithData(user);
        }catch (Exception e){
            e.printStackTrace();
            return ResultJsonToHtmlUtil.failedWithErrorMessage(e.getMessage());
        }
    }





}

