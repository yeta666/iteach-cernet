package com.swust.kelab.web.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.support.ServletContextResource;

import com.swust.kelab.model.CommonQuery;
import com.swust.kelab.model.QueryData;
import com.swust.kelab.service.LogDBService;
import com.swust.kelab.web.json.JsonAndView;

@Controller
@RequestMapping("/log")
public class LogDBController {
	final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private LogDBService logDBService;

	public void setLogDBService(LogDBService logDBService) {
		this.logDBService = logDBService;
	}

	/**
	 * 通过查询参数，导出excel日志 query中包含时间段，以及按各种搜索字段 其中 searchType=1 按用户名检索，
	 * 
	 * @param query
	 * @logType 类型 ，1表示管理员日志 2表示学生日志
	 * @return
	 */
	@RequestMapping(value = "/exportLog", method = RequestMethod.GET)
	public void exportLog(CommonQuery query, int maxCount,
			HttpServletRequest request, HttpServletResponse response) {
		String basePath = request.getSession().getServletContext()
				.getRealPath("/");
		try {
		    if(query.getSearchWord()!=null&&
		            query.getSearchWord().length()>0){
		        String t = URLDecoder.decode(query.getSearchWord(),"utf-8");
		        query.setSearchWord(t);
		    }
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		String fileName = logDBService.exportLog(query, maxCount, basePath);
		if (fileName != null) {
			ServletContextResource downFile = new ServletContextResource(
					request.getSession().getServletContext(), fileName);
			String Name = fileName.substring(fileName.lastIndexOf("/") + 1);

			logDBService.insertNewLog(request, LogDBService.SELECT_OPERATION,
					"日志管理", "导出日志记录");

			String agintname = request.getHeader("User-Agent").toUpperCase();
			try {
				Name = new String(Name.getBytes(), "ISO-8859-1");
				if (agintname.indexOf("MSIE") > 0)
					fileName = URLEncoder.encode(Name, "ISO-8859-1");// IE浏览器
			} catch (UnsupportedEncodingException e1) {
				logger.error("写入输出流失败！\n" + e1.getLocalizedMessage());
			}

			OutputStream os = null;
			try {
				os = response.getOutputStream();
				response.reset();
				response.setHeader("Content-Disposition",
						"attachment; filename=" + Name);
				response.setContentType("application/octet-stream; charset=utf-8");
				os.write(FileUtils.readFileToByteArray(downFile.getFile()));
				os.flush();
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
			return;
		}
		try {
			response.getWriter().print("对不起！导出失败，可能是数据库不存在你要查询的日志记录");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 分页查询数据
	 * 
	 * @param query
	 *            查询对象，包括查询时间段，是否有查询字段（用户名）
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/queryLogs", method = RequestMethod.POST)
	public JsonAndView queryLog(
			@RequestParam(value = "pageArray") String pageArray,
			@RequestParam(value = "recordPerPage") int rows,
			@RequestParam(value = "endTime") String endTime,
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "searchWord", required = false) String searchWord) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = null, endDate = null;
		try {
			if (startTime != null && !startTime.equals("")) {
				startDate = sdf.parse(startTime);
				String[] eachDate = endTime.split("-");
				String day = (Integer.valueOf(
						Integer.valueOf(eachDate[2]).intValue()+1)).toString();
				endTime = (eachDate[0] + "-" +
						eachDate[1] + "-" + 
						(day.length() == 1 ? "0"+day : day));
				endDate = sdf.parse(endTime);
			}
		} catch (ParseException pe) {
			pe.printStackTrace();
		}
		CommonQuery query = null;
		if (query == null) {
			System.out.println("接受到的参数为空");
			query = new CommonQuery();
			if (startDate != null) {
				query.setStartTime(startDate);
				query.setEndTime(endDate);
			} else
				query.setEndTime(new Date());
			query.setRecordPerPage(10);
			String[] pageList = pageArray.split(",");
			int[] pageNums = new int[pageList.length];
			for (int i = 0; i < pageNums.length; i++) {
				if (!pageList[i].equals(""))
					pageNums[i] = Integer.valueOf(pageList[i]);
			}
			query.setPageArray(pageNums);
			query.setSearchWord(searchWord);
		}
		JsonAndView jv = new JsonAndView();
		QueryData qd = logDBService.queryLog(query);
		if (qd != null) {
			jv.addData("totalPage", qd.getTotalPage());
			jv.addData("totalCount", qd.getTotalCount());
			jv.addData("pageData", qd.getPageData());
		} else {
			jv.setErrcode(1);
			jv.addData("totalPage", 0);
			jv.addData("totalCount", 0);
			jv.addData("pageData", null);
			jv.setErrmsg("sorry!没有查询到数据");
		}
		return jv;
	}

	/**
	 * 删除查询条件下的日志
	 * 
	 * @param query
	 *            查询对象，包括查询时间段，是否有查询字段（用户名）
	 * @return
	 */
	@RequestMapping(value = "/deleteLogs", method = RequestMethod.POST)
	public JsonAndView deleteLogs(CommonQuery query, HttpServletRequest request) {
		JsonAndView jv = new JsonAndView();
		if (query != null) {
			if (logDBService.deleteLogs(query) > 0) {
				logDBService.insertNewLog(request,
						LogDBService.DELETE_OPERATION, "日志管理", "日志记录");
				jv.setErrcode(0);
			} else {
				jv.setErrcode(1); // 删除失败
				jv.setErrmsg("没有找到你查询条件对应的记录！");
			}
		}
		return jv;
	}
}
