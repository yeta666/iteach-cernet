package com.swust.kelab.repos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.swust.kelab.domain.VedioServer;

/**
 * 视频服务器信息表DAO层<br>
 * @author EasonLian
 */
@Repository()
public class VedioServerDAO {

	@Autowired
	private SqlSession sqlSession;
	
	public List<VedioServer> viewLocalVedioServerList(String schoolName) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("schoolName", schoolName);
		return sqlSession.selectList("vedioService.viewLocalVedioServer",params);
	}
}
