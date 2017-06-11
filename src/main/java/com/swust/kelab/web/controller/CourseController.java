package com.swust.kelab.web.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.swust.kelab.domain.Course;
import com.swust.kelab.domain.CourseCategory;
import com.swust.kelab.domain.CourseSelectInformation;
import com.swust.kelab.domain.Department;
import com.swust.kelab.domain.User;
import com.swust.kelab.model.CommonQuery;
import com.swust.kelab.model.CourseCheckModel;
import com.swust.kelab.model.CourseSelectInfoModel;
import com.swust.kelab.model.QueryData;
import com.swust.kelab.service.CourseService;
import com.swust.kelab.service.LogDBService;
import com.swust.kelab.utils.CookieUtil;
import com.swust.kelab.web.json.JsonAndView;

/**
 * 本类包含所有课程相关查询。<br>
 * 本类以/course作为默认路径前缀。<br>
 * 依赖： {@link com.swust.kelab.service.CourseService}<br>
 * <br>
 * 
 * @see com.swust.kelab.service.CourseService
 * @version 1.0
 * @author Easonlian
 */
@Controller()
@RequestMapping("/course")
public class CourseController {
	final Logger logger = LoggerFactory.getLogger(getClass());

	private LogDBService logDBService;

	private CourseService courseService;

	@Resource
    HttpServletRequest request;
	@Autowired
	public void setLogDBService(LogDBService logDBService) {
		this.logDBService = logDBService;
	}

	@Autowired
	public void setCourseService(CourseService courseService) {
		this.courseService = courseService;
	}

	/**
	 * 通过教师id查询他的课程
	 * 
	 * @param request
	 * @author easonlian
	 */
	@RequestMapping(value = "/viewAllCourseByTeacher.do")
	public JsonAndView viewAllCourseByTeacher(HttpServletRequest request) {
		JsonAndView jav = new JsonAndView();
		User user = CookieUtil.getCookieUser(request);
		return jav.addData(
				"myCourseList",
				courseService.viewAllTeacherCourseInfo(user.getUserId()));
				//courseService.viewAllCourseByTeacher(user.getUserId(),user.getUserDepartType()));
	}

	/**
	 * 查询所有课程和id，前台构建option下拉列表框<br>
	 * 访问路径：/handler/course/viewAllCourseNameList.do
	 * 
	 * @return 包含用户所有课程id、课程name的List
	 * @author EasonLian
	 */
	@RequestMapping(value = "/viewAllCourseNameList.do", method = RequestMethod.GET)
	public JsonAndView viewAllCourseList(HttpServletRequest request) {
		JsonAndView jav = new JsonAndView();
		jav.addAllData(courseService.viewAllCourseList(CookieUtil
				.getCookieUser(request)));
		return jav;
	}

	/**
	 * find all the choosed course of the logged student.<br>
	 * to visit : /handler/course/viewCourseList.do<br>
	 * 
	 * @author EasonLian
	 * @param javax
	 *            .servlet.http.HttpServletRequest
	 * @param javax
	 *            .servlet.http.HttpServletResponse
	 * @throws Exception
	 */
	@RequestMapping(value = "/viewCourseList.do", method = RequestMethod.GET)
	public JsonAndView viewCourseList(HttpServletRequest request,
			HttpServletResponse response) {
		User user = CookieUtil.getCookieUser(request);
		return new JsonAndView().addData("courseList", courseService.viewCourseListService(request.getSession().getServletContext()
						.getRealPath("/"),
						user == null ? null : user.getUserId(),
						user == null ? null : user.getUserType(), false));
	}

	/**
	 * find all the existed course .<br>
	 * to visit : /handler/course/viewAllCourseList.do<br>
	 * 
	 * @author EasonLian
	 * @param javax
	 *            .servlet.http.HttpServletRequest
	 * @param javax
	 *            .servlet.http.HttpServletResponse
	 * @throws Exception
	 */
	@RequestMapping(value = "/viewAllCourseList.do", method = RequestMethod.GET)
	public JsonAndView viewAllCourseList(HttpServletRequest request,
			HttpServletResponse response) {
		return new JsonAndView().addData("courseList", courseService
				.viewCourseListService(request.getSession().getServletContext()
						.getRealPath("/"), null, null, true));
	}

