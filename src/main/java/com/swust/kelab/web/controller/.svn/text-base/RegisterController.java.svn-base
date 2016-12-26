package com.swust.kelab.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.swust.kelab.domain.Department;
import com.swust.kelab.domain.User;
import com.swust.kelab.service.DepartmentService;
import com.swust.kelab.service.GradeService;
import com.swust.kelab.service.UserService;
import com.swust.kelab.web.json.JsonAndView;

@Controller
@RequestMapping("register")
public class RegisterController {

	private UserService userService;

	private GradeService gradeService;

	private DepartmentService departmentService;

	private final static Logger LOGGER = LoggerFactory
			.getLogger("com.registerController");

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Autowired
	public void setGradeService(GradeService gradeService) {
		this.gradeService = gradeService;
	}

	@Autowired
	public void setDepartmentService(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	/**
	 * 查询所有的学校
	 * 
	 * @return
	 * @author 罗晖
	 */
	@RequestMapping(value = "/department", method = RequestMethod.GET)
	public JsonAndView findDepartmentInfo() {
		JsonAndView jv = new JsonAndView();
		LOGGER.debug("====department====");
		List<Department> schools = departmentService.findDepartmentsByType(3);
		if (schools != null) {
			for (Department school : schools) {
				school.setDepaAbbreviation("");
				school.setDepaCode("");
			}
			jv.addData("depatement", schools);
		} else {
			jv.setRet(false);
			jv.setErrcode(1);
			jv.setErrmsg("获取学校列表失败！");
		}

		return jv;
	}

	/**
	 * 根据学校的id查询学校所有的年级
	 * 
	 * @param depa_id
	 * @return 年级的信息
	 * @author 罗晖
	 */
	@RequestMapping(value = "/gradeInfo", method = RequestMethod.POST)
	public JsonAndView findGradeInfo(@RequestParam("depa_id") String depa_id) {
		JsonAndView jv = new JsonAndView();
		LOGGER.debug("=====schoolInfo=====");
		if (depa_id != null && !depa_id.equals("")) {
			jv.addData("gradeInfo",
					gradeService.findGradesByDepaId(Integer.parseInt(depa_id)));
		}
		return jv;
	}

	/**
	 * 根据年级的id查询所有的班级
	 * 
	 * @param grade_id
	 * @return 一个年级的所有班级
	 * @author 罗晖
	 */
	@RequestMapping(value = "/classesInfo", method = RequestMethod.POST)
	public JsonAndView findeClassesInfo(
			@RequestParam("grade_id") String grade_id) {
		JsonAndView jv = new JsonAndView();
		LOGGER.debug("========classesInfo=========");
		if (grade_id != null && !grade_id.equals("")) {
			jv.addData("classesInfo", gradeService.findClassesByGradId(Integer
					.parseInt(grade_id)));
		}
		return jv;
	}

	/**
	 * 用户注册
	 * 
	 * @param user
	 * @return 返回注册信息
	 * @author 罗晖
	 */
	@RequestMapping(value = "/registerUser", method = RequestMethod.POST)
	public JsonAndView registerUser(User user) {
		JsonAndView jv = new JsonAndView();
		LOGGER.debug("========registerUser=========");
		LOGGER.debug("usergender" + user.getUserGender());
		Map<String, Object> data = null;
		if (user != null) {
			user.setUserVerify(true);
			data = userService.insertUser(user);
		}
		if (data != null) {
			jv.addAllData(data);
		} else {
			data = new HashMap<String, Object>();
			data.put("status", "3");
			jv.addAllData(data);
		}

		return jv;
	}

}
