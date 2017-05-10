package com.swust.kelab.repos;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.swust.kelab.domain.ReUserRole;
import com.swust.kelab.domain.Role;
import com.swust.kelab.domain.User;

@Repository()
public class ReUserRoleDAO {

	private SqlSession sqlSession;

	@Autowired
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	/**
	 * 根据用户id获取用户对于的角色列表
	 * 
	 * @param userId
	 *            用户id
	 * @return 角色列表
	 * @see Role
	 */
	public List<Role> viewRoleListByUser(int userId) {
		return sqlSession.selectList("reUserRole.viewRoleListByUser", userId);
	}

	/**
	 * 插入用户权限,成功后返回ReroId
	 * 
	 * @param userRole
	 * @return rero_id
	 * 
	 */
	public void saveUserRole(List<ReUserRole> roles) {
		sqlSession.insert("reUserRole.saveUserRole", roles);
	}
	/**
	 * 插入用户权限,成功后返回ReroId
	 * 
	 * @param userRole
	 * @return rero_id
	 * 
	 */
	public Integer saveUserRole(ReUserRole roles) {
		sqlSession.insert("reUserRole.saveUserRoleSingal", roles);
		return roles.getReroId();
	}

	/**
	 * 查询此用户该权限是否已经存在,若存在返回该权限id,否则返回空
	 * 
	 * @param userRole
	 * @return rero_id
	 */
	public Integer findReroIdByReUserRole(ReUserRole userRole) {
		return sqlSession.selectOne("reUserRole.findReUserRoleId", userRole);
	}
	
	/**
	 * 删除用户权限
	 * @param userRoleIds
	 * @return
	 */
	public Integer deleteUserRole(Map<String, Object> deleMap){
		return sqlSession.delete("reUserRole.deleteReUserRoleId", deleMap);
	}
	
}
