package com.swust.kelab.web.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.swust.kelab.domain.BbsPost;
import com.swust.kelab.model.CommonQuery;
import com.swust.kelab.model.QueryData;
import com.swust.kelab.service.BbsPostService;
import com.swust.kelab.service.LogDBService;
import com.swust.kelab.service.SelectCourseService;
import com.swust.kelab.service.SystemParameterService;
import com.swust.kelab.web.json.JsonAndView;

/**
 * 处理bbs主贴的controller
 * 
 * @author 吴岘辉
 * 
 */
@Controller
@RequestMapping("/bbsPost")
public class BbsPostController {
    final Logger logger = LoggerFactory.getLogger(getClass());
    
	@Autowired
	private BbsPostService bbsPostService;

	@Autowired
	private SystemParameterService systemParameterService;
	
	@Autowired
	private LogDBService logDBService;
	
	@Autowired
	private SelectCourseService selectCourseService;
	
	/**
	 * 查看主贴列表
	 * 
	 * @param query
	 *            查询对象，主要是分页
	 * @param departId
	 *            查询的部门id,只针对教务员， 即通过这个字段是否有有效值，就可判断， 用户是普通的教师、学生，还是教务员
	 * @param userId
	 *            用户id
	 * @param courseId
	 *            课程id
	 * @param queryType
	 *            查询类型(对论坛管理无效)，1表示所有，2表示我的讨论，3表示我的回复
	 * @return 主贴列表
	 * @see JsonAndView,QueryData
	 */
	@RequestMapping(value = "/viewBbsPostList", method = RequestMethod.POST)
	public JsonAndView viewBbsPostList(CommonQuery query, int departId, int userId, int courseId, int queryType) {
		JsonAndView jav = new JsonAndView();
		QueryData qd = bbsPostService.viewBbsPostList(query, departId, userId, courseId, queryType);
		if (qd == null) {
			jav.setRet(false);
			jav.setErrcode(1);
			jav.setErrmsg("查询主贴列表失败！");
		} else {
			jav.addData("totalPage", qd.getTotalPage());
			jav.addData("totalCount", qd.getTotalCount());
			jav.addData("pageData", qd.getPageData());
		}
		return jav;
	}

	/**
	 * 访问主贴详细信息
	 * 
	 * @param bbsPostId
	 * @return 详细信息
	 * @see JsonAndView
	 */
	@RequestMapping(value = "/viewBbsPostDetail", method = RequestMethod.POST)
	public JsonAndView viewBbsPostDetail(int bbsPostId) {
		JsonAndView jav = new JsonAndView();
		Map post = bbsPostService.viewBbsPostDetail(bbsPostId);
		if (post != null) {

			jav.addAllData(post);
		} else {
			jav.setRet(false);
			jav.setErrcode(1);
			jav.setErrmsg("查询主贴详细信息失败！");
		}
		return jav;
	}

	/**
	 * 更新主贴访问数
	 * 
	 * @return 更新是否成功的标识
	 * @see JsonAndView
	 */
	@RequestMapping(value = "/addVisitNum", method = RequestMethod.POST)
	public JsonAndView updateBbsPostVisitNum(int bbsPostId) {
		JsonAndView jav = new JsonAndView();
		String result = bbsPostService.updateBbsPost(bbsPostId, 1);
		if (result != "success") {
			jav.setRet(false);
			jav.setErrcode(1);
			jav.setErrmsg("更新帖子的访问数失败！");
		}
		return jav;
	}

	/**
	 * 创建一个主贴
	 * 
	 * @param bbsPost
	 *            主贴参数
	 * @param attaches
	 *            所有附件id，多个用逗号隔开
	 * @param request  
	 *            请求对象，用于日志操作
	 * @return 操作结果
	 * @see JsonAndView
	 */
	@RequestMapping(value = "/createBbsPost", method = RequestMethod.POST)
	public JsonAndView createBbsPost(BbsPost bbsPost
	        , String attaches,HttpServletRequest request) {
		JsonAndView jav = new JsonAndView();
		//发帖检测
		String checkResult=systemParameterService.bbsPostReplyCheck(
                bbsPost.getBbpoUserId(), bbsPost.getBbpoTitle(), 
                bbsPost.getBbpoContent());
		if(checkResult==null||!checkResult.equals("success")){
		    jav.setRet(false);
		    jav.setErrcode(2);
		    jav.setErrmsg("发帖失败！"+checkResult);
		    return jav;
		}
		//存入数据库
		String result = bbsPostService.createBbsPost(bbsPost, attaches);
		if (result != "success") {
			jav.setRet(false);
			jav.setErrcode(1);
			jav.setErrmsg("新增帖子失败！");
		}else{
		    //日志记录
		    logDBService.insertNewLog(request, 
		            LogDBService.ADD_OPERATION, "发起讨论",
		            "发新帖："+bbsPost.getBbpoTitle());
		    //更新总成绩
		    try {
                selectCourseService.updateCourseTotalScore(
                        bbsPost.getBbpoUserId(),
                        bbsPost.getBbpoCourId());
            } catch(Exception e) {
                logger.error("updateCourseTotalScore error!\n"+e.getLocalizedMessage());
            }
		}
		return jav;
	}

