package com.swust.kelab.service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.swust.kelab.domain.Course;
import com.swust.kelab.domain.Department;
import com.swust.kelab.domain.ReUserRole;
import com.swust.kelab.domain.Role;
import com.swust.kelab.domain.StudentMassedLearning;
import com.swust.kelab.domain.User;
import com.swust.kelab.model.ClasModel;
import com.swust.kelab.model.CommonQuery;
import com.swust.kelab.model.PageData;
import com.swust.kelab.model.QueryData;
import com.swust.kelab.model.StudentMassedLearningInfoModel;
import com.swust.kelab.model.TeacherModel;
import com.swust.kelab.model.UserManageBackInfo;
import com.swust.kelab.model.UserPersonalInfo;
import com.swust.kelab.model.UserSearchInfoModel;
import com.swust.kelab.repos.BbsPostDAO;
import com.swust.kelab.repos.CourseDAO;
import com.swust.kelab.repos.DepartmentDao;
import com.swust.kelab.repos.GradeDao;
import com.swust.kelab.repos.MassedLearningDao;
import com.swust.kelab.repos.ReSelectCourseDAO;
import com.swust.kelab.repos.ReUserRoleDAO;
import com.swust.kelab.repos.UserDAO;
import com.swust.kelab.repos.bean.ListQuery;
import com.swust.kelab.utils.AuthenticationInformation;
import com.swust.kelab.utils.MD5;

/**
 * Dependence on {@link com.swust.kelab.repos.UserDAO}<br>
 * <br>
 * 
 * @version 1.0
 * @author Easonlian
 * @see com.swust.kelab.repos.UserDAO
 */
@Service()
public class UserService {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ReUserRoleDAO reUserRoleDAO;
	
	@Autowired
	private BbsPostDAO bbsPostDAO;
	
	@Autowired
	private ReSelectCourseDAO reSelectCourseDAO;

	private UserDAO userDAO;
	
	@Autowired
	private DepartmentDao departmentDao;

	private MassedLearningDao massedLearningDao;

	@Autowired
	public void setMassedLearningDao(MassedLearningDao massedLearningDao) {
		this.massedLearningDao = massedLearningDao;
	}

	/** Injected by Spring */
	@Autowired
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	@Autowired
	private CourseDAO courseDAO;
	
	@Autowired
	private GradeDao gradeDAO;
	
