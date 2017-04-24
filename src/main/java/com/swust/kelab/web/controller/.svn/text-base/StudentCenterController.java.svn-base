package com.swust.kelab.web.controller;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.swust.kelab.domain.Course;
import com.swust.kelab.domain.NoticeAnnouncement;
import com.swust.kelab.domain.User;
import com.swust.kelab.model.BBSReplyModel;
import com.swust.kelab.model.CommonQuery;
import com.swust.kelab.model.CourseCheckModel;
import com.swust.kelab.model.CourseModel;
import com.swust.kelab.model.QueryData;
import com.swust.kelab.model.SelectCourseModel;
import com.swust.kelab.model.StudentInfoModel;
import com.swust.kelab.recom.RecomService;
import com.swust.kelab.service.CourseService;
import com.swust.kelab.service.LogDBService;
import com.swust.kelab.service.StudentService;
import com.swust.kelab.utils.CookieUtil;
import com.swust.kelab.web.json.JsonAndView;

@Controller
@RequestMapping("/sc")
public class StudentCenterController {
	@Autowired
	private StudentService studentService;

	@Autowired
	private CourseService courseService;

	@Autowired
	private LogDBService logDBService;
	@Autowired
	private RecomService recomService;
	
	private final static double STUDENT_SELECT_COURSE_SCORE = 3;

