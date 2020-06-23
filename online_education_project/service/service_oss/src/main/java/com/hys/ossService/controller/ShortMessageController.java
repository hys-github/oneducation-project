package com.hys.ossService.controller;

import com.hys.ossService.service.ShortMessageService;
import com.hys.utils.AutoRandomStringUtil;
import com.hys.utils.ResultJsonToHtmlUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * @Auth 86191
 * @Date 2020/4/22
 *
 *      发送短信的controller
 */
@Api(description = "短信验证controller")
@RequestMapping("/aliyun/sms")
@RestController
@CrossOrigin
@Slf4j
public class ShortMessageController {

    @Autowired
    ShortMessageService shortMessageService;

    @Autowired
    RedisTemplate<String,String> redisTemplate;


    /**
     * @param phone 接受验证码的手机号
     * @return  成功，返回不带数据的json；失败，返回异常信息
     *
     *      放松短信验证码到手机号上
     */
    @ApiOperation(value = "发送短信到手机上操作")
    @GetMapping("/send/short/message/to/your/phone/{phone}")
    public ResultJsonToHtmlUtil<String> sendShortMessageToYourPhone(
            @ApiParam(name = "phone",value = "短信接受的手机号") @PathVariable("phone") String phone){
        try {
            // 查看该手机号码是否已经发送了验证码
            String verify = redisTemplate.opsForValue().get(phone);
            // 判断是否已发送验证码
            if(verify!=null){
                return ResultJsonToHtmlUtil.failedWithErrorMessage("已发送验证码到 【"+phone+"】，请5分钟之后在发送");
            }

            // 生成6为数字的验证码
            String sixVerify = AutoRandomStringUtil.getSixBitRandom();
            log.info("verify==="+sixVerify);

            // 发送消息到手机上,并返回结果boolean类型
            boolean flag = shortMessageService.sendShortMessage(sixVerify,phone);

            if(flag) {
                // 成功发送验证码，将验证码存储在redis中，并设置5分钟过期时间
                redisTemplate.opsForValue().set(phone,sixVerify,5L, TimeUnit.MINUTES);

                return ResultJsonToHtmlUtil.successWithOutOfData();
            }else{
                return ResultJsonToHtmlUtil.failedWithErrorMessage("发送验证码失败，查看后台错误");
            }

        }catch (Exception e){
            e.printStackTrace();
            return ResultJsonToHtmlUtil.failedWithErrorMessage(e.getMessage());
        }
    }



}
