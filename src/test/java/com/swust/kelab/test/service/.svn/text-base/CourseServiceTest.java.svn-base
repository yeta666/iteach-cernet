package com.swust.kelab.test.service;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.swust.kelab.domain.Course;
import com.swust.kelab.domain.CourseSelectInformation;
import com.swust.kelab.domain.User;
import com.swust.kelab.model.CommonQuery;
import com.swust.kelab.model.CourseBbsModel;
import com.swust.kelab.model.CourseListModel;
import com.swust.kelab.model.CourseModel;
import com.swust.kelab.model.CourseSelectInfoModel;
import com.swust.kelab.model.PageData;
import com.swust.kelab.model.QueryData;
import com.swust.kelab.model.TeacherCourseModel;
import com.swust.kelab.service.CourseService;
import com.swust.kelab.utils.JsonUtil;

public class CourseServiceTest {

	private CourseService courseService;

	@Before
	public void init() {
		ApplicationContext ctx = new ClassPathXmlApplicationContext(
				new String[] { "classpath:spring/applicationContext.xml",
						"classpath:spring/dao.xml",
						"classpath:spring/service.xml" });
		courseService = ctx.getBean(CourseService.class);
	}
	
	//@Test()
	public void viewAllCourseByTeacherTest() {
		List data = courseService.viewAllCourseByTeacher(2,1);
		System.out.println("**********************\n" + JsonUtil.getJSON(data));
	}

	/**
	 * @author EasonLian
	 */
	// @Test()
	public void viewAllCourseTest() {
		Map data = courseService.viewAllCourseList(null);
		System.out.println("**********************\n" + JsonUtil.getJSON(data));
	}

	// @Test()
	public void resourceTest() {
		List<CourseModel> list = courseService.viewCourseListService("",2,1, true);
		System.out.println(JsonUtil.getJSON(list));
		// System.out.println("\n------   "+list.size());
		// for(CourseModel r : list) {
		// System.out.println(r.getCourName()+"    *****   "+r.getChapters().size());
		// }
	}

	//@Test
	public void viewCoursesByDepartIdTest() {
		int departId = 0;
		int type=2;
		List<Map> courses = courseService.viewCoursesByDepartId(departId,type);
		System.out.println(courses.size());
		if (courses != null) {
			for (Map map : courses) {
				System.out.println(map.get("courseId") + "\t"
						+ map.get("courseName"));
			}
		}
		System.out.println("end");
	}

	//@Test
	public void viewCourseListByUserIdTest() {
		int userId = 1;
		int type=1;
		List<Map> courses = courseService.viewCoursesByUserId(userId,type);
		if (courses != null) {
			for (Map map : courses) {
				System.out.println(map.get("courseId") + "\t"
						+ map.get("courseName"));
			}
		}
	}

	/**
	 * 测试课程注册
	 * 
	 * @author 张鑫
	 */
	// @Test
	public void testRegisterCourse() {
		Course c = new Course();
		c.setCourTepaId(1);
		c.setCourName("this is just an test course");
		c.setCourCode("1111111111");
		c.setCourCreateUserid(2);
		c.setCourCredit(1.2f);
		c.setCourVerify(0);
		c.setCourCreateTime(new Date());
		// int info = courseService.registerCourse(c);
	}

	// @Test
	public void viewCourseBbsListTest() {
		CommonQuery query = new CommonQuery();

		int departId = 4;
		QueryData staResult = courseService.viewCourseBbsList(query, departId);
		if (staResult != null) {
			System.out.println("totalCount:" + staResult.getTotalCount());
			System.out.println("totalPage:" + staResult.getTotalPage());
			List<PageData> datas = staResult.getPageData();
			if (datas == null) {
				System.out.println("空数据");
				return;
			}
			for (PageData pageData : datas) {
				System.out.println("pageNum:" + pageData.getPage());
				List<CourseBbsModel> ttsms = (List<CourseBbsModel>) pageData
						.getData();
				System.out.println(ttsms.size());
				for (CourseBbsModel ttsm : ttsms) {
					System.out.println(ttsm.getCourseName() + "\t"
							+ ttsm.getCourseCode() + "\t" + ttsm.getModerator()
							+ "\t" + ttsm.getBbsStatus());
				}
			}
		}
		System.out.println("end");
	}

