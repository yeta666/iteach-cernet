package com.swust.kelab.service;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.swust.kelab.domain.NoticeAnnouncement;
import com.swust.kelab.domain.ReSelectCourse;
import com.swust.kelab.domain.User;
import com.swust.kelab.model.BBSReplyModel;
import com.swust.kelab.model.CommonQuery;
import com.swust.kelab.model.CourseModel;
import com.swust.kelab.model.PageData;
import com.swust.kelab.model.QueryData;
import com.swust.kelab.model.SelectCourseModel;
import com.swust.kelab.model.StudentInfoModel;
import com.swust.kelab.model.TeacherInfoModel;
import com.swust.kelab.repos.BBSDAO;
import com.swust.kelab.repos.CourseCateDAO;
import com.swust.kelab.repos.CourseDAO;
import com.swust.kelab.repos.NoticeAnnouncementDAO;
import com.swust.kelab.repos.ReSelectCourseDAO;
import com.swust.kelab.repos.UserDAO;
import com.swust.kelab.repos.bean.ListQuery;
import com.swust.kelab.utils.FormatUtil;

/**
 * 学生用户相关服务层
 * 
 * @author ZhangXin
 * 
 */
@Service("studentService")
public class StudentService {
	private final static Logger LOGGER = LoggerFactory
			.getLogger("com.registerController");

	@Resource(name = "reSelectCourseDao")
	private ReSelectCourseDAO reSelectCourseDao;

	@Resource(name = "announcementDao")
	private NoticeAnnouncementDAO noticeAnnouncementDao;

	@Resource(name = "courseDao")
	private CourseDAO courseDao;

	@Resource(name = "bbsDao")
	private BBSDAO bbsDao;

	@Autowired
	private UserDAO userDao;

	private SystemParameterService systemParameterService;

	@Autowired
	public void setSystemParameterService(
			SystemParameterService systemParameterService) {
		this.systemParameterService = systemParameterService;
	}

	@Autowired
	private CourseCateDAO courseCateDAO;

	public CourseCateDAO getCourseCateDAO() {
		return courseCateDAO;
	}

	public void setCourseCateDAO(CourseCateDAO courseCateDAO) {
		this.courseCateDAO = courseCateDAO;
	}

	/**
	 * 查询某个学生的某一门课程的详细信息
	 * 
	 * @author zhangxin
	 * @param userId
	 *            学生id
	 * @param courseId
	 *            课程id
	 * @return SelectCourseModel
	 * @throws Exception
	 */
	@Transactional
	public CourseModel studentCentainCourse(int reselectId) throws Exception {
		CourseModel rsc = null;
		rsc = reSelectCourseDao.findSelectedCertainCourse(reselectId);
		return rsc;
	}

	/**
	 * 通过学生id查询学生选课情况
	 * 
	 * @author zhangxin
	 * @param id
	 * @return List<SelectCourseModel>
	 * @throws Exception
	 */
	@Transactional
	public List<SelectCourseModel> studentPage(int id) throws Exception {
		List<SelectCourseModel> rsc = null;
		rsc = reSelectCourseDao.findSelectCourseByUser(id);
		return rsc;
	}

