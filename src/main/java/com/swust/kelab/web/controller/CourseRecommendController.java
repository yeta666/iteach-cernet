package com.swust.kelab.web.controller;

import com.swust.kelab.course_recommend.CourseRecommendService;
import com.swust.kelab.web.json.JsonAndView;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 与课程推荐请求有关的的控制器类
 * Created by yeta on 2017/5/14/014.
 */
@Controller
@RequestMapping(value = "/recom")
public class CourseRecommendController {

    @Autowired
    private CourseRecommendService courseRecommendService;

    /**
     * 获取推荐课程
     * @return
     */
    @RequestMapping(value = "/courseRecommend")
    @ResponseBody
    public JsonAndView getCourseRecommend(@RequestParam(value = "userId") String userId, HttpServletRequest request){
        JsonAndView jv = new JsonAndView();
		jv.addData("result", courseRecommendService.getCourseRecommend(userId, request));
		return jv;
    }

}
