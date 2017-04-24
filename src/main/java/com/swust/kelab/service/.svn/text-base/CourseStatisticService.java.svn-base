package com.swust.kelab.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.swust.kelab.domain.Course;
import com.swust.kelab.domain.Department;
import com.swust.kelab.domain.User;
import com.swust.kelab.model.CommonQuery;
import com.swust.kelab.model.CourseEvaluateModel;
import com.swust.kelab.model.CourseLearnModel;
import com.swust.kelab.model.CourseSelectInfoModel;
import com.swust.kelab.model.CourseStatisticModel;
import com.swust.kelab.model.DepartmentModel;
import com.swust.kelab.model.GradeStaticModel;
import com.swust.kelab.model.LearningProgressModel;
import com.swust.kelab.model.PageData;
import com.swust.kelab.model.PlatformStatisticModel;
import com.swust.kelab.model.QueryData;
import com.swust.kelab.model.TeacherBbsModel;
import com.swust.kelab.model.TeacherTutorshipStatisticModel;
import com.swust.kelab.repos.BbsPostDAO;
import com.swust.kelab.repos.BbsReplyDAO;
import com.swust.kelab.repos.CourseDAO;
import com.swust.kelab.repos.DepartmentDao;
import com.swust.kelab.repos.EvaluateMethodDAO;
import com.swust.kelab.repos.LearningProcessRecordDAO;
import com.swust.kelab.repos.ReSelectCourseDAO;
import com.swust.kelab.repos.ResourceDAO;
import com.swust.kelab.repos.UserDAO;
import com.swust.kelab.repos.bean.ListQuery;
import com.swust.kelab.utils.ExportAsExcel;

/**
 * 课程统计（包括学生个人中心的课程统计、学习进度统计，以及教师辅导统计和平台应用统计等）的业务逻辑
 * 
 * @author 吴岘辉
 *
 */
@Service()
public class CourseStatisticService {
	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private LearningProcessRecordDAO learningProcessRecordDAO;

	@Autowired
	private CourseDAO courseDAO;

	@Autowired
	private ReSelectCourseDAO reSelectCourseDAO;

	@Autowired
	private BbsPostDAO bbsPostDAO;

	@Autowired
	private BbsReplyDAO bbsReplyDAO;

	@Autowired
	private DepartmentDao departmentDAO;

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private ResourceDAO resourceDAO;

	@Autowired
	CourseService courseService;

	@Autowired
	private EvaluateMethodDAO evaluateMethodDAO;

	/**
	 * 获取相应学生的课程统计
	 * 
	 * @param stuId
	 *            学生id
	 * @author 吴岘辉
	 * @return 课程统计信息
	 * @see CourseStatisticModel
	 */
	public List<LearningProgressModel> staCourseLearnByStu(int stuId) {
		List<LearningProgressModel> result = null;
		// 根据选课表，查询课程学习情况
		try {
			result = reSelectCourseDAO.staLearningProgress(stuId);
		} catch (Exception e) {
			logger.error("staLearningProgress error!\n" + e.getMessage());
			return result;
		}
		return result;
	}

