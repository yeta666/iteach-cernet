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

import com.swust.kelab.domain.Chapter;
import com.swust.kelab.domain.Resource;
import com.swust.kelab.model.ChapterModel;
import com.swust.kelab.model.CourseModel;
import com.swust.kelab.model.PageData;
import com.swust.kelab.recom.RecomDao;
import com.swust.kelab.repos.ChapterDAO;
import com.swust.kelab.repos.CourseDAO;
import com.swust.kelab.repos.CourseCateDAO;
import com.swust.kelab.repos.LearnProcessRecordDAO;
import com.swust.kelab.repos.ReChapterResourceDAO;
import com.swust.kelab.utils.FormatUtil;
import com.swust.kelab.utils.JsonUtil;

/**
 * 跟章节相关的 业务逻辑层<br>
 * 
 * @author EasonLian
 *
 */
@Service()
public class ChapterService {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	private ChapterDAO chapterDAO;
	private CourseDAO courseDAO;
	@Autowired
	private ReChapterResourceDAO rcreDAO;
	@Autowired
	private RecomDao recomDao;
	@Autowired
	private LearnProcessRecordDAO lpreDAO;
	
	   @Autowired
	    private CourseCateDAO courseCateDAO;
	
	@Autowired
	public void setCourseDAO(CourseDAO courseDAO) {
		this.courseDAO = courseDAO;
	}
	@Autowired
	public void setChapterDAO(ChapterDAO chapterDAO) {
		this.chapterDAO = chapterDAO;
	}
	
