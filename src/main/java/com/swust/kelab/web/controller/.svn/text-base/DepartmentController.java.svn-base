package com.swust.kelab.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.support.ServletContextResource;
import org.springframework.web.multipart.MultipartFile;

import com.swust.kelab.domain.Department;
import com.swust.kelab.model.CommonQuery;
import com.swust.kelab.model.QueryData;
import com.swust.kelab.service.DepartmentService;
import com.swust.kelab.service.LogDBService;
import com.swust.kelab.web.json.JsonAndView;

/**
 * 部门操作的controller
 * 
 * @author 吴岘辉
 *
 */
@Controller
@RequestMapping("/department")
public class DepartmentController {
    final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private LogDBService logDBService;
    
    /**
	 * 通过部门id和部门类型查询部门列表<br>
	 * @param depaType
	 * @param depaIds
	 * @author easonlian
	 */
    @RequestMapping(value="/viewAllDepaListBelongsToParentDepar.do")
	public JsonAndView viewAllDepaListBelongsToParentDepar(
			@RequestParam(value="depaType",required=true) Integer depaType,
			@RequestParam(value="depaParentIds",required=false) String depaParentIds) {
		JsonAndView jav = new JsonAndView();
		List<Department> depaList = 
				departmentService.viewAllDepaListBelongsToParentDepar(depaType,depaParentIds);
		return jav.addData("deparList", depaList);
	}

