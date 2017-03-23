package com.swust.kelab.test.service;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.swust.kelab.domain.EvaluateMethod;
import com.swust.kelab.service.EvaluateMethodService;


public class EvaluateMethodTest {
    private EvaluateMethodService evaluateMethodService;
    
    @Before
    public void init() {
        ApplicationContext ctx = 
                new ClassPathXmlApplicationContext(
                        new String[]{"classpath:spring/applicationContext.xml",
                                     "classpath:spring/dao.xml",
                                     "classpath:spring/service.xml"});
        evaluateMethodService = ctx.getBean(EvaluateMethodService.class);
    }
    
    @Test
    public void addEvaluateMethodTest() {
    	EvaluateMethod em = new EvaluateMethod();
    	em.setEvmeId(7);
    	em.setEvmeName("zxvzxcv");
    	em.setEvmePattern("zxcvzcv");
    	em.setEvmeThrehold("zxcvzxcvzcvzxcv");
    	System.out.println(evaluateMethodService.addEvaluateMethod(em));
    }
    
//    @Test
    public void viewAllEvaMethodsTest(){
        List<EvaluateMethod> result=evaluateMethodService
                .viewAllEvaluateMethods();
        if (result != null) {
            System.out.println("size:"+result.size());
            for (EvaluateMethod em : result) {
                System.out.println(em.getEvmeId()+"\t"+em.getEvmeName());
            }
        }
        System.out.println("end");
    }
    
}
