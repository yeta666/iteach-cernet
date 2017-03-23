package com.swust.kelab.web.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.swust.kelab.service.LogDBService;
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
@RequestMapping("/banner")
public class BannerController {
    final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private LogDBService logDBService;
    /**
     * 返回所有banner
     * @author htx
     * @return JsonAndView jv
     * @throws UnsupportedEncodingException
     */
    @SuppressWarnings("static-access")
    @RequestMapping(value = "/viewAllBanner", method = RequestMethod.GET)
    public JsonAndView viewAllBanner(HttpServletRequest request, HttpServletResponse response)
            throws UnsupportedEncodingException {
        Map<String, Object> data = new HashMap<String, Object>();
        List<Object> resultList = new ArrayList<Object>();
        JsonAndView jv = new JsonAndView();
        List<String> fileNames = new ArrayList<String>();
        String directory = request.getSession().getServletContext().getRealPath("static/img/banner");
        File file = new File(directory);
        if (file.isDirectory()) {
            String[] filelist = file.list();
            for (int j = 0; j < filelist.length; j++) {
                File readfile = new File(directory + "/" + filelist[j]);
                if (!readfile.isDirectory()) {
                    Map<String, Object> result = new HashMap<String, Object>();
                    String fileName = readfile.getName();
                    String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
                    fileNames.add(fileName);
                    result.put("filename", fileName);
                    result.put("filetype", suffix);
                    result.put("filesize", (double) readfile.length() / 1000 + "k");
                    resultList.add(result);
                }
            }
        }
        String cookies = JsonUtil.getJSON(fileNames);
        CookieUtil cookieUtil = new CookieUtil();
        cookieUtil.colsCookie(cookies, "Banners", "/", "", 10000, response);
        data.put("result", resultList);
        jv.addAllData(data);
        return jv;
    }

    /**
     * 添加banner
     * 
     * @author htx
     * @param filename
     * @return
     * @throws IOException
     * @throws IllegalStateException
     */
    @RequestMapping(value = "/addBanner", method = RequestMethod.POST)
    public void addBanner(HttpServletRequest request, HttpServletResponse response, MultipartFile fileInput)
            throws IllegalStateException, IOException {
        Map<String, Object> data = new HashMap<String, Object>();
        String type = "";
        String oriFileName = fileInput.getOriginalFilename();
        int position = oriFileName.lastIndexOf(".");
        if (position >= 0) {
            type = oriFileName.substring(position);
        }
        String newFileName = System.currentTimeMillis() + type;
        try {
            fileInput.transferTo(new File(request.getSession().getServletContext()
                    .getRealPath("static/img/banner/" + newFileName)));
            // 防止返回数据被IE当做下载流
            response.reset();
            response.setContentType("text/html; charset=utf-8");
            PrintWriter pw = response.getWriter();
            pw.write("{'ret':true,'data':{'state':'success','message':'上传图片成功！'}}");
            pw.flush();
            pw.close();
            logDBService.insertNewLog(request, logDBService.ADD_OPERATION, "顶部图片管理", "添加顶部图片"); 
        } catch (Exception e) {
            logger.error("存储文件失败！\n" + e.getLocalizedMessage());
            try {
                response.reset();
                response.setContentType("text/html; charset=utf-8");
                PrintWriter pw = response.getWriter();
                pw.write("{'ret':false,'errcode':2,'errmsg':'存储文件失败！'}");
                pw.flush();
                pw.close();
            } catch (IOException e1) {
                logger.error("use response's printWriter errror!" + e1.getLocalizedMessage());
            }
        }
    }

    /**
     * 删除banner
     * 
     * @author htx
     * @param filename
     * @return
     */
    @SuppressWarnings("finally")
    @RequestMapping(value = "/delBanner", method = RequestMethod.POST)
    public JsonAndView addLink(HttpServletRequest request, HttpServletResponse response, String delbanners) {
        Map<String, Object> data = new HashMap<String, Object>();
        JsonAndView jv = new JsonAndView();
        try {
            String[] banners = delbanners.split(",");
            for (int i = 0; i < banners.length; i++) {
                String delbanner = banners[i];
                String directory = request.getSession().getServletContext().getRealPath("static/img/banner");
                File savefile = new File(new File(directory), delbanner);
                if (savefile.exists()) {
                    savefile.delete();
                }
                data.put("state", "sucess");
                data.put("message", " 顶部图片删除成功！ ");
                logDBService.insertNewLog(request, logDBService.DELETE_OPERATION, "顶部图片管理", "删除顶部图片"); 
            }
        } catch (Exception e) {
            data.put("state", "fail");
            data.put("message", " 顶部图片删除失败！ ");
            e.printStackTrace();
        } finally {
            jv.addAllData(data);
            return jv;
        }
    }
}
