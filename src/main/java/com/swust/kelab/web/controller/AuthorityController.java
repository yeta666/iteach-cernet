package com.swust.kelab.web.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.swust.kelab.domain.Role;
import com.swust.kelab.domain.User;
import com.swust.kelab.service.AuthorityService;
import com.swust.kelab.service.LogDBService;
import com.swust.kelab.utils.CookieUtil;
import com.swust.kelab.web.json.JsonAndView;

/**
 * 权限管理
 * 
 * @author yangzq
 * 
 */
@Controller()
@RequestMapping("/authority")
public class AuthorityController {

	@Resource
	private AuthorityService authorityService;
	
	@Resource
	private LogDBService logDBService;

	/**
	 * 用户管理---权限管理，获取所有权限
	 * 
	 * @author yangzq
	 * @param request
	 * @param query
	 * @return
	 */
	@RequestMapping(value = "/queryAuthority", method = RequestMethod.GET)
	public JsonAndView queryAuthority(HttpServletRequest request) {
		JsonAndView jv = new JsonAndView();
		List<Role> roles = authorityService.queryAuthority();
		if (roles == null) {
			jv.setRet(false);
			jv.setErrmsg("权限获取失败！");
			return jv;
		}
		jv.addData("pageData", roles);
		return jv;
	}

	/**
	 * 获取所有的已有功能模块
	 * 
	 * @author yangzq
	 * @return
	 */
	@RequestMapping(value = "/queryAllCompetence", method = RequestMethod.GET)
	public JsonAndView queryAllCompetence() {
		JsonAndView jv = new JsonAndView();
		List<Map<String, Object>> list = authorityService.queryCompetence();
		jv.addData("json", list);
		return jv;
	}

	/**
	 * 获取角色的所有actions
	 * 
	 * @author yangzq
	 * @param roleId
	 * @return
	 */
	@RequestMapping(value = "/queryRoleActions", method = RequestMethod.POST)
	public JsonAndView queryRoleActions(Integer roleId) {
		JsonAndView jv = new JsonAndView();
		// 验证传入参数是否合法
		if (roleId == null || roleId <= 0) {
			jv.setRet(false);
			jv.setErrmsg("角色id不能为空！");
			return jv;
		}
		List<String> list = authorityService.queryRoleActions(roleId);
		jv.addData("actions", list);
		return jv;
	}

	/**
	 * 创建新角色，并传入权限
	 * 
	 * @author yangzq
	 * @param rolename
	 * @param roledesc
	 * @param authority
	 * @return
	 */
	@RequestMapping(value = "/createRole", method = RequestMethod.POST)
	public JsonAndView createRole(HttpServletRequest request,String rolename, String roledesc,
			String[] authority) {
		JsonAndView jv = new JsonAndView();
		if (rolename == null || rolename == "" || authority == null || authority.length == 0) {
			jv.setRet(false);
			jv.setErrmsg("参数传入有误！");
			return jv;
		}
		try {
			authorityService.createRole(rolename, roledesc, authority);
			logDBService.insertNewLog(request, 1, "权限管理", "新增角色权限:"+rolename);
		} catch (Exception e) {
			e.printStackTrace();
			jv.setRet(false);
		}
		return jv;
	}

	/**
	 * 编辑角色权限
	 * 
	 * @author yangzq
	 * @param rolename
	 * @param roledesc
	 * @param authority
	 * @return
	 */
	@RequestMapping(value = "/editAuthority", method = RequestMethod.POST)
	public JsonAndView editAuthority(HttpServletRequest request,String roleName, String roleDesc,
			Integer roleId, String[] authority) {
		JsonAndView jv = new JsonAndView();
		if (roleName == null || roleName == ""  || roleId == null || roleId <= 0
				|| authority == null || authority.length == 0) {
			jv.setRet(false);
			jv.setErrmsg("参数传入有误！");
			return jv;
		}
		try {
			authorityService.editAuthority(roleName, roleDesc,roleId, authority);
			logDBService.insertNewLog(request, 3, "权限管理", "编辑角色权限:"+roleName);
		} catch (Exception e) {
			e.printStackTrace();
			jv.setRet(false);
		}
		return jv;
	}

	
	/**
	 * 删除角色数据和对应权限信息
	 * @author yangzq
	 * @param delRoles
	 * @return
	 */
	@RequestMapping(value = "/delRoleAuthority",method = RequestMethod.POST)
	public JsonAndView delRoleAuthority(HttpServletRequest request,String delRoles){
		JsonAndView jv = new JsonAndView();
		if(delRoles == null || delRoles == ""){
			jv.setRet(false);
			jv.setErrmsg("删除角色id有误!");
			return jv;
		}
		String[] roleIds = delRoles.split(","); 
		try {
			authorityService.deleteRoleAuthority(roleIds);
			logDBService.insertNewLog(request, 2, "权限管理", "删除角色："+roleIds.toString());
		} catch (Exception e) {
			e.printStackTrace();
			jv.setRet(false);
		}
		return jv;
	}
	
	/**
	 * 用户对应的action权限白名单（action type=1）
	 * @author yangzq
	 * @return
	 */
	@RequestMapping(value="/uAcWhiteList",method= RequestMethod.GET)
	public JsonAndView uAcWhiteList(HttpServletRequest request){
		JsonAndView jv = new JsonAndView();
		User user = CookieUtil.getCookieUser(request);
		jv.addData("acWhiteList", authorityService.uAcWhiteList(user.getUserId()));
		return jv;
	}
	
}
