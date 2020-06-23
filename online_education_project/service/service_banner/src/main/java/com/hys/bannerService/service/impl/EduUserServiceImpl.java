package com.hys.bannerService.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hys.bannerService.entity.PO.EduUser;
import com.hys.bannerService.entity.VO.LoginVO;
import com.hys.bannerService.entity.VO.RegisterVO;
import com.hys.bannerService.mapper.EduUserMapper;
import com.hys.bannerService.service.EduUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hys.utils.JwtUtils;
import com.hys.utils.ResultJsonToHtmlUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author hys
 * @since 2020-04-22
 */
@Service
public class EduUserServiceImpl extends ServiceImpl<EduUserMapper, EduUser> implements EduUserService {

    @Autowired
    RedisTemplate<String,String> redisTemplate;

    /**
     * @param loginVO   登录用户上传上来的账号和密码
     * @return  成功，返回token；失败，返回异常信息
     *
     *      判断用户是否登录成功
     */
    @Override
    @Transactional(readOnly = true)
    public ResultJsonToHtmlUtil<String> judgeUserLogin(LoginVO loginVO) {
        // 得到登录用户的电话号码
        String mobile = loginVO.getMobile();

        // 通过登录用户的电话号码查询用户的信息
        QueryWrapper<EduUser> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        EduUser eduUser = baseMapper.selectOne(wrapper);

        // 判断该登录用户电话号码是否已经注册过
        if(eduUser==null){
            return ResultJsonToHtmlUtil.failedWithErrorMessage("对不起，该手机号还未进行注册，请先注册在登录");
        }

        // 判断该登录用户是否被禁用，不允许登录；默认true为禁用该用户，false为不禁用该用户
        if(eduUser.getIsDisabled()){
            return ResultJsonToHtmlUtil.failedWithErrorMessage("对不起，该手机号已经被禁用!!!");
        }

        // 判断登录密码是否正确
        String loginVOPassword = loginVO.getPassword();
        String userPassword = eduUser.getPassword();

        // 采用spring security中的默认加密方法加密
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // String encode = passwordEncoder.encode(loginVOPassword);
        boolean matches = passwordEncoder.matches(loginVOPassword, userPassword);

        if(!matches){
            return ResultJsonToHtmlUtil.failedWithErrorMessage("对不起，密码错误，请重新输入密码!!!");
        }

        // 如果以上判断均成功，则登录成功，返回token
        String jwtToken = JwtUtils.getJwtToken(eduUser.getId(), eduUser.getNickname());

        return ResultJsonToHtmlUtil.successWithData(jwtToken);
    }


    /**
     * @param registerVO    用户注册上传上来的信息
     * @return  成功，返回不带数据的json；失败，返回异常信息
     *
     *      用户注册的service实现类
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    @Override
    public ResultJsonToHtmlUtil<String> userRegister(RegisterVO registerVO) {

        // 得到用户上传上来的手机验证码
        String verifyCode = registerVO.getVerifyCode();
        String mobile = registerVO.getMobile();

        // 从redis中查出该手机号码的验证码
        String code = redisTemplate.opsForValue().get(mobile);

        // 判断redis中是否存有该手机号码的验证码
        if(code==null){
            return ResultJsonToHtmlUtil.failedWithErrorMessage("对不起，该验证码不存在或者已过期!!!");
        }
        // 如果判断验证码不一致，返回异常信息
        if(!(Objects.equals(verifyCode,code))){
            return ResultJsonToHtmlUtil.failedWithErrorMessage("对不起，输入验证码错误，请重新输入!!!");
        }

        // 如果验证码一致，则将注册用户的信息插入mysql中
        EduUser eduUser = new EduUser();
        BeanUtils.copyProperties(registerVO,eduUser);
        // 将密码加密后存入数据库中
        String password = eduUser.getPassword();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode(password);
        eduUser.setPassword(encode);
        // 设置该用户一个默认头像
        eduUser.setAvatar("https://online-education-project.oss-cn-shenzhen.aliyuncs.com/2020/04/20/e6b74117_litter_girl3.jpg");
        baseMapper.insert(eduUser);

        return ResultJsonToHtmlUtil.successWithOutOfData();
    }





}
