package com.swust.kelab.test.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.swust.kelab.model.ClasModel;
import com.swust.kelab.service.GradeInfoService;

public class GradeClassTest {

            private GradeInfoService gradeInfoService;

    @Before
    public void init() {
        ApplicationContext ctx = 
                new ClassPathXmlApplicationContext(
                        new String[]{"classpath:spring/applicationContext.xml",
                                "classpath:spring/dao.xml",
                        "classpath:spring/service.xml"});
        gradeInfoService = ctx.getBean(GradeInfoService.class);
    }
    
    @Test
    public void test(){
//        Grade grade = new Grade();
//        grade.setGradId(42);
//         grade.setGradDepaId(3);
//         grade.setGradName("2021");
//       try {
//           gradeInfoService.updateGrade(grade);
//      } catch (Exception e) {
//             // TODO Auto-generated catch block
//            e.printStackTrace();
//         }
//        Clas clas = new Clas();
//        clas.setClasId(196);
//        clas.setClasGradId(3);
//        clas.setClasName("superJoner");
//        clas.setClasRemark("superclass");
//        String[] str = {"196"};
//        try {
//            gradeInfoService.deleteClass();
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        //CommonQuery commonquery =new CommonQuery();
        //gradeInfoService.selectAllClass(2, 2, commonquery);
         ClasModel clas=gradeInfoService.selectClass(1);
         System.out.println(clas.getGradeName());
         System.out.println(clas.getSchoolName());
         
         
    }

}