	/**
	 * 查询所有课程信息(分页需求)
	 * 
	 * @author zhangxin
	 * @return List<CourseModel>
	 * @throws Exception
	 */
	@Transactional
	public QueryData selectCourseInfo(CommonQuery commonquery,
			String projectPath, Integer userId, Integer cateId,
			Integer departId, Date openTime, Date closeTime) throws Exception {

		QueryData queryData = new QueryData();
		// 构造查询条件
		ListQuery query = commonquery.format();
		query.fill("userId", userId);
		query.fill("cateId", cateId);
		query.fill("openTime", openTime);
		query.fill("closeTime", closeTime);
		query.fill("departmentId", departId);
		String search = commonquery.getSearchWord();
		if (null != search)
			search = "%" + search + "%";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("search", search);
		map.put("cateId", cateId);
		map.put("openTime", openTime);
		map.put("closeTime", closeTime);
		map.put("departmentId", departId);

		query.fill("search", search);

		int totalCount = courseDao.findCourseCount(map);
		queryData.setTotalCount(totalCount);
		if (totalCount == 0) {
			return queryData;
		}
		if (commonquery.getRecordPerPage() <= 0) {
			commonquery.setRecordPerPage(6);
		}
		query.fill("maxCount", commonquery.getRecordPerPage());
		int totalPage = QueryData.computeTotalPage(totalCount,
				commonquery.getRecordPerPage());
		queryData.setTotalPage(totalPage);
		List<PageData> pageDataList = Lists.newArrayList();
		// 未指定页数，则只读取前三页数据
		if (commonquery.getPageArray() == null) {
			commonquery.setPageArray(new int[] { 1, 2, 3 });
		}
		// 分别获取每页的数据
		for (int i = 0; i < commonquery.getPageArray().length; i++) {
			int pagef = commonquery.getPageArray()[i];
			if (pagef <= 0 || pagef > totalPage) {
				continue;
			}
			query.fill(
					"startIndex",
					QueryData.computeStartIndex(pagef,
							commonquery.getRecordPerPage()));
			List<CourseModel> courseModel = reSelectCourseDao
					.findCourseByName(query);

			ListIterator<CourseModel> itea = courseModel.listIterator();
			while (itea.hasNext()) {
				CourseModel cm = itea.next();
				//转换课程类别
                String courType=cm.getCourType();
                if(courType.equals("B")){
                    cm.setCourType("必修");
                }else
                if(courType.equals("XIA")){
                    cm.setCourType("选修IA");
                }else
                if(courType.equals("XIB")){
                    cm.setCourType("选修IB");
                }else
                if(courType.equals("XII")){
                    cm.setCourType("选修II");
                }
				// 课程主讲老师列表
				String courTeacherIds = cm.getCourTeacherIds();
				// 课程类别列表
				String courCatesIds = cm.getCourCateIds();
				if (courTeacherIds != null && !courTeacherIds.equals("")) {
					// 查询课程主讲教师
					courTeacherIds = removeSuffixAndPreffix(courTeacherIds);
					List<TeacherInfoModel> courTeaList = courseDao
							.viewAllCourTeacherByCourse(courTeacherIds);
					String teacherNames = "";
					for (TeacherInfoModel teachName : courTeaList) {
						if (null != teachName.getUserCoverPictureId()
						        &&teachName.getUserCoverPictureId()>0)
							teacherNames += "../../"
									+ teachName.getFileLocation()
									+ teachName.getFileName() + "$&$"
									+ teachName.getUserRealname() + "$&$"
									+ teachName.getUserRemark() + "$&$"
									+ teachName.getUserId() + "$$$";
						else {
							teacherNames += teachName.getUserRealname() + "$&$"
									+ teachName.getUserRemark() + "$&$"
									+ teachName.getUserId() + "$$$";
						}
					}
					/*
					 * if (teacherNames != "") teacherNames =
					 * teacherNames.substring(0, teacherNames.length() - 1);
					 */
					cm.setCourTeachers(teacherNames);
				} else {
					cm.setCourTeachers("");
				}

				if (courCatesIds != null && !courCatesIds.equals("")) {
					// 查询课程类型
					courCatesIds = removeSuffixAndPreffix(courCatesIds);
					List<String> courCateList = courseCateDAO
							.viewCocaNameByIds(courCatesIds);
					String courCateName = "";
					for (String cateName : courCateList)
						courCateName += (cateName + "，");
					if (courCateName != "")
						courCateName = courCateName.substring(0,
								courCateName.length() - 1);
					cm.setCourCates(courCateName);
				} else {
					cm.setCourCates("");
				}
			}

			addCourseImge(courseModel, projectPath);
			formateCourseOpenAndCloseDate(courseModel);
			pageDataList.add(new PageData(pagef, courseModel));
		}
		// 装载返回结果
		queryData.setPageData(pageDataList);
		return queryData;
	}

	private void addCourseImge(List<CourseModel> course, String projectPath) {
		Iterator<CourseModel> it = course.iterator();
		while (it.hasNext()) {
			CourseModel cm = it.next();
			// 判断路径是否存在，不存在文件，返回默认图片
			String relativePath = projectPath + "/" + cm.getCourImg() + cm.getFileName();
			if (!new File(relativePath).exists()) {
				cm.setCourImg("upload/eduman/coursepic.jpg");
				cm.setFileName("");
			}
		}
	}

	private void formateCourseOpenAndCloseDate(List<CourseModel> courseModel) {
		if (courseModel != null) {
			for (int index = 0; index < courseModel.size(); index++) {
				CourseModel course = courseModel.get(index);
				course.setOpentTime(FormatUtil.formatDate(course.getOpenDate()));
				course.setCloseTime(FormatUtil.formatDate(course.getCloseDate()));
			}
		}
	}

	@Transactional
	public StudentInfoModel studentInfo(int id) throws Exception {
		return reSelectCourseDao.findStudentInfo(id);
	}

	@Transactional
	public List<NoticeAnnouncement> announceInfo() throws Exception {
		return noticeAnnouncementDao.findAnnouncement();
	}

	@Transactional
	public List<BBSReplyModel> userReply(int id) throws Exception {
		return bbsDao.findReply(id);
	}

