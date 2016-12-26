package com.swust.kelab.web.controller;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.swust.kelab.domain.VedioServer;
import com.swust.kelab.model.CommonQuery;
import com.swust.kelab.model.QueryData;
import com.swust.kelab.service.LogDBService;
import com.swust.kelab.service.VideoServerService;
import com.swust.kelab.web.json.JsonAndView;

/**
 * 服务器管理
 * 
 * @author 李晓伟
 * 
 */
@Controller
@RequestMapping("/videoServer")
public class VideoServerController {
    final Logger logger = LoggerFactory.getLogger(getClass());
    private VideoServerService videoServerService;
    @Autowired
    private LogDBService logDBService;
    @Autowired
    public void setVideoServerService(VideoServerService videoServerService) {
        this.videoServerService = videoServerService;
    }

    /**
     * 查询所有服务器
     * 
     * @param query
     * @return
     */
    @RequestMapping(value = "/selectAllServer", method = RequestMethod.POST)
    public JsonAndView selectAllServer(CommonQuery query) {
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        if (query == null) {
            jv.setErrcode(601);
            jv.setErrmsg("数据格式错误");
            return jv;
        }
        // 对象数组
        QueryData queryData = videoServerService.selectAllServer(query);
        jv.addData("totalPage", queryData.getTotalPage());
        jv.addData("totalCount", queryData.getTotalCount());
        jv.addData("pageData", queryData.getPageData());
        return jv;
    }

    /**
     * 增加服务器
     * @author lancer
     * @param vedioServer
     * @return
     */
    @SuppressWarnings("finally")
    @RequestMapping(value = "/ insertServer", method = RequestMethod.POST)
    public JsonAndView insertServer(VedioServer vedioServer,HttpServletRequest request) {
        Map<String, Object> data = new HashMap<String, Object>();
        JsonAndView jv = new JsonAndView();
        try {
            InetAddress.getByName(vedioServer.getVeseOuterIp());
            videoServerService.insertServer(vedioServer);
            data.put("state", "添加成功!");
            data.put("message", "服务器添加成功!");
            logDBService.insertNewLog(request, logDBService.ADD_OPERATION, "服务器管理", "添加服务器");  
        } catch (Exception e) {
            data.put("state", "添加失败");
            data.put("message", ",域名或客户端可访问IP错误！");
        } finally {
            jv.addAllData(data);
            return jv;
        }

    }

    /**
     * 修改服务器
     * 
     * @param vedioServer
     * @return
     */
    @SuppressWarnings("finally")
    @RequestMapping(value = "/updateServer", method = RequestMethod.POST)
    public JsonAndView updateServer(VedioServer vedioServer,HttpServletRequest request) {
        Map<String, Object> data = new HashMap<String, Object>();
        JsonAndView jv = new JsonAndView();
        try {
            InetAddress.getByName(vedioServer.getVeseOuterIp());
            videoServerService.updateServer(vedioServer);
            data.put("state", "修改成功！");
            data.put("message", "修改服务器成功！");
            logDBService.insertNewLog(request, logDBService.UPDATA_OPERATION, "服务器管理", "修改服务器");
            jv.addAllData(data);
        } catch (Exception e) {
            data.put("state", "修改失败!");
            data.put("message", "域名或客户端可访问IP错误！!");
            jv.addAllData(data);
        } finally {
            return jv;
        }
    }

    /**
     * 删除服务器Id
     * 
     * @param veseIdQuery
     * @return
     */
    @SuppressWarnings("finally")
    @RequestMapping(value = "/deleteServer", method = RequestMethod.POST)
    public JsonAndView deleteServer(String veseId,HttpServletRequest request) {
        Map<String, Object> data = new HashMap<String, Object>();
        JsonAndView jv = new JsonAndView();
        try {
            videoServerService.deleteServer(veseId.split(","));
            data.put("state", "删除成功！");
            logDBService.insertNewLog(request, logDBService.ADD_OPERATION, "服务器管理", "删除服务器");
            jv.addAllData(data);
        } catch (Exception e) {
            data.put("state", "删除失败！");
            jv.addAllData(data);
        } finally {
            return jv;
        }
    }

    /**
     * 根据Id查询
     * 
     * @param veseId
     * @return
     */
    @RequestMapping(value = "/selectById", method = RequestMethod.POST)
    public JsonAndView selectById(int veseId) {
        Map<String, Object> data = new HashMap<String, Object>();
        JsonAndView jv = new JsonAndView();
        data.put("data", videoServerService.selectById(veseId));
        jv.addAllData(data);
        return jv;
    }

}
