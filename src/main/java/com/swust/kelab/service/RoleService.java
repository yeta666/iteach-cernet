package com.swust.kelab.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swust.kelab.domain.ReUserRole;
import com.swust.kelab.domain.Role;
import com.swust.kelab.domain.User;
import com.swust.kelab.repos.ReUserRoleDAO;
import com.swust.kelab.repos.RoleDao;

@Service()
public class RoleService {

	private RoleDao roleDao;

	private ReUserRoleDAO reUserRoleDAO;

	@Autowired
	public void setRoleDao(RoleDao roleDao) {
		this.roleDao = roleDao;
	}

	@Autowired
	public void setReUserRoleDAO(ReUserRoleDAO reUserRoleDAO) {
		this.reUserRoleDAO = reUserRoleDAO;
	}

	public List<Role> findRoleByRole(Role role) {
		return roleDao.findRolesByRole(role);
	}

	public Integer insertRole(Role role) {
		return roleDao.insertRole(role);
	}

	public Integer insertReUserRole(ReUserRole reUserRole) {
		return reUserRoleDAO.saveUserRole(reUserRole);
	}

	/**
	 * 查询某用户的所有权限
	 * 
	 * @param userId
	 * @return
	 */
	public List<Role> findRolesByUser(int userId) {
		return reUserRoleDAO.viewRoleListByUser(userId);
	}

	/**
	 * 查询某用户类型的所有权限
	 * 
	 * @param userId
	 * @return
	 */
	public List<Role> findRolesByUserType(int userType) {
		List<Role> roles = roleDao.findAllRole();

		if (roles.size() < 5) {//内置类型必须大于5
			roles.clear();
			return roles;
		}

		if (userType == User.USERTYPE_SYSTEM_MANAGER) {
		}
		if (userType == User.USERTYPE_TEACHING_MANAGER) {
			roles.remove(Role.ROLE_ADMINISTRATOR - 1);
			roles.remove(Role.ROLE_SYSTEM_MANAGER - 1);
			roles.remove(Role.ROLE_TEACHING_MANAGER - 1);
		}
		if (userType == User.USERTYPE_TEACHER) {
			roles.remove(Role.ROLE_ADMINISTRATOR - 1);
			roles.remove(Role.ROLE_SYSTEM_MANAGER - 1);
			roles.remove(Role.ROLE_TEACHING_MANAGER - 1);
			roles.remove(Role.ROLE_TEACHER - 1);
		}
		if (userType == User.USERTYPE_STUDENT) {
			roles.clear();
		}
		return roles;
	}

	/**
	 * 查询所有角色
	 * 
	 * @author ZhangXin
	 * @return
	 * @throws Exception
	 */
	public List<Role> findAllRoles() throws Exception {
		return roleDao.findAllRole();
	}
}
