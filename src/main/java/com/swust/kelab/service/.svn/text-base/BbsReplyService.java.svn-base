package com.swust.kelab.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.swust.kelab.domain.Attachment;
import com.swust.kelab.domain.BbsReply;
import com.swust.kelab.domain.Role;
import com.swust.kelab.domain.User;
import com.swust.kelab.model.BBSReplyModel;
import com.swust.kelab.model.BbsPostModel;
import com.swust.kelab.model.CommonQuery;
import com.swust.kelab.model.PageData;
import com.swust.kelab.model.QueryData;
import com.swust.kelab.repos.AttachmentDAO;
import com.swust.kelab.repos.BbsPostDAO;
import com.swust.kelab.repos.BbsReplyDAO;
import com.swust.kelab.repos.ReSelectCourseDAO;
import com.swust.kelab.repos.ReUserRoleDAO;
import com.swust.kelab.repos.UserDAO;
import com.swust.kelab.repos.bean.ListQuery;
import com.swust.kelab.utils.FormatUtil;

/**
 * 处理论坛回复的Service
 * 
 * @author 吴岘辉
 *
 */
@Service
public class BbsReplyService {
    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private BbsReplyDAO bbsReplyDAO;

    @Autowired
    private AttachmentDAO attachmentDAO;

    @Autowired
    private ReUserRoleDAO reUserRoleDAO;

    @Autowired
    private ReSelectCourseDAO reSelectCourseDAO;

    @Autowired
    private BbsPostDAO bbsPostDAO;

    @Autowired
    private BbsPostService bbsPostService;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private SelectCourseService selectCourseService;
    
    /**
     * 查询主贴对应的回帖列表
     * 
     * @param query      分页信息
     * @param bbsPostId  主贴id
     * @return           回帖列表
     * @see QueryData,CommonQuery
     */
    public QueryData viewBbsReplyListByPost(CommonQuery query,int bbsPostId){
        QueryData result=new QueryData();
        //查询总的回帖数
        int replyNum=0;
        try {
            replyNum=bbsReplyDAO.countReplyNumByPost(bbsPostId);
        } catch (Exception e) {
            logger.error("countReplyNumByPost error!\n"+e.getLocalizedMessage());
            result=null;
            return result;
        }
        if(replyNum<=0){
            return result;
        }
        //查询所有回复的基本信息
        result.setTotalCount(replyNum);
        if(query.getRecordPerPage()<=0){
            query.setRecordPerPage(10);
        }
        int totalPage=QueryData.computeTotalPage(replyNum, query.getRecordPerPage());
        result.setTotalPage(totalPage);
        if(query.getPageArray()==null){
            query.setPageArray(new int[] { 1, 2, 3 });
        }
        int startIndex=(query.getPageArray()[0]-1)*query.getRecordPerPage();
        int fetchSize=query.getPageArray().length*query.getRecordPerPage();
        ListQuery myQuery=query.format();
        myQuery.fill("startIndex", startIndex);
        myQuery.fill("maxCount", fetchSize);
        myQuery.fill("bbsPostId", bbsPostId);
        List<BBSReplyModel> replys=null;
        try {
            replys=bbsReplyDAO.viewReplyList(myQuery);
        } catch (Exception e) {
            logger.error("viewReplyList error!\n"+e.getLocalizedMessage());
            result=null;
            return result;
        }
        if(replys==null||replys.size()<=0){
            return result;
        }
        //时间格式转换
        for (BBSReplyModel brm : replys) {
            brm.setRealReplyTime(FormatUtil.formatDate(brm.getReplyTime()));
        }
        //查询附件信息
        for (BBSReplyModel brm : replys) {
            if(brm.isHasAttach()){
                Map q=new HashMap();
                q.put("sourceId", brm.getReplyId());
                q.put("sourceType", 2);
                List<Attachment> attas=null;
                try {
                    attas=attachmentDAO.getAttachBySourceTypeId(q);
                } catch(Exception e) {
                    logger.error("getAttachBySourceTypeId error!\n"+e.getLocalizedMessage());
                }
                if(attas!=null&&attas.size()>0){
                    brm.setAttas(attas);
                }
            }
        }
        //按照分页封装数据
        //封装数据
        List<PageData> pageDataList = Lists.newArrayList();  
        for (int k = 0; k < query.getPageArray().length; k++) {
            int page = query.getPageArray()[k];
            if (page <= 0 || page > totalPage) {
                continue;
            }
            startIndex=k*query.getRecordPerPage();
            int endIndex=startIndex+query.getRecordPerPage();
            if(startIndex>=replys.size()){
                continue;
            }
            if(endIndex>replys.size()){
                endIndex=replys.size();
            }
            List<BBSReplyModel> pageDatas=replys.subList(startIndex, endIndex);
            pageDataList.add(new PageData(page, pageDatas));
        }
        result.setPageData(pageDataList);
        return result;
    }