	/**
	 * 下拉列表框加载的所有课程名字
	 * 
	 * @author yangzq
	 * @return
	 */
	@RequestMapping(value = "/dropDownCourse", method = RequestMethod.POST)
	public JsonAndView dropDownCourse(Integer userId, Integer departId,
			Integer departType, Integer userType) {
		return new JsonAndView().addData("courseList", courseService
				.dropDownCourse(userId, departId, departType, userType));
	}

	@RequestMapping(value = "/testCourseView", method = RequestMethod.GET)
	public JsonAndView testCourseView(int userId) {
		return new JsonAndView().addData("testCourse",
				courseService.testCourseView(userId));
	}

	/**
	 * 通过部门id，获取其与所有上级部门创建的课程
	 * 
	 * @param departId
	 *            当前部门id,0表示所有
	 * @Param type 0表示不限制论坛开闭状态，1表示查询论坛开放的课程， 2表示查询论坛关闭的课程
	 * @return 课程列表
	 * @see JsonAndView，Course
	 */
	@RequestMapping(value = "/viewCourseListByDepart", method = RequestMethod.POST)
	public JsonAndView viewCourseListByDepartId(int departId, int type) {
		JsonAndView jav = new JsonAndView();
		List<Map> courses = courseService.viewCoursesByDepartId(departId, type);
		if (courses == null) {
			jav.setRet(false);
			jav.setErrcode(1);
			jav.setErrmsg("查询部门课程列表失败！");
		} else {
			jav.addData("courses", courses);
		}
		return jav;
	}

	@RequestMapping(value = "/viewCourseList", method = RequestMethod.POST)
	public JsonAndView viewCourseList(int userId, int departId,
			HttpServletRequest request) {
		JsonAndView jav = new JsonAndView();
		List<Map<String, Object>> courses = courseService.viewCourseListByMentroId(userId, departId);
		logDBService.insertNewLog(request, LogDBService.SELECT_OPERATION, "集中学习", "集中学习中课程列表");
		if (courses == null) {
			jav.setRet(false);
			jav.setErrcode(1);
			jav.setErrmsg("查询部门课程列表失败！");
		} else {
			jav.addData("courses", courses);
		}
		return jav;
	}

	/**
	 * 通过用户id获取对应的课程列表；<br>
	 * 若为学生则获取其选择的课程列表； <br>
	 * 若为教师，则获取其主讲或辅导的课程列表。
	 * 
	 * @param userId
	 *            用户id
	 * @Param type 0表示不限制论坛开闭状态，1表示查询论坛开放的课程， 2表示查询论坛关闭的课程
	 * @return 课程列表
	 * @see JsonAndView
	 * @author 吴岘辉
	 */
	@RequestMapping(value = "/viewCoursesByUser", method = RequestMethod.POST)
	public JsonAndView viewCourseListByUserId(int userId, int type) {
		JsonAndView jav = new JsonAndView();
		List<Map> courses = courseService.viewCoursesByUserId(userId, type);
		if (courses == null) {
			jav.setRet(false);
			jav.setErrcode(1);
			jav.setErrmsg("查询用户的课程列表失败！");
		} else {
			jav.addData("courses", courses);
		}
		return jav;
	}

	/**
	 * 查询课程论坛列表
	 * 
	 * @param query
	 *            查询对象，其中searchType的含义为：1表示课程名， 2表示课程代码，3表示论坛开闭状态，4表示版主名称
	 * @param departId
	 *            课程所属的部门id（针对市级教务员）
	 * @return 课程论坛列表
	 * @see JsonAndView，CommonQuery
	 */
	@RequestMapping(value = "/viewCoursesBbsList", method = RequestMethod.POST)
	public JsonAndView viewCourseBbsList(CommonQuery query, int departId) {
		JsonAndView jav = new JsonAndView();
		QueryData qd = courseService.viewCourseBbsList(query, departId);
		if (qd == null) {
			jav.setRet(false);
			jav.setErrcode(1);
			jav.setErrmsg("查询课程论坛列表失败！");
		} else {
			jav.addData("totalPage", qd.getTotalPage());
			jav.addData("totalCount", qd.getTotalCount());
			jav.addData("pageData", qd.getPageData());
		}
		return jav;
	}

