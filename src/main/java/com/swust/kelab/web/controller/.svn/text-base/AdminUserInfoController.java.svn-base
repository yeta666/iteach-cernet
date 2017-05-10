package com.swust.kelab.web.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.support.ServletContextResource;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.swust.kelab.domain.User;
import com.swust.kelab.service.AdminUserInfoService;
import com.swust.kelab.service.LogDBService;
import com.swust.kelab.service.LoginService;
import com.swust.kelab.service.UserService;
import com.swust.kelab.utils.CookieUtil;
import com.swust.kelab.web.json.JsonAndView;

/**
 * 负责处理教务管理员管理用户信息 ，校务管理员分析校级教务管理员，市级教务管理员<br>
 * 
 * @author lujoCom
 * 
 */
@Controller
@RequestMapping("/adminUserInfo")
public class AdminUserInfoController {

	private final static int FAILED = 0;

	private final static int SUCCESS = 1;

	private final static int ERROR = 2;
	/**
	 * 教务员
	 */
	private final static int ADMINTEACHER = 3;

	/**
	 * 管理员
	 */
	private final static int ADMIN = 4;

	private UserService userService;

	@Autowired
	private LogDBService logDBService;

	private AdminUserInfoService adminUserInfoService;

	final Logger logger = LoggerFactory
			.getLogger(AdminUserInfoController.class);

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Resource
	HttpServletRequest request;

	@Autowired
	public void setAdminUserInfoService(
			AdminUserInfoService adminUserInfoService) {
		this.adminUserInfoService = adminUserInfoService;
	}

	/**
	 * 在添加用户时，如果该用户已经存在，则在返回的状态status设置为1， 否则设置为2<br>
	 * 
	 * @param user
	 * @return
	 * @author lujoCom
	 */
	@RequestMapping(value = "/isUserExist", method = RequestMethod.POST)
	public JsonAndView isInfoExist(User user) {
		JsonAndView jv = new JsonAndView();
		Map<String, Object> data = new HashMap<String, Object>();

		if (userService.findUsersByUser(user).size() != 0) {
			data.put("status", "1");
		} else {
			data.put("status", "2");
		}

		return jv.addAllData(data);
	}

	/**
	 * 添加信息的用户
	 * 
	 * @param user
	 * @return
	 * @author lujoCom
	 */
	@RequestMapping(value = "/addNewUser", method = RequestMethod.POST)
	public JsonAndView addNewUser(User user, String userRoles) {
		JsonAndView jv = new JsonAndView();
		Map<String, Object> data = null;
		try {
			data = userService.addNewUserToDB(user, userRoles);
			logDBService.insertNewLog(request, LogDBService.ADD_OPERATION,
					"用户管理", "添加新用户：" + user.getUserLoginname());
			jv.setRet(true);
			jv.addAllData(data);
		} catch (Exception e) {
			e.printStackTrace();
			jv.setRet(false);
			jv.setErrmsg("添加用户出现异常,异常信息" + e.getMessage());
		}
		return jv;
	}

	/**
	 * 通过用户id查询用户基本信息
	 * 
	 * @param userId
	 * @return
	 * @author lujoCom
	 */
	@RequestMapping(value = "/queryUserInfoById", method = RequestMethod.POST)
	public JsonAndView queryUserInfoById(Integer userId) {
		JsonAndView jv = new JsonAndView();
		User user = userService.findOneUser(userId);
		Map<String, Object> map = new HashMap<String, Object>();
		if (user != null) {
			map.put("userInfo", user);
			jv.setRet(true);
			jv.addAllData(map);
		} else {
			jv.setRet(false);
			jv.setErrmsg("查无此人！");
		}
		return jv;
	}

