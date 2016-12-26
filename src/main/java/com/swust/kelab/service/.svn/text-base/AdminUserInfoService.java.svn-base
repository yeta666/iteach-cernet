package com.swust.kelab.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.swust.kelab.domain.Clas;
import com.swust.kelab.domain.Department;
import com.swust.kelab.domain.Grade;
import com.swust.kelab.domain.ReUserRole;
import com.swust.kelab.domain.User;
import com.swust.kelab.model.ClasModel;
import com.swust.kelab.model.UserModel;
import com.swust.kelab.repos.DepartmentDao;
import com.swust.kelab.repos.GradeDao;
import com.swust.kelab.repos.ReUserRoleDAO;
import com.swust.kelab.repos.RoleDao;
import com.swust.kelab.repos.UserDAO;
import com.swust.kelab.utils.CookieUtil;
import com.swust.kelab.utils.ExcelResolver_03;
import com.swust.kelab.utils.ExportAsExcel;
import com.swust.kelab.utils.MD5;

/**
 * 管理用户信息的service
 * 
 * @author lujoCom
 * 
 */
@Service()
public class AdminUserInfoService {

	final static Logger logger = LoggerFactory
			.getLogger(AdminUserInfoService.class);

	private RoleDao roleDao;

	private UserDAO userDAO;

	private GradeDao gradeDao;

	private ReUserRoleDAO userRoleDao;

	private DepartmentDao departmentDao;

	@Autowired
	public void setRoleDao(RoleDao roleDao) {
		this.roleDao = roleDao;
	}

	@Autowired
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	@Autowired
	public void setGradeDao(GradeDao gradeDao) {
		this.gradeDao = gradeDao;
	}

	@Autowired
	public void setUserRoleDao(ReUserRoleDAO userRoleDao) {
		this.userRoleDao = userRoleDao;
	}

	@Autowired
	public void setDepartmentDao(DepartmentDao departmentDao) {
		this.departmentDao = departmentDao;
	}

	/**
	 * 给用户添加权限
	 * 
	 * @param userId
	 *            用户id
	 * @param roleId
	 *            权限id
	 * @return
	 * @author lujoCom
	 */
	@Transactional
	public Map<String, Object> addRoleToUser(Integer userId, Integer roleId)
			throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();

		ReUserRole reUserRole = new ReUserRole();
		reUserRole.setReroUserId(userId);
		reUserRole.setReroRoleId(roleId);
		reUserRole.setReroTime(new Date());

