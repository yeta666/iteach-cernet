package com.swust.kelab.test.service;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.swust.kelab.domain.User;
import com.swust.kelab.model.CommonQuery;
import com.swust.kelab.model.QueryData;
import com.swust.kelab.model.TeacherModel;
import com.swust.kelab.model.UserSearchInfoModel;
import com.swust.kelab.repos.UserDAO;
import com.swust.kelab.service.UserService;

public class UserServiceTest {

	private UserService userService;
	private UserDAO userDAO;
	@Before
	public void init() {
		ApplicationContext ctx = 
				new ClassPathXmlApplicationContext(
						new String[]{"classpath:spring/applicationContext.xml",
									 "classpath:spring/dao.xml",
									 "classpath:spring/service.xml"});
		userService = ctx.getBean(UserService.class);
		userDAO = ctx.getBean(UserDAO.class);
	}
	
	//@Test
	public void userTest() {
		if(userService != null) {
			User user = userService.findOneUser(1);
			System.out.println(user);
		}
	}
	//@Test
	public void TestQueryUser(){
		String ids="7,8";
		List id = Arrays.asList(ids.split(","));
		List<TeacherModel> userList = userDAO.findUsersByUserIds(id);
		System.out.println(userList.get(0).getTeacherName());
	}
	
	//@Test
	public void testSearchUser()
	{
		UserSearchInfoModel usif = new UserSearchInfoModel();
		usif.setDeptId(2);
		usif.setType(2);
		usif.setCourseId(22);
		CommonQuery com = new CommonQuery();
		try {
			@SuppressWarnings("unused")
			QueryData q = userService.searchUsersInfo(com,usif);
			
			System.out.println();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	//@Test
	public void viewUsersByRoleTest(){
	    int roleId=1;
	    int depaId=3;
	    List<User> users=userService.viewUsersByRole(roleId,depaId);
	    if(users!=null){
	        for (User user : users) {
                System.out.println(user.getUserId()+"\t"+user.getUserRealname());
            }
	    }
	}
	
	//@Test
    public void viewUsersByTypeTest(){
        int userType=2;
        int depaId=4;
        List<User> users=userService.viewUsersByTypeAndDepa(userType,depaId);
        if(users!=null){
            for (User user : users) {
                System.out.println(user.getUserId()+"\t"+user.getUserRealname());
            }
        }
    }
    
    @Test
    public void viewAllMajorTeachers(){
        int teaId=404102;
        List<TeacherModel> users=userService.viewAllMajorTeachers(teaId);
        if(users!=null){
            System.out.println("教师数："+users.size());
            for (TeacherModel user : users) {
                System.out.println(user.getTeacherId()+"\t"
                        +user.getTeacherName()+"\t"
                        +user.getSchoolName()+"\t"
                        +user.getTeacherRemark()+"\t"
                        +user.getCourseNames()+"\t");
            }
        }
    }
}
