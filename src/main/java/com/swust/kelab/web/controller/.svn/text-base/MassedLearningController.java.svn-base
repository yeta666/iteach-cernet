package com.swust.kelab.web.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.swust.kelab.model.CommonQuery;
import com.swust.kelab.model.MassedLearningModel;
import com.swust.kelab.model.QueryData;
import com.swust.kelab.service.LogDBService;
import com.swust.kelab.service.MassedLearningService;
import com.swust.kelab.web.json.JsonAndView;

@Controller
@RequestMapping("/massedLearning")
public class MassedLearningController {

	/**
	 * 失败
	 */
	final static private Integer FAILED = 0;

	private LogDBService logDBService;

	private MassedLearningService massedLearningService;

	@Autowired
	public void setLogDBService(LogDBService logDBService) {
		this.logDBService = logDBService;
	}

	@Autowired
	public void setMassedLearningService(
			MassedLearningService massedLearningService) {
		this.massedLearningService = massedLearningService;
	}

	/**
	 * 添加新的集中学习记录
	 * 
	 * @param ml
	 * @return
	 * @author lujoCom
	 */
	@RequestMapping(value = "/addNewMassedLearning", method = RequestMethod.POST)
	public JsonAndView createNewMassedLearning(MassedLearningModel mlModel,
			HttpServletRequest request) {
		JsonAndView jv = new JsonAndView();
		logDBService.insertNewLog(request, LogDBService.ADD_OPERATION, "集中学习",
				"集中学习记录");
		jv.addAllData(massedLearningService.addMassedLearningInfo(mlModel));
		return jv;
	}

	/**
	 * 给学生添加集中选课记录
	 * 
	 * @param addStuIds
	 * @param smleMaleId
	 * @return
	 * @author lujoCom
	 */
	@RequestMapping(value = "/addStudentToOneMassedLearning", method = RequestMethod.POST)
	public JsonAndView addStudentToOneMassedLearning(String addStuIds,
			String deleStudent, Integer smleMaleId, HttpServletRequest request) {
		JsonAndView jv = new JsonAndView();
		logDBService.insertNewLog(request, LogDBService.ADD_OPERATION, "集中学习",
				"参加集中学习的学生");
		if (massedLearningService.deleteStudentFromMassedLearning(deleStudent,
				smleMaleId) == FAILED) {
			jv.setRet(false);
			jv.setErrmsg("添加学生失败，发生未知错误..");
			return jv;
		}

		jv.addAllData(massedLearningService.addStudentToMassedLearning(
				addStuIds, smleMaleId));
		return jv;
	}

	/**
	 * 筛选集中学习信息
	 * 
	 * @param query
	 * @param mlModel
	 * @return
	 */
	@RequestMapping(value = "/selectMassedLearningInfo", method = RequestMethod.POST)
	public JsonAndView selectMassedLearningInfo(CommonQuery query,
			MassedLearningModel mlModel, HttpServletRequest request) {
		JsonAndView jv = new JsonAndView();
		logDBService.insertNewLog(request, LogDBService.SELECT_OPERATION,
				"集中学习", "集中学习列表");
		QueryData qd = null;
		try {
			qd = massedLearningService.queryMassedLearningInfo(query, mlModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		jv.addData("totalPage", qd.getTotalPage());
		jv.addData("totalCount", qd.getTotalCount());
		jv.addData("pageData", qd.getPageData());

		return jv;
	}

	/**
	 * 开始集中学习，并记录集中学习的时间
	 * 
	 * @param maleId
	 * @param studyTime
	 * @param courseId
	 * @return
	 */
	@RequestMapping(value = "/startMassedLearning", method = RequestMethod.POST)
	public JsonAndView startMassedLearning(
			@RequestParam("maleId") Integer maleId,
			@RequestParam("studyTime") Integer studyTime,
			@RequestParam("courseId") Integer courseId,
			HttpServletRequest request) {
		JsonAndView jv = new JsonAndView();
		Map<String, Object> data = null;
		logDBService.insertNewLog(request, LogDBService.UPDATA_OPERATION,
				"集中学习", "开始集中学习");
		try {
			data = massedLearningService.startMassedLearning(maleId, studyTime,
					courseId);
		} catch (Exception e) {
			jv.setRet(false);
			jv.setErrmsg("未知错误，请稍后再试");
			return jv;
		}
		jv.setRet(true);
		jv.addAllData(data);
		return jv;
	}

	/**
	 * 批量删除集中学习
	 * 
	 * @author ZhangXin
	 * @param mlIds
	 * @return
	 */
	@RequestMapping(value = "/deleteMassedLearning", method = RequestMethod.POST)
	public JsonAndView deleteMassedLearning(
			@RequestParam("maleIds") String maleIds, HttpServletRequest request) {
		JsonAndView jv = new JsonAndView();
		Integer status;
		logDBService.insertNewLog(request, LogDBService.UPDATA_OPERATION,
				"集中学习", "删除集中学习");
		try {
			if (maleIds == "")
				status = 0;
			else {
				String[] males = maleIds.split(",");
				List<Integer> li = new LinkedList<Integer>();
				for (String e : males) {
					li.add(Integer.valueOf(e));
				}
				
				status = massedLearningService.deleteMl(li,maleIds);
			}
		} catch (Exception e) {
			e.printStackTrace();
			jv.setRet(false);
			jv.setErrmsg("未知错误，请稍后再试");
			return jv;
		}
		jv.setRet(true);
		jv.addData("status", status);
		return jv;
	}

}
