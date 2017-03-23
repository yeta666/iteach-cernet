package com.swust.kelab.test.service;

import java.net.InetAddress;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.swust.kelab.model.VideoServerModel;
import com.swust.kelab.service.VideoServerService;
import com.swust.kelab.utils.JsonUtil;

public class VideoServiceTest {
    private VideoServerService videoServerService;

    @Before
    public void init() {
        
        ApplicationContext ctx = new ClassPathXmlApplicationContext(new String[] {
                "classpath:spring/applicationContext.xml", "classpath:spring/dao.xml", "classpath:spring/service.xml" });
        videoServerService = ctx.getBean(VideoServerService.class);
    }

    @Test()
    public void testasfasdf() {
		try {
			Map map = videoServerService.selectByOrder("10.10.4.25");
			System.out.println(JsonUtil.getJSON(map));
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
//    @Test
    public void test() {
//        VideoServerModel videoServerModel=videoServerService.selectById(2);
//        System.out.println(videoServerModel.getVeseDepart());   
        try {
            Map<String,Object> map = videoServerService.selectByOrder("10.10.4.1");
            @SuppressWarnings("unchecked")
            Set<VideoServerModel> set =  (Set<VideoServerModel>) map.get("in");
            System.out.println(set.size());
            @SuppressWarnings("unchecked")
            List<VideoServerModel> list = (List<VideoServerModel>) map.get("out");
            InetAddress inet= InetAddress.getByName("dvcxvx");
            System.out.println(inet);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          
        }
       
    }

}
