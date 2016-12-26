package com.swust.kelab.web.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.support.ServletContextResource;

import com.swust.kelab.domain.Course;
import com.swust.kelab.model.CommonQuery;
import com.swust.kelab.model.CourseStatisticModel;
import com.swust.kelab.model.LearningProgressModel;
import com.swust.kelab.model.QueryData;
import com.swust.kelab.service.CourseStatisticService;
import com.swust.kelab.service.LogDBService;
import com.swust.kelab.service.SelectCourseService;
import com.swust.kelab.utils.CookieUtil;
import com.swust.kelab.web.json.JsonAndView;

/**
 * 课程统计的控制器，包括学习时间、学习次数，论坛交互次数和学习进度统计
 * 
 * @author 吴岘辉
 * 
 */
@Controller
@RequestMapping("/courseStatistic")
public class CourseStatisticController {
	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private SelectCourseService selectCourseService;

	@Autowired
	private CourseStatisticService courseStatisticService;

	@Autowired
	private LogDBService logDBService;

	/**
	 * 针对特定学生的课程统计（学习时间、学习次数和论坛交互次数）
	 * 
	 * @param request
	 *            请求对象
	 * @return 统计信息
	 * @see JsonAndView,CookieUtil,Course,CourseStatisticModel
	 */
	@RequestMapping(value = "/student", method = RequestMethod.POST)
	public JsonAndView staStuCourseLearning(HttpServletRequest request) {
		JsonAndView jav = new JsonAndView();

		// 获取学生id
		Cookie[] cookies = request.getCookies();
		String cookieValue = CookieUtil.findCookieValue(cookies, "user");
		if (cookieValue == null || cookieValue == "") {
			jav.setRet(false);
			jav.setErrcode(1);
			jav.setErrmsg("获取cookie失败！可能未登录或登录失效！");
			return jav;
		}
		String[] objArry = CookieUtil.legality(cookieValue, request);
		int stuId = Integer.parseInt(objArry[0]);
		// int stuId=1;

		// 获取相应的课程统计信息
		List<LearningProgressModel> staResult = courseStatisticService
				.staCourseLearnByStu(stuId);

		// 封装返回结果
		if (staResult != null) {
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("staResult", staResult);
			jav.addAllData(result);
		} else {
			jav.setRet(false);
			jav.setErrcode(2);
			jav.setErrmsg("课程统计失败！");
		}
		return jav;
	}

	/**
	 * 统计老师的课程辅导情况（论坛答疑次数，发起讨论次数）
	 * 
	 * @param query
	 *            查询对象 *
	 * @param departId
	 *            部门id
	 * @param courseId
	 *            课程id
	 * @param teacherName
	 *            教师名称
	 * @return
	 */
	@RequestMapping(value = "/teacher", method = RequestMethod.POST)
	public JsonAndView staTeacherTutorship(CommonQuery query, int departId,
			int courseId, String teacherName) {
		JsonAndView jav = new JsonAndView();
		QueryData staResult = courseStatisticService.staTeacherTutorship(query,
				departId, courseId, teacherName);
		if (staResult == null) {
			jav.setRet(false);
			jav.setErrcode(1);
			jav.setErrmsg("统计教师辅导情况失败！");
		} else {
			jav.addData("totalPage", staResult.getTotalPage());
			jav.addData("totalCount", staResult.getTotalCount());
			jav.addData("pageData", staResult.getPageData());
		}
		return jav;
	}

	/**
	 * 平台应用统计，按照学校分别统计（学生人数、教师人数、学习时间、学习次数和论坛交互次数）
	 * 
	 * @param query
	 *            基本查询条件，包括分页信息、时间区间查询和关键字查询
	 * @return 统计信息
	 * @see CommonQuery，QueryData
	 */
	@RequestMapping(value = "/platform", method = RequestMethod.POST)
	public JsonAndView staPlatformApplication(CommonQuery query) {
		JsonAndView jav = new JsonAndView();
		QueryData staResult = courseStatisticService
				.staPlatformApplication(query);
		if (staResult == null) {
			jav.setRet(false);
			jav.setErrcode(1);
			jav.setErrmsg("统计平台应用数据失败！");
		} else {
			jav.addData("totalPage", staResult.getTotalPage());
			jav.addData("totalCount", staResult.getTotalCount());
			jav.addData("pageData", staResult.getPageData());
		}
		return jav;
	}

