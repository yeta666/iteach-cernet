package com.swust.kelab.repos;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.swust.kelab.domain.Action;
import com.swust.kelab.domain.FunctionAction;
import com.swust.kelab.domain.ReAuthority;
import com.swust.kelab.model.FunctionModel;

/**
 * 权限管理dao
 * @author yangzq
 *
 */
@Repository()
public class AuthorityDAO {
	
	private SqlSession sqlSession;

	@Autowired
	public void setSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	/**
	 * 查询所有功能模块
	 * 存放缓存
	 * @author yangzq
	 * @param query
	 * @return
	 */
	@Cacheable(value="defaultCache")
	public List<FunctionModel> queryCompetence() {
		return  this.sqlSession.selectList("authority.selectFunctions");
	}
	
	
	/**
	 * 查询所有actions
	 * @author yangzq
	 * @param roleIds
	 * @return
	 */
	public List<ReAuthority> selectActions(List<Integer> roleIds){
		return  this.sqlSession.selectList("authority.selectActions",roleIds);
	}
	
	
	
	/**
	 * 查询角色所有action编号
	 * @author yangzq
	 * @param roleId
	 * @return
	 */
	public List<ReAuthority> queryRoleAcions(Integer roleId){
		return  this.sqlSession.selectList("authority.selectRoleFunctions", roleId);
	}

	/**
	 * 新增保存的角色模块
	 * @author yangzq
	 * @param reAuthority
	 */
	public void saveReAuthority(ReAuthority reAuthority) throws Exception {
		this.sqlSession.insert("authority.saveReAuthority", reAuthority);
	}

	
	/**
	 * 新增保存模块对应的功能
	 * @author yangzq
	 * @param functionAction
	 */
	public void saveFunctionAction(FunctionAction functionAction) throws Exception {
		this.sqlSession.insert("authority.saveFunctionAction", functionAction);
	}

	/**
	 * 根据roleId删除ReAuthority
	 * 并且清除缓存
	 * @author yangzq
	 * @param roleId
	 */
	@CacheEvict(value="defaultCache",allEntries=true)
	public void deleteReAuthorityByRoleId(String[] roleId) {
		this.sqlSession.delete("authority.deleteReAuthorityByRoleId", roleId);
	}
	
	/**
	 * 用户编号筛选出对应角色的action
	 * @author yangzq
	 * @param userId
	 * @return
	 */
	public List<String> queryUserAuthorAction(Integer userId) {
		return this.sqlSession.selectList("authority.queryUserAuthorAction", userId);
	}
	
	/**
	 * 查询所有对应的url
	 * @author yangzq
	 * @param actionIds
	 * @return
	 */
	public List<String> queryAllActionUrl(String[] actionIds) {
		return this.sqlSession.selectList("authority.queryAllActionUrl", actionIds);
	}
	
	

	/**
	 * 保存一二级栏目
	 * @author yangzq
	 * @param func
	 */
	@CacheEvict(value="defaultCache",allEntries=true)
	public void saveFunctionModel(FunctionModel func) {
		sqlSession.insert("authority.saveFunctionModel", func);
	}

	/**
	 * 保存action与栏目的联系表
	 * @author yangzq
	 * @param funcAction
	 */
	public void saveFuncAction(FunctionAction funcAction) {
		sqlSession.insert("authority.saveFuncAction", funcAction);
	}

	/**
	 * 保存action
	 * @author yangzq
	 * @param action
	 * @return
	 */
	@CacheEvict(value="defaultCache",allEntries=true)
	public int saveAction(Action action) {
		sqlSession.insert("authority.saveAction", action);
		return action.getActiId();
	}

	
	/**
	 * 查询一级栏目
	 * @author yangzq
	 * @return
	 */
	public List<FunctionModel> firLevelCol() {
		return sqlSession.selectList("authority.firLevelCol");
	}
	
	/**
	 * 查找二级栏目
	 * @author yangzq
	 * @param firId
	 * @return
	 */
	public List<FunctionModel> secLevelCol(int firId) {
		return sqlSession.selectList("authority.secLevelCol", firId);
	}
	
	/**
	 * 编辑栏目信息
	 * @author yangzq
	 * @param func
	 */
	@CacheEvict(value="defaultCache",allEntries=true)
	public void editFunctionModel(FunctionModel func) {
		sqlSession.update("authority.editFunctionModel", func);
	}

	/**
	 * 编辑操作
	 * @author yangzq
	 * @param action
	 */
	@CacheEvict(value="defaultCache",allEntries=true)
	public void editAction(Action action) {
		sqlSession.update("authority.editAction", action);
	}

	/**
	 * 根据ActionId查询funcAction表中的fumoIds
	 * @author yangzq
	 * @param actIds
	 * @return
	 */
	public List<String> queryFuncActionFumoIds(String actIds) {
		return sqlSession.selectList("authority.queryFuncActionFumoIds", actIds);
	}

	/**
	 * 根据fumoId获取所有actionIds
	 * @author yangzq
	 * @param fumoIds
	 * @return
	 */
	public List<String> queryFuncActionsByFumoIds(String fumoIds){
		return sqlSession.selectList("authority.queryFuncActionsByFumoIds", fumoIds);
	}
	
	/**
	 * 删除action
	 * @author yangzq
	 * @param actionids
	 */
	@CacheEvict(value="defaultCache",allEntries=true)
	public void deleteAction(List<String> actionids) {
		sqlSession.delete("authority.deleteAction", actionids);
	}

	/**
	 * 删除functionAction
	 * @author yangzq
	 * @param del
	 */
	@CacheEvict(value="defaultCache",allEntries=true)
	public void deleteFunctionAction(Map<String, Object> del) {
		sqlSession.delete("authority.deleteFunctionAction", del);
	}

	/**
	 * 删除functionModel
	 * @author yangzq
	 * @param del
	 */
	@CacheEvict(value="defaultCache",allEntries=true)
	public void deleteFunctionModel(Map<String, Object> del) {
		sqlSession.delete("authority.deleteFunctionModel", del);
	}

}
