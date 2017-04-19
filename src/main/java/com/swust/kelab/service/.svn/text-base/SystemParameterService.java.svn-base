package com.swust.kelab.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swust.kelab.domain.BbsPost;
import com.swust.kelab.domain.BbsReply;
import com.swust.kelab.domain.SystemParameter;
import com.swust.kelab.model.SystemParameterModel;
import com.swust.kelab.repos.BbsPostDAO;
import com.swust.kelab.repos.BbsReplyDAO;
import com.swust.kelab.repos.SystemParameterDAO;

@Service
public class SystemParameterService {
    final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private BbsPostDAO bbsPostDAO;
    
    @Autowired
    private BbsReplyDAO bbsReplyDAO;

    private SystemParameterDAO systemParameterDAO;

    @Autowired
    public void setSystemParameterDAO(SystemParameterDAO systemParameterDAO) {

        this.systemParameterDAO = systemParameterDAO;
    }

    /**
     * 返回所有参数
     * 
     * @author 李晓伟
     * @param pType
     * @return List<SystemParameterModel> list
     */
    public List<SystemParameter> selectAllSypa(int pType) {
        return systemParameterDAO.selectAllSypa(pType);
    }

    /**
     * 修改参数
     * @author 李晓伟
     * @param list
     * @throws Exception
     */
    public void updateSypa(List<SystemParameterModel> list) throws Exception {

        systemParameterDAO.updateSypa(list);
    }



    /**
     * 修改友情链接
     * @author 李晓伟
     * @param
     * @throw Exception
     */

    public void updateLink(SystemParameter   sypa) throws Exception{
        systemParameterDAO.updateLink(sypa);
    }

    /**
     * 增加友情链接
     * @author 李晓伟
     * @param
     * @throw Exception
     */
    public void addLink(SystemParameter sypa)throws Exception{
        systemParameterDAO.addLink(sypa);
    }

    /**
     * 删除友情链接
     * @author 李晓伟
     * @param
     * @throw Exception
     */
    public void deleteLink(int linkId)throws Exception{
        systemParameterDAO.deleteLink(linkId);
    }
    
    /**
     * 论坛帖子（发帖和回帖）检测
     * 
     * @param userId      发帖人id
     * @param bbsTitle    贴子标题
     * @param bbsContent  帖子内容
     * @return            检测结果
     */
    public String bbsPostReplyCheck(int userId,String bbsTitle,String bbsContent){
        String result="success";
        //发帖时间间隔检查
        BbsPost lastPost=null;
        try {
            lastPost=bbsPostDAO.viewlastPost(userId);
        } catch(Exception e) {
            logger.error("viewlastPost error!\n"+e.getLocalizedMessage());
            return "查询上次发帖失败！";
        }
        BbsReply lastReply=null;
        try {
            lastReply=bbsReplyDAO.viewlastReply(userId);
        } catch(Exception e) {
            logger.error("viewlastReply error!\n"+e.getLocalizedMessage());
            return "查询上次回帖失败！";
        }
        if(lastPost!=null||lastReply!=null){
            //获取时间间隔
            String intervalStr=attainValueByEnName("postInterval");
            if(intervalStr!=null){
                long interval=0;
                try {
                    interval=Long.parseLong(intervalStr);
                } catch(Exception e) {
                    logger.error("parse postInterval error!\n"+e.getLocalizedMessage());
                    return "解析发帖时间间隔参数失败！";
                }                    
                if(interval>0){
                    Date now=new Date();
                    if(lastPost!=null&&lastPost.getBbpoTime()
                            .getTime()+interval*1000L>now.getTime()
                            ||lastReply!=null&&lastReply.getBbreTime()
                            .getTime()+interval*1000L>now.getTime()){
                        return "你的发帖过于频繁！最短的发帖时间间隔为："
                            +interval+"秒。";
                    }
                }
            }else{
                return "获取发帖时间间隔参数失败！";
            }
        }
        
        //帖子长度检查
        String minLengthStr=attainValueByEnName("minWordsNum");
        if(minLengthStr!=null){
            int minLength=0;
            try {
                minLength=Integer.parseInt(minLengthStr);
            } catch(Exception e) {
                logger.error("parse minLength error!\n"+e.getLocalizedMessage());
                return "解析帖子最短长度参数失败！";
            }  
            if(bbsContent==null||bbsContent.length()<minLength){
                return "帖子内容太短！帖子的最短长度为："
                        +minLength+"个字。";
            }
        }else{
            return "获取帖子最短长度参数失败！";
        }
        
        //敏感信息检查
        String sensitiveWords=attainValueByEnName("sensitiveWords");
        if(sensitiveWords!=null){
            if(sensitiveWords.length()<=0){
                return result;
            }
            String[] words=sensitiveWords.split(";");
            if(bbsTitle!=null&&bbsTitle.length()>0){
                for (String word : words) {
                    if(bbsTitle.contains(word)){
                        return  "帖子标题包含违禁词语！";
                    }
                } 
            }
            if(bbsContent!=null&&bbsContent.length()>0){
                for (String word : words) {
                    if(bbsContent.contains(word)){
                        return  "帖子内容包含违禁词语！";
                    }
                } 
            }                
        }else{
            return "获取帖子敏感词参数失败！";
        }
        return result;
    }
    
    /**
     * 论坛的附件检测
     * @param format  附件的后缀，比如“exe”
     * @param size    附件大小，单位为字节
     * @return        检测结果
     */
    public String bbsAttachCheck(String format,long size){
        String result="success";
        //附件格式检测
        if(format==null||format.length()<=0){
            return "论坛不支持无格式文件！";
        }
        String formats=attainValueByEnName("fileFormats");
        if(formats!=null){
            if(formats.length()>0){
                String[] formatArray=formats.split(";");
                int i;
                for(i=0;i<formatArray.length;i++){
                    if(format.equals(formatArray[i])){
                        break;
                    }
                }
                if(i>=formatArray.length){
                    return "文件格式不支持！";
                }
            }
        }else{
            return "获取允许的附件格式失败！";
        }
        
        //附件大小检测
        String limitStr=attainValueByEnName("fileSizeLimit");
        if(limitStr!=null){
            if(limitStr.length()<=0){
                return result;
            }
            long sizeLimit=0;
            try {
                sizeLimit=Long.parseLong(limitStr);
            } catch(Exception e) {
                logger.error("parse fileSizeLimit error!\n"+e.getLocalizedMessage());
                return "解析附件最大限制参数失败！";
            }
            if(size>sizeLimit*1024*1024){
                return "文件过大！允许的文件大小上限为："+sizeLimit+"兆。";
            }
        }else{
            return "获取附件大小限制参数失败！";
        }
        return result;
    }
    
    /**
     * 通过英文名，获取相应的系统参数
     * @param enName  参数的英文名
     * @return        参数值
     */
    public String attainValueByEnName(String enName){
        String result=null;
        if(enName==null||enName.length()<=0){
            return result;
        }
        SystemParameter syspam=null;
        try {
            syspam=systemParameterDAO
                    .viewOneParameterByEnName(enName);
        } catch(Exception e) {
            logger.error("viewOneParameterByEnName error!\n"+e.getLocalizedMessage());
            return result;            
        }
        if(syspam!=null){
            result=syspam.getSypaValue();
        }
        return result;
    }

}
