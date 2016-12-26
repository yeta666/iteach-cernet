package com.swust.kelab.test.service;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.swust.kelab.domain.Course;
import com.swust.kelab.model.CommonQuery;
import com.swust.kelab.model.CourseLearnModel;
import com.swust.kelab.model.CourseStatisticModel;
import com.swust.kelab.model.LearningProgressModel;
import com.swust.kelab.model.PageData;
import com.swust.kelab.model.PlatformStatisticModel;
import com.swust.kelab.model.QueryData;
import com.swust.kelab.model.TeacherTutorshipStatisticModel;
import com.swust.kelab.service.CourseStatisticService;
import com.swust.kelab.service.SelectCourseService;

public class CourseStatisticServiceTest {
    private CourseStatisticService courseStatisticService;
    
    private SelectCourseService selectCourseService;
    
    @Before
    public void init() {
        ApplicationContext ctx = 
                new ClassPathXmlApplicationContext(
                        new String[]{"classpath:spring/applicationContext.xml",
                                     "classpath:spring/dao.xml",
                                     "classpath:spring/service.xml"});
        courseStatisticService = ctx.getBean(CourseStatisticService.class);
        selectCourseService = ctx.getBean(SelectCourseService.class);
    }
    
    //@Test
    public void staCourseLearnByStu() {        
        if(courseStatisticService != null) {
            int stuId=3;
            //获取相应的课程统计信息
            List<LearningProgressModel> staResult=courseStatisticService
                    .staCourseLearnByStu(stuId);
            
            if(staResult!=null&&staResult.size()>0){
                System.out.println(staResult.size());
                for (LearningProgressModel courseStatisticModel : staResult) {
                    System.out.println(courseStatisticModel.getCourseName()+"\t"+
                            courseStatisticModel.getLearnTimeScore()+"\t"+
                            courseStatisticModel.getLearnNumScore()+"\t"+
                            courseStatisticModel.getBbsDiscussScore());
                }
            }
            System.out.println("end");
        }
    }
    
    //@Test
    public void staPlatformApplication(){
        CommonQuery query=new CommonQuery();
        QueryData staResult=courseStatisticService.staPlatformApplication(query);
        
        if(staResult!=null){
            System.out.println("totalCount:"+staResult.getTotalCount());
            System.out.println("totalPage:"+staResult.getTotalPage());
            List<PageData> datas=staResult.getPageData();
            for (PageData pageData : datas) {
                System.out.println("pageNum:"+pageData.getPage());
                List<PlatformStatisticModel> psms=(List<PlatformStatisticModel>)pageData.getData();
                for (PlatformStatisticModel psm : psms) {
                    System.out.println(psm.getSchoolName()+"\t"+
                            psm.getTeacherNum()+"\t"+
                            psm.getStudentNum()+"\t"+
                            psm.getTotalLearningNum()+"\t"+
                            psm.getTotalLearningTime()+"\t"+
                            psm.getPostNum()+"\t"+
                            psm.getReplyNum());
                }
            }
        }
        System.out.println("end");
    }
    
    //@Test
    public void staTeacherTutorshipTest(){
        CommonQuery query=new CommonQuery();
        int departId=-1;
        int courseId=0;
        String teacherName="";
        QueryData staResult=courseStatisticService.staTeacherTutorship(query,departId,courseId,teacherName);
        if(staResult!=null){
            System.out.println("totalCount:"+staResult.getTotalCount());
            System.out.println("totalPage:"+staResult.getTotalPage());
            List<PageData> datas=staResult.getPageData();
            if(datas==null){
                System.out.println("空数据");
                return;
            }
            for (PageData pageData : datas) {
                System.out.println("pageNum:"+pageData.getPage());
                List<TeacherTutorshipStatisticModel> ttsms
                        =(List<TeacherTutorshipStatisticModel>)
                        pageData.getData();
                System.out.println(ttsms.size());
                for (TeacherTutorshipStatisticModel ttsm : ttsms) {
                    System.out.println(ttsm.getTeacherName()+"\t"+
                            ttsm.getSchoolName()+"\t"+
                            ttsm.getCourseName()+"\t"+
                            ttsm.getCourseCode()+"\t"+
                            ttsm.getStudentNum()+"\t"+
                            ttsm.getPostNum()+"\t"+ttsm.getReplyNum());
                }
            }
        }
        System.out.println("end");
    }
    
    //@Test
    public void staLearningProgressTest(){
        int userId=1;
        List<LearningProgressModel> staResult=courseStatisticService
                .staLearningProgress(userId);
        if(staResult!=null&&staResult.size()>0){
            for (LearningProgressModel lpm : staResult) {
                System.out.println(lpm.getCourseName()+"\t"
                        +lpm.getMassedLearnScore()+"\t"
                        +lpm.getLearnNumScore()+"\t"
                        +lpm.getLearnTimeScore()+"\t"
                        +lpm.getBbsDiscussScore()+"\t"                       
                        +lpm.getTestScore()+"\t"
                        +lpm.getSubAssessScore()+"\t"
                        +lpm.getTotalScore()+"\t");
            }
        }
        System.out.println("end");
    }
    
    //@Test
    public void staPlatformTotalInforTest(){
        Map staResult=courseStatisticService.staPlatformTotalInfor();
        if(staResult!=null){
            System.out.println("onlineUserNum:"+staResult.get("onlineUserNum"));
            System.out.println("teacherNum:"+staResult.get("teacherNum"));
            System.out.println("courseNum："+staResult.get("courseNum"));
            System.out.println("resourceNum："+staResult.get("resourceNum"));
            System.out.println("studentNum:"+staResult.get("studentNum"));
            System.out.println("learnNum:"+staResult.get("learnNum"));
            System.out.println("learnTime:"+staResult.get("learnTime"));
            System.out.println("bbsNum:"+staResult.get("bbsNum"));
        }
        System.out.println("end");
    }
    
    @Test
    public void staCourseLearningListTest(){
        CommonQuery query=new CommonQuery();
        int departId=0;
        int courseId=0;
        int noStuFilter=0;
        QueryData staResult=courseStatisticService.staCourseLearningState(query, departId, courseId,0,noStuFilter);
        if(staResult!=null){
            System.out.println("totalCount:"+staResult.getTotalCount());
            System.out.println("totalPage:"+staResult.getTotalPage());
            List<PageData> datas=staResult.getPageData();
            if(datas==null){
                System.out.println("空数据");
                return;
            }
            for (PageData pageData : datas) {
                System.out.println("pageNum:"+pageData.getPage());
                List<CourseLearnModel> ttsms
                        =(List<CourseLearnModel>)
                        pageData.getData();
                System.out.println(ttsms.size());
                for (CourseLearnModel ttsm : ttsms) {
                    System.out.println(ttsm.getCourseId()+"\t"+
                            ttsm.getCourseName()+"\t"+
                            ttsm.getCourseCode()+"\t"+
                            ttsm.getSelectNum()+"\t"+
                            ttsm.getOnlineNum());
                }
            }
        }
        System.out.println("end");
    }
}
