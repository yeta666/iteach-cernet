package com.swust.kelab.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.swust.kelab.domain.Attachment;
import com.swust.kelab.domain.Department;
import com.swust.kelab.domain.Resource;
import com.swust.kelab.domain.User;
import com.swust.kelab.model.PageData;
import com.swust.kelab.model.ResourceModel;
import com.swust.kelab.repos.CourseDAO;
import com.swust.kelab.repos.DepartmentDao;
import com.swust.kelab.repos.ResourceDAO;
import com.swust.kelab.utils.CommonUtil;

/**
 * 
 * @see com.swust.kelab.repos.ResourceDAO
 * @author EasonLian
 */
@Service()
public class ResourceService {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	private ResourceDAO resourceDAO;
	
	@Autowired
	private DepartmentDao departmentDAO;
	
	@Autowired
	private CourseDAO courDAO;
	
	@Autowired
	public void setResourceDAO(ResourceDAO resourceDAO) {
		this.resourceDAO = resourceDAO;
	}
	
	/**
	 * 查看最近资源列表
	 * @author EasonLian
	 */
	public Map<String, Object> viewRecentResourceList() {
		Map<String,Object> dataMap = new HashMap<String,Object>();
		try {
			dataMap.put("recentResoList", resourceDAO.viewRecentResourceList());
		} catch(Exception e) {
			logger.error("viewRecentResourceList error!");
		}
		return dataMap;
	}
	
	/**
	 * 通过资源id查询单个
	 * @param resoId
	 * @return
	 */
	public Resource viewResourceByResoId(Integer resoId) {
		try {
			Resource reso = resourceDAO.selectOneById(resoId,3);
			return reso ;
		} catch(Exception e){ 
			logger.error("viewResourceByResoId error !!!");
			return null;
		}
	}
	
	/**
	 * 通过课程id查询封面图片，作为视频默认图
	 * @param courId
	 * @return
	 */
	public String getCourseCoverImgById(String projectPath,Integer courId) {
		try {
			Attachment atta = courDAO.getCourseCoverImgById(courId);
			if(atta != null) {
				String imgPath = projectPath + atta.getAttaLocation() + atta.getAttaFilename();
				if(new File(imgPath).exists()) {
					return imgPath;
				}
			}
		} catch(Exception e) {
			logger.error("getCourseCoverImgById error!");
		}
		return "/upload/eduman/3138727465400534854.png";
	}
	
	/**
	 * 查询所有的未被分配的资源数据,以及对应用户已选择的资源
	 * @author EasonLian
	 */
	public Map<String,Object> viewResourceListByChapterPage(
			Integer resoId, String resoName,Integer resoType,Integer chapId,Integer courId) {
		Map<String,Object> dataMap = new HashMap<String,Object>();
		try {
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("resoId", resoId);
			params.put("resoName", resoName);
			params.put("resoType", resoType);
			params.put("courId", courId);
			params.put("chapId", chapId);
			params.put("resoStatus", 1);
			//	查询所有未分配的资源
			dataMap.put("freeResoList", 
					resourceDAO.viewResourceListByChapterPage(params));
			//	被当前章节选中的资源
			if(chapId == null)
				dataMap.put("chosenResoList", null);
			else {
				dataMap.put("chosenResoList", 
						resourceDAO.viewResourceListWithUserChoose(params));
			}
			return dataMap;
		} catch(Exception e) {
			logger.error("viewResourceListByChapter error!");
			return null;
		}
	}
	
	/**
	 * 删除资源数据<br>
	 * @param ids 待删除的id
	 * @return 是否批量删除成功
	 * @author EasonLian
	 * @throws Exception
	 */
	@Transactional()
	public boolean delResourceByAdmin(String ids) {
		try {
			if(ids != null) {
				if(ids.endsWith(","))
					ids = ids.substring(0, ids.length()-1);
				return resourceDAO.delResourceByAdmin("("+ids+")") >=0 ? true: false;
			}
			return false;
		} catch(Exception e) {
			logger.error("delResourceByAdmin Error!!!!");
			return false;
		}
	}
	
	/**
	 * 待添加的Resource对象<br>
	 * @param resource
	 * @return 是否保存成功
	 */
	public Integer addResourceByAdmin(Resource reso) {
		try {
			if(resourceDAO.addResourceByAdmin(reso) == 1)
				return reso.getResoId()==null ? null : reso.getResoId();
			else
				return null;
		} catch(Exception e) {
			logger.error("addResourceByAdmin Error!");
			return null;
		}
	}
	
