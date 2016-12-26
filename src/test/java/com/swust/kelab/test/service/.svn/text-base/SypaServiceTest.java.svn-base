package com.swust.kelab.test.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.swust.kelab.domain.SystemParameter;
import com.swust.kelab.model.CommonQuery;
import com.swust.kelab.service.SelectCourseService;
import com.swust.kelab.service.SystemParameterService;

public class SypaServiceTest {

    private  SystemParameterService  systemParameterService;
    private SelectCourseService   selectCourseService ;

    @Before 
    public  void init() {
        ApplicationContext ctx = 
                new ClassPathXmlApplicationContext(
                        new String[]{"classpath:spring/applicationContext.xml",
                                "classpath:spring/dao.xml",
                        "classpath:spring/service.xml"});
        systemParameterService = ctx.getBean(SystemParameterService.class);
        selectCourseService =ctx.getBean(SelectCourseService.class);
    }

    // @Test
    //       public void test(){
    //        CommonQuery query = new CommonQuery();
    //        try {
    //           String path= selectCourseService.exportCourseSelectList("iteach/",query,12, 4, 23, 1);
    //           System.out.println(path);
    //        } catch (Exception e) {
    //            // TODO Auto-generated catch block
    //            e.printStackTrace();
    //        }
    //    }

    public void selectAllSypa(){
        for( SystemParameter sypaModel:systemParameterService.selectAllSypa(10)){
            System.out.println(sypaModel.getSypaEnName());
            System.out.println(sypaModel.getSypaValue());
        }
        try {
            systemParameterService.deleteLink(32);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    //     @Test
    //   public void updateSypa(){
    //         
    //        SystemParameter  sypaModel1= new SystemParameter();
    //        sypaModel1.setSypaName("李");
    //        sypaModel1.setSypaRemark("我坚信");
    //        sypaModel1.setSypaType(10);
    //        sypaModel1.setSypaValue("是个奇葩");
    ////        
    ////        SystemParameterModel  sypaModel2= new SystemParameterModel();
    ////        sypaModel2.setSypaId("24");
    ////        sypaModel2.setSypaValue("100");
    ////        list.add(sypaModel1);
    ////        list.add(sypaModel2);
    ////        
    ////        try {
    ////            systemParameterService.updateSypa(list);
    ////        } catch (Exception e) {
    ////            // TODO Auto-generated catch block
    ////            e.printStackTrace();
    ////        }
    //       try {
    //        systemParameterService.addLink(sypaModel1);
    //    } catch (Exception e) {
    //        // TODO Auto-generated catch block
    //        e.printStackTrace();
    //    }
    //}
    
    //@Test
    public void attainSyspamValueByEnNameTest(){
        String enName="fileFormats";
        String value=systemParameterService.attainValueByEnName(enName);
        System.out.println(value);
    }
    
    //@Test
    public void bbsPostReplyCheckTest(){
        int userId=3;
        String bbsTitle="";
        String bbsContent="saaadsaf";
        String result=systemParameterService.bbsPostReplyCheck(
                userId, null, bbsContent);
        System.out.println(result);
    }
    
    @Test
    public void bbsAttachCheckTest(){
        String format="exe";
        long size=123400000000L;
        String result=systemParameterService.bbsAttachCheck(format, size);
        System.out.println(result);
    }

}

