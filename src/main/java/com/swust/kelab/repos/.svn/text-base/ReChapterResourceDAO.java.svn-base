package com.swust.kelab.repos;

import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository()
public class ReChapterResourceDAO {
	
	@Autowired
	private SqlSession sqlSession;
	
	/**
	 * 批量添加
	 * @param params
	 * @author EasonLian
	 */
	public int addReChapterResourceDAO(Map<String,Object> params) {
		return sqlSession.insert("reChapterResource.addReChapterResource", params);
	}
	
	/**
	 * 批量删除资源章节中间表
	 * @param params
	 * @throws Exception
	 * @author easonlian
	 */
	public int delReChapterResourceDAO(Map<String,Object> params) throws Exception {
		return sqlSession.delete("reChapterResource.delReChapterResource",params);
	}
}
