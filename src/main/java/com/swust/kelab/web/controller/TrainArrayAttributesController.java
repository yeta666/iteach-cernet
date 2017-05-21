package com.swust.kelab.web.controller;

import com.swust.kelab.adaptive_ui.TrainArrayAttributesService;
import com.swust.kelab.web.json.JsonAndView;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 与训练集属性请求有关的的控制器类
 * Created by yeta on 2017/5/14/014.
 */
@Controller
@RequestMapping(value = "/trainArrayAttributes")
public class TrainArrayAttributesController {

    @Autowired
    private TrainArrayAttributesService trainArrayAttributesService;

    /**
     * 获取所有训练集属性
     * @return
     */
    @RequestMapping(value = "/getAll")
    @ResponseBody
    public JsonAndView getAll(HttpServletRequest request){
        JsonAndView jv = new JsonAndView();
		jv.addData("result", trainArrayAttributesService.getAll(request));
		return jv;
    }
  

}