	/**
	 * 学生的学习进度统计
	 * 
	 * @param userId
	 *            学生id
	 * @return 学习进度信息
	 * @see JsonAndView
	 */
	@RequestMapping(value = "/progress", method = RequestMethod.POST)
	public JsonAndView staLearningProgress(int userId) {
		JsonAndView jav = new JsonAndView();
		List<LearningProgressModel> staResult = courseStatisticService
				.staLearningProgress(userId);
		if (staResult == null) {
			jav.setRet(false);
			jav.setErrcode(1);
			jav.setErrmsg("统计学生学习进度信息失败！");
		} else {
			jav.addData("progressInfo", staResult);
		}
		return jav;
	}

	/**
	 * 总的平台应用统计
	 * 
	 * @return 统计信息
	 * @see JsonAndView
	 */
	@RequestMapping(value = "/totalPlatform", method = RequestMethod.POST)
	public JsonAndView staPlatformTotalInfor() {
		JsonAndView jav = new JsonAndView();
		Map<String, Integer> staResult = courseStatisticService
				.staPlatformTotalInfor();
		if (staResult == null) {
			jav.setRet(false);
			jav.setErrcode(1);
			jav.setErrmsg("统计总的平台应用信息失败！");
		} else {
			jav.addData("staResult", staResult);
		}
		return jav;
	}

	/**
	 * 统计课程学习进度
	 * 
	 * @param query
	 *            查询对象,包含分页信息
	 * @param departId
	 *            机构id,仅统计指定机构及其所有上级机构创建的课程
	 * @param courseId
	 *            课程id
	 * @param noStuFilter
	 *            是否对没有学生的课程进行过滤，1表示过滤，0表示不过滤
	 * @return 课程学习信息
	 * @see JsonAndView,CommonQuery,QueryData
	 */
	@RequestMapping(value = "/staCourseLearning", method = RequestMethod.POST)
	public JsonAndView staCourseLearningState(CommonQuery query, int departId,
			int courseId, int passOrNot, int noStuFilter) {
		JsonAndView jav = new JsonAndView();
		QueryData staResult = courseStatisticService.staCourseLearningState(
				query, departId, courseId, passOrNot,noStuFilter);
		if (staResult == null) {
			jav.setRet(false);
			jav.setErrcode(1);
			jav.setErrmsg("统计课程学习状态失败！");
		} else {
			jav.addData("totalPage", staResult.getTotalPage());
			jav.addData("totalCount", staResult.getTotalCount());
			jav.addData("pageData", staResult.getPageData());
		}
		return jav;
	}