		Integer id = userRoleDao.findReroIdByReUserRole(reUserRole);
		if (id == null) {
			userRoleDao.saveUserRole(reUserRole);
			data.put("addRoleIndo", "添加成功");
		} else {
			data.put("addRoleIndo", "该用户已拥有此权限");
		}
		return data;
	}

	/**
	 * 彻底删除用户信息
	 * 
	 * @param userIds
	 * @return
	 * @author 罗晖
	 */
	@Transactional
	public Map<String, Object> deleteUserInfo(String userIds) {
		Map<String, Object> data = new HashMap<String, Object>();
		if (userIds == null || userIds.equals("")) {
			data.put("deleteUserInfo", "删除失败，请选择需要删除的用户信息");
			return data;
		}
		try {
			String[] userId = userIds.split(",");
			Map<String, Object> deleteMap = new HashMap<String, Object>();
			deleteMap.put("roleUserIds", userId);
			// 删除此用户的权限
			userRoleDao.deleteUserRole(deleteMap);
			Integer rowInteger = userDAO.deleteUserInfo(userId);
			if (rowInteger <= 0) {
				data.put("deleteUserInfo", "删除失败，无用户信息可以删除");
				return data;
			}
			data.put("deleteUserInfo", "删除成功");

		} catch (Exception e) {
			e.printStackTrace();
			data.put("deleteUserInfo", "删除失败，该用户还有其他信息未删除，请删除其他信息后再来删除此用户信息");
		}
		return data;
	}

	/**
	 * 用于教务管理员删除用户信息<br>
	 * 教务管理员删除用户信息，不能完全删除用户信息，<br>
	 * 仅仅只是把用户的激活状态改为非激活状态
	 * 
	 * @param userId
	 * @return
	 * @author lujocom
	 */
	public Map<String, Object> modifyUserVerify(String userId) {
		Map<String, Object> returnData = new HashMap<String, Object>();
		if (userId == null || userId.equals("")) {
			returnData.put("deleteUserInfo", "无用户信息需要删除，请选择需要删除的用户信息");
			return returnData;
		}
		try {
			String regex = "^(\\d+,)*\\d+$";
			if (!userId.matches(regex)) {
				returnData.put("deleteUserInfo", "删除失败，删除数据格式错误!");
				return returnData;
			}
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("verify", new Integer(0));
			data.put("userIds", userId.split(","));

			if (!userDAO.modifyPersonVerify(data)) {
				returnData.put("deleteUserInfo", "不存在此用户，请稍后再试");
				return returnData;
			}
			returnData.put("deleteUserInfo", "删除成功！");

		} catch (Exception e) {
			e.printStackTrace();
			returnData.put("deleteUserInfo", "删除过程中出现意外，请稍后再试！");
			return returnData;
		}

		return returnData;
	}

	/**
	 * 批量导入用户信息
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public Map<String, Object> importUserInfoFromExcel(InputStream inputStream,
			String fileType, int userType, HttpServletRequest request)
			throws Exception {
		long startTime = System.currentTimeMillis();
		Map<String, Object> getData = new HashMap<String, Object>();
		Map<String, Object> returnData = new HashMap<String, Object>();
		List<UserModel> users = new ArrayList<UserModel>();
		List<UserModel> errorUsers = null;
		if (fileType.equals(".xls")) {
			// 处理03版本的excel
			HSSFSheet sheet = ExcelResolver_03.newInstance()
					.createWorkbook(inputStream).getSheet(0);
			if (!ExcelResolver_03.isUserInfoExcel(sheet, userType)) {
				returnData.put("status", new Integer(2));
				return returnData;
			}
			getData = ExcelResolver_03.newInstance().getUserInfoFromExcel(
					sheet, userType);
		} /*else {
			// 处理07版本的excel
			XSSFSheet sheet = ExcelResolver_07.newInstance()
					.createWorkBook(inputStream).getSheet(0);
			if (!ExcelResolver_07.isUserInfoExcel(sheet)) {
				returnData.put("status", new Integer(0));
				return returnData;
			}
			getData = ExcelResolver_07.newInstance()
					.getUserInfoFromExcel(sheet);
		}*/
		users = (List<UserModel>) getData.get("userData");
		errorUsers = (List<UserModel>) getData.get("errorDataRow");

		User user = null;
		Integer index = 0;
		for (UserModel u : users) {
			user = new User();
			u = importUserInfoToDB(u, user, userType);
			if (u != null) {
				u.setId(++index);
				errorUsers.add(u);
			}
		}
		String fileName  = null;
		// 将有误的用户信息保存在excel中
		if (errorUsers.size() != 0) {
			fileName = saveErrorUserInfoToExcel(request, "errorUserInfo", errorUsers,
					userType);
			returnData.put("fileName", fileName);
			returnData.put("status", new Integer(0));
			return returnData;
		}
		
		returnData.put("status", new Integer(1));
		long endTime = System.currentTimeMillis();
		logger.debug("time:" + (endTime - startTime));
		return returnData;
	}

	/**
	 * 将合法的用户信息保存到数据库中
	 * 
	 * @param userModel
	 * @param user
	 * @param userType
	 * @return 不合法的用户信息
	 * @throws Exception
	 */
	private UserModel importUserInfoToDB(UserModel userModel, User user,
			int userType) throws Exception {

		user.setUserLoginname(userModel.getUseLoginName());
		Integer userId = null;
		//判断信息是否齐全
        if(userModel.getUseLoginName()==null
                ||userModel.getUseLoginName().length()<=0
                ||userModel.getUserRealName()==null
                ||userModel.getUserRealName().length()<=0
                ){
            userModel.setErrorReason("用户信息不全！");
            return userModel;
        }
		// 判断该用户信息是否已经存在，若已存在查出用户id
		if (userDAO.findUsersByUser(user).size() != 0) {
			userModel.setErrorReason("该用户已存在！");
			return userModel;
		}
		user.setUserType(userType);
		user.setUserRealname(userModel.getUserRealName());
		user.setUserGender(userModel.getUserGender());
		user.setUserIdNum(userModel.getUserIdNum());
		user.setUserAddress(userModel.getUserAddress());
		user.setUserEmail(userModel.getUserEmail());
		user.setUserPhoneNum(userModel.getUserPhoneNum());
		user.setUserRemark(userModel.getUserRemark());
		user.setUserEduLevel(userModel.getUserEduLevel());
		user.setUserWorkunit(userModel.getUserWorkUnit());
		user.setUserVerify(true);
		// 密码用md5进行加密
		user.setUserPwd(MD5.getMD5Str(userModel.getUseLoginName()));

		// 判断部门是否存在于数据库中，不存在则保存数据库中，并返回部门id
		Department department = new Department();
		department.setDepaName(userModel.getUserDepatmentName());
		
		List<Department> departs = departmentDao
				.findDepartmentsByDepar(department);

		int departId = 0;
		if (departs.size() == 0) {
			userModel
					.setErrorReason("此用户的部门或者学校不存在，请根据《departmentInfo.xls》填写用户部门或者学校！");
			return userModel;
		} else {
			departId = departs.get(0).getDepaId();
		}
		user.setUserDepaId(departId);

		// 判断用户是否存在年级，
		String gradeName = userModel.getUserGrade();
		Integer gradeId = null;
		if (gradeName != null && !gradeName.equals("")) {
			Grade grade = new Grade();
			grade.setGradName(gradeName);
			grade.setGradDepaId(departId);
			// 根据年级名称以及学校id查询该学校该年级是否存在，若是不存在将此用户信息返回给用户重新填写
			gradeId = gradeDao.findGradIdByGraNamAndSchId(grade);
			if (gradeId == null) {
				userModel
						.setErrorReason("此学生的年级不存在，请根据《departmentInfo.xls》填写学生的年级信息");
				return userModel;
			}
		}

		// 判断用户信息中是否存在班级
		String className = userModel.getUserClassName();
		Integer classId = null;
		if (className != null && !"".equals(className)) {
			Clas clas = new Clas();
			clas.setClasName(className);
			clas.setClasGradId(gradeId);
			// 根据班级信息查询班级id,若班级id为空则说明数据库中不存在此班级,则将此用户信息返回给用户重新填写
			classId = gradeDao.findClasIdByClaNamAndGradId(clas);
			if (classId == null) {
				userModel
						.setErrorReason("此学生的年级不存在，请根据《departmentInfo.xls》填写学生的班级信息");
				return userModel;
			}
			user.setUserClasId(classId);
			user.setUserCadasExamNum(userModel.getUserAdasExamNum());
			user.setUserYearOfEntrance(userModel.getUserYearOfEntrance());
		}

		// 不存在此用户，保存用户信息，同时在用户与角色的中间表中添加一条数据
		userId = userDAO.insertUser(user);
		ReUserRole userRole = new ReUserRole();
		userRole.setReroUserId(userId);
		userRole.setReroRoleId(user.getUserType());
		userRole.setReroTime(new Date());

		Integer reUserRole = userRoleDao.findReroIdByReUserRole(userRole);
		if (reUserRole == null) {
			userRoleDao.saveUserRole(userRole);
		}
		return null;
	}

	/**
	 * 将有误的用户导入信息保存在excel中等待用户下载
	 * 
	 * @param request
	 * @param fileName
	 * @param userInfos
	 * @param userType
	 */
	public String saveErrorUserInfoToExcel(HttpServletRequest request,
			String fileName, List<UserModel> userInfos, int userType) {
		User user = CookieUtil.getCookieUser(request);
		Map<String, String> propertyName = getPropertyName(userType + 2);
		String filePath = request.getSession().getServletContext()
				.getRealPath("/upload/temp");
		File errorFile = newFile(filePath, user.getUserLoginname()+System.currentTimeMillis()+".xls");
		try {
			logger.debug(errorFile.getCanonicalPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ExportAsExcel.exportExcel(UserModel.class.getName(), propertyName,
				"用户导入信息错误数据", errorFile, userInfos);

		return errorFile.getName();
	}

	/**
	 * 将用户信息模板与部门或者学校班级信息写入压缩文件中
	 * 
	 * @param fileName
	 * @param userType
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public String writeUserInfoModelToFile(String fileName, Integer userType,
			HttpServletRequest request) throws IOException {
		String result = "fail";
		User user = CookieUtil.getCookieUser(request);
		// 查询所有部门或者学校的所有班级信息
		Map<String, Object> querySchInfo = new HashMap<String, Object>();
		querySchInfo.put("departId", user.getUserDepaId());
		querySchInfo.put("departType", user.getUserDepartType());
		querySchInfo.put("userType", userType);
		
		
		Map<String, String> propertyName = getPropertyName(userType);
		// 将部门信息写入excel文件中
		String filePath = request.getSession().getServletContext()
				.getRealPath("/upload/temp");
		File departInfos = newFile(filePath, "departmentInfo.xls");

		String userInfoModelPath = request.getSession().getServletContext()
				.getRealPath("/upload/resource");

		File userInfoModel = null;
		boolean isFinish = false;
		if (userType == 1) {
			List<ClasModel> schoolInfo = gradeDao
					.getSchoolAndGraClasInfo(querySchInfo);
			isFinish = ExportAsExcel.exportExcel(ClasModel.class.getName(),
					propertyName, "学校以及政府部门列表", departInfos, schoolInfo);
			// 学生用户信息导入模板
			userInfoModel = new File(new File(userInfoModelPath),
					"studentInfoTemplate.xls");
		} else {
			List<Department> departmentInfo = departmentDao.findDepartmentsByDepar(null);
			isFinish = ExportAsExcel.exportExcel(Department.class.getName(),
					propertyName, "学校以及政府部门列表", departInfos, departmentInfo);
			// 教师或者教务员信息导入模板
			userInfoModel = new File(new File(userInfoModelPath),
					"teacherAndAcdemicDeanInfoTemplate.xls");
		}
		File exportZip = newFile(filePath, fileName);

		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(
				exportZip));
		if (isFinish) {
			zipFile(zos, departInfos);
			zipFile(zos, userInfoModel);
			zos.close();
		}
		result = "success";
		return result;
	}

	/**
	 * 根据将要导入用户信息类型，返回列名
	 * 
	 * @param type
	 * @return
	 */
	private Map<String, String> getPropertyName(int type) {
		Map<String, String> propertyName = new HashMap<String, String>();
		if (type == 1) {
			propertyName.put("schoolId", "学校ID");
			propertyName.put("schoolName", "学校名称");
			propertyName.put("gradeName", "年级");
			propertyName.put("clasName", "班级");
		} else if (type == 2) {
			propertyName.put("depaId", "学校ID/部门ID");
			propertyName.put("depaName", "学校名称/部门名称");
		} else if (type == 3) {
			// 错误学生信息导出列名
			propertyName.put("id", "序号");
			propertyName.put("useLoginName", "学籍号");
			propertyName.put("userRealName", "姓名");
			propertyName.put("userGender", "性别");
			propertyName.put("userGrade", "年级");
			propertyName.put("userClassName", "班级");
			propertyName.put("userDepatmentName", "学校");
			propertyName.put("userAdasExamNum", "考籍号");
			propertyName.put("userYearOfEntrance", "入学时间");
			propertyName.put("userIdNum", "身份证号");
			propertyName.put("userPhoneNum", "电话号码");
			propertyName.put("userAddress", "通讯地址");
			propertyName.put("userEmail", "邮箱");
			propertyName.put("userRemark", "备注");
			propertyName.put("errorReason", "错误原因");
		} else if (type == 4) {
			// 错误教师或教务员信息导出列名
			propertyName.put("id", "序号");
			propertyName.put("useLoginName", "工号");
			propertyName.put("userRealName", "姓名");
			propertyName.put("userGender", "性别");
			propertyName.put("userDepatmentName", "学校");
			propertyName.put("userIdNum", "身份证号");
			propertyName.put("userPhoneNum", "电话号码");
			propertyName.put("userAddress", "通讯地址");
			propertyName.put("userEmail", "邮箱");
			propertyName.put("userRemark", "备注");
			propertyName.put("errorReason", "错误原因");
		}

		return propertyName;
	}

	public static File newFile(String filePath, String fileName) {
		File file = new File(new File(filePath), fileName);
		File parentFile = file.getParentFile();
		if (!parentFile.exists()) {
			parentFile.mkdirs();
		} else if (file.exists()) {
			file.delete();
			file = new File(new File(filePath), fileName);
		}
		return file;
	}

	public static void zipFile(ZipOutputStream zos, File file) {
		try {
			FileInputStream fis = new FileInputStream(file);
			zos.putNextEntry(new ZipEntry(file.getName()));
			int len;
			while ((len = fis.read()) != -1) {
				zos.write(len);
			}
			// zos.closeEntry();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