	/**
	 * 按照查询条件，统计相应辅导教师的辅导情况
	 * 
	 * @param query
	 *            查询条件
	 * @return
	 */
	public QueryData staTeacherTutorship(CommonQuery query, int departId,
			int courseId, String teacherName) {
		QueryData staResult = new QueryData();

		// 处理机构id
		if (departId > 0) {
			// 查询部门类型
			Department depart = null;
			try {
				depart = departmentDAO.queryDepartmentById(departId);
			} catch (Exception e) {
				logger.error("queryDepartType error!\n"
						+ e.getLocalizedMessage());
				staResult = null;
				return staResult;
			}
			int departType = -1;
			if (depart != null) {
				departType = depart.getDepaType();
			}
			if (departType != 3) {
				departId = 0;
			}
		}
		// 获取相应的辅导教师
		List<User> teachers = null;
		try {
			Map teacherQuery = new HashMap();
			teacherQuery.put("userType", 2);
			if (departId > 0) {
				teacherQuery.put("depaId", departId);
			}
			if (teacherName != null && !teacherName.equals("")) {
				teacherQuery.put("name", teacherName);
			}
			teachers = userDAO.viewUsersByTypeAndDepa(teacherQuery);
		} catch (Exception e) {
			logger.error("viewUsersByTypeAndDepa error!\n"
					+ e.getLocalizedMessage());
			staResult = null;
		}
		if (teachers == null || teachers.size() == 0) {
			return staResult;
		}

		// 放入map，方面查询
		Map<Integer, String> teacherIds = new HashMap<Integer, String>();
		for (User teacherModel : teachers) {
			teacherIds.put(teacherModel.getUserId(),
					teacherModel.getUserRealname());
		}

		// 获取辅导教师的课程信息（id、名称、辅导教师）
		List<Course> courses = null;
		try {
			courses = courseDAO.viewCourseBaseInfor();
		} catch (Exception e) {
			logger.error("viewCourseMentroTeachers error!\n"
					+ e.getLocalizedMessage());
			staResult = null;
			return staResult;
		}
		if (courses == null || courses.isEmpty()) {
			return staResult;
		}
		// 构造教师所辅导的课程列表
		Map<Integer, List<Integer>> teacherCourses = new HashMap<Integer, List<Integer>>();
		;
		if (courses != null && courses.size() > 0) {
			for (Course course : courses) {
				if (courseId > 0 && course.getCourId() != courseId) {
					continue;
				}
				String mentroTeachers = course.getCourMentroTeaids();
				if (mentroTeachers == null || mentroTeachers.isEmpty()
						|| !mentroTeachers.matches("^,(\\d+,)+$")) {
					continue;
				}
				// 去除首尾的符号
				mentroTeachers = mentroTeachers.substring(1,
						mentroTeachers.length() - 1);
				String ids[] = mentroTeachers.split(",");
				if (ids != null) {
					for (String id : ids) {
						int teacherId = Integer.parseInt(id);
						if (!teacherIds.containsKey(teacherId)) {
							continue;
						}
						List<Integer> cours = teacherCourses.get(teacherId);
						if (cours == null) {
							cours = new ArrayList<Integer>();
							cours.add(course.getCourId());
							teacherCourses.put(teacherId, cours);
						} else {
							cours.add(course.getCourId());
						}
					}
				}
			}
		}

		// 查询机构（获取教师所属机构名称）\
		Map<Integer, String> depaNames = new HashMap<Integer, String>();
		List<DepartmentModel> departments = null;
		try {
			departments = departmentDAO.queryDepartName(departId);
		} catch (Exception e) {
			logger.error("departmentDAO error!\n" + e.getLocalizedMessage());
		}
		if (departments != null && departments.size() > 0) {
			for (DepartmentModel depa : departments) {
				depaNames.put(depa.getDepaId(), depa.getDepaName());
			}
		}

		List<TeacherTutorshipStatisticModel> totalStaInfo = new ArrayList<TeacherTutorshipStatisticModel>();
		// 未指定页数，则只读取前三页数据
		if (query.getPageArray() == null) {
			query.setPageArray(new int[] { 1, 2, 3 });
		}
		if (query.getRecordPerPage() <= 0) {
			query.setRecordPerPage(10);
		}
		// 查询未辅导任何课程的教师辅导情况，特殊处理
		if (courseId == -1) {
			for (int i = 0; i < teachers.size(); i++) {
				List<Integer> mentorCourses = teacherCourses.get(teachers
						.get(i).getUserId());
				if (mentorCourses == null || mentorCourses.size() == 0) {
					TeacherTutorshipStatisticModel tbm = new TeacherTutorshipStatisticModel();
					tbm.setTeacherId(teachers.get(i).getUserId());
					tbm.setCourseId(-1);
					tbm.setSchoolId(teachers.get(i).getUserDepaId());
					totalStaInfo.add(tbm);
				}
			}
			int totalCount = totalStaInfo.size();
			int totalPage = QueryData.computeTotalPage(totalCount,
					query.getRecordPerPage());
			staResult.setTotalCount(totalCount);
			staResult.setTotalPage(totalPage);
			List<PageData> pageDataList = Lists.newArrayList();
			for (int k = 0; k < query.getPageArray().length; k++) {
				int page = query.getPageArray()[k];
				if (page <= 0 || page > totalPage) {
					continue;
				}
				List<TeacherTutorshipStatisticModel> ttsms = new ArrayList<TeacherTutorshipStatisticModel>();
				int startIndex = QueryData.computeStartIndex(page,
						query.getRecordPerPage());
				for (int i = startIndex; i < totalStaInfo.size()
						&& i < startIndex + query.getRecordPerPage(); i++) {
					TeacherTutorshipStatisticModel ttsm = totalStaInfo.get(i);
					// 设置基本信息
					ttsm.setTeacherName(teacherIds.get(ttsm.getTeacherId()));
					ttsm.setSchoolName(depaNames.get(ttsm.getSchoolId()));
					ttsm.setCourseCode("无");
					ttsm.setCourseType("无");
					ttsm.setCourseName("暂无辅导课程");
					ttsm.setStudentNum(0);
					ttsm.setPostNum(0);
					ttsm.setReplyNum(0);
					ttsms.add(ttsm);
				}
				pageDataList.add(new PageData(page, ttsms));
			}
			staResult.setPageData(pageDataList);
			// 尽快释放内存
			if (depaNames != null) {
				depaNames.clear();
				depaNames = null;
			}
			if (teacherIds != null) {
				teacherIds.clear();
				teacherIds = null;
			}
			if (teachers != null) {
				teachers.clear();
				teachers = null;
			}
			if (courses != null) {
				courses.clear();
				courses = null;
			}
			if (teacherCourses != null) {
				teacherCourses.clear();
				teacherCourses = null;
			}
			return staResult;
		}

		// 统计总的记录数和页数
		int totalCount = 0;
		for (Integer teaId : teacherIds.keySet()) {
			if (teacherCourses.containsKey(teaId)) {
				totalCount += teacherCourses.get(teaId).size();
			} else if (courseId <= 0) {
				totalCount += 1;
			}
		}
		int totalPage = QueryData.computeTotalPage(totalCount,
				query.getRecordPerPage());
		staResult.setTotalCount(totalCount);
		staResult.setTotalPage(totalPage);
		if (totalCount == 0) {
			return staResult;
		}

		// 根据分页构建统计结果框架
		int count = 0;// 已经扫描的记录数
		int startIndex = (query.getPageArray()[0] - 1)
				* query.getRecordPerPage();
		int fetchSize = query.getPageArray().length * query.getRecordPerPage();
		int endSize = startIndex + fetchSize;
		for (User user : teachers) {
			List<Integer> courIds = teacherCourses.get(user.getUserId());
			if (courIds != null) {
				int curNum = courIds.size();
				if (count < endSize || count + curNum >= startIndex) {
					int begin = 0;
					if (count < startIndex) {
						begin = startIndex - count - 1;
					}
					int end = curNum;
					if (count + curNum > endSize) {
						end = end - (count + curNum - endSize);
					}
					for (int i = begin; i < end; i++) {
						TeacherTutorshipStatisticModel tbm = new TeacherTutorshipStatisticModel();
						tbm.setTeacherId(user.getUserId());
						tbm.setCourseId(courIds.get(i));
						tbm.setSchoolId(user.getUserDepaId());
						totalStaInfo.add(tbm);
					}
				}
				count += curNum;
			} else if (courseId <= 0) {
				if (count >= startIndex && count < endSize) {
					TeacherTutorshipStatisticModel tbm = new TeacherTutorshipStatisticModel();
					tbm.setTeacherId(user.getUserId());
					tbm.setCourseId(-1);
					tbm.setSchoolId(user.getUserDepaId());
					totalStaInfo.add(tbm);
				}
				count++;
			}
			if (count >= endSize) {
				break;
			}
		}

		// 根据分页进一步对课程进行过滤，减少统计量
		// 统计相应页码对应的课程
		Map<Integer, Boolean> realCourses = new HashMap<Integer, Boolean>();
		for (int i = 0; i < totalStaInfo.size(); i++) {
			int course_id = totalStaInfo.get(i).getCourseId();
			if (course_id <= 0) {
				continue;
			}
			if (!realCourses.containsKey(course_id)) {
				realCourses.put(course_id, true);
			}
		}
		for (int i = 0; i < courses.size(); i++) {
			if (!realCourses.containsKey(courses.get(i).getCourId())) {
				courses.remove(i);
				i--;
			}
		}
		// 得到需要查询的老师和课程id列表
		List<Integer> tutorCourses = new ArrayList<Integer>(
				realCourses.keySet());

		// 特殊处理
		if (tutorCourses.size() <= 0) {// 不需要统计
			List<PageData> pageDataList = Lists.newArrayList();
			for (int k = 0; k < query.getPageArray().length; k++) {
				int page = query.getPageArray()[k];
				if (page <= 0 || page > totalPage) {
					continue;
				}
				List<TeacherTutorshipStatisticModel> ttsms = new ArrayList<TeacherTutorshipStatisticModel>();
				int begin = k * query.getRecordPerPage();
				for (int i = begin; i < totalStaInfo.size()
						&& i < begin + query.getRecordPerPage(); i++) {
					TeacherTutorshipStatisticModel ttsm = totalStaInfo.get(i);
					// 设置基本信息
					ttsm.setTeacherName(teacherIds.get(ttsm.getTeacherId()));
					ttsm.setSchoolName(depaNames.get(ttsm.getSchoolId()));
					ttsm.setCourseCode("无");
					ttsm.setCourseType("无");
					ttsm.setCourseName("暂无辅导课程");
					ttsm.setStudentNum(0);
					ttsm.setPostNum(0);
					ttsm.setReplyNum(0);
					ttsms.add(ttsm);
				}
				pageDataList.add(new PageData(page, ttsms));
			}
			staResult.setPageData(pageDataList);
			// 尽快释放内存
			if (depaNames != null) {
				depaNames.clear();
				depaNames = null;
			}
			if (teachers != null) {
				teachers.clear();
				teachers = null;
			}
			if (courses != null) {
				courses.clear();
				courses = null;
			}
			if (teacherCourses != null) {
				teacherCourses.clear();
				teacherCourses = null;
			}
			return staResult;
		}

		// 开始统计(尽量少的统计（无法按照分页统计），因为数量为0的统计不出来，但是却要显示,无法确定具体的分页数据)
		// 统计相应课程的选课人数
		List<CourseSelectInfoModel> selectInfo = null;
		try {
			selectInfo = reSelectCourseDAO.staCourseSelectInfo(tutorCourses);
		} catch (Exception e) {
			logger.error("staCourseSelectInfo error!\n"
					+ e.getLocalizedMessage());
			staResult = null;
			return staResult;
		}
		// 放入map，方便查询
		Map<Integer, Integer> courseSelectInfo = new HashMap<Integer, Integer>();
		if (selectInfo != null && selectInfo.size() > 0) {
			for (CourseSelectInfoModel csi : selectInfo) {
				courseSelectInfo.put(csi.getCourseId(), csi.getSelectNum());
			}
		}

		// 统计辅导信息
		Map tutorQuery = new HashMap();
		for (int i = 0; i < totalStaInfo.size(); i++) {
			TeacherTutorshipStatisticModel ttsm = totalStaInfo.get(i);
			// 设置基本信息
			ttsm.setTeacherName(teacherIds.get(ttsm.getTeacherId()));
			ttsm.setSchoolName(depaNames.get(ttsm.getSchoolId()));
			int courId = ttsm.getCourseId();
			int teaId = ttsm.getTeacherId();
			if (courId <= 0) {
				ttsm.setCourseCode("无");
				ttsm.setCourseType("无");
				ttsm.setCourseName("暂无辅导课程");
				ttsm.setStudentNum(0);
				ttsm.setPostNum(0);
				ttsm.setReplyNum(0);
			} else {
				if (i > 0 && totalStaInfo.get(i - 1).getCourseId() == courId) {
					ttsm.setCourseCode(totalStaInfo.get(i - 1).getCourseCode());
					ttsm.setCourseType(totalStaInfo.get(i - 1).getCourseType());
					ttsm.setCourseName(totalStaInfo.get(i - 1).getCourseName());
					ttsm.setStudentNum(totalStaInfo.get(i - 1).getStudentNum());
				} else {
					for (Course course : courses) {
						if (course.getCourId() == courId) {
							ttsm.setCourseName(course.getCourName());
							ttsm.setCourseCode(course.getCourCode());
							ttsm.setCourseType(course.getCourType());
							if (courseSelectInfo == null
									|| courseSelectInfo.get(courId) == null) {
								ttsm.setStudentNum(0);
							} else {
								ttsm.setStudentNum(courseSelectInfo.get(courId));
							}
							break;
						}
					}
				}
				tutorQuery.put("courId", courId);
				tutorQuery.put("teaId", teaId);
				// 统计发帖数
				int postNum = 0;
				try {
					postNum = bbsPostDAO.staTeacherPostNumByCourse(tutorQuery);
				} catch (Exception e) {
					logger.error("staTeacherPostNumByCourse error!\n"
							+ e.getLocalizedMessage());
					return null;
				}
				ttsm.setPostNum(postNum);
				// 统计答疑数
				int replyNum = 0;
				try {
					replyNum = bbsReplyDAO
							.staTeacherReplyNumByCourse(tutorQuery);
				} catch (Exception e) {
					logger.error("staTeacherReplyNumByCourse error!\n"
							+ e.getLocalizedMessage());
					return null;
				}
				ttsm.setReplyNum(replyNum);
			}
		}

		// 按照分页整合统计结果
		List<PageData> pageDataList = Lists.newArrayList();
		for (int k = 0; k < query.getPageArray().length; k++) {
			int page = query.getPageArray()[k];
			if (page <= 0 || page > totalPage) {
				continue;
			}
			int begin = k * query.getRecordPerPage();
			int end = begin + query.getRecordPerPage();
			if (end > totalStaInfo.size()) {
				end = totalStaInfo.size();
			}
			List<TeacherTutorshipStatisticModel> ttsms = totalStaInfo.subList(
					begin, end);
			pageDataList.add(new PageData(page, ttsms));
		}
		staResult.setPageData(pageDataList);

		// 尽快释放内存
		if (depaNames != null) {
			depaNames.clear();
			depaNames = null;
		}
		if (teachers != null) {
			teachers.clear();
			teachers = null;
		}
		if (courses != null) {
			courses.clear();
			courses = null;
		}
		if (teacherCourses != null) {
			teacherCourses.clear();
			teacherCourses = null;
		}
		if (tutorCourses != null) {
			tutorCourses.clear();
			tutorCourses = null;
		}
		if (selectInfo != null) {
			selectInfo.clear();
			selectInfo = null;
		}
		if (courseSelectInfo != null) {
			courseSelectInfo.clear();
			courseSelectInfo = null;
		}
		return staResult;
	}

