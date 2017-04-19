package com.swust.kelab.repos;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.swust.kelab.model.FunctionModel;
import com.swust.kelab.repos.bean.Query;
import com.swust.kelab.domain.Action;
import com.swust.kelab.domain.Role;

@Repository()
public class RoleDao {

	private SqlSession session;

	@Autowired
	public void setSession(SqlSession session) {
		this.session = session;
	}

	
	/**
	 * 查询Role
	 * @param role
	 * @return 查询成功返回一个List<Department> <br>
	 *         若数据库中无数据则返回一个size为0的list
	 * @see Role
	 * @author lujoCom
	 */
	public List<Role> findRolesByRole(Role role){
		return this.session.selectList("Role.selectRoleByRole", role);
	}
	
	/**
	 * 添加新的角色返回新角色的id
	 * @param role
	 * @return
	 * 		插入成功的id，id>0
	 */
	public Integer insertRole(Role role){
		this.session.insert("Role.insertRole", role);
		return role.getRoleId();
	}
	
	/**
	 * 根据用户编号获取所有的角色编号
	 * @author yangzq
	 * @param userId
	 * @return
	 */
	public List<Integer> selectRoleIdsByUserId(Integer userId){
		return session.selectList("Role.selectRoleIdsByUserId", userId);
	}
	
	/**
	 * 更新角色信息
	 * @author yangzq
	 * @param role
	 * @throws Exception
	 */
	public void updateRole(Role role) throws Exception  {
		// TODO Auto-generated method stub
		this.session.update("Role.updateRole", role);
	}
	
	
	/**
	 * 删除角色用户的关联表
	 * @author yangzq
	 * @param roleIds
	 * @throws Exception
	 */
	public void deleteReUserRoles(String[] roleIds) throws Exception{
		this.session.delete("Role.deleteReUserRoles", roleIds);
	}
	
	/**
	 * 批量删除角色和对应权限
	 * @author yangzq
	 * @param roleIds
	 */
	public void deleteRoles(String[] roleIds) throws Exception {
		// TODO Auto-generated method stub
		this.session.delete("Role.deleteRoles", roleIds);
	}
	
	public List<Role> findAllRole()
	{
		return this.session.selectList("Role.findAllRoles");
	}

}
