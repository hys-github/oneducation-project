package com.hys.easyExcel;

import com.alibaba.excel.EasyExcel;
import com.hys.eduService.entity.VO.TestExcelEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auth 86191
 * @Date 2020/4/15
 *
 *      实现写操作，也就是创造一个excel表格
 */
public class TestWriteExcel {

    public static void main(String[] args) {

        // 创造将数据写入到的路径文件中,当这个文件存在时，复写（自动删除在重写）
        String excelPath="D:\\write_excel.xlsx";

        List<TestExcelEntity> data=data();

        // 执行写操作：write中的两个参数：第一个参数写入excel的文件路径；第二个参数导入实体类的分装属性
        // sheet：在excel中的sheet
        // 第一种方法简介且不需要关闭流
        EasyExcel.write(excelPath,TestExcelEntity.class).sheet("学生信息列表").doWrite(data);

        // 第二种方法麻烦，还必须关闭流
//        ExcelWriter excelWrite = EasyExcel.write(excelPath, TestExcelEntity.class).build();
//        WriteSheet writeSheet = EasyExcel.writerSheet("学生信息列表").build();
//        excelWrite.write(data,writeSheet);
//        excelWrite.finish();

    }
    
    // 循环设置要添加的数据，最终封装到list集合中
    private static List<TestExcelEntity> data() {
        List<TestExcelEntity> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            TestExcelEntity data = new TestExcelEntity();
            data.setId(i);
            data.setName("张三"+i);
            data.setAge(18+i);
            list.add(data);
        }
        return list;
    }
    
    
}
