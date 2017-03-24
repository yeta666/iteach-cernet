package com.swust.kelab.test.service;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.swust.kelab.domain.Attachment;
import com.swust.kelab.domain.BbsReply;
import com.swust.kelab.model.BBSReplyModel;
import com.swust.kelab.model.CommonQuery;
import com.swust.kelab.model.PageData;
import com.swust.kelab.model.QueryData;
import com.swust.kelab.service.BbsReplyService;

public class BbsReplyServiceTest {
    private BbsReplyService bbsReplyService;
    
    @Before
    public void init() {
        ApplicationContext ctx = 
                new ClassPathXmlApplicationContext(
                        new String[]{"classpath:spring/applicationContext.xml",
                                     "classpath:spring/dao.xml",
                                     "classpath:spring/service.xml"});
        bbsReplyService = ctx.getBean(BbsReplyService.class);
    }
    
    //@Test
    public void viewBbsReplyListByPostTest(){
        CommonQuery query=new CommonQuery();
        int bbsPostId=5;
        QueryData qd=bbsReplyService.viewBbsReplyListByPost(query,bbsPostId);
        if(qd!=null){
            System.out.println("totalCount:"+qd.getTotalCount());
            System.out.println("totalPage:"+qd.getTotalPage());
            List<PageData> datas=qd.getPageData();
            if(datas==null){
                System.out.println("空数据");
                return;
            }
            for (PageData pageData : datas) {
                System.out.println("pageNum:"+pageData.getPage());
                List<BBSReplyModel> ttsms=(List<BBSReplyModel>)pageData.getData();
                System.out.println(ttsms.size());
                for (BBSReplyModel ttsm : ttsms) {
                    System.out.println(ttsm.getReplyId()+"\t"+
                            ttsm.getReplyContent()+"\t"+
                            ttsm.getReplyUserName()+"\t"+
                            ttsm.getRealReplyTime());
                    if(ttsm.isHasAttach()){
                        List<Attachment> attachs=(List<Attachment>)ttsm.getAttas();
                        if(attachs!=null&&attachs.size()>0){
                            for (Attachment attach : attachs) {
                                System.out.println(attach.getAttaFilename()+"\t"+
                                        attach.getAttaLocation());
                            }
                        }
                    }
                }
            }
        }
        System.out.println("end");
    }
    
    //@Test
    public void createBbsReplyTest(){
        BbsReply bbsReply=new BbsReply();
        bbsReply.setBbreBbpoId(1);
        bbsReply.setBbreContent("reply,aaaaa");
        bbsReply.setBbreUserId(1);
        String attaches="4";
        String result=bbsReplyService.createBbsReply(bbsReply, attaches);
        System.out.println(result);
    }
    
    @Test
    public void deleteBbsReplyTest(){
        int replyId=44;
        System.out.println(bbsReplyService.deleteBbsReply(replyId));
    }
}
