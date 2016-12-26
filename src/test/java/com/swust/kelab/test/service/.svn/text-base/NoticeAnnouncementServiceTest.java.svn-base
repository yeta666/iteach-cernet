package com.swust.kelab.test.service;

import org.junit.Before;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.swust.kelab.domain.NoticeAnnouncement;
import com.swust.kelab.service.NoticeAnnouncementService;
import com.swust.kelab.utils.JsonUtil;

public class NoticeAnnouncementServiceTest {
	
    private NoticeAnnouncementService noticeServce;
    
    @Before
    public void init() {
        ApplicationContext ctx = 
                new ClassPathXmlApplicationContext(
                        new String[]{"classpath:spring/applicationContext.xml",
                                     "classpath:spring/dao.xml",
                                     "classpath:spring/service.xml"});
        noticeServce = ctx.getBean(NoticeAnnouncementService.class);
    }
    
//    @Test()
    public void delTest() {
    	noticeServce.delNoticeAnnouncement("3,",null);
    }
    
//    @Test
    public void modTest() {
    	NoticeAnnouncement notice = new NoticeAnnouncement();
    	notice.setNoanId(3);
    	notice.setNoanTitle("title 1");
    	notice.setNoanContent("content 1");
    	notice.setNoanRoleids(3+"");
    	notice.setNoanTopdays(2);
    	notice.setNoanIsimport(true);
    	notice.setNoanCreatorid(2);
//    	noticeServce.addOrModNoticeAnnouncement(notice,"");
    }
    
//    @Test
    public void addTest() {
    	NoticeAnnouncement notice = new NoticeAnnouncement();
    	notice.setNoanTitle("title");
    	notice.setNoanContent("content");
    	notice.setNoanRoleids(2+""); 
    	notice.setNoanTopdays(4);
    	notice.setNoanIsimport(false);
    	notice.setNoanCreatorid(2);
//    	noticeServce.addOrModNoticeAnnouncement(notice,"");
    }
    
//    @Test
    public void viewTest(){
//    	System.out.println(
//    			JsonUtil.getJSON(
//    					noticeServce.viewNoticeAnnouncementList("1,2,3",10,1,null,null,null,null)));
    }
}
