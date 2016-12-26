package com.swust.kelab.repos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.swust.kelab.domain.Course;
import com.swust.kelab.domain.ReSelectCourse;
import com.swust.kelab.model.CourseLearnModel;
import com.swust.kelab.model.CourseModel;
import com.swust.kelab.model.CourseSelectInfoModel;
import com.swust.kelab.model.CourseSelectModel;
import com.swust.kelab.model.LearningProgressModel;
import com.swust.kelab.model.PlatformStatisticModel;
import com.swust.kelab.model.SelectCourseModel;
import com.swust.kelab.model.StudentInfoModel;
import com.swust.kelab.repos.bean.ListQuery;
import com.swust.kelab.repos.bean.Query;

/**
 * Re_SelectCourse表的数据库访问类
 * 
 * @author ZhangXin
 * @author EasonLian
 * 
 */
@Repository("reSelectCourseDao")
public class ReSelectCourseDAO {
	final Logger logger = LoggerFactory.getLogger(getClass());

	private SqlSession sqlSession;

	@Autowired
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	/**
	 * 更新学生课程的详细记录<br>
	 * 
	 * @param rsco
	 * @author EasonLian
	 * @throws Exception
	 */
	public void updateOneLoginAndLearningtimeScore(ReSelectCourse rsco)
			throws Exception {
		sqlSession.update("reSelectCourse.updateOneRsco", rsco);
	}

