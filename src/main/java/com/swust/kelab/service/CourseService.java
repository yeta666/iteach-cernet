package com.swust.kelab.service;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.swust.kelab.domain.Attachment;
import com.swust.kelab.domain.Course;
import com.swust.kelab.domain.CourseCategory;
import com.swust.kelab.domain.CourseSelectInformation;
import com.swust.kelab.domain.Department;
import com.swust.kelab.domain.EvaluateMethod;
import com.swust.kelab.domain.ExamPaper;
import com.swust.kelab.domain.ReSelectCourse;
import com.swust.kelab.domain.User;
import com.swust.kelab.model.CommonQuery;
import com.swust.kelab.model.CourseBbsModel;
import com.swust.kelab.model.CourseCheckModel;
import com.swust.kelab.model.CourseDetailModel;
import com.swust.kelab.model.CourseEvaluateModel;
import com.swust.kelab.model.CourseListModel;
import com.swust.kelab.model.CourseModel;
import com.swust.kelab.model.CourseSelectInfoModel;
import com.swust.kelab.model.CourseTrainModel;
import com.swust.kelab.model.ExamInfo;
import com.swust.kelab.model.ExamQuestionModel;
import com.swust.kelab.model.LearningProgressModel;
import com.swust.kelab.model.PageData;
import com.swust.kelab.model.QueryData;
import com.swust.kelab.model.TeacherCourseModel;
import com.swust.kelab.model.TeacherModel;
import com.swust.kelab.model.TestCondition;
import com.swust.kelab.model.TestCourseViewModel;
import com.swust.kelab.repos.AttachmentDAO;
import com.swust.kelab.repos.BbsPostDAO;
import com.swust.kelab.repos.CourseCateDAO;
import com.swust.kelab.repos.CourseDAO;
import com.swust.kelab.repos.DepartmentDao;
import com.swust.kelab.repos.EvaluateMethodDAO;
import com.swust.kelab.repos.ExamDAO;
import com.swust.kelab.repos.ReSelectCourseDAO;
import com.swust.kelab.repos.ReUserRoleDAO;
import com.swust.kelab.repos.UserDAO;
import com.swust.kelab.repos.bean.ListQuery;
import com.swust.kelab.utils.CommonUtil;
import com.swust.kelab.utils.FormatUtil;

@Service(value = "courseService")
public class CourseService {

	final Logger logger = LoggerFactory.getLogger(getClass());

	private final static String SUCCESS = "success";

	private final static String ERROR = "error";

	@Resource
	private ExamDAO examDAO;

	@Resource
	private UserDAO userDAO;

	@Resource
	private CourseDAO courseDAO;

	@Autowired
	private CourseCateDAO courseCateDAO;

	@Autowired
	private ReUserRoleDAO reUserRoleDAO;

	@Autowired
	private ReSelectCourseDAO reSelectCourseDAO;

	@Autowired
	private BbsPostDAO bbsPostDAO;

	@Autowired
	private DepartmentDao departmentDao;

	@Autowired
	public void setCourseDAO(CourseDAO courseDAO) {
		this.courseDAO = courseDAO;
	}

	@Autowired
	private AttachmentDAO attachmentDAO;

	@Autowired
	private EvaluateMethodDAO evaluateMethodDAO;

