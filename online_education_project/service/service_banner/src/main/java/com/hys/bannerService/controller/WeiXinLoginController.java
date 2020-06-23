package com.hys.bannerService.controller;

import com.google.gson.Gson;
import com.hys.bannerService.entity.PO.EduUser;
import com.hys.bannerService.service.EduUserService;
import com.hys.bannerService.utils.WeiXinProperties;
import com.hys.globalConfig.ComhysException;
import com.hys.utils.HttpClientUtils;
import com.hys.utils.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URLEncoder;
import java.util.HashMap;

/**
 * @Auth 86191
 * @Date 2020/4/23
 *
 *      微信登录：由于微信注册开发限制在企业，个人还不具备微信扫描服务
 *      所以用的时是尚硅谷的企业微信开发
 */
@Controller
@RequestMapping("/api/ucenter/wx")
@CrossOrigin
@Api(description = "微信登录时执行的操作")
@Slf4j
public class WeiXinLoginController {

    @Autowired
    EduUserService eduUserService;


    /**
     * @return
     *
     *      微信生成二维码是腾讯开发的，使用固定的url访问即可生成
     *              String url = "https://open.weixin.qq.com/?..."
     */
    @ApiOperation(value = "生成微信二维码的代码")
    @GetMapping("/login")
    public String weiXinLogin(){
        try {
            /**
             *    第一种方法：
             *      固定地址，后面拼接参数
             *              String url = "https://open.weixin.qq.com/" +
             *               "connect/qrconnect?appid="+ ConstantWxUtils.WX_OPEN_APP_ID+"&response_type=code";
             *      其中参数：
             *          appid：企业微信的唯一识别id（必须要填）
             *          redirect_uri：该微信配备的域名，请使用urlEncode对链接进行处理(官方要求)
             *          state(可填可不填，官方文档建议填下)：用于保持请求和回调的状态，授权请求后原样带回给第三方。
             *                  该参数可用于防止csrf攻击（跨站请求伪造攻击），建议第三方带上该参数，
             *                  可设置为简单的随机数加session进行校验
             */
            // 微信开放平台授权baseUrl  %s相当于?代表占位符
            String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                    "?appid=%s" +
                    "&redirect_uri=%s" +
                    "&response_type=code" +
                    "&scope=snsapi_login" +
                    "&state=%s" +
                    "#wechat_redirect";
            // 得到固定的微信域名
            String redirectUrl = WeiXinProperties.REDIRECT_URL;
            // 使用urlEncode对链接进行处理
            redirectUrl = URLEncoder.encode(redirectUrl,"utf-8");

            // 设置%s，将其用实际的参数代替，使用String.format(String,Object...);
            // 注意顺序，返回要跳转的url字符串
            String url = String.format(baseUrl, WeiXinProperties.APP_ID, redirectUrl, "hys_edu");

            // 重定向到微信官方提供的地址，会生成一个二位码返回
            return "redirect:"+url;
        }catch (Exception e){
            e.printStackTrace();
            throw new ComhysException("微信扫描登录失败");
        }
    }


