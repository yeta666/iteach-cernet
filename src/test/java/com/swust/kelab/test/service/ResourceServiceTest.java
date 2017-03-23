package com.swust.kelab.test.service;

import java.io.File;
import java.util.Date;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.swust.kelab.domain.Resource;
import com.swust.kelab.service.ResourceService;
import com.swust.kelab.utils.JsonUtil;
import com.swust.kelab.utils.VideoUtil;

public class ResourceServiceTest {

    private ResourceService resourceService;

    @Before
    public void init() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext(new String[] {
                "classpath:spring/applicationContext.xml", "classpath:spring/dao.xml", "classpath:spring/service.xml" });
        resourceService = ctx.getBean(ResourceService.class);
    }
    
//    @Test
    public void viewRecentResourceListTest() {
    	System.out.println(JsonUtil.getJSON(resourceService.viewRecentResourceList()));
    }
    
//    @Test()
    public void viewResourceListByChapter() {
    	System.out.println(JsonUtil.getJSON(resourceService.viewResourceListByChapterPage(null, null, null,1,2)));
    }
    
//    @Test() 
    public void addResource() {
    	Resource reso = new Resource();
    	reso.setResoCourId(2);
    	reso.setResoType(1);
    	reso.setResoLocation("http://www.google.com.hk");
    	reso.setResoDownnum(0);
    	reso.setResoUserid(2);
    	Integer is = resourceService.addResourceByAdmin(reso);
    	System.out.println("\n*************\n"+is);
    }
    
//    @Test()
    public void vedioDuration() {
    	File file = new File("E:\\(MV)周杰伦.-.07.心雨.flv");
        Long time = VideoUtil.getVideoDuration(file);
        System.out.println(time);
    }

//    @Test()
    public void viewResourceListForAdmin() {
    	Map<String, Object> map = resourceService.viewResourceListForAdmin(null,null,null, 2, null,"2010-12-10","2013-7-18",null, "1,2,3", 10);
    	System.out.println("\n"+JsonUtil.getJSON(map));
    }
    
    // @Test()
    public void selectOneResource() {
        Resource r = resourceService.downloadDocument(3,2);
        System.out.println(r == null);
    }

//    @Test()
    public void viewResourceList() {
        Map<String,Object> list = resourceService.viewResourceList(null,null, null, null, 3, -1, -1);
        System.out.println(JsonUtil.getJSON(list));
    }

    // @Test()
    public void resourceTest() {
//        Calendar c1 = Calendar.getInstance();
        // 2013-06-23 17:02:51
//        c1.set(2013, 5, 22, 9, 1, 25);
//        Date d1 = c1.getTime();
//        c1.set(2013, 6, 25, 9, 1, 25);
//        Date d2 = c1.getTime();
    }

//    @Test
    public void userTest() {

        if (resourceService != null) {
            for (int i = 0; i < 10; i++) {
                Resource resource = new Resource();
                resource.setResoCourId(i % 2 == 0 ? 1 : 2);
                resource.setResoType(2);
                resource.setResoLocation("d://test_" + i + "//adfaf");
                resource.setResoUserid(1);
                resource.setResoTitle("xxxxx_" + i);
                resource.setResoAddtime(new Date());
                boolean flag = resourceService.insertResource(resource);
                System.out.println("是否成功:" + flag);
            }
        }
    }
}
