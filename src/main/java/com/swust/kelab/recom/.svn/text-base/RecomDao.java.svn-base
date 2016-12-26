package com.swust.kelab.recom;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.swust.kelab.model.CourseModel;
import com.swust.kelab.model.UserModel;
@Repository
public class RecomDao {
	final Logger logger = LoggerFactory.getLogger(getClass());

	private SqlSession sqlSession;

	@Autowired
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	public int getUserCount(){
		return (Integer)sqlSession.selectOne("recom.queryUserCount");
	}
	public int getCourseCount(){
		return (Integer)sqlSession.selectOne("recom.queryCourseCount");
	}
	public int getRateSize(){
		return (Integer)sqlSession.selectOne("recom.queryRateCount");
	}
	public List<Integer> getUsersByBatch(int page,int pageSize){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("startIndex", (page-1)*pageSize);
		map.put("pageSize", pageSize);
		return sqlSession.selectList("recom.getUsersByBatch", map);
	}
	public List<Integer> getCoursesByBatch(int page,int pageSize){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("startIndex", (page-1)*pageSize);
		map.put("pageSize", pageSize);
		return sqlSession.selectList("recom.getCoursesByBatch", map);
	}
	public List<UserRate> getUserRateByBatch(int page,int pageSize){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("startIndex", (page-1)*pageSize);
		map.put("pageSize", pageSize);
		return sqlSession.selectList("recom.getUserRateByBatch", map);
	}
	public List<Integer> getUserRateItemIds(int userId){
		return sqlSession.selectList("recom.getUserRateItemIds", userId);
	}
	public double getMeanRate(){
		return (Double)sqlSession.selectOne("recom.getMeanRate");
	}
	
	public UserRecom getUserRecom(int userId) {
		return sqlSession.selectOne("recom.getUserRecom", userId);
	}
	
	public UserRate getUserCourseRate(int userId,int courseId){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("courseId", courseId);
		return sqlSession.selectOne("recom.selectUserCourseRate", map);
	}
	public int insertUserRate(int userId, int courseId, double rate) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("courseId", courseId);
		map.put("rate", rate);
		return sqlSession.insert("recom.insertUserRate", map);
	}
	
	public int updateUserRecomItems(List<UserRecom> recomList) {
		return sqlSession.insert("recom.updateUserRecomItems", recomList);
	}
	public int replaceIntoUserRecom(int userId,String recom,Date time){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("recom", recom);
		map.put("time", time);
		return sqlSession.update("recom.replaceIntoUserRecom", map);
	}
	//更新新评分
	public int updateUserRate(int userId, int courseId, double score) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("courseId", courseId);
		map.put("rate", score);
		if(this.getUserCourseRate(userId, courseId)!=null){
			return sqlSession.update("recom.updateUserRate", map);
		}else{
			return this.insertUserRate(userId, courseId, score);
		}
	}

	public int insertUserRateByBatch(List<UserRate> userRateList) {
		return sqlSession.insert("recom.insertUserRateByBatch", userRateList);
	}

	/**
	 * 获得选课最多的几门课程
	 * @param i
	 * @return
	 */
	public List<Integer> getTopKSelectCourse(Integer N) {
		return sqlSession.selectList("recom.getTopKSelectCourse", N);
	}
}
