package com.swust.kelab.repos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.swust.kelab.domain.Chapter;
import com.swust.kelab.model.ChapterModel;

/**
 * 章节相关的数据库操作<br>
 * 
 * @author EasonLian
 *
 */
@Repository()
public class ChapterDAO {

	private SqlSession sqlSession;
	
	@Autowired
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	/**
	 * 通过chapId查找单条数据
	 * @param chapId
	 * @return 查到的条目对象
	 */
	public Chapter findOneChapter(Integer chapId) throws Exception {
		return sqlSession.selectOne("chapter.findOneChapter", chapId);
	}
	
	/**
	 * 修改章节信息
	 * @param chapName
	 * @param courId
	 * @param chapDescribe
	 * @param chapOrdinal
	 * @return 是否成功
	 * @throws Exception
	 */
	public int modChapterByAdmin(String chapName, Integer courId,
			String chapDescribe, String chapOrdinal,Integer chapId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("chapId", chapId);
		params.put("chapName", chapName);
		params.put("courId", courId);
		params.put("chapDescribe", chapDescribe);
		params.put("chapOrdinal", chapOrdinal);
		return sqlSession.update("chapter.updateChapterByAdmin", params);
	}
	
	/**
	 * 添加章节资源
	 * @param chapName
	 * @param chapDescribe
	 * @param chapOrdinal
	 * @return 添加的行数
	 * @author EasonLian
	 */
	public int addChapterByAdmin(Chapter chapter) throws Exception {
		return sqlSession.insert("chapter.addChapterByAdmin", chapter);
	}
	
	/**
	 * 删除章节，以及章节资源中间表<br>
	 * @param ids
	 * @return 被删除的条数
	 * @throws Exception
	 * @author EasonLian
	 */
	public int delChapterByAdmin(String ids) throws Exception {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ids", ids);
		//	删除和资源中间表的数据
		sqlSession.delete("reChapterResource.delReChapterResourceByChapId",params);
		sqlSession.delete("learnProcessRecord.delLearningProcessRecordByChapId",params);
		return sqlSession.delete("chapter.delChapterByAdmin",params);
	}
	
	/**
	 * 管理员章节管理页面<br>
	 * 查询所有章节及他所属的资源文件<br>
	 * @param courId
	 * @return List<Chapter>
	 */
	public List<ChapterModel> viewAllChapterList(Map<String, Object> params) throws Exception {
		return sqlSession.selectList("chapter.viewAllChapterList",params);
	}
	
//	/**
//	 * 查询章节信息、以及条件查询
//	 * @param params
//	 * @return
//	 * @throws Exception
//	 */
//	public Integer viewAllChapterCountWithParams(Map<String, Object> params) throws Exception {
//		return sqlSession.selectOne("",params);
//	}
	
	/**
	 * 通过chapId查询所属的课程<br>
	 * @param chapId 章节id
	 * @author EasonLian
	 * @return courId 章节id
	 */
	public Integer selectCourIdByChapId(Integer chapId) throws Exception {
		return sqlSession.selectOne("chapter.selectCourIdByChapId",chapId);
	}
	
	/**
	 * @author EasonLian
	 * @param courId
	 * @return 课程下的所有章节
	 * @throws 将错误抛出到service层处理
	 */
	public List<ChapterModel> viewChapterListByCourse(Integer courId) throws Exception {
		return sqlSession.selectList("chapter.viewChapterListByCourse", courId);
	}
	
	/**
     * 评价一个课程的章节（主要用于课程的排序）
     * @param map 章节及评分信息
     * @return 影响的数据行数
     */
    public Integer assessChapter(Map map){
        return (Integer)sqlSession.update("chapter.assessChapter", map);
    }
}
