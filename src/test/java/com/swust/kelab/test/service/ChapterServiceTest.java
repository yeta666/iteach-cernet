package com.swust.kelab.test.service;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.swust.kelab.service.ChapterService;
import com.swust.kelab.utils.JsonUtil;

public class ChapterServiceTest {

    private ChapterService chapterService;

    @Before
    public void init() {
        ApplicationContext ctx = 
                new ClassPathXmlApplicationContext(
                        new String[]{"classpath:spring/applicationContext.xml",
                                "classpath:spring/dao.xml",
                        "classpath:spring/service.xml"});
        chapterService = ctx.getBean(ChapterService.class);
    }

    //@Test()
	public void viewTest() {
		Map data = chapterService.viewChapterListByCourse("",1, 4);
		System.out.println(JsonUtil.getJSON(data));
	}
    
//    @Test()
    public void resourceTest2() {
//        String line = JsonUtil.getJSON(chapterService.viewAllChapterList(null,2,3));
//        System.out.println(line);
    }
    
//    @Test()
    public void resourceTest() {
        String line = JsonUtil.getJSON(chapterService.viewChapterListByCourse("",1,2));
        System.out.println(line);
    }
    
    @Test()
    public void assessChpterTest() {
        //boolean result = chapterService.assessChapter(8, 4);
        //System.out.println(result);
    }

}