	/**
	 * 添加和修改章节、以及添加和删除去中的资源关联
	 * @param chapOrdinal
	 * @param chapId
	 * @param courId
	 * @param chapName
	 * @param chapDescribe
	 * @param addResoIds
	 * @param delResoIds
	 * @return
	 */
	@Transactional
	public int addOrModChapterWithResource(String chapOrdinal, Integer chapId, 
			Integer courId, String chapName, String chapDescribe, String addResoIds, String delResoIds) {
		try {
			Chapter chapter = new Chapter();
			chapter.setChapCourId(courId);
			chapter.setChapName(chapName);
			chapter.setChapDescribe(chapDescribe);
			chapter.setChapOrdinal(chapOrdinal);
			Map<String,Object> addResoParams = new HashMap<String,Object>();
			if( chapId == null) {		//	当章节id为空时，为添加
				if( chapterDAO.addChapterByAdmin(chapter) == 1 ) {
					if(!addResoIds.equals("")) {
						String[] resoIds = addResoIds.split(",");
						List<Integer> list1 = new ArrayList<Integer>();
						for(String resoId : resoIds) 
							list1.add(Integer.valueOf(resoId));
						addResoParams.put("resoIdList", list1);
						addResoParams.put("chapId", chapter.getChapId());
						rcreDAO.addReChapterResourceDAO(addResoParams);
					}
					return 1;
				}
			} else {					//	当章节id!=null时，为修改
				chapter.setChapId(chapId);
				//	当修改章节字段成功后，修改章节资源中间表数据
				if( chapterDAO.modChapterByAdmin(chapName, courId, chapDescribe, chapOrdinal, chapId) == 1) {
					String[] addResoIdArray = addResoIds.split(",");
					if(delResoIds.endsWith(","))
						delResoIds = delResoIds.substring(0,delResoIds.length()-1);
					List<Integer> addResoList = new ArrayList<Integer>();
					//	创建添加删除参数对象
					addResoParams.put("chapId", chapId);
					if(addResoIdArray.length != 0
							&& (!addResoIdArray[0].equals(""))) {
						for(String resoId : addResoIdArray) 
							addResoList.add(Integer.valueOf(resoId));
						addResoParams.put("resoIdList", addResoList);
						rcreDAO.addReChapterResourceDAO(addResoParams);
					}
					if(!delResoIds.equals("")) {
						addResoParams.put("resoIds", "(" + delResoIds + ")");
						rcreDAO.delReChapterResourceDAO(addResoParams);
					}
					return 1;
				}
			}
			return 0;
		} catch(Exception e) {
			logger.error("addOrModChapterWithResource error!");
e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * 通过chapId查找单条数据
	 * @param chapId
	 * @return 查到的条目对象
	 */
	@Transactional()
	public Chapter findOneChapter(Integer chapId) {
		try {
			return chapterDAO.findOneChapter(chapId);
		} catch(Exception e) {
			logger.error("findOneChapter Error!!!");
			return null;
		}
	}
	
	/**
	 * 修改章节信息
	 * @param chapName
	 * @param courId
	 * @param chapDescribe
	 * @param chapOrdinal
	 * @return 是否成功
	 */
	@Transactional()
	public boolean modChpaterByAdmin(String chapName, Integer courId,
			String chapDescribe, String chapOrdinal,Integer chapId) {
		try {
			return chapterDAO.modChapterByAdmin(
					chapName, courId, chapDescribe, chapOrdinal, chapId) ==1 ? true :false;
		} catch(Exception e) {
			logger.error("modChapterByAdmin ErroR!!!");
			return false;
		}
	}
	
	/**
	 * 添加章节资源
	 * @param chapName
	 * @param chapDescribe
	 * @param chapOrdinal
	 * @return 添加的行数
	 * @author EasonLian
	 */
	@Transactional()
	public boolean addChpaterByAdmin(String chapName,
			Integer courId,String chapDescribe,String chapOrdinal) {
//		try {
//			return chapterDAO.addChapterByAdmin(
//					chapName, courId, chapDescribe, chapOrdinal) == 1 ? true :false;
//		} catch(Exception e) {
//e.printStackTrace();
//			logger.error("AddChapterByAdmin ErroR!!!");
//			return false;
//		}
		return true;
	}
	
	/**
	 * 删除章节，以及章节资源中间表<br>
	 * @param ids
	 * @return 被删除的条数
	 * @author EasonLian
	 */
	@Transactional()
	public boolean delChapterByAdmin(String ids) {
		try {
			if(ids.endsWith(","))
				ids = ids.substring(0, ids.length()-1);
			return chapterDAO.delChapterByAdmin(ids) >= 0 ? true : false;
		} catch(Exception e) {
			logger.error("delChapterByAdmin error!");
			return false;
		}
	}
	
	/**
	 * 管理员章节管理页面<br>
	 * 查询所有章节及他所属的资源文件<br>
	 * @param courId
	 * @return List<Chapter>
	 */
	@Transactional()
	public Map<String,Object> viewAllChapterList(
			Integer courId,
			String chapName,
			String pages,
			Integer userType,
			Integer userId,
			int rows) {
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			//	分解页面页码
			String[] page = pages.split(",");
			//	设置查询参数,执行查询
			Map<String,Object> params = new HashMap<String, Object>();
			params.put("courId", courId);
 			params.put("chapName", chapName);
 			params.put("userType", userType);
 			params.put("userId", "," + userId + ",");
 			//System.out.println(JsonUtil.getJSON(params));
 			//	如果是教师登录只能查看教师的所有课程
// 			if(userType == 2) {
// 				List<CourseModel> courList = courseDAO.viewAllCourseByTeacher(userId);
// 				String myCourIds = "";
// 				for(CourseModel cm : courList)
// 					myCourIds += (cm.getCourId() + ",");
// 				params.put("myCourIds",myCourIds.substring(0,myCourIds.length()-1));
// 			}
			List<ChapterModel> allList = chapterDAO.viewAllChapterList(params);
			//	分页包装数据
			int totalSize = allList.size();
			List<PageData> pageDataList = new ArrayList<PageData>();
			for(int i=0;i<page.length;i++) {
				int pageNum = Integer.valueOf(page[i]);
				int start = (pageNum-1) * rows;
				int end = start + rows;
				if(end > totalSize)
					end = totalSize;
				if(start > totalSize)
					break;
				List<ChapterModel> subList = allList.subList(start, end);
				PageData pageData = new PageData(pageNum,subList);
				pageDataList.add(pageData);
			}
			map.put("pageData", pageDataList);
			map.put("totalCount", totalSize);
			map.put("totalPage", (totalSize%rows == 0)?totalSize/rows:(totalSize/rows+1));
			return map;
		} catch(Exception e) {
			logger.error("viewAllChapterList error!");
			return null;
		}
	}
	
	/**
	 * @param courId
	 * @param userId
	 * @return 课程下的所有章节，以及课程相关信息
	 */
	@Transactional()
	public Map<String,Object> viewChapterListByCourse(
				String projectPath,Integer courId,Integer userId) {
		Map<String, Object> data = new HashMap<String,Object>();
		try {
			List<ChapterModel> cmList = chapterDAO.viewChapterListByCourse(courId);
			Iterator<ChapterModel> it = cmList.iterator();
			while(it.hasNext()) {
				ChapterModel cm = it.next();
				List<Resource> resoList = cm.getResources();
				Iterator<Resource> resoIt = resoList.iterator();
				while(resoIt.hasNext()) {
					Resource reso = resoIt.next();
					if(reso.getResoType() == 3) {
						int recordCount = 
								lpreDAO.isUserViewThisChapterVideoReso(
										userId, cm.getChapId(), reso.getResoId());
						reso.setResoStatus(recordCount > 0 ? 1:0);
					}
				}
			}
			data.put("chapterList", cmList);
			CourseModel courseModel = courseDAO.selectOneByCourId(courId,userId);
			//规范返回的课程model
			//转换课程类别
            String courType=courseModel.getCourType();
            if(courType.equals("B")){
                courseModel.setCourType("必修");
            }else
            if(courType.equals("XIA")){
                courseModel.setCourType("选修IA");
            }else
            if(courType.equals("XIB")){
                courseModel.setCourType("选修IB");
            }else
            if(courType.equals("XII")){
                courseModel.setCourType("选修II");
            }
			Date openDate=courseModel.getOpenDate();
			Date closeDate=courseModel.getCloseDate();
			if(openDate!=null){
			    courseModel.setOpentTime(FormatUtil
			            .formatDate(openDate, "yyyy-MM-dd"));
			}else{
			    courseModel.setOpentTime("#"); 
			}
			if(closeDate!=null){
			    courseModel.setCloseTime(FormatUtil
			            .formatDate(closeDate, "yyyy-MM-dd"));
			}else{
			    courseModel.setCloseTime("#"); 
			}
			String pattern=courseModel.getCourTepaPattern();
			String threhold=courseModel.getCourTepaThrehold();
			if(pattern==null||pattern.isEmpty()){
			    courseModel.setCourTepaPattern("#");
			}
			if(threhold==null||threhold.isEmpty()){
			    courseModel.setCourTepaThrehold("#");
			}
			//增加了选课人数、主讲教师、课程类型等信息  modify by MrYang
			//判断该课程是否被选
			int isChooseCourse = courseDAO.isChooseCourse(courId, userId);
			if(isChooseCourse >=1)
			    isChooseCourse = 1;
			else
			    isChooseCourse = 0;
			courseModel.setCourState(isChooseCourse);
			
             //  查询选课人数
			courseModel.setCourseStuCount(courseDAO.getCourseChoosedNum(courId));
             //  查询课程类型
             String courseCateIds = courseModel.getCourCateIds();
             courseCateIds = removeSuffixAndPreffix(courseCateIds);
             String courCateName = "";
             if(courseCateIds!=null&&!courseCateIds.isEmpty()){
                 List<String> courCateList = courseCateDAO.viewCocaNameByIds(courseCateIds);
                 for(String cateName:courCateList)
                     courCateName += (cateName+",");
                 if(!courCateName.equals(""))
                     courCateName = courCateName.substring(0,courCateName.length()-1);
             }
             courseModel.setCourCateIds(courCateName);
             //  查询课程主讲教师
             String courseTeachers = courseModel.getCourTeacherIds();
             courseTeachers = removeSuffixAndPreffix(courseTeachers);
             String teacherNames = "";
             if(courseTeachers!=null&&!courseTeachers.isEmpty()){
                 List<String> courTeaList = courseDAO.viewAllCourTeacherNameByCourse(courseTeachers);
                 for(String teachName: courTeaList)
                     teacherNames += (teachName+"，");
                 if(teacherNames!="")
                     teacherNames = teacherNames.substring(0,teacherNames.length()-1);
             }else{
                 teacherNames="#";
                 courseModel.setCourTeacherIds("#");
             }
             courseModel.setCourTeachers(teacherNames);
			if(!new File(projectPath+courseModel.getCourImg()+courseModel.getFileName()).exists()) {
				courseModel.setCourImg("upload/eduman/coursepic.jpg");
				courseModel.setFileName("");
			}
			data.put("course", courseModel);
			return data;
		} catch(Exception e) {
			logger.error("viewChapterListByCourse error!\n"+e.getLocalizedMessage());
			return null;
		}
	}
	
	/**
	 * 去除参数前或后多余的逗号
	 * @param args
	 * @author easonlian
	 */
	private static String removeSuffixAndPreffix(String args) {
		if (args == null)
			return null;
		if (args.startsWith(","))
			args = args.substring(1, args.length());
		if (args.endsWith(","))
			args = args.substring(0, args.length() - 1);
		return args;
	}
	
	/**
     * 评价一个课程的章节（主要用于课程的排序）
     * @param chapterId 章节id   
     * @param score     评分
	 * @param score2 
     * @return 操作是否成功的标识
     */
	public boolean assessChapter(int userId, int chapterId,double score){
	    boolean result=true;
	    if(chapterId<=0||score<=0){
	        return false;
	    }
	    //获取章节
	    Chapter chapter=null;
	    try {
	        chapter=chapterDAO.findOneChapter(chapterId);
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("findOneChapter error!\n"+e.getLocalizedMessage());
            return false;
        }
	    if(chapter==null||chapter.getChapId()<=0){
	        return false;
	    }
	    int courseId = chapter.getChapCourId();
	    //保存评分
	    Map<String,Object> map=new HashMap<String, Object>();
	    map.put("chapterId", chapterId);
	    map.put("score", score);
	    map.put("courseId", chapter.getChapCourId());
	    //更新课程评分(必须先更新课程，否则会重复计算导致错误)
        try {
            int change=courseDAO.assessCourse(map);
            if(change<=0){
                return false;
            }
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("assessCourse error!\n"+e.getLocalizedMessage());
            return false;
        }
        recomDao.updateUserRate(userId,courseId,score);
	    return result;
	}
}
