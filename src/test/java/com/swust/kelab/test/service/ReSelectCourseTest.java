package com.swust.kelab.test.service;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.swust.kelab.domain.Course;
import com.swust.kelab.domain.User;
import com.swust.kelab.model.BbsPostModel;
import com.swust.kelab.model.CommonQuery;
import com.swust.kelab.model.CourseSelectModel;
import com.swust.kelab.model.PageData;
import com.swust.kelab.model.QueryData;
import com.swust.kelab.model.SelectCourseModel;
import com.swust.kelab.service.SelectCourseService;
import com.swust.kelab.service.StudentService;
import com.swust.kelab.service.UserService;

public class ReSelectCourseTest {
 private SelectCourseService selectCourseService;
 private UserService userService;
    
    @Before
    public void init() {
        ApplicationContext ctx = 
                new ClassPathXmlApplicationContext(
                        new String[]{"classpath:spring/applicationContext.xml",
                                     "classpath:spring/dao.xml",
                                     "classpath:spring/service.xml"});
        selectCourseService = ctx.getBean(SelectCourseService.class);
        userService = ctx.getBean(UserService.class);
    }
    
    @Test
    public void viewCourseSelectList(){
        CommonQuery query=new CommonQuery();
        //query.setSearchType(0);
        //query.setSearchWord("ello");
        int courseId=0;
        int departId=0;
        int gradeId=0;
        int classId=0;
        int userId=0;
        int ismajor=0;
        int passOrNot=-1;
        int confirmOrNot=0;
        int courseYear=1;
        int courseTerm=0;
        int courseTermPhase=0;
        int courseArtScience=0;
        String courseType="XII";
        QueryData staResult=selectCourseService.viewCourseSelectList(
                query, courseId, departId, gradeId, classId,
                userId,ismajor,passOrNot,confirmOrNot,courseYear,courseTerm,
                courseTermPhase,courseArtScience,courseType);
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
                List<CourseSelectModel> ttsms=(List<CourseSelectModel>)pageData.getData();
                System.out.println(ttsms.size());
                for (CourseSelectModel ttsm : ttsms) {
                    System.out.println(ttsm.getStuName()+"\t"+
                            ttsm.getStuNum()+"\t"+
                            ttsm.getSchoolName()+"\t"+
                            ttsm.getStuGrade()+"\t"+
                            ttsm.getStuClass()+"\t"+
                            ttsm.getCourseName()+"\t"+
                            ttsm.getTotalScore()+"\t"+
                            ttsm.getFormarttedTime());
                }
            }
        }
        System.out.println("end");
    }
    
    //@Test
    public void updateTotalScoreTest(){
        int userId=38847;
        int courId=59;
        selectCourseService.updateCourseTotalScore(
                userId, courId);
    }
    //@Test
    public void updateAllTotalScore() throws Exception{
    	List<User> studentList = userService.viewAllStudent();
    	for (User user : studentList) {
    		int userId=user.getUserId();
            int courId=0;
            List<Course> courses = selectCourseService.viewSelectedCourses(userId);

    		for (Course course : courses)
    		{
    			courId = course.getCourId().intValue();
    			selectCourseService.updateCourseTotalScore(
    	                userId, courId);
    		}
		}
    }
}