	/**
	 * 修改用户基本信息
	 * 
	 * @param user
	 * @return
	 * @author lujoCom
	 */
	@RequestMapping(value = "/modefiedUserInfo", method = RequestMethod.POST)
	public JsonAndView modefiedUserInfo(User user, String roleDel,
			String roleAdd) {
		JsonAndView jv = new JsonAndView();

		Map<String, Object> map = userService.modifyUserInfo(user, roleDel,
				roleAdd);
		if (map != null) {
			logDBService.insertNewLog(request, LogDBService.UPDATA_OPERATION,
					"用户管理", "修改用户信息：" + user.getUserLoginname());
			jv.setRet(true);
			jv.addAllData(map);
		} else {
			jv.setRet(false);
			jv.setErrmsg("修改失败!!");
		}

		return jv;
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
	public JsonAndView addRoleToUser(Integer userId, Integer roleId) {
		JsonAndView jv = new JsonAndView();
		try {
			jv.setRet(true);
			jv.addAllData(adminUserInfoService.addRoleToUser(userId, roleId));
		} catch (Exception e) {
			jv.setRet(false);
			jv.setErrmsg("添加权限发生错误,错误信息是" + e.getMessage());
			e.printStackTrace();
		}
		return jv;
	}

	/**
	 * 用户删除，<br>
	 * 当操作人员为管理员时，会进行删除操作，删除没有进行级联，删除发生异常时则为此用户还包含其他信息，删除失败<br>
	 * 教务员则是修改用户的状态
	 * 
	 * @param userIds
	 * @return
	 * @author lujoCom
	 */
	@RequestMapping(value = "/deleteUserInfos", method = RequestMethod.POST)
	public JsonAndView deleteUserInfo(HttpServletRequest request, String userIds) {
		JsonAndView jv = new JsonAndView();
		Map<String, Object> data = null;
		User user = CookieUtil.getCookieUser(request);
		if (user.getUserType() == ADMIN) {
			data = adminUserInfoService.deleteUserInfo(userIds);

		} else if (user.getUserType() == ADMINTEACHER) {
			data = adminUserInfoService.modifyUserVerify(userIds);
		}

		logDBService.insertNewLog(request, LogDBService.DELETE_OPERATION,
				"用户管理", "删除用户：" + userIds.split(",").length + "人");
		if (data == null) {
			jv.setRet(false);
			jv.setErrmsg("发生未知错误，请稍后再试");
			return jv;
		}
		jv.setRet(true);
		jv.setData(data);

		return jv;
	}

	/**
	 * 用户信息批量导入<br>
	 * file 是input
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @author lujoCom
	 */
	@RequestMapping(value = "/importUserInfo", method = RequestMethod.POST)
	public void importUserInfo(int userType, HttpServletRequest request,
			HttpServletResponse response) {

		CommonsMultipartFile comMultiFile = (CommonsMultipartFile) ((MultipartHttpServletRequest) request)
				.getFile("file");
		String fileName = comMultiFile.getOriginalFilename();
		String fileType = fileName.substring(fileName.lastIndexOf("."));
		// 防止返回数据被IE当做下载流
		response.reset();
		response.setContentType("text/html; charset=utf-8");
		PrintWriter pw = null;
		try {
			if (!fileType.equals(".xls")) {
				pw = response.getWriter();
				pw.write("{'ret':false,'errcode':1,'data':'文件格式不正确,必须是excel表格'}");
				pw.flush();
				pw.close();
				return;
			}

			Map<String, Object> data = adminUserInfoService
					.importUserInfoFromExcel(comMultiFile.getInputStream(),
							fileType, userType, request);
			String type = "非数据库对应类型";
			switch (userType) {
			case 1:
				type = "学生";
				break;
			case 2:
				type = "老师";
				break;
			case 3:
				type = "教务管理员";
				break;
			case 4:
				type = "系统管理员";
				break;
			default:
				break;
			}
			switch ((Integer) data.get("status")) {
			case SUCCESS:
				pw = response.getWriter();
				pw.write("{'ret':true,'status':2,'data':'数据导入成功'}");
				pw.flush();
				pw.close();
				logDBService
						.insertNewLog(request, LogDBService.DELETE_OPERATION,
								"用户管理", "批量导入用户：" + type);
				break;

			case FAILED:
				pw = response.getWriter();
				pw.write("{'ret':true,'status':3,'data':'数据导入失败','errorData':'"
						+ data.get("fileName") + "'}");
				pw.flush();
				pw.close();
				break;

			case ERROR:
				pw = response.getWriter();
				pw.write("{'ret':false,'status':4,'data':'数据格式不正确,非此类用户信息格式，请下载此用户信息格式模板'}");
				pw.flush();
				pw.close();
				break;
			}

		} catch (Exception e) {
			try {
				pw = response.getWriter();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			pw.write("{'ret':false,'status':5,'data':'数据在导入的过程中出现意外的错误，请重新导入'}");
			pw.flush();
			pw.close();
		}
	}

	/**
	 * 下载用户有误的导入信息
	 * 
	 * @param fileName
	 * @param userType
	 * @param request
	 * @param response
	 * @author lujoCom
	 */
	@RequestMapping(value = "/downloadErrorUserInfo", method = RequestMethod.GET)
	public void downloadErrorUserInfo(String fileName,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			// 获取文件资源
			String filePath = "upload/temp/" + fileName;
			if (filePath == null || filePath.isEmpty()) {
				return;
			}

			ServletContextResource downFile = new ServletContextResource(
					request.getSession().getServletContext(), filePath);
			if (!downFile.exists()) {
				logger.error("找不到文件或者文件不存在！！");
				return;
			}
			String agintname = request.getHeader("User-Agent").toUpperCase();
			fileName = new String(fileName.getBytes(), "ISO-8859-1");
			if (agintname.indexOf("MSIE") > 0)
				fileName = URLEncoder.encode(fileName, "ISO-8859-1");// IE浏览器
			OutputStream os = response.getOutputStream();
			try {
				response.reset();
				response.setHeader("Content-Disposition",
						"attachment; filename=" + fileName);
				response.setContentType("application/octet-stream; charset=utf-8");
				os.write(FileUtils.readFileToByteArray(downFile.getFile()));
				os.flush();
			} finally {
				if (os != null) {
					os.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 下载用户信息导入模板
	 * 
	 * @param fileName
	 *            文件名字是userInfoMode.zip
	 * @param request
	 * @param response
	 * @author lujoCom
	 */
	@RequestMapping(value = "/downloadUserInfoModel", method = RequestMethod.GET)
	public void downloadUserInfoModel(String fileName, Integer userType,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			// 获取文件资源
			String filePath = "upload/temp/" + fileName;
			if (filePath == null || filePath.isEmpty()) {
				return;
			}
			if (fileName.contains("userInfoMode.zip")) {
				String result = adminUserInfoService.writeUserInfoModelToFile(
						fileName, userType, request);
				if (result.equals("fail"))
					return;
			}
			ServletContextResource downFile = new ServletContextResource(
					request.getSession().getServletContext(), filePath);
			if (!downFile.exists()) {
				logger.error("找不到文件或者文件不存在！！");
				return;
			}
			String agintname = request.getHeader("User-Agent").toUpperCase();
			fileName = new String(fileName.getBytes(), "ISO-8859-1");
			if (agintname.indexOf("MSIE") > 0)
				fileName = URLEncoder.encode(fileName, "ISO-8859-1");// IE浏览器
			OutputStream os = response.getOutputStream();
			try {
				response.reset();
				response.setHeader("Content-Disposition",
						"attachment; filename=" + fileName);
				response.setContentType("application/octet-stream; charset=utf-8");
				os.write(FileUtils.readFileToByteArray(downFile.getFile()));
				os.flush();
			} finally {
				if (os != null) {
					os.close();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
