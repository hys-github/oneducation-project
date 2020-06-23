package com.hys.easyExcel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.hys.eduService.entity.VO.TestExcelEntity;

import java.util.Map;

/**
 * @Auth 86191
 * @Date 2020/4/15
 *          进行读excel操作，必须要加入监听器
 */
public class TestExcelReadListener extends AnalysisEventListener<TestExcelEntity> {

    @Override
    // 这个方法就是读excel表格的默认执行方法
    // 且是一行一行的读excel表格操作
    // 其中TestExcelEntity为AnalysisEventListener中的泛型实体类，封装了所读excel表格中的每一行信息
    public void invoke(TestExcelEntity testExcelEntity, AnalysisContext analysisContext) {

        // 一般都是在这里进行数据库存储的操作，将数据一行一行的存储到数据库中
        System.err.println(testExcelEntity);

    }

    @Override
    // 读取表格的表头等信息
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {

        System.out.println(headMap);

    }

    @Override
    // 读取完成之后做的操作
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