	/**
	 * 更改课程论坛的开闭状态
	 * 
	 * @param courseIds
	 *            课程论坛id，多个用逗号隔开
	 * @param newStatus
	 *            新状态，1表示开启，2表示关闭
	 * @return 操作结果
	 * @see JsonAndView
	 */
	@RequestMapping(value = "/setCourseBbsStatus", method = RequestMethod.POST)
	public JsonAndView setCourseBbsStatus(String courseIds, int newStatus) {
		JsonAndView jav = new JsonAndView();
		String result = courseService.setCourseBbsStatus(courseIds, newStatus);
		if (result != "success") {
			jav.setRet(false);
			jav.setErrcode(1);
			jav.setErrmsg("更改论坛开闭状态失败！");
		}
		return jav;
	}

	/**
	 * 设置课程的主讲教师以及辅导老师,以便老师管理课程
	 * 
	 * @param courseId
	 *            教师ids(一个教师与一个教师之间以英文逗号隔开)
	 * @param addType
	 *            添加教师的教师类型，为1时为主讲教师，为2时为辅讲教师
	 * @param teacherIds
	 * @param request
	 * @return
	 * @author lujoCom
	 */
	@RequestMapping(value = "/setTeacherToCourse", method = RequestMethod.POST)
	public JsonAndView setTeacherToCourse(Integer courseId, Integer addType,
			String teacherIds, HttpServletRequest request) {
		JsonAndView jv = new JsonAndView();

		Map<String, Object> data = new HashMap<String, Object>();
		try {
			data = courseService.setTeacherToCourse(courseId, addType,
					teacherIds);
			Course courseInfo = courseService
					.queryOneCourseByCourseId(courseId);
			if(courseInfo != null){
				logDBService.insertNewLog(request, LogDBService.UPDATA_OPERATION,
						"辅导教师管理", courseInfo.getCourName() + "课程辅导教师列表");
			}
			if (data.get("status").equals("error")) {
				jv.setRet(false);
				jv.setErrmsg((String) data.get("setTeaToCouInfo"));
				return jv;
			}

			jv.setRet(true);
			jv.setData(data);

		} catch (Exception e) {
			e.printStackTrace();
			jv.setRet(false);
			jv.setErrmsg("错误信息" + e.getMessage());
		}

		return jv;
	}

	/**
	 * @param commonQuery
	 *            查询对象，其中searchType的含义为：1表示课程名， 2表示课程代码
	 * @param departId
	 *            学校id
	 * @param departType
	 *            操作人的机构类型
	 * @return
	 */
	@RequestMapping(value = "/viewCourseAndTeahcerInfo", method = RequestMethod.POST)
	public JsonAndView viewCourseAndTeahcerInfo(CommonQuery commonQuery,
			@RequestParam("departId") Integer departId,int departType) {
		JsonAndView jv = new JsonAndView();
		QueryData qd = courseService.findCourseOfTeachersByQuery(commonQuery,
				departId,departType);
		if (qd == null) {
			jv.setRet(false);
			jv.setErrmsg("查询发生异常错误，请稍后再试...");
			return jv;
		}
		jv.setRet(true);
		jv.addData("totalPage", qd.getTotalPage());
		jv.addData("totalCount", qd.getTotalCount());
		jv.addData("pageData", qd.getPageData());

		return jv;
	}

	/**
	 * 课程审批查询，输入课程名称、课程代码（初始化时没有，由审批人填写）、
	 * 考核方式标识、审核情况（通过/不通过）、课程类别、申请人、申请时间等信息具体查询
	 * 
	 * @author Pery
	 * @param query
	 *            其中 searchType 1 表示按课程名称查询，
	 *            <p>
	 *            2按课程代码查询，
	 *            <p>
	 *            3 按审核情况查询
	 * @param departmentId
	 *            要查询课程所属学校id,为0表示查询所有学校
	 * @return 课程结构列表
	 */
	@RequestMapping(value = "/queryCourseOfCheck", method = RequestMethod.POST)
	public JsonAndView queryCourseOfCheck(CommonQuery query, int departmentId) {
		JsonAndView jav = new JsonAndView();
		QueryData qd = courseService.queryCourseOfCheck(query, departmentId);
		if (qd != null) {
			Map result = new HashMap<String, List<Department>>();
			jav.addData("totalPage", qd.getTotalPage());
			jav.addData("totalCount", qd.getTotalCount());
			jav.addData("pageData", qd.getPageData());
		} else {
			jav.setRet(false);
			jav.setErrcode(1);
			jav.setErrmsg("获取课程列表失败！");
		}
		return jav;
	}

