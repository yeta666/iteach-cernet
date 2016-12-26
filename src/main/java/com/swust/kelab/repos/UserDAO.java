package com.swust.kelab.repos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.swust.kelab.domain.ReSelectCourse;
import com.swust.kelab.domain.User;
import com.swust.kelab.model.GradeStaticModel;
import com.swust.kelab.model.StudentMassedLearningInfoModel;
import com.swust.kelab.model.TeacherModel;
import com.swust.kelab.model.UserManageBackInfo;
import com.swust.kelab.model.UserModel;
import com.swust.kelab.model.UserPersonalInfo;
import com.swust.kelab.repos.bean.ListQuery;

@Repository()
public class UserDAO {

	private SqlSession sqlSession;

	/** Injected by Spring */
	@Autowired
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	/**
	 * 传入查询user的条件返回获得user信息，对应多个User结果
	 * 
	 * @author yangZQ
	 * @param user
	 * @return
	 */
	public List<User> findUsersByUser(User user) {
		return sqlSession.selectList("User.findUsersByUser", user);
	}

	/**
	 * 登陆与退出登陆重新设置用户的状态
	 * 
	 * @author yangzq
	 * @param user
	 */
	public void updateUserState(User user) {
		sqlSession.update("User.updateUserState", user);
	}

	public User selectOneUserById(Integer userId) throws Exception {
		return sqlSession.selectOne("User.selectOneUserById", userId);
	}

	public User findOneUser(int userId) {
		return sqlSession.selectOne("User.findOneUser", userId);
	}

	public void modifyUserPwd(User user) throws Exception {
		sqlSession.update("User.updateUserPwd", user); 
	}

	/**
	 * 添加用户成功后返回用户id
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public Integer insertUser(User user) throws Exception {
		sqlSession.insert("User.insertUser", user);
		return user.getUserId();
	}

	/**
	 * 修改用户信息
	 * 
	 * @param user
	 * @return 返回修改的行数
	 */
	public Integer modifyUserInfo(User user) {
		return sqlSession.update("User.updateUserPwd", user);
	}

	/**
	 * 根据用户id的数组，实现批量删除用户信息
	 * 
	 * @param userIds
	 * @return
	 */
	public Integer deleteUserInfo(String[] userIds) {
		return sqlSession.delete("User.deleteUserInfo", userIds);
	}

	/**
	 * 通过角色id，获取用户基本信息及所在部门名称
	 * 
	 * @param roleId
	 *            角色id
	 * @return 用户列表
	 * @see TeacherModel
	 */
	public List<TeacherModel> viewUserByRole(int roleId) {
		return sqlSession.selectList("User.viewUserByRole", roleId);
	}

	/**
	 * 查询当前在线人数
	 * 
	 * @return 在线人数
	 */
	public int staOnlineUserNum() {
		Integer convert = sqlSession.selectOne("User.staOnlineUserNum");
		return convert.intValue();
	}

	/**
	 * 统计总的教师数
	 * 
	 * @return 教师数
	 */
	public int staTeacherNum() {
		Integer teacherNum = sqlSession.selectOne("User.staTeacherNum");
		return teacherNum.intValue();
	}

	/**
	 * 统计总的学生数
	 * 
	 * @return 学生数
	 */
	public int staStudentNum() {
		Integer stuNum = sqlSession.selectOne("User.staStudentNum");
		return stuNum.intValue();
	}
	
	/**
	 * 统计每个年级的学生数
	 * 
	 * @return 学生数
	 */
	public List<GradeStaticModel> staGradeStudentNum() {
	    return sqlSession.selectList("User.staGradeStudentNum");
	}

	/**
	 * 传入多个user的id，返回对应多个User结果
	 * 
	 * @author Pery
	 * @param list
	 * @return 用户结果
	 */
	public List<TeacherModel> findUsersByUserIds(List userIds) {
		return sqlSession.selectList("User.findUsersByUserIds", userIds);
	}

	/**
	 * 通过过滤信息查询用户人数
	 * 
	 * @param query
	 * @return
	 */
	public int findUserCountByQuery(ListQuery query) {
		Integer in = sqlSession.selectOne("User.findUserCountByQuery", query);
		return in.intValue();
	}

	/**
	 * 通过过滤信息查询用户集合
	 * 
	 * @param query
	 * @return
	 */
	public List<UserManageBackInfo> findUserByQuery(ListQuery query) {
		return sqlSession.selectList("User.findUserByQuery", query);
	}

	/**
	 * 通过角色id和机构id获取用户列表(id和realname),供筛选使用
	 * 
	 * @param roleId
	 *            角色id,非正数表示所有角色
	 * @return 用户列表
	 */
	public List<User> viewUsersByRole(Map query) {
		return sqlSession.selectList("User.viewUsersByRole", query);
	}

	/**
	 * 通过学校年级班级查询学生信息
	 * 
	 * @param query
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<StudentMassedLearningInfoModel> findStudentByIds(Map query) {
		return sqlSession.selectList("User.findStudentByIds", query);
	}

	/**
	 * 计算通过学校年级班级查询学生信息的信息总条数
	 * 
	 * @param query
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Integer tatolStudentByIds(Map query) {
		return sqlSession.selectOne("User.totalStudentByIds", query);
	}

	/**
	 * 根据用户类型和所属机构等获取用户列表
	 * 
	 * @param userType
	 *            用户类型
	 * @return 用户列表
	 */
	public List<User> viewUsersByTypeAndDepa(Map query) {
		return sqlSession.selectList("User.viewUsersByTypeAndDepa", query);
	}
	
	public List<User> viewAllStudentList() {
		return sqlSession.selectList("User.viewAllStudent");
	}

	/**
	 * 修改单个User部分信息
	 * 
	 * @author ZhangXin
	 * @param info
	 * @return
	 */
	public boolean modifyPersonalInfo(UserPersonalInfo info) throws Exception {
		int rowEff = sqlSession.update("User.modifyPersonalInfo", info);
		return rowEff > 0 ? true : false;
	}

	public boolean modifyPersonVerify(Map<String, Object> data) {
		int rowEff = sqlSession.update("User.modifyPersonVerify", data);
		return rowEff > 0 ? true : false;
	}

	/**
	 * 通过用户id查询user表，及department表
	 * @param userId
	 * @return UserModel
	 * @author easonlian
	 * @throws Exception
	 */
	public UserModel viewOneUserByUserId(Integer userId) throws Exception {
		return sqlSession.selectOne("User.viewOneUserByUserId",userId);
	}

	public List<User> confirmUserInfo( String userRealname, 
			Integer depaId,String userIdNum,Integer gradId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("userRealname", userRealname);
		params.put("depaId", depaId);
		params.put("userIdNum", userIdNum);
		params.put("gradId", gradId);
		return sqlSession.selectList("User.confirmUserInfo",params);
	}
	
	/**
	 * 获取相应的教师的个人信息
	 * 
	 * @param teacherIds 指定的教师id列表
	 * @return 信息列表
	 */
	public List<TeacherModel> viewAllMajorTeachers(List<Integer> teacherIds){
	    return sqlSession.selectList("User.viewUserInfoByIds",teacherIds);
	}
	
	/**
	 * 查询指定用户的选课时需要的额外信息
	 * @param u 指定用户
	 * @return  额外信息
	 */
	public ReSelectCourse viewUserScInfo(User u){
	    return sqlSession.selectOne("viewUserScInfo", u);
	}
}
