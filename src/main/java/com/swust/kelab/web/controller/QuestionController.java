package com.swust.kelab.web.controller;

import com.swust.kelab.adaptive_ui.QuestionService;
import com.swust.kelab.httpClient.Result;
import com.swust.kelab.web.json.JsonAndView;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 获取调查表题目请求的控制器类
 * Created by yeta on 2017/5/14/014.
 */
@Controller
@RequestMapping(value = "/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    /**
     * 获取所有调查表题目
     * @return
     */
    @RequestMapping(value = "/getAll")
    @ResponseBody
    public JsonAndView getAll(HttpServletRequest request){
        JsonAndView jv = new JsonAndView();
		jv.addData("result", questionService.getAll(request));
		return jv;
    }

}
