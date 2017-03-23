package com.swust.kelab.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swust.kelab.domain.Attachment;
import com.swust.kelab.repos.AttachmentDAO;

/**
 * 处理附件新增、更改、删除等操作的service
 * 
 * @author 吴岘辉
 *
 */
@Service
public class AttachmentService {
    final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private AttachmentDAO attachmentDAO;
    
    /**
     * 新增附件
     * 
     * @param attach  附件信息
     * @return        新保存的附件id，若新增失败，则返回-1
     * @see   Attachment
     */
    public int createAttachment(Attachment attach){
        int result=-1;
        try {
            int change=attachmentDAO.createAttachment(attach);
            if(change>0){
                result=attach.getAttaId();
            }
        } catch(Exception e) {
            logger.error("createAttachment error!\n"+e.getLocalizedMessage());
        }        
        return result;
    }
    
    /**
     * 删除指定附件
     * 
     * @param attachId 附件id
     * @param         路径前缀
     * @return         操作结果
     */
    public String deleteAttachment(int attachId,String filePath){
        String result="fail";
        //删除数据库记录
        int change=-1;
        try {
            change=attachmentDAO.deleteAttachment(attachId);
        } catch(Exception e) {
            logger.error("deleteAttachment error!\n"+e.getLocalizedMessage());
        } 
        if(change>0){        
            //附件存在，则删除        
            File file=new File(filePath);
            if(file.exists()){
                file.delete();
            }
            result="success";
        }
        return result;
    }
    
    /**
     * 增加附件的下载数
     * @param attachId
     * @return
     */
    public String addAttachDownNum(int attachId){
        String result="fail";
        int change=-1;
        try {
            change=attachmentDAO.addAttachDownNum(attachId);
        } catch(Exception e) {
            logger.error("addAttachDownNum error!\n"+e.getLocalizedMessage());
        } 
        if(change>0){
            result="success";
        }
        return result;
    }
    
    /**
     * 通过id获取Attachment
     * 
     * @param attachId 附件id
     * @return         附件对象
     */
    public Attachment viewAttachmentById(int attachId){
        Attachment attach=null;
        try {
            attach=attachmentDAO.selectAttachById(attachId);
        } catch(Exception e) {
            logger.error("selectAttachById error!\n"+e.getLocalizedMessage());
        } 
        return attach;
    }
    /**
     * 查询某类附件的所有信息
     * @param type
     * @return
     */
	public List<Attachment> findAttachmentsByType(int type) {
		return attachmentDAO.findAttachmentsByType(type);
	}
	/**
	 * 添加attachment
	 * @param atta
	 * @return
	 */
	public Integer addAttachment(Attachment atta) {
		// TODO Auto-generated method stub
		return attachmentDAO.addAttachment(atta);
	}

	public Attachment findAttachmentById(int attaId) {
		// TODO Auto-generated method stub
		Attachment a = attachmentDAO.findAttachmentById(attaId);
		return a;
	}
	
	/**
     * 改变附件的原件id
     * 
     * @param sourceId 原件id
     * @param attachId 附件id
     * @return         
     */
    public int changeAttachSourceId(int attachId,int sourceId){
        int change=0;
        try {
            Map query=new HashMap();
            query.put("sourceId",sourceId);
            query.put("attachId",attachId);
            change=attachmentDAO.changeAttachSourceId(query);
        } catch(Exception e) {
            logger.error("selectAttachById error!\n"+e.getLocalizedMessage());
        } 
        return change;
    }
}
