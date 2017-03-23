package com.swust.kelab.repos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.swust.kelab.domain.Attachment;
import com.swust.kelab.domain.Course;
import com.swust.kelab.domain.CourseCategory;
import com.swust.kelab.domain.CourseSelectInformation;
import com.swust.kelab.domain.EvaluateMethod;
import com.swust.kelab.domain.User;
import com.swust.kelab.model.CourseBbsModel;
import com.swust.kelab.model.CourseCheckModel;
import com.swust.kelab.model.CourseDetailModel;
import com.swust.kelab.model.CourseLearnModel;
import com.swust.kelab.model.CourseListModel;
import com.swust.kelab.model.CourseModel;
import com.swust.kelab.model.CourseSelectInfoModel;
import com.swust.kelab.model.CourseTrainModel;
import com.swust.kelab.model.TeacherCourseModel;
import com.swust.kelab.model.TeacherInfoModel;
import com.swust.kelab.model.TestCourseViewModel;
import com.swust.kelab.repos.bean.ListQuery;
import com.swust.kelab.repos.bean.Query;

/**
 * 
 * 课程相关查询<br>
 * 
 * @author EasonLian YangZQ
 * 
 */
@Repository("courseDao")
public class CourseDAO {

	private SqlSession sqlSession;

	@Autowired
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	/**
	 * 通过课程查询其主讲老师姓名
	 * 
	 * @param courId
	 * @author easonlian
	 * @throws Exception
	 */
	public List<String> viewAllCourTeacherNameByCourse(String teaIds)
			throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("teaIds", teaIds);
		return sqlSession.selectList("course.viewAllCourTeacherNameByCourse",
				params);
	}

	/**
	 * 通过课程查询其主讲老师信息
	 * 
	 * @author ZhangXin
	 * @param teaIds
	 * @return
	 */
	public List<TeacherInfoModel> viewAllCourTeacherByCourse(String teaIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("teaIds", teaIds);
		return sqlSession.selectList("course.viewAllCourTeacherByCourse",
				params);
	}

	/**
	 * 通过课程id查询选课人数
	 * 
	 * @param courId
	 * @author EasonLian
	 * @throws Exception
	 */
	public Integer getCourseChoosedNum(Integer courId) throws Exception {
		return sqlSession.selectOne("course.getCourseChoosedNum", courId);
	}

	/**
	 * 通过课程id查询课程封面
	 * 
	 * @param courId
	 * @author easonlian
	 * @throws Exception
	 */
	public List<Integer> viewAllCourIdByTeacherId(Integer userId)
			throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		return sqlSession.selectList("course.viewAllCourIdByTeacherId", params);
	}

	/**
	 * 通过课程id查询课程封面
	 * 
	 * @param courId
	 * @author easonlian
	 * @throws Exception
	 */
	public Attachment getCourseCoverImgById(Integer courId) throws Exception {
		return sqlSession.selectOne("course.getCourseCoverImgById", courId);
	}

	/**
	 * 通过章节id查找对应的课程信息<br>
	 * 
	 * @param chapId
	 * @return Course对象
	 * @throws Exception
	 * @author EasonLian
	 */
	public Course selectOneCourByChapId(Integer chapId) throws Exception {
		return sqlSession.selectOne("course.selectOneCourByChapId", chapId);
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
	public List<CourseModel> viewCourseListByUserId(Integer userId,
			Integer userType, String courIds, boolean isViewAll)
			throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("isViewAll", isViewAll);
		params.put("userType", userType);
		// if(courIds !=null && !courIds.equals(""))
		// params.put("courIds", courIds);
		return sqlSession.selectList("course.viewCourseList", params);
	}

	/**
	 * 按照课程id查询课程信息<br>
	 * 
	 * @param courId
	 * @param userId
	 * @return CourseModel
	 * @throws Exception
	 * @author EasonLian
	 */
	public CourseModel selectOneByCourId(Integer courId, Integer userId)
			throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("courId", courId);
		params.put("userId", userId);
		return sqlSession.selectOne("course.selectOneByCourId", params);
	}

	/**
	 * 按照课程id和用户id查询课程信息是否被该用户选择<br>
	 * 
	 * @param courId
	 * @param userId
	 * @return Integer
	 * @throws Exception
	 * @author 杨春明
	 */
	public int isChooseCourse(Integer courId, Integer userId) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("courId", courId);
		params.put("userId", userId);
		return (Integer) sqlSession.selectOne("course.isChooseCourse", params);
	}

	/**
	 * 查询所有课程和id，前台构建option下拉列表框<br>
	 * 
	 * @return 包含所有课程id、课程name的List
	 * @throws Exception
	 * @author EasonLian
	 */
	public List<Course> viewAllCourseList(Map<String, Object> params)
			throws Exception {
		return sqlSession.selectList("course.viewAllCourNameList", params);
	}

	public List<TestCourseViewModel> testCourseView(Map<String, Object> map)
			throws Exception {
		return sqlSession.selectList("course.testCourseView", map);
	}

	public int selectCount(ListQuery query) {
		return (Integer) sqlSession.selectOne("course.selectCount", query);
	}

	public List<CourseTrainModel> selectList(Query query) {
		List<CourseTrainModel> results = sqlSession.selectList("course.select",
				query);
		return results;
	}

	public CourseTrainModel selectOne(Query query) {
		CourseTrainModel results = sqlSession.selectOne("course.selectCourse",
				query);
		return results;
	}

	/**
	 * 查询所有课程的基本信息：名称、教师
	 * 
	 * @return 查询结果
	 * @see Course
	 */
	public List<Course> viewCourseBaseInfor() {
		return sqlSession.selectList("course.queryCourseBaseInfor");
	}

	/**
	 * 查询指定部门对于的课程列表
	 * 
	 * @param departIds
	 *            部门id列表
	 * @return 课程列表
	 * @see Course
	 */
	public List<Course> viewCourseListByDepartment(Map query) {
		return sqlSession
				.selectList("course.viewCourseListByDepartment", query);
	}

	/**
	 * 统计通过审核的课程数
	 * 
	 * @return 课程数
	 */
	public int staVerifiedCourseNum() {
		Integer courseNum = sqlSession.selectOne("course.staVerifiedCourseNum");
		return courseNum.intValue();
	}

	/**
	 * 查询所属课程的总数目
	 * 
	 * @param courseName
	 *            课程名（模糊匹配）
	 * @return 课程总数
	 */
	public int findCourseCount(Map<String, Object> courseName) {
		Integer i = sqlSession.selectOne("course.count", courseName);
		return i.intValue();
	}

	/**
	 * 下拉列表框加载所有课程名
	 * 
	 * @author yangzq
	 * @param query
	 * @return
	 * @throws Exception
	 */
	public List<CourseModel> dropDownCourse(Map<String, Object> query)
			throws Exception {
		return sqlSession.selectList("course.dropDownCourse", query);
	}

	/**
	 * 老师注册课程
	 * 
	 * @author 张鑫
	 * @param c
	 * @return 注册是否成功
	 */
	public int insertCourse(Course c) {
		return sqlSession.insert("course.course-register", c);
	}

	/**
	 * 通过课程代码查找课程
	 * 
	 * @param code
	 * @return
	 */
	public Map<String, Object> findCourseByCode(String code) {
		return sqlSession.selectOne("course.select-bycore", code);
	}

	/**
	 * 统计符合条件的课程论坛总数
	 * 
	 * @param query
	 *            查询条件
	 * @return 论坛总数
	 */
	public int countCourseBbsNum(Map query) {
		Integer result = sqlSession
				.selectOne("course.countCourseBbsNum", query);
		return result.intValue();
	}

	/**
	 * 查询符合条件的课程论坛列表
	 * 
	 * @param query
	 *            查询对象
	 * @return 论坛列表
	 * @see CourseBbsModel
	 */
	public List<CourseBbsModel> viewCourseBbsList(Map query) {
		return sqlSession.selectList("course.viewCourseBbsList", query);
	}

	/**
	 * 批量更改bbs论坛的开闭状态
	 * 
	 * @param query
	 *            查询对象，包含所有论坛id，以及新的状态值
	 */
	public void setCourseBbsStatus(Map query) {
		sqlSession.update("course.setCourseBbsStatus", query);
	}

	/**
	 * 修改课程信息
	 * 
	 * @param course
	 * @return
	 * @author lujoCom
	 */
	public Integer updateCourseInfo(Course course) {
		return sqlSession.update("course.updateCourseInfo", course);
	}

	/**
	 * 通过课程一些信息查询课程详细时间
	 * 
	 * @param course
	 * @return
	 */
	public List<Course> findCourseByCourse(Course course) {
		return sqlSession.selectList("course.findCourseByCourse", course);
	}

	/**
	 * 根据课程id查询课程
	 * 
	 * @param courseId
	 * @return
	 */
	public Course findCourseByid(Integer courseId) {

		if (courseId == null || courseId <= 0) {
			return null;
		}

		Course course = new Course();
		course.setCourId(courseId);
		if (findCourseByCourse(course).size() == 0) {
			return null;
		}
		return findCourseByCourse(course).get(0);
	}

	/**
	 * 审批查询结果记录数
	 * 
	 * @param myQuery
	 * @return
	 */
	public int countCourseOfCheck(ListQuery myQuery) {
		int count = ((Integer) sqlSession.selectOne(
				"course.countCourseOfCheck", myQuery)).intValue();
		System.out.println(count);
		if (count < 0) {
			count = 0;
		}
		return count;
	}

	/**
	 * 审批查询
	 * 
	 * @param myQuery
	 * @return
	 */
	public List<CourseCheckModel> queryCourseCheck(ListQuery myQuery) {
		return sqlSession.selectList("course.queryCourseOfCheck", myQuery);
	}

	/**
	 * 根据课程的id或者部门id查询选课的时间
	 * 
	 * @param csin
	 * @return
	 */
	public List<CourseSelectInfoModel> queryCourseSelectInfo(
			CourseSelectInformation csin) {
		return sqlSession.selectList(
				"courseSelectInfo.selectCourseSelectInfoById", csin);
	}

	/**
	 * 更新选课信息
	 * 
	 * @param data
	 * @return
	 */
	public Integer updateCourseSelectInfo(Map<String, Object> data) {
		return sqlSession.update("courseSelectInfo.updateCourseSelectInfoTime",
				data);
	}

	/**
	 * 删除选课信息中的时间
	 * 
	 * @param map
	 * @return
	 */
	public Integer deleteTimeOfCourseSelect(Map<String, Object> map) {
		return sqlSession.delete("courseSelectInfo.deleteTimeOfCourseSelect",
				map);
	}

	/**
	 * 添加选课信息
	 * 
	 * @param csin
	 * @return
	 */
	public Integer insertAllCourseSelectInfo(List<CourseSelectInformation> csins) {
		return sqlSession.insert("courseSelectInfo.addAllCourseSelectInfo",
				csins);
	}

	/**
	 * 查询一门课程详情
	 * 
	 * @param courseId
	 *            课程id
	 * @return 课程详情
	 * @see CourseCheckModel
	 */
	public CourseCheckModel queryOneCheckCourse(int courseId) {
		return sqlSession.selectOne("course.queryOneCheckCourse", courseId);
	}

	/**
	 * 审核课程
	 * 
	 * @param query
	 */
	public int checkCourse(Map<String, Object> query) {
		return ((Integer) sqlSession.update("course.checkCourse", query))
				.intValue();
	}

	/**
	 * 查询所有课程开放时间信息总数
	 * 
	 * @param query
	 * @return
	 */
	public int findCourseInfoTotal(Query query) {
		return ((Integer) sqlSession.selectOne(
				"courseSelectInfo.selectCourseCount", query)).intValue();
	}

	/**
	 * 查询所有课程开放时间信息
	 * 
	 * @param query
	 * @return
	 */
	public List<CourseSelectInfoModel> findAllCourseInfoMsg(Query query) {
		return sqlSession.selectList(
				"courseSelectInfo.selectAllCourseSelectInfoByQuery", query);
	}

	/**
	 * 统计指定条件的课程总数
	 * 
	 * @param query
	 *            查询条件
	 * @return 课程总数
	 */
	public int countTotalCourseNumByQuery(Map query) {
		return ((Integer) sqlSession.selectOne("course"
				+ ".countTotalNumByQuery", query)).intValue();
	}

	/**
	 * 根据查询条件,获取已通过审核的课程列表
	 * 
	 * @param query
	 *            查询对象
	 * @return 课程列表
	 */
	public List<CourseListModel> viewCourseListByQuery(Map query) {
		return sqlSession.selectList("course.viewCourseListByQuery", query);
	}

	/**
	 * 创建课程
	 * 
	 * @param course
	 *            课程对象
	 * @return 新创建课程的ID
	 * @see Course
	 */
	public Integer createCourse(Course course) {
		sqlSession.insert("course.create", course);
		return course.getCourId();
	}

	/**
	 * 查询一门课程所属的部门id
	 * 
	 * @param courseId
	 *            课程id
	 * @return 课程所属部门id
	 */
	public int queryDepaIdOfOneCourse(int courseId) {
		return ((Integer) sqlSession.selectOne("course.selectDepaIdCouse",
				courseId)).intValue();
	}

	/**
	 * 审核完一门课程后，向选课记录表中增加一条记录
	 * 
	 * @param query
	 *            （包括选课课程id，课程所属部门id，选课是否有效（默认有效1））
	 * @return
	 */
	public int insertOneSelectInfo(Map query) {
		return ((Integer) sqlSession.insert(
				"courseSelectInfo.insertOneBycheck", query)).intValue();
	}

	/**
	 * 根据id获取课程的详细信息
	 * 
	 * @param courseId
	 *            课程id
	 * @return 课程详细信息
	 */
	public CourseDetailModel viewCourseDetail(int courseId) {
		return sqlSession.selectOne("course.viewDetail", courseId);
	}

	/**
	 * 批量删除课程
	 * 
	 * @param courseIds
	 *            课程id列表
	 * @return 删除的记录条数
	 */
	public int deleteCourses(List<Integer> courseIds) {
		return sqlSession.delete("course.delete", courseIds);
	}

	/**
	 * 根据条件(教师id 查询类型),获取对应的课程的基本信息 教师id为teacherId,查询类型为type:1表示查询教师主讲的课程
	 * ,2表示辅导的课程,3表示both
	 * 
	 * @param userId
	 *            教师id
	 * @return 课程列表
	 */
	public List<Course> viewCourseListByTeacher(Map query) {
		return sqlSession.selectList("course.viewCoursesByTeacher", query);
	}

	/**
	 * 获取所有考核方式
	 * 
	 * @return
	 */
	public List<EvaluateMethod> findAllEvaMethod() {
		return sqlSession.selectList("evaluateMethod.viewAll");
	}

	/**
	 * 查询所有的类别
	 * 
	 * @return
	 */
	public List<CourseCategory> findAllCategory() throws Exception {
		return sqlSession.selectList("courseCategory.all");
	}

	/**
	 * 查询课程的所有主讲教师与辅导教师
	 * 
	 * @param query
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<CourseDetailModel> findCourseTeachersByQuery(Map query) {
		return sqlSession
				.selectList("course.queryCourseTeachersByQuery", query);
	}

	/**
	 * 查询课程的条数
	 * 
	 * @param query
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Integer countCourseTeachersByQuery(Map query) {
		return ((Integer) sqlSession.selectOne(
				"course.countCourseTeachersByQuery", query)).intValue();
	}

	public List<CourseModel> viewCourseInfoByMentroId(
			Map<String, Object> queryData) {
		return sqlSession.selectList("course.viewCourseByMentroId", queryData);

	}

	/**
	 * 查询某个老师的所有所受课程的选课人数
	 * 
	 * @param userId
	 * @return
	 */
	public List<TeacherCourseModel> viewAllTeacherCourseInfo(int userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		// params.put("depaId", depaId);
		return sqlSession.selectList("course.viewAllTeacherCourseInfo", params);
	}

	/**
	 * 通过教师id查询他的课程
	 * 
	 * @param userId
	 * @author easonlian
	 */
	public List<CourseModel> viewAllCourseByTeacher(Integer userId)
			throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", "," + userId + ",");
		return sqlSession.selectList("course.viewAllCourseByTeacher", params);
	}

	/**
	 * 查询教师课程总数
	 * 
	 * @param courId
	 * @return
	 * @throws Exception
	 */
	public int getAllCourseByTeacherCount(Integer courId, Integer userDepaType)
			throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("courId", courId);
		params.put("depaType", userDepaType);
		return ((Integer) sqlSession.selectOne(
				"course.getAllCourseByTeacherCount", params)).intValue();
	}

	/**
	 * 查询指定的课程列表,包含课程的基本信息,用于课程选课统计
	 * 
	 * @param query
	 *            查询条件
	 * @return 课程列表
	 */
	public List<CourseLearnModel> viewCourseBaseList(Map query) {
		return sqlSession.selectList("course.viewCourseBaseList", query);
	}

	/**
	 * 根据条件,查询课程学习情况列表,用户课程选课统计
	 * 
	 * @param query
	 *            查询条件
	 * @return 课程学习情况列表
	 */
	public List<CourseLearnModel> viewCourseLearningList(Map query) {
		return sqlSession.selectList("course.viewCourseLearnList", query);
	}

	/**
	 * 通过课程id，部门id，以及当前时间，来判断当前课程是否可选，若可选返回当前课程的选课信息id,否则返回空
	 * 
	 * @param data
	 * @return
	 * @author lujoCom
	 */
	public Integer findCourseSelecIdBytime(Map<String, Object> data) {
		return sqlSession.selectOne("courseSelectInfo.findCourseSelecIdBytime",
				data);
	}

	/**
	 * 通过学生id查询课程id
	 * 
	 * @param userId
	 * @author easonlian
	 * @throws Exception
	 */
	public List<Integer> viewAllCourIdByStudentId(Integer userId)
			throws Exception {
		return sqlSession.selectList(
				"courseSelectInfo.viewAllCourIdByStudentId", userId);
	}

	/**
	 * 设置课程的选课人数
	 * 
	 * @param data
	 * @return
	 * @author lujoCom
	 */
	public Integer setCourseChooseNum(Map<String, Object> data) {
		return sqlSession.update("course.setCourseChooseNum", data);
	}

	/**
	 * 根据课程编号和用户类型，部门类型等查询条件过滤查询课程 用于题库导入时分辨用户是否有对应课程试题的导入权限
	 * 
	 * @author yangzq
	 * @param query
	 * @return
	 */
	public Integer findCourseByAuthority(Map<String, Object> query) {
		return sqlSession.selectOne("course.findCourseByAuthority", query);
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
		return sqlSession.selectList("course.viewCoursesByEvaMethod", evaMetId);
	}

	/**
	 * 更改课程的评分（发生在对课程章节进行评价时）
	 * 
	 * @param map
	 *            课程及评分信息
	 * @return 影响的数据库记录数
	 */
	public Integer assessCourse(Map map) {
		return (Integer) sqlSession.update("course.assessCourse", map);
	}

	/**
	 * 查询教务统计-学生成绩查看的相应课程
	 * 
	 * @param query
	 *            查询条件，比如课程类别、学年等
	 * @return 课程列表
	 */
	public List<Course> viewStatisticCourseList(Map query) {
		return sqlSession.selectList("course.viewStatisticCourseList", query);
	}

	/**
	 * 
	 * @description 根据课程编码查询课程ID
	 * @author libo
	 * @date 2014-9-22 上午1:05:36
	 * 
	 * @param courCode
	 *            课程编码
	 * @return
	 */
	public int getCourIdByCode(String courCode) {
		return (Integer)sqlSession.selectOne("course.getCourIdByCode", courCode);
	}

	/**
	 * 
	 * @description 根据课程编码查询课程
	 * @author libo
	 * @date 2014-9-22 上午1:05:36
	 * 
	 * @param courCode
	 *            课程编码
	 * @return
	 */
	public Course getCourByCode(String courCode) {
		return sqlSession.selectOne("course.getCourByCode", courCode);
	}

	/**
	 * 查询课程名称
	 * 
	 * @param courseId
	 *            参数可选，非正表示查询所有课程
	 * @return
	 */
	public List<Course> viewCourseName(int courseId) {
		return sqlSession.selectList("course.viewCourseName", courseId);
	}

	/**
	 * 删除courIds所对应的选课信息表
	 * 
	 * @author libo
	 * 
	 * @param courseId
	 *            删除的课程id
	 * 
	 * @return 删除的信息条数
	 */
	public Integer deleteSelectInfo(List<Integer> courIds) {
		int count = 0;
		count = sqlSession
				.delete("courseSelectInfo.deleteByCourId", courIds);
		return Integer.valueOf(count);
	}

	public List<CourseModel> selectCourseByIds(List<Integer> ids) {
		return sqlSession.selectList("course.selectCourseInfoByIds", ids);
	}
}
