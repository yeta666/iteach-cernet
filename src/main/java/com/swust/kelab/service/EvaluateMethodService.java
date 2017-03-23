package com.swust.kelab.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swust.kelab.domain.EvaluateMethod;
import com.swust.kelab.model.PageData;
import com.swust.kelab.repos.EvaluateMethodDAO;

/**
 * 处理考核方式的service
 * @author Wu,EasonLian
 *
 */
@Service
public class EvaluateMethodService {
    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private EvaluateMethodDAO evaluateMethodDAO;
    
    /**
	 * 添加评估方法
	 * @param EvaluateMethod
	 * @return 影响的行数
	 * @author EasonLian
	 */
    public int addEvaluateMethod(EvaluateMethod evme) {
    	try {
    		return evme.getEvmeId() == null? 
    				evaluateMethodDAO.addEvaluateMethod(evme)
    				: evaluateMethodDAO.modeEvaluateMethod(evme);
    	} catch(Exception e) {
e.printStackTrace();
			logger.error("addEvaluateMethod error!");
			return 0;
    	}
    }
    
    /**
     * 批量删除方法
     * @param ids
     * @return 是否全部删除成功
     */
    public boolean delAllEvaluateMethods(String ids) {
    	try {
    		if(ids.endsWith(","))
    			ids = ids.substring(0,ids.length()-1);
    		String[] idsArray = ids.split(",");
    		int effectRows = evaluateMethodDAO.delEvaluateMethod("("+ids+")");
    		return effectRows == idsArray.length ? true:false;
    	} catch(Exception e) {
			logger.error("delAllEvaluateMethods error!");
			return false;
    	}
    }
    
    /**
     * 查询所有的考核方式
     * 
     * @return  考核方式列表
     */
    public List<EvaluateMethod> viewAllEvaluateMethods(){
        List<EvaluateMethod> result=null;
        try {
            result=evaluateMethodDAO.viewAllEvaluateMethods();
        } catch(Exception e) {
            logger.error("viewAllEvaluateMethods error!\n"+e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 查询所有考核方式列表
     * @param pages
     * @param rows
     * @param evmeId
     * @param evmeName
     * @param evmeDescribe
     * @author EasonLian
     */
	public Map<String, Object> viewEvaluateMethodList(String pages, int rows,
			Integer evmeId, String evmeName, String evmeDescribe) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			String[] pageNums = pages.split(",");
			List<PageData> pageDataList = new ArrayList<PageData>();
			int offset = (Integer.valueOf(pageNums[0])-1)*rows;
			params.put("start",offset);
			params.put("end",pageNums.length * rows);
			params.put("evmeId", evmeId);
			params.put("evmeName", evmeName);
			params.put("evmeDescribe", evmeDescribe);
			List<EvaluateMethod> emList = evaluateMethodDAO.viewEvaluateMethodList(params);
			for(int i=0;i<pageNums.length;i++) {
				int pageNum = Integer.valueOf(pageNums[i]);
				int start = i * rows;
				if(start > emList.size())
					break;
				int end = start + rows;
				if(end > emList.size())
					end = emList.size();
				PageData pageData = new PageData(pageNum,emList.subList(start, end));
				pageDataList.add(pageData);
			}
			dataMap.put("pageData", pageDataList);
			int totalCount = evaluateMethodDAO.getEvaluateMethodCount(params);
			dataMap.put("totalCount", totalCount);
			dataMap.put("totalPage", totalCount%rows == 0?totalCount/rows:(totalCount/rows+1));
			return dataMap;
		} catch(Exception e) {
			logger.error("viewEvaluateMethodList error!");
			return null;
		}
	}
}
