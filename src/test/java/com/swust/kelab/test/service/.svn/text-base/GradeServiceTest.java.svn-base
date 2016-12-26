package com.swust.kelab.test.service;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.swust.kelab.model.ClassModel;
import com.swust.kelab.model.GradeClassModel;
import com.swust.kelab.service.GradeService;

public class GradeServiceTest {
    private GradeService gradeService;
    
    @Before
    public void init() {
        ApplicationContext ctx = 
                new ClassPathXmlApplicationContext(
                        new String[]{"classpath:spring/applicationContext.xml",
                                     "classpath:spring/dao.xml",
                                     "classpath:spring/service.xml"});
        gradeService = ctx.getBean(GradeService.class);
    }
    
    @Test
    public void viewGradeClassByDepartTest(){
        int departId=1;
        List<GradeClassModel> result=gradeService
                .viewGradeClassByDepart(departId);
        if(result==null||result.size()==0){
            System.out.println("error null!");
        }else{
            for (GradeClassModel gcm : result) {
                System.out.println(gcm.getGradId()+"\t"+gcm.getGradName());
                List<ClassModel> classes=gcm.getClasses();
                if(classes!=null&&classes.size()>0){
                    for (ClassModel cm : classes) {
                        System.out.println("\t\t"+cm.getClasId()+"\t"
                                +cm.getClasName());
                    }
                }
            }
        }
    }
}
