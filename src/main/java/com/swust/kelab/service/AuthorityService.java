package com.swust.kelab.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.swust.kelab.domain.Action;
import com.swust.kelab.domain.FunctionAction;
import com.swust.kelab.domain.ReAuthority;
import com.swust.kelab.domain.Role;
import com.swust.kelab.model.FunctionModel;
import com.swust.kelab.repos.AuthorityDAO;
import com.swust.kelab.repos.RoleDao;

/**
 * 用户权限管理service
 * 
 * @author yangzq
 * 
 */
@Service()
public class AuthorityService {

	final Logger logger = LoggerFactory.getLogger(getClass());
	@Resource
	private AuthorityDAO authorityDAO;

	@Resource
	private RoleDao roleDao;

	/**
	 * 用户管理----权限管理
	 * 
	 * @author yangzq
	 * @param query
	 * @return
	 */
	public List<Role> queryAuthority() {
		// TODO Auto-generated method stub
		// 传入空的role对象，查询所有角色
		List<Role> roles = roleDao.findRolesByRole(new Role());
		return roles;
	}

	/**
	 * 用户管理--权限管理数据
	 * 
	 * @author yangzq
	 * @param roleId
	 *            为0则是新增，不为0则是编辑
	 * @return
	 */
	public List<Map<String, Object>> queryCompetence() {
		// 请求角色所有模块数据
		List<FunctionModel> functions = authorityDAO.queryCompetence();
		// 父模块功能封装list
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (FunctionModel func : functions) {
			// 父模块
			Map<String, Object> map = new HashMap<String, Object>();
			System.out.println(func.getFumoName());
			if (func.getFunctions() != null && func.getFunctions().size() > 0) {
				// 子模块封装subList
				List<Map<String, Object>> subList = new ArrayList<Map<String, Object>>();
				for (FunctionModel subfunc : func.getFunctions()) {
					// 子模块功能封装为subcols
					Map<String, Object> subcols = new HashMap<String, Object>();
					subcols.put("colid", subfunc.getFumoId());// 子模块id
					subcols.put("colname", subfunc.getFumoName());// 子模块名称
					subcols.put("colurl", subfunc.getFumoUrl());// 子模块URL
					subcols.put("coltype", subfunc.getFumoType());// 子模块type
					subcols.put("colicon", subfunc.getFumoIcon());// 子模块icon
					// 子模块功能封装list
					List<Map<String, Object>> sub = new ArrayList<Map<String, Object>>();
					// 遍历子模块功能
					for (Action action : subfunc.getActions()) {
						Map<String, Object> subCol = new HashMap<String, Object>();
						subCol.put("colid",
								subfunc.getFumoId() + "+" + action.getActiId());// 子模块对应功能id
						subCol.put("colname", action.getActiName());// 子模块对应功能名称
						subCol.put("colurl", action.getActiUrl());// 子模快对应功能url
						subCol.put("coltype", action.getActiType());// 子模块对应功能type
						sub.add(subCol);
					}
					// 子模块处理好封装到subcols
					subcols.put("subcols", sub);
					subList.add(subcols);
				}
				// 将子模块放到父模块里
				map.put("subcols", subList);
				map.put("colid", func.getFumoId());// 父模块id
				map.put("colname", func.getFumoName());// 父模块名称
				map.put("colurl", func.getFumoUrl());// 父模块url
				map.put("coltype", func.getFumoType());// 父模块type
				map.put("colicon", func.getFumoIcon());// 父模块icon
			}
			list.add(map);
		}
		return list;
	}

	/**
	 * 查询角色的actions
	 * 
	 * @author yangzq
	 * @param roleId
	 * @return
	 */
	public List<String> queryRoleActions(Integer roleId) {
		// 若roleId不为空，则将角色对应的actionid返回list
		List<ReAuthority> roleAus = authorityDAO.queryRoleAcions(roleId);
		List<String> roleActs = new ArrayList<String>();
		for (ReAuthority roleAu : roleAus) {
			if (roleAu.getReauActions() != null) {
				String[] ids = roleAu.getReauActions().split(",");
				for (String id : ids) {
					if (id != "" && !"".equals(id)) {
						String act = roleAu.getReauFumoId() + "+" + id;
						roleActs.add(act);
					}
				}
			}
		}
		return roleActs;
	}