	/**
	 * 查询选课信息中的开放时间与关闭时间
	 * 
	 * @param courseId
	 * @param deparId
	 * @return
	 * @author lujoCom
	 */
	@RequestMapping(value = "/findCouSelectInfo", method = RequestMethod.POST)
	public JsonAndView findCourseSelectInfo(Integer courseId, Integer deparId) {
		JsonAndView jv = new JsonAndView();
		CourseSelectInformation csin = new CourseSelectInformation();
		csin.setCsinCourId(courseId);
		csin.setCsinDepaId(deparId);
		Map<String, Object> map = new HashMap<String, Object>();
		List<CourseSelectInfoModel> infoModels = courseService
				.findCourseSelectInfo(csin);
		if (infoModels.size() == 0) {
			jv.setRet(false);
			map.put("courseSelectInfo", "无选课信息");
			jv.setData(map);
			return jv;
		}
		map.put("courseSelectInfo", infoModels);
		jv.setRet(true);
		jv.setData(map);
		return jv;
	}

	/**
	 * 设置选课信息的开放时间以及关闭时间
	 * 
	 * @param csinId
	 * @param openDate
	 * @param closeDate
	 * @param remark
	 * @return
	 * @author lujoCom
	 */
	@RequestMapping(value = "/setTimeOfSelectCourse", method = RequestMethod.POST)
	public JsonAndView setTimeOfSelectCourse(String csinId, Date openDate,
			Date closeDate, String remark) {
		JsonAndView jv = new JsonAndView();
		Map<String, Object> data = courseService.setCourseSelectTime(csinId,
				openDate, closeDate, remark);
		if (data == null) {
			jv.setRet(false);
			jv.setErrmsg("发生未知错误,请稍后再试");
			return jv;
		}
		
		logDBService.insertNewLog(request,
				LogDBService.ADD_OPERATION, "选课时间管理", "添加选课时间");
		jv.setRet(true);
		jv.addAllData(data);

		return jv;
	}

	/**
	 * 删除选课的开放和关闭时间<br>
	 * csinIds格式为: 1,2(最后一个元素后不含逗号)<br>
	 * 参数中的值不能出现出了英文字符逗号以及数字之外的字符，否则返回数据格式不符合条件<br>
	 * 
	 * @param courseIds
	 * @param departIds
	 * @return
	 * @author lujoCom
	 */
	@RequestMapping(value = "/deleteTimeOfSelectCourse", method = RequestMethod.POST)
	public JsonAndView deleteTimeOfSelcetCourse(String csinIds) {
		JsonAndView jv = new JsonAndView();

		if (csinIds == null || csinIds.equals("")) {
			jv.setRet(false);
			jv.setErrmsg("删除失败，未选课选课信息，请选择需要删除的选课信息");
			return jv;
		}

		Map<String, Object> data = courseService
				.deleteCourseSelectTime(csinIds);
		if (data == null) {
			jv.setRet(false);
			jv.setErrmsg("删除时间失败，发生未知错误，请稍后再试!");
			return jv;
		}
		logDBService.insertNewLog(request,
				LogDBService.DELETE_OPERATION, "选课时间管理", "删除选课时间");
		jv.setRet(true);
		jv.setData(data);

		return jv;
	}

	/**
	 * 查询某一审批课程的详情
	 * 
	 * @param courseId
	 * @return
	 */
	@RequestMapping(value = "queryOneCheckCourse", method = RequestMethod.POST)
	public JsonAndView queryOneCheckCourse(int courseId) {
		JsonAndView jv = new JsonAndView();
		CourseCheckModel course = courseService.queryOneCheckCourse(courseId);
		Map<String, Object> map = new HashMap<String, Object>();
		if (course != null) {
			map.put("courseInfo", course);
			jv.setRet(true);
			jv.addAllData(map);
		} else {
			jv.setRet(false);
			jv.setErrmsg("该课程没有查询到!");
		}
		return jv;
	}