    /**
     * 新增回复
     * 
     * @param bbsReply  回复对象
     * @param attaches  附件id，多个用逗号隔开
     * @return          新增结果
     * @see BbsReply
     */
    public String createBbsReply(BbsReply bbsReply,String attaches){
        String result="fail";
        //判断是否有附件
        if(attaches!=null&&!attaches.isEmpty()&&attaches.matches("\\d+(,\\d+)*")){
            bbsReply.setBbreHasattach(true);
        }else{
            bbsReply.setBbreHasattach(false);
        }
        //存储回复
        int replyId=0;
        try {
            int insert=bbsReplyDAO.createBbsReply(bbsReply);
            if(insert>0){
                replyId=bbsReply.getBbreId();
            }
        } catch(Exception e) {
            logger.error("createBbsReply error!\n"+e.getLocalizedMessage());
        }
        if(replyId<=0){
            return result;
        }
        //设置附件的原id
        if(bbsReply.getBbreHasattach()){
            String[] idStr=attaches.split(",");
            for (String str : idStr) {
                int attachId=Integer.parseInt(str);
                Map query=new HashMap();
                query.put("attachId", attachId);
                query.put("sourceId", replyId);
                int change=0;
                try {
                    change=attachmentDAO.changeAttachSourceId(query);
                } catch(Exception e) {
                    logger.error("changeAttachSourceId error!\n"+e.getLocalizedMessage());
                }
                if(change<=0){
                    logger.error("changeAttachSourceId failed!");
                }
            }
        }

        //增加主贴的回复数
        bbsPostService.updateBbsPost(bbsReply.getBbreBbpoId(), 2);

        //若发帖人为学生，则要增加其bbs讨论数,并更新成绩
        User user=userDAO.findOneUser(bbsReply.getBbreUserId());
        if(user!=null&&user.getUserType()==1){
            BbsPostModel bbsPost=null;
            try {
                bbsPost=bbsPostDAO.viewBbsPostDetail(bbsReply.getBbreBbpoId());
            } catch(Exception e) {
                logger.error("viewBbsPostDetail error!\n"+e.getLocalizedMessage());
            }
            if(bbsPost!=null){
                Map query=new HashMap();
                query.put("stuId", bbsReply.getBbreUserId());
                query.put("courseId", bbsPost.getCourseId());
                try {
                    reSelectCourseDAO.addBbsDiscussNum(query);
                } catch(Exception e) {
                    logger.error("addBbsDiscussNum error!\n"+e.getLocalizedMessage());
                }
                //更新总成绩
                try {
                    selectCourseService.updateCourseTotalScore(
                            bbsReply.getBbreUserId(),
                            bbsPost.getCourseId());
                } catch(Exception e) {
                    logger.error("updateCourseTotalScore error!\n"+e.getLocalizedMessage());
                }
            }                
        }
        result="success";
        return result;
    }

    /**
     * 删除回帖
     * 
     * @param bbsReplyId 回帖id
     * @return           操作结果
     */
    public String deleteBbsReply(int bbsReplyId){
        String result="success";
        //先获取要删除的回帖
        BbsReply bbsReply=null;
        try {
            bbsReply=bbsReplyDAO.viewOneReply(bbsReplyId);
        } catch(Exception e) {
            logger.error("viewOneReply error!\n"+e.getLocalizedMessage());
        }
        if(bbsReply==null){
            logger.warn("回帖(id为："+bbsReplyId+")不存在，可能已经被其他用户删除！");
        }else{
            //获取发帖人id和主贴id
            int userId=bbsReply.getBbreUserId();
            int postId=bbsReply.getBbreBbpoId();
            int deleteNum=0;
            try {
                deleteNum=bbsReplyDAO.deleteReplyById(bbsReplyId);
            } catch(Exception e) {
                logger.error("deleteReplyById error!\n"+e.getLocalizedMessage());
                result="删除回帖失败！";
            } 
            if(deleteNum!=1){
                logger.warn("回帖(id为："+bbsReplyId+")可能已经被其他用户删除！");
            }else{
                //查询主贴
                BbsPostModel bbsPost=null;
                try {
                    bbsPost=bbsPostDAO.viewBbsPostDetail(postId);
                } catch(Exception e) {
                    logger.error("viewBbsPostDetail error!\n"+e.getLocalizedMessage());
                }
                //更新主贴相应信息                
                if(bbsPost!=null){                  
                    //减少主贴的回复数
                    bbsPostService.updateBbsPost(postId, 3);
                    //如果是学生，则减少其对应课程的论坛讨论数
                    User user=userDAO.findOneUser(userId);
                    if(user!=null&&user.getUserType()==1){
                        Map query=new HashMap();
                        query.put("stuId", userId);
                        query.put("courseId", bbsPost.getCourseId());
                        query.put("reduceNum", 1);
                        try {
                            reSelectCourseDAO.reduceBbsDiscussNum(query);
                        } catch(Exception e) {
                            logger.error("reduceBbsDiscussNum error!\n"+e.getLocalizedMessage());
                        }
                        //更新总成绩
                        try {
                            selectCourseService.updateCourseTotalScore(
                                    userId,
                                    bbsPost.getCourseId());
                        } catch(Exception e) {
                            logger.error("updateCourseTotalScore error!\n"+e.getLocalizedMessage());
                        }
                    }
                }          
            }
        }        
        return result;
    }
}
