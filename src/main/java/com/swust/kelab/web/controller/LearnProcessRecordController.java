package com.swust.kelab.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.swust.kelab.domain.User;
import com.swust.kelab.service.LearnProcessRecordService;
import com.swust.kelab.utils.CookieUtil;
import com.swust.kelab.web.json.JsonAndView;

/**
 * 
 * 包含学习记录方法<br>
 * 
 * @author EasonLian
 *
 */
@Controller()
@RequestMapping("/record")
public class LearnProcessRecordController {
	
	private LearnProcessRecordService learnProcessRecordService;
	
	@Autowired
	public void setLearnProcessRecordController(LearnProcessRecordService learnProcessRecordService) {
		this.learnProcessRecordService = learnProcessRecordService;
	}
	
	/**
	 * 添加学生章节视频学习时间记录<br>
	 * 访问路径：/handler/record/addLearnProcessRecord.do
	 * @param chapId 章节id
	 * @return JsonAndView
	 */
	@RequestMapping(value="/addLearnProcessRecord.do",method=RequestMethod.GET)
	public JsonAndView addNewLearnProcessRecord(
			@RequestParam(value="chapId") int chapId,
			@RequestParam(value="resoId") int resoId,
			HttpServletRequest request) {
		JsonAndView jav = new JsonAndView();
		User user = CookieUtil.getCookieUser(request);
		if(user.getUserType() != 1)
			return jav.addData("status",0);
		return jav.addData("status",
				learnProcessRecordService.insertLearnProcessRecord(user.getUserId(), chapId,resoId));
	}

	/**
	 * 记录学生章节学习时间<br>
	 * 访问路径: /handler/record/recordLearnProcess.do<br>
	 * @param chapId 章节id
	 * @param studyTime 学习时间
	 * @return JsonAndView
	 */
	@RequestMapping(value="/recordLearnProcess.do",method=RequestMethod.GET)
	public JsonAndView recordLearnProcess(
			HttpServletRequest request,
			@RequestParam(value="lpreId",required=false) Integer lpreId,
			@RequestParam(value="chapId") int chapId,
			@RequestParam(value="studyTime") int studyTime,
			@RequestParam(value="resoId") Integer resoId) {
		JsonAndView jav = new JsonAndView();
		User user = CookieUtil.getCookieUser(request);
		int status = learnProcessRecordService.
				recordLearnProcess(lpreId, user.getUserId(), chapId, studyTime,resoId);
		jav.addData("status",status);
		return jav;
	}
}
