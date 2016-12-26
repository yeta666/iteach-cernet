package com.swust.kelab.test.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.swust.kelab.service.VedioServerService;
import com.swust.kelab.utils.JsonUtil;

public class VedioServerServiceTest {

    private VedioServerService vss;

    @Before
    public void init() {
        ApplicationContext ctx = 
                new ClassPathXmlApplicationContext(
                        new String[]{"classpath:spring/applicationContext.xml",
                                "classpath:spring/dao.xml",
                        "classpath:spring/service.xml"});
        vss = ctx.getBean(VedioServerService.class);
    }

    @Test()
    public void resourceTest() {
        String line = JsonUtil.getJSON(vss.viewLocalVedioServerList("绵阳一中"));
        System.out.println(line);
    }

}
