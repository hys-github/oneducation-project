package com.hys.eduService;

import com.hys.eduService.entity.PO.EduTeacher;
import com.hys.eduService.mapper.EduTeacherMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Auth 86191
 * @Date 2020/4/10
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootMainClassTest {

    @Autowired
    EduTeacherMapper EduTeacherMapper;

    @Test
    public void insertEduTeacher(){

        for (int i = 0; i < 10; i++) {

            EduTeacher EduTeacher = new EduTeacher();

            EduTeacher.setName("人生"+i);
            EduTeacher.setAvatar("几乎呼唤"+i);
            EduTeacher.setCareer("就哈佛哈hi和"+i);
            EduTeacher.setIntroduce("几乎好的"+i);
            EduTeacher.setLevel(i);
            EduTeacher.setSort(i);

            EduTeacherMapper.insert(EduTeacher);
        }
    }


}
