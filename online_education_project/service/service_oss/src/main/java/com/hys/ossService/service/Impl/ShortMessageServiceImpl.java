package com.hys.ossService.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.hys.ossService.config.OSSProperties;
import com.hys.ossService.service.ShortMessageService;
import com.hys.utils.ResultJsonToHtmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auth 86191
 * @Date 2020/4/22
 */
@Service
@Slf4j
public class ShortMessageServiceImpl implements ShortMessageService {


    /**
     * @param sixVerify     生成的6位验证码，将发送到用户手机号上
     * @param phone         接受验证码的手机号
     * @return      成功，返回不带数据的json；失败，controller抓取异常信息
     *
     *      发送短信到手机上
     */
    @Override
    public boolean sendShortMessage(String sixVerify,String phone) {

        DefaultProfile profile = DefaultProfile.getProfile("default",
                OSSProperties.ACCESS_KEY_ID, OSSProperties.ACCESS_KEY_SECRET);

        DefaultAcsClient client = new DefaultAcsClient(profile);

        // 设置相关固定的参数
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");

        //设置发送相关的参数
        // 这些参数的key都是固定的，阿里云这样设置的
        request.putQueryParameter("PhoneNumbers",phone); //手机号
        request.putQueryParameter("SignName","我的项目之在线教育网站"); // 申请阿里云 签名名称
        request.putQueryParameter("TemplateCode","SMS_188640879"); // 申请阿里云 模板code
        
        Map<String,String> map=new HashMap<>();
        // 其中键code是阿里云上的标准；必须这样写，且必须转换位json字符串
        map.put("code",sixVerify);
        String jsonString = JSONObject.toJSONString(map);

        // 验证码数据，转换json数据传递
        request.putQueryParameter("TemplateParam", jsonString);
        
        try {
            
            // 发送消息到阿里云上，将验证码发送到用户手机上
            CommonResponse response = client.getCommonResponse(request);

            boolean flag = response.getHttpResponse().isSuccess();

            return flag;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
