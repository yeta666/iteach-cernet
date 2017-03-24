package com.swust.kelab.repos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.swust.kelab.domain.NoticeAnnouncement;
import com.swust.kelab.model.NoticeAnnouncementModel;

/**
 * 广播通知消息DAO类
 * @author EasonLian,ZhangXin
 *
 */
@Repository("announcementDao")
public class NoticeAnnouncementDAO {
	
	private SqlSession sqlSession;
	
	@Autowired
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
//	public NoticeAnnouncement viewOneNoticeAnnouncementById(Integer noanId) throws Exception  {
//		return sqlSession.selectOne("noticeAnnouncement",noanId);
//	}
	
	/**
	 * 批量删除消息
	 * @param notice
	 * @return
	 * @throws Exception
	 * @author easonlian
	 */
	public int delNoticeAnnouncement(String ids) throws Exception {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("ids", ids);
		return sqlSession.delete("noticeAnnouncement.delNoticeAnnouncement",params);
	}
	
	/**
	 * 批量添加消息
	 * @param notice
	 * @return
	 * @throws Exception
	 * @author easonlian
	 */
	public int addNoticeAnnouncement(NoticeAnnouncement notices) {
		return sqlSession.insert("noticeAnnouncement.addNoticeAnnouncement",notices);
	}
	
	/**
	 * 修改单条消息
	 * @param notice
	 * @return
	 * @author easonlian
	 */
	public int modNoticeAnnouncement(NoticeAnnouncement notice) throws Exception {
		return sqlSession.update("noticeAnnouncement.modNoticeAnnouncement",notice);
	}
	
	/**
	 * 查询一定数量的消息
	 * @param roleIds
	 * @throws Exception
	 * @author easonlian
	 */
	public List<NoticeAnnouncementModel> viewNoticeAnnouncementList(
			Map<String,Object> params) throws Exception {
		return sqlSession.selectList("noticeAnnouncement.viewNoticeAnnouncementList",params);
	}
	
	/**
	 * 模糊查询时的总条数
	 * @param roleIds
	 * @param noanId
	 * @return 总条数
	 * @throws Exception
	 */
	public int getNoticeAnnouncementListCount(
												Integer userType,
												Integer noanId) throws Exception {
		Map<String,Object> params = new HashMap<String,Object>();		//	暂时没有设置条件，默认全部查询出来
		params.put("userType", userType);
		params.put("noanId", noanId);
		return ((Integer)sqlSession.selectOne("noticeAnnouncement.getNoticeAnnouncementListCount", params)).intValue();
	}
	
	public List<NoticeAnnouncement> findAnnouncement()
	{
		return sqlSession.selectList("domain.announcement.all");
	}

	/**
	 * 根据id查询单条通知公告
	 * @param noanId
	 * @author easonlian
	 */
	public NoticeAnnouncement viewOneNoticeAnnouncement(Integer noanId) {
		return sqlSession.selectOne("noticeAnnouncement.viewOneNoticeAnnouncement",noanId);
	}
}
