package com.hys.eduService.controller.daemon;

import com.hys.utils.ResultJsonToHtmlUtil;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auth 86191
 * @Date 2020/4/11
 */
@RestController
@RequestMapping("/eduTeacher/user")
@CrossOrigin
public class EduLoginController {

    @RequestMapping("/login")
    public ResultJsonToHtmlUtil<Map<String,Object>> toLogin(){

        Map<String,Object> map=new HashMap<>();

        map.put("token","admin");

        return ResultJsonToHtmlUtil.successWithData(map);

    }

    @RequestMapping("/info")
    public ResultJsonToHtmlUtil<Map<String,String>> getInfo(){

        Map<String,String> map=new HashMap<>();

        map.put("name","小米粒");
        map.put("roles","[小米粒]");
        map.put("avatar","https://com-hys.oss-cn-shenzhen.aliyuncs.com/2020-04-02/40d7ca945.jpg");

        return ResultJsonToHtmlUtil.successWithData(map);
    }


}
