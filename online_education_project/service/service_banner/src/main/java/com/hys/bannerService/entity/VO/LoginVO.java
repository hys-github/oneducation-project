package com.hys.bannerService.entity.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Auth 86191
 * @Date 2020/4/22
 *
 *      面向用户登录的实体类封装类
 *          封装登录用户的账号(电话号码）和密码
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginVO implements Serializable {

    // 上传登录用户的手机号
    private String mobile;

    // 上传登录用户的密码
    private String password;

}