	/**
	 * 审核一门课程
	 * 
	 * @param courseId
	 *            被审核的课程id
	 * @param status
	 *            审核状态 ，1为审核通过 ，2为审核拒绝
	 * @param openToAll
	 *            是否对外开放，true（1）表示对外开放，false（0）表示仅对创建部门开放
	 * @param courState
	 *            课程开闭状态
	 * @param request
     *            请求对象，用于记录日志
	 * @return
	 */
	@RequestMapping(value = "checkCouse", method = RequestMethod.POST)
	public JsonAndView checkCouse(int courseId, int status, Boolean openToAll,
			int courState,HttpServletRequest request) {
		JsonAndView jv = new JsonAndView();
		int result = courseService.checkCourse(courseId, status, openToAll,
				courState);
		if (result > 0) {
			jv.setRet(true);
			//日志记录
			String logInfo="ID为："+courseId+"的课程：";
			if(status==1){
			    logInfo+="通过审核";
			}else{
			    logInfo+="未通过审核";
			}
            logDBService.insertNewLog(request, 
                    LogDBService.UPDATA_OPERATION, "课程审批",
                    logInfo);
		} else {
			jv.setRet(false);
			jv.setErrmsg("操作失败");
		}
		return jv;
	}

	/**
	 * 老师选课时间分页查询
	 * 
	 * @author ZhangXin
	 * @param query
	 * @return
	 */
	@RequestMapping(value = "/courseTime", method = RequestMethod.POST)
	public JsonAndView teacherSelectCourseTime(CommonQuery query, Integer school) {
		JsonAndView jv = new JsonAndView();
		if (school != null && school <= 0)
			school = null;
		QueryData queryData = null;
		try {
			queryData = courseService.selectCourseInfo(query, school);
		} catch (Exception e) {
			e.printStackTrace();
		}
		jv.addData("totalPage", queryData.getTotalPage());
		jv.addData("totalCount", queryData.getTotalCount());
		return jv.addData("pageData", queryData.getPageData());
	}

	/**
	 * 课程管理中,查询课程列表
	 * 
	 * @param query
	 *            查询对象,其中searchType:1表示课程名称,2表示课程代码
	 * @param departId
	 *            开设机构id,用于筛选,0表示所有
	 * @param courseCateId
	 *            课程领域id,用于筛选,,0表示所有
	 * @param courseType 
	 *            课程类别,为空表示所有
	 * @return 课程列表
	 * @see JsonAndView,CommonQuery,QueryData
	 */
	@RequestMapping(value = "/courseList", method = RequestMethod.POST)
	public JsonAndView viewCourseListByQuery(CommonQuery query, int departId,
			int courseCateId,String courseType) {
		JsonAndView jav = new JsonAndView();
		QueryData qd = courseService.viewCourseListByQuery(query, departId,
				courseCateId,courseType);
		if (qd == null) {
			jav.setRet(false);
			jav.setErrcode(1);
			jav.setErrmsg("查询课程列表失败！");
		} else {
			jav.addData("totalPage", qd.getTotalPage());
			jav.addData("totalCount", qd.getTotalCount());
			jav.addData("pageData", qd.getPageData());
		}
		return jav;
	}

	/**
	 * 创建课程
	 * 
	 * @param course
	 *            新的课程对象
	 * @param request
	 *            请求对象，用于记录日志
	 * @return 操作结果
	 * @see JsonAndView,Course
	 */
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public JsonAndView createCourse(Course course
	        ,HttpServletRequest request) {
		JsonAndView jav = new JsonAndView();
		String result = courseService.createCourse(course);
		if (result != "success") {
			jav.setRet(false);
			jav.setErrcode(1);
			jav.setErrmsg("新增课程失败！");
		}else{
            //日志记录
            logDBService.insertNewLog(request, 
                    LogDBService.ADD_OPERATION, "课程管理",
                    "新增课程："+course.getCourName());
        }
		return jav;
	}

	/**
	 * 删除课程,可同时删除多个(用逗号隔开)
	 * 
	 * @param courseIds
	 *            课程id字符串
	 * @param request
     *            请求对象，用于记录日志
	 * @return 操作结果
	 * @see JsonAndView
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public JsonAndView deleteCourses(String courseIds
	        ,HttpServletRequest request) {
		JsonAndView jav = new JsonAndView();
		String result = courseService.deleteCourses(courseIds);
		if (result != "success") {
			jav.setRet(false);
			jav.setErrcode(1);
			jav.setErrmsg("部分或全部课程删除失败,相应课程可能在其他地方有使用！");
		}else{
            //日志记录
            logDBService.insertNewLog(request, 
                    LogDBService.DELETE_OPERATION, "课程管理",
                    "删除课程，ID包括："+courseIds);
        }
		return jav;
	}

	/**
	 * 根据id获取课程的详细信息
	 * 
	 * @param courseId
	 *            课程id
	 * @return 包含课程详细信息的map
	 */
	@RequestMapping(value = "/viewDetail", method = RequestMethod.POST)
	public JsonAndView viewCourseDetail(int courseId) {
		JsonAndView jav = new JsonAndView();
		Map result = courseService.viewCourseDetail(courseId);
		if (result == null) {
			jav.setRet(false);
			jav.setErrcode(1);
			jav.setErrmsg("查询课程详细信息失败！");
		} else {
			jav.setData(result);
		}
		return jav;
	}