	/**
	 * 退选课程
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public Map<String, Object> deleteSelectCourceById(Map<String, Object> id)
			throws Exception {
		Map<String, Object> deleteSelCourInfo = new HashMap<String, Object>();

		// 用以判断用户类型是否为学生（仅仅限制学生选课时间和选课学分总数）
		int userId = (Integer) id.get("userId");
		User user = userDao.findOneUser(userId);
		int userType = (user == null ? -1 : user.getUserType());

		if (id.get("courseId") == null) {
			deleteSelCourInfo.put("deleteSelCourInfo", "请选择需要退选的课程");
			deleteSelCourInfo.put("status", 1);
			return deleteSelCourInfo;
		}
		LOGGER.debug("delete select course by id");

		// 查询当前课程是否在选课时间范围内
		Integer row = courseDao.findCourseSelecIdBytime(id);
		if (userType == User.USERTYPE_STUDENT && row == null) {
			deleteSelCourInfo.put("deleteSelCourInfo", "退课失败，不在退课时间内不能进行退课");
			deleteSelCourInfo.put("status", 2);
			return deleteSelCourInfo;
		}
		int result = reSelectCourseDao.changeScValidState(id);
		if (result == 0) {
			deleteSelCourInfo.put("deleteSelCourInfo", "退课失败");
			deleteSelCourInfo.put("status", 2);
		} else {
			id.put("type", 2);
			courseDao.setCourseChooseNum(id);
			deleteSelCourInfo.put("deleteSelCourInfo", "退课成功!!!");
			deleteSelCourInfo.put("status", 3);
		}
		return deleteSelCourInfo;
	}

	/**
	 * 选课
	 * 
	 * @param rsc
	 * @author LuoHui
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public Map<String, Object> insertSelectCource(Map<String, Object> queryData)
			throws Exception {
		Map<String, Object> insertSelCourInfo = new HashMap<String, Object>();

		// 用以判断用户类型是否为学生（仅仅限制学生选课时间和选课学分总数）
		int userId = (Integer) queryData.get("userId");
		User user = userDao.findOneUser(userId);
		int userType = (user == null ? -1 : user.getUserType());

		if (queryData.get("userId").equals(new Integer(0))
				|| queryData.get("courseId").equals(new Integer(0))) {
			insertSelCourInfo.put("insertSelCourInfo", "用户或者课程信息错误");
			insertSelCourInfo.put("status", "1");
			return insertSelCourInfo;
		}
		ReSelectCourse sc=reSelectCourseDao.findOneCourceById(queryData);
		if (sc != null&&sc.getRscoValid()==1) {
			insertSelCourInfo.put("insertSelCourInfo", "该科目已经选修，请选修其他科目");
			insertSelCourInfo.put("status", "2");
			return insertSelCourInfo;
		}
		// 查询当前课程是否在选课时间范围内
		Date courrentDate = new Date();
		queryData.put("courrentDate", courrentDate);
		Integer id = courseDao.findCourseSelecIdBytime(queryData);
		if (userType == User.USERTYPE_STUDENT && id == null) {// 仅仅针对学生用户
			insertSelCourInfo.put("insertSelCourInfo", "该课程还未到选课时间，不能选课");
			insertSelCourInfo.put("status", "2");
			return insertSelCourInfo;
		}
		// 选课学分限制
		String courseMaxCredit = systemParameterService
				.attainValueByEnName("courseMaxCredit");
		Integer credit = null;

		if (courseMaxCredit.matches("\\d+$")) {
			credit = Integer.parseInt(courseMaxCredit);
		}

		Integer selectedCredit = reSelectCourseDao
				.selectCreditOfStudent(queryData);
		Integer courseCredit = (Integer) queryData.get("courseCredit");
		if (userType == User.USERTYPE_STUDENT
				&& credit < selectedCredit + courseCredit) {
		    String info="您的学分剩余不足，您只能选择"
                    + (credit - selectedCredit) + "以下学分的课程";
		    if(credit - selectedCredit<=0){
		        info="您已没有剩余的学分数，无法完成选课！";
		    }
			insertSelCourInfo.put("insertSelCourInfo",info );
			insertSelCourInfo.put("status", "5");
			return insertSelCourInfo;
		}
		//开始加入选课记录
		int result;
		if(sc!=null&&sc.getRscoValid()==2){
		    //以前选过，只是退选了，只需修改相应字段的值
		    Map query=new HashMap();
		    query.put("rscoId", sc.getRscoId());
		    query.put("valid", 1);
		    result=reSelectCourseDao.changeScValidState(query);
		}else{
		    ReSelectCourse rsc = new ReSelectCourse();
	        rsc.setRscoUserId((Integer) queryData.get("userId"));
	        rsc.setRscoCourId((Integer) queryData.get("courseId"));
	        rsc.setRscoTime(courrentDate);
	        rsc.setRscoVerify(1);
	        ReSelectCourse reSc=userDao.viewUserScInfo(user);
	        rsc.setRscoDepartName(reSc.getRscoDepartName());
	        if(user.getUserType()==1){
	            rsc.setRscoClassName(reSc.getRscoClassName());
	            rsc.setRscoGradeId(reSc.getRscoGradeId());
	            rsc.setRscoGradeName(reSc.getRscoGradeName());
	        }
	        LOGGER.debug("insert selectCourse");
	        result = reSelectCourseDao.insertSelectCourse(rsc);
		}
		if (result == 0) {
            insertSelCourInfo.put("insertSelCourInfo", "选修失败，请重新选修该科目!");
            insertSelCourInfo.put("status", "3");
        } else {
            queryData.put("type", 1);
            courseDao.setCourseChooseNum(queryData);
            insertSelCourInfo.put("insertSelCourInfo", "该科目选修成功！");
            insertSelCourInfo.put("status", "4");
        }
		return insertSelCourInfo;
	}

	/**
	 * 查找特定课程（通过课程id）status 1 ：查询成功 2 ：查询失败
	 * 
	 * @author ZhangXin
	 * @param courseId
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	public Map<String, Object> searchOneCourse(int courseId, String projectPath)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		CourseModel cm = reSelectCourseDao.findOneCourse(courseId);
		// 判断路径是否存在，不存在文件，返回默认图片
		String relativePath = projectPath + cm.getCourImg() + cm.getFileName();
		if (!new File(relativePath).exists()) {
			cm.setCourImg("upload/eduman/coursepic.jpg");
			cm.setFileName("");
			cm.setImgLocation("");
		}
		if (null != cm) {
			map.put("status", 1);
			map.put("value", cm);
		} else
			map.put("status", 2);
		return map;
	}

	/**
	 * 查询课程（通过课程名）
	 * 
	 * @author ZhangXin
	 * 
	 * @param courseName
	 * @return
	 * @throws Exception
	 */
	/*
	 * public Map<String, Object> requestCourseByName(String courseName) throws
	 * Exception { Map<String,Object> map = new HashMap<String,Object>();
	 * List<CourseModel> courses =
	 * reSelectCourseDao.findCourseByName(courseName); if(null == courses) {
	 * map.put("status", 1); } else { map.put("status", 2); map.put("value",
	 * courses); } return map; }
	 */
	/**
	 * 去除参数前或后多余的逗号
	 * 
	 * @param args
	 * @author easonlian
	 */
	private static String removeSuffixAndPreffix(String args) {
		if (args == null)
			return null;
		if (args.startsWith(","))
			args = args.substring(1, args.length());
		if (args.endsWith(","))
			args = args.substring(0, args.length() - 1);
		return args;
	}

