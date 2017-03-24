package com.swust.kelab.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.swust.kelab.domain.CourseCategory;
import com.swust.kelab.model.CommonQuery;
import com.swust.kelab.model.QueryData;
import com.swust.kelab.service.CourseCategoryService;
import com.swust.kelab.service.LogDBService;
import com.swust.kelab.web.json.JsonAndView;

/**
 * 课程类别的处理器
 * 
 * @author 吴峴辉
 *
 */
@Controller
@RequestMapping("/courseCategory")
public class CourseCategoryController {
    @Autowired
    private CourseCategoryService courseCategoryService;
    
    @Autowired
    private LogDBService logDBService;
    
    /**
     * 根据条件,查询类别列表
     * 
     * @param query     查询对象(分页信息)
     * @return          类别列表
     * @see ModelAndView,CommonQuery,QueryData
     */
    @RequestMapping(value="/viewList",method=RequestMethod.POST)
    public JsonAndView viewCourseCateList(CommonQuery query){
        JsonAndView jav=new JsonAndView();
        QueryData qd=courseCategoryService.viewCourseCateList(query);
        if(qd==null){
            jav.setRet(false);
            jav.setErrcode(1);
            jav.setErrmsg("查询课程类别列表失败！");
        }else{
            jav.addData("totalPage", qd.getTotalPage());
            jav.addData("totalCount", qd.getTotalCount());
            jav.addData("pageData", qd.getPageData());
        }
        return jav;
    }
    
    /**
     * 创建新的课程类别
     * 
     * @param courseCate 课程类别对象
     * @param request    请求对象，用户日志记录
     * @return           操作结果
     * @see JsonAndView,CourseCategory
     */
    @RequestMapping(value="/create",method=RequestMethod.POST)
    public JsonAndView createCourseCate(CourseCategory courseCate
            ,HttpServletRequest request){
        JsonAndView jav=new JsonAndView();
        String result=courseCategoryService
                .createCourseCate(courseCate);
        if(result!="success"){
            jav.setRet(false);
            jav.setErrcode(1);
            jav.setErrmsg("新增课程类别失败！");
        }else{
            //日志记录
            logDBService.insertNewLog(request, 
                    LogDBService.ADD_OPERATION, "课程类别管理",
                    "新增课程类别："+courseCate.getCocaName());
        }
        return jav;
    }
    
    /**
     * 批量删除课程类别
     * 
     * @param courseCateIds 课程类别Id,多个用逗号隔开
     * @param request    请求对象，用户日志记录
     * @return           操作结果
     * @see JsonAndView
     */
    @RequestMapping(value="/delete",method=RequestMethod.POST)
    public JsonAndView delCourseCates(String courseCateIds
            ,HttpServletRequest request){
        JsonAndView jav=new JsonAndView();
        String result=courseCategoryService
                .delCourseCates(courseCateIds);
        if(result!="success"){
            jav.setRet(false);
            jav.setErrcode(1);
            jav.setErrmsg("部分或全部课程领域删除失败,相应领域可能在其他地方有使用！");
        }else{
            //日志记录
            logDBService.insertNewLog(request, 
                    LogDBService.DELETE_OPERATION, "课程领域管理",
                    "删除课程领域，ID包括："+courseCateIds);
        }
        return jav;
    }
    
    /**
     * 修改课程类别
     * 
     * @param courseCate 新的课程类别对象
     * @param request    请求对象，用户日志记录
     * @return           操作结果
     * @see JsonAndView,CourseCategory
     */
    @RequestMapping(value="/modify",method=RequestMethod.POST)
    public JsonAndView modifyCourseCate(CourseCategory courseCate
            ,HttpServletRequest request){
        JsonAndView jav=new JsonAndView();
        String result=courseCategoryService
                .modifyCourseCate(courseCate);
        if(result!="success"){
            jav.setRet(false);
            jav.setErrcode(1);
            jav.setErrmsg("修改课程领域失败！");
        }else{
            //日志记录
            logDBService.insertNewLog(request, 
                    LogDBService.UPDATA_OPERATION, "课程领域管理",
                    "修改课程领域："+courseCate.getCocaName());
        }
        return jav;
    }
    /**
     * 获取所有课程领域
     * 
     * @param isOpen 1表示开放，0表示关闭
     * @return
     */
    @RequestMapping(value="/viewAllCates",method=RequestMethod.POST)
    public JsonAndView viewAllCourseCates(int isOpen){
        JsonAndView jav=new JsonAndView();
        List<CourseCategory> result=courseCategoryService
                .viewAllCourseCates(isOpen);
        if(result==null){
            jav.setRet(false);
            jav.setErrcode(1);
            jav.setErrmsg("获取课程领域失败！");
        }else{
            jav.addData("courseCates", result);
        }
        return jav;
    }
    
}
