package com.swust.kelab.test.service;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.swust.kelab.domain.Attachment;
import com.swust.kelab.domain.BbsPost;
import com.swust.kelab.model.BbsPostModel;
import com.swust.kelab.model.CommonQuery;
import com.swust.kelab.model.PageData;
import com.swust.kelab.model.QueryData;
import com.swust.kelab.service.BbsPostService;
import com.swust.kelab.web.controller.BbsPostController;

public class BbsPostServiceTest {
    private BbsPostService bbsPostService;
    
    @Before
    public void init() {
        ApplicationContext ctx = 
                new ClassPathXmlApplicationContext(
                        new String[]{"classpath:spring/applicationContext.xml",
                                     "classpath:spring/dao.xml",
                                     "classpath:spring/service.xml"});
        bbsPostService = ctx.getBean(BbsPostService.class);
    }
    
    //@Test
    public void viewBbsPostList(){
        CommonQuery query=new CommonQuery();
        query.setSearchWord("ello");
        int userId=1;
        int departId=0;
        int courseId=0;
        int queryType=1;
        QueryData staResult=bbsPostService.viewBbsPostList(query, departId, userId, courseId,queryType);
        if(staResult!=null){
            System.out.println("totalCount:"+staResult.getTotalCount());
            System.out.println("totalPage:"+staResult.getTotalPage());
            List<PageData> datas=staResult.getPageData();
            if(datas==null){
                System.out.println("空数据");
                return;
            }
            for (PageData pageData : datas) {
                System.out.println("pageNum:"+pageData.getPage());
                List<BbsPostModel> ttsms=(List<BbsPostModel>)pageData.getData();
                System.out.println(ttsms.size());
                for (BbsPostModel ttsm : ttsms) {
                    System.out.println(ttsm.getBbpoTitle()+"\t"+
                            ttsm.getCourseName()+"\t"+
                            ttsm.getUserName()+"\t"+
                            ttsm.getRealTime()+"\t"+
                            ttsm.getBbpoVisitnum()+"\t"+
                            ttsm.getBbpoReplynum()+"\t"+
                            ttsm.getRealUpdatetime()+"\t"+
                            ttsm.isBbpoIsbest()+"\t"+
                            ttsm.isBbpoIstop());
                }
            }
        }
        System.out.println("end");
    }
    
    //@Test
    public void viewBbsPostDetailTest(){
        int bbsPostId=2;
        Map post=bbsPostService.viewBbsPostDetail(bbsPostId);
        if(post!=null){
            System.out.println(post.get("postTitle")+"\t"+
                        post.get("courseName")+"\t"+
                        post.get("userName")+"\t"+
                        post.get("pubTime")+"\t"+
                        post.get("updataTime")+"\t"+
                        post.get("visitNum"));
            List<Attachment> attachs=(List<Attachment>)post.get("attachs");
            if(attachs!=null&&attachs.size()>0){
                for (Attachment attach : attachs) {
                    System.out.println(attach.getAttaFilename()+"\t"+
                            attach.getAttaLocation());
                }
            }
            
        }
    }
    
    //@Test
    public void updateBbsPostTest(){
        int bbsPostId=1;
        int updateType=2;
        String result=bbsPostService.updateBbsPost(bbsPostId, updateType);
        System.out.println(result);
    }
    
    //@Test
    public void createBbsPostTest(){
        BbsPost bbsPost=new BbsPost();
        bbsPost.setBbpoCourId(1);
        bbsPost.setBbpoUserId(1);
        bbsPost.setBbpoTitle("3333333");
        bbsPost.setBbpoContent("bbbbbbbbb");
        String attaches="2";
        String result=bbsPostService.createBbsPost(bbsPost, attaches);
        System.out.println(result);
    }
    
    //@Test
    public void setPostTopTest(){
        int postId=2;
        int setValue=2;
        System.out.println(bbsPostService.setPostTop(postId, setValue));
    }
    
  //  @Test
    public void setPostEssenceTest(){
        int postId=2;
        int setValue=2;
        System.out.println(bbsPostService.setPostEssence(postId, setValue));
    }
}
