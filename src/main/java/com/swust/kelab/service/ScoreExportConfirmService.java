package com.swust.kelab.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.swust.kelab.domain.Attachment;
import com.swust.kelab.domain.Course;
import com.swust.kelab.domain.Department;
import com.swust.kelab.domain.ScoreExportConfirm;
import com.swust.kelab.domain.User;
import com.swust.kelab.model.CommonQuery;
import com.swust.kelab.model.CourseSelectModel;
import com.swust.kelab.model.PageData;
import com.swust.kelab.model.QueryData;
import com.swust.kelab.model.ScoreExportConfirmModel;
import com.swust.kelab.repos.AttachmentDAO;
import com.swust.kelab.repos.CourseDAO;
import com.swust.kelab.repos.DepartmentDao;
import com.swust.kelab.repos.ReSelectCourseDAO;
import com.swust.kelab.repos.ScoreExportConfirmDAO;
import com.swust.kelab.repos.UserDAO;
import com.swust.kelab.repos.bean.ListQuery;
import com.swust.kelab.utils.ExportAsExcel;
import com.swust.kelab.utils.FormatUtil;

@Service
public class ScoreExportConfirmService {
    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ScoreExportConfirmDAO scoreExportConfirmDAO;
    
    @Autowired
    private DepartmentDao departmentDao;
    
    @Autowired
    private AttachmentDAO attachmentDAO;
    
    @Autowired
    private CourseDAO courseDAO;
    
    @Autowired
    private UserDAO userDAO;
    
    @Autowired
    private ReSelectCourseDAO reSelectCourseDAO;
    
    /**
     * 插入导出成绩的记录
     * @param sec 导出信息
     * @return  新产生的id
     */
    public int insertScoreExport(ScoreExportConfirm sec){
        int newId=0;
        try {
            scoreExportConfirmDAO.createExportConfirm(sec);
        } catch (Exception e) {
            logger.error("createExportConfirm error!");
        }
        newId=sec.getSecoId();
        return newId;
    }
    
