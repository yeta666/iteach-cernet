package com.swust.kelab.test.service;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.swust.kelab.domain.CourseCategory;
import com.swust.kelab.model.BbsPostModel;
import com.swust.kelab.model.CommonQuery;
import com.swust.kelab.model.CourseCategoryModel;
import com.swust.kelab.model.PageData;
import com.swust.kelab.model.QueryData;
import com.swust.kelab.service.CourseCategoryService;

public class CourseCategoryServiceTest {
    private CourseCategoryService courseCategoryService;
    
    @Before
    public void init() {
        ApplicationContext ctx = 
                new ClassPathXmlApplicationContext(
                        new String[]{"classpath:spring/applicationContext.xml",
                                     "classpath:spring/dao.xml",
                                     "classpath:spring/service.xml"});
        courseCategoryService = ctx.getBean(CourseCategoryService.class);
    }
    
   // @Test
    public void viewCourseCateListTest(){
        CommonQuery query=new CommonQuery();
        QueryData qd=courseCategoryService.viewCourseCateList(query);
        if(qd!=null){
            System.out.println("totalCount:"+qd.getTotalCount());
            System.out.println("totalPage:"+qd.getTotalPage());
            List<PageData> datas=qd.getPageData();
            if(datas==null){
                System.out.println("空数据");
                return;
            }
            for (PageData pageData : datas) {
                System.out.println("pageNum:"+pageData.getPage());
                List<CourseCategoryModel> ttsms=(List<CourseCategoryModel>)pageData.getData();
                System.out.println(ttsms.size());
                for (CourseCategoryModel ttsm : ttsms) {
                    System.out.println(ttsm.getCocaName()+"\t"+
                            ttsm.getCocaCode()+"\t"+
                            ttsm.getCreatorName()+"\t"+
                            ttsm.getCocaDescirbe()+"\t"+
                            ttsm.getCocaState()+"\t"+
                            ttsm.getFormatCreateTime()+"\t"+
                            ttsm.getCourseNum()
                           );
                }
            }
        }
        System.out.println("end");
    }
    
    //@Test
    public void createCourseCateTest(){
        CourseCategory cc=new CourseCategory();
        cc.setCocaCode("125");
        cc.setCocaCreateUserid(2);
        cc.setCocaDescirbe("hahaha");
        cc.setCocaName("生活哲学");
        cc.setCocaState(1);
        System.out.println(courseCategoryService.createCourseCate(cc));
    }
    
    //@Test
    public void delCourseCatesTest(){
        String cateIds="5";
        System.out.println(courseCategoryService.delCourseCates(cateIds));
    }
    
    //@Test
    public void modifyCourseCateTest(){
        CourseCategory cc=new CourseCategory();
        cc.setCocaId(3);
        cc.setCocaCode("125");
        cc.setCocaDescirbe("hehehe");
        cc.setCocaName("生活哲学");
        cc.setCocaState(1);
        System.out.println(courseCategoryService.modifyCourseCate(cc));
    }
    
    @Test
    public void viewAllCatesTest(){
        int isOpen=0;
        List<CourseCategory> result=courseCategoryService
                .viewAllCourseCates(isOpen);
        if(result!=null){
            for (CourseCategory cc : result) {
                System.out.println(cc.getCocaId()
                        +"\t"+cc.getCocaName());
            }
        }
    }
}
