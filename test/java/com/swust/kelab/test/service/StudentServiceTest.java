package com.swust.kelab.test.service;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.swust.kelab.domain.NoticeAnnouncement;
import com.swust.kelab.model.BBSReplyModel;
import com.swust.kelab.model.CommonQuery;
import com.swust.kelab.model.CourseModel;
import com.swust.kelab.model.QueryData;
import com.swust.kelab.model.SelectCourseModel;
import com.swust.kelab.model.StudentInfoModel;
import com.swust.kelab.service.StudentService;

public class StudentServiceTest {
	private StudentService studentService;

	@Before
	public void init() {
		ApplicationContext ctx = 
				new ClassPathXmlApplicationContext(
						new String[]{"classpath:spring/applicationContext.xml",
								"classpath:spring/dao.xml",
						"classpath:spring/service.xml"});
		studentService = ctx.getBean(StudentService.class);
	}

	@Test
	public void userTest() {
		try {
			@SuppressWarnings("unused")
			List<SelectCourseModel> list = studentService.studentPage(2);

			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

/*	@Test
	public void courseTest()
	{
		try {
			CommonQuery cq = new CommonQuery();
			@SuppressWarnings("unused")
			QueryData qd = studentService.selectCourseInfo(cq, "");

			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	@Test
	public void studentTest()
	{
		try {
			@SuppressWarnings("unused")
			StudentInfoModel s = studentService.studentInfo(2);

			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void annouTest()
	{
		try {
			@SuppressWarnings("unused")
			List<NoticeAnnouncement> list = studentService.announceInfo();

			System.out.println();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void bbsReplyTest()
	{
		try {
			@SuppressWarnings("unused")
			List<BBSReplyModel> bbs = studentService.userReply(2);

			System.out.println();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void selectOneTermTest()
	{
		try {
			@SuppressWarnings("unused")
			CourseModel scm = studentService.studentCentainCourse(43);

			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void selectOneCourseTest()
	{
		try {
//			@SuppressWarnings("unused")
//			Map<String ,Object> map = studentService.searchOneCourse(1);

//			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*	@Test
	public void selectCourseByNameTest()
	{
		try {
			@SuppressWarnings("unused")
			Map<String ,Object> map = studentService.requestCourseByName("程序员");

			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

}
