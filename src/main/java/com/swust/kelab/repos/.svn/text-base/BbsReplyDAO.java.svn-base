package com.swust.kelab.repos;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.swust.kelab.domain.BbsReply;
import com.swust.kelab.model.BBSReplyModel;
import com.swust.kelab.model.CourseLearnModel;
import com.swust.kelab.model.CourseStatisticModel;
import com.swust.kelab.model.PlatformStatisticModel;
import com.swust.kelab.model.TeacherBbsModel;
import com.swust.kelab.repos.bean.ListQuery;

@Repository()
public class BbsReplyDAO {
	final Logger logger = LoggerFactory.getLogger(getClass());

	private SqlSession sqlSession;

	@Autowired
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	/**
	 * 根据课程来统计回帖数
	 * 
	 * @param params
	 *            查询参数
	 * @see CourseStatisticModel
	 */
	public List<CourseStatisticModel> staReplyNumByCourse(Map params) {
		try {
			return sqlSession
					.selectList("bbsReply.staReplyNumByCourse", params);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e.getLocalizedMessage());
		}
		return null;
	}

	/**
	 * 根据学校来统计回帖数
	 * 
	 * @return 回帖数统计
	 * @see PlatformStatisticModel
	 */
	public List<PlatformStatisticModel> staReplyNumBySchool() {
		return sqlSession.selectList("bbsReply.staReplyBySchool");
	}

	/**
	 * 统计相关教师对应课程的答疑数
	 * 
	 * @param query
	 *            查询对象
	 * @return 答疑统计信息
	 * @see TeacherBbsModel
	 */
	public Integer staTeacherReplyNumByCourse(Map query) {
		return (Integer) sqlSession
				.selectOne("bbsReply.staTeacherReply", query);
	}

	/**
	 * 统计总的回帖数
	 * 
	 * @return 回帖数
	 */
	public int staTotalReplyNum() {
		Integer convert = sqlSession.selectOne("bbsReply.staTotalReplyNum");
		return convert.intValue();
	}

	/**
	 * 查询主贴对应的回帖总数
	 * 
	 * @param bbsPostId
	 *            主贴id
	 * @return 回帖总数
	 */
	public int countReplyNumByPost(int bbsPostId) {
		Integer convert = sqlSession.selectOne("bbsReply.countReplyNumByPost",
				bbsPostId);
		return convert.intValue();
	}

	/**
	 * 查询一个主贴对应的所有回帖列表
	 * 
	 * @param query
	 *            查询条件
	 * @return 回帖列表
	 */
	public List<BBSReplyModel> viewReplyList(Map query) {
		return sqlSession.selectList("bbsReply.viewReplyList", query);
	}

	/**
	 * 新增一条回帖
	 * 
	 * @param bbsReply
	 *            回帖对象，包含回帖的详细内容
	 * @return 新增的条数
	 */
	public int createBbsReply(BbsReply bbsReply) {
		Integer convert = sqlSession
				.insert("bbsReply.createBbsReply", bbsReply);
		return convert.intValue();
	}

	/**
	 * 删除某个主贴的所有回复
	 * 
	 * @param bbsPostId
	 *            主贴id
	 */
	public int deleteReplyOfPost(int bbsPostId) {
		return ((Integer) sqlSession.delete("bbsReply.deleteReplyOfPost",
				bbsPostId)).intValue();
	}

	/**
	 * 获取指定用户最近的回复（时间）
	 * 
	 * @param userId
	 *            用户id
	 * @return 回复对象
	 */
	public BbsReply viewlastReply(int userId) {
		return sqlSession.selectOne("bbsReply.viewlastReply", userId);
	}

	/**
	 * 删除指定回帖
	 * 
	 * @param replyId
	 *            回帖id
	 * @return 实际删除的数据记录条数
	 */
	public int deleteReplyById(int replyId) {
		return sqlSession.delete("bbsReply.deleteReplyById", replyId);
	}

	/**
	 * 根据指定id获取一条回帖
	 * 
	 * @param replyId
	 *            回帖id
	 * @return 回帖
	 */
	public BbsReply viewOneReply(int replyId) {
		return sqlSession.selectOne("bbsReply.viewOneReply", replyId);
	}

	/**
	 * 统计相应课程的回帖数
	 * 
	 * @param courIds
	 *            相应的课程id列表
	 * @return 相应课程的回帖情况
	 */
	public List<CourseLearnModel> staCourseBbsReplyState(List<Integer> courIds) {
		return sqlSession
				.selectList("bbsReply.staCourseBbsReplyState", courIds);
	}

	/**
	 * 
	 * @description 根据查询条件统计回帖数
	 * @author libo
	 * @date 2014-9-19 上午12:48:22
	 * 
	 * @param myQuery
	 *            查询条件，包括课程id列表，机构id等
	 * @return 课程列表
	 */
	public List<CourseLearnModel> staCourseBbsReplyState(ListQuery myQuery) {
		return sqlSession.selectList("bbsReply.staCourseBbsReplyStateByQuery",
				myQuery);
	}
}