    /**
     * @param code
     * @param state
     * @return
     *        当微信官方生成的二维码被扫描后，回调这个地址（尚硅谷给的定义）
     *      http://127.0.0.1:8150/api/ucenter/wx/callback?code=...&state=...
     *      使用这个地址完成对微信登录用户的注册或者登录操作
     */
    @ApiOperation(value = "当微信登录扫描成功时，自动跳转到这个回调这个url")
    @GetMapping("/callback")
    public String callBack(String code,String state){
         try {
             log.info("code==="+code);
             log.info("state==="+state);
             //1 获取code值，临时票据，类似于验证码(官方返回的code)
             //2 拿着code请求 微信固定的地址，得到两个值 accsess_token 和 openid
             String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                     "?appid=%s" +
                     "&secret=%s" +
                     "&code=%s" +
                     "&grant_type=authorization_code";
             // 得到路经
             String accessTokenUrl = String.format(baseAccessTokenUrl, WeiXinProperties.APP_ID, WeiXinProperties.APP_SECRET, code);

             // 请求这个拼接好的地址，得到返回两个值 accsess_token(访问凭证) 和 openid(每个微信的唯一标识)
             // 使用httpclient发送请求，得到返回结果
             String accessTokenInfo = HttpClientUtils.get(accessTokenUrl);
             log.info("accessTokenInfo======"+accessTokenInfo);
             // {"access_token":"32_aGR4yyYpBd_N4346hxEsWQ_ASjr1nGjfQvAjdx8-4KW5AmvivqRMuy-uYWeU-t8xc3kRwnVNto4q-mkTvPOaOyG-tvlTdU8L-vy_LMqa2OE",  访问凭证
             // "expires_in":7200,
             // refresh_token":"32_hCa_5DKWbVOYWW_QI8hSktJqQzODAEz2ISUChqSmEtQSy5QNhKwAswnRA05Jt0OF3ERZHzZfyR3burD9Uxbf_TJokxRx6XsFwhFL7dTJB1c",
             // "openid":"o3_SC56qnFdqOlKxMWsNuNceRU7o",    // 微信唯一标识
             // "scope":"snsapi_login",
             // "unionid":"oWgGz1HOrbmoxWOsG10uca7qQZJM"}


             // 从accessTokenInfo字符串获取出来两个值 accsess_token 和 openid
             // 把accessTokenInfo字符串转换map集合，根据map里面key获取对应值
             // 使用json转换工具 Gson
             Gson gson = new Gson();
             HashMap accessTokenMap = gson.fromJson(accessTokenInfo, HashMap.class);
             // 访问凭证
             String access_token = (String)accessTokenMap.get("access_token");
             // 每个微信的唯一标识
             String openid = (String)accessTokenMap.get("openid");

             // 通过openid在数据库中查询是否已经注册了该微信用户
             //TODO

             // 拿着得到accsess_token 和 openid，再去请求微信提供固定的地址，获取到扫描人信息
             // 访问微信的资源服务器，获取用户信息
             String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                     "?access_token=%s" +
                     "&openid=%s";
             String userInfoUrl = String.format(baseUserInfoUrl, access_token, openid);
             // 发送请求
             // 获取返回userinfo字符串扫描人信息
             String userInfo = HttpClientUtils.get(userInfoUrl);
             log.info("userInfo======"+userInfo);
             // userInfo=={"openid":"o3_SC56qnFdqOlKxMWsNuNceRU7o",
             // "nickname":"那来那么多的如果",
             // "sex":1,"language":"zh_CN","city":"","province":"","country":"CN",
             // "headimgurl":"http:\/\/thirdwx.qlogo.cn\/mmopen\/vi_32\/qOBZUo2icJyZDLQBbqviaK56nPqfpTTibFJ9J8NvX1MjmmiaDbqRRUf9FxbDWEvc3ka7tz7ubpaFQDEJVgzLEUTDbw\/132",
             // "privilege":[],"unionid":"oWgGz1HOrbmoxWOsG10uca7qQZJM"}

             // 将json字符串转换为hashmap
             HashMap userInfoMap = gson.fromJson(userInfo, HashMap.class);
             log.info("userInfoMap-----"+userInfoMap);
             // userInfoMap-----{country=CN, unionid=oWgGz1HOrbmoxWOsG10uca7qQZJM, province=, city=,
             // openid=o3_SC56qnFdqOlKxMWsNuNceRU7o, sex=1.0, nickname=那来那么多的如果, headimgurl=http://thirdwx.qlogo.cn/mmopen/vi_32/qOBZUo2icJyZDLQBbqviaK56nPqfpTTibFJ9J8NvX1MjmmiaDbqRRUf9FxbDWEvc3ka7tz7ubpaFQDEJVgzLEUTDbw/132,
             // language=zh_CN, privilege=[]}

             String nickname = (String)userInfoMap.get("nickname");//昵称
             String headimgurl = (String)userInfoMap.get("headimgurl");//头像

             EduUser eduUser = new EduUser();
             eduUser.setOpenid(openid);
             eduUser.setAvatar(headimgurl);
             eduUser.setNickname(nickname);
             // 将微信登录用户保存在数据库中
             eduUserService.save(eduUser);

             // 使用jwt根据member对象生成token字符串
             String jwtToken = JwtUtils.getJwtToken(eduUser.getId(), eduUser.getNickname());

             // 将token字符串返回到登录成功的首页页面
             return "redirect:http://127.0.0.1:3000?token="+jwtToken;
         }catch (Exception e){
             e.printStackTrace();
             throw new ComhysException("登录失败");
         }
    }



}