	/**
	 * 通过教师id查询他的课程
	 * 
	 * @param userId
	 * @author easonlian
	 */
	public List<CourseModel> viewAllCourseByTeacher(Integer userId,
			Integer userDepaType) {
		try {
			List<CourseModel> courseModel = courseDAO
					.viewAllCourseByTeacher(userId);
			Iterator<CourseModel> it = courseModel.iterator();
			while (it.hasNext()) {
				CourseModel cm = it.next();
				cm.setCourseStuCount(courseDAO.getAllCourseByTeacherCount(
						cm.getCourId(), userDepaType));
			}
			return courseModel;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 根据用户角色，主讲老师、管理员身份， 查询所有课程和id，前台构建option下拉列表框<br>
	 * 
	 * @return 包含所有课程id、课程name的List,封装到map中
	 * @author EasonLian
	 */
	public Map<String, Object> viewAllCourseList(User user) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		try {
			List<Department> depaList = departmentDao
					.viewAllDepaListBelongsToParentDepar(user.getUserDepaId(),
							user.getUserDepartType(), null);
			List<Course> courseList = new ArrayList<Course>();
			Map<String, Object> param = new HashMap<String, Object>();
			if (user.getUserType() == User.USERTYPE_TEACHING_MANAGER
					|| user.getUserType() == User.USERTYPE_SYSTEM_MANAGER)// 用户为教务管理员或系统管理员
			{
				List<User> userList = null;
				for (Department depa : depaList) {
					// 获取管辖机构的所有教师（系统管理员一般属于最高级机构）
					param.put("userType", User.USERTYPE_TEACHER);
					param.put("depaId", depa.getDepaId());
					userList = userDAO.viewUsersByTypeAndDepa(param);
					for (User usr : userList) {
						param.put("userId", usr.getUserId());
						courseList.addAll(courseDAO.viewAllCourseList(param));// 添加管辖机构主讲教师的课程
					}
				}
			}
			if (user.getUserType() == User.USERTYPE_TEACHER)// 用户为教师
			{
				param.put("userId", user.getUserId());
				courseList = courseDAO.viewAllCourseList(param);// 获取该教师主讲的课程
			}
			dataMap.put("viewAllCourse", courseList);
			return dataMap;
		} catch (Exception e) {
			logger.error("viewAllCourseList ErroR!\n" + e.getLocalizedMessage());
			return null;
		}
	}

	/**
	 * 
	 * @param userId
	 *            登陆的用户id
	 * @return 含有个人所选课程及其包含的章节信息
	 * @author EasonLian
	 * @throws Exception
	 *             抛到service层统一处理
	 * @see classpath:mapper/CourseMapper.xml
	 */
	public List<CourseModel> viewCourseListService(String projectPath,
			Integer userId, Integer userType, boolean isViewAll) {
		// try {
		String courIds = null;
		// 查询出教师所有教授的课程
		// if(userType != null && userType == 2) {
		// List<Integer> courIdList =
		// courseDAO.viewAllCourIdByTeacherId(userId);
		// if(!courIdList.isEmpty()) {
		// courIds = "";
		// for(Integer courId : courIdList)
		// courIds += (courId + ",");
		// }
		// }
		List<CourseModel> list = null;
		try {
			list = courseDAO.viewCourseListByUserId(
					userId,
					userType,
					courIds == null ? null : courIds.substring(0,
							courIds.length() - 1), isViewAll);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Iterator<CourseModel> it = list.iterator();
		while (it.hasNext()) {
			CourseModel cm = it.next();
			// 查询选课人数
			int chooseNum = 0;
			try {
				chooseNum = courseDAO.getCourseChoosedNum(cm.getCourId());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cm.setCourseStuCount(chooseNum);
			// 查询课程类型
			String courseCateIds = cm.getCourCateIds();
			courseCateIds = removeSuffixAndPreffix(courseCateIds);
			List<String> courCateList = null;
			try {
				courCateList = courseCateDAO.viewCocaNameByIds(courseCateIds);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String courCateName = "";
			for (String cateName : courCateList)
				courCateName += (cateName + ",");
			if (!courCateName.equals(""))
				courCateName = courCateName.substring(0,
						courCateName.length() - 1);
			cm.setCourCateIds(courCateName);
			// 查询课程主讲教师
			String courseTeachers = cm.getCourTeacherIds();
			courseTeachers = removeSuffixAndPreffix(courseTeachers);
			List<String> courTeaList = null;
			try {
				courTeaList = courseDAO
						.viewAllCourTeacherNameByCourse(courseTeachers);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String teacherNames = "";
			for (String teachName : courTeaList)
				teacherNames += (teachName + "，");
			if (teacherNames != "")
				teacherNames = teacherNames.substring(0,
						teacherNames.length() - 1);
			cm.setCourTeacherIds(teacherNames);
			// 判断路径是否存在，不存在文件，返回默认图片
			String relativePath = projectPath + cm.getCourImg()
					+ cm.getFileName();
			if (!new File(relativePath).exists()) {
				cm.setCourImg("upload/eduman/coursepic.jpg");
				cm.setFileName("");
			}
		}
		return list;
		/*
		 * } catch (Exception e) { logger.error("viewCourseList error!"); return
		 * null; }
		 */
	}

	private static String removeSuffixAndPreffix(String args) {
		if (args == null)
			return null;
		if (args.startsWith(","))
			args = args.substring(1, args.length());
		if (args.endsWith(","))
			args = args.substring(0, args.length() - 1);
		if (args.equals(""))
			return null;
		return args;
	}

	/**
	 * 自测展现我的选课，并且课程是已通过
	 * 
	 * @author yangZq
	 * @param userId
	 * @return
	 */
	public List<TestCourseViewModel> testCourseView(int userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map.put("userId", userId);
			map.put("rscoVerify", 1);// 选课是否通过标识0，未通过，1通过
			return courseDAO.testCourseView(map);
		} catch (Exception e) {
			logger.error("testCourseView error!");
			e.printStackTrace();
			return null;
		}
	}

	public List<CourseModel> getCourseByIds(List<Integer> ids){
		return courseDAO.selectCourseByIds(ids);
	}
	/**
	 * 自测模块获取所有课程信息
	 * 
	 * @author JesseHe
	 * @param userId
	 * @param courState
	 * @param commonquery
	 * @return
	 */
	@Transactional
	public QueryData viewAllCourse(int userId, int courState,
			CommonQuery commonquery) {
		QueryData queryData = new QueryData();
		// 构造查询条件
		ListQuery query = commonquery.format();
		if (userId > 0) {
			query.fill("userId", userId);
		}
		query.fill("courState", courState);
		query.fill("rscoVerify", 1);
		int totalCount = courseDAO.selectCount(query);
		queryData.setTotalCount(totalCount);
		if (totalCount == 0) {
			return queryData;
		}
		if (commonquery.getRecordPerPage() <= 0) {
			commonquery.setRecordPerPage(10);
		}
		query.fill("maxCount", commonquery.getRecordPerPage());
		int totalPage = QueryData.computeTotalPage(totalCount,
				commonquery.getRecordPerPage());
		queryData.setTotalPage(totalPage);
		List<PageData> pageDataList = Lists.newArrayList();
		// 未指定页数，则只读取前三页数据
		if (commonquery.getPageArray() == null) {
			commonquery.setPageArray(new int[] { 1, 2, 3 });
		}
		// 分别获取每页的数据
		for (int i = 0; i < commonquery.getPageArray().length; i++) {
			int page = commonquery.getPageArray()[i];
			if (page <= 0 || page > totalPage) {
				continue;
			}
			query.fill(
					"startIndex",
					QueryData.computeStartIndex(page,
							commonquery.getRecordPerPage()));
			List<CourseTrainModel> courseModel = courseDAO.selectList(query);
			pageDataList.add(new PageData(page, courseModel));
		}
		// 装载返回结果
		queryData.setPageData(pageDataList);
		return queryData;
	}

	/**
	 * 自测模块根据课程id获取所有对应测试试卷
	 * 
	 * @author JesseHe
	 * @param courId
	 * @param commonquery
	 * @return
	 */
	@Transactional
	public QueryData viewAllTest(int userId, int courseId,
			CommonQuery commonquery) {
		QueryData queryData = new QueryData();
		// 构造查询条件
		ListQuery query = commonquery.format();
		if (userId > 0) {
			query.fill("userId", userId);
		}
		if (courseId > 0) {
			query.fill("courseId", courseId);
		}
		Date date = new Date();
		int totalCount = examDAO.selectCount(query);
		queryData.setTotalCount(totalCount);
		if (totalCount == 0) {
			return queryData;
		}
		if (commonquery.getRecordPerPage() <= 0) {
			commonquery.setRecordPerPage(10);
		}
		query.fill("maxCount", commonquery.getRecordPerPage());
		int totalPage = QueryData.computeTotalPage(totalCount,
				commonquery.getRecordPerPage());
		queryData.setTotalPage(totalPage);
		List<PageData> pageDataList = Lists.newArrayList();
		// 未指定页数，则只读取前三页数据
		if (commonquery.getPageArray() == null) {
			commonquery.setPageArray(new int[] { 1, 2, 3 });
		}
		// 分别获取每页的数据
		for (int i = 0; i < commonquery.getPageArray().length; i++) {
			int page = commonquery.getPageArray()[i];
			if (page <= 0 || page > totalPage) {
				continue;
			}
			query.fill(
					"startIndex",
					QueryData.computeStartIndex(page,
							commonquery.getRecordPerPage()));
			List<ExamInfo> examInfos = examDAO.selectList(query);
			for (ExamInfo examInfo : examInfos) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("userId", userId);
				map.put("courseId", examInfo.getCourseId());
				examInfo.setCourseId(examInfo.getCourseId());
				int count = examDAO.checkCourseIsTest(map);
				String status = "";
				if (count > 0) {
					status = "(已考)";
				}
				examInfo.setExinState("开放中" + status);
				examInfo.setState(1);
				examInfo.setExinBgtime(DateFormatUtils.ISO_DATE_FORMAT
						.format(examInfo.getExinCreatetime()));
				if (examInfo.getExinBegintime() != null) {
					if (date.before(examInfo.getExinBegintime())) {
						examInfo.setExinState("未开放");
						examInfo.setState(0);
					}
					examInfo.setExinBgtime(DateFormatUtils.ISO_DATE_FORMAT
							.format(examInfo.getExinBegintime()));
				} else if (date.before(examInfo.getExinCreatetime())) {
					examInfo.setExinState("未开放");
					examInfo.setState(0);
				}
				if (examInfo.getExinEndtime() != null) {
					if (date.after(examInfo.getExinEndtime())) {
						examInfo.setExinState("已结束");
						examInfo.setState(2);
					}
					examInfo.setExinEdtime(DateFormatUtils.ISO_DATE_FORMAT
							.format(examInfo.getExinEndtime()));
				} else {
					examInfo.setExinEdtime("");
				}
				// 查询考生资格
				List<TestCondition> testConditions = examDAO
						.loadTestCondition(map);
				if (testConditions != null && testConditions.size() > 0) {
					TestCondition testCondition = testConditions.get(0);
					if (testCondition.getTotalScore() >= 0
							&& testCondition.getLimitScore() >= 0
							&& testCondition.getTotalScore() >= testCondition
									.getLimitScore()) {
						examInfo.setQualification(1);
					}
				} else {
					examInfo.setQualification(0);
				}
				//
			}
			pageDataList.add(new PageData(page, examInfos));
		}
		// 装载返回结果
		queryData.setPageData(pageDataList);
		return queryData;
	}

	/**
	 * 获取课程信息
	 * 
	 * @param courId
	 * @return
	 */
	public CourseModel viewCourseInfo(Integer courId) {
		CourseModel courseModel = new CourseModel();
		try {
			courseModel = courseDAO.selectOneByCourId(courId, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return courseModel;
	}

	/**
	 * 根据paperid获取考试的相关数据
	 * 
	 * @author yangzq
	 * @param paperId
	 * @return
	 */
	public Map<String, Object> viewExamPaper(int paperId, int examinId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("f", "f");
		if (examinId > 0) {
			// 根据考试id查询考试信息
			ExamInfo examInfo = examDAO.selectExamInfoById(examinId);
			// 格式化开始时间和结束时间
			if (examInfo.getExinBegintime() != null
					&& !"".equals(examInfo.getExinBegintime())) {
				examInfo.setExinBgtime(DateFormatUtils.ISO_DATE_FORMAT
						.format(examInfo.getExinBegintime()));
			} else {
				examInfo.setExinBgtime(DateFormatUtils.ISO_DATE_FORMAT
						.format(examInfo.getExinCreatetime()));
			}
			if (examInfo.getExinEndtime() != null
					&& !"".equals(examInfo.getExinEndtime())) {
				examInfo.setExinEdtime(DateFormatUtils.ISO_DATE_FORMAT
						.format(examInfo.getExinEndtime()));
			} else {
				examInfo.setExinEdtime("考试不截止");
			}
			map.put("examInfo", examInfo);
		}
		if (paperId > 0) {
			// 根据试卷id查询试卷
			ExamPaper examPaper = examDAO.selectExamPaper(paperId);
			if (examPaper != null && !"".equals(examPaper)) {
				// 根据试卷id查询试题信息
				List<ExamQuestionModel> examQueList = examDAO
						.queryPaperData(paperId);
				int single = 0;// 单选
				int multi = 0;// 多选
				int judge = 0;// 判断
				int fill = 0;// 填空
				int answer = 0;// 问答
				for (ExamQuestionModel eque : examQueList) {
					// 单选
					if (eque.getExType().equals(CommonUtil.SINGLE)) {
						single++;
					}
					// 多选
					else if (eque.getExType().equals(CommonUtil.MULTI)) {
						multi++;
					}
					// 判断
					else if (eque.getExType().equals(CommonUtil.JUDGE)) {
						judge++;
					}
					// 填空
					else if (eque.getExType().equals(CommonUtil.Fill)) {
						fill++;
					}
					// 问答
					else if (eque.getExType().equals(CommonUtil.Q_A)) {
						answer++;
					}
				}
				map.put("f", "s");
				map.put("singel_num", single);
				map.put("multi_num", multi);
				map.put("judge_num", judge);
				map.put("fill_num", fill);
				map.put("answer_num", answer);
				map.put("examPaper", examPaper);
				map.put("examQueList", examQueList);
			} else {
				map.put("msg", "试卷未找到");
			}
		} else {
			map.put("msg", "试卷未找到");
		}
		return map;
	}

	/**
	 * 获取一个部门及其所有上级部门创建的课程列表
	 * 
	 * @param departId
	 *            部门id
	 * @Param type 0表示不限制论坛开闭状态，1表示查询论坛开放的课程， 2表示查询论坛关闭的课程
	 * @return 课程列表
	 * @see Department，Course
	 */
	@SuppressWarnings("unchecked")
	public List<Map> viewCoursesByDepartId(int departId, int type) {
		List<Map> result = new ArrayList<Map>();
		// 获取所有需要查询的部门id
		List<Integer> departIds = new ArrayList<Integer>();
		if (departId > 0) {
			departIds.add(departId);
			Department depart = null;
			try {
				depart = departmentDao.queryDepartmentById(departId);
			} catch (Exception e) {
				logger.error("queryDepartType error!\n"
						+ e.getLocalizedMessage());
				return null;
			}
			if (depart != null && depart.getDepaParentId() != null) {
				int parentId = depart.getDepaParentId();
				while (parentId > 0) {
					Department d = null;
					try {
						d = departmentDao.queryDepartmentById(parentId);
					} catch (Exception e) {
						logger.error("queryDepartType error!\n"
								+ e.getLocalizedMessage());
					}
					if (d == null) {
						break;
					}
					departIds.add(parentId);
					if (d.getDepaParentId() == null) {
						break;
					}
					parentId = d.getDepaParentId();
				}
			}
		}
		// 查询相关的课程
		List<Course> courses = null;
		Map query = new HashMap();
		query.put("departIds", departIds);
		if (type > 0) {
			query.put("type", type);
		}

		try {
			courses = courseDAO.viewCourseListByDepartment(query);
		} catch (Exception e) {
			logger.error("viewCourseListByDepartment error!\n"
					+ e.getLocalizedMessage());
			return null;
		}
		if (courses != null && courses.size() > 0) {
			for (Course course : courses) {
				Map infor = new HashMap();
				infor.put("courseId", course.getCourId());
				infor.put("courseName", course.getCourName());
				result.add(infor);
			}
		}
		return result;
	}

	/**
	 * 获取一个用户（教师或者学生）对应的课程列表
	 * 
	 * @param userId
	 *            用户id
	 * @Param type 0表示不限制论坛开闭状态，1表示查询论坛开放的课程， 2表示查询论坛关闭的课程
	 * @return 课程列表
	 * @see Course
	 */
	public List<Map> viewCoursesByUserId(int userId, int type) {
		List<Map> result = null;
		// 根据用户身份类型获取课程列表
		result = new ArrayList<Map>();
		List<Course> allCourses = new ArrayList<Course>();
		// 保存已添加的课程，防重复
		Map<Integer, Boolean> addedCourses = new HashMap<Integer, Boolean>();

		// 按照选课和授课，分别处理
		// 获取所选课程列表
		List<Course> selectedCourses = null;
		try {
			Map<String, Integer> query = new HashMap<String, Integer>();
			query.put("userId", userId);
			if (type > 0) {
				query.put("bbsOpenStatus", type);
			}
			selectedCourses = reSelectCourseDAO.viewSelectedCourses(query);
		} catch (Exception e) {
			logger.error("viewSelectedCourses error!\n"
					+ e.getLocalizedMessage());
		}
		if (selectedCourses != null && selectedCourses.size() > 0) {
			for (Course course : selectedCourses) {
				if (!addedCourses.containsKey(course.getCourId())) {
					allCourses.add(course);
					addedCourses.put(course.getCourId(), true);
				}
			}
		}

		// 获取所有主讲或者辅导的课程
		Map temp = new HashMap();
		temp.put("teacherId", userId);
		temp.put("type", 3);
		if (type > 0) {
			temp.put("bbsOpenStatus", type);
		}
		List<Course> courses = null;
		try {
			courses = courseDAO.viewCourseListByTeacher(temp);
		} catch (Exception e) {
			logger.error("viewCourseListByTeacher error!\n" + e.getMessage());
		}
		if (courses != null && courses.size() > 0) {
			for (Course course : courses) {
				if (!addedCourses.containsKey(course.getCourId())) {
					allCourses.add(course);
					addedCourses.put(course.getCourId(), true);
				}
			}
		}

		if (allCourses.size() > 0) {
			for (Course c : allCourses) {
				Map course = new HashMap();
				course.put("courseId", c.getCourId());
				course.put("courseName", c.getCourName());
				result.add(course);
			}
		}
		addedCourses.clear();
		addedCourses = null;
		return result;
	}

	/**
	 * 下拉列表框加载所有课程名
	 * 
	 * @author yangzq
	 * @param userType
	 * @param departType
	 * @param departId
	 * @return
	 */
	public List<CourseModel> dropDownCourse(Integer userId, Integer departId,
			Integer departType, Integer userType) {
		Map<String, Object> query = new HashMap<String, Object>();
		if (userType.intValue() == 1) {
			query.put("userId", userId);
		}
		if (userType.intValue() == 2) {
			query.put("teacherId", "," + userId + ",");
		}
		if (userType.intValue() == 3 && departType.intValue() != 1
				|| userType.intValue() == 4 && departType.intValue() != 1) {
			query.put("departId", departId);
		}
		try {
			return courseDAO.dropDownCourse(query);
		} catch (Exception e) {
			logger.error("dropDownCourse error!");
			return null;
		}
	}

	/**
	 * @author 张鑫
	 * @return
	 */
	public Map<String, Object> registerCourse(Course c) {
		Map<String, Object> map = courseDAO.findCourseByCode(c.getCourCode());
		Map<String, Object> result = new HashMap<String, Object>();
		if (map != null && map.get("cour_id") != null) {
			result.put("info", "sorry the code is exist");
			result.put("status", 1);
			return result;
		}
		result.put("info", "insert ok");
		result.put("status", 2);

		int info = courseDAO.insertCourse(c);
		result.put("ifsuccess", info);
		return result;
	}

	/**
	 * 显示课程论坛列表
	 * 
	 * @param query
	 *            查询对象
	 * @param departId
	 *            部门id
	 * @return 课程论坛列表
	 * @see QueryData,CourseBbsModel
	 */
	public QueryData viewCourseBbsList(CommonQuery query, int departId) {
		QueryData result = new QueryData();
		// 构造查询条件
		ListQuery myQuery = query.format();
		if (departId > 0) {
			Department depart = departmentDao.queryDepartmentById(departId);
			// 仅对校级进行筛选
			if (depart.getDepaParentId() != null
					&& depart.getDepaParentId() > 0) {
				myQuery.put("departId", departId);
			}
		}
		// 查询总条数
		int totalNum = 0;
		try {
			totalNum = courseDAO.countCourseBbsNum(myQuery);
		} catch (Exception e) {
			logger.error("countCourseBbsNum error!\n" + e.getLocalizedMessage());
			result = null;
		}
		if (totalNum == 0) {
			return result;
		}
		// 按照分页查询数据
		// 默认分页信息
		if (query.getRecordPerPage() <= 0) {
			query.setRecordPerPage(10);
		}
		if (query.getPageArray() == null) {
			query.setPageArray(new int[] { 1, 2, 3 });
		}
		result.setTotalCount(totalNum);
		int totalPage = QueryData.computeTotalPage(totalNum,
				query.getRecordPerPage());
		result.setTotalPage(totalPage);
		int startIndex = (query.getPageArray()[0] - 1)
				* query.getRecordPerPage();
		int fetchSize = query.getPageArray().length * query.getRecordPerPage();
		myQuery.setStartIndex(startIndex);
		myQuery.setMaxCount(fetchSize);
		List<CourseBbsModel> bbsModels = null;
		try {
			bbsModels = courseDAO.viewCourseBbsList(myQuery);
		} catch (Exception e) {
			logger.error("viewCourseBbsList error!\n" + e.getLocalizedMessage());
			result = null;
		}
		// 封装数据
		int endIndex;
		if (bbsModels != null && bbsModels.size() > 0) {
			// 处理
			for (CourseBbsModel cbm : bbsModels) {
				if (cbm.getBbsPostOn() == 1) {
					cbm.setBbsStatus(true);
				}
			}
			// 封装数据
			List<PageData> pageDataList = Lists.newArrayList();
			for (int k = 0; k < query.getPageArray().length; k++) {
				int page = query.getPageArray()[k];
				if (page <= 0 || page > totalPage) {
					continue;
				}
				startIndex = k * query.getRecordPerPage();
				endIndex = startIndex + query.getRecordPerPage();
				if (startIndex >= bbsModels.size()) {
					continue;
				}
				if (endIndex > bbsModels.size()) {
					endIndex = bbsModels.size();
				}
				List<CourseBbsModel> pageDatas = bbsModels.subList(startIndex,
						endIndex);
				pageDataList.add(new PageData(page, pageDatas));
			}
			result.setPageData(pageDataList);
		}
		return result;
	}

	/**
	 * 批量更改bbs论坛的开闭状态
	 * 
	 * @param courseIds
	 *            课程id
	 * @param newStatus
	 *            新状态
	 * @return 操作结果
	 */
	public String setCourseBbsStatus(String courseIds, int newStatus) {
		String result = "success";
		if (courseIds != null && !courseIds.isEmpty()) {
			if (courseIds.matches("\\d+(,\\d+)*")) {
				String[] idStrs = courseIds.split(",");
				if (idStrs.length > 0) {
					List<Integer> ids = new ArrayList<Integer>();
					for (String string : idStrs) {
						ids.add(Integer.parseInt(string));
					}
					if (ids.size() > 0) {
						Map query = new HashMap();
						query.put("courseIds", ids);
						query.put("bbsStatus", newStatus);
						try {
							courseDAO.setCourseBbsStatus(query);
						} catch (Exception e) {
							logger.error("setCourseBbsStatus error!\n"
									+ e.getLocalizedMessage());
							result = "fail";
						}
					}
				}
			} else {
				result = "fail";
			}
		}
		return result;
	}

	/**
	 * 查询审批课程列表
	 * 
	 * @param query
	 * @param departmentId
	 * @return
	 */
	public QueryData queryCourseOfCheck(CommonQuery query, int departmentId) {
		QueryData result = new QueryData();
		ListQuery myQuery = query.format();
		myQuery.fill("departmentId", departmentId);
		int allCount = 0;
		try {
			allCount = courseDAO.countCourseOfCheck(myQuery);
		} catch (Exception e) {
			logger.error("countCourseOfCheck error!\n" + e.getMessage());
			return null;
		}
		if (allCount <= 0) {
			return result;
		}
		List<CourseCheckModel> list = null;
		result.setTotalCount(allCount);
		if (query.getRecordPerPage() <= 0) {
			query.setRecordPerPage(10);
		}
		int totalPage = QueryData.computeTotalPage(allCount,
				query.getRecordPerPage());
		result.setTotalPage(totalPage);
		if (query.getPageArray() == null) {
			query.setPageArray(new int[] { 1, 2 });
		}
		int startIndex = (query.getPageArray()[0] - 1)
				* query.getRecordPerPage();
		int fetchSize = query.getPageArray().length * query.getRecordPerPage();
		// System.out.println("第"+startIndex+"页，每页"+fetchSize+"条");
		myQuery.setStartIndex(startIndex);
		myQuery.fill("startIndex", startIndex);
		myQuery.fill("maxCount", fetchSize);
		try {
			list = courseDAO.queryCourseCheck(myQuery);
			initialCourseCheck(list);
		} catch (Exception e) {
			logger.error("queryCourseCheck error!\n" + e.getMessage());

		}
		// 按照分页封装数据
		// 封装数据
		List<PageData> pageDataList = Lists.newArrayList();
		for (int k = 0; k < query.getPageArray().length; k++) {
			int page = query.getPageArray()[k];
			if (page <= 0 || page > totalPage) {
				continue;
			}
			startIndex = k * query.getRecordPerPage();
			int endIndex = startIndex + query.getRecordPerPage();
			if (startIndex >= list.size()) {
				continue;
			}
			if (endIndex > list.size()) {
				endIndex = list.size();
			}
			List<CourseCheckModel> pageDatas = list.subList(startIndex,
					endIndex);
			pageDataList.add(new PageData(page, pageDatas));
		}
		result.setPageData(pageDataList);
		return result;
	}

	/**
	 * 设置所有审批课程的分类信息
	 * 
	 * @param list
	 *            查询得到的所有审批课程
	 */
	private void initialCourseCheck(List<CourseCheckModel> list) {
		// 查询所有分类
		Map<String, String> map = courseCateDAO.getAllEvame();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (int i = 0; i < list.size(); i++) {
			CourseCheckModel course = list.get(i);
			Date time;
			try {
				time = sf.parse(course.getCourCreateTime());
			} catch (ParseException e) {
				time = new Date();
				logger.error("parse time error!\n" + e.getMessage());
			}
			course.setCourCreateTime(sf.format(time));
			String cateStrs = course.getCourCateIds();
			if (cateStrs != null && cateStrs.length() >= 3) {
				String[] cates = cateStrs.substring(1, cateStrs.length() - 1)
						.split(",");
				String temp = "";
				for (int j = 0; j < cates.length; j++) {
					temp += map.get(cates[j]) + ",";
				}
				if (temp.endsWith(",")) {
					temp = temp.substring(0, temp.length() - 1);
				}
				course.setCourCateName(temp);
			}
		}
	}

	/**
	 * 查询课程的所有主讲教师以及辅导教师
	 * 
	 * @param commonQuery
	 * @param deprtId
	 * @param departType
	 * @return
	 */
	public QueryData findCourseOfTeachersByQuery(CommonQuery commonQuery,
			Integer deprtId, int departType) {
		String searchWord = commonQuery.getSearchWord();
		if (searchWord != null && searchWord.length() > 0) {
			commonQuery.setSearchWord("%" + searchWord + "%");
		}
		ListQuery myQuery = commonQuery.format();
		if (departType != 3 && deprtId > 0) {
			myQuery.fill("courDepaId", deprtId);
		}
		if (departType == 3) {
			myQuery.fill("privateLimit", 1);
		}
		Integer countQuery = courseDAO.countCourseTeachersByQuery(myQuery);
		QueryData queryData = new QueryData();

		if (countQuery <= 0) {
			queryData.setTotalCount(0);
			return queryData;
		}

		queryData.setTotalCount(countQuery);

		if (commonQuery.getRecordPerPage() <= 0) {
			commonQuery.setRecordPerPage(10);
		}
		myQuery.fill("maxCount", commonQuery.getRecordPerPage());
		int totalPage = QueryData.computeTotalPage(countQuery,
				commonQuery.getRecordPerPage());

		queryData.setTotalPage(totalPage);
		List<PageData> pageDatas = Lists.newArrayList();
		if (commonQuery.getPageArray() == null) {
			commonQuery.setPageArray(new int[] { 1, 2, 3 });
		}
		try {
			for (int index = 0; index < commonQuery.getPageArray().length; index++) {
				int indexPage = commonQuery.getPageArray()[index];
				if (indexPage <= 0 || indexPage > totalPage)
					continue;
				myQuery.fill(
						"startIndex",
						QueryData.computeStartIndex(indexPage,
								commonQuery.getRecordPerPage()));

				List<CourseDetailModel> courseDataList = courseDAO
						.findCourseTeachersByQuery(myQuery);

				setTeacherToCourser(courseDataList);
				pageDatas.add(new PageData(indexPage, courseDataList));
			}
			queryData.setPageData(pageDatas);
		} catch (Exception e) {
			return null;
		}
		return queryData;
	}

	/**
	 * 解析信息放入课程的主讲教师与辅导教师map中
	 * 
	 * @param courseDataList
	 * @throws NumberFormatException
	 */
	private void setTeacherToCourser(List<CourseDetailModel> courseDataList)
			throws NumberFormatException {
		// 主讲教师存放id
		Map<Integer, Object> teacherInfo;
		// 辅导教师存放信息
		Map<Integer, Object> mentroTeacherInfo;
		// 所有待查教师存放信息(名称)
		Map<Integer, Object> queryTeachIds = new TreeMap<Integer, Object>();
		// 所有待查教师存放信息(机构)
		Map<Integer, Integer> queryTeachDepaIds = new TreeMap<Integer, Integer>();

		for (CourseDetailModel c : courseDataList) {
			teacherInfo = new TreeMap<Integer, Object>();
			mentroTeacherInfo = new TreeMap<Integer, Object>();
			String teacherIds = c.getCourTeacherIds();
			String mentroTeacherIds = c.getCourMentroTeaids();

			// 将主讲教师的信息存放在map中
			putTeacherInfoToMap_before(teacherIds, queryTeachIds, teacherInfo);
			// 将辅导教师的信息存放在map中
			putTeacherInfoToMap_before(mentroTeacherIds, queryTeachIds,
					mentroTeacherInfo);

			c.setCourTeacherIdMap(teacherInfo);
			c.setCourMentroTeaIdMap(mentroTeacherInfo);
		}
		List<Integer> userIds = Lists.newArrayList(queryTeachIds.keySet());
		List<TeacherModel> teacherInfoList = userDAO
				.findUsersByUserIds(userIds);
		// 将所有的教师查出来放到map中
		for (TeacherModel t : teacherInfoList) {
			queryTeachIds.put(t.getTeacherId(), t.getTeacherName());
			queryTeachDepaIds.put(t.getTeacherId(), t.getSchoolId());
		}

		for (CourseDetailModel c : courseDataList) {
			// 存放主讲教师姓名以及id
			c.setCourTeacherName(putTeacherInfoToMap_end(
					c.getCourTeacherIdMap(), queryTeachIds));
			// 存放辅导教师姓名以及id
			Map mentroTeaIds = c.getCourMentroTeaIdMap();
			c.setCourMentroTeaName(putTeacherInfoToMap_end(mentroTeaIds,
					queryTeachIds));
			// 存放辅导教师机构id
			String mentroTeaDeapIds = "";
			if (mentroTeaIds != null && mentroTeaIds.size() > 0) {
				for (Object id : mentroTeaIds.keySet()) {
					if (queryTeachDepaIds.get(id) == null) {
						continue;
					}
					mentroTeaDeapIds += queryTeachDepaIds.get(id) + ",";
				}
				if (mentroTeaDeapIds.endsWith(",")) {
					mentroTeaDeapIds = mentroTeaDeapIds.substring(0,
							mentroTeaDeapIds.length() - 1);
				}
			}
			c.setCourMentroTeaDepaIds(mentroTeaDeapIds);
			c.setCourTeacherIdMap(null);
			c.setCourMentroTeaIdMap(null);
		}
	}

	/**
	 * 在查询教师之前做解析 解析字符串，将教师id分别放入两个Map中
	 * 
	 * @param teacherIds
	 * @param queryData
	 * @param teacherData
	 */
	private void putTeacherInfoToMap_before(String teacherIds,
			Map<Integer, Object> queryData, Map<Integer, Object> teacherData) {
		if (teacherIds == null || teacherIds.equals("")
				|| !teacherIds.matches("^,(\\d+,)+$"))
			return;

		teacherIds = teacherIds.substring(1, teacherIds.lastIndexOf(","));
		String[] teaIds = teacherIds.split(",");
		for (String s : teaIds) {
			teacherData.put(Integer.valueOf(s), "");
			queryData.put(Integer.valueOf(s), "");
		}

	}

	/**
	 * 查询之后将map中的数据取到目标map中
	 * 
	 * @param targetMap
	 * @param fromMap
	 * @return 返回教师名字的字符串
	 */
	private String putTeacherInfoToMap_end(Map<Integer, Object> targetMap,
			Map<Integer, Object> fromMap) {
		String teacherName = "";
		if (targetMap == null || fromMap == null) {
			return teacherName;
		}
		for (Integer id : targetMap.keySet()) {
			if (fromMap.get(id) == "") {
				continue;
			}
			teacherName += fromMap.get(id) + ",";
		}
		if (teacherName.endsWith(",")) {
			teacherName = teacherName
					.substring(0, teacherName.lastIndexOf(","));
		}
		return teacherName;
	}

	/**
	 * 由教务管理员设置课程的主讲教师以及辅导教师
	 * 
	 * @param courseId
	 *            课程ids
	 * @param addType
	 *            添加教师的教师类型，为1时为主讲教师，为2时为辅讲教师
	 * @param teacherIds
	 *            教师ids(一个教师与一个教师之间以英文逗号隔开)
	 * @return
	 * @throws Exception
	 * @author lujoCom
	 */
	public Map<String, Object> setTeacherToCourse(Integer courseId,
			Integer addType, String teacherIds) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		String regex = "^,(\\d+,)+$";

		if (teacherIds == null) {
			teacherIds = "";
		}

		// 判断是否有此课程
		Course course = new Course();
		course = courseDAO.findCourseByid(courseId);
		if (course == null) {
			data.put("status", ERROR);
			data.put("setTeaToCourInfo", "无此课程信息");
			return data;
		}
		if (teacherIds != null && !"".equals(teacherIds)
				&& !teacherIds.matches(regex)) {
			data.put("status", ERROR);
			data.put("setTeaToCouInfo", "教师数据格式不确");
			return data;
		}
		course.setCourId(courseId);
		if (addType == 1) {
			course.setCourMentroTeaids(null);
			course.setCourTeacherIds(teacherIds);
		} else {
			course.setCourTeacherIds(null);
			course.setCourMentroTeaids(teacherIds);
		}

		courseDAO.updateCourseInfo(course);

		data.put("status", SUCCESS);
		data.put("setTeaMessage", "修改成功");

		return data;
	}

	/**
	 * 根据课程id以及部门id查询选课开放与关闭时间
	 * 
	 * @param csin
	 * @return
	 */
	public List<CourseSelectInfoModel> findCourseSelectInfo(
			CourseSelectInformation csin) {
		return courseDAO.queryCourseSelectInfo(csin);
	}

	/**
	 * 设置选课的开放与关闭时间
	 * 
	 * @param csinId
	 * @param openDate
	 * @param closeDate
	 * @param remark
	 * @return
	 */
	public Map<String, Object> setCourseSelectTime(String csinId,
			Date openDate, Date closeDate, String remark) {
		Map<String, Object> data = new HashMap<String, Object>();
		if (openDate.compareTo(closeDate) > 0) {
			data.put("setCourseSelectTime", "时间错误!");
			return data;
		}
		try {
			Map<String, Object> timeInfo = new HashMap<String, Object>();
			String[] csinIds = new String[1];
			if (csinId.contains(",")) {
				csinIds = csinId.split(",");
			} else {
				csinIds[0] = csinId;
			}
			timeInfo.put("csinIds", changeIntoIntegers(csinIds));
			timeInfo.put("csinOpentime", openDate);
			timeInfo.put("csinClosetime", closeDate);
			timeInfo.put("csinRemark", remark);
			if (closeDate.compareTo(new Date()) < 0) {
				timeInfo.put("csinOn", false);
			} else {
				timeInfo.put("csinOn", true);
			}

			Integer row = courseDAO.updateCourseSelectInfo(timeInfo);
			if (row == null || row <= 0) {
				data.put("setCourseSelectTime", "课程时间设置失败");
				return data;
			}
			data.put("setCourseSelectTime", "课程选课开放时间与关闭时间设置成功");
		} catch (NumberFormatException e) {
			e.printStackTrace();
			data.put("setCourseSelectTime", "数据格式错误，请重新选择");
			return data;
		}

		return data;
	}

	/**
	 * 批量设置选课信息 ，其中参数courseIds格式为: 1,2(最后一个元素后不含逗号)<br>
	 * 参数中的值不能出现出了英文字符逗号以及数字之外的字符，否则返回数据格式不符合条件<br>
	 * 
	 * @param courseIds
	 * @param departId
	 * @param openDate
	 * @param closeDate
	 * @return
	 */
	@Transactional
	public Map<String, Object> setAllCourseSelectTime(String courseIds,
			Integer departId, Date openDate, Date closeDate) {
		Map<String, Object> returnData = new HashMap<String, Object>();
		try {

			if (closeDate.compareTo(openDate) < 0) {
				returnData.put("setAllCourseSelectTime", "关闭时间在开放时间之前，请重新选择时间");
				return returnData;
			}
			if (!(courseIds == null || courseIds.equals(""))) {
				String[] courseId = courseIds.split(",");
				List<CourseSelectInformation> list = getCourseSelectInformations(
						changeIntoIntegers(courseId), departId, openDate,
						closeDate);
				if (list.size() != 0) {
					courseDAO.insertAllCourseSelectInfo(list);
				}
				returnData.put("setAllCourseSelectTime", "批量添加选课时间成功");
			}

		} catch (NumberFormatException e) {
			e.printStackTrace();
			returnData.put("setAllCourSelTime", "数据格式错误");
		}
		return returnData;
	}

	/**
	 * 将数据封装成一个list
	 * 
	 * @param courseIds
	 * @param departId
	 * @param openDate
	 * @param closeDate
	 * @return
	 */
	private List<CourseSelectInformation> getCourseSelectInformations(
			Integer[] courseIds, Integer departId, Date openDate, Date closeDate) {
		List<CourseSelectInformation> courseSelecInfos = new ArrayList<CourseSelectInformation>();
		CourseSelectInformation courseSelectInfo = null;
		for (Integer id : courseIds) {
			courseSelectInfo = new CourseSelectInformation();
			courseSelectInfo.setCsinCourId(id);
			courseSelectInfo.setCsinDepaId(departId);

			// 判断该学校的该课程是否已经存在选课信息表中
			List<CourseSelectInfoModel> exitCourseSelectInfo = courseDAO
					.queryCourseSelectInfo(courseSelectInfo);
			if (exitCourseSelectInfo.size() != 0) {
				// 存在，则更新改课程的选课开放时间与关闭时间
				courseSelectInfo.setCsinOpentime(openDate);
				courseSelectInfo.setCsinClosetime(closeDate);
				courseSelectInfo.setCsinOn(true);
				/* courseDAO.updateCourseSelectInfo(courseSelectInfo); */
				continue;
			}
			courseSelectInfo.setCsinOpentime(openDate);
			courseSelectInfo.setCsinClosetime(closeDate);
			courseSelectInfo.setCsinOn(true);
			courseSelecInfos.add(courseSelectInfo);
		}
		return courseSelecInfos;
	}

	/**
	 * 删除选课的开放时间与关闭时间<br>
	 * csinIds格式为: 1,2(最后一个元素后不含逗号)<br>
	 * 参数中的值不能出现出了英文字符逗号以及数字之外的字符，否则返回数据格式不符合条件<br>
	 * 
	 * @param courseIds
	 * @return 返会删除时间信息
	 * @author lujoCom
	 */
	@Transactional
	public Map<String, Object> deleteCourseSelectTime(String csinIds) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> returnData = new HashMap<String, Object>();
		try {
			if (!(csinIds == null || csinIds.equals(""))) {
				String[] csinid = csinIds.split(",");
				map.put("csinIds", changeIntoIntegers(csinid));
				map.put("csinOpentime", null);
				map.put("csinClosetime", null);
				map.put("csinOn", false);
			}
		} catch (NumberFormatException e) {
			returnData.put("deleteInfo", "数据格式错误，请检查数据格式");
			return returnData;
		}

		Integer id = courseDAO.updateCourseSelectInfo(map);
		if (id == null || id <= 0) {
			returnData.put("deleteInfo", "无课程需要删除开放选课时间！");
			return returnData;
		}
		returnData.put("deleteInfo", "删除课程选课时间成功");

		return returnData;
	}

	/**
	 * 将字符串数组转化为int数组
	 * 
	 * @param numbers
	 * @return
	 * @throws NumberFormatException
	 * @author lujoCom
	 */
	private Integer[] changeIntoIntegers(String[] numbers)
			throws NumberFormatException {
		Integer[] numb = new Integer[numbers.length];
		int index = 0;
		for (String s : numbers) {
			numb[index] = Integer.parseInt(s);
			index++;
		}

		return numb;
	}

	/**
	 * 查询一审批课程的详情
	 * 
	 * @param courseId
	 * @return
	 */
	public CourseCheckModel queryOneCheckCourse(int courseId) {
		CourseCheckModel course = courseDAO.queryOneCheckCourse(courseId);
		Map<String, String> map = courseCateDAO.getAllEvame();
		String cate = "";
		if (course.getCourCateIds() != null) {
			String[] cates = course.getCourCateIds()
					.substring(1, course.getCourCateIds().length() - 1)
					.split(",");
			for (int i = 0; i < cates.length; i++) {
				cate += map.get(cates[i]) + ",";
			}
			if (cate.endsWith(",")) {
				cate = cate.substring(0, cate.length() - 1);
			}
		}
		course.setCourCateName(cate);
		List<String> userName = new ArrayList<String>();
		List ids = null;
		List<TeacherModel> userList = null;
		if (course.getCourTeacherIds() != null) {
			ids = Arrays.asList(course.getCourTeacherIds()
					.substring(1, course.getCourTeacherIds().length() - 1)
					.split(","));
			userList = userDAO.findUsersByUserIds(ids);
			for (int i = 0; i < userList.size(); i++) {
				userName.add(userList.get(i).getTeacherName());
			}
		}
		course.setCourTeacherName(userName);
		List<String> mentorsName = new ArrayList<String>();
		if (course.getCourMentorTeaIds() != null) {
			ids = Arrays.asList(course.getCourMentorTeaIds()
					.substring(1, course.getCourMentorTeaIds().length() - 1)
					.split(","));
			userList = userDAO.findUsersByUserIds(ids);
			for (int i = 0; i < userList.size(); i++) {
				mentorsName.add(userList.get(i).getTeacherName());
			}
		}
		course.setCourMentorTeaName(mentorsName);

		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			course.setCourCreateTime(sf.format(sf.parse(course
					.getCourCreateTime())));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return course;
	}

	final static int CHECK_COURSER_AGREE = 1;// 课程审核通过

	final static int CHECK_COURSE_REJECT = 2; // 课程审核拒绝

	/**
	 * 对某一课程进行审核 status为1表示审核通过，2表示审核没有通过，为0表示已经审核
	 * 
	 * @param courseId
	 *            被审核的课程id
	 * @param status
	 *            审核类型
	 * @param openToAll
	 *            是否对外开放
	 * @param courState
	 *            课程开闭状态
	 * @return 操作结果
	 */
	public int checkCourse(int courseId, int status, Boolean openToAll,
			int courState) {
		Map<String, Object> query = new HashMap<String, Object>();
		query.put("courseId", courseId);
		query.put("status", status);
		if (status == CHECK_COURSER_AGREE) {
			query.put("openToAll", openToAll);
			query.put("courState", courState);
		}
		int result = -1;
		try {
			result = courseDAO.checkCourse(query);
			if (result > 0 && status == CHECK_COURSER_AGREE) { // 审核通过后
				int depaId = courseDAO.queryDepaIdOfOneCourse(courseId);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("courseId", courseId);
				map.put("depaId", depaId);
				map.put("on", 1);
				courseDAO.insertOneSelectInfo(map);
			} else if (status == CHECK_COURSE_REJECT) {
				// 拒绝后，删除该课程
				deleteCourses("" + courseId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("课程" + courseId + "审核失败");
		}
		return result;
	}

	/**
	 * 查询所有选课时间信息(分页需求)
	 * 
	 * @author zhangxin
	 * @return List<CourseModel>
	 * @throws Exception
	 */
	@Transactional
	public QueryData selectCourseInfo(CommonQuery commonquery, Integer school)
			throws Exception {

		QueryData queryData = new QueryData();
		// 构造查询条件
		ListQuery query = commonquery.format();
		query.fill("school", school);
		String search = commonquery.getSearchWord();
		if (null != search)
			search = "%" + search + "%";
		int totalCount = courseDAO.findCourseInfoTotal(query);
		queryData.setTotalCount(totalCount);
		if (totalCount == 0) {
			return queryData;
		}
		if (commonquery.getRecordPerPage() <= 0) {
			commonquery.setRecordPerPage(6);
		}
		// query.fill("maxCount", commonquery.getRecordPerPage());
		int totalPage = QueryData.computeTotalPage(totalCount,
				commonquery.getRecordPerPage());
		queryData.setTotalPage(totalPage);
		List<PageData> pageDataList = Lists.newArrayList();
		// 未指定页数，则只读取前三页数据
		if (commonquery.getPageArray() == null) {
			commonquery.setPageArray(new int[] { 1, 2, 3 });
		}
		// 获取数据，然后分页
		query.fill("startIndex", QueryData.computeStartIndex(
				commonquery.getPageArray()[0], commonquery.getRecordPerPage()));
		query.fill(
				"maxCount",
				commonquery.getPageArray().length
						* commonquery.getRecordPerPage());
		List<CourseSelectInfoModel> result = courseDAO
				.findAllCourseInfoMsg(query);
		for (int i = 0; i < commonquery.getPageArray().length; i++) {
			int pagef = commonquery.getPageArray()[i];
			if (pagef <= 0 || pagef > totalPage) {
				continue;
			}
			int end = (i + 1) * commonquery.getRecordPerPage();
			if (end > result.size()) {
				end = result.size();
			}
			List<CourseSelectInfoModel> courseInfoModel = result.subList(i
					* commonquery.getRecordPerPage(), end);
			setCourseSelectInformationOn(courseInfoModel);
			pageDataList.add(new PageData(pagef, formatDate(courseInfoModel)));
		}
		// 装载返回结果
		queryData.setPageData(pageDataList);
		return queryData;
	}

	/**
	 * 此课程开放选课的结束时间对比当前时间，如果结束时间在当前时间之前，则此开放选课时间为无效
	 * 
	 * @param courseInfoModel
	 */
	private void setCourseSelectInformationOn(
			List<CourseSelectInfoModel> courseInfoModel) {
		if (courseInfoModel != null) {
			for (CourseSelectInfoModel c : courseInfoModel) {
				if (c.getCloseTime() != null
						&& c.getCloseTime().compareTo(new Date()) < 0) {
					c.setCourseOn(false);
				}
			}
		}
	}

	private List<CourseSelectInfoModel> formatDate(
			List<CourseSelectInfoModel> list) {
		for (CourseSelectInfoModel model : list) {
			if (model.getOpenTime() != null) {
				model.setOpenTimeStr(FormatUtil.formatDate(model.getOpenTime(),
						"yyyy-MM-dd"));
			}
			if (model.getCloseTime() != null) {
				model.setCloseTimeStr(FormatUtil.formatDate(
						model.getCloseTime(), "yyyy-MM-dd"));
			}
		}
		return list;
	}

	/**
	 * 根据查询条件,获取课程列表
	 * 
	 * @param query
	 *            查询条件
	 * @param departId
	 *            机构id
	 * @param courseCateId
	 *            课程类别id
	 * @param courseType
	 *            课程类别,为空表示所有
	 * @return 课程列表
	 */
	public QueryData viewCourseListByQuery(CommonQuery query, int departId,
			int courseCateId, String courseType) {
		QueryData result = new QueryData();
		// 处理查询条件
		ListQuery myQuery = query.format();
		if (departId > 0) {
			myQuery.fill("departId", departId);
		}
		if (courseCateId > 0) {
			myQuery.fill("courseCateId", courseCateId);
		}
		if (courseType != null && !courseType.isEmpty()) {
			myQuery.fill("courseType", courseType);
		}
		if (query.getRecordPerPage() <= 0) {
			query.setRecordPerPage(10);
		}
		if (query.getPageArray() == null) {
			query.setPageArray(new int[] { 1, 2, 3 });
		}
		// 查询总数
		int totalNum = 0;
		try {
			totalNum = courseDAO.countTotalCourseNumByQuery(myQuery);
		} catch (Exception e) {
			logger.error("countTotalCourseNumByQuery error!\n"
					+ e.getLocalizedMessage());
			result = null;
			return result;
		}
		// 查询具体列表
		result.setTotalCount(totalNum);
		result.setTotalPage(QueryData.computeTotalPage(totalNum,
				query.getRecordPerPage()));
		List<CourseListModel> courses = null;
		if (totalNum > 0) {
			int startIndex = (query.getPageArray()[0] - 1)
					* query.getRecordPerPage();
			int fetchSize = query.getPageArray().length
					* query.getRecordPerPage();
			myQuery.fill("startIndex", startIndex);
			myQuery.fill("maxCount", fetchSize);
			try {
				courses = courseDAO.viewCourseListByQuery(myQuery);
			} catch (Exception e) {
				logger.error("viewCourseListByQuery error!\n"
						+ e.getLocalizedMessage());
				result = null;
				return result;
			}
		}

		// 数据处理(领域名称、类别)和封装
		if (courses != null && courses.size() > 0) {
			// 领域名称查询
			Map cates = null;
			try {
				cates = courseCateDAO.getAllEvame();
			} catch (Exception e) {
				logger.error("getAllEvame error!\n" + e.getLocalizedMessage());
				result = null;
				return result;
			}
			if (cates != null && !cates.isEmpty()) {
				for (CourseListModel clm : courses) {
					String cateIds = clm.getCourCateids();
					String cateNames = "";
					if (cateIds != null && !cateIds.isEmpty()
							&& cateIds.matches("^,(\\d+,)+$")) {
						String[] ids = cateIds.substring(1,
								cateIds.length() - 1).split(",");
						for (String id : ids) {
							Object name = cates.get(id);
							if (name != null) {
								if (!cateNames.isEmpty()) {
									cateNames += ",";
								}
								cateNames += name.toString();
							}
						}
					}
					clm.setCateNames(cateNames);
				}
			}
			// 空类别处理和类别转换
			for (CourseListModel clm : courses) {
				String courType = clm.getCourType();
				if (courType == null || courType.isEmpty()) {
					clm.setCourType("暂无类别");
				} else {
					if (courType.equals("B")) {
						clm.setCourType("必修");
					} else if (courType.equals("XIA")) {
						clm.setCourType("选修IA");
					} else if (courType.equals("XIB")) {
						clm.setCourType("选修IB");
					} else if (courType.equals("XII")) {
						clm.setCourType("选修II");
					}
				}
			}
			// 分页封装
			int startIndex, endIndex;
			List<PageData> pageDataList = Lists.newArrayList();
			for (int k = 0; k < query.getPageArray().length; k++) {
				int page = query.getPageArray()[k];
				if (page <= 0 || page > result.getTotalPage()) {
					continue;
				}
				startIndex = k * query.getRecordPerPage();
				endIndex = startIndex + query.getRecordPerPage();
				if (startIndex >= courses.size()) {
					continue;
				}
				if (endIndex > courses.size()) {
					endIndex = courses.size();
				}
				List<CourseListModel> pageDatas = courses.subList(startIndex,
						endIndex);
				pageDataList.add(new PageData(page, pageDatas));
			}
			result.setPageData(pageDataList);
		}
		return result;
	}

	/**
	 * 课程创建
	 * 
	 * @param course
	 *            新的课程对象
	 * @return 操作结果
	 * @see Course
	 */
	public String createCourse(Course course) {
		String result = "fail";
		Integer courseId = null;
		try {
			courseId = courseDAO.createCourse(course);
		} catch (Exception e) {
			logger.error("createCourse error!\n" + e.getLocalizedMessage());
		}
		if (courseId != null) {
			// 默认新建的课程通过审核
			int check = this.checkCourse(courseId, 1,
					course.getCourOpenToAll(), course.getCourState());
			if (check == 1 || check == 0)
				result = "success";
		}
		return result;
	}

	/**
	 * 删除课程,批量删除时,多个课程id用逗号隔开
	 * 
	 * @param courseIds
	 *            课程id构造的字符串
	 * @return 删除结果
	 */
	public String deleteCourses(String courseIds) {
		String result = "fail";
		if (courseIds != null && !courseIds.isEmpty()) {
			String[] idStrs = courseIds.split(",");
			if (idStrs != null && idStrs.length > 0) {
				List<Integer> courIds = new ArrayList<Integer>();
				try {
					for (String s : idStrs) {
						courIds.add(Integer.parseInt(s));
					}
				} catch (Exception e) {
					logger.error("Integer parse error!\n"
							+ e.getLocalizedMessage());
				}
				if (courIds.size() > 0) {
					int deleteNum = 0;
					int deleteNumS = 0;
					try {
						deleteNumS = courseDAO.deleteSelectInfo(courIds);
						deleteNum = courseDAO.deleteCourses(courIds);
					} catch (Exception e) {
						logger.error("deleteCourses error!\n"
								+ e.getLocalizedMessage());
					}
					if (deleteNum == courIds.size()
							&& deleteNumS == courIds.size()) {
						result = "success";
					}
				}
			}
		}
		return result;
	}

	/**
	 * 查看课程的详细信息
	 * 
	 * @param courseId
	 *            课程id
	 * @return 包含课程详细信息的map
	 */
	public Map viewCourseDetail(int courseId) {
		Map<String, Object> result = null;
		CourseDetailModel cdm = null;
		try {
			cdm = courseDAO.viewCourseDetail(courseId);
		} catch (Exception e) {
			logger.error("viewCourseDetail error!\n" + e.getLocalizedMessage());
		}
		if (cdm != null) {
			result = new HashMap<String, Object>();
			// 基本信息
			result.put("courId", cdm.getCourId());
			result.put("courCateids", cdm.getCourCateids());
			result.put("courCode", cdm.getCourCode());
			result.put("courName", cdm.getCourName());
			result.put("courTepaId", cdm.getCourTepaId());
			result.put("courCredit", cdm.getCourCredit());
			result.put("courYear", cdm.getCourYear());
			result.put("courTerm", cdm.getCourTerm());
			result.put("courStudyPhase", cdm.getCourStudyPhase());
			result.put("courArtScience", cdm.getCourArtScience());
			result.put("departName", cdm.getDepartName());
			result.put("departType", cdm.getDepartType());
			result.put("courCreatorName", cdm.getCourCreatorName());
			result.put("courCreateTime",
					FormatUtil.formatDate(cdm.getCourCreateTime()));
			result.put("courDescribe", cdm.getCourDescribe());
			result.put("courTimeSchedule", cdm.getCourTimeSchedule());
			result.put("courPoston", cdm.isCour_poston());
			result.put("courForumName", cdm.getCourForumName());
			result.put("courVerify", cdm.getCourVerify());
			result.put("courOpenToAll", cdm.getCourOpenToAll() == null ? 0
					: cdm.getCourOpenToAll());
			result.put("courState", cdm.getCourState());
			result.put("courTestLimitScore", cdm.getCourTestLimitScore());
			if (cdm.getCourType() == null) {
				result.put("courType", "");
			} else {
				result.put("courType", cdm.getCourType());
			}
			// 教师名称查询
			String teaIds = cdm.getCourTeacherIds();
			result.put("courTeaIds", cdm.getCourTeacherIds());
			String mentroTeaIds = cdm.getCourMentroTeaids();
			List<Integer> realTeaIds = new ArrayList<Integer>();
			List<Integer> realMentroTeaIds = new ArrayList<Integer>();
			List<Integer> idList = new ArrayList<Integer>();
			if (teaIds != null && teaIds.matches("^,(\\d+,)+$")) {
				String[] ids = teaIds.substring(1, teaIds.length() - 1).split(
						",");
				try {
					for (String id : ids) {
						int realId = Integer.parseInt(id);
						realTeaIds.add(realId);
						idList.add(realId);
					}
				} catch (Exception e) {
					logger.error("Integer parse error!\n"
							+ e.getLocalizedMessage());
				}
			}
			if (mentroTeaIds != null && mentroTeaIds.matches("^,(\\d+,)+$")) {
				String[] ids = mentroTeaIds.substring(1,
						mentroTeaIds.length() - 1).split(",");
				try {
					for (String id : ids) {
						int teaId = Integer.parseInt(id);
						realMentroTeaIds.add(teaId);
						boolean flag = true;
						for (Integer temp : idList) {
							if (temp == teaId) {
								flag = false;
								break;
							}
						}
						if (flag) {
							idList.add(teaId);
						}
					}
				} catch (Exception e) {
					logger.error("Integer parse error!\n"
							+ e.getLocalizedMessage());
				}
			}
			List<TeacherModel> tms = null;
			try {
				tms = userDAO.findUsersByUserIds(idList);
			} catch (Exception e) {
				logger.error("findUsersByUserIds error!\n"
						+ e.getLocalizedMessage());
			}
			if (tms != null && tms.size() > 0) {
				// 主讲教师
				String teaNames = "";
				for (Integer teaId : realTeaIds) {
					for (TeacherModel tm : tms) {
						if (tm.getTeacherId() == teaId) {
							if (!teaNames.isEmpty()) {
								teaNames += ",";
							}
							teaNames += tm.getTeacherName();
						}
					}
				}
				result.put("teaNames", teaNames);
				// 辅导教师
				String mentroTeaNames = "";
				for (Integer teaId : realMentroTeaIds) {
					for (TeacherModel tm : tms) {
						if (tm.getTeacherId() == teaId) {
							if (!mentroTeaNames.isEmpty()) {
								mentroTeaNames += ",";
							}
							mentroTeaNames += tm.getTeacherName();
						}
					}
				}
				result.put("mentroTeaNames", mentroTeaNames);
			}
			// 课程图片附件信息
			result.put("courCoverPictureId", cdm.getCourCoverPictureId());
			if (cdm.getCourCoverPictureId() > 0) {
				Attachment attach = null;
				try {
					attach = attachmentDAO.selectAttachById(cdm
							.getCourCoverPictureId());
				} catch (Exception e) {
					logger.error("selectAttachById error!\n"
							+ e.getLocalizedMessage());
				}
				if (attach != null) {
					result.put("coverPicAttachName",
							attach.getAttaOriFilename());
					result.put("coverPicLocation", attach.getAttaLocation());
					result.put("coverPicRealName", attach.getAttaFilename());
				}
			}
		}
		return result;
	}

	/**
	 * 修改 课程
	 * 
	 * @param course
	 *            课程对象
	 * @return 操作结果
	 * @see Course
	 */
	public String modifyCourse(Course course) {
		String result = "success";
		Course oriCourse = null;
		try {
			oriCourse = courseDAO.findCourseByid(course.getCourId());
		} catch (Exception e) {
			logger.error("findCourseByid error!\n" + e.getLocalizedMessage());
		}
		if (oriCourse == null) {
			return "fail";
		}
		try {
			courseDAO.updateCourseInfo(course);
		} catch (Exception e) {
			logger.error("updateCourseInfo error!\n" + e.getLocalizedMessage());
			result = "fail";
		}
		if (result.equals("success")) {
			// 同步主贴表里面的课程名称
			String courName = course.getCourName();
			if (courName != null && !courName.equals(oriCourse.getCourName())) {
				Map query = new HashMap();
				query.put("courId", course.getCourId());
				query.put("courName", courName);
				try {
					bbsPostDAO.updateDepaCourInfo(query);
				} catch (Exception e) {
					logger.error("updateDepaCourInfo error!\n"
							+ e.getLocalizedMessage());
				}
			}
		}
		return result;
	}

	/**
	 * @author ZhangXin
	 * @return
	 */
	public List<CourseCategory> findAllCategory() throws Exception {
		List<CourseCategory> list = courseDAO.findAllCategory();
		return list;
	}

	/**
	 * 获取考核方式
	 * 
	 * @return
	 */
	public Map<String, Object> viewEvaMethod() {
		Map<String, Object> map = new HashMap<String, Object>();
		List<EvaluateMethod> list;
		try {
			list = courseDAO.findAllEvaMethod();
			map.put("result", list);
			map.put("info", "success");
		} catch (Exception e) {
			map.put("info", "error");
		}
		return map;
	}

	public List<Map<String, Object>> viewCourseListByMentroId(Integer userId, Integer departId) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		Map<String, Object> queryData = new HashMap<String, Object>();
		queryData.put("userId", userId);

		List<CourseModel> courses = courseDAO.viewCourseInfoByMentroId(queryData);
		if (courses.size() == 0) {
			return null;
		}
		Map<String, Object> courseInfo = null;
		for (CourseModel c : courses) {
			courseInfo = new HashMap<String, Object>();
			courseInfo.put("courseId", c.getCourId());
			courseInfo.put("courseName", c.getCourName());
			courseInfo.put("courseMaxStuNum", c.getCourMaxStudentNum());
			courseInfo.put("courseMaxTime", c.getCourMaxTime());
			result.add(courseInfo);
		}

		return result;
	}

	/**
	 * 查询某个老师的所有所受课程的选课人数
	 * 
	 * @param userId
	 * @return
	 */
	@Transactional
	public List<TeacherCourseModel> viewAllTeacherCourseInfo(int userId) {
		List<TeacherCourseModel> list = courseDAO
				.viewAllTeacherCourseInfo(userId);
		return list;
	}

	/**
	 * @param courseId
	 * @return
	 * @author lujoCom
	 */
	public Course queryOneCourseByCourseId(Integer courseId) {
		if (courseId == null || courseId.equals(0)) {
			return null;
		}
		return courseDAO.findCourseByid(courseId);
	}

	/**
	 * 根据考核方式查询相关的课程列表
	 * 
	 * @param evaMetId
	 *            考核方式id
	 * 
	 * @return 课程列表
	 */
	public List<Course> viewCoursesByEvaMethod(int evaMetId) {
		List<Course> result = new ArrayList<Course>();
		try {
			result = courseDAO.viewCoursesByEvaMethod(evaMetId);
		} catch (Exception e) {
			logger.error("getEvaluateMethodByCourse error!"
					+ e.getLocalizedMessage());
			return null;
		}
		return result;
	}

	/**
	 * 更新相关课程的所有选课成绩（主要用户考核方式发生变化的时候）
	 * 
	 * @param courseIds
	 *            课程id，多个用，隔开
	 * 
	 * @return 操作是否成功的标识
	 */
	public boolean updateCoursesScore(String courseIds) {
		boolean result = true;
		if (courseIds == null || courseIds.length() == 0) {
			return true;
		}
		// 计算总分
		// 获取相关的课程
		List<Integer> courIds = new ArrayList<Integer>();
		String[] ids = courseIds.split(",");
		for (String id : ids) {
			int courId = Integer.parseInt(id);
			courIds.add(courId);
		}
		// 查询课程的考核方式
		List<CourseEvaluateModel> evaMethods = Lists.newArrayList();
		try {
			evaMethods = evaluateMethodDAO.getEvaluateMethodByCourse(courIds);
		} catch (Exception e) {
			logger.error("getEvaluateMethodByCourse error!"
					+ e.getLocalizedMessage());
			return false;
		}
		// 解析考核方式，并放入map
		Map<Integer, List<Double>> patterns = new HashMap<Integer, List<Double>>();
		Map<Integer, List<Integer>> threholds = new HashMap<Integer, List<Integer>>();
		if (evaMethods != null && evaMethods.size() > 0) {
			for (CourseEvaluateModel cem : evaMethods) {
				String patternStr = cem.getEvaluatePattern();
				// System.out.println("pattern:"+patternStr);
				if (patternStr != null && patternStr.length() > 0) {
					List<Double> p = new ArrayList<Double>();
					String[] temp = patternStr.split(",");
					for (String str : temp) {
						p.add(1.0 * Double.parseDouble(str) / 100);
					}
					patterns.put(cem.getCourseId(), p);
				}
				String threholdStr = cem.getThrehold();
				// System.out.println("hold:"+threholdStr);
				if (threholdStr != null && threholdStr.length() > 0) {
					List<Integer> t = new ArrayList<Integer>();
					String[] temp = threholdStr.split(",");
					for (String str : temp) {
						t.add(Integer.parseInt(str));
					}
					threholds.put(cem.getCourseId(), t);
				}
			}
		}
		// 获取所有课程先关的选课记录
		List<ReSelectCourse> rscs = null;
		try {
			Map query = new HashMap();
			query.put("courIds", courIds);
			rscs = reSelectCourseDAO.attainCourseSelectInfo(query);
		} catch (Exception e) {
			logger.error("attainCourseSelectInfo error!"
					+ e.getLocalizedMessage());
			return false;
		}
		if (rscs == null || rscs.isEmpty()) {
			return true;
		}

		// 根据考核方式重新计算成绩
		// 集中学习并入视频学习计算
		for (ReSelectCourse sc : rscs) {
			List<Integer> threhold = threholds.get(sc.getRscoCourId());
			List<Double> pattern = patterns.get(sc.getRscoCourId());
			if (threhold != null && threhold.size() == 4 && pattern != null
					&& pattern.size() == 4) {
				Double tempScore = 0.0;
				Double totalScore = 0.0;
				// 计算成绩
				// 集中学习
				/*
				 * if(sc.getRscoMassedlearnscore()>=threhold.get(0)){
				 * tempScore=100.0*pattern.get(0); }else{
				 * tempScore=100.0*sc.getRscoMassedlearnscore()
				 * /threhold.get(0)*pattern.get(0); } totalScore+=tempScore;
				 */
				// 学习次数
				if (sc.getRscoLoginscore() >= threhold.get(0)) {
					tempScore = 100.0 * pattern.get(0);
				} else {
					tempScore = 100.0 * sc.getRscoLoginscore()
							/ threhold.get(0) * pattern.get(0);
				}
				totalScore += tempScore;
				// (视频)学习时间
				sc.setRscoLearntimescore(sc.getRscoLearntimescore()
						+ sc.getRscoMassedlearnscore());
				if (sc.getRscoLearntimescore() >= threhold.get(1)) {
					tempScore = 100.0 * pattern.get(1);
				} else {
					tempScore = 100.0 * sc.getRscoLearntimescore()
							/ threhold.get(1) * pattern.get(1);
				}
				totalScore += tempScore;
				// 论坛讨论
				if (sc.getRscoBbsdiscussscore() >= threhold.get(2)) {
					tempScore = 100.0 * pattern.get(2);
				} else {
					tempScore = 100.0 * sc.getRscoBbsdiscussscore()
							/ threhold.get(2) * pattern.get(2);
				}
				totalScore += tempScore;
				// 在线自测
				if (sc.getRscoTestscore() >= threhold.get(3)) {
					tempScore = 100.0 * pattern.get(3);
				} else {
					tempScore = 100.0 * sc.getRscoTestscore() / threhold.get(3)
							* pattern.get(3);
				}
				totalScore += tempScore;
				/*
				 * //主观评价 if(sc.getRscoSubassessscore()>=threhold.get(5)){
				 * tempScore=100.0*pattern.get(5); }else{
				 * tempScore=100.0*sc.getRscoSubassessscore()
				 * /threhold.get(5)*pattern.get(5); } totalScore+=tempScore;
				 */
				if (totalScore != sc.getRscoTotalscore() * 1.0) {
					sc.setRscoTotalscore(Float.parseFloat(totalScore.toString()));
					sc.setScoreChanged(true);
				}
			} else {
				logger.error("找不到相应课程的考核方式,或者考核方式格式有误!");
				return false;
			}
		}
		patterns.clear();
		patterns = null;
		threholds.clear();
		threholds = null;

		// 分批次更新
		int batchNum = 5000;
		int count = 0;
		List<ReSelectCourse> needChanges = new ArrayList<ReSelectCourse>();
		for (ReSelectCourse sc : rscs) {
			if (sc.isScoreChanged()) {
				needChanges.add(sc);
				count++;
				if (count >= batchNum) {
					try {
						reSelectCourseDAO.updateTotalScoreById(needChanges);
					} catch (Exception e) {
						logger.error("updateTotalScoreById error!"
								+ e.getLocalizedMessage());
						return false;
					}
					needChanges.clear();
					count = 0;
				}
			}
		}
		if (count > 0 && !needChanges.isEmpty()) {
			try {
				reSelectCourseDAO.updateTotalScoreById(needChanges);
			} catch (Exception e) {
				logger.error("updateTotalScoreById error!"
						+ e.getLocalizedMessage());
				return false;
			}
		}
		return result;
	}

	/**
	 * 判断课程名是否存在
	 * 
	 * @param courseName
	 *            课程名称
	 * 
	 * @return 课程名冲突情况，1表示冲突，2表示不冲突，0表示出错
	 */
	public int checkCourseName(String courName) {
		int result = 2;
		List<Course> cs = null;
		Course query = new Course();
		query.setCourName(courName);
		try {
			cs = courseDAO.findCourseByCourse(query);
		} catch (Exception e) {
			logger.error("findCourseByCourse error!" + e.getLocalizedMessage());
			result = 0;
		}
		if (cs != null && cs.size() >= 1) {
			result = 1;
		}
		return result;
	}
}