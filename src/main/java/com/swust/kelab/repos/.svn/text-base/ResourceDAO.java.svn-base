package com.swust.kelab.repos;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.swust.kelab.domain.Resource;
import com.swust.kelab.model.ResourceModel;

/**
 * 
 * @author EasonLian
 * @since 1.0
 */
@Repository()
public class ResourceDAO {

	private SqlSession sqlSession;
	
	@Autowired
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	/**
	 * 查看最近资源列表
	 * @author EasonLian
	 */
	public List<Resource> viewRecentResourceList() throws Exception {
		return sqlSession.selectList("resource.viewRecentResourceList");
	}
	
	/**
	 * 查询用户选择的课程
	 * @param params
	 * @return
	 */
	public List<Resource> viewResourceListWithUserChoose(Map<String, Object> params) {
		return sqlSession.selectList("resource.viewResourceListWithUserChoose", params);
	}
	
	/**
	 * 查询所有的未被分配的资源数据
	 * @author EasonLian
	 */
	public List<Resource> viewResourceListByChapterPage(Map<String, Object> params) {
		return sqlSession.selectList("resource.viewResourceListByChapterPage",params);
	}
	
	/**
	 * 删除资源数据<br>
	 * @param ids 待删除的id
	 * @return 被删除的行
	 * @throws Exception
	 */
	public int delResourceByAdmin(String ids) throws Exception {
		Map<String ,Object> params = new HashMap<String, Object>();
		params.put("ids", ids);
		sqlSession.delete("resource.delRcreByChapterId",params);
		return sqlSession.delete("resource.delResourceByAdminByUpdate",params);
	}
	
	/**
	 * 待添加的Resource对象<br>
	 * @param resource
	 * @return status状态
	 */
	public int addResourceByAdmin(Resource reso) {
		if(reso.getResoId() == null)
			return sqlSession.insert("resource.addResourceByAdmin", reso);
		else
			return sqlSession.update("resource.modResourceByAdmin", reso);
	}
	
	/**
	 * 修改Resource对象<br>
	 * @param resource
	 * @return status状态
	 */
	public int modifyResourceByAdmin(Resource reso) throws Exception {
		return sqlSession.update("resource.modResourceByAdmin", reso);
	}
	
	/**
	 * 通过id查询一条
	 * @author EasonLian
	 * @param resoId
	 * @return Resource
	 * @throws Exception
	 */
	public Resource selectOneById(Integer resoId,Integer resoType) throws Exception {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("resoId",resoId);
		params.put("resoType", resoType);
		return sqlSession.selectOne("resource.selectOneResourceById",params);
	}
	
	/**
	 * 资源总条数查询<br>
	 * 以及模糊查询<br>
	 * @author EasonLian
	 * @param page
	 * @param rows
	 * @param resoName
	 * @param resoType
	 * @return 资源对象数组
	 */
	public Integer getResourceCount(
			   Integer courId,
			   String resoTitle,
			   Integer resoType,
			   String courName,
			   int page,
			   int rows ) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("resoTitle",resoTitle);
		params.put("courId", courId);
		params.put("resoType",resoType);
		params.put("courName", courName);
		params.put("start",(page-1)*rows);
		params.put("rows",rows);
		return sqlSession.selectOne("resource.getResourceCount",params);
	}
	
	/**
	 * admin页面的资源管理页面基本列表查询，<br>
	 * 以及模糊查询<br>
	 * @author EasonLian
	 * @param page
	 * @param rows
	 * @param resoName
	 * @param resoType
	 * @param startTime	起始毫秒
	 * @param resoName 结束毫秒
	 * @param keyWord
	 * @return 资源对象数组
	 */
	public List<ResourceModel> viewResourceListForAdmin(
											   String courIds,
											   String depaIds,
											   Integer courId,
											   String resoTitle,
											   Integer resoType,
											   String courName,
											   Long startTime,
											   Long endTime,
											   String keyWord,
											   int page,
											   int rows ) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("courIds", courIds);
		params.put("depaIds", depaIds);
		params.put("resoTitle",resoTitle);
		params.put("courId", courId);
		params.put("resoType",resoType);
		params.put("courName", courName);
		params.put("sTime", startTime);
		params.put("eTime", endTime);
		params.put("keyWord", keyWord);
		params.put("start",page);
		params.put("rows",rows);
		return sqlSession.selectList("resource.viewResourceListForAdmin", params);
	}
	
	/**
	 * 按照类型查询Resource对象
	 * @param resoId
	 * @param resoType
	 * @return
	 * @throws Exception
	 */
	public Resource selectOneResource(Integer resoId,Integer resoType) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("resoId", resoId);
		//	指定查询文档类型数据
		params.put("resoType", resoType);
		return sqlSession.selectOne("resource.selectOneResource",params);
	}
	
	
	public List<Resource> viewResourceList(Integer courId,
			String resoTitle,Integer resoType,Date beginTime,Date endTime,
			Integer chapId,int page,int rows) throws Exception {
		Map<String,Object> params = new HashMap<String,Object>();
		//	Set all select param into a Map
		params.put("courId", courId);
		params.put("resoTitle", resoTitle);
		params.put("resoType", resoType);
		params.put("beginTime", beginTime);
		params.put("endTime", endTime);
		params.put("chapId", chapId);
		params.put("start", (page-1)*rows);
		params.put("rows", rows);
		return sqlSession.selectList("resource.viewResourceList", params);
	}
	
	public int insertResource(Resource resource) throws Exception {
		return sqlSession.insert("resource.insertResource",resource);
	}
	
	/**
	 * 统计总的资源数
	 * 
	 * @return 资源数
	 */
	public int staResourceNum(){
	    Integer reNum=sqlSession.selectOne("resource.staResourceNum");
	    return reNum.intValue();
	}
	
	/**
	 * 查询某门课程的所有资源
	 * 
	 * @author ZhangXin
	 * @param courseId
	 * @return
	 */
	public List<Resource> findResourceList(int courseId) throws Exception
	{
		return sqlSession.selectList("resource.findResourceList",courseId);
	}
}
