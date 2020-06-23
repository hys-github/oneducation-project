package com.hys.eduService.controller.daemon;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hys.eduService.entity.PO.EduTeacher;
import com.hys.eduService.entity.VO.TeacherPageVO;
import com.hys.eduService.service.EduTeacherService;
import com.hys.utils.ExceptionPrintLogToFileUtil;
import com.hys.utils.ResultJsonToHtmlUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hys
 * @since 2020-04-09
 */
@Api(description = "教师管理")
@RestController
@RequestMapping("/eduService/teacher")
@Slf4j
@CrossOrigin(origins = "*")
public class EduTeacherController {

    @Autowired
    EduTeacherService eduTeacherService;

    private static final Logger logger= LoggerFactory.getLogger(EduTeacherController.class);

    /**
     * @return
     *      查询所有的教师
     */
    @ApiOperation(value = "查询所有教师列表")
    @GetMapping("/query/all/teachers")
    public ResultJsonToHtmlUtil<List<EduTeacher>> getAllTeacher(){

        List<EduTeacher> EduTeachers = eduTeacherService.list(null);

        return ResultJsonToHtmlUtil.successWithData(EduTeachers);
    }

    /**
     * @param id
     * @return
     *      通过教师的id删除教师
     */
    @ApiOperation(value ="通过id删除教师")
    @DeleteMapping("/delete/teacher/{id}")
    public ResultJsonToHtmlUtil<String> deleteTeacherById(@ApiParam(name = "id",value = "讲师的主键id",required = true) @PathVariable("id")String id){
        try{
            boolean b = eduTeacherService.removeById(id);

            return ResultJsonToHtmlUtil.successWithOutOfData();

        }catch (Exception e){
            e.printStackTrace();

            // log.error(e.getMessage());
            log.error(ExceptionPrintLogToFileUtil.getMessage(e));

            return ResultJsonToHtmlUtil.failedWithErrorMessage(e.getMessage());
        }
    }

    /**
     * @param pageNum       第几页
     * @param pageSize      每页的条数
     * @param teacherPageVO 多条件查询
     * @return      返回查询符合条件的教师集合
     */
    @ApiOperation(value ="查询教师分页" )
    @PostMapping("/query/teacher/page/{pageNum}/{pageSize}")
    public ResultJsonToHtmlUtil<Map<String,Object>> getTeacherPage(
            @ApiParam(name = "pageNum",value = "查询第几页",required = true)@PathVariable("pageNum")Integer pageNum,
            @ApiParam(name="pageSize",value = "每页的条数",required = true)@PathVariable("pageSize") Integer pageSize,
            @RequestBody(required = false)TeacherPageVO teacherPageVO){

        try {

            Page<EduTeacher> teacherPOPage=new Page<>(pageNum,pageSize);

            QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();

            String name = teacherPageVO.getName();
            Integer level = teacherPageVO.getLevel();
            String beginTime = teacherPageVO.getBeginTime();
            String endTime = teacherPageVO.getEndTime();

            if (!StringUtils.isEmpty(name)) {
                wrapper.like("name",name);
            }
            if (!StringUtils.isEmpty(level)) {
                wrapper.eq("level",level);
            }
            if (!StringUtils.isEmpty(beginTime)) {
                wrapper.ge("create_time",beginTime);
            }
            if (!StringUtils.isEmpty(endTime)) {
                wrapper.le("create_time",endTime);
            }

            // 进行排序操作,降序排列
            wrapper.orderByDesc("create_time");

            eduTeacherService.page(teacherPOPage,wrapper);

            List<EduTeacher> EduTeacherList = teacherPOPage.getRecords();
            long totalNum = teacherPOPage.getTotal();

            Map<String,Object> map=new HashMap<>();

            map.put("totalNum",totalNum);
            map.put("eduTeacherList",EduTeacherList);

            return ResultJsonToHtmlUtil.successWithData(map);
        }catch (Exception e){

            e.printStackTrace();
            // log.error(e.getMessage());
            log.error(ExceptionPrintLogToFileUtil.getMessage(e));
            return ResultJsonToHtmlUtil.failedWithErrorMessage(e.getMessage());
        }

    }

    /**
     * @param eduTeacher 从表单中传过来的teacher对象
     * @return  成功，返回不带数据；失败，返回失败信息
     *      添加教师功能
     */
    @ApiOperation(value = "添加教师")
    @PostMapping("/add/teacher")
    public ResultJsonToHtmlUtil<String> addEduTeacher(@RequestBody EduTeacher eduTeacher){
        
        try {

            eduTeacherService.save(eduTeacher);

            return ResultJsonToHtmlUtil.successWithOutOfData();

        }catch(Exception e) {
            e.printStackTrace();
            // log.error(e.getMessage());
            log.error(ExceptionPrintLogToFileUtil.getMessage(e));
            return ResultJsonToHtmlUtil.failedWithErrorMessage(e.getMessage());
        }
    }

    /**
     * @param id    教师的主键
     * @return      成功，返回满足条件的teacher对象；失败，返回异常信息
     *          查询教师通过主键id
     */
    @ApiOperation(value="查询教师通过主键id")
    @GetMapping("/query/teacher/by/id/{id}")
    public ResultJsonToHtmlUtil<EduTeacher> queryTeacherById(
            @ApiParam(name = "id",value = "教师主键id",required = true)@PathVariable("id")String id){

        try{

            EduTeacher eduTeacher = eduTeacherService.getById(id);

            return ResultJsonToHtmlUtil.successWithData(eduTeacher);
        }catch (Exception e){
            e.printStackTrace();
            // log.error(e.getMessage());
            log.error(ExceptionPrintLogToFileUtil.getMessage(e));
            return ResultJsonToHtmlUtil.failedWithErrorMessage(e.getMessage());
        }

    }

    /**
     * @param eduTeacher    传过来的教师对象
     * @return  成功，返回不带数据；失败，返回异常信息
     *      修改教师的信息
     */
    @ApiOperation(value = "教师的修改功能")
    @PostMapping("/update/teacher")
    public ResultJsonToHtmlUtil<String> updateTeacher(@RequestBody EduTeacher eduTeacher){

        try{
            eduTeacherService.updateById(eduTeacher);

            return ResultJsonToHtmlUtil.successWithOutOfData();
        }catch (Exception e){
            e.printStackTrace();
            // log.error(e.getMessage());
            log.error(ExceptionPrintLogToFileUtil.getMessage(e));
            return ResultJsonToHtmlUtil.failedWithErrorMessage(e.getMessage());
        }

    }


}

