package com.swust.kelab.test.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.swust.kelab.domain.Department;
import com.swust.kelab.model.CommonQuery;
import com.swust.kelab.model.QueryData;
import com.swust.kelab.service.DepartmentService;
import com.swust.kelab.utils.ExportAsExcel;
import com.swust.kelab.utils.JsonUtil;

public class DepartmentServiceTest {
    @Autowired
    private DepartmentService departmentService;
    
    @Before
    public void init() {
        ApplicationContext ctx = 
                new ClassPathXmlApplicationContext(
                        new String[]{"classpath:spring/applicationContext.xml",
                                     "classpath:spring/dao.xml",
                                     "classpath:spring/service.xml"});
        departmentService = ctx.getBean(DepartmentService.class);
    }
    
    @Test()
    public void viewAllDepaListBelongsToParentDeparTest() {
    	System.out.println(JsonUtil.getJSON(
    			departmentService.viewAllDepaListBelongsToParentDepar(2, "6,5")));
    }
    
//    @Test
    public void viewSchoolListTest(){
        List<Department> schools=departmentService.findDepartmentsByType(4);
        //封装返回结果
        if(schools!=null){
            System.out.println(schools.size());
            for (Department department : schools) {
                System.out.println(department.getDepaName());
            }            
        }
    }
    
    //@Test
    public void queryDepartByIdTest(){
        int departId=3;        
        Department depart=departmentService.queryDepartmentById(departId);
        if(depart!=null){
            System.out.println(depart.getDepaName()+"\t"+depart.getDepaType());
        }
        System.out.println("end");
    }
    //@Test
    public void queryDeparentByParent(){
    	String keyword = null;
    	int id = 5;
    	List<Department> list = departmentService.queryDepartmentByParent(id, keyword);
    	System.out.println("查询到记录："+list.get(0).getDepaName());
    }
    //@Test
    public void updateOneDepartment(){
    	int id = 7;
    	Department d = new Department();
    	d.setDepaId(id);
    	d.setDepaName("Pery");
    	d.setDepaAbbreviation("西科大");
    	d.setDepaCode("499");
    	d.setDepaParentId(4);
    	d.setDepaType(2);
    	departmentService.updateOneDepartment(d);
    }
    //@Test
    public void deleteBatch(){
    	String ids = "14,15";
    	departmentService.deleteBatchDepartment(ids);
    }
   //@Test
    public void selectByMutilTerm(){
    	CommonQuery query = new CommonQuery();
    	query.setRecordPerPage(10);
    	query.setSearchType(0);
    	int departId=-1;
    	int parentId=-1;
    	int screenType = 0;
    	 QueryData qd = departmentService.queryByMutiTerm( query,  screenType,parentId,departId);
    	 System.out.println("结果数："+qd.getPageData().get(0).toString());
    }
}
