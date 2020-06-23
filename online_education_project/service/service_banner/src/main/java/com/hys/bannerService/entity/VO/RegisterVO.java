package com.hys.bannerService.entity.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Auth 86191
 * @Date 2020/4/22
 *
 *      用户注册的页面实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterVO implements Serializable {

    // 注册用户的手机号
    private String mobile;

    // 注册用户的昵称
    private String nickname;

    // 注册用户的密码
    private String password;

    // 注册用户的验证码
    private String verifyCode;

}
