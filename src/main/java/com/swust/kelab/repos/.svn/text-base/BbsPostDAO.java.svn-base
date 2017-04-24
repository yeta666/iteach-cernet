package com.swust.kelab.repos;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.swust.kelab.domain.BbsPost;
import com.swust.kelab.model.BbsPostModel;
import com.swust.kelab.model.CourseLearnModel;
import com.swust.kelab.model.CourseStatisticModel;
import com.swust.kelab.model.PlatformStatisticModel;
import com.swust.kelab.model.TeacherBbsModel;
import com.swust.kelab.repos.bean.ListQuery;

@Repository()
public class BbsPostDAO {
	final Logger logger = LoggerFactory.getLogger(getClass());

	private SqlSession sqlSession;

	@Autowired
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	/**
	 * 根据课程来统计发帖数
	 * 
	 * @param params
	 *            查询参数
	 */
	public List<CourseStatisticModel> staPostNumByCourse(Map params) {
		try {
			return sqlSession.selectList("bbsPost.staPostNumByCourse", params);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e.getLocalizedMessage());
		}
		return null;
	}

	/**
	 * 按照学校统计发帖数
	 * 
	 * @return 发帖数统计
	 * @see PlatformStatisticModel
	 */
	public List<PlatformStatisticModel> staPostNumBySchool() {
		return sqlSession.selectList("bbsPost.staPostNumBySchool");
	}

	/**
	 * 统计相关教师对应课程的发帖数
	 * 
	 * @param query
	 *            查询对象
	 * @return 发帖统计信息
	 * @see TeacherBbsModel
	 */
	public Integer staTeacherPostNumByCourse(Map query) {
		return (Integer) sqlSession.selectOne("bbsPost.staTeacherPost", query);
	}

	/**
	 * 统计总的发帖数
	 * 
	 * @return 发帖数
	 */
	public int staTotalPostNum() {
		Integer postNum = sqlSession.selectOne("bbsPost.staTotalPostNum");
		return postNum.intValue();
	}

	/**
	 * 统计主帖列表的帖子总数
	 * 
	 * @param queryType
	 *            查询类型，3表示查询我的回复对应的主贴列表
	 * @param myQuery
	 *            查询条件
	 * @return 主贴总数
	 */
	public int countPostNum(ListQuery myQuery, int queryType) {
		Integer postNum = 0;
		if (queryType == 3) {
			postNum = sqlSession.selectOne("bbsPost.countPostNumByReply",
					myQuery);
		} else {
			postNum = sqlSession.selectOne("bbsPost.countPostNum", myQuery);
		}
		return postNum.intValue();
	}

	/**
	 * 统计主帖列表的帖子总数
	 * 
	 * @param queryType
	 *            查询类型，3表示查询我的回复对应的主贴列表
	 * @param myQuery
	 *            查询条件
	 * @return 主贴总数
	 */
	public int countTopNum(ListQuery myQuery, int queryType) {
		Integer topNum = 0;
		if (queryType == 3) {
			topNum = sqlSession
					.selectOne("bbsPost.countTopNumByReply", myQuery);
		} else {
			topNum = sqlSession.selectOne("bbsPost.countTopNum", myQuery);
		}
		return topNum.intValue();
	}

	/**
	 * 查询主帖列表
	 * 
	 * *@param queryType 查询类型1表示查询置顶主贴， 2表示查询非置顶的主贴，3表示查询我的回复对应的置顶主贴
	 * 4表示查询我的回复对于的非置顶主贴
	 * 
	 * @param myQuery
	 *            查询条件
	 * @return 主贴列表
	 * @see BbsPost
	 */
	public List<BbsPostModel> viewBbsPostList(ListQuery myQuery, int queryType) {
		switch (queryType) {
		case 1:
			return sqlSession.selectList("bbsPost.viewTopPostList", myQuery);
		case 2:
			return sqlSession.selectList("bbsPost.viewPostList", myQuery);
		case 3:
			return sqlSession.selectList("bbsPost.viewTopPostListByReply",
					myQuery);
		case 4:
			return sqlSession
					.selectList("bbsPost.viewPostListByReply", myQuery);
		default:
			break;
		}
		return null;
	}

	/**
	 * 查询一个主贴的详细信息
	 * 
	 * @param bbsPostId
	 *            主贴id
	 * @return 详细信息
	 * @see BbsPostModel
	 */
	public BbsPostModel viewBbsPostDetail(int bbsPostId) {
		return sqlSession.selectOne("bbsPost.viewBbsPostDetail", bbsPostId);
	}

	/**
	 * 增加主贴的访问数
	 * 
	 * @param bbsPostId
	 *            主贴id
	 * @return 影响的数据库行数
	 */
	public int addVisitNum(int bbsPostId) {
		return ((Integer) sqlSession.update("bbsPost.addVisitNum", bbsPostId))
				.intValue();
	}

	/**
	 * 增加主贴回帖数，同时设置最近更新时间
	 * 
	 * @param bbsPostId
	 *            主贴id
	 * @return 影响的数据库行数
	 */
	public int addReplyNum(Map query) {
		return ((Integer) sqlSession.update("bbsPost.addReplyNum", query))
				.intValue();
	}

	/**
	 * 创建主贴
	 * 
	 * @param bbsPost
	 *            主贴对象
	 * @return 插入的数据条数
	 */
	public int createBbsPost(BbsPost bbsPost) {
		return ((Integer) sqlSession.insert("bbsPost.createBbsPost", bbsPost))
				.intValue();
	}

	/**
	 * 置顶
	 * 
	 * @param postId
	 * @return
	 */
	public int setPostTop(Map query) {
		return ((Integer) sqlSession.update("bbsPost.setPostTop", query))
				.intValue();
	}

	/**
	 * 置精华贴
	 * 
	 * @param postId
	 * @return
	 */
	public int setPostEssence(Map query) {
		return ((Integer) sqlSession.update("bbsPost.setPostBest", query))
				.intValue();
	}

	/**
	 * 删除某个主贴
	 * 
	 * @param bbsPostId
	 */
	public int deleteOnePost(int bbsPostId) {
		return ((Integer) sqlSession.delete("bbsPost.deleteOnePost", bbsPostId))
				.intValue();
	}

	/**
	 * 获取最近一次的发帖（发帖时间）
	 * 
	 * @param userId
	 *            发帖人id
	 * @return
	 */
	public BbsPost viewlastPost(int userId) {
		return sqlSession.selectOne("bbsPost.viewlastPost", userId);
	}

	/**
	 * 减少主贴的回复数（回帖被删除）
	 * 
	 * @param bbsPostId
	 *            主贴id
	 * @return 影响的数据行数
	 */
	public int reduceReplyNum(int bbsPostId) {
		return ((Integer) sqlSession
				.update("bbsPost.reduceReplyNum", bbsPostId)).intValue();
	}

	public Integer updateDepaCourInfo(Map query) {
		return (Integer) sqlSession.update("bbsPost.updateDepaCourInfo", query);
	}

	/**
	 * 统计相应课程的主贴数
	 * 
	 * @param courIds
	 *            相应的课程id列表
	 * @return 相应课程的主贴情况
	 */
	public List<CourseLearnModel> staCourseBbsPostState(List<Integer> courIds) {
		return sqlSession.selectList("bbsPost.staCourseBbsPostState", courIds);
	}

	/**
	 * 
	 * @description 根据查询条件统计主贴数
	 * @author libo
	 * @date 2014-9-19 上午12:46:13
	 * 
	 * @param myQuery
	 *            查询条件，包括课程id，机构id等
	 * @return
	 */
	public List<CourseLearnModel> staCourseBbsPostState(ListQuery myQuery) {
		return sqlSession.selectList("bbsPost.staCourseBbsPostStateByQuery",
				myQuery);
	}
}