	/**
	 * 按照课程id、用户id查询ReSelectCourseDAO<br>
	 * 
	 * @param courId
	 * @param userId
	 * @author EasonLian
	 * @return ReSelectCourse
	 */
	public ReSelectCourse selectOneByUserIdAndUserId(Integer courId,
			Integer userId) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("courId", courId);
		params.put("userId", userId);
		return sqlSession.selectOne(
				"reSelectCourse.selectOneByUserIdAndCourId", params);
	}

	/**
	 * 查询id所属课程
	 * 
	 * @author ZhangXin
	 * @param courseId
	 * @return CourseModel
	 */
	public CourseModel findOneCourse(int courseId) {
		return sqlSession.selectOne("course.selectOneCourse", courseId);
	}

	/**
	 * 查询所有已选择的课程
	 * 
	 * @param id
	 * @return
	 */
	public List<SelectCourseModel> findSelectCourseByUser(int id) {
		return sqlSession.selectList("reSelectCourse.sc", id);
	}

	/**
	 * 通过选课表的id查询特定的选课记录
	 * 
	 * @author ZhangXin
	 * @param reselectId
	 *            选课记录id
	 * @return CourseModel
	 */
	public CourseModel findSelectedCertainCourse(int reselectId) {
		return sqlSession.selectOne("reSelectCourse.selectedCertainCourse",
				reselectId);
	}

	/**
	 * 分页查询课程
	 * 
	 * @author ZhangXin
	 * @return List<CourseModel>
	 */
	public List<CourseModel> findAllCourse(Map<String, Object> sande) {
		return sqlSession.selectList("reSelectCourse.allcourse", sande);
	}

	/**
	 * 查询id所属用户的用户信息
	 * 
	 * @author ZhangXin
	 * @param id
	 * @return StudentInfoModel
	 */
	public StudentInfoModel findStudentInfo(int id) {
		return sqlSession.selectOne("domain.student.info", id);
	}

	/**
	 * 通过id查询一条选课记录
	 * 
	 * @param id
	 * @return
	 */
	public ReSelectCourse findOneCourceById(Map<String, Object> id) {
		return sqlSession.selectOne("reSelectCourse.findOneCourceById", id);
	}

	/**
	 * 更改选课记录的有效性
	 * 
	 * @author LuoHui
	 * @param id
	 * @return int 标识删除是否成功
	 */
	public int changeScValidState(Map<String, Object> id) {
		return sqlSession.update("reSelectCourse.changeScValidState", id);
	}

	/**
	 * 插入一条选课记录
	 * 
	 * @author LuoHui
	 * @param rsc
	 * @return
	 */
	public int insertSelectCourse(ReSelectCourse rsc) {
		return sqlSession.insert("reSelectCourse.insertSelectCourse", rsc);
	}

	/**
	 * 查询指定学生所选的课程
	 * 
	 * @param stuId
	 *            学生id
	 * @return 课程列表
	 * @author 吴岘辉
	 * @see Course
	 */
	public List<Course> viewSelectedCourses(Map query) {
		try {
			return sqlSession.selectList("reSelectCourse.queryCourseByStudent",
					query);

		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e.getLocalizedMessage());
		}
		return null;
	}

	/**
	 * 统计课程的选课人数
	 * 
	 * @param courseIds
	 *            统计的课程id列表，若为空，则统计所有课程
	 * @return 选课人数统计信息
	 * @author 吴岘辉
	 * @see CourseSelectInfoModel
	 */
	public List<CourseSelectInfoModel> staCourseSelectInfo(
			List<Integer> courseIds) {
		return sqlSession.selectList("reSelectCourse.staCourseSelectInfo",
				courseIds);
	}

	/**
	 * 统计学生所选课程的学习进度
	 * 
	 * @param userId
	 *            学生的id
	 * @return 学习进度信息
	 * @see LearningProgressModel
	 */
	public List<LearningProgressModel> staLearningProgress(int userId) {
		return sqlSession.selectList("reSelectCourse.staLearningProgress",
				userId);
	}

	/**
	 * 通过课程名称查询课程列表
	 * 
	 * @author ZhangXin
	 * 
	 * @param courseName
	 *            不完整或者完整的课程名
	 * @return List<CourseModel>
	 */
	public List<CourseModel> findCourseByName(Query query) {
		// return sqlSession.selectList("course.selectCourseByName",
		// "%"+courseName+"%");
		return sqlSession.selectList("course.selectCourseByNameQuery", query);
	}

	/**
	 * 增加指定选课的论坛交互数
	 * 
	 * @param query
	 *            查询条件，包括学生id、课程id等
	 * 
	 * @return 影响的数据库行数
	 */
	public int addBbsDiscussNum(Map query) {
		Integer i = sqlSession.update("reSelectCourse.addBbsDiscussNum", query);
		return i.intValue();
	}

	/**
	 * 根据查询条件,统计选课总数
	 * 
	 * @param query
	 *            查询条件对象
	 * @return 选课总数
	 */
	public int countSelectNumByQuery(Map query) {
		Integer i = (Integer) sqlSession.selectOne(
				"reSelectCourse.countTotalNum", query);
		return i.intValue();
	}

	/**
	 * 根据条件 查询选课统计信息(学生 课程 学习进度)
	 * 
	 * @param query
	 *            查询对象
	 * @return 选课统计列表
	 */
	public List<CourseSelectModel> viewCourseSelectList(Map query) {
		return sqlSession.selectList("reSelectCourse.viewList", query);
	}

	/**
	 * 更新学生的集中学习成绩
	 * 
	 * @param data
	 * @return
	 * @author lujoCom
	 */
	public Integer updateStudentsMassLearningScore(Map<String, Object> data) {
		return sqlSession.update(
				"reSelectCourse.updateStudentMassedLearnScore", data);
	}

	/**
	 * 减少某学生对应所选的某门课程的讨论数 *
	 * 
	 * @param query
	 *            查询条件，包含了学生id和课程id
	 * @return 影响的记录条数
	 */
	public int reduceBbsDiscussNum(Map query) {
		Integer i = sqlSession.update("reSelectCourse.reduceBbsDiscussNum",
				query);
		return i.intValue();
	}

	/**
	 * 根据学生id查询学生所有课程的学分
	 * 
	 * @param query
	 * @return
	 */
	public Integer selectCreditOfStudent(Map<String, Object> query) {
		return sqlSession.selectOne("reSelectCourse.selectCreditOfStudent",
				query);
	}

	/**
	 * 根据选课学习情况，统计整个平台的学习次数和学习时间
	 * 
	 * @return
	 */
	public PlatformStatisticModel staTotalLearnInfo() {
		return sqlSession.selectOne("reSelectCourse.staTotalLearnInfo");
	}

	/**
	 * 根据选课学习情况，按学校分别统计学习次数和学习时间
	 * 
	 * @return
	 */
	public List<PlatformStatisticModel> staSchoolLearnInfo() {
		return sqlSession.selectList("reSelectCourse.staSchoolLearnInfo");
	}

	/**
	 * 更新相应选课记录的成绩
	 * 
	 * @param query
	 *            包含选课的id和最新总分
	 * @return
	 */
	public Integer updateTotalScoreById(List<ReSelectCourse> query) {
		return sqlSession.update("reSelectCourse.updateTotalScore", query);
	}

	/**
	 * 获取相关课程的所有选课记录及成绩信息
	 * 
	 * @param query
	 *            相应的课程id
	 * @return
	 */
	public List<ReSelectCourse> attainCourseSelectInfo(Map query) {
		return sqlSession.selectList("reSelectCourse.attainCourseSelectInfo",
				query);
	}

	/**
	 * 更新选课表的冗余字段（机构和用户信息）
	 * 
	 * @param query
	 * @return
	 */
	public Integer updateUserDepaInfo(Map query) {
		return sqlSession.update("reSelectCourse.updateUserDepaInfo", query);
	}

	/**
	 * 更新选课表的在线测试分数
	 * 
	 * @param query
	 * @return
	 */
	public Integer updateUserTestScore(Map query) {
		return sqlSession.update("reSelectCourse.updateUserTestScore", query);
	}

	/**
	 * 统计相应课程的学习时间和学习次数
	 * 
	 * @param courIds
	 *            相应的课程id列表
	 * @return 相应课程的学习情况
	 */
	public List<CourseLearnModel> staCourseLearnState(List<Integer> courIds) {
		return sqlSession.selectList("reSelectCourse.staCourseLearnState",
				courIds);
	}
	
	/**
	 * 统计每个学校每个年级针对每门课程的学习情况
	 * @param query
	 * @return
	 */
	public List<CourseLearnModel> staCourseLearnStateByGrade(Map query) {
	    return sqlSession.selectList("reSelectCourse.staCourseLearnStateByGrade",
	            query);
	}

	/**
	 * 
	 * @description 根据查询条件获取学习时间等信息
	 * @author libo
	 * @date 2014-9-19 上午12:43:55
	 * 
	 * @param myQuery
	 *            查询条件，包括课程、机构信息
	 * @return
	 */
	public List<CourseLearnModel> staCourseLearnState(ListQuery myQuery) {
		return sqlSession.selectList(
				"reSelectCourse.staCourseLearnStateByQuery", myQuery);
	}

	/**
	 * 根据条件 查询学生成绩导出的相关信息(学生 课程 学习进度等)
	 * 
	 * @param query
	 *            查询对象
	 * @return 选课统计列表
	 */
	public List<CourseSelectModel> viewExportCourseSelectList(Map query) {
		return sqlSession.selectList("reSelectCourse.viewExportList", query);
	}

	/**
	 * 确认一个选课成绩
	 * 
	 * @param query
	 * @return
	 */
	public Integer confirmOneScore(Map query) {
		return sqlSession.update("reSelectCourse.confirmOneScore", query);
	}

}