    /**
     * 根据机构类型查询所有该类型的机构
     * @param type 部门类型，1表示市，2表示区，3表示学校，4表示所有
     * @return 
     */
    @RequestMapping(value="/viewDepartments",method=RequestMethod.POST)
    public JsonAndView viewDepartmentList(int type){
        JsonAndView jav=new JsonAndView();
        List<Department> departments=departmentService.findDepartmentsByType(type);
        //封装返回结果
        if(departments!=null){
            for (Department department : departments) {
                department.setDepaAbbreviation("");
                department.setDepaCode("");
            }
            Map result=new HashMap();
            result.put("departments", departments);
            jav.addAllData(result);
        }else{
            jav.setRet(false);
            jav.setErrcode(1);
            if(type==3){
                jav.setErrmsg("获取学校列表失败！");
            }
            if(type==2){
                jav.setErrmsg("获取区级列表失败！");
            }
        }
        return jav;
    }
    
    
    /**
     * 根据机构类型查询所有该类型上级机构
     * @param type 部门类型，1表示市，2表示区，3表示学校，4表示所有
     * @return 
     */
    @RequestMapping(value="/viewParentDepartments",method=RequestMethod.POST)
    public JsonAndView viewParentDepartmentList(int type){
        JsonAndView jav=new JsonAndView();
        List<Department> departments=departmentService.findParentDepartmentsByType(type);
        //封装返回结果
        if(departments!=null){
            for (Department department : departments) {
                department.setDepaAbbreviation("");
                department.setDepaCode("");
            }
            Map result=new HashMap();
            result.put("departments", departments);
            jav.addAllData(result);
        }else{
            jav.setRet(false);
            jav.setErrcode(1);
            if(type==3){
                jav.setErrmsg("获取学校列表失败！");
            }
            if(type==2){
                jav.setErrmsg("获取区级列表失败！");
            }
        }
        return jav;
    }
    /**
     * 根据上级组织条件检索其下属机构
     * @author pery
     * @param parentId 上级组织id
     * @param keyword 检索关键词
     * @return 返回机构列表
     */
    @RequestMapping(value="viewDepartmentsByParent",method=RequestMethod.POST)
    public JsonAndView viewDepartmentByParent(int parentId,String keyword){
        JsonAndView jav=new JsonAndView();
        List<Department> departmentList = null;
        departmentList = departmentService.queryDepartmentByParent(parentId, keyword);
        if(departmentList.size()>0){
            Map result=new HashMap<String,List<Department>>();
            result.put("departments", departmentList);
            jav.addAllData(result);
        }else{
            jav.setErrmsg("sorry!没有查询到数据");
        }
        return jav;
    }
    /**
     * 根据departmentId修改该机构信息
     * @author pery
     * @param department 机构对象
     * @param request    请求对象，用于记录日志
     * @return
     */
    @RequestMapping(value="updateDepartment",method=RequestMethod.POST)
    public JsonAndView updateDepartment(Department department
            ,HttpServletRequest request){
        JsonAndView jav=new JsonAndView();
        String result = departmentService.updateOneDepartment(department);
        if(!result.equals("success")){
            jav.setRet(false);
            jav.setErrmsg("更新失败");
            return jav;
        }else{
            //日志记录
            logDBService.insertNewLog(request, 
                    LogDBService.UPDATA_OPERATION, "机构管理",
                    "修改机构："+department.getDepaName());
        }
        return jav;
    }
    /**
     * 根据机构id字符串批量删除机构，id以逗号分隔
     * @author pery
     * @param departmentIds eg：1,2,4
     * @param request    请求对象，用于记录日志
     * @return
     */
    @RequestMapping(value="deleteDepartments",method=RequestMethod.POST)
    public JsonAndView deleteDepartment(String departmentIds
            ,HttpServletRequest request){
        JsonAndView jav=new JsonAndView();
        String result = departmentService.deleteBatchDepartment(departmentIds);
        if(result.endsWith("fail")){
            jav.setRet(false);
            jav.setErrmsg("部分或全部机构删除失败！可能原因是：1、该机构的下属机构未被删除；2、存在用户属于该机构！");
            return jav;
        }else{
            //日志记录
            logDBService.insertNewLog(request, 
                    LogDBService.DELETE_OPERATION, "机构管理",
                    "删除机构，ID包括："+departmentIds);
        }
        return jav;
    }
    /**
     * 新增一个机构
     * @author pery
     * @param department
     * @param request    请求对象，用于记录日志
     * @return
     */
    @RequestMapping(value="addDepartment",method=RequestMethod.POST)
    public JsonAndView addDepartment(Department department
            ,HttpServletRequest request){
        JsonAndView jav=new JsonAndView();
        if(department.getDepaType()==1){
            List<Department>  departs=departmentService
                    .findDepartmentsByType(1);
            if(departs!=null&&departs.size()>0){
                jav.setRet(false);
                jav.setErrmsg("新增失败!市级机构只能拥有一个，且已存在市级机构！");
                return jav;
            }
        }
        int result = departmentService.insertDepartment(department);
        if(result>0){
            //日志记录
            logDBService.insertNewLog(request, 
                    LogDBService.ADD_OPERATION, "机构管理",
                    "新增机构："+department.getDepaName());
            return jav;            
        }else{
            jav.setRet(false);
            jav.setErrmsg("新增失败");
            return jav;
        }
    }
    /**
     * 机构筛选查询
     * @author pery
     * @param query 查询对象，包括 搜索字段,searchType 为1：按名称搜索，2：部门简称 3：部门代码
     *  和搜索关键词，以及分页信息
     * @param screenType 筛选条件 0：全部，1,：市，2：区，3：学校
     * @param parentId 上级机构id
     * @param departId 机构id
     * @return 查询结果JSON对象
     */
    @RequestMapping(value="searchByMutiTerm",method=RequestMethod.POST)
    public JsonAndView queryByMutiTerm(CommonQuery query,int screenType,int parentId,int departId){
        JsonAndView jav = new JsonAndView();
        QueryData qd = departmentService.queryByMutiTerm(query,screenType,parentId,departId);
        if(qd!=null){
            Map result=new HashMap<String,List<Department>>();
            jav.addData("totalPage", qd.getTotalPage());
            jav.addData("totalCount", qd.getTotalCount());
            jav.addData("pageData", qd.getPageData());
        }else{
            jav.setRet(false);
            jav.setErrmsg("sorry!没有查询到数据");
        }
        return jav;
    }
    /***
     * 通过筛选导出全部查询结果并下载excel
     * @author pery
     * @param query 查询对象-包括 搜索字段,searchType 为1：按名称搜索，2：部门简称 3：部门代码
     *  和搜索关键词
     * @param screenType  筛选条件 0：全部，1,：市，2：区，3：学校
     * @param request
     * @param response
     */
    @RequestMapping(value = "/exportExcel", method = RequestMethod.GET)
    public void exportExcel(CommonQuery query,int screenType, HttpServletRequest request, HttpServletResponse response){
        String basePath = request.getSession().getServletContext().getRealPath("/");
        String filePath =departmentService.queryAndGetExcel(basePath, query, screenType);
        if(filePath!=null){
            ServletContextResource downFile = new ServletContextResource(request.getSession().getServletContext(), filePath);
            if(!downFile.exists()){
                return ;
            }
            String fileName=filePath.substring(filePath.lastIndexOf("/")+1);
            String agintname = request.getHeader("User-Agent").toUpperCase();
            try {
                fileName=new String(fileName.getBytes(),"ISO-8859-1");
                if (agintname.indexOf("MSIE") > 0)
                    fileName = URLEncoder.encode(fileName,"ISO-8859-1");//IE浏览器
            } catch (UnsupportedEncodingException e1) {
                // TODO Auto-generated catch block
                logger.error("写入输出流失败！\n"+e1.getLocalizedMessage());
            }
            
            OutputStream os = null;
            try {
                os = response.getOutputStream();
                response.reset();
                response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
                response.setContentType("application/octet-stream; charset=utf-8");
                os.write(FileUtils.readFileToByteArray(downFile.getFile()));
                os.flush();
            }catch (Exception e) {
                logger.error("写入输出流失败！\n"+e.getLocalizedMessage());
            } finally {
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        logger.error("关闭输出流失败！\n"+e.getLocalizedMessage());
                    }
                }
                //删除临时文件
                try {
                    downFile.getFile().delete();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    logger.error("删除临时文件失败！\n"+e.getLocalizedMessage());
                }
            }
        }
    }
    
    /**
     * 批量导入部门，仅支持导入excel文件
     * 
     * @param postfile  导入文件
     * @param request   请求对象
     * @param response  相应对象
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public void importExcel(MultipartFile postfile, 
            HttpServletRequest request,HttpServletResponse response){        
        //获取excel表单
        HSSFSheet sheet=null;
        if(postfile==null){
            try {
                PrintWriter pw=response.getWriter();
                pw.write("{'ret':false,'errcode':1,'errmsg':'获取上传文件失败！'}");
                pw.flush();
                pw.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                logger.error("use response's printWriter errror!"+e.getLocalizedMessage());
            }
            return;
        }
        String tempFilePath=request.getSession().getServletContext()
                .getRealPath("upload/temp/"+System.currentTimeMillis()
                +".xls");
        try {
            if(postfile.getOriginalFilename()!=null&&postfile.getOriginalFilename().endsWith(".xls")){
                File file = new File(tempFilePath);
                if(!file.getParentFile().exists()){
                    file.getParentFile().mkdir();
                }
                postfile.transferTo(file);
                POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(new File(tempFilePath)));
                HSSFWorkbook wb = new HSSFWorkbook(fs);  
                sheet=wb.getSheetAt(0);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error("获取Excel工作表失败！\n"+e.getLocalizedMessage());
        }
        if(sheet==null){
            try {
                PrintWriter pw=response.getWriter();
                pw.write("{'ret':false,'errcode':2,'errmsg':'获取Excel工作表失败！'}");
                pw.flush();
                pw.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                logger.error("use response's printWriter errror!"+e.getLocalizedMessage());
            }
            return;
        }
        //解析excel
        List<Department> departs=departmentService
                .parseExcelToDeparts(sheet);
        if(departs==null||departs.size()<=0){
            try {
                PrintWriter pw=response.getWriter();
                pw.write("{'ret':false,'errcode':3,'errmsg':'无数据或者解析Excel工作表失败，请检查上传文件的内容格式！'}");
                pw.flush();
                pw.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                logger.error("use response's printWriter errror!"+e.getLocalizedMessage());
            }
            return;
        }
        String cityStr="";
        List<Department>  citys=departmentService
                .findDepartmentsByType(1);
        if(citys!=null&&citys.size()>0){
            for (int i=0;i<departs.size();i++) {
                Department d=departs.get(i);
                if(d.getDepaType()==1){
                    if(cityStr.length()<=0){
                        cityStr="机构导入失败！因为已经有该机构！";
                    }
                    departs.remove(i);
                    i--;
                }
            }
        }
        
        //批量插入
        String result=departmentService.batchInsertDeparts(departs);
        //防止返回数据被IE当做下载流
        response.reset();
        response.setContentType("text/html; charset=utf-8");
        try {
            PrintWriter pw=response.getWriter();
            if(result.equals("success")&&cityStr.length()<=0){
                pw.write("{'ret':true}");
            }else{
                pw.write("{'ret':false,'errcode':4,'errmsg':'批量导入机构部分或者全部失败,请检查导入文件！"+cityStr+"'}");
            }
            pw.flush();
            pw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.error("use response's printWriter errror!"+e.getLocalizedMessage());
        }
        File tempfile=new File(tempFilePath);
        if(tempfile.exists()){
            tempfile.delete();
        }
    }
}
