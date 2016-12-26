package com.swust.kelab.test.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.swust.kelab.recom.RecomService;

public class RecomServiceTest{
    private RecomService recomService;
    
    @Before
    public void init() {
        ApplicationContext ctx = 
                new ClassPathXmlApplicationContext(
                        new String[]{"classpath:spring/applicationContext.xml",
                                     "classpath:spring/dao.xml",
                                     "classpath:spring/service.xml"});
        recomService = ctx.getBean(RecomService.class);
    }
    
    
    @Test
    public void recomScheduled(){
    	recomService.recomScheduled();
    }
}
