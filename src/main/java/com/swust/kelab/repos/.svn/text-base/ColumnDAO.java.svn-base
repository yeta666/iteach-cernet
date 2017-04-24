package com.swust.kelab.repos;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.swust.kelab.model.FunctionModel;

@Repository()
public class ColumnDAO {

	@Resource
	private SqlSession sqlSession;
	
	/**
	 * 查询栏目
	 * @author yangzq
	 * @param query
	 * @return
	 */
	public List<FunctionModel> queryColumns(String[] actionIds){
		return sqlSession.selectList("authority.selectColumns", actionIds);
	}
	
	
	
	/**
	 * 根据二级栏目查询所有权限模块
	 * @author yangzq
	 * @param fumoIds
	 * @return
	 */
	public List<FunctionModel> queryAuthorityColumns(List<Integer> fumoIds){
		return sqlSession.selectList("authority.queryAuthorityColumns", fumoIds);
	}
}
