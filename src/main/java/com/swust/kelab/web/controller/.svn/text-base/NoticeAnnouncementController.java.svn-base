package com.swust.kelab.web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.swust.kelab.domain.NoticeAnnouncement;
import com.swust.kelab.domain.User;
import com.swust.kelab.service.NoticeAnnouncementService;
import com.swust.kelab.utils.CookieUtil;
import com.swust.kelab.web.json.JsonAndView;

/**
 * 用户消息相关操作
 * @author EasonLian
 * 
 */
@Controller()
@RequestMapping("/noticeAnnouncement")
public class NoticeAnnouncementController {

	@Autowired
	private NoticeAnnouncementService noticeService;
	
	/**
	 * 批量删除消息
	 * to visit: ../../handler/noticeAnnouncement/delNoticeAnnouncement.do
	 * @param ids
	 * @author easonlian
	 */
	@RequestMapping(value="/delNoticeAnnouncement.do")
	public JsonAndView delNoticeAnnouncement(
			HttpServletRequest request,
			@RequestParam(value="ids") String ids) {
		JsonAndView jav = new JsonAndView();
		User user = CookieUtil.getCookieUser(request);
		return jav.addData("status", 
				noticeService.delNoticeAnnouncement(ids,user==null?null:user.getUserId()));
	}
	
	/**
	 * 添加与修改通知
	 * @param request
	 * @param notice
	 * @return
	 */
	@RequestMapping(value="/addNoticeAnnouncementList.do",method=RequestMethod.POST)
	public JsonAndView addOrModNoticeAnnouncement(
			HttpServletRequest request,
			/*@RequestParam(value="courIds",required=false) String courIds,
			@RequestParam(value="deparIds",required=false) String deparIds, */
			@RequestParam(value="roleIds",required=false) String roleIds,
			NoticeAnnouncement notice) {
		JsonAndView jav = new JsonAndView();
		User user = CookieUtil.getCookieUser(request);
		notice.setNoanCreatorid(user.getUserId());
		if(notice.getNoanTopdays() != null
				&& notice.getNoanTopdays() > 0)
			notice.setNoanIsimport(true);
		return jav.addData("status", 
//				noticeService.addOrModNoticeAnnouncement(user,notice,roleIds,courIds,deparIds));
				noticeService.addOrModNoticeAnnouncement(request,user,notice,roleIds,null,null));
	}
	
	/**
	 * 根据id查询单条通知公告
	 * @param noanId
	 * @author easonlian
	 */
	@RequestMapping(value="/viewOneNoticeAnnouncement.do")
	public JsonAndView viewOneNoticeAnnouncement(@RequestParam(value="noanId")Integer noanId) {
		return new JsonAndView().addData("oneNotice", noticeService.viewOneNoticeAnnouncement(noanId));
	}
	
	/**
	 * 通过用户角色查询消息
	 * to visit: /handler/noticeAnnouncement/viewNoticeAnnouncementList.do
	 * @param httpservletrequest
	 * @author EasonLian
	 */
	@RequestMapping(value="/viewNoticeAnnouncementList.do",method=RequestMethod.POST)
	public JsonAndView viewNoticeAnnouncementList(
			HttpServletRequest request,
			@RequestParam(value="pageArray") String pages,
			@RequestParam(value="recordPerPage") Integer rows,
			@RequestParam(value="startTime",required=false) String startTime,
			@RequestParam(value="endTime",required=false) String endTime,
			@RequestParam(value="keyWord",required=false) String keyword,
			@RequestParam(value="id",required=false) Integer noanId) {
		JsonAndView jav = new JsonAndView();
		User user = CookieUtil.getCookieUser(request);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = null,endDate = null;
		try {
			if(startTime != null && !startTime.equals("")) {
				startDate = sdf.parse(startTime);
				if(endTime != null && !endTime.equals("")) {
					String[] eachDate = endTime.split("-");
					String day = (Integer.valueOf(
							Integer.valueOf(eachDate[2]).intValue()+1)).toString();
					endTime = (eachDate[0] + "-" +
							eachDate[1] + "-" + 
							(day.length() == 1 ? "0"+day : day));
					endDate = sdf.parse(endTime);
				} else 
					endDate = new Date();
			}
			return jav.addAllData(
					noticeService.viewNoticeAnnouncementList(
							pages,rows,startDate,endDate,keyword,user,noanId));
		} catch(ParseException pe) {
			pe.printStackTrace();
		}
		return null;
	}
}
