package com.swust.kelab.web.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.swust.kelab.domain.Role;
import com.swust.kelab.domain.User;
import com.swust.kelab.service.RoleService;
import com.swust.kelab.service.UserService;
import com.swust.kelab.utils.CookieUtil;
import com.swust.kelab.web.json.JsonAndView;

/**
 * 
 * @author ZhangXin
 * 
 */
@Controller
@RequestMapping("/role")
public class RoleController {
	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private RoleService roleService;
	@Autowired
	private UserService userService;
	@Resource
	HttpServletRequest request;

	/**
	 * 查询所有角色
	 * 
	 * @return
	 */
	@SuppressWarnings("finally")
	@RequestMapping(value = "/findAllRole", method = RequestMethod.POST)
	public JsonAndView findRoles() {
		JsonAndView jv = new JsonAndView();
		try {
		    User user = CookieUtil.getCookieUser(request);
			List<Role> roles = roleService.findRolesByUserType(user
					.getUserType());
			jv.addData("roles", roles);
		} catch (Exception e) {
			jv.addData("error", "can not find roles!\n"+e.getLocalizedMessage());
		} finally {
			return jv;
		}
	}

	/**
	 * 查询某用户的所有权限
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/findRoleById", method = RequestMethod.POST)
	public JsonAndView findRolesById(Integer userId) {
		JsonAndView jv = new JsonAndView();
		List<Role> roles = roleService.findRolesByUser(userId);
		jv.addData("roles", roles);
		return jv;

	}

	/**
	 * 查询某用户类型的所有权限
	 * 
	 * @param userType
	 * @return
	 */
	@RequestMapping(value = "/findRoleByUserType", method = RequestMethod.POST)
	public JsonAndView findRolesByUserType(Integer userType) {
		JsonAndView jv = new JsonAndView();
		List<Role> roles = roleService.findRolesByUserType(userType);
		jv.addData("roles", roles);
		return jv;

	}

}
