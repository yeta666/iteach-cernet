package com.swust.kelab.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.swust.kelab.domain.User;
import com.swust.kelab.model.CommonQuery;
import com.swust.kelab.model.QueryData;
import com.swust.kelab.service.LogDBService;
import com.swust.kelab.service.ScoreExportConfirmService;
import com.swust.kelab.utils.CookieUtil;
import com.swust.kelab.web.json.JsonAndView;

/**
 * 处理学生信息导出查询和确认的controller
 * 
 * @author Wu
 * 
 */
@Controller
@RequestMapping("/scoreExportConfirm")
public class ScoreExportConfirmController {
    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    ScoreExportConfirmService scoreExportConfirmService;
    
    @Autowired
    private LogDBService logDBService;
    
    /**
     * 查询学生导出信息的记录列表
     * 
     * @param query         其中的searchWord指定导出人或者确认人的名称
     * @param confirmOrNot  是否确认，0表示所有，1表示已确认，2表示未确认
     * @param exportDepaId  导出机构id，0表示所有
     * @param confirmDepaId 确认机构id,0表示所有
     * @return  返回导出的记录列表
     */
    @RequestMapping(value = "/viewList", method = RequestMethod.POST)
    public JsonAndView viewExportConfirmList(CommonQuery query,
            int confirmOrNot,int exportDepaId,int confirmDepaId){
        JsonAndView jav = new JsonAndView();
        QueryData qd =scoreExportConfirmService.viewExportConfirmList(
                query, confirmOrNot, exportDepaId, confirmDepaId); 
        if (qd == null) {
            jav.setRet(false);
            jav.setErrcode(1);
            jav.setErrmsg("查询成绩导出信息失败！");
        } else {
            jav.addData("totalPage", qd.getTotalPage());
            jav.addData("totalCount", qd.getTotalCount());
            jav.addData("pageData", qd.getPageData());
        }
        return jav;
    }
    
    /**
     * 确认一次导出记录
     * 
     * @param exportId        原始导出记录的id，对应数据库中的seco_id
     * @param confirmAttaId   确认的附件id
     * @param confirmRemark   确认的备注信息（前台应限制字数，否则后台会存储失败）
     * @return    返回操作是否成功的提示信息
     */
    @RequestMapping(value = "/confirm", method = RequestMethod.POST)
    public JsonAndView confirmOneExport(HttpServletRequest request
            ,int exportId,int confirmAttaId,String confirmRemark){
        JsonAndView jav = new JsonAndView();
        String basePath = request.getSession()
                .getServletContext().getRealPath("/");
        User me = CookieUtil.getCookieUser(request);
        String result=scoreExportConfirmService.confirmOneExport(basePath,
                exportId, confirmAttaId, confirmRemark,me.getUserId()); 
        if (result == null) {
            jav.setRet(false);
            jav.setErrcode(1);
            jav.setErrmsg("确认信息时发生异常！请重试或联系管理员！");
        } else {
            jav.setRet(true);
            Map map=new HashMap();
            map.put("info", result);
            jav.setData(map);
            logDBService.insertNewLog(request, LogDBService.SELECT_OPERATION, "成绩导出确认统计", "确认导出的学生成绩");
        }
        return jav;
    }
    
    /**
     * 删除成绩导出记录：只有未确认的导出记录才能被删除
     *               ；只有自己或者上级部门的人可以删除自己的导出记录
     * @param request    包含操作者的cookie信息
     * @param exportIds  需要删除的记录id，多个用逗号隔开，例如“1,3,5”
     * @return  操作是否成功的提示
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public JsonAndView deleteExportInfo(HttpServletRequest request
            ,String exportIds){
        JsonAndView jav = new JsonAndView();
        String result=scoreExportConfirmService.deleteExportInfo(exportIds); 
        if (result == null) {
            jav.setRet(false);
            jav.setErrcode(1);
            jav.setErrmsg("删除导出记录时发生异常！请重试或联系管理员！");
        } else {
            jav.setRet(true);
            Map map=new HashMap();
            map.put("info", result);
            jav.setData(map);
            logDBService.insertNewLog(request, LogDBService.SELECT_OPERATION, "成绩导出确认统计", "删除成绩导出记录");
        }
        return jav;
    }
}
