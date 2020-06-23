package com.hys.globalConfig;

import com.hys.utils.ResultJsonToHtmlUtil;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Auth 86191
 * @Date 2020/4/10
 */
@ControllerAdvice
public class ExceptionResolveHandler {

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResultJsonToHtmlUtil<String> ComHysExceptionResolver(Exception e){
        e.printStackTrace();
        return ResultJsonToHtmlUtil.failedWithErrorMessage(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = ComhysException.class)
    public ResultJsonToHtmlUtil<String> ComHysExceptionResolver(ComhysException e){
        e.printStackTrace();
        return ResultJsonToHtmlUtil.failedWithErrorMessage(e.getMessage());
    }

}