	/**
	 * 修改Resource对象<br>
	 * @param resource
	 * @return 是否保存成功
	 */
	public String modifyResourceByAdmin(Resource reso,String relativePath) {
		try {
			Resource savedReso = resourceDAO.selectOneById(reso.getResoId(),null);
			if(savedReso == null)
				return null;
			else {
				File oldFile = new File(relativePath + savedReso.getResoLocation());
				if(oldFile.exists())
					oldFile.delete();
				savedReso.setResoCourId(reso.getResoCourId());
				savedReso.setResoType(reso.getResoType());
				savedReso.setResoTitle(reso.getResoTitle());
				savedReso.setResoFilename(reso.getResoFilename());
				savedReso.setResoDescribe(reso.getResoDescribe());
				savedReso.setResoLocation(reso.getResoLocation());
				savedReso.setResoVediotime(reso.getResoVediotime());
				savedReso.setResoStatus(reso.getResoStatus());
				savedReso.setResoUserid(reso.getResoUserid());
				return (resourceDAO.modifyResourceByAdmin(savedReso) == 1) ? 
						(reso.getResoLocation() == null ? "": reso.getResoLocation()) : null;
			}
		} catch(Exception e) {
			logger.error("modifyResourceByAdmin Error!");
e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 查询总条数
	 * @author EasonLian
	 * @return 总条数
	 */
	public Integer getResourceCount(
			Integer courId,
			String resoTitle,
			Integer resoType,
			String courName,
			int page,
			int rows) {
		try {
			return resourceDAO.
					getResourceCount(courId, resoTitle, resoType, courName, page, rows);
		} catch (Exception e) {
			logger.error("getResourceCount Error !!!");
			return 0;
		}
	}
	
	/**
	 * admin页面的资源管理页面基本列表查询，<br>
	 * 以及模糊查询<br>
	 * @author EasonLian
	 * @param page
	 * @param rows
	 * @param resoName
	 * @param resoType
	 * @return 资源对象数组
	 */
	@Transactional
	public Map<String, Object> viewResourceListForAdmin(
													User user,
													Integer courId,
													String resoTitle,
													Integer resoType,
													String courName,
													String startTime,
													String endTime,
													String keyWord,
													String pages,
													int rows ) {
		Map<String, Object> dataMap = new HashMap<String,Object>();
		courId=courId==null||courId<=0?null:courId;
		try {
			String[] pageArray = pages.split(",");
			int page = Integer.valueOf(pageArray[0]);
			int fetchSize = rows * pageArray.length;
			if(page > 0 && rows > 0) {
				Date sTime = null;
				Date eTime = null;
				try {
					sTime = CommonUtil.getDateFromString("yyyy-MM-dd", startTime);
					eTime = CommonUtil.getDateFromString("yyyy-MM-dd", endTime);
				} catch(Exception e) {
					logger.error("ViewResourceListByAdmin\n日期转换错误！");
				}
				String courIds = "";
				String depaIds = "";
				if(user != null) {
					if(user.getUserType() == 2) {			//	如果是教师，只查询他主讲课程
						List<Integer> courList = courDAO.viewAllCourIdByTeacherId(user.getUserId());
						if(courList!=null&&!courList.isEmpty()){
							for(Integer myCourId : courList)
								courIds += (myCourId + ",");
							courIds = courIds.substring(0, courIds.length()-1);
						}else{
						    courIds=null;
						}
						depaIds=null;
					} else if(user.getUserType() == 3) {
						List<Department> depaList = departmentDAO.viewAllDepaListBelongsToParentDepar(
			        			user.getUserDepaId(),user.getUserDepartType(), null);
						if(depaList!=null&!depaList.isEmpty()){
							for(Department depa : depaList)
								depaIds += depa.getDepaId() + ",";
							depaIds = depaIds.substring(0,depaIds.length()-1);
			        	}else{
			        	    depaIds=null;
			        	}
						 courIds=null;
					}else{
					    courIds=null;
					    depaIds=null;
					}
				}
				//	查询指定条数
				List<ResourceModel> resoList = 
						resourceDAO.viewResourceListForAdmin(courIds,depaIds,courId,resoTitle,resoType,
								courName,sTime == null? null:sTime.getTime()/1000,
								eTime == null? null:eTime.getTime()/1000,keyWord, (page-1)*rows, fetchSize);
				List<PageData> allData = new ArrayList<PageData>();
				//	循环分配页码
				for(int i=0;i<pageArray.length;i++) {
					int pageNum = Integer.valueOf(pageArray[i]);
					int start = i * rows;
					int end = start + rows;
					if(end > resoList.size())
						end = resoList.size();
					if(start >= resoList.size())
						break;
					List<ResourceModel> subResoList = resoList.subList(start,end);
					PageData pageData = new PageData(pageNum,subResoList);
					allData.add(pageData);
				}
				dataMap.put("pageData",allData);
				int total = getResourceCount(courId,resoTitle, resoType, courName, page, rows);
				dataMap.put("totalCount", total);
				int totalPage = total/rows;
				dataMap.put("totalPage",(total%rows == 0) ? totalPage:(totalPage+1));
				return dataMap;
			}
		} catch(Exception e) {
			logger.error("viewResourceListForAdmin Error!"+e.getLocalizedMessage(),e);
		}
		return dataMap;
	}
	
	/**
	 * download one selected resource.<br>
	 * 
	 * @Author EasonLian
	 * @return Resource Entity
	 */
	public Resource downloadDocument(Integer rid,Integer resoType) {
		try {
			if(resoType == null 
					|| resoType == 0)
				resoType = 2;
			Resource resource = null;
			switch(resoType) {
			case 1 : resource = resourceDAO.selectOneResource(rid,Resource.LINK);break;
			case 2 : resource = resourceDAO.selectOneResource(rid,Resource.DOCUMENT);break;
			case 3 : resource = resourceDAO.selectOneResource(rid,Resource.VEDIO);break;
			default : break;
			}
			if(resource != null) {
				if(resource.getResoFilename() != null 
						&& !resource.getResoFilename().equals("")
						&& resource.getResoLocation() != null
						&& !resource.getResoLocation().equals("")) {
					return resource;
				}
			}
			return null;
		} catch (Exception e) {
			logger.error("downladDocument Error!");
			return null;
		}
	}
	
	/**
	 * view all resource method which is limmited by course,<br>
	 * resoTitle,resoTypel,beginTime and endTime<br>
	 * 
	 * @author EasonLian
	 * @param courId 课程id
	 * @param resoTitle 资源名称
	 * @param resoType 资源类型
	 * @param beginTime 上传起始时间
	 * @param endTime 上传结束时间
	 * @return 分类的资源对象
	 */
	public Map<String, Object> viewResourceList(Integer courId,String resoTitle,
			Date beginTime,Date endTime,Integer chapId,int page,int rows) {
		Map<String, Object> resourceMap = new HashMap<String, Object>();
		try {
			List<Resource> resourceList =  resourceDAO.viewResourceList(
					courId,resoTitle,null,beginTime,endTime,chapId,page,rows);
			//	将所有资源数据按照类型分类
			List<Resource> links = new ArrayList<Resource>();
			List<Resource> documents = new ArrayList<Resource>();
			Iterator<Resource> it = resourceList.iterator();
			while(it.hasNext()) {
				Resource res = it.next();
				switch(res.getResoType()) {
				case 1 :		//	type=1 为 链接资源
					links.add(res);
					break;
				case 2 :		//	type=2 为 文档资源
					documents.add(res);
					break;
				default :
					break;
				}
			}
			resourceMap.put("links", links);
			resourceMap.put("documents",documents);
			return resourceMap;
		} catch(Exception e) {
			logger.error("viewResourceList error!");
			return null;
		}
	}
	
	/**
	 * insert one resource into database<br>
	 * 
	 * @param resource
	 * @author EasonLian
	 * @return if insert option was succesful
	 */
	@Transactional
	public boolean insertResource(Resource resource) {
		try {
			//	while the returned affectedRows = 1,return true
			int affectedRows = resourceDAO.insertResource(resource);
			return affectedRows==1 ? true:false;
		} catch(Exception e) {
			logger.error("insert Resource error!");
			return false;
		}
	}
	
	@SuppressWarnings("finally")
	public List<Resource> findResourceList(int courseId)
	{
		List<Resource> resources = null;
		try {
			resources = resourceDAO.findResourceList(courseId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("findResourceList error");
		}
		finally
		{
			return resources;
		}
	}
}
