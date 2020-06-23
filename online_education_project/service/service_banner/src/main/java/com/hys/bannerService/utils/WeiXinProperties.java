package com.hys.bannerService.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Auth 86191
 * @Date 2020/4/23
 *
 * 当项目已启动，spring接口，spring加载之后，执行接口一个方法
 *  *      项目启动，自动注入，此方式直接就注入值，不需要手动注入(@Autowired)
 */
@Component
public class WeiXinProperties implements InitializingBean {

    // 企业微信开放后的唯一appid
    @Value("${wx.open.APP_ID}")
    private String appId;

    // 微信开放密码
    @Value("${wx.open.APP_SECRET}")
    private String appSecret;

    // edirect_uri的域名
    @Value("${wx.open.REDIRECT_URL}")
    private String redirectUrl;

    public static String APP_ID;
    public static String APP_SECRET;
    public static String REDIRECT_URL;

    @Override
    // 这个方法在项目启动就执行，将properties中的值自动注入到此类中
    public void afterPropertiesSet() throws Exception {
        APP_ID=appId;
        APP_SECRET=appSecret;
        REDIRECT_URL=redirectUrl;
    }
}
