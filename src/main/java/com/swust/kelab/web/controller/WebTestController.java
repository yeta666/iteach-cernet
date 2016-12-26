package com.swust.kelab.web.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.swust.kelab.service.WebTestService;
import com.swust.kelab.web.json.JsonAndView;

@RequestMapping("/webtest")
@Controller
public class WebTestController {
    @Resource
    private WebTestService webTestService;

    @SuppressWarnings("unused")
    private void errorData(JsonAndView jv) {
        jv.setRet(false);
        jv.setErrcode(601);
        jv.setErrmsg("数据格式错误");
    }

    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public JsonAndView login() {
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        jv.addData("test", webTestService.query());
        return jv;

    }
}
