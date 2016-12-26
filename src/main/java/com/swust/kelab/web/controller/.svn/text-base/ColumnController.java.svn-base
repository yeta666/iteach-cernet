package com.swust.kelab.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.protocol.HTTP;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.swust.kelab.domain.User;
import com.swust.kelab.service.ColumnService;
import com.swust.kelab.service.LogDBService;
import com.swust.kelab.utils.CookieUtil;
import com.swust.kelab.web.json.JsonAndView;

/**
 * 系统栏目
 * 
 * @author yangzq
 * 
 */
@Controller
@RequestMapping("/column")
public class ColumnController {
    final Logger logger = LoggerFactory.getLogger(getClass());
    @Resource
    private ColumnService columnService;
    @Autowired
    private LogDBService  logDBService;

    /**
     * 加载栏目
     * 
     * @author yangzq
     * @param request
     * @return
     */
    @RequestMapping(value = "/loadColumns", method = RequestMethod.POST)
    public JsonAndView loadColumns(@Param("firstCol") int firstCol,HttpServletRequest request, HttpServletResponse response) {
        JsonAndView jv = new JsonAndView();
        // 获取人员信息
        User user = CookieUtil.getCookieUser(request);
        if (user == null) {
            jv.setRet(false);
            return jv;
        }
        List<Map<String, Object>> colVal = null;
		try {
			colVal = columnService.queryAllColumns(user.getUserId(),firstCol, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			jv.setRet(false);
            return jv;
		}
        jv.addData("colval", colVal);
        return jv;
    }
    
    
    /**
     * 查找一级栏目
     * @author yangzq
     * @return
     */
    @RequestMapping(value="/firLevelCol",method=RequestMethod.GET)
    public JsonAndView firLevelCol(){
    	JsonAndView jv = new JsonAndView();
    	return jv.addData("firLevelCol", columnService.firLevelCol());
    }
    
    /**
     * 查找二级栏目
     * @author yangzq
     * @param firId
     * @return
     */
    @RequestMapping(value="/secLevelCol",method=RequestMethod.POST)
    public JsonAndView secLevelCol(@Param("firId")int firId){
    	JsonAndView jv = new JsonAndView();
    	if(firId == 0){
    		return jv.setRet(false);
    	}
    	return jv.addData("subCol", columnService.secLevelCol(firId));
    }
    
    
    /**
     * 删除栏目
     * @author yangzq
     * @param roleName
     * @param roleDesc
     * @param roleId
     * @param authority
     * @return
     */
	@RequestMapping(value = "/delCols", method = RequestMethod.POST)
	public JsonAndView delCols(String[] data,HttpServletRequest request) {
		JsonAndView jv = new JsonAndView();
		if (data.length <= 0) {
			jv.setRet(false);
			jv.setErrmsg("参数传入有误！");
			return jv;
		}
		try {
			columnService.delCols(data);
			logDBService.insertNewLog(request, logDBService.DELETE_OPERATION, "栏目管理", "删除栏目");
		} catch (Exception e) {
			e.printStackTrace();
			jv.setRet(false);
			return jv;
		}
		return jv;
	}
	
    /**
     * 栏目操作新增
     * @author yangzq
     * @return
     */
    @RequestMapping(value = "/saveNewCol", method = RequestMethod.POST)
	public JsonAndView saveNewCol(@Param("type") int type,
			@Param("parentid") int parentid, @Param("colname") String colname,
			@Param("colurl") String colurl,@Param("colicon")String colicon, @Param("colorder") int colorder,HttpServletRequest request) {
		JsonAndView jv = new JsonAndView();
		if(type<=0 || type > 3 || colname == null || colname == ""){
			jv.setRet(false);
			jv.setErrmsg("参数获取失败!");
			return jv;
		}
		try {
			columnService.saveNewCol(type,parentid,colname,colurl,colicon,colorder);
			logDBService.insertNewLog(request, logDBService.ADD_OPERATION, "栏目管理", "添加栏目");
		} catch (Exception e) {
			jv.setRet(false);
			jv.setErrmsg("保存失败！请确认参数是否正确，具体错误信息：\n"+e.getMessage());
			return jv;
		}
		return jv;
	}
    

    /**
     * 编辑栏目
     * @author yangzq
     * @param firColId
     * @return
     */
	@RequestMapping(value = "/editCols", method = RequestMethod.POST)
	public JsonAndView editCols(@Param("type") int type,
			@Param("parentid") int parentid, @Param("colid") int colid,
			@Param("colname") String colname, @Param("colurl") String colurl,
			@Param("colorder") int colorder ,HttpServletRequest request) {
		JsonAndView jv = new JsonAndView();
		if (type <= 0 || type > 3 || colid == 0 || colname == null
				|| colname == "") {
			jv.setRet(false);
			jv.setErrmsg("参数获取失败!");
			return jv;
		}
		try {
			columnService.editCols(type, parentid,colid, colname, colurl, colorder);
			logDBService.insertNewLog(request, logDBService.UPDATA_OPERATION, "栏目管理", "修改栏目："+colname);
		} catch (Exception e) {
			jv.setRet(false);
			jv.setErrmsg(e.getMessage());
			return jv;
		}
		return jv;
	}
    
    /**
     * 从session获取存放的子栏目
     * ****************弃用*************
     * 
     * @author yangzq
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/subColumns", method = RequestMethod.POST)
    public JsonAndView subColumns(@Param("firColId") int firColId, HttpServletRequest request) {
        JsonAndView jv = new JsonAndView();
        // 获取人员信息
        User user = CookieUtil.getCookieUser(request);
        if (user == null) {
            jv.setRet(false);
            return jv;
        }
        Map<String, Object> colSession = (Map<String, Object>) request.getSession().getAttribute("subcolSession");
        List<Map<String, Object>> subcol = new ArrayList<Map<String, Object>>();
        if (colSession != null) {
        	subcol = (List<Map<String, Object>>) colSession.get("subcols");
            for (Map<String, Object> col : subcol) {
                if (Integer.parseInt(col.get("parentId").toString()) == firColId) {
                	List<Map<String, Object>> subActions = (List<Map<String, Object>>)col.get("subCols");
                	//把二级模块集中封装到一个对象
                	List<Map<String,Object>> actions = new ArrayList<Map<String, Object>>();;
                	for(Map<String, Object> action : subActions){
                		//获取二级模块所辖权限功能
                		//actions = (List<Map<String, Object>>)action.get("subcols");
                		for(Map<String, Object> ac:(List<Map<String, Object>>)action.get("subcols")){
                			actions.add(ac);
                		}
                	}
                    jv.addData("subcols", col.get("subCols"));
                    jv.addData("actions", actions);
                }
            }
        }else{
        	jv.setRet(false);
        }
        return jv;
    }

}
