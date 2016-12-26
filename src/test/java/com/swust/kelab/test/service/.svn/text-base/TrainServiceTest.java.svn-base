package com.swust.kelab.test.service;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.swust.kelab.model.CommonQuery;
import com.swust.kelab.model.PageData;
import com.swust.kelab.model.QueryData;
import com.swust.kelab.model.ScoreModel;
import com.swust.kelab.service.CourseService;
import com.swust.kelab.service.TrainService;

public class TrainServiceTest {
    private CourseService courseService;
    private TrainService trainService;

    @Before
    public void init() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext(new String[] {
                "classpath:spring/applicationContext.xml", "classpath:spring/dao.xml", "classpath:spring/service.xml" });
        courseService = ctx.getBean(CourseService.class);
        trainService = ctx.getBean(TrainService.class);
    }

//    @Test
//    public void courseTest() {
//
//        if (courseService != null) {
//            CourseModel trainModel = courseService.viewCourseInfo(2);
//            System.out.println(trainModel.getCourName());
//        }
//    }
//    @Test
//    public void getAllCourse(){
//        //trainService.getAllExamCource();
//        for(ExamCourModel cour : trainService.getAllExamCource()){
//            System.out.println(cour.getCourId());
//            System.out.println(cour.getCourName());
//        }
//    }
    @Test
    public void getAllScore(){
        CommonQuery commonquery = new CommonQuery();
        QueryData data =  trainService.showAllScore(commonquery, 1, 1, 4, 2, 2);
       
}
    }
