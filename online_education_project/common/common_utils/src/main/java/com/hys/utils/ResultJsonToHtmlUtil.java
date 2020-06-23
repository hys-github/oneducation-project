package com.hys.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Auth 86191
 * @Date 2020/4/10
 *      返回到页面数据的封装类
 *      result：返回ajax时成功还是失败,boolean类型
 *      errorMessage：string类型，返回失败信息
 *      data：自定义传过来的类型，返回的封装数据
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultJsonToHtmlUtil<T> {

    // 返回ajax时成功还是失败
    private boolean result;

    // 返回状态码20000，成功
    private Integer code;

    // 返回的ajax出现的异常信息
    private String errorMessage;

    // 返回封装的数据
    private T data;

    public static final boolean SUCCESS = true;
    public static final boolean FAILED = false;
    public static final Integer success = 20000;
    public static final Integer failed = 20000;
    public static final String NO_MESSAGE = "NO_MESSAGE";
    public static final String NO_DATA = "NO_DATA";

    /**
     * @param data  要封装的数据
     * @param <T>   泛型，封装数据的类型
     * @return  返回成功后带数据的封装类
     */
    public static <T> ResultJsonToHtmlUtil<T> successWithData(T data){
        return new ResultJsonToHtmlUtil<>(SUCCESS,success,NO_MESSAGE,data);
    }

    /**
     * @param <E>
     * @return  返回成功不带数据的封装类
     */
    public static <E> ResultJsonToHtmlUtil<E> successWithOutOfData(){
        return new ResultJsonToHtmlUtil<>(SUCCESS,success,NO_MESSAGE,null);
    }


    /**
     * @param errorMessage  失败时，出现的异常信息的封装
     * @param <E>
     * @return      失败时，返回不带数据的，封装了异常信息
     */
    public static <E> ResultJsonToHtmlUtil<E> failedWithErrorMessage(String errorMessage){
        return new ResultJsonToHtmlUtil<>(FAILED,failed,errorMessage,null);
    }


}