	/**
	 * 添加用户成功在返回的map中的status设为1，
	 * 添加用户中用户信息中关键信息例如学号，考籍号以及邮箱已存在时，返回的map中的status设为2，
	 * 添加用户异常信息，返回的map中的status设为3
	 * 
	 * @param user
	 * @return whether the INSERET operation was successful.
	 * @Author EasonLian
	 */
	@SuppressWarnings({ "finally" })
	@Transactional
	public Map<String, Object> insertUser(User user) {
		Map<String, Object> insertResultInfo = AuthenticationInformation
				.authenticateUserInfo(user);
		if (insertResultInfo == null) {
			User user_login = new User();
			user_login.setUserLoginname((user.getUserLoginname()));
			insertResultInfo = new HashMap<String, Object>();
			try {
				insertResultInfo.put("authenticationInfo", "");
				if (userDAO.findUsersByUser(user_login).size() == 0) {
					Integer userId = userDAO.insertUser(user);
					//
					ReUserRole userRole = new ReUserRole();
					int roleId = null!=user.getUserType()&&user.getUserType()==2?2:1;
					userRole.setReroRoleId(roleId);
					userRole.setReroUserId(userId);
					userRole.setReroTime(new Date());
					reUserRoleDAO.saveUserRole(userRole);
					insertResultInfo.put("status", "1");
				} else {
					insertResultInfo.put("status", "2");
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.debug("userService ----> insert" + e.getMessage());
				insertResultInfo.put("status", "3");
			} finally {
				return insertResultInfo;
			}
		} else {
			return insertResultInfo;
		}
	}

	/**
	 * 用户管理-添加新用户
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public Map<String, Object> addNewUserToDB(User user, String userRoles)
			throws Exception {
		// 对将要添加的用户信息进行正则表达式的验证
		Map<String, Object> addNewUserResult = AuthenticationInformation
				.authenticateUserInfo(user);

		// 验证通过
		if (addNewUserResult == null) {
			Integer userId = userDAO.insertUser(user);
			addNewUserResult = new HashMap<String, Object>();
			if (userId != null) {
				List<ReUserRole> role = new LinkedList<ReUserRole>();
				String[] roles = userRoles.split(",");
				Integer mainRole = user.getUserType();
				for (String ele : roles) {
					Integer val = Integer.valueOf(ele);
					if (val.equals(mainRole))
						continue;
					ReUserRole ru = new ReUserRole();
					ru.setReroRoleId(val);
					ru.setReroTime(new Date());
					ru.setReroUserId(userId);
					role.add(ru);
				}
				ReUserRole ru = new ReUserRole();
				ru.setReroRoleId(mainRole);
				ru.setReroTime(new Date());
				ru.setReroUserId(userId);
				role.add(ru);
				reUserRoleDAO.saveUserRole(role);
				addNewUserResult.put("addNewUserInfo", "添加用户成功!");
			} else {
				addNewUserResult.put("addNewUserInfo", addNewUserResult);
			}
		}

		return addNewUserResult;
	}

	public Map<String, Object> modifyUserInfo(User user, String roleDele,
			String roleAdd) {
	    //获取原来的用户信息
	    Map<String, Object> map = new HashMap<String, Object>();
	    User oriUser=null;
	    try {
            oriUser=userDAO.findOneUser(user.getUserId());
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("findOneUser faild!\n"+e.getLocalizedMessage());
        }
	    if(oriUser==null){
	        map.put("modifyInfo", "修改失败");
            return map;
	    }
		String[] roleDeles = roleDele.split(",");
		if (roleDele.length() > 0) {
			Map<String, Object> deleteMap = new HashMap<String, Object>();
			Integer[] roleUserIds = new Integer[1];
			roleUserIds[0] = user.getUserId();
			deleteMap.put("roleUserIds", roleUserIds);
			deleteMap.put("roleDeles", roleDeles);
			reUserRoleDAO.deleteUserRole(deleteMap);
		}

		List<ReUserRole> addList = new ArrayList<ReUserRole>();
		String[] roleAdds = roleAdd.split(",");
		if (roleAdd.length() > 0) {
			for (String s : roleAdds) {
				ReUserRole userRole = new ReUserRole();
				userRole.setReroUserId(user.getUserId());
				userRole.setReroRoleId(Integer.parseInt(s));
				userRole.setReroTime(new Date());
				addList.add(userRole);
			}
			reUserRoleDAO.saveUserRole(addList);
		}

		Integer row = userDAO.modifyUserInfo(user);
		if (row == null) {
			map.put("modifyInfo", "修改失败");
			return map;
		}
		map.put("modifyInfo", "修改成功");
		
		//同步选课表和bbs表的冗余字段
		boolean bbpoChange=false;
		boolean rscoChange=false;
		Map query=new HashMap();
		query.put("userId", user.getUserId());
		Integer newDepaId=user.getUserDepaId();
		String newName=user.getUserRealname();
		if(newDepaId!=oriUser.getUserDepaId()&&newDepaId!=null){
		    query.put("depaId", newDepaId);
		    Department depart=departmentDao
		            .queryDepartmentById(newDepaId);
		    if(depart!=null){
		        query.put("depaName", depart.getDepaName());
		    }
		    bbpoChange=true;
		    rscoChange=true;
		}
		if(newName!=oriUser.getUserRealname()&&newName!=null){
		    query.put("userName", user.getUserRealname());
		    bbpoChange=true;
		}
		if(oriUser.getUserType()==1){
		    Integer newClasId=user.getUserClasId();
		    if(newClasId!=oriUser.getUserClasId()&&newClasId!=null){
		        Map q=new HashMap();
		        q.put("classId", newClasId);
		        List<ClasModel> classes=gradeDAO.queryClassGrade(q);
		        if(classes!=null&&classes.size()>0){
		            ClasModel clas=classes.get(0);
		            query.put("className", clas.getClasName());
		            query.put("gradeId", clas.getGradeId());
		            query.put("gradeName", clas.getGradeName());
		            rscoChange=true;
		        }
		    }
		}
		//更新主贴表
		if(bbpoChange){
		    try {
                bbsPostDAO.updateDepaCourInfo(query);
            } catch (Exception e) {
                // TODO: handle exception
                logger.error("updateDepaCourInfo faild!\n"+e.getLocalizedMessage());
            }
		}
		
		//更新选课表
		if(rscoChange){
		    try {
                reSelectCourseDAO.updateUserDepaInfo(query);
            } catch (Exception e) {
                // TODO: handle exception
                logger.error("updateUserDepaInfo faild!\n"+e.getLocalizedMessage());
            }
		}
		
		return map;
	}

	/**
	 * @param userId
	 * @return get all info of the logged user.
	 * @author EasonLian
	 */
	@Transactional
	public User findOneUser(int userId) {
		return userDAO.findOneUser(userId);
	}

	/**
	 * @param oldPwd
	 * @param newPwd
	 * @param userId
	 * @return whether modify was successful.
	 * @author EasonLian
	 */
	@Transactional
	public boolean modifyUserPwd(String oldPwd, String newPwd, int userId) {
		try {
			User user = userDAO.findOneUser(userId);
			// contract the oldPwd is correct,then,update new password
			if (user.getUserPwd().equals(oldPwd)) {
				user.setUserPwd(newPwd);
				userDAO.modifyUserPwd(user);
				return true;
			} else
				return false;
		} catch (Exception e) {
			logger.error("modifyUserPwd error!");
			return false;
		}
	}

	@Transactional
	public List<User> findUsersByUser(User user) {
		return userDAO.findUsersByUser(user);
	}

	/**
	 * 
	 * @param commonquery
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public QueryData searchUsersInfo(CommonQuery commonquery,
			UserSearchInfoModel userSearch) throws Exception {

		QueryData queryData = new QueryData();
		// 构造查询条件
		ListQuery query = commonquery.format();
		String search = commonquery.getSearchWord();
		if (null != search)
			search = "%" + search + "%";
		query.fill("searchModel", userSearch);
		int totalCount = userDAO.findUserCountByQuery(query);
		queryData.setTotalCount(totalCount);
		if (totalCount == 0) {
			return queryData;
		}
		if (commonquery.getRecordPerPage() <= 0) {
			commonquery.setRecordPerPage(6);
		}
		query.fill("maxCount", commonquery.getRecordPerPage());
		// query.fill("userVerify", new Integer(1));
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
			int pagef = commonquery.getPageArray()[i];
			if (pagef <= 0 || pagef > totalPage) {
				continue;
			}
			query.fill(
					"startIndex",
					QueryData.computeStartIndex(pagef,
							commonquery.getRecordPerPage()));
			List<UserManageBackInfo> userModel = userDAO.findUserByQuery(query);
			pageDataList.add(new PageData(pagef, userModel));
		}
		// 装载返回结果
		queryData.setPageData(pageDataList);
		return queryData;
	}

	/**
	 * 根据角色id和机构id,获取用户列表
	 * 
	 * @param roleId
	 *            角色id
	 * @param depaId
	 *            机构id
	 * @return 用户列表
	 */
	public List<User> viewUsersByRole(int roleId, int depaId) {
		List<User> result = null;
		try {
			Map query = new HashMap();
			if (roleId > 0) {
				query.put("roleId", roleId);
				query.put("depaId", depaId);
			}
			result = userDAO.viewUsersByRole(query);
		} catch (Exception e) {
			logger.error("viewUsersByRole error!\n" + e.getMessage());
		}
		return result;
	}

	/**
	 * 查询学生的基本信息，用于给集中学习添加学生，查询时
	 * 
	 * @param commonQuery
	 * @param smlInfo
	 * @return
	 * @author lujoCom
	 * @throws Exception
	 */
	public QueryData findStudentByIds(CommonQuery commonQuery,
			StudentMassedLearningInfoModel smlInfo) throws Exception {
		QueryData queryData = new QueryData();
		//构建查询条件
		ListQuery query = commonQuery.format();
		query.fill("studentName", smlInfo.getStuName());
		query.fill("classId", smlInfo.getStuClasId());
		query.fill("gradeId", smlInfo.getStuGradeId());
		query.fill("departId", smlInfo.getStuSchoolId());
		query.fill("courseId", smlInfo.getStuCourseId());
		//查询总条数
		int totalCount = userDAO.tatolStudentByIds(query);
		queryData.setTotalCount(totalCount);
		if (totalCount <= 0) {
			//总条数为0时，直接将空数据返回
			return queryData;
		}
		//设置每页显示条数
		if (commonQuery.getRecordPerPage() <= 0) {
			commonQuery.setRecordPerPage(10);
		}
		
		//将每页显示最大条数构建在查询条件中
		query.fill("maxCount", commonQuery.getRecordPerPage());
		//计算当前一共多少页
		int totalPage = QueryData.computeTotalPage(totalCount,
				commonQuery.getRecordPerPage());
		//将总页数构建在返回数据中
		queryData.setTotalPage(totalPage);
		
		List<PageData> pageDataList = Lists.newArrayList();
		
		//默认显示第1,2,3页，用作缓冲数据
		if (commonQuery.getPageArray() == null) {
			commonQuery.setPageArray(new int[] { 1, 2, 3 });
		}
		//循环当前缓冲页
		for (int index = 0; index < commonQuery.getPageArray().length; index++) {
			int indexPage = commonQuery.getPageArray()[index];
			//当当前页数大于总页数或者当前页数为0时进入下次循环
			if (indexPage > totalPage || indexPage <= 0)
				continue;
			//将当前页数的起始索引构建在查询条件中
			query.fill(
					"startIndex",
					QueryData.computeStartIndex(indexPage,
							commonQuery.getRecordPerPage()));
			//将学生信息构建在当前页的返回数据中
			List<StudentMassedLearningInfoModel> students = userDAO
					.findStudentByIds(query);

			Map<String, Object> data = new HashMap<String, Object>();
			data.put("pageData", students);
			//构造查询有实践冲突的学生的查询条件
			//将集中学习的开始时间的字符串形式转换成date类型
			if (StringUtils.isNotBlank(smlInfo.getStuMaleStartTimeStr())) {
				smlInfo.setStuMaleStartTime(DateUtils.parseDate(
						smlInfo.getStuMaleStartTimeStr(), "yyyy-MM-dd HH:mm"));
			}
			//将集中学习的结束时间的字符串形式转换成date类型
			if (StringUtils.isNotBlank(smlInfo.getStuMaleEndTimeStr())) {
				smlInfo.setStuMaleEndTime(DateUtils.parseDate(
						smlInfo.getStuMaleEndTimeStr(), "yyyy-MM-dd HH:mm"));
			}
			//将每页中与此集中学习的有时间冲突的学生查出来，返回给前台
			data.put(
					"conflictMassed",
					findConflictMassedLearning(students,
							smlInfo.getStuMaleStartTime(),
							smlInfo.getStuMaleEndTime(), smlInfo.getStuMaleId()));
			//将已经参加集中学习的学生构建在每页返回数据中
			String attendStudent = findAttendedMassedLearningStudent(smlInfo
					.getStuMaleId());
			data.put("attendedMassedLearningStuIds", attendStudent);
			//将每页的查询结果构建在所有页的查询结果中
			pageDataList.add(new PageData(indexPage, data));
		}
		queryData.setPageData(pageDataList);

		return queryData;
	}

	/**
	 * 返回时间冲突的学生id，义字符串的形式返回
	 * 
	 * @param students
	 * @param startTime
	 * @param endTime
	 * @return 有时间冲突的学生id
	 * @author lujoCom
	 */
	private String findConflictMassedLearning(
			List<StudentMassedLearningInfoModel> students, Date startTime,
			Date endTime, Integer maleId) {

		String stuNum = "";
		Integer[] stuIds = new Integer[students.size()];
		Integer index = 0;
		//将所有学生id遍历出来，存放在学生id的Integer数组中
		for (StudentMassedLearningInfoModel smlInfoModel : students) {
			stuIds[index++] = smlInfoModel.getStuId();
		}
		//构造查询时间冲突的查询条件
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("startTime", startTime);
		data.put("endTime", endTime);
		data.put("stuIds", stuIds);
		data.put("maleId", maleId);
		
		//查询时间冲突的学生
		List<StudentMassedLearningInfoModel> confilctMasseds = massedLearningDao
				.findConflictMassedLearning(data);

		if (confilctMasseds.size() == 0) {
			return "";
		}
		//遍历查询结果，将有冲突的学生id构造在一个字符串中
		for (StudentMassedLearningInfoModel student : students) {
			int stuId = student.getStuId();
			for (StudentMassedLearningInfoModel confilctMassed : confilctMasseds) {
				if (stuId == confilctMassed.getStuId()) {
					stuNum += stuId + ",";
				}
			}
		}

		return stuNum;
	}

	/**
	 * 查询已经参见集中学习课程的学生id以及学生名字<br>
	 * 格式如下：2463,张雨柔@2469,李媛媛@2477,李嘉豪
	 * 
	 * @param maleId
	 * @return
	 * @author lujoCom
	 */
	public String findAttendedMassedLearningStudent(Integer maleId) {
		StudentMassedLearning sml = new StudentMassedLearning();
		sml.setSmleMaleId(maleId);
		//查询已经参加此集中学习的学生名单
		List<StudentMassedLearningInfoModel> studentInfos = massedLearningDao
				.findStudentMassedLearning(sml);
		
		StringBuffer stuIds = new StringBuffer();
		
		if (studentInfos.size() == 0) {
			stuIds.append("");
			return stuIds.toString();
		}
		//遍历结果，并将结果以固定的格式保存在一个字符串中
		for (StudentMassedLearningInfoModel s : studentInfos) {
			stuIds.append(s.getStuId() + "," + s.getStuName() + "@");
		}
		
		return stuIds.subSequence(0, stuIds.toString().lastIndexOf("@")).toString();
		
	}

	/**
	 * 获取用户对应的角色列表(角色id,角色名)
	 * 
	 * @param userId
	 * @return
	 */
	public List<Role> viewUserRoles(int userId) {
		List<Role> result = null;
		try {
			result = reUserRoleDAO.viewRoleListByUser(userId);
		} catch (Exception e) {
			logger.error("viewRoleListByUser error!\n" + e.getMessage());
		}
		return result;
	}

	/**
	 * 根据类型和所属机构获取用户列表
	 * 
	 * @param userType
	 *            用户类型
	 * @return 用户列表
	 * @see User
	 */
	public List<User> viewUsersByTypeAndDepa(int userType, int depaId) {
		List<User> result = null;
		try {
			Map query = new HashMap();
			if (userType > 0) {
				query.put("userType", userType);
			}
			if (depaId > 0) {
				query.put("depaId", depaId);
			}
			result = userDAO.viewUsersByTypeAndDepa(query);
		} catch (Exception e) {
			logger.error("viewUsersByType error!\n" + e.getMessage());
		}
		return result;
	}

	/**
	 * 修改单个User部分信息
	 * 
	 * @author ZhangXin
	 * @param info
	 * @return
	 */
	@SuppressWarnings("finally")
	@Transactional
	public Map<String, Object> modifyPersonalInfo(UserPersonalInfo info) {
		Map<String, Object> ma = new HashMap<String, Object>();
		try {
			if (userDAO.modifyPersonalInfo(info)) {
				ma.put("status", 1);
				ma.put("info", "修改成功");
				
			} else {
				ma.put("status", 2);
				ma.put("info", "修改失败");
			}
		} catch (Exception e) {
			ma.put("status", 3);
			ma.put("info", "修改用户信息出现异常");
			e.printStackTrace();
		} finally {
			return ma;
		}
	}

	/**
	 * 通过资料找回密码
	 * 
	 * @param userRealname
	 * @param depaId
	 * @param gradId
	 * @param clasId
	 * @author easonlian
	 */
	public String confirmUserInfo(String userRealname,
			Integer depaId, String userIdNum,Integer gradId) {
		try {
			List<User> userList = userDAO.confirmUserInfo(
					userRealname, depaId, userIdNum,gradId);
			if (userList.size() == 1) {
				User user = userDAO.findOneUser(userList.get(0).getUserId());
				user.setUserPwd(MD5.getMD5Str(user.getUserLoginname()));
				userDAO.modifyUserPwd(user);
				return user.getUserLoginname();
			} else
				return null;
		} catch (Exception e) {
			logger.error("confirmUserInfo error!");
			return null;
		}
	}
	
	/**
	 * 取得所有的学生
	 */
	public List<User> viewAllStudent() {
		List<User> result = null;
		try {
			result = userDAO.viewAllStudentList();
		} catch (Exception e) {
			logger.error("viewUsersByRole error!\n" + e.getMessage());
		}
		return result;
	}
	
	/**
	 * 获取主讲教师的信息（包括个人信息，和所授课程）
	 * 
	 * @param teaId 指定的教师id，若为非正整数，则获取所有主讲教师信息
	 * @return 主讲教师列表
	 */
	public List<TeacherModel> viewAllMajorTeachers(int teaId){
	    List<TeacherModel> result=new ArrayList<TeacherModel>();
	    //获取课程信息，从而找到主讲教师
	    List<Course> courses=null;
        try {
            courses=courseDAO.viewCourseBaseInfor();
        } catch(Exception e) {
            logger.error("viewCourseBaseInfor error!\n"+e.getLocalizedMessage()); 
            return null;
        }
        if(courses==null||courses.isEmpty()){
            return result;
        }   
        //通过课程的主讲教师信息，构造教师所主讲的课程列表
        Map<Integer,String> teacherCourses=new 
                HashMap<Integer, String>();           
        for (Course course : courses) {            
            String majorTeaIds=course.getCourTeacherIds();
            if(majorTeaIds==null||majorTeaIds.isEmpty()
                    ||!majorTeaIds.matches("^,(\\d+,)+$")){
                continue;
            }
            //去除首尾的符号
            majorTeaIds=majorTeaIds
                    .substring(1,majorTeaIds.length()-1);
            String ids[]=majorTeaIds.split(",");
            if(ids!=null&&ids.length>0){
                for (String id : ids) {
                    int teacherId=Integer.parseInt(id); 
                    if(teaId>0&&teacherId!=teaId){
                        continue;
                    }
                    String cours=teacherCourses.get(teacherId);
                    if(cours==null){
                        cours=course.getCourName();
                        teacherCourses.put(teacherId, cours);
                    }else{
                        teacherCourses.put(teacherId,
                                cours+","+course.getCourName());
                    }
                }
            }
        }
        if(teacherCourses.isEmpty()){
            return result;
        }
	    //获取主讲教师基本信息
        try {
            List<Integer> teacherIds=new ArrayList<Integer>(teacherCourses.keySet());
            result=userDAO.viewAllMajorTeachers(teacherIds);
        } catch(Exception e) {
            logger.error("viewAllMajorTeachers error!\n"+e.getLocalizedMessage()); 
            return null;
        }
	    //拼装信息
	    if(result!=null&&result.size()>0){
	        for (TeacherModel tm : result) {	            
                tm.setCourseNames(teacherCourses.get(tm.getTeacherId()));
                String filePath="";
                if(tm.getTeacherCoverLocation()==null){
                    tm.setTeacherCoverLocation("static/img/");
                }
                if(tm.getTeacherCoverFileName()==null){
                    tm.setTeacherCoverFileName("user.jpg");
                }
                if(tm.getTeacherRemark()==null
                        ||tm.getTeacherRemark().isEmpty()){
                    tm.setTeacherRemark("该教师暂无简介。");
                }
            }
	    }        
	    return result;
	}
	
	/**
	 * 获取服务器状态
	 * 
	 * @author MrYang
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	public Map<String, Object> serverStatus() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		int kb = 1024 * 1024;
		// 可使用内存
		long totalMemory = Runtime.getRuntime().totalMemory() / kb;
		// 剩余内存
		long freeMemory = Runtime.getRuntime().freeMemory() / kb;
		// 最大可使用内存
		long maxMemory = Runtime.getRuntime().maxMemory() / kb;
		// 操作系统
		String osName = System.getProperty("os.name");
		// 用户当前目录
		String userHome = System.getProperty("user.home");
		// JDK版本
		String jkdVersion = System.getProperty("java.specification.version");
		// JDK路径
		String jkdPath = System.getProperty("java.home");
		// 当前程序主目录
		String appDir = System.getProperty("user.dir");
		// 操作系统类型
		String osType = System.getProperty("os.arch");
		// 操作系统内部版本号
		String osVersion = System.getProperty("os.version");
		// 服务器IP
		InetAddress myIPaddress = null;
		try {
			myIPaddress = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String serIPAddr = myIPaddress.getHostAddress();
		// 系统存储容量
		// String
		// appdir=ServletActionContext.getServletContext().getRealPath("/");
		map.put("result", "1");
		map.put("totalmemory", totalMemory);
		map.put("freememory", freeMemory);
		map.put("maxmemory", maxMemory);
		map.put("osname", osName);
		map.put("userhome", userHome);
		map.put("jdkversion", jkdVersion);
		map.put("jdkpath", jkdPath);
		// map.put("appdir", appdir);
		map.put("ostype", osType);
		map.put("osversion", osVersion);
		map.put("servipaddr", serIPAddr);
		return map;

	}
}
