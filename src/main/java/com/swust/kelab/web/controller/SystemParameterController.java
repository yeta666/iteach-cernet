package com.swust.kelab.web.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.swust.kelab.domain.SystemParameter;
import com.swust.kelab.model.SystemParameterModel;
import com.swust.kelab.service.LogDBService;
import com.swust.kelab.service.SystemParameterService;
import com.swust.kelab.utils.CookieUtil;
import com.swust.kelab.utils.JsonUtil;
import com.swust.kelab.web.json.JsonAndView;

/**
 * 系统参数与友情链接管理
 * 
 * @author lancer
 * 
 */
@Controller
@RequestMapping("/sypaController")
public class SystemParameterController {

    private SystemParameterService systemParameterService;
    @Autowired
    private LogDBService logDBService;

    @Autowired
    public void setSystemParameterService(SystemParameterService systemParameterService) {
        this.systemParameterService = systemParameterService;
    }

    /**
     * 返回所有系统参数
     * 
     * @author 李晓伟
     * @return JsonAndView jv
     */
    @RequestMapping(value = "/viewAllSypa", method = RequestMethod.POST)
    public JsonAndView selectAllSypa(@RequestParam("pType") int pType, HttpServletResponse response) {
        Map<String, Object> data = new HashMap<String, Object>();
        JsonAndView jv = new JsonAndView();
        List<SystemParameter> parameters = systemParameterService.selectAllSypa(pType);
        data.put("result", parameters);
        if (pType == 3) {
            List<Object> parametersList = new ArrayList<Object>();
            for (int i = 0; i < parameters.size(); i++) {
                Map<String, Object> map = new HashMap<String, Object>();
                SystemParameter systemParameter = parameters.get(i);
                map.put("SypaEnName", systemParameter.getSypaEnName());
                map.put("SypaValue", systemParameter.getSypaValue());
                parametersList.add(map);
            }
            try {
                String parametersResult = JsonUtil.getJSON(parametersList);
                CookieUtil.colsCookie(parametersResult, "parameters", "/", "", 10000, response);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        jv.addAllData(data);
        return jv;
    }

    /**
     * 修改系统参数
     * 
     * @author 李晓伟
     * @param array
     * @return jv
     */
    @RequestMapping(value = "/updateSypa", method = RequestMethod.POST)
    @SuppressWarnings("finally")
    public JsonAndView updateSypa(String arrayList, HttpServletRequest request) {
        String[] array = arrayList.split(",");
        List<SystemParameterModel> list = new ArrayList<SystemParameterModel>();
        for (int i = 0; i < array.length; i++) {
            SystemParameterModel sypaModel = new SystemParameterModel();
            sypaModel.setSypaId(array[i].split("～")[0]);
            sypaModel.setSypaValue(array[i].split("～")[1]);
            list.add(sypaModel);
        }
        Map<String, Object> data = new HashMap<String, Object>();
        JsonAndView jv = new JsonAndView();
        try {
            systemParameterService.updateSypa(list);
            data.put("state", "修改成功！");
            logDBService.insertNewLog(request, logDBService.UPDATA_OPERATION, "系统参数管理", "修改系统参数，配置系统");
        } catch (Exception e) {
            data.put("state", "修改失败！");
        } finally {
            jv.addAllData(data);
            return jv;
        }
    }

    /**
     * 添加友情链接
     * 
     * @author 李晓伟
     * @param sypa
     * @return
     */
    @SuppressWarnings("finally")
    @RequestMapping(value = "/addLink", method = RequestMethod.POST)
    public JsonAndView addLink(SystemParameter sypa, HttpServletRequest request) {
        Map<String, Object> data = new HashMap<String, Object>();
        JsonAndView jv = new JsonAndView();
        try {
            systemParameterService.addLink(sypa);
            data.put("state", "success");
            logDBService.insertNewLog(request, logDBService.ADD_OPERATION, "友情链接管理", "添加友情链接");
        } catch (Exception e) {
            data.put("state", "failed");
        } finally {
            jv.addAllData(data);
            return jv;
        }
    }

    /**
     * 修改友情链接
     * 
     * @author 李晓伟
     * @param sypa
     * @return
     */
    @SuppressWarnings("finally")
    @RequestMapping(value = "/updateLink", method = RequestMethod.POST)
    public JsonAndView updateLink(SystemParameter sypa,HttpServletRequest request) {
        Map<String, Object> data = new HashMap<String, Object>();
        JsonAndView jv = new JsonAndView();
        try {
            systemParameterService.updateLink(sypa);
            data.put("state", "success");
            logDBService.insertNewLog(request, logDBService.UPDATA_OPERATION, "友情链接管理", "修改友情链接："+sypa.getSypaName());
        } catch (Exception e) {
            data.put("state", "failed");
        } finally {
            jv.addAllData(data);
            return jv;
        }
    }

    /**
     * 删除友情链接
     * 
     * @author 李晓伟
     * @param linkId
     * @return
     */
    @SuppressWarnings("finally")
    @RequestMapping(value = "/deleteLink", method = RequestMethod.POST)
    public JsonAndView deleteLink(Integer linkId,HttpServletRequest request) {
        Map<String, Object> data = new HashMap<String, Object>();
        JsonAndView jv = new JsonAndView();
        try {
            systemParameterService.deleteLink(linkId);
            data.put("state", "success");
            logDBService.insertNewLog(request, logDBService.DELETE_OPERATION, "友情链接管理", "删除友情链接");
        } catch (Exception e) {
            data.put("state", "failed");
        } finally {
            jv.addAllData(data);
            return jv;
        }
    }

    /**
     * 获取指定英文名的系统参数的值
     * 
     * @param enName 英文名
     * @return 查询结果
     * @author Wu Xianhui
     * @see JsonAndView
     */
    @RequestMapping(value = "/attainValueByEnName", method = RequestMethod.POST)
    public JsonAndView attainSyspamValueByEnName(String enName) {
        JsonAndView jav = new JsonAndView();
        String result = systemParameterService.attainValueByEnName(enName);
        if (result != null) {
            jav.addData("value", result);
        } else {
            jav.setRet(false);
            jav.setErrmsg(result);
        }
        return jav;
    }

}