	/**
	 * 新增保存角色模块权限
	 * 
	 * @author yangzq
	 * @param rolename
	 * @param roledesc
	 * @param authority
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public void createRole(String rolename, String roledesc, String[] authority)
			throws Exception {
		Role role = new Role();
		role.setRoleName(rolename);
		role.setRoleDescirbe(roledesc);
		Integer roleId = roleDao.insertRole(role);
		this.commonSaveReAuthority(roleId, authority);
	}

	/**
	 * 编辑角色权限
	 * 
	 * @author yangzq
	 * @param roleName
	 * @param roleDesc
	 * @param roleId
	 * @param authority
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public void editAuthority(String roleName, String roleDesc, Integer roleId,
			String[] authority) throws Exception {
		Role role = new Role();
		role.setRoleId(roleId);
		role.setRoleName(roleName);
		role.setRoleDescirbe(roleDesc);
		// 更新角色
		roleDao.updateRole(role);

		// 删除旧有的权限信息
		authorityDAO.deleteReAuthorityByRoleId(String.valueOf(roleId)
				.split(","));
		// 保存新的角色权限
		this.commonSaveReAuthority(roleId, authority);
	}

	/**
	 * 通用保存角色权限表
	 * 
	 * @author yangzq
	 * @param roleName
	 * @param roleDesc
	 * @param roleId
	 * @param authority
	 * @throws Exception
	 */
	private void commonSaveReAuthority(Integer roleId, String[] authority)
			throws Exception {
		if (authority != null && !"".equals(authority)) {
			for (String au : authority) {
				// 处理页面传入的数组参数，剔出掉杂项
				String col = au
						.replaceAll("firstCol=|secondCol=|actionId=", "");
				String[] coluauth = col.split("&");
				if (coluauth.length > 0) {
					// 第一列不为空，则把主栏目加入
					// if (!StringUtils.isEmpty(coluauth[0])) {
					// int fumoId = Integer.valueOf(coluauth[0]);
					// hs.add(fumoId);
					// } else {
					// throw new Exception("栏目编号不存在！");
					// }
					// 如果第二列不为空，则是对应模块id
					if (!StringUtils.isEmpty(coluauth[1])) {
						int fumoId = Integer.valueOf(coluauth[1]);
						String actions = "";
						// 从第三个编号开始
						for (int i = 2; i < coluauth.length; i++) {
							if (!StringUtils.isEmpty(coluauth[i])) {
								if (!"".equals(actions)) {
									actions += "," + coluauth[i];
								} else {
									actions = coluauth[i];
								}
							}
						}
						// 筛选出模块对应的所有acitonid
						ReAuthority reAuthority = new ReAuthority();
						reAuthority.setReauCreatetime(new Date());
						reAuthority.setReauFumoId(fumoId);// 存放模块
						reAuthority.setReauRoleId(roleId);// 存放角色id
						reAuthority.setReauActions(actions);// 存放功能id
						authorityDAO.saveReAuthority(reAuthority);
					} else {
						throw new Exception("模块编号非法！");
					}
				}
			}
		} else {
			throw new Exception("参数传入有误！");
		}
	}

	/**
	 * 删除角色权限 级联删除相关的三张表
	 * 
	 * @author yangzq
	 * @param delRoles
	 */
	@Transactional(rollbackFor = Exception.class)
	public void deleteRoleAuthority(String[] roleIds) throws Exception {

		// 添加对内置角色删除的限制
		for (int i = 0; i < roleIds.length; i++) {
			int roleId = Integer.parseInt(roleIds[i]);
			if (roleId == Role.ROLE_STUDENT || roleId == Role.ROLE_TEACHER
					|| roleId == Role.ROLE_TEACHING_MANAGER
					|| roleId == Role.ROLE_ADMINISTRATOR
					|| roleId == Role.ROLE_SYSTEM_MANAGER) {
				throw new Exception("不可删除内置角色");
			}
		}
		// 删除角色用户关联表
		roleDao.deleteReUserRoles(roleIds);
		// 删除角色权限信息
		authorityDAO.deleteReAuthorityByRoleId(roleIds);
		// 删除角色信息
		roleDao.deleteRoles(roleIds);
	}

	/**
	 * 获取用户action百名单
	 * 
	 * @author yangzq
	 * @param userId
	 * @return
	 */
	public String uAcWhiteList(Integer userId) {
		List<String> actions = authorityDAO.queryUserAuthorAction(userId);
		String[] actionIds = actions.toString().replaceAll(" ", "")
				.replace("[", "").replace("]", "").split(",");
		List<String> urls = authorityDAO.queryAllActionUrl(actionIds);
		String whiteList = "";
		for (String url : urls) {
			if (whiteList != "") {
				whiteList += url + ",";
			} else {
				whiteList += "," + url + ",";
			}
		}
		return whiteList;
	}

}