	/**
	 * 查询学生所有的选课信息
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/student", method = RequestMethod.POST)
	public JsonAndView studentSelect(@RequestParam("userId") int userId) {
		JsonAndView jv = new JsonAndView();
		List<SelectCourseModel> rsc = null;
		try {
			rsc = studentService.studentPage(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jv.addData("result", rsc);

	}

	/**
	 * 查询特定学生的特定课程
	 * 
	 * 
	 * @author zhangxin
	 * @param userId
	 *            学生id
	 * @param courseId
	 *            课程id
	 * @return JsonAndView
	 */
	@RequestMapping(value = "/searchCertainCourse", method = RequestMethod.POST)
	public JsonAndView searchCertainCourse(
			@RequestParam("reselectId") int reselectId) {
		JsonAndView jv = new JsonAndView();
		CourseModel rsc = null;
		try {
			rsc = studentService.studentCentainCourse(reselectId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jv.addData("result", rsc);

	}

	/**
	 * 查询所有课程信息(分页需求)
	 * 
	 * @param query
	 * 
	 * @param request
	 * @param userId
	 *            用户id
	 * @param cateId
	 *            类型id
	 * @param opentTime
	 *            课程的开放选课时间
	 * @param closeTime
	 *            课程的结束选课时间
	 * @return
	 * @author ZhangXin
	 */
	@RequestMapping(value = "/course", method = RequestMethod.POST)
	public JsonAndView studentCourse(CommonQuery query,
			HttpServletRequest request, Integer userId, Integer cateId,
			String openTime, String closeTime) {
		JsonAndView jv = new JsonAndView();
		// Map<String, Object> courses = null;
		Date startDate = null;
		Date closeDate = null;
		try {
			if (openTime != null && !openTime.equals("")) {
				startDate = DateUtils.parseDate(openTime, "yyyy-MM-dd HH");
			}
			if (closeTime != null && !closeTime.equals("")) {
				closeDate = DateUtils.parseDate(closeTime, "yyyy-MM-dd HH");
			}
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			startDate = null;
			closeDate = null;
		}
		User user = CookieUtil.getCookieUser(request);
		QueryData queryData = null;
		try {
			queryData = studentService.selectCourseInfo(query, request
					.getSession().getServletContext().getRealPath("/"), userId,
					cateId, user.getUserDepaId(), startDate, closeDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		jv.addData("totalPage", queryData.getTotalPage());
		jv.addData("totalCount", queryData.getTotalCount());
		return jv.addData("pageData", queryData.getPageData());
	}

	/**
	 * 查询用户信息
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/studentinfo", method = RequestMethod.POST)
	public JsonAndView studentInfo(@RequestParam("userId") int userId) {
		JsonAndView jv = new JsonAndView();
		StudentInfoModel info = null;
		try {
			info = studentService.studentInfo(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jv.addData("result", info);
	}

	/**
	 * 教务处近期通知信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/announcement", method = RequestMethod.POST)
	public JsonAndView announcement() {
		JsonAndView jv = new JsonAndView();
		List<NoticeAnnouncement> annc = null;
		try {
			annc = studentService.announceInfo();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jv.addData("result", annc);
	}

	/**
	 * bbs上回复信息
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/reply", method = RequestMethod.POST)
	public JsonAndView requestReply(@RequestParam("userId") int userId) {
		JsonAndView jv = new JsonAndView();
		List<BBSReplyModel> reply = null;
		try {
			reply = studentService.userReply(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jv.addData("result", reply);
	}

	/**
	 * 退课
	 * 
	 * @param userId
	 * @param courseId
	 * @return
	 */
	@RequestMapping(value = "/deleteSelCou", method = RequestMethod.POST)
	public JsonAndView deleteSelectCourse(
			@RequestParam("courseId") int courseId,
			@RequestParam("userId") int userId, HttpServletRequest request) {
		JsonAndView jv = new JsonAndView();

		Map<String, Object> deletSelCourInfo = null;
		Map<String, Object> id = new HashMap<String, Object>();
		id.put("courseId", courseId);
		id.put("userId", userId);
		id.put("courrentDate", new Date());
		id.put("valid", 2);
		try {
			deletSelCourInfo = studentService.deleteSelectCourceById(id);
			Course courseInfo = courseService
					.queryOneCourseByCourseId(courseId);
			if (courseInfo != null)
				logDBService.insertNewLog(request,
						LogDBService.DELETE_OPERATION, "学习中心", "退选"
								+ courseInfo.getCourName() + "课程");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jv.addData("dropCourseInfo", deletSelCourInfo);
	}

	/**
	 * 选课
	 * 
	 * @param userId
	 * @param courseId
	 * @return
	 */
	@RequestMapping(value = "/selectCourse", method = RequestMethod.POST)
	public JsonAndView selectCourse(@RequestParam("userId") int userId,
			@RequestParam("courseId") int courseId,
			@RequestParam("courseCredit") int courseCredit,
			HttpServletRequest request) {
		JsonAndView jv = new JsonAndView();
		Map<String, Object> selCourInfo = null;
		Map<String, Object> queryDate = new HashMap<String, Object>();
		queryDate.put("userId", userId);
		queryDate.put("courseId", courseId);
		queryDate.put("courseCredit", courseCredit);
		try {
			selCourInfo = studentService.insertSelectCource(queryDate);
			Course courseInfo = courseService
					.queryOneCourseByCourseId(courseId);
			recomService.insertUserRate(userId, courseId, STUDENT_SELECT_COURSE_SCORE);
			if (courseInfo != null)
				logDBService.insertNewLog(request, LogDBService.ADD_OPERATION,
						"学习中心", "选修" + courseInfo.getCourName() + "课程");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jv.addAllData(selCourInfo);
	}

	/**
	 * 通过课程id查找某一个课程
	 * 
	 * @author ZhangXin
	 * @return JsonAndView
	 * @param courseId
	 *            课程ID
	 */
	/*
	 * @RequestMapping(value = "/searchCourseByName", method =
	 * RequestMethod.POST) public JsonAndView
	 * searchCourseByName(@RequestParam("courseName") String courseName) {
	 * JsonAndView jv = new JsonAndView(); Map<String, Object> map = null; try {
	 * map = studentService.requestCourseByName(courseName); } catch (Exception
	 * e) { e.printStackTrace(); } return jv.addData("searchResult", map); }
	 */

	/**
	 * 通过课程id查找某一个课程
	 * 
	 * @author ZhangXin
	 * @return JsonAndView
	 * @param courseId
	 *            课程ID
	 */
	@RequestMapping(value = "/searchOneCourse", method = RequestMethod.POST)
	public JsonAndView searchOneCourse(@RequestParam("courseId") int courseId,
			HttpServletRequest request) {
		JsonAndView jv = new JsonAndView();
		Map<String, Object> map = null;
		try {
			map = studentService.searchOneCourse(courseId, request.getSession()
					.getServletContext().getRealPath("/"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jv.addData("searchResult", map);
	}

	/**
	 * 获取服务器的状态信息
	 * 
	 * @author MrYang
	 * @return JsonAndView
	 */

	@RequestMapping(value = "/serverStatus", method = RequestMethod.POST)
	public JsonAndView serverStatus() {
		JsonAndView jv = new JsonAndView();
		Map<String, Object> map = null;
		try {
			map = studentService.serverStatus();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jv.addData("searchResult", map);
	}

}
