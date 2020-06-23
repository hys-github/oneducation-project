package com.hys.eduService.config;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hys.eduService.entity.PO.EduSubject;
import com.hys.eduService.entity.VO.excel.SubjectExcelVO;
import com.hys.eduService.service.EduSubjectService;

import java.util.Map;

/**
 * @Auth 86191
 * @Date 2020/4/15
 *
 *      读取excel表格的监听器
 *          监听器不能交给spring管理，所以不能直接直接注入任何spring管理的组件
 *          在这个监听器上不能加入@Component,@Service等注入到spring中的注解
 */
public class SubjectExcelListener extends AnalysisEventListener<SubjectExcelVO> {

    /*
            不能直接注入，因为这个监听器不能注入到spring中，所以不能直接自动使用spring中的任何组件
            只能手动创建,因为要做添加到数据库中，所以这个属性必须要
        @Autowired
        private EduSubjectService eduSubjectService;
    */

    public EduSubjectService eduSubjectService;

    public SubjectExcelListener(){}

    public SubjectExcelListener(EduSubjectService eduSubjectService){
        this.eduSubjectService=eduSubjectService;
    }

    @Override
    // 这个回调方法实现了接受excel一行一行读时的数据，封装在定义的实体类中
    // 在这个方法中进行存入数据库中的操作
    public void invoke(SubjectExcelVO data, AnalysisContext context) {

        // 从数据库中通过一级课程名查询出一级课程分类的信息
        EduSubject existOneSubject = this.judgeOneSubjectName(eduSubjectService, data.getOneSubjectName());

        // 如果一级课程名不存在，则添加到数据库中
        if(existOneSubject==null){
           existOneSubject=new EduSubject();

            existOneSubject.setTitle(data.getOneSubjectName());
            existOneSubject.setParentId("0");

            // 将这个课程信息存储到数据库中
            // 注意：mybatis-plus这个插入操作实现了 keyProperty="" useGeneratedKeys="true"自动返回插入的数据（包括id)
            this.eduSubjectService.save(existOneSubject);
        }

        // 从数据库中通过二级课程名查询出二级课程分类的信息
        EduSubject existTwoSubject = this.judgeTwoSubjectName(eduSubjectService, data.getTwoSubjectName(), existOneSubject.getId());

        // 判断从文件中传过来的二级课程名是否存在
        if(existTwoSubject==null){
            existTwoSubject=new EduSubject();
            existTwoSubject.setTitle(data.getTwoSubjectName());
            existTwoSubject.setParentId(existOneSubject.getId());

            this.eduSubjectService.save(existTwoSubject);
        }

    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {


    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }

    /**
     * @param eduSubjectService : 操作数据库的service类
     * @param oneSubjectName    ：传过来的一级分类名
     * @return  返回mysql中是否有这个一级课程分类名的信息
     *      条件查询：通过课程名查询数据库中是否存在此一级课程
     */
    private EduSubject judgeOneSubjectName(EduSubjectService eduSubjectService,String oneSubjectName){

        QueryWrapper<EduSubject> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("title",oneSubjectName);
        queryWrapper.eq("parent_id","0");

        EduSubject eduSubject = eduSubjectService.getOne(queryWrapper);

        return eduSubject;
    }

    /**
     * @param eduSubjectService : 操作数据库的service类
     * @param twoSubjectName    ：传过来的二级分类名
     * @return  返回mysql中是否有这个二级课程分类名的信息
     *      条件查询：通过课程名查询数据库中是否存在此二级课程
     */
    private EduSubject judgeTwoSubjectName(EduSubjectService eduSubjectService,String twoSubjectName,String parentId){

        QueryWrapper<EduSubject> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("title",twoSubjectName);
        queryWrapper.eq("parent_id",parentId);

        EduSubject eduSubject = eduSubjectService.getOne(queryWrapper);

        return eduSubject;
    }



}