	/**
	 * 按学校统计平台应用情况
	 * 
	 * @return 平台应用统计
	 * @see PlatformStatisticModel，CommonQuery，QueryData
	 */
	public QueryData staPlatformApplication(CommonQuery query) {
		QueryData staResult = new QueryData();
		// 获取学校列表
		List<Department> schools = null;
		try {
			schools = departmentDAO.queryDepatmentByType(3);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("queryDepatmentByType failed!\n"
					+ e.getLocalizedMessage());
			staResult = null;
		}
		if (schools == null || schools.size() == 0) {
			return staResult;
		}

		// 先全部统计
		// 统计学校教师和学生人数
		List<PlatformStatisticModel> teaNumSta = null;
		try {
			teaNumSta = departmentDAO.staTeacherNumBySchool();
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("staTeacherNumBySchool failed!\n"
					+ e.getLocalizedMessage());
		}
		List<PlatformStatisticModel> stuNumSta = null;
		try {
			stuNumSta = departmentDAO.staStudentNumBySchool();
		} catch (Exception e) {
			logger.error("staTeacherNumBySchool failed!\n"
					+ e.getLocalizedMessage());
		}
		// 统计学习记录
		List<PlatformStatisticModel> learnRecordSta = null;
		try {
			learnRecordSta = reSelectCourseDAO.staSchoolLearnInfo();
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("staSchoolLearnInfo failed!\n"
					+ e.getLocalizedMessage());
		}
		// 统计论坛交互次数
		List<PlatformStatisticModel> postNumSta = null;
		try {
			postNumSta = bbsPostDAO.staPostNumBySchool();
		} catch (Exception e) {
			logger.error("staPostNumByCourse error!\n"
					+ e.getLocalizedMessage());
		}
		List<PlatformStatisticModel> replyNumSta = null;
		try {
			replyNumSta = bbsReplyDAO.staReplyNumBySchool();
		} catch (Exception e) {
			logger.error("staReplyNumByCourse error!\n"
					+ e.getLocalizedMessage());
		}

		// 分页整合统计结果
		int totalCount = schools.size();
		if (query.getRecordPerPage() <= 0) {
			query.setRecordPerPage(10);
		}
		int totalPage = QueryData.computeTotalPage(totalCount,
				query.getRecordPerPage());
		staResult.setTotalCount(totalCount);
		staResult.setTotalPage(totalPage);
		ListQuery myQuery = query.format();

		List<PageData> pageDataList = Lists.newArrayList();
		for (int k = 0; k < query.getPageArray().length; k++) {
			int page = query.getPageArray()[k];
			if (page <= 0 || page > totalPage) {
				continue;
			}
			int startIndex = QueryData.computeStartIndex(page,
					query.getRecordPerPage());
			List<PlatformStatisticModel> result = new ArrayList<PlatformStatisticModel>();
			int i, j;
			for (i = startIndex; i < schools.size()
					&& i < startIndex + query.getRecordPerPage(); i++) {
				Department school = schools.get(i);
				PlatformStatisticModel psm = new PlatformStatisticModel();
				// 设置学校信息
				psm.setSchoolId(school.getDepaId());
				psm.setSchoolName(school.getDepaName());
				// 设置教师总数
				psm.setTeacherNum(0);
				if (teaNumSta != null) {
					for (j = 0; j < teaNumSta.size(); j++) {
						if (teaNumSta.get(j).getSchoolId() == school
								.getDepaId()) {
							psm.setTeacherNum(teaNumSta.get(j).getTeacherNum());
							break;
						}
					}
				}
				// 设置学生总数和当前在线数
				psm.setStudentNum(0);
				if (stuNumSta != null) {
					for (j = 0; j < stuNumSta.size(); j++) {
						if (stuNumSta.get(j).getSchoolId() == school
								.getDepaId()) {
							psm.setStudentNum(stuNumSta.get(j).getStudentNum());
							psm.setOnlineStuNum(stuNumSta.get(j)
									.getOnlineStuNum());
							break;
						}
					}
				}
				// 设置学习记录统计
				psm.setTotalLearningNum(0);
				psm.setTotalLearningTime(0);
				if (learnRecordSta != null) {
					for (j = 0; j < learnRecordSta.size(); j++) {
						if (learnRecordSta.get(j).getSchoolId() == school
								.getDepaId()) {
							psm.setTotalLearningNum(learnRecordSta.get(j)
									.getTotalLearningNum());
							psm.setTotalLearningTime(learnRecordSta.get(j)
									.getTotalLearningTime());
							break;
						}
					}
				}
				// 设置论坛讨论数
				psm.setPostNum(0);
				psm.setReplyNum(0);
				if (postNumSta != null) {
					for (j = 0; j < postNumSta.size(); j++) {
						if (postNumSta.get(j).getSchoolId() == school
								.getDepaId()) {
							psm.setPostNum(postNumSta.get(j).getPostNum());
							break;
						}
					}
				}
				if (replyNumSta != null) {
					for (j = 0; j < replyNumSta.size(); j++) {
						if (replyNumSta.get(j).getSchoolId() == school
								.getDepaId()) {
							psm.setReplyNum(replyNumSta.get(j).getReplyNum());
							break;
						}
					}
				}
				result.add(psm);
			}
			pageDataList.add(new PageData(page, result));
		}
		staResult.setPageData(pageDataList);
		// 尽快清空内存
		schools.clear();
		schools = null;
		if (teaNumSta != null) {
			teaNumSta.clear();
			teaNumSta = null;
		}
		if (stuNumSta != null) {
			stuNumSta.clear();
			stuNumSta = null;
		}
		if (learnRecordSta != null) {
			learnRecordSta.clear();
			learnRecordSta = null;
		}
		if (postNumSta != null) {
			postNumSta.clear();
			postNumSta = null;
		}
		if (replyNumSta != null) {
			replyNumSta.clear();
			replyNumSta = null;
		}
		return staResult;
	}

