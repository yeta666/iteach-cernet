package com.swust.kelab.test.service;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.swust.kelab.domain.Attachment;
import com.swust.kelab.service.AttachmentService;

public class AttachmentServiceTest {
    private AttachmentService attachmentService;
    
    @Before
    public void init() {
        ApplicationContext ctx = 
                new ClassPathXmlApplicationContext(
                        new String[]{"classpath:spring/applicationContext.xml",
                                     "classpath:spring/dao.xml",
                                     "classpath:spring/service.xml"});
        attachmentService = ctx.getBean(AttachmentService.class);
    }
    
    @Test
    public void createAttachmentTest(){
       Attachment attach=new Attachment();
       //attach.setAttaId(3);
       attach.setAttaFilename("abc.txt");
       attach.setAttaLocation("upload/");
       attach.setAttaSourceid(1);
       attach.setAttaSourceType(2);
       attach.setAttaOriFilename("测试.txt");
       int newId=attachmentService.createAttachment(attach);
       System.out.println(newId);
    }
    
    //@Test
    public void deleteAttachmentTest(){
        int attachId=3;
        String path="";
        String result=attachmentService.deleteAttachment(attachId,path);
        System.out.println(result);
    }
    
    //@Test
    public void addAttachDownNumTest(){
        int attachId=4;
        String result=attachmentService.addAttachDownNum(attachId);
        System.out.println(result);
    }
    
    @Test
    public void viewAttachmentByIdTest(){
        int attachId=1;
        Attachment attach=attachmentService.viewAttachmentById(attachId);
        if(attach==null){
            System.out.println(" null 对象！");
        }else{
            System.out.println(attach.getAttaFilename());
            System.out.println(attach.getAttaLocation());
            System.out.println(attach.getAttaOriFilename());
        }
        
    }
    @Test
    public void findAttaByType()
    {
    	List<Attachment> list = attachmentService.findAttachmentsByType(1);
    }
}
