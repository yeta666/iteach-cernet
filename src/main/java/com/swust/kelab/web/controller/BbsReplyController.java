package com.swust.kelab.web.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.swust.kelab.domain.BbsReply;
import com.swust.kelab.model.CommonQuery;
import com.swust.kelab.model.QueryData;
import com.swust.kelab.service.BbsPostService;
import com.swust.kelab.service.BbsReplyService;
import com.swust.kelab.service.SelectCourseService;
import com.swust.kelab.service.SystemParameterService;
import com.swust.kelab.web.json.JsonAndView;

/**
 * 处理论坛回复的控制器
 * 
 * @author 吴岘辉
 *
 */
@Controller
@RequestMapping("/bbsReply")
public class BbsReplyController {
    @Autowired
    BbsReplyService bbsReplyService;
    
    @Autowired
    private SystemParameterService systemParameterService;
    
    /**
     * 查询主贴对应的回帖列表
     * 
     * @param query      分页信息
     * @param bbsPostId  主贴id
     * @return           回帖列表
     * @see JsonAndView,QueryData,CommonQuery
     */
    @RequestMapping(value="/viewReplyList",method=RequestMethod.POST)
    public JsonAndView viewBbsReplyListByPost(CommonQuery query,int bbsPostId){
        JsonAndView jav=new JsonAndView();
        QueryData qd=bbsReplyService.viewBbsReplyListByPost(query,bbsPostId);
        if(qd==null){
            jav.setRet(false);
            jav.setErrcode(1);
            jav.setErrmsg("查询主贴对应的回帖列表失败！");
        }else{
            jav.addData("totalPage", qd.getTotalPage());
            jav.addData("totalCount", qd.getTotalCount());
            jav.addData("pageData", qd.getPageData());
        }
        return jav;
    }
    
    
    /**
     * 新增bbs回复
     * 
     * @param bbsReply 回复对象，包含了附件的详细信息
     * @param attaches 回复对应的附件id,多个附件id用逗号隔开
     * @return 操作结果
     * @see JsonAndView,BbsReply
     */
    @RequestMapping(value="/createBbsReply",method=RequestMethod.POST)
    public JsonAndView createBbsReply(BbsReply bbsReply,String attaches){
        JsonAndView jav=new JsonAndView();
        //回帖检测
        String checkResult=systemParameterService.bbsPostReplyCheck(
                bbsReply.getBbreUserId(), "", 
                bbsReply.getBbreContent());
        if(checkResult==null||!checkResult.equals("success")){
            jav.setRet(false);
            jav.setErrcode(2);
            jav.setErrmsg("回复失败！"+checkResult);
            return jav;
        }
        //存入数据库
        String result=bbsReplyService.createBbsReply(bbsReply, attaches);
        if(result!="success"){
            jav.setRet(false);
            jav.setErrcode(1);
            jav.setErrmsg("回复失败！");
        }
        return jav;
    }
    
    /**
     * 删除回帖
     * 
     * @param bbsReplyId  回帖id
     * @return            操作结果
     * @see JsonAndView
     */
    @RequestMapping(value="/deleteBbsReply",method=RequestMethod.POST)
    public JsonAndView deleteBbsReply(int bbsReplyId){
        JsonAndView jav=new JsonAndView();
        String result=bbsReplyService.deleteBbsReply(bbsReplyId);
        if(result!="success"){
            jav.setRet(false);
            jav.setErrcode(1);
            jav.setErrmsg(result);
        }
        return jav;
    }
    
    
}
