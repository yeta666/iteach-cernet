package com.swust.kelab.web.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.swust.kelab.domain.User;
import com.swust.kelab.service.ChapterService;
import com.swust.kelab.utils.CookieUtil;
import com.swust.kelab.web.json.JsonAndView;

/**
 * 
 * 包含所有对 章节的操作<br>
 * 
 * @author EasonLian
 *
 */
@Controller()
@RequestMapping("chapter")
public class ChapterController {
	
	private ChapterService chapterService;
	
	@Autowired
	public void setChapterService(ChapterService chapterService) {
		this.chapterService = chapterService;
	}
	
	/**
	 * 重写的添加和修改方法，同时包含增加包含的资源
	 * to visit : /handler/chapter/addOrModChapterWithResource.do
	 * @author EasonLian
	 */
	@RequestMapping(value="/addOrModChapterWithResource.do",method=RequestMethod.POST)
	public JsonAndView addOrModChapterWithResource(
			@RequestParam(value="chapId",required=false) Integer chapId,
			@RequestParam(value="chapOrdinal") String chapOrdinal,
			@RequestParam(value="chapCourId") Integer courId,
			@RequestParam(value="chapName") String chapName,
			@RequestParam(value="chapDescribe",required=false) String chapDescribe,
			@RequestParam(value="addResoIds",required=false) String addResoIds,
			@RequestParam(value="delResoIds",required=false) String delResoIds) {
		JsonAndView jav = new JsonAndView();
		int isSuccess = chapterService.addOrModChapterWithResource(
				chapOrdinal, chapId, courId, chapName, chapDescribe, addResoIds, delResoIds);
		return jav.addData("status", isSuccess == 1? 1:0);
	}
	
	/**
	 * 通过chapId查找单条数据
	 * @param chapId
	 * @return 查到的条目对象
	 */
	@RequestMapping(value="/findOneChapter.do",method=RequestMethod.GET)
	public JsonAndView findOneChapter(
			@RequestParam(value="chapId") Integer chapId) {
		JsonAndView jav = new JsonAndView();
		jav.addData("chapter", chapterService.findOneChapter(chapId));
		return jav;
	}
	
	/**
	 * 修改章节信息
	 * @param chapName
	 * @param courId
	 * @param chapDescribe
	 * @param chapOrdinal
	 * @return 是否成功
	 */
	@RequestMapping(value="/modChapterByAdmin.do",method=RequestMethod.POST)
	public JsonAndView modChapterByAdmin(
				@RequestParam(value="chapId") Integer chapId,
				@RequestParam(value="chapName") String chapName,
				@RequestParam(value="courid") Integer courId,
				@RequestParam(value="chapDescribe",required=false) String chapDescribe,
				@RequestParam(value="chapOrdinal",required=false) String chapOrdinal
			) {
		JsonAndView jav = new JsonAndView();
		if(chapterService.modChpaterByAdmin(chapName,courId, chapDescribe, chapOrdinal,chapId))
			jav.addData("status", 1);
		return jav;
	}
	
	/**
	 * 添加章节
	 * @param chapName
	 * @param chapDescribe
	 * @param chapOrdinal
	 * @return 是否添加成功
	 * @author EasonLian
	 */
	@RequestMapping(value="/addChapterByAdmin.do",method=RequestMethod.POST)
	public JsonAndView addChapterByAdmin(
				@RequestParam(value="chapName") String chapName,
				@RequestParam(value="courId") Integer courId,
				@RequestParam(value="chapDescribe",required=false) String chapDescribe,
				@RequestParam(value="chapOrdinal",required=false) String chapOrdinal
			) {
		JsonAndView jav = new JsonAndView();
		if(chapterService.addChpaterByAdmin(chapName, courId, chapDescribe, chapOrdinal))
			jav.addData("status", 1);
		return jav;
	}
	
	/**
	 * 删除章节，以及章节资源中间表<br>
	 * @param ids
	 * @return 被删除的条数
	 * @throws Exception
	 */
	@RequestMapping(value="/delChapterByAdmin.do",method=RequestMethod.POST)
	public JsonAndView delChapterByAdmin(
			@RequestParam("ids") String ids) {
		JsonAndView jav = new JsonAndView();
		if(chapterService.delChapterByAdmin(ids))
			jav.addData("status", 1);
		return jav;
	}
	
	/**
	 * 管理员章节管理页面<br>
	 * 查询所有章节及他所属的资源文件<br>
	 * to visit: /handler/chapter/viewAllChapterList.do
	 * @param courId
	 * @return List<Chapter>
	 */
	@RequestMapping(value="/viewAllChapterList.do",method=RequestMethod.POST)
	public JsonAndView viewAllChapterList(
				HttpServletRequest request,
				@RequestParam(value="courId",required=false) Integer courId,
				@RequestParam(value="chapName",required=false) String chapName, 
				@RequestParam(value="pageArray") String pages,
				@RequestParam(value="recordPerPage") int rows) {
		JsonAndView jav = new JsonAndView();
		User user = CookieUtil.getCookieUser(request);
		return jav.addAllData(
				chapterService.viewAllChapterList(
						courId,chapName,pages,user.getUserType(),user.getUserId(),rows));
	}

	/**
	 * 通过课程Id，查询所有所属的章节，以及课程信息.<br>
	 * 访问路径: /handler/chapter/viewChapterListByCourse.do
	 * @param courId
	 * @param userId
	 * @return JsonAndView
	 */
	@RequestMapping(value="/viewChapterListByCourse.do",method=RequestMethod.GET)
	public JsonAndView viewChapterListByCourse(
			HttpServletRequest request,
			@RequestParam(value="courId") int courId) {
		
		JsonAndView jav = new JsonAndView();
		User user = CookieUtil.getCookieUser(request);
		 
		Map<String, Object> data=chapterService.viewChapterListByCourse(
                request.getSession().getServletContext()
                .getRealPath("/"),courId,user.getUserId());
		if(data==null){
		    jav.setRet(false);
		    jav.setErrmsg("获取课程章节信息失败");
		}else{
		    jav.setRet(true);
		    jav.addAllData(data);
		}
		return jav;
	}
	
	/**
	 * 评价一个课程的章节（主要用于课程的排序）
	 * @param chapterId 章节id   
	 * @param score     评分
	 * @return
	 */
	@RequestMapping(value="/assessChapter",method=RequestMethod.POST)
    public JsonAndView assessChapter(HttpServletRequest request,int chapterId,double score) {
        JsonAndView jav = new JsonAndView(); 
        User user = CookieUtil.getCookieUser(request);      
        if(user!=null){
        	int userId = user.getUserId();
        	 if(!chapterService.assessChapter(userId,chapterId, score)){
                 jav.setRet(false);
                 jav.setErrmsg("评价课程章节信息失败");
             }else{
                 jav.setRet(true);
             }
        }else{
        	 jav.setRet(true);
        }
        return jav;
    }
}