	// @Test
	public void setCourseBbsStatusTest() {
		String courseIds = "1,2";
		int newStatus = 2;
		System.out.println(courseService.setCourseBbsStatus(courseIds,
				newStatus));
	}
//
//	// 无权限的
//	// @Test
//	public void setCourseTeaIds() {
//		User user = new User();
//		user.setUserId(43);
//		try {
//			Map<String, Object> map = courseService.setTeacherToCourse(20,
//					"43,44,45,", "46,47,48");
//
//			Set<String> set = map.keySet();
//
//			for (String s : set) {
//				System.out.println("结果是" + map.get(s));
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	// 有权限的 ,添加主讲教师
//	// @Test
//	public void setCourseTeaIds_2() {
//		User user = new User();
//		user.setUserId(82);
//		try {
//			Map<String, Object> map = courseService.setTeacherToCourse(20,
//					"26,31,43,44,45,", "");
//
//			Set<String> set = map.keySet();
//
//			for (String s : set) {
//				System.out.println(s + ":" + map.get(s));
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	// 添加辅导老师
//	// @Test
//	public void setCourseTeaIds_3() {
//		User user = new User();
//		user.setUserId(82);
//		try {
//			Map<String, Object> map = courseService.setTeacherToCourse(20, "",
//					"26,31,43,44,45,");
//
//			Set<String> set = map.keySet();
//
//			for (String s : set) {
//				System.out.println(s + ":" + map.get(s));
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	// 即添加辅导教师，也添加主讲教师
//	// @Test
//	public void setCourseTeaIds_4() {
//		User user = new User();
//		user.setUserId(82);
//		try {
//			Map<String, Object> map = courseService.setTeacherToCourse(20,
//					"31,43,44,45,", "26,31,43,44,45,");
//
//			Set<String> set = map.keySet();
//
//			for (String s : set) {
//				System.out.println(s + ":" + map.get(s));
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}

	// @Test
	public void findCourseSelectInfo() {
		CourseSelectInformation csin = new CourseSelectInformation();
		csin.setCsinCourId(13);
		csin.setCsinDepaId(23);

		List<CourseSelectInfoModel> list = courseService
				.findCourseSelectInfo(csin);

		for (CourseSelectInfoModel C : list) {
			System.out.println("课程代码:" + C.getCourseCode() + ",课程选课关闭时间:"
					+ C.getCloseTime().toGMTString() + ",部门名称:"
					+ C.getDepartName());
		}

	}

	// @Test
	public void findCourseSelectInfo_2() {
		CourseSelectInformation csin = new CourseSelectInformation();
		// csin.setCsinCourId(13);
		csin.setCsinDepaId(21);

		List<CourseSelectInfoModel> list = courseService
				.findCourseSelectInfo(csin);
		SimpleDateFormat sf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");

		for (CourseSelectInfoModel C : list) {
			SimpleDateFormat formatter = new SimpleDateFormat(
					"YYYY-MM-DD HH:mm:ss");

			String dateString = formatter.format(C.getCloseTime());
			ParsePosition pos = new ParsePosition(8);
			Date currentTime_2 = formatter.parse(dateString, pos);
			System.out.println("课程代码:" + C.getCourseCode() + ",课程选课关闭时间:"
					+ currentTime_2 + ",部门名称:" + C.getDepartName());
		}

	}

	//@Test
	public void findCourseSelectInfo_3() {
		CourseSelectInformation csin = new CourseSelectInformation();
		 csin.setCsinCourId(1);
		 csin.setCsinDepaId(1);

		List<CourseSelectInfoModel> list = courseService
				.findCourseSelectInfo(csin);

		for (CourseSelectInfoModel C : list) {
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");

			System.out.println("课程代码:" + C.getCourseCode() + ",课程选课关闭时间:"
					+ formatter.format(C.getCloseTime()) + ",部门名称:"
					+ C.getDepartName() + ",是否有效" + C.getCourseOn());
		}

	}

	// @Test
	public void testDate() {
		Date currentTime = new Date();
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(formatter.format(currentTime));
	}

	//@Test
	public void setCourseSelectTime() {
		
		Map<String, Object> data = courseService.setCourseSelectTime("13,14,15", new Date(2013 - 1900, 4, 16, 10, 45, 00),new Date(2013 - 1900, 6, 16, 10, 45, 00),"");
		Set<String> set = data.keySet();

		for (String s : set) {
			System.out.println(s + ":" + data.get(s));
		}
	}

	//@Test
	public void setCourseSelectTime_2() {
		/*CourseSelectInformation csin = new CourseSelectInformation();
		csin.setCsinCourId(13);
		csin.setCsinDepaId(21);

		csin.setCsinOpentime(new Date());
		csin.setCsinClosetime(new Date(2013 - 1900, 7, 16, 10, 45, 00));

//		Map<String, Object> data = courseService.setCourseSelectTime(csin);
		Set<String> set = data.keySet();

		for (String s : set) {
			System.out.println(s + ":" + data.get(s));
		}*/
		
		
		String str = "1";
		
		String[] strings = str.split(",");
		
		System.out.println(strings.length);
		
		
		
	}

	// @Test
	public void setCourseSelectTime_3() {
		CourseSelectInformation csin = new CourseSelectInformation();
		csin.setCsinCourId(14);
		csin.setCsinDepaId(23);

		csin.setCsinOpentime(new Date());
		csin.setCsinClosetime(new Date(2013 - 1900, 7, 16, 10, 45, 00));

		/*Map<String, Object> data = courseService.setCourseSelectTime(csin);
		Set<String> set = data.keySet();

		for (String s : set) {
			System.out.println(s + ":" + data.get(s));
		}*/
	}

	//@Test
	public void deleteCourseSelectTime_1() {

		String courseIds = "13,14";

		Map<String, Object> data = courseService
				.deleteCourseSelectTime(courseIds);

		Set<String> set = data.keySet();
		for (String s : set) {
			System.out.println(s + ":" + data.get(s));
		}
	}

	//@Test
	public void deleteCourseSelectTime_2() {

		String departIds = "4";

		Map<String, Object> data = courseService
				.deleteCourseSelectTime(departIds);

		Set<String> set = data.keySet();
		for (String s : set) {
			System.out.println(s + ":" + data.get(s));
		}
	}

	// @Test
	public void testNull() {
		Map<String, Object> map = new HashMap<String, Object>();
		System.out.println(map.get("test") == null);
	}

	//@Test
	public void testQueryCheckCouse() {
		CommonQuery query = new CommonQuery();
		query.setSearchType(3);
		query.setSearchWord("0");
		int departmentId = 0;
		// courseService.queryCourseOfCheck(query,departmentId);
		courseService.queryOneCheckCourse(2);
	}

	//@Test
	public void insertAllCourseInfo_1() {

		String courseIds = "1,2,3,4";
		Integer departId = 1;

		Map<String, Object> map = courseService.setAllCourseSelectTime(
				courseIds, departId, new Date(2013 - 1900, 5, 16, 10, 45, 00),
				new Date(2013 - 1900, 7, 16, 10, 45, 00));

		Set<String> set = map.keySet();

		for (String s : set) {
			System.out.println(s + ":" + map.get(s));
		}
	}

	//@Test
	public void insertAllCourseInfo_2() {

		String courseIds = "1,2,3,4";
		Integer departId = 1;

		Map<String, Object> map = courseService.setAllCourseSelectTime(
				courseIds, departId, new Date(2013 - 1900, 7, 16, 10, 45, 00),
				new Date());

		Set<String> set = map.keySet();

		for (String s : set) {
			System.out.println(s + ":" + map.get(s));
		}
	}
	
	@Test
    public void viewCourseListByQueryTest(){
        CommonQuery query=new CommonQuery();
        //query.setSearchType(2);
        //query.setSearchWord("79");
        int departId=0;
        int courseCateId=0;
        String courseType="IA";
        QueryData staResult=courseService.viewCourseListByQuery(query, departId, courseCateId,courseType);
        if(staResult!=null){
            System.out.println("totalCount:"+staResult.getTotalCount());
            System.out.println("totalPage:"+staResult.getTotalPage());
            List<PageData> datas=staResult.getPageData();
            if(datas==null){
                System.out.println("空数据");
                return;
            }
            for (PageData pageData : datas) {
                System.out.println("pageNum:"+pageData.getPage());
                List<CourseListModel> ttsms=(List<CourseListModel>)pageData.getData();
                System.out.println(ttsms.size());
                for (CourseListModel ttsm : ttsms) {
                    System.out.println(ttsm.getCourName()+"\t"+
                            ttsm.getCourCode()+"\t"+
                            ttsm.getCourCredit()+"\t"+
                            ttsm.getDepartName()+"\t"+
                            ttsm.getCateNames()
                           );
                }
            }
        }
        System.out.println("end");
    }
	
    //@Test
	public void createCourseTest(){
	    Course c=new Course();
	    c.setCourCateids("a1a");
	    c.setCourCode("1234");
	    //c.setCourCoverpictureid(1);
	    c.setCourCreateUserid(2);
	    c.setCourCredit(3.0f);
	    c.setCourDescribe("hehehe1");
	    c.setCourName("nini");
	    c.setCourTepaId(1);
	    c.setCourTimeSchedule("zai继续耍");
	    System.out.println(courseService.createCourse(c));
	}
	//@Test
    public void checkOneCourse(){
//        int courseId = 20;
//        int status = 1;
//        courseService.checkCourse(courseId, status);
    }
	
	//@Test
	public void deleteCoursesTest(){
	    String courseIds="19,20";
	    String result=courseService.deleteCourses(courseIds);
	    System.out.println(result);
	}
	
	//@Test
	public void viewCourseDetailTest(){
	    int courseId=1;
	    Map result=courseService.viewCourseDetail(courseId);
	    if(result!=null){
	        Set keys=result.keySet();
	        for (Object id : keys) {
                System.out.println(id.toString()+"\t"+result.get(id));
            }
	    }
	}
	
	//@Test
	public void modifyCourseTest(){
	    Course c=new Course();
	    c.setCourId(22);
        c.setCourCateids("a1a5");
        c.setCourCode("123455");
        c.setCourCoverpictureid(2);
        c.setCourCreateUserid(2);
        c.setCourCredit(3.0f);
        c.setCourDescribe("hehehe1");
        c.setCourName("nininini");
        c.setCourTepaId(1);
        c.setCourTimeSchedule("zaizai继续耍");
        System.out.println(courseService.modifyCourse(c));
	}
	
	//@Test
	public void fooo()
	{
		List<TeacherCourseModel> list = courseService.viewAllTeacherCourseInfo(4);
		
		System.out.println("dsfsdf");
	}

}
