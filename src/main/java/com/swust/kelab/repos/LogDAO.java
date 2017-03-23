package com.swust.kelab.repos;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.swust.kelab.domain.Log;
import com.swust.kelab.model.LogModel;
import com.swust.kelab.repos.bean.ListQuery;
@Repository()
public class LogDAO {
final Logger logger= LoggerFactory.getLogger(this.getClass());
	private SqlSession sqlSession;
	
	@Autowired
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	public void insertLog(Log log){
		this.sqlSession.insert("log.saveOptionLog",log);
	}
	
	/**
	 * 根据条件查询需要日志
	 * @param map
	 * @return
	 */
	public List<LogModel> queryLogsByExport(ListQuery queryMap) {
		return sqlSession.selectList("log.queryLogsByExport", queryMap);
	}
	/**
	 * 查询日志总记录数
	 * @param queryMap
	 */
	public int queryLogsCount(ListQuery queryMap) {
		return ((Integer)sqlSession.selectOne("log.queryLogsCount", queryMap)).intValue();
	}
	/**
	 * 到处
	 * @param map
	 * @return
	 */
	public List<LogModel> queryLogs(ListQuery map) {
		return sqlSession.selectList("log.queryLogs", map);
	}
	/**
	 * 删除查询条件下的日志
	 * @param queryMap
	 */
	public int deleteLogs(ListQuery queryMap) {
		return ((Integer)sqlSession.delete("log.deleteLogs", queryMap)).intValue();
	}
}