	/**
	 * 获取服务器状态
	 * 
	 * @author MrYang
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	public Map<String, Object> serverStatus() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		int kb = 1024 * 1024;
		// 可使用内存
		long totalMemory = Runtime.getRuntime().totalMemory() / kb;
		// 剩余内存
		long freeMemory = Runtime.getRuntime().freeMemory() / kb;
		// 最大可使用内存
		long maxMemory = Runtime.getRuntime().maxMemory() / kb;
		// 操作系统
		String osName = System.getProperty("os.name");
		// 用户当前目录
		String userHome = System.getProperty("user.home");
		// JDK版本
		String jkdVersion = System.getProperty("java.specification.version");
		// JDK路径
		String jkdPath = System.getProperty("java.home");
		// 当前程序主目录
		String appDir = System.getProperty("user.dir");
		// 操作系统类型
		String osType = System.getProperty("os.arch");
		// 操作系统内部版本号
		String osVersion = System.getProperty("os.version");
		// 服务器IP
		InetAddress myIPaddress = null;
		try {
			myIPaddress = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String serIPAddr = myIPaddress.getHostAddress();
		// 系统存储容量
		// String
		// appdir=ServletActionContext.getServletContext().getRealPath("/");
		map.put("result", "1");
		map.put("totalmemory", totalMemory);
		map.put("freememory", freeMemory);
		map.put("maxmemory", maxMemory);
		map.put("osname", osName);
		map.put("userhome", userHome);
		map.put("jdkversion", jkdVersion);
		map.put("jdkpath", jkdPath);
		// map.put("appdir", appdir);
		map.put("ostype", osType);
		map.put("osversion", osVersion);
		map.put("servipaddr", serIPAddr);
		return map;

	}
}