    /**
     * 请求成绩导出记录列表
     * 
     * @param query         其中的searchWord指定导出人或者确认人的名称
     * @param confirmOrNot  是否确认，0表示所有，1表示已确认，2表示未确认
     * @param exportDepaId  导出机构id，0表示所有
     * @param confirmDepaId 确认机构id,0表示所有
     * @return  返回导出的记录列表
     */
    public QueryData viewExportConfirmList(CommonQuery query,
            int confirmOrNot,int exportDepaId,int confirmDepaId){
        QueryData qd=new QueryData();
        //构造查询条件
        ListQuery myQuery = query.format();
        if(confirmOrNot>0){
            myQuery.fill("confirm", confirmOrNot); 
        }
        if(exportDepaId>0){
            myQuery.fill("exportDepartId", exportDepaId);
        }
        if(confirmDepaId>0){
            myQuery.fill("confirmDepartId", confirmDepaId);
        }
        //统计总数
        int totalCount=0;
        try {
            totalCount=scoreExportConfirmDAO.countTotalExportNum(myQuery);
        } catch (Exception e) {
            logger.error("countTotalExportNum error!");
            return null;
        }
        qd.setTotalCount(totalCount);
        if(totalCount<=0){
            return qd;
        }
        //分页处理
        if (query.getRecordPerPage() <= 0) {
            query.setRecordPerPage(10);
        }
        if (query.getPageArray() == null) {
            query.setPageArray(new int[] { 1, 2, 3 });
        }
        int totalPage = QueryData.computeTotalPage(totalCount,
                query.getRecordPerPage());
        qd.setTotalPage(totalPage);
        int startIndex = (query.getPageArray()[0] - 1)
                * query.getRecordPerPage();
        int fetchSize = query.getPageArray().length * query.getRecordPerPage();
        myQuery.fill("startIndex", startIndex);
        myQuery.fill("maxCount", fetchSize);
        
        //查询记录
        List<ScoreExportConfirmModel> data=null;
        try {
            data=scoreExportConfirmDAO.viewExportConfirmsByQuery(myQuery);
        } catch (Exception e) {
            logger.error("viewExportConfirmsByQuery error!");
            return null;
        }
        if(data==null || data.size()<=0){
            return qd;
        }
        
        //转换时间、请求机构信息
        Map<Integer,String> depaNames=new HashMap<Integer,String>();
        Map<Integer,Integer> depaTypes=new HashMap<Integer,Integer>();
        List<Department> departs=null;
        try {
            departs=departmentDao.queryDepatmentByType(0);
        } catch (Exception e) {
            logger.error("queryDepatmentByType error!");
            return null;
        }
        if(departs!=null&&departs.size()>0){
            for (Department d : departs) {
                depaNames.put(d.getDepaId(), d.getDepaName());
                depaTypes.put(d.getDepaId(), d.getDepaType());
            }
        }
        for (ScoreExportConfirmModel sec : data) {
            //时间
            if(sec.getSecoExportTime()!=null){
                sec.setExportTimeStr(FormatUtil.formatDate(
                        sec.getSecoExportTime()));
            }else{
                sec.setExportTimeStr("");
            }
            if(sec.getSecoConfirmTime()!=null){
                sec.setConfirmTimeStr(FormatUtil.formatDate(
                        sec.getSecoConfirmTime()));
            }else{
                sec.setConfirmTimeStr("");
            }
            //机构
            if(sec.getExportDepartId()!=null){
                if(depaNames.containsKey(
                        sec.getExportDepartId())){
                    sec.setExportDepartName(depaNames.get(
                            sec.getExportDepartId()));
                }
                if(depaTypes.containsKey(sec.getExportDepartId())){
                    sec.setExportDepartType(depaTypes.get(
                            sec.getExportDepartId()));
                }
            }else{
                sec.setExportDepartName("");
            }
            if(sec.getConfirmDepartId()!=null
                    &&depaNames.containsKey(
                    sec.getConfirmDepartId())){
                sec.setConfirmDepartName(depaNames.get(
                        sec.getConfirmDepartId()));
            }else{
                sec.setConfirmDepartName("");
            }            
        }
        //封装数据，构造结果页
        List<PageData> pageDataList = Lists.newArrayList();  
        for (int k = 0; k < query.getPageArray().length; k++) {
            int page = query.getPageArray()[k];
            if (page <= 0 || page > totalPage) {
                continue;
            }
            startIndex=k*query.getRecordPerPage();
            int endIndex=startIndex+query.getRecordPerPage();
            if(startIndex>=data.size()){
                continue;
            }
            if(endIndex>data.size()){
                endIndex=data.size();
            }
            List<ScoreExportConfirmModel> pageDatas=data.subList(startIndex, endIndex);
            pageDataList.add(new PageData(page, pageDatas));
        }
        qd.setPageData(pageDataList);
        return qd;
    }
    