	/**
	 * 按照学生id统计其所选课程的学习进度
	 * 
	 * @param userId
	 *            用户id
	 * @return 学习进度信息
	 * @see LearningProgressModel
	 */
	public List<LearningProgressModel> staLearningProgress(int userId) {
		// 查询统计信息
		List<LearningProgressModel> result = null;
		try {
			result = reSelectCourseDAO.staLearningProgress(userId);
		} catch (Exception e) {
			logger.error("staPostNumByCourse error!" + e.getLocalizedMessage());
			return result;
		}
		if (result == null || result.size() <= 0) {
			return result;
		}
		// 计算总分
		// 获取相关的课程，并对类型做转换
		List<Integer> courseIds = new ArrayList<Integer>();
		for (LearningProgressModel lpm : result) {
			courseIds.add(lpm.getCourseId());
			String courType = lpm.getCourseType();
			if (courType.equals("B")) {
				lpm.setCourseType("必修");
			} else if (courType.equals("XIA")) {
				lpm.setCourseType("选修IA");
			} else if (courType.equals("XIB")) {
				lpm.setCourseType("选修IB");
			} else if (courType.equals("XII")) {
				lpm.setCourseType("选修II");
			}
		}
		// 查询课程的考核方式
		List<CourseEvaluateModel> evaMethods = Lists.newArrayList();
		try {
			evaMethods = evaluateMethodDAO.getEvaluateMethodByCourse(courseIds);
		} catch (Exception e) {
			logger.error("getEvaluateMethodByCourse error!"
					+ e.getLocalizedMessage());
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
		// 根据考核方式获取成绩
		for (LearningProgressModel lpm : result) {
			List<Integer> threhold = threholds.get(lpm.getCourseId());
			List<Double> pattern = patterns.get(lpm.getCourseId());
			// 集中学习的时间被合并到视频学习时间中，不单独计算成绩
			if (threhold != null && threhold.size() == 4 && pattern != null
					&& pattern.size() == 4) {
				Double tempScore = 0.0;
				Double totalScore = 0.0;
				// 集中学习
				/*
				 * if(lpm.getMassedLearnScore()>=threhold.get(0)){
				 * tempScore=100.0*pattern.get(0); }else{
				 * tempScore=100.0*lpm.getMassedLearnScore()
				 * /threhold.get(0)*pattern.get(0); } totalScore+=tempScore;
				 * lpm.
				 * setReal_massedLearnScore(Float.valueOf(tempScore.toString(
				 * )));
				 */
				// 学习次数
				if (lpm.getLearnNumScore() >= threhold.get(0)) {
					tempScore = 100.0 * pattern.get(0);
				} else {
					tempScore = 100.0 * lpm.getLearnNumScore()
							/ threhold.get(0) * pattern.get(0);
				}
				totalScore += tempScore;
				lpm.setReal_learnNumScore(Float.valueOf(tempScore.toString()));
				// (视频)学习时间
				lpm.setLearnTimeScore(lpm.getLearnTimeScore()
						+ lpm.getMassedLearnScore());
				if (lpm.getLearnTimeScore() >= threhold.get(1)) {
					tempScore = 100.0 * pattern.get(1);
				} else {
					tempScore = 100.0 * lpm.getLearnTimeScore()
							/ threhold.get(1) * pattern.get(1);
				}
				totalScore += tempScore;
				lpm.setReal_learnTimeScore(Float.valueOf(tempScore.toString()));
				// 论坛讨论
				if (lpm.getBbsDiscussScore() >= threhold.get(2)) {
					tempScore = 100.0 * pattern.get(2);
				} else {
					tempScore = 100.0 * lpm.getBbsDiscussScore()
							/ threhold.get(2) * pattern.get(2);
				}
				totalScore += tempScore;
				lpm.setReal_bbsDiscussScore(Float.valueOf(tempScore.toString()));
				// 在线自测
				if (lpm.getTestScore() >= threhold.get(3)) {
					tempScore = 100.0 * pattern.get(3);
				} else {
					tempScore = 100.0 * lpm.getTestScore() / threhold.get(3)
							* pattern.get(3);
				}
				totalScore += tempScore;
				lpm.setReal_testScore(Float.valueOf(tempScore.toString()));
				/*
				 * //主观评价 if(lpm.getSubAssessScore()>=threhold.get(5)){
				 * tempScore=100.0*pattern.get(5); }else{
				 * tempScore=100.0*lpm.getSubAssessScore()
				 * /threhold.get(5)*pattern.get(5); } totalScore+=tempScore;
				 * lpm.
				 * setReal_subAssessScore(Float.valueOf(tempScore.toString()));
				 */
				// 总分
				lpm.setTotalScore(Float.valueOf(totalScore.toString()));
			} else {
				logger.error("找不到相应课程的考核方式,或者考核方式格式有误");
				lpm.setTotalScore(-1);
			}
		}
		patterns.clear();
		patterns = null;
		threholds.clear();
		threholds = null;
		return result;
	}

	/**
	 * 统计平台总的应用情况，比如总的学校数、总的课程数、总的教师数、总的学生人数、总的学习次数、学习时间、总的资源数、当前在线人数等信息
	 * 
	 * @return 统计信息
	 */
	public Map<String, Integer> staPlatformTotalInfor() {
		Map<String, Integer> result = new HashMap<String, Integer>();
		// 统计当前在线人数
		int onlineNum = 0;
		try {
			onlineNum = userDAO.staOnlineUserNum();
		} catch (Exception e) {
			logger.error("staOnlineUserNum error!" + e.getLocalizedMessage());
		}
		// 统计教师数
		int teacherNum = 0;
		try {
			teacherNum = userDAO.staTeacherNum();
		} catch (Exception e) {
			logger.error("staTeacherNum error!" + e.getLocalizedMessage());
		}
		// 统计开设的课程数
		int verifiedCourseNum = 0;
		try {
			verifiedCourseNum = courseDAO.staVerifiedCourseNum();
		} catch (Exception e) {
			logger.error("staVerifiedCourseNum error!"
					+ e.getLocalizedMessage());
		}
		// 统计资源数
		int resourceNum = 0;
		try {
			resourceNum = resourceDAO.staResourceNum();
		} catch (Exception e) {
			logger.error("staResourceNum error!" + e.getLocalizedMessage());
		}
		// 统计学生人数
		int studentNum = 0;
		try {
			studentNum = userDAO.staStudentNum();
		} catch (Exception e) {
			logger.error("staStudentNum error!" + e.getLocalizedMessage());
		}
		// 统计总的学习次数和学习时间
		int learnNum = 0;
		int learnTime = 0;
		try {
			PlatformStatisticModel psm = reSelectCourseDAO.staTotalLearnInfo();
			if (psm != null) {
				learnNum = psm.getTotalLearningNum();
				learnTime = psm.getTotalLearningTime();
			}
		} catch (Exception e) {
			logger.error("staTotalLearningRecord error!"
					+ e.getLocalizedMessage());
		}
		// 统计总的论坛发帖数
		int postNum = 0;
		try {
			postNum = bbsPostDAO.staTotalPostNum();
		} catch (Exception e) {
			logger.error("staTotalPostNum error!" + e.getLocalizedMessage());
		}
		// 统计总的论坛回复数
		int replyNum = 0;
		try {
			replyNum = bbsReplyDAO.staTotalReplyNum();
		} catch (Exception e) {
			logger.error("staTotalReplyNum error!" + e.getLocalizedMessage());
		}

		// 整理统计结果
		result.put("onlineUserNum", onlineNum);
		result.put("teacherNum", teacherNum);
		result.put("courseNum", verifiedCourseNum);
		result.put("resourceNum", resourceNum);
		result.put("studentNum", studentNum);
		result.put("learnNum", learnNum);
		result.put("learnTime", learnTime);
		result.put("bbsNum", postNum + replyNum);

		return result;
	}

	/**
	 * 统计课程学习状态
	 * 
	 * @param query
	 *            查询状态
	 * @param departId
	 *            机构id,仅统计指定机构及其所有上级机构创建的课程
	 * @param courseId
	 *            课程id
	 * @param noStuFilter
	 *            是否对没有学生的课程进行过滤，1表示过滤，0表示不过滤
	 * @return 课程学校状态
	 */
	public QueryData staCourseLearningState(CommonQuery query, int departId,
			int courseId, int passOrNot, int noStuFilter) {
		QueryData qd = new QueryData();
		// 查询条件
		ListQuery myQuery = query.format();
		if (departId > 0) {
			myQuery.fill("departId", departId);
		}
		if (noStuFilter > 0) {
			myQuery.fill("noStuFilter", noStuFilter);
		}
		List<Integer> courseIds = new ArrayList<Integer>();
		if (courseId > 0) {
			courseIds.add(courseId);
		} else {
			if (departId > 0) {
				myQuery.fill("departId", departId);
				List<Map> courses = courseService.viewCoursesByDepartId(
						departId, 0);
				if (courses != null && courses.size() > 0) {
					for (Map map : courses) {
						courseIds.add((Integer) map.get("courseId"));
					}
				} else {
					return qd;
				}
			}
		}
		myQuery.fill("courseIds", courseIds);
		myQuery.fill("passOrNot", passOrNot);

		// 查询课程基本信息并获取记录总数
		int totalNum = 0;
		List<CourseLearnModel> courses = null;
		try {
			courses = courseDAO.viewCourseBaseList(myQuery);
		} catch (Exception e) {
			logger.error("viewCourseBaseList error!" + e.getMessage());
			return null;
		}
		if (courses != null && courses.size() > 0) {
			totalNum = courses.size();
		}
		if (totalNum <= 0) {
			return qd;
		}

		// 转换课程类别，清空计数
		for (CourseLearnModel clm : courses) {
			String courType = clm.getCourseType();
			if (courType == null) {
				clm.setCourseType("未知");
			} else {
				if (courType.equals("B")) {
					clm.setCourseType("必修");
				} else if (courType.equals("XIA")) {
					clm.setCourseType("选修IA");
				} else if (courType.equals("XIB")) {
					clm.setCourseType("选修IB");
				} else if (courType.equals("XII")) {
					clm.setCourseType("选修II");
				}
			}
			// 清空计数
			clm.setSelectNum(0);
			clm.setOnlineNum(0);
			clm.setBbsPostNum(0);
			clm.setBbsReplyNum(0);
			clm.setLearnNum(0);
			clm.setLearnTime(0);
		}

		// 统计选课人数
		List<CourseLearnModel> staData = null;
		try {
			staData = courseDAO.viewCourseLearningList(myQuery);
		} catch (Exception e) {
			logger.error("viewCourseLearningList error!" + e.getMessage());
			return null;
		}
		// 统计课程的学习时间、学习次数
		List<CourseLearnModel> staLearnData = null;
		try {
			// staLearnData=reSelectCourseDAO.staCourseLearnState(courseIds);
			staLearnData = reSelectCourseDAO.staCourseLearnState(myQuery);
		} catch (Exception e) {
			logger.error("staCourseLearnState error!" + e.getMessage());
			return null;
		}
		// 统计bbs讨论数
		List<CourseLearnModel> staBBsPostData = null;
		try {
			// staBBsPostData=bbsPostDAO.staCourseBbsPostState(courseIds);
			staBBsPostData = bbsPostDAO.staCourseBbsPostState(myQuery);
		} catch (Exception e) {
			logger.error("staCourseBbsPostState error!" + e.getMessage());
			return null;
		}
		List<CourseLearnModel> staBBsReplyData = null;
		try {
			// staBBsReplyData=bbsReplyDAO.staCourseBbsReplyState(courseIds);
			staBBsReplyData = bbsReplyDAO.staCourseBbsReplyState(myQuery);
		} catch (Exception e) {
			logger.error("staCourseBbsReplyState error!" + e.getMessage());
			return null;
		}

		if (query.getRecordPerPage() <= 0) {
			query.setRecordPerPage(10);
		}
		if (query.getPageArray() == null) {
			query.setPageArray(new int[] { 1, 2, 3 });
		}
		qd.setTotalCount(totalNum);
		int totalPage = QueryData.computeTotalPage(totalNum,
				query.getRecordPerPage());
		qd.setTotalPage(totalPage);
		int startIndex = (query.getPageArray()[0] - 1)
				* query.getRecordPerPage();
		int fetchSize = query.getPageArray().length * query.getRecordPerPage();
		myQuery.fill("startIndex", startIndex);
		myQuery.fill("maxCount", fetchSize);
		// 整合课程的基本信息和统计信息
		if (staData != null && staData.size() > 0) {
			for (CourseLearnModel sta : staData) {
				for (CourseLearnModel course : courses) {
					if (sta.getCourseId() == course.getCourseId()) {
						course.setSelectNum(sta.getSelectNum());
						course.setOnlineNum(sta.getOnlineNum());
						break;
					}
				}
			}
		}
		if (staLearnData != null && staLearnData.size() > 0) {
			for (CourseLearnModel sta : staLearnData) {
				for (CourseLearnModel course : courses) {
					if (sta.getCourseId() == course.getCourseId()) {
						course.setLearnNum(sta.getLearnNum());
						course.setLearnTime(sta.getLearnTime());
						break;
					}
				}
			}
		}
		if (staBBsPostData != null && staBBsPostData.size() > 0) {
			for (CourseLearnModel sta : staBBsPostData) {
				for (CourseLearnModel course : courses) {
					if (sta.getCourseId() == course.getCourseId()) {
						course.setBbsPostNum(sta.getBbsPostNum());
						break;
					}
				}
			}
		}
		if (staBBsReplyData != null && staBBsReplyData.size() > 0) {
			for (CourseLearnModel sta : staBBsReplyData) {
				for (CourseLearnModel course : courses) {
					if (sta.getCourseId() == course.getCourseId()) {
						course.setBbsReplyNum(sta.getBbsReplyNum());
						break;
					}
				}
			}
		}
		// 分页封装数据
		int endIndex;
		List<PageData> pageDataList = Lists.newArrayList();
		for (int k = 0; k < query.getPageArray().length; k++) {
			int page = query.getPageArray()[k];
			if (page <= 0 || page > totalPage) {
				continue;
			}
			startIndex = QueryData.computeStartIndex(page,
					query.getRecordPerPage());
			endIndex = startIndex + query.getRecordPerPage();
			if (startIndex >= courses.size()) {
				continue;
			}
			if (endIndex > courses.size()) {
				endIndex = courses.size();
			}
			List<CourseLearnModel> pageDatas = courses.subList(startIndex,
					endIndex);
			pageDataList.add(new PageData(page, pageDatas));
		}
		qd.setPageData(pageDataList);
		return qd;
	}

	/**
	 * 将相应老师的课程辅导情况（论坛答疑次数，发起讨论次数）写入文件
	 * 
	 * @param basePath
	 *            文件路径的前缀
	 * @param query
	 *            查询对象
	 * @param departId
	 *            部门id
	 * @param courseId
	 *            课程id
	 * @param teacherName
	 *            教师名称
	 * @return 文件全路径（包括文件名）
	 */
	public String writeTeacherTutorInfor(String basePath, CommonQuery query,
			int departId, int courseId, String teacherName) {
		String path = null;
		// 处理机构id
		if (departId > 0) {
			// 查询部门类型
			Department depart = null;
			try {
				depart = departmentDAO.queryDepartmentById(departId);
			} catch (Exception e) {
				logger.error("queryDepartType error!\n"
						+ e.getLocalizedMessage());
				return null;
			}
			int departType = -1;
			if (depart != null) {
				departType = depart.getDepaType();
			}
			if (departType != 3) {
				departId = 0;
			}
		}
		// 获取相应的辅导教师
		List<User> teachers = null;
		try {
			Map teacherQuery = new HashMap();
			teacherQuery.put("userType", 2);
			if (departId > 0) {
				teacherQuery.put("depaId", departId);
			}
			if (teacherName != null && !teacherName.equals("")) {
				teacherQuery.put("name", teacherName);
			}
			teachers = userDAO.viewUsersByTypeAndDepa(teacherQuery);
		} catch (Exception e) {
			logger.error("viewUsersByTypeAndDepa error!\n"
					+ e.getLocalizedMessage());
		}
		if (teachers == null || teachers.size() == 0) {
			return path;
		}

		// 放入map，方面查询
		Map<Integer, String> teacherIds = new HashMap<Integer, String>();
		for (User teacherModel : teachers) {
			teacherIds.put(teacherModel.getUserId(),
					teacherModel.getUserRealname());
		}

		// 获取辅导教师的课程信息（id、名称、辅导教师）
		List<Course> courses = null;
		try {
			courses = courseDAO.viewCourseBaseInfor();
		} catch (Exception e) {
			logger.error("viewCourseMentroTeachers error!\n"
					+ e.getLocalizedMessage());
			return path;
		}
		if (courses == null || courses.isEmpty()) {
			return null;
		}
		// 构造教师所辅导的课程列表
		Map<Integer, List<Integer>> teacherCourses = new HashMap<Integer, List<Integer>>();
		;
		if (courses != null && courses.size() > 0) {
			for (Course course : courses) {
				if (courseId > 0 && course.getCourId() != courseId) {
					continue;
				}
				String mentroTeachers = course.getCourMentroTeaids();
				if (mentroTeachers == null || mentroTeachers.isEmpty()
						|| !mentroTeachers.matches("^,(\\d+,)+$")) {
					continue;
				}
				// 去除首尾的符号
				mentroTeachers = mentroTeachers.substring(1,
						mentroTeachers.length() - 1);
				String ids[] = mentroTeachers.split(",");
				if (ids != null) {
					for (String id : ids) {
						int teacherId = Integer.parseInt(id);
						if (!teacherIds.containsKey(teacherId)) {
							continue;
						}
						List<Integer> cours = teacherCourses.get(teacherId);
						if (cours == null) {
							cours = new ArrayList<Integer>();
							cours.add(course.getCourId());
							teacherCourses.put(teacherId, cours);
						} else {
							cours.add(course.getCourId());
						}
					}
				}
			}
		}

		// 查询机构（获取教师所属机构名称）\
		Map<Integer, String> depaNames = new HashMap<Integer, String>();
		List<DepartmentModel> departments = null;
		try {
			departments = departmentDAO.queryDepartName(departId);
		} catch (Exception e) {
			logger.error("departmentDAO error!\n" + e.getLocalizedMessage());
		}
		if (departments != null && departments.size() > 0) {
			for (DepartmentModel depa : departments) {
				depaNames.put(depa.getDepaId(), depa.getDepaName());
			}
		}

		List<TeacherTutorshipStatisticModel> ttsms = new ArrayList<TeacherTutorshipStatisticModel>();
		// 查询未辅导任何课程的教师辅导情况，特殊处理
		if (courseId == -1) {
			for (int i = 0; i < teachers.size(); i++) {
				List<Integer> mentorCourses = teacherCourses.get(teachers
						.get(i).getUserId());
				if (mentorCourses == null || mentorCourses.size() == 0) {
					TeacherTutorshipStatisticModel ttsm = new TeacherTutorshipStatisticModel();
					// 设置基本信息
					ttsm.setTeacherId(teachers.get(i).getUserId());
					ttsm.setCourseId(-1);
					ttsm.setTeacherName(teacherIds.get(teachers.get(i)
							.getUserId()));
					ttsm.setSchoolName(depaNames.get(teachers.get(i)
							.getUserDepaId()));
					ttsm.setCourseCode("无");
					ttsm.setCourseName("暂无辅导课程");
					ttsm.setStudentNum(0);
					ttsm.setPostNum(0);
					ttsm.setReplyNum(0);
					ttsms.add(ttsm);
				}
			}
		} else {
			for (User user : teachers) {
				List<Integer> courIds = teacherCourses.get(user.getUserId());
				if (courIds != null) {
					for (int i = 0; i < courIds.size(); i++) {
						TeacherTutorshipStatisticModel tbm = new TeacherTutorshipStatisticModel();
						tbm.setTeacherId(user.getUserId());
						tbm.setCourseId(courIds.get(i));
						tbm.setSchoolId(user.getUserDepaId());
						ttsms.add(tbm);
					}
				} else if (courseId <= 0) {
					TeacherTutorshipStatisticModel tbm = new TeacherTutorshipStatisticModel();
					tbm.setTeacherId(user.getUserId());
					tbm.setCourseId(-1);
					tbm.setSchoolId(user.getUserDepaId());
					ttsms.add(tbm);
				}
			}

			// 统计对应的课程
			Map<Integer, Boolean> realCourses = new HashMap<Integer, Boolean>();
			if (courseId > 0) {
				realCourses.put(courseId, true);
			} else {
				for (int i = 0; i < ttsms.size(); i++) {
					int course_id = ttsms.get(i).getCourseId();
					if (course_id <= 0) {
						continue;
					}
					if (!realCourses.containsKey(course_id)) {
						realCourses.put(course_id, true);
					}
				}
			}
			for (int i = 0; i < courses.size(); i++) {
				if (!realCourses.containsKey(courses.get(i).getCourId())) {
					courses.remove(i);
					i--;
				}
			}
			// 得到需要查询的老师和课程id列表
			List<Integer> tutorCourses = new ArrayList<Integer>(
					realCourses.keySet());

			// 特殊处理
			if (tutorCourses.size() <= 0) {
				for (int i = 0; i < ttsms.size(); i++) {
					TeacherTutorshipStatisticModel ttsm = ttsms.get(i);
					// 设置基本信息
					ttsm.setTeacherName(teacherIds.get(ttsm.getTeacherId()));
					ttsm.setSchoolName(depaNames.get(ttsm.getSchoolId()));
					ttsm.setCourseCode("无");
					ttsm.setCourseName("暂无辅导课程");
					ttsm.setStudentNum(0);
					ttsm.setPostNum(0);
					ttsm.setReplyNum(0);
				}
			} else {
				// 开始统计
				// 统计相应课程的选课人数
				List<CourseSelectInfoModel> selectInfo = null;
				try {
					selectInfo = reSelectCourseDAO
							.staCourseSelectInfo(tutorCourses);
				} catch (Exception e) {
					logger.error("staCourseSelectInfo error!\n"
							+ e.getLocalizedMessage());
					return path;
				}
				// 放入map，方便查询
				Map<Integer, Integer> courseSelectInfo = new HashMap<Integer, Integer>();
				if (selectInfo != null && selectInfo.size() > 0) {
					for (CourseSelectInfoModel csi : selectInfo) {
						courseSelectInfo.put(csi.getCourseId(),
								csi.getSelectNum());
					}
				}
				// 统计辅导信息
				Map tutorQuery = new HashMap();
				// 整合统计结果
				for (int i = 0; i < ttsms.size(); i++) {
					TeacherTutorshipStatisticModel ttsm = ttsms.get(i);
					// 设置基本信息
					ttsm.setTeacherName(teacherIds.get(ttsm.getTeacherId()));
					ttsm.setSchoolName(depaNames.get(ttsm.getSchoolId()));
					int courId = ttsm.getCourseId();
					int teaId = ttsm.getTeacherId();
					if (courId <= 0) {
						ttsm.setCourseCode("无");
						ttsm.setCourseName("暂无辅导课程");
						ttsm.setStudentNum(0);
						ttsm.setPostNum(0);
						ttsm.setReplyNum(0);
					} else {
						if (i > 0 && ttsms.get(i - 1).getCourseId() == courId) {
							ttsm.setCourseCode(ttsms.get(i - 1).getCourseCode());
							ttsm.setCourseName(ttsms.get(i - 1).getCourseName());
							ttsm.setStudentNum(ttsms.get(i - 1).getStudentNum());
						} else {
							for (Course course : courses) {
								if (course.getCourId() == courId) {
									ttsm.setCourseName(course.getCourName());
									ttsm.setCourseCode(course.getCourCode());
									if (courseSelectInfo == null
											|| courseSelectInfo.get(courId) == null) {
										ttsm.setStudentNum(0);
									} else {
										ttsm.setStudentNum(courseSelectInfo
												.get(courId));
									}
									break;
								}
							}
						}
						tutorQuery.put("courId", courId);
						tutorQuery.put("teaId", teaId);
						// 统计发帖数
						int postNum = 0;
						try {
							postNum = bbsPostDAO
									.staTeacherPostNumByCourse(tutorQuery);
						} catch (Exception e) {
							logger.error("staTeacherPostNumByCourse error!\n"
									+ e.getLocalizedMessage());
							return null;
						}
						ttsm.setPostNum(postNum);
						// 统计答疑数
						int replyNum = 0;
						try {
							replyNum = bbsReplyDAO
									.staTeacherReplyNumByCourse(tutorQuery);
						} catch (Exception e) {
							logger.error("staTeacherReplyNumByCourse error!\n"
									+ e.getLocalizedMessage());
							return null;
						}
						ttsm.setReplyNum(replyNum);
					}
				}
				if (selectInfo != null) {
					selectInfo.clear();
					selectInfo = null;
				}
				if (courseSelectInfo != null) {
					courseSelectInfo.clear();
					courseSelectInfo = null;
				}
			}
			if (tutorCourses != null) {
				tutorCourses.clear();
				tutorCourses = null;
			}
		}

		// 尽快释放内存
		if (depaNames != null) {
			depaNames.clear();
			depaNames = null;
		}
		if (teachers != null) {
			teachers.clear();
			teachers = null;
		}
		if (courses != null) {
			courses.clear();
			courses = null;
		}
		if (teacherIds != null) {
			teacherIds.clear();
			teacherIds = null;
		}
		if (teacherCourses != null) {
			teacherCourses.clear();
			teacherCourses = null;
		}

		// 导出操作
		if (ttsms.size() <= 0) {
			return path;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("teacherName", "教师姓名");
		map.put("schoolName", "所属机构");
		map.put("courseName", "课程名称");
		map.put("courseCode", "课程代码");
		map.put("studentNum", "选课数");
		map.put("postNum", "发起讨论数");
		map.put("replyNum", "答疑数");
		path = "upload/temp/教师辅导情况表.xls";
		File file = new File(basePath + "/" + path);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		if (file.exists()) {
			file.delete();
		}
		try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("创建文件失败!\n" + e.getLocalizedMessage());
		}
		boolean flag = ExportAsExcel.exportExcel(
				"com.swust.kelab.model.TeacherTutorshipStatisticModel", map,
				"教师辅导情况", file, ttsms);
		if (flag == true) {
			return path;
		}
		return null;
	}

	/**
	 * 到需要导出的课程选修信息写入临时文件，准备导入
	 * 
	 * @param basePath
	 *            临时文件的基本路径
	 * @param departId
	 *            机构id
	 * @param courseId
	 *            课程id
	 * @param filter
	 *            是否需要对未被选修的课程进行过滤，1表示需要，0表示不需要
	 * @return 返回临时文件的路径
	 */
	public String writeCourseLearningInfor(String basePath, int departId,
			int courseId, int filter) {
		String path = null;
		// 统计课程选修信息
		// 查询课程名称
		List<Course> courses = null;
		try {
			courses = courseDAO.viewCourseName(courseId);
		} catch (Exception e) {
			logger.error("viewCourseName error!" + e.getMessage());
			return null;
		}
		// 缓存课程名称
		Map<Integer, String> courseNames = new HashMap<Integer, String>();
		if (courses != null && courses.size() > 0) {
			for (Course c : courses) {
				courseNames.put(c.getCourId(), c.getCourName());
			}
		}

		// 统计每个年级的注册人数
		List<GradeStaticModel> gradeStuNums = null;
		try {
			gradeStuNums = userDAO.staGradeStudentNum();
		} catch (Exception e) {
			logger.error("staGradeStudentNum error!" + e.getMessage());
			return null;
		}
		// 缓存年级的注册学生数
		Map<Integer, Integer> gradeStuCounts = new HashMap<Integer, Integer>();
		if (gradeStuNums != null && gradeStuNums.size() > 0) {
			for (GradeStaticModel gsm : gradeStuNums) {
				gradeStuCounts.put(gsm.getGradeId(), gsm.getStudentNum());
			}
		}

		// 统计课程的学习时间、学习次数、bbs讨论数
		Map<String, Integer> query = new HashMap<String, Integer>();
		if (departId > 0) {
			query.put("departId", departId);
		}
		if (courseId > 0) {
			query.put("courseId", courseId);
		}
		query.put("filter", filter);
		List<CourseLearnModel> staLearnData = null;
		try {
			staLearnData = reSelectCourseDAO.staCourseLearnStateByGrade(query);
		} catch (Exception e) {
			logger.error("staCourseLearnStateByGrade error!" + e.getMessage());
			return null;
		}

		// 整合课程的基本信息和统计信息
		if (staLearnData != null && staLearnData.size() > 0) {
			for (CourseLearnModel sta : staLearnData) {
				// 名称
				sta.setCourseName(courseNames.get(sta.getCourseId()));
				// 注册人数
				sta.setStuNum(gradeStuCounts.get(sta.getGradeId()));
			}
		}
		courseNames.clear();
		gradeStuCounts.clear();
		courseNames = null;
		gradeStuCounts = null;

		// 将数据写入文件
		if (staLearnData.size() <= 0) {
			return path;
		}
		path = "upload/temp/课程选修情况_" + System.currentTimeMillis() + ".xls";
		File file = new File(basePath + "/" + path);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		if (file.exists()) {
			file.delete();
		}
		try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("创建文件失败!\n" + e.getLocalizedMessage());
		}
		boolean flag = ExportAsExcel.exportCourseLearningState(file,
				staLearnData);
		if (flag == true) {
			return path;
		}
		return null;
	}
}
