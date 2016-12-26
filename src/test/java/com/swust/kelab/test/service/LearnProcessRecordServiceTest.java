package com.swust.kelab.test.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.swust.kelab.service.LearnProcessRecordService;

public class LearnProcessRecordServiceTest {
	private LearnProcessRecordService studentService;

	@Before
	public void init() {
		ApplicationContext ctx = 
				new ClassPathXmlApplicationContext(
						new String[]{"classpath:spring/applicationContext.xml",
								"classpath:spring/dao.xml",
						"classpath:spring/service.xml"});
		studentService = ctx.getBean(LearnProcessRecordService.class);
	}
	
//	@Test
	public void addRecord() {
		try {
			int test = studentService.insertLearnProcessRecord(2, 3,2);
			System.out.println(test);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void updateRecord() {
		studentService.recordLearnProcess(12 ,2, 3, 5641,13);
	}
}
