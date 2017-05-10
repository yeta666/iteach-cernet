package com.swust.kelab.repos;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.swust.kelab.model.BBSReplyModel;

@Repository("bbsDao")
public class BBSDAO {
	
	private SqlSession sqlSession;
	
	
	@Autowired
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	public List<BBSReplyModel> findReply(int id)
	{
		return sqlSession.selectList("domain.bbs.reply",id);
	}
	
}
