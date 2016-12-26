package com.swust.kelab.service;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.swust.kelab.domain.Log;
import com.swust.kelab.domain.User;
import com.swust.kelab.model.CommonQuery;
import com.swust.kelab.model.LogModel;
import com.swust.kelab.model.PageData;
import com.swust.kelab.model.QueryData;
import com.swust.kelab.repos.LogDAO;
import com.swust.kelab.repos.bean.ListQuery;
import com.swust.kelab.utils.CookieUtil;
import com.swust.kelab.utils.ExportAsExcel;
import com.swust.kelab.utils.RemoteIpUtil;

@Service
public class LogDBService {
	final Logger logger = LoggerFactory.getLogger(getClass());
	/**
	 * 添加操作
	 */
	final static public Integer ADD_OPERATION = 1;
	/**
	 * 删除操作
	 */
	final static public Integer DELETE_OPERATION = 2;
	/**
	 * 更新操作
	 */
	final static public Integer UPDATA_OPERATION = 3;
	/**
	 * 查询操作
	 */
	final static public Integer SELECT_OPERATION = 4;

	@Autowired
	private LogDAO logDAO;

	public String exportLog(CommonQuery query, int maxCount, String base) {
		ListQuery map = query.format();
		if (maxCount == 1) {
			maxCount = 10000;
		} else if (maxCount == 2) {
			maxCount = 20000;
		} else if (maxCount == 3) {
			maxCount = 30000;
		} else if (maxCount == 4) {
			maxCount = 40000;
		} else if (maxCount == 5) {
			maxCount = 50000;
		} else if (maxCount == 6) {
			maxCount = 60000;
		} else {
			maxCount = 10000;
		}
		map.fill("maxCount", maxCount);
		List<LogModel> logList = logDAO.queryLogsByExport(map);
		if (logList == null || logList.size() <= 0) {
			return null;
		}
		Map<String, String> titleMap = getTitleOfExcel();
		String fileName = "upload/temp/log_" + System.currentTimeMillis()
				+ ".xls";
		File excelFile = new File(base + "/" + fileName);
		if (!excelFile.getParentFile().exists()) {
		    excelFile.getParentFile().mkdirs();
        }
        if(excelFile.exists()){
            excelFile.delete();
        }
        try {
            excelFile.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.error("创建文件失败!\n"+e.getLocalizedMessage());
        }
		boolean flag = ExportAsExcel.exportExcel(
				"com.swust.kelab.model.LogModel", titleMap, "日志记录", excelFile,
				logList);
		if (flag) {
			return fileName;
		}
		return null;
	}

	/**
	 * 向导出的excel中添加列名称
	 * 
	 * @return
	 */
	private Map<String, String> getTitleOfExcel() {
		Map<String, String> titleMap = new HashMap<String, String>();
		titleMap.put("id", "记录id");
		titleMap.put("logTime", "操作时间");
		titleMap.put("logIp", "操作ip");
		titleMap.put("logClient", "操作客户端");
		titleMap.put("logType", "log类型（1:管理员，2：学生）");
		titleMap.put("userName", "用户ID");
		titleMap.put("logOpt", "操作描述");
		return titleMap;
	}

	/**
	 * 分页查询日志
	 * 
	 * @param query
	 * @return
	 */
	public QueryData queryLog(CommonQuery query) {
		QueryData result = new QueryData();
		List<LogModel> list = null;
		int totalPage = 0;
		try {
			ListQuery queryMap = query.format();
			int allCount = logDAO.queryLogsCount(queryMap);
			if (allCount <= 0) {
				return null;
			}
			result.setTotalCount(allCount);
			if (query.getRecordPerPage() <= 0) {
				query.setRecordPerPage(20); // 每页20条
			}
			totalPage = QueryData.computeTotalPage(allCount,
					query.getRecordPerPage());
			result.setTotalPage(totalPage);
			if (query.getPageArray() == null) {
				query.setPageArray(new int[] { 1, 2, 3 });
			}
			int startIndex = (query.getPageArray()[0] - 1)
					* query.getRecordPerPage();
			int endIndex = query.getPageArray().length
					* query.getRecordPerPage();
			queryMap.fill("startIndex", startIndex);
			queryMap.fill("maxCount", endIndex);
			list = logDAO.queryLogs(queryMap);
			if (list == null || list.size() <= 0) {
				return null;
			}
		} catch (Exception e) {
			logger.error("日志查询失败：" + e.getLocalizedMessage());
		}
		List<PageData> pageDataList = Lists.newArrayList();
		return initialQueryData(result, pageDataList, query, list, totalPage);
	}

