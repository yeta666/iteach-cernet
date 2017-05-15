package com.swust.kelab.web.controller;

import com.swust.kelab.adaptive_ui.UserTypeService;
import com.swust.kelab.httpClient.Result;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 接收前端页面请求用户类型的控制器
 * Created by yeta on 2017/5/14/014.
 */
@Controller
@RequestMapping(value = "/userType")
public class UserTypeController {

    @Autowired
    private UserTypeService userTypeService;

    /**
     * 获取用户类型的方法
     * @param userId
     * @param answers
     * @return
     */
    @RequestMapping(value = "/getUserType", method = RequestMethod.POST)
    @ResponseBody
    public Result getUserType(@RequestParam(value = "userId", required = true) String userId,
                              @RequestParam(value = "answers", required = false) String answers,
                              HttpServletRequest request){
        return userTypeService.getUserType(userId, answers, request);
    }
}
