package com.swust.kelab.repos;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.swust.kelab.domain.LearnProcessRecord;

/**
 * 
 * 视频学习记录登记<br> 
 * @author Administrator
 */
@Repository()
public class LearnProcessRecordDAO {

	@Autowired
	private SqlSession sqlSession;
	
	public LearnProcessRecord findOneByUserIdAndChapId(Integer userId,Integer chapId) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("chapId", chapId);
		LearnProcessRecord lpr = sqlSession.selectOne("learnProcessRecord.selectOneByUserIdAndChapId", params);
		return lpr;
	}
	
	/**
     * 通过userid chapid resoid，查看用户是否看过这个章节的这个视频
     * @param userId
     * @param chapId
     * @param resoId
     * @author easonlian
     * @throws Exception
     */
    public int isUserViewThisChapterVideoReso(
    		Integer userId,Integer chapId,Integer resoId) throws Exception {
    	Map<String,Object> params = new HashMap<String,Object>();
    	params.put("userId", userId);
    	params.put("chapId", chapId);
    	params.put("resoId", resoId);
    	return ((Integer)sqlSession.selectOne("learnProcessRecord.isUserViewThisChapterVideoReso",params)).intValue();
    }
	
	/**
	 * 更新学生学习时间段<br>
	 * @author EasonLian
	 * @param stydyTime
	 * @throws Exception
	 */
	public void recordLearnProcess(Integer lpreId,Integer userId,Integer chapId,int studyTime) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("studyTime",studyTime);
		params.put("endtime", new Date());
		params.put("lpreId", lpreId);
		sqlSession.update("learnProcessRecord.updateOneLearnProcessRecord", params);
	}
	
	/**
	 * 新建学生学习时间段<br>
	 * @param userId
	 * @param chapId
	 * @param content
	 * @param endTime
	 * @param learntime
	 * @throws Exception
	 * @author EasonLian
	 */
	public LearnProcessRecord insertLearnProcessRecord(Integer userId,Integer chapId,
				Integer resoId,String content) throws Exception {
		LearnProcessRecord lpr = new LearnProcessRecord();
		lpr.setLpreUserId(userId);
		lpr.setLpreChapId(chapId);
		lpr.setLpreResoId(resoId);
		lpr.setLpreContent("");
		lpr.setLpreLearntime(0);
		sqlSession.insert(
				"learnProcessRecord.insertOneLearnProcessRecord", lpr);
		return lpr;
	}
	
	/**
	 * 查询存在的记录条数
	 * @param userId
	 * @param chapId
	 * @param resoId
	 * @author easonlian
	 * @throws Exception
	 */
	public int getLearnProcessCountByResoId(Integer userId,Integer chapId, Integer resoId) throws Exception {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("userId", userId);
		params.put("chapId", chapId);
		params.put("resoId", resoId);
		return ((Integer)sqlSession.selectOne("learnProcessRecord.getLearnProcessCountByResoId",params)).intValue();
	}
}
