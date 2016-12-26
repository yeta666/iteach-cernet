package com.swust.kelab.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.swust.kelab.domain.NoticeAnnouncement;
import com.swust.kelab.domain.User;
import com.swust.kelab.model.NoticeAnnouncementModel;
import com.swust.kelab.model.PageData;
import com.swust.kelab.repos.NoticeAnnouncementDAO;
import com.swust.kelab.repos.UserDAO;

/**
 * 通知消息service
 * @author EasonLian
 *
 */
@Service()
public class NoticeAnnouncementService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private LogDBService logService;

	@Autowired
	private NoticeAnnouncementDAO noticeDAO;
	
	@Autowired
	private UserDAO userDAO;
	
//	@Autowired
//	private ReUserRoleDAO rurDAO;
//	
//	@Autowired
//	private CourseDAO courseDAO;
//	
//	@Autowired
//	private DepartmentDao departmentDAO;
	
	/**
	 * 通过用户角色查询消息
	 * @param user
	 * @param noanId
	 * @param pages 待查询的页码
	 * @param rows 每页显示行数
	 * @author easonlian
	 */
	@Transactional
	public Map<String,Object> viewNoticeAnnouncementList(
													String pages,
													Integer rows,
													Date startTime,
													Date endTime,
													String keyword,
													User user,
													Integer noanId) {
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<PageData> pageDataList = new ArrayList<PageData>();
		try {
			String[] pageNums = pages.split(",");
			int start = (Integer.valueOf(pageNums[0]) - 1) * rows;
			int totalRows = start + rows * pageNums.length;
			//	查询该用户的类别
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("userType1", user.getUserType());
			params.put("userId",user.getUserId());
			params.put("userDepaId", user.getUserDepaId());
			params.put("userDepaType", user.getUserDepartType());
			params.put("startTime", startTime);
			params.put("endTime", endTime); 
			params.put("keyword", keyword == null ? null :(keyword.equals("") ? null:keyword));
			params.put("noanId", noanId);
			params.put("start", start);
			params.put("rows", totalRows);
			List<NoticeAnnouncementModel> allList = new ArrayList<NoticeAnnouncementModel>();
//			switch(user.getUserType()) {
//			case 4 :		//	管理员，暂无处理
//				break;
//			case 3 : 		//	教务管理员，暂无处理
//				break;
//			case 2 :		//	教师，查询他所有的部门
//				UserModel um = userDAO.viewOneUserByUserId(user.getUserId());
//				if(um != null) {
//					params.put("userDepaId", um.getUserDepaId());
//					params.put("userDepaParentId", um.getUserDepaParentId());
//				}
//				break;
//			case 1 :
//				List<Integer> courIds = courseDAO.viewAllCourIdByStudentId(userId);
//				if(!courIds.isEmpty()) {
//					String courId = "";
//					for(Integer cid : courIds)
//						courId += (cid+",");
//					params.put("courIds", courId.substring(0,courId.length()));
//				} 
//				break;
//			default :
//				break;
//			}
			allList = noticeDAO.viewNoticeAnnouncementList(params);
//			int totalSize = noticeDAO.getNoticeAnnouncementListCount(user.getUserType(), noanId);
			int totalSize = allList.size();
			//	通过页码装配数据
			for(String pageNum : pageNums) {
				int pageNUM = Integer.valueOf(pageNum);
				int pStart = (pageNUM - 1) * rows;
				if(pStart > allList.size())
					break;
				int pEnd = pStart + rows;
				if(pEnd > allList.size())
					pEnd = allList.size();
				List<NoticeAnnouncementModel> subList = allList.subList(start, pEnd);
				Iterator<NoticeAnnouncementModel> iter = subList.iterator();
				while(iter.hasNext()) {
					NoticeAnnouncementModel eachModel = iter.next();
					if(eachModel.getNoanCreatorid().intValue() == user.getUserId().intValue()
							|| user.getUserType() == 4)
						eachModel.setNoanCreatorid(1);
					else
						eachModel.setNoanCreatorid(0);
				}
				PageData pageData = new PageData(pageNUM,subList);
				pageDataList.add(pageData);
			}
			dataMap.put("pageData", pageDataList);
			dataMap.put("totalCount", totalSize);
			dataMap.put("totalPage", (totalSize%rows == 0)?totalSize/rows:(totalSize/rows+1));
		} catch(Exception e) {
			logger.error("viewNoticeAnnouncementList error!!!");
e.printStackTrace();
		}
		return dataMap;
	}

	/**
	 * 添加 修改通知消息
	 * @param notice
	 * @param userId
	 * @author easonlian
	 */
	@Transactional
	public int addOrModNoticeAnnouncement(HttpServletRequest request,User user,
			NoticeAnnouncement notice,String roleIds,String courIds,String deparIds) {
		try {
			if(user != null) {
				if(notice.getNoanId() != null) {
					//	当noanId != null时，为修改消息
					notice.setNoanRoleids(roleIds);
					notice.setNoanCreatorid(user.getUserId());
					return noticeDAO.modNoticeAnnouncement(notice);
				}
				Integer userType = user.getUserType();
				userType = user.getUserType();
				notice.setNoanCreatorid(user.getUserId());
				notice.setNoanRoleids(roleIds+userType+",");
				noticeDAO.addNoticeAnnouncement(notice);
				String logTarget = user.getUserType() == 3 ? 
						"教务管理员添加公告" : user.getUserType() == 4 ? "管理员添加通知公告" : "-";
				logService.insertNewLog(
						request, LogDBService.ADD_OPERATION, "通知公告", logTarget);
//				if(userType == 4) {							//	管理员发送消息
//					String[] rids = roleIds.split(",");
//					NoticeAnnouncement[] notices = new NoticeAnnouncement[rids.length+1];
//					for(int i=0;i<rids.length;i++) {
//						if(!rids[i].equals("")) {
//							Integer ridInt = Integer.valueOf(rids[i]);
//							notice.setNoanRoleids(ridInt);
//							notices[i] = new NoticeAnnouncement(notice);
//						}
//					}
//					notice.setNoanRoleids(4);
//					notices[rids.length] = new NoticeAnnouncement(notice);
//					noticeDAO.addNoticeAnnouncement(notices);
//				} else if(userType == 3) {					//	教务管理员发送消息
//					switch(user.getUserDepartType()) {
//					case 1 :			//	市级教务员
//					case 2 :			//	区级教务员
//						List<Department> depaIdList = departmentDAO.
//									viewAllDepaListBelongsToParentDepar(
//											user.getUserDepaId(), user.getUserDepartType(),null);
//						NoticeAnnouncement[] notices2 = new NoticeAnnouncement[depaIdList.size()];
//						String[] rids2 = roleIds.split(",");
//						for(int i=0;i<depaIdList.size();i++) {
//							if(!rids2[i].equals("")) {
//								Integer ridInt = Integer.valueOf(rids2[i]);
//								notice.setNoanRoleids(ridInt);
//								notice.setNoanDeparid(depaIdList.get(i).getDepaId());
//								notices2[i] = new NoticeAnnouncement(notice);
//							}
//						}
//						noticeDAO.addNoticeAnnouncement(notices2);
//						break;
//					case 3 :			//	校级教务员
//						String[] rids3 = roleIds.split(",");
//						NoticeAnnouncement[] notices3 = new NoticeAnnouncement[rids3.length];
//						for(int i=0;i<rids3.length;i++) {
//							if(!rids3[i].equals("")) {
//								Integer ridInt = Integer.valueOf(rids3[i]);
//								notice.setNoanDeparid(null);;
//								notice.setNoanRoleids(ridInt);
//								notices3[i] = new NoticeAnnouncement(notice);
//							}
//						}
//						noticeDAO.addNoticeAnnouncement(notices3);
//						break;
//					default :
//						throw new Exception("添加公告：userDeparType不合法！");
//					}
//				} else if(userType == 2) {					//	教师给自己课程的学生发送消息
//					/*if(courIds == null
//							|| courIds.equals("")) {
//						//	查询教师所有的课程
//						String myCourIds = ",";
//						List<CourseModel> courList = 
//								courseDAO.viewAllCourseByTeacher(user.getUserId());
//						for(CourseModel cm : courList)
//							myCourIds += (cm.getCourId() + ",");
//						String[] cids = courIds.split(",");
//						for(String cid : cids) {
//							Integer cidInt = Integer.valueOf(cid);
//							if(myCourIds.contains(","+cidInt+",")) {
//								notice.setNoanCourid(","+cidInt+",");
//								noticeDAO.addNoticeAnnouncement(new NoticeAnnouncement[]{notice});
//							}
//						}
//					}*/
//				}
			}
			return 1;
		} catch(Exception e) {
			logger.error("addOrModNoticeAnnouncement error!!!\n"+e.getLocalizedMessage());
			return 0;
		}
	}
	
	/**
	 * 根据id查询单条通知公告
	 * @param noanId
	 * @author easonlian
	 */
	@Transactional
	public NoticeAnnouncement viewOneNoticeAnnouncement(Integer noanId) {
		try {
			return noticeDAO.viewOneNoticeAnnouncement(noanId);
		} catch(Exception e) {
e.printStackTrace();
			return null;
		}
	}

	/**
	 * 批量添加消息
	 * @param notice
	 * @return
	 * @throws Exception
	 * @author easonlian
	 */
	@Transactional
	public int delNoticeAnnouncement(String ids,Integer userId) {
		try {
			if(ids.endsWith(","))
				ids = ids.substring(0,ids.length()-1);
			return noticeDAO.delNoticeAnnouncement(ids)>0 ? 1:0;
		} catch(Exception e) {
			return 0;
		}
	}
}
