package com.swust.kelab.web.controller;

import com.swust.kelab.adaptive_ui.UserAnswersService;
import com.swust.kelab.web.json.JsonAndView;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 与用户答案请求有关的的控制器类
 * Created by yeta on 2017/5/14/014.
 */
@Controller
@RequestMapping(value = "/userAnswers")
public class UserAnswersController {

    @Autowired
    private UserAnswersService userAnswersService;

    /**
     * 获取所有用户答案
     * @return
     */
    @RequestMapping(value = "/getAll")
    @ResponseBody
    public JsonAndView getAll(HttpServletRequest request){
        JsonAndView jv = new JsonAndView();
		jv.addData("result", userAnswersService.getAll(request));
		return jv;
    }
    
    /**
     * 删除一个或多个用户答案
     * @param ids
     * @param request
     * @return
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public JsonAndView delete(@RequestParam(value = "ids", required = true) String ids,
    		HttpServletRequest request){
    	JsonAndView jv = new JsonAndView();
		jv.addData("result", userAnswersService.delete(ids, request));
		return jv;
    }

}
