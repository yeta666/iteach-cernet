package com.swust.kelab.web.controller;

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.swust.kelab.domain.Course;
import com.swust.kelab.service.CourseService;
import com.swust.kelab.web.json.JsonAndView;

@Controller
@RequestMapping("/teacher")
public class TeacherController {
    
	@Autowired
    private CourseService courseService;

	@RequestMapping(value = "/registerCourse", method = RequestMethod.POST)
    public JsonAndView searchOneCourse(@RequestParam("courseName") String courseName
    		,@RequestParam("userId") Integer userId,@RequestParam("courseCore") String courseCore) {
        JsonAndView jv = new JsonAndView();
        if(userId==null)
        {
        	jv.addData("insertResult", "error");
        	return jv;
        }
        Course c = new Course();
        c.setCourTepaId(1);
		c.setCourName(courseName);
		c.setCourCode(courseCore);
		c.setCourCreateUserid(userId);
		c.setCourCredit(1.2f);
		c.setCourVerify(0);
		c.setCourCreateTime(new Date());
		
		
        Map<String, Object> info = null;
        try {
        	info = courseService.registerCourse(c);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jv.addData("insertResult",info);
    }
	
	/**
     * 查询某个老师的所有所受课程的选课人数
     * @param userId
     * @return
     */
	@RequestMapping(value = "/getTeacherCourseInfo", method = RequestMethod.POST)
    public JsonAndView searchOneCourse(Integer userId) {
        JsonAndView jv = new JsonAndView();
        return jv.addData("data", courseService.viewAllTeacherCourseInfo(userId));
    }
}
