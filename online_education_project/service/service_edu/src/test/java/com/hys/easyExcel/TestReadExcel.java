package com.hys.easyExcel;

import com.alibaba.excel.EasyExcel;
import com.hys.eduService.entity.VO.TestExcelEntity;

/**
 * @Auth 86191
 * @Date 2020/4/15
 */
public class TestReadExcel {
    public static void main(String[] args) {

        String excelPath="D:\\write_excel.xlsx";

        EasyExcel.read(excelPath, TestExcelEntity.class,new TestExcelReadListener()).sheet().doRead();


    }
}
