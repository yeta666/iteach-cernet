package com.swust.kelab.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.swust.kelab.domain.EvaluateMethod;
import com.swust.kelab.service.EvaluateMethodService;
import com.swust.kelab.service.LogDBService;
import com.swust.kelab.web.json.JsonAndView;


/**
 * 
 * 课程评估方Controller
 * @author EasonLian
 *
 */
@Controller()
@RequestMapping("/evaluateMethod")
public class EvaluateMethodController {
	
	@Autowired
	private EvaluateMethodService emService;
	
	@Autowired
	private LogDBService logService;
	
	/**
     * 查询所有的考核方式
     *
     * @return 查询结果
     * @see JsonAndView,EvaluateMethod
     */
    @RequestMapping(value = "/viewAllEvaMethods", method = RequestMethod.POST)
    public JsonAndView viewAllEvaMethods() {
        JsonAndView jav = new JsonAndView();
        List<EvaluateMethod> result=emService
                .viewAllEvaluateMethods();
        if (result == null) {
            jav.setRet(false);
            jav.setErrcode(1);
            jav.setErrmsg("获取考核方式失败！");
        } else {
            jav.addData("evaMethods", result);
        }
        return jav;
    }
	
	/**
	 * 添加评估方法
	 * to visit: /handler/evaluateMethod/addEvaluateMethod.do
	 * @param name
	 * @param pattern
	 * @param threhold
	 * @param describe
	 * @author EasonLian
	 */
	@RequestMapping(value="/addEvaluateMethod.do",method=RequestMethod.POST)
	public JsonAndView addEvaluateMethod(
			HttpServletRequest request,
			@RequestParam(value="id",required=false) Integer id,
			@RequestParam(value="name") String name,
			@RequestParam(value="pattern") String pattern,
			@RequestParam(value="threhold") String threhold,
			@RequestParam(value="describe") String describe) {
		JsonAndView jav = new JsonAndView();
		EvaluateMethod evme = new EvaluateMethod();
		evme.setEvmeId(id);
		evme.setEvmeName(name);
		evme.setEvmePattern(pattern);
		evme.setEvmeThrehold(threhold);
		evme.setEvmeDescribe(describe);
		logService.insertNewLog(request, LogDBService.ADD_OPERATION, "addEvaluateMethod", "-");
		return jav.addData("status", emService.addEvaluateMethod(evme));
	}
	
	/**
	 * 批量删除评估方法
	 * to visit: /handler/evaluateMethod/delEvaluateMethod.do
	 * @return 是否删除成功
	 * @author EasonLian
	 */
	@RequestMapping(value="/delEvaluateMethod.do",method=RequestMethod.POST)
	public JsonAndView delEvaluateMethod(
			HttpServletRequest request,
			@RequestParam("ids") String ids) {
		JsonAndView jav = new JsonAndView();
		logService.insertNewLog(request, LogDBService.ADD_OPERATION, "delEvaluateMethod", "-");
		return  jav.addData("status", emService.delAllEvaluateMethods(ids)? 1:0);
	}

	/**
	 * 查询所有的评估方法
	 * to visit: /handler/evaluateMethod/viewEvaluateMethodList.do
	 * @param pages	查询的页数
	 * @param rows 每页的条数
	 * @param evmeId
	 * @param evmeName
	 * @param evmeDescribe
	 * @return 所有的评估方法数据
	 * @author EasonLian
	 */
	@RequestMapping(value="/viewEvaluateMethodList.do")
	public JsonAndView viewEvaluateMethodList(
			/* 分页参数 */
			@RequestParam(value="pageArray") String pages,
			@RequestParam(value="recordPerPage") int rows,
			/* 模糊查询参数 */
			@RequestParam(value="id",required=false) Integer evmeId,
			@RequestParam(value="name",required=false) String evmeName,
			@RequestParam(value="describe",required=false) String evmeDescribe) {
		JsonAndView jav = new JsonAndView();
		return jav.addAllData(
				emService.viewEvaluateMethodList(
						pages, rows, evmeId, evmeName, evmeDescribe));
	}
}