	/**
	 * 导出课程选修信息
	 * 
	 * @param departId
	 * @param courseId
	 * @param filter
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/exportCourseLearningState", method = RequestMethod.GET)
    public void exportCourseLearningState(int departId, int courseId, int filter, HttpServletRequest request,
            HttpServletResponse response) {
        String basePath = request.getSession().getServletContext()
                .getRealPath("/");
        String filePath = null;
        filePath = courseStatisticService.writeCourseLearningInfor(basePath,
                    departId, courseId,filter);
        if (filePath != null) {
            ServletContextResource downFile = new ServletContextResource(
                    request.getSession().getServletContext(), filePath);
            if (!downFile.exists()) {
                logger.error("下载的文件不存在！");
                try {
                    response.reset();
                    response.sendRedirect("../../static/html/down_error.html");
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return;
            }
            String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
            String agintname = request.getHeader("User-Agent").toUpperCase();
            try {
                fileName = new String(fileName.getBytes(), "ISO-8859-1");
                if (agintname.indexOf("MSIE") > 0)
                    fileName = URLEncoder.encode(fileName, "ISO-8859-1");// IE浏览器
            } catch (UnsupportedEncodingException e1) {
                // TODO Auto-generated catch block
                logger.error("写入输出流失败！\n" + e1.getLocalizedMessage());
            }

            OutputStream os = null;
            try {
                os = response.getOutputStream();
                response.reset();
                response.setHeader("Content-Disposition",
                        "attachment; filename=" + fileName);
                response.setContentType("application/octet-stream; charset=utf-8");
                os.write(FileUtils.readFileToByteArray(downFile.getFile()));
                os.flush();
                // 日志记录
                logDBService.insertNewLog(request,
                        LogDBService.SELECT_OPERATION, "课程选修统计", "导出课程选修信息");
            } catch (Exception e) {
                logger.error("写入输出流失败！\n" + e.getLocalizedMessage());
            } finally {
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        logger.error("关闭输出流失败！\n" + e.getLocalizedMessage());
                    }
                }
                // 删除临时文件
                try {
                    downFile.getFile().delete();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    logger.error("删除临时文件失败！\n" + e.getLocalizedMessage());
                }
            }
        } else {
            logger.error("数据为空或者获取需要导出的数据时失败！");
            try {
                response.reset();
                response.sendRedirect("../../static/html/down_error.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
	
	/**
	 * 导出老师的课程辅导情况（论坛答疑次数，发起讨论次数）
	 * 
	 * @param query
	 *            查询对象 *
	 * @param departId
	 *            部门id
	 * @param courseId
	 *            课程id
	 * @param teacherName
	 *            教师名称
	 * @param request
	 *            请求对象
	 * @param response
	 *            响应对象
	 * @return
	 */
	@RequestMapping(value = "/exportTeacherTutorInfor", method = RequestMethod.GET)
	public void exportTeacherTutorInfor(CommonQuery query, int departId,
			int courseId, String teacherName, HttpServletRequest request,
			HttpServletResponse response) {
		String basePath = request.getSession().getServletContext()
				.getRealPath("/");
		String filePath = null;
		try {
			filePath = courseStatisticService.writeTeacherTutorInfor(basePath,
					query, departId, courseId,
					URLDecoder.decode(teacherName, "UTF-8"));
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		}
		if (filePath != null) {
			ServletContextResource downFile = new ServletContextResource(
					request.getSession().getServletContext(), filePath);
			if (!downFile.exists()) {
				logger.error("下载的文件不存在！");
				try {
					response.reset();
					response.sendRedirect("../../static/html/down_error.html");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return;
			}
			String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
			String agintname = request.getHeader("User-Agent").toUpperCase();
			try {
				fileName = new String(fileName.getBytes(), "ISO-8859-1");
				if (agintname.indexOf("MSIE") > 0)
					fileName = URLEncoder.encode(fileName, "ISO-8859-1");// IE浏览器
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				logger.error("写入输出流失败！\n" + e1.getLocalizedMessage());
			}

			OutputStream os = null;
			try {
				os = response.getOutputStream();
				response.reset();
				response.setHeader("Content-Disposition",
						"attachment; filename=" + fileName);
				response.setContentType("application/octet-stream; charset=utf-8");
				os.write(FileUtils.readFileToByteArray(downFile.getFile()));
				os.flush();
				// 日志记录
				logDBService.insertNewLog(request,
						LogDBService.SELECT_OPERATION, "课程辅导统计", "导出课程辅导列表");
			} catch (Exception e) {
				logger.error("写入输出流失败！\n" + e.getLocalizedMessage());
			} finally {
				if (os != null) {
					try {
						os.close();
					} catch (IOException e) {
						logger.error("关闭输出流失败！\n" + e.getLocalizedMessage());
					}
				}
				// 删除临时文件
				try {
					downFile.getFile().delete();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					logger.error("删除临时文件失败！\n" + e.getLocalizedMessage());
				}
			}
		} else {
			logger.error("数据为空或者获取需要导出的数据时失败！");
			try {
				response.reset();
				response.sendRedirect("../../static/html/down_error.html");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