	/**
	 * 查询结果分页封装
	 * 
	 * @param result
	 *            返回处理结果
	 * @param pageDataList
	 *            每一页信息
	 * @param query
	 *            查询条件 如每页大小，第几页
	 * @param list
	 *            查询结果记录
	 * @param totalPage
	 *            总页数
	 * @return
	 * 
	 * @fixbug easonlian
	 */
	private QueryData initialQueryData(QueryData result,
			List<PageData> pageDataList, CommonQuery query,
			List<LogModel> list, int totalPage) {
		int minPageNum = query.getPageArray()[0];
		for(int pageNum : query.getPageArray()) {
			if(pageNum<minPageNum)
				minPageNum = pageNum;
		}
		int beginPosition = QueryData.computeStartIndex(minPageNum,
						query.getRecordPerPage()); 
		for (int i = 0; i < query.getPageArray().length; i++) {
			int page = query.getPageArray()[i];
			if (page <= 0 || page > totalPage) {
				continue;
			}
			int startIndex = QueryData.computeStartIndex(page,
					query.getRecordPerPage())-beginPosition;
			int endIndex = startIndex + query.getRecordPerPage();
			if (startIndex >= list.size()) {
				break;
			}
			if (endIndex > list.size()) {
				endIndex = list.size();
			}
			List<LogModel> pageDatas = list.subList(startIndex, endIndex);
			pageDataList.add(new PageData(page, pageDatas));
		}
		result.setPageData(pageDataList);
		return result;
	}

	/**
	 * 删除查询条件下的日志
	 * 
	 * @param query
	 */
	public int deleteLogs(CommonQuery query) {
		ListQuery queryMap = query.format();
		try {
			return logDAO.deleteLogs(queryMap);
		} catch (Exception e) {
			logger.error("删除失败：" + e.getLocalizedMessage());
		}
		return -1;
	}

	/**
	 * 插入系统操作日志
	 * 
	 * @param request
	 *            HttpServletRequest请求
	 * @param operateType
	 *            操作类型有以下四种<br>
	 *            LogDBService.ADD_OPERATION,LogDBService.DELETE_OPERATION,<br>
	 *            LogDBService.UPDATA_OPERATION,LogDBService.SELECT_OPERATION
	 * @param where
	 *            在哪个二级栏目下
	 * 
	 * @param taget
	 *            针对的目标是什么，如果没有目标以'-'代替
	 * @author lujoCom
	 */
	@Transactional
	public void insertNewLog(HttpServletRequest request, Integer operateType,
			String where, String taget) {

		Log log = new Log();
		User user = CookieUtil.getCookieUser(request);
		log.setLogTime(new Date());
		if (operateType.equals(ADD_OPERATION)) {
			log.setLogOpt(where + "|添加|" + taget);
		} else if (operateType.equals(DELETE_OPERATION)) {
			log.setLogOpt(where + "|删除|" + taget);
		} else if (operateType.equals(UPDATA_OPERATION)) {
			log.setLogOpt(where + "|更新|" + taget);
		} else if (operateType.equals(SELECT_OPERATION)) {
			log.setLogOpt(where + "|查看|" + taget);
		}

		log.setLogUserId(user.getUserId());
		log.setLogType(user.getUserType());
		log.setLogIp(RemoteIpUtil.getRemoteIpAddr(request));
		log.setLogClient(request.getHeader("User-Agent"));
		logger.debug("客户端是：" + RemoteIpUtil.getRemoteIpAddr(request));
		logDAO.insertLog(log);

	}

}
