package com.swust.kelab.web.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.swust.kelab.model.CourseModel;
import com.swust.kelab.recom.RecomService;
import com.swust.kelab.web.json.JsonAndView;

@Controller
public class RecomCourseController {

	@Autowired
	private RecomService recomService;
	
	@RequestMapping(value="/courseReom",method = RequestMethod.POST)
	public JsonAndView courseReom(HttpServletRequest request,HttpServletResponse respons){
		JsonAndView jv = new JsonAndView();
		 HttpSession session = request.getSession();
         Integer userId=(Integer)(session.getAttribute("userId"));
         if(userId!=null){
        	 List<CourseModel> recom = recomService.getUserRecomCourse(userId);
        	 jv.addData("result", recom);
         }else{
        	 List<CourseModel> recom = recomService.getIndexCourseRecom();
        	 jv.addData("result", recom);
         }
		return jv;
	}
	
	@RequestMapping(value="/rate",method = RequestMethod.POST)
	public JsonAndView rate(HttpServletRequest request,HttpServletResponse respons,
			@RequestParam(value="courseId",required=true) int courseId,@RequestParam(value="rate",required=true) double rate){
		JsonAndView jv = new JsonAndView();
		 HttpSession session = request.getSession();
         Integer userId=(Integer)(session.getAttribute("userId"));
         if(userId!=null){
        	 int id = recomService.insertUserRate(userId, courseId, rate);
        	 jv.addData("result", id);
         }else{
        	 jv.setRet(false);
        	 jv.setErrcode(404);
        	 jv.setErrmsg("当前userId没找到");
         }
		return jv;
	}
}
