package com.swust.kelab.web.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

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
import org.springframework.web.multipart.MultipartFile;

import com.swust.kelab.domain.Attachment;
import com.swust.kelab.service.AttachmentService;
import com.swust.kelab.service.SystemParameterService;
import com.swust.kelab.web.json.JsonAndView;

/**
 * 负责处理文件上传下载的控制器
 * 
 * @author 吴岘辉
 *
 */
@Controller
@RequestMapping("/load")
public class FileUpDownloadController {
    final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private AttachmentService attachmentService;
    
    @Autowired
    private SystemParameterService systemParameterService;
    
    /**
     * 文件下载
     * 
     * @param  attach    附件对象，包含了文件的基本信息
     * @param request    请求对象
     * @param response   返回对象
     * 
     * @throws IOException
     */
    @RequestMapping(value="/download",method=RequestMethod.GET)  
    public void downloadFile(Attachment attach,HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        //获取文件资源
        if((attach.getAttaLocation()==null
                ||attach.getAttaLocation().isEmpty()
                ||attach.getAttaFilename()==null
                ||attach.getAttaFilename().isEmpty())
                &&attach.getAttaId()!=null
                &&attach.getAttaId()>0){
            try {
                attach=attachmentService.findAttachmentById(
                        attach.getAttaId());
            } catch (Exception e) {
                // TODO: handle exception
                logger.error("findAttachmentById error!\n"
                +e.getLocalizedMessage());
            }
        }
        //获取文件路径
        String filePath=attach.getAttaLocation()+attach.getAttaFilename();
        if(filePath==null||filePath.isEmpty()){
            return;
        }
        ServletContextResource downFile=new ServletContextResource(
                request.getSession().getServletContext(), filePath);
        if(!downFile.exists()){
            logger.error("找不到文件或者文件不存在！！filePath:"+filePath);
            try {
                response.reset();
                response.sendRedirect("../../static/html/down_error.html");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return;
        }
        
        //若为附件，则增加附件的下载数
        if(attach.getAttaId()>0){
            String result=attachmentService.addAttachDownNum(
                    attach.getAttaId());
            if(result!="success"){
                logger.error("增加文件下载数失败！");
            }
        }
        
        //封装下载
        String sourceName=attach.getAttaOriFilename();
        if(sourceName==null||sourceName.isEmpty()){
            sourceName=attach.getAttaFilename();
        }
        String agintname = request.getHeader("User-Agent").toUpperCase();
        sourceName=new String(sourceName.getBytes(),"ISO-8859-1");
        if (agintname.indexOf("MSIE") > 0)
            sourceName = URLEncoder.encode(sourceName,"ISO-8859-1");//IE浏览器
        OutputStream os = response.getOutputStream();  
        try {  
            response.reset();  
            response.setHeader("Content-Disposition", "attachment; filename="+sourceName);  
            response.setContentType("application/octet-stream; charset=utf-8");  
            os.write(FileUtils.readFileToByteArray(downFile.getFile()));  
            os.flush();  
        } finally {  
            if (os != null) {  
                os.close();  
            }  
        }
        //下面的写法不支持IE
        /*HttpHeaders headers = new HttpHeaders();  
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);  
        headers.setContentDispositionFormData("attachment", sourceName);  
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(downFile.getFile()),  
                                          headers, HttpStatus.CREATED);*/  
    }
    
    /**
     * 文件上传
     * 
     * @param file      文件对象，对应 <input type="file" name="file"/>
     * @param fileType  附件类型 1表示主贴、2表示回帖、3表示试题、4表示选项、5表示首页logo、6表示课程封面、7表示成绩导出和确认的文件
     * @param location  附件保存位置，不同类型的附件保存位置一般不同，比如fileType为7，则保存位置为"upload/score/"
     * @return          保存的附件id
     * @see MultipartFile,Attachment
     */
    @RequestMapping(value="/upload",method=RequestMethod.POST) 
    public void uploadFile(MultipartFile postfile,int fileType,String location,
            HttpServletRequest request,HttpServletResponse response){
        //获取原文件名和文件格式
        String oriFileName=postfile.getOriginalFilename();
        String type="";
        int position = oriFileName.lastIndexOf(".");        
        if(position>=0){
            type=oriFileName.substring(position+1);
        }
        //附件检测
        if(fileType==1||fileType==2){
            String checkResult=systemParameterService.bbsAttachCheck(type, 
                    postfile.getSize());
            if(checkResult==null||!checkResult.equals("success")){
                try {
                    response.reset();
                    response.setContentType("text/html; charset=utf-8");
                    PrintWriter pw=response.getWriter();
                    pw.write("{'ret':false,'errcode':1,'errmsg':'上传附件失败！"+checkResult+"'}");
                    pw.flush();
                    pw.close();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    logger.error("use response's printWriter errror!"+e1.getLocalizedMessage());
                }
                return;
            }
        }
        
        //保存到数据库
        Attachment attach=new Attachment();
        String newFileName=(int)(Math.random()*1000)+"5"+System.currentTimeMillis()+"5"+(int)(Math.random()*10000)+"5"+"."+type;
        attach.setAttaFilename(newFileName);
        if(location==null||location.isEmpty()){
            location="upload/";
        }
        attach.setAttaLocation(location);
        attach.setAttaOriFilename(oriFileName);
        attach.setAttaSourceType(fileType);
        int attachId=attachmentService.createAttachment(attach);
        if(attachId<=0){
            try {
                response.reset();
                response.setContentType("text/html; charset=utf-8");
                PrintWriter pw=response.getWriter();
                pw.write("{'ret':false,'errcode':1,'errmsg':'存储附件到数据库失败！'}");
                pw.flush();
                pw.close();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                logger.error("use response's printWriter errror!"+e1.getLocalizedMessage());
            }
            return;
        }
        //存储文件
        try {
            postfile.transferTo(new File(request.getSession().getServletContext().getRealPath(location+newFileName)));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error("存储文件失败！\n"+e.getLocalizedMessage());
            try {
                response.reset();
                response.setContentType("text/html; charset=utf-8");
                PrintWriter pw=response.getWriter();
                pw.write("{'ret':false,'errcode':2,'errmsg':'存储文件失败！'}");
                pw.flush();
                pw.close();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                logger.error("use response's printWriter errror!"+e1.getLocalizedMessage());
            }
            return;
        }
        //防止返回数据被IE当做下载流
        response.reset();
        response.setContentType("text/html; charset=utf-8");
        try {
            PrintWriter pw=response.getWriter();
            pw.write("{'ret':true,'data':{'attachId':"+attachId+",'fileName':'"+oriFileName+"','realName':'"+newFileName+"'}}");
            pw.flush();
            pw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.error("use response's printWriter errror!"+e.getLocalizedMessage());
        }
        
    }
}