    /**
     * 确认一次导出记录
     * 
     * @param basePath        文件的基本路径
     * @param exportId        原始导出记录的id，对应数据库中的seco_id
     * @param confirmAttaId   确认的附件id
     * @param confirmRemark   确认的备注信息（前台应限制字数，否则后台会存储失败）
     * @param confirmUserId   确认人id
     * @return    返回操作是否成功的提示信息
     */
    public String confirmOneExport(String basePath,int exportId,
            int confirmAttaId,String confirmRemark,int confirmUserId){
        String result="确认操作成功！";
        //获取确认文件
        File confirmFile=null;
        Attachment confirmAttach=null;
        try {
            confirmAttach=attachmentDAO.findAttachmentById(
                    confirmAttaId);
        } catch (Exception e) {
            logger.error("findAttachmentById error!\n"+e.getMessage());
            return null;
        }
        if(confirmAttach==null){
            result="确认失败，查找上传的确认附件记录失败！";
            return result;
        }
        confirmFile=new File(basePath+"/"
                +confirmAttach.getAttaLocation()
                +confirmAttach.getAttaFilename());
        if(confirmFile==null||!confirmFile.exists()){
            return "确认失败，上传的确认附件已不存在！";
        }
        
        //获取原导出文件
        File sourceFile=null;
        if(exportId>0){
            ScoreExportConfirmModel secm=null;
            try {
                secm=scoreExportConfirmDAO.viewOneExportById(exportId);
            } catch (Exception e) {
                logger.error("viewOneExportById error!\n"+e.getMessage());
                //删除确认文件
                attachmentDAO.deleteAttachment(confirmAttaId);
                confirmFile.delete();
                return null;
            }
            if(secm==null){
                result="确认失败，查找原始导出记录失败！";
                //删除确认文件
                attachmentDAO.deleteAttachment(confirmAttaId);
                confirmFile.delete();
                return result;
            }
            Attachment sourceAttach=null;
            try {
                sourceAttach=attachmentDAO.findAttachmentById(
                        secm.getExportAttachId());
            } catch (Exception e) {
                logger.error("findAttachmentById error!\n"+e.getMessage());
                //删除确认文件
                attachmentDAO.deleteAttachment(confirmAttaId);
                confirmFile.delete();
                return null;
            }
            if(sourceAttach==null){
                //删除确认文件
                attachmentDAO.deleteAttachment(confirmAttaId);
                confirmFile.delete();
                result="确认失败，查找导出时的附件记录失败！";
                return result;
            }
            sourceFile=new File(basePath+"/"
                    +sourceAttach.getAttaLocation()
                    +sourceAttach.getAttaFilename());
        }
        if(sourceFile==null||!sourceFile.exists()){
            //删除确认文件
            attachmentDAO.deleteAttachment(confirmAttaId);
            confirmFile.delete();
            return "确认失败，导出时的文件已不存在！";
        }
        
        //开始进行确认
        List<CourseSelectModel> successConfirm=
                new ArrayList<CourseSelectModel>(); //保存确认成功的记录
        File resultFile=null;
        String location="upload/score/";
        try {
            resultFile=ExportAsExcel.confirmExportInfo(basePath,
                    location,sourceFile, confirmFile,successConfirm);
        } catch (Exception e) {
            logger.error("Confirm progress caused error!\n"+e.getMessage());
            //删除确认文件
            attachmentDAO.deleteAttachment(confirmAttaId);
            confirmFile.delete();
            return "确认失败，请检查确认文件与原导入文件是否匹配!";
        }
        if(resultFile==null){
            //删除确认文件
            attachmentDAO.deleteAttachment(confirmAttaId);
            confirmFile.delete();
            return "确认失败，请检查确认文件与原导入文件是否匹配！";
        }
        
        //更新选课表的确认字段
        if(successConfirm.size()>0){
            //查询所有课程
            List<Course> courses=null;
            try {
                courses=courseDAO.viewCourseBaseInfor();
            } catch (Exception e) {
                logger.error("viewCourseBaseInfor error!\n"+e.getMessage());
                //删除确认文件
                attachmentDAO.deleteAttachment(confirmAttaId);
                confirmFile.delete();
                return null;
            }
            Map<String,Integer> courIds=new HashMap<String, Integer>();
            if(courses!=null&&courses.size()>0){
                for (Course course : courses) {
                    courIds.put(course.getCourName(), course.getCourId());
                }
            }
            //缓存学生的学籍号和id
            Map<String,Integer> stuIds=new HashMap<String, Integer>();
            for(CourseSelectModel csm:successConfirm){
                String stuId=csm.getStuNum();
                int userId=0;
                if(stuIds.containsKey(stuId)){
                    userId=stuIds.get(stuId);
                }else{
                    User query=new User();
                    query.setUserLoginname(stuId);
                    List<User> users=null;                    
                    try {
                        users=userDAO.findUsersByUser(query);
                    } catch (Exception e) {
                        logger.error("findUsersByUser error!\n"+e.getMessage());
                        //删除确认文件
                        attachmentDAO.deleteAttachment(confirmAttaId);
                        confirmFile.delete();
                        return null;
                    }
                    if(users!=null&&users.size()==1){
                        userId=users.get(0).getUserId();
                        stuIds.put(stuId, userId);
                    }
                }
                int courId=0;
                String courName=csm.getCourseName();
                if(courIds.containsKey(csm)){
                    courId=courIds.get(courName);
                }
                if(userId>0&&courId>0){
                    try {
                        Map query=new HashMap();
                        query.put("userId", userId);
                        query.put("courseId", courId);
                        query.put("confirmId", exportId);
                        reSelectCourseDAO.confirmOneScore(query);
                    } catch (Exception e) {
                        logger.error("confirmOneScore error!\n"+e.getMessage());
                        //删除确认文件
                        attachmentDAO.deleteAttachment(confirmAttaId);
                        confirmFile.delete();
                        return null;
                    }
                }
            }
            courses.clear();
            courses=null;
            courIds.clear();
            courIds=null;
            stuIds.clear();
            stuIds=null;
            successConfirm.clear();
            successConfirm=null;
        }
        
        //保存确认信息
        boolean correct=true;
        int resultAttachId=0;
        //保存结果文件
        if(resultFile!=null&&resultFile.exists()){
            //确认存在不一致的地方
            result="确认时发现有不一致或者错误的信息！请查看确认结果文件！";
            correct=false;
            Attachment resultAttach=new Attachment();
            resultAttach.setAttaDescribe("成绩导出的确认结果文件。");
            resultAttach.setAttaFilename(resultFile.getName());
            resultAttach.setAttaLocation(location);
            resultAttach.setAttaOriFilename("学生成绩确认结果文件-"+(int)(Math.random()*1000)+".xls");
            resultAttach.setAttaSourceid(exportId);
            resultAttach.setAttaSourceType(7);
            try {
                attachmentDAO.createAttachment(resultAttach);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("createAttachment error!\n"+e.getMessage());
                //删除确认文件
                attachmentDAO.deleteAttachment(confirmAttaId);
                confirmFile.delete();
                return null;
            }
            resultAttachId=resultAttach.getAttaId();
            if(resultAttachId<=0){
                result="确认失败，保存确认的结果文件到数据库失败！";
                //删除确认文件
                attachmentDAO.deleteAttachment(confirmAttaId);
                confirmFile.delete();
                return result;
            }
        }
        //更新导出记录的确认信息
        ScoreExportConfirm newSec=new ScoreExportConfirm();
        newSec.setSecoId(exportId);
        newSec.setSecoConfirmAttachId(confirmAttaId);
        newSec.setSecoConfirmRemark(confirmRemark);
        newSec.setSecoConfirmTime(new Date());
        newSec.setSecoConfirmUserId(confirmUserId);
        if(!correct){
            newSec.setSecoResultAttachId(resultAttachId);
        }
        try {
            scoreExportConfirmDAO.confirmOneExport(newSec);
        } catch (Exception e) {
            logger.error("confirmOneExport error!\n"+e.getMessage());
            //删除确认文件
            attachmentDAO.deleteAttachment(confirmAttaId);
            confirmFile.delete();
            //删除结果文件
            attachmentDAO.deleteAttachment(resultAttachId);
            resultFile.delete();
            return null;
        }
        //更新确认附件的原件id
        Map query=new HashMap();
        query.put("sourceId",exportId);
        query.put("attachId",resultAttachId);
        try {
            attachmentDAO.changeAttachSourceId(query);
        } catch (Exception e) {
            logger.error("changeAttachSourceId error!\n"+e.getMessage());
            //删除确认文件
            attachmentDAO.deleteAttachment(confirmAttaId);
            confirmFile.delete();
            //删除结果文件
            attachmentDAO.deleteAttachment(resultAttachId);
            resultFile.delete();
            return null;
        }
        
        return result;
    }
    
    /**
     * 批量删除导出记录
     * @param exportIds
     * @return
     */
    public String deleteExportInfo(String exportIds){
        String result="删除成功！";
        if(exportIds==null||exportIds.length()>0){
            return "未指定删除项！";
        }
        String[] idStrs=exportIds.split(",");
        List<Integer> ids=new ArrayList<Integer>();
        for(String idStr:idStrs){
            ids.add(Integer.parseInt(idStr));
        }
        try {
            int affectNum=scoreExportConfirmDAO.deleteExport(ids);
            if(affectNum!=ids.size()){
                result="部分或全部项删除失败，请刷新后重试！";
            }
        } catch (Exception e) {
            logger.error("deleteExport error!\n"+e.getMessage());
            return null;
        }        
        return result;
    }
    
}