	/**
	 * 修改课程的基本信息
	 * 
	 * @param course
	 *            课程对象
	 * @param request
     *            请求对象，用于记录日志
	 * @return 操作结果
	 * @see JsonAndView,Course
	 */
	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public JsonAndView modifyCourse(Course course
	        ,HttpServletRequest request) {
		JsonAndView jav = new JsonAndView();
		String result = courseService.modifyCourse(course);
		if (result != "success") {
			jav.setRet(false);
			jav.setErrcode(1);
			jav.setErrmsg("修改课程失败！");
		}else{
            //日志记录
            logDBService.insertNewLog(request, 
                    LogDBService.UPDATA_OPERATION, "课程管理",
                    "修改课程："+course.getCourName());
        }
		return jav;
	}

	/**
	 * @author ZhangXin
	 * 
	 *         获取所有考核方式
	 * @return
	 */
	@RequestMapping(value = "/evaluateMethod", method = RequestMethod.POST)
	public JsonAndView getEvaluateMethod() {
		JsonAndView jav = new JsonAndView();
		Map<String, Object> result = courseService.viewEvaMethod();
		jav.addData("data", result);
		return jav;
	}

	/**
	 * @author ZhangXin
	 * @return
	 */
	@SuppressWarnings("finally")
	@RequestMapping(value = "/categorysort", method = RequestMethod.POST)
	public JsonAndView categorySort() {

		JsonAndView jav = new JsonAndView();
		List<CourseCategory> result = null;
		try {
			result = courseService.findAllCategory();
		} catch (Exception e) {
			e.printStackTrace();
			jav.setErrmsg("查询失败");
		} finally {
			jav.addData("data", result);
			return jav;
		}
	}
	
	/**
     * 根据考核方式查询相关的课程列表
     * 
     * @param evaMetId 考核方式id
     * 
     * @return 课程列表
     */
    @RequestMapping(value = "/viewCoursesByEM", method = RequestMethod.POST)
    public JsonAndView viewCoursesByEvaMethod(int evaMetId) {
        JsonAndView jav = new JsonAndView();
        List<Course> courses=courseService
                .viewCoursesByEvaMethod(evaMetId);
        if(courses!=null){
            jav.setRet(true);
            if(!courses.isEmpty()){
                jav.addData("courses", courses);
            }
        }else{
            jav.setRet(false);
            jav.setErrmsg("获取该考核方式对应的课程失败！请联系管理员！");
        }
        return jav;
    }
	
	/**
	 * 更新相关课程的所有选课成绩（主要用户考核方式发生变化的时候）
	 * 
	 * @param courseIds 课程id，多个用，隔开
	 * 
	 * @return 更新情况
	 */
	@RequestMapping(value = "/updateScore", method = RequestMethod.POST)
    public JsonAndView updateCoursesScore(String courseIds) {
	    JsonAndView jav = new JsonAndView();
	    if(courseService.updateCoursesScore(courseIds)){
	        jav.setRet(true);
	    }else{
	        jav.setRet(false);
	        jav.setErrmsg("更新课程的选课成绩时发生异常，请联系管理员！");
	    }
        return jav;
	}
	
	/**
     * 判断课程名是否存在
     * 
     * @param courseName 课程名称
     * 
     * @return 课程名冲突情况，data中的flag为true表示冲突
     */
    @RequestMapping(value = "/checkCourseName", method = RequestMethod.POST)
    public JsonAndView checkCourseName(String courseName) {
        JsonAndView jav = new JsonAndView();
        int result=courseService.checkCourseName(courseName);
        if(result>0){
            jav.setRet(true);
            if(result==1){
                jav.addData("flag", true);
            }else{
                jav.addData("flag", false);
            }
        }else{
            jav.setRet(false);
            jav.setErrmsg("判断课程名是否冲突时发生异常，请联系管理员！");
        }
        return jav;
    } 
}