	/**
	 * 1表示置顶
	 */
	static final int SET_POST_TOP = 1;
	/**
	 * 2表示置精华
	 */
	static final int SET_POST_BEST = 2;

	/**
	 * 批量置帖子为精华贴或者置顶贴
	 * 
	 * @author Pangjt
	 * 
	 * @param bbsPostIds
	 *            操作的帖子id字符串,多个用逗号隔开
	 * @param setType
	 *            操作类型，1表示置顶 2表示置精华
	 * @param setValue
	 *            设置的值，1表示设置 2表示取消
	 * @param request
	 *            请求对象，用于日志记录
	 * @return 操作状态
	 * @see JsonAndView,QueryData
	 */
	@RequestMapping(value = "/setPostType", method = RequestMethod.POST)
	public JsonAndView setPostType(String bbsPostIds, int setType
	        , int setValue, HttpServletRequest request) {
		JsonAndView jav = new JsonAndView();
		String bbsPost[] = bbsPostIds.split(",");
		int postIds[] = new int[bbsPost.length]; // 帖子Id数组
		String result = null;
		boolean success=true;
		for (int i = 0; i < bbsPost.length; i++) {
			postIds[i] = Integer.valueOf(bbsPost[i]);
			if (setType == SET_POST_TOP) {
				result = bbsPostService.setPostTop(postIds[i], setValue);
				if (!result.equals("success")&&success) {
					success=false;
				}
			}
			if (setType == SET_POST_BEST) {
				result = bbsPostService.setPostEssence(postIds[i], setValue);
				if (!result.equals("success")&&success) {
                    success=false;
                }
			}
		}
		if(!success){
            jav.setRet(false);
            jav.setErrmsg("操作失败！");
        }else{
            //日志记录
            String logInfo="";
            if(setValue==2){
                logInfo+="取消";
            }
            if(setType==1){
                logInfo+="置顶";
            }else{
                logInfo+="置精华";
            }
            logInfo+=",帖子ID包括："+bbsPostIds;
            logDBService.insertNewLog(request, 
                    LogDBService.UPDATA_OPERATION, "我的讨论 或者 课程讨论管理",
                    logInfo);
        }
		return jav;
	}

	/**
	 * 	 批量删除帖子以及帖子的相关信息，如回帖，回帖附件 
	 * @param bbsPostId 被删除的帖子id字符串，用逗号隔开eg:1,2,3
	 * @param request 请求，用于获得附件绝对路径的前缀路径以及日志记录
	 * @return
	 */
	@RequestMapping(value = "/deletePost", method = RequestMethod.POST)
	public JsonAndView deleteOnePost(String bbsPostIds, HttpServletRequest request) {
		String basePath = request.getSession().getServletContext().getRealPath("/");
		JsonAndView jav = new JsonAndView();
		String postId[] = bbsPostIds.split(",");
		String result=null;
		boolean success=true;
		for (int i = 0; i < postId.length; i++) {
			result = bbsPostService.deleteOnePost(Integer.valueOf(postId[i]), basePath);
			if (result.equals("fail")&&success) {
				success=false;
			}
		}
		if(!success){
		    jav.setRet(false);
		    jav.setErrmsg("删除帖子部分或者全部失败！可能帖子已被其他用户删除！");
		}
        //日志记录
        logDBService.insertNewLog(request, 
                LogDBService.DELETE_OPERATION, "我的讨论 或者 课程论坛管理",
                "删帖，ID包括："+bbsPostIds);
        
		return jav;
	}
}
