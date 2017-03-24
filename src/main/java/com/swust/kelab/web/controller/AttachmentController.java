package com.swust.kelab.web.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.swust.kelab.domain.Attachment;
import com.swust.kelab.service.AttachmentService;
import com.swust.kelab.web.json.JsonAndView;

/**
 * 负责处理附件相关操作的控制器
 * 
 * @author 吴岘辉
 *
 */
@Controller
@RequestMapping("/attachment")
public class AttachmentController {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private AttachmentService attachmentService;
    
    /**
     * 删除附件
     * 
     * @param attachId 附件id 
     * @param request 请求对象，用来获取附件的真实路径
     * @return 操作结果
     * @see JsonAndView
     */
    @RequestMapping(value="/delete",method=RequestMethod.POST)
    public JsonAndView deleteAttachment(int attachId,HttpServletRequest request){
        JsonAndView jav=new JsonAndView();
        Attachment attach=attachmentService.viewAttachmentById(attachId);
        if(attach==null){
            jav.setRet(false);
            jav.setErrcode(1);
            jav.setErrmsg("找不到相应的附件！");
            return jav;
        }
        String filePath = request.getSession().getServletContext().getRealPath("/") + "/" + attach.getAttaLocation() + attach.getAttaFilename();
        String result=attachmentService.deleteAttachment(attachId,filePath);
        if(result!="success"){
            jav.setRet(false);
            jav.setErrcode(2);
            jav.setErrmsg("删除附件失败！");
        }
        return jav;
    }
    /**
     * 查询某类附件的所有信息
     * @param type
     * @return
     */
    @RequestMapping(value="/findAttachment",method=RequestMethod.POST)
    public JsonAndView findAttachmentsByType(int type){
        JsonAndView jav=new JsonAndView();
        List<Attachment> attach=attachmentService.findAttachmentsByType(type);
       jav.addData("data", attach);
        return jav;
    }
    
    /**
	 * 添加attachment
	 * @param atta
	 * @return
	 */
    @RequestMapping(value="/add",method=RequestMethod.POST)
    public JsonAndView addAttachment(MultipartFile logopic,Attachment atta,HttpServletRequest request,HttpServletResponse response){
        JsonAndView jav=new JsonAndView();
        atta.setAttaLocation("upload/");
        Integer attach=attachmentService.addAttachment(atta);
        jav.addData("data", attach);
        
        try {
        	logopic.transferTo(new File(request.getSession().getServletContext().getRealPath("/upload/"+atta.getAttaFilename())));
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			jav.addData("errormsg", "sava attachment fail");
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("use response's printWriter errror!"+e.getLocalizedMessage());
			e.printStackTrace();
		}
        return jav;
    }
    
    
    /**
     * @author ZhangXin
     * 
     * 通过附件id查询附件
     * @param type
     * @return
     */
    @RequestMapping(value="/findAttachmentById",method=RequestMethod.POST)
    public JsonAndView findAttachmentById(int attaId){
        JsonAndView jav=new JsonAndView();
        Attachment attach=attachmentService.findAttachmentById(attaId);
        jav.addData("attachment", attach);
        return jav;
    }
}
