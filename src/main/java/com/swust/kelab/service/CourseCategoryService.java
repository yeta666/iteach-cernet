package com.swust.kelab.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.common.collect.Lists;
import com.swust.kelab.domain.CourseCategory;
import com.swust.kelab.model.BbsPostModel;
import com.swust.kelab.model.CommonQuery;
import com.swust.kelab.model.CourseCategoryModel;
import com.swust.kelab.model.PageData;
import com.swust.kelab.model.QueryData;
import com.swust.kelab.repos.CourseCateDAO;
import com.swust.kelab.repos.bean.ListQuery;
import com.swust.kelab.utils.FormatUtil;
import com.swust.kelab.web.json.JsonAndView;

/**
 * 处理课程类别的service
 * @author Wu
 *
 */
@Service
public class CourseCategoryService {
    final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private CourseCateDAO courseCateDAO;
    
    /**
     * 获取类别列表
     * 
     * @param query  查询对象
     * @return       类别列表
     * @see CourseCategoryModel
     */
    public QueryData viewCourseCateList(CommonQuery query){
        QueryData result=new QueryData();
        //获取总数
        int totalNum=0;
        try {
            totalNum=courseCateDAO.countTotalCate();
        } catch(Exception e) {
            logger.error("countTotalCate error!\n"+e.getLocalizedMessage());
            result=null;
            return result;
        }
        //分页处理
        result.setTotalCount(totalNum);
        // 默认分页信息
        if(query.getRecordPerPage()<=0){
            query.setRecordPerPage(10);
        }
        if (query.getPageArray() == null) {
            query.setPageArray(new int[] { 1, 2, 3 });
        }
        result.setTotalPage(QueryData.computeTotalPage(totalNum
                , query.getRecordPerPage()));
        //查询列表
        List<CourseCategoryModel> cates=null;
        if(totalNum>0){
            ListQuery myQuery=query.format();
            int startIndex=(query.getPageArray()[0]-1)*query.getRecordPerPage();
            int fetchSize=query.getPageArray().length*query.getRecordPerPage();
            myQuery.fill("startIndex", startIndex);
            myQuery.fill("maxCount", fetchSize);
            try {
                cates=courseCateDAO.viewCourseCateList(myQuery);
            } catch(Exception e) {
                logger.error("viewCourseCateList error!\n"+e.getLocalizedMessage());
                result=null;
                return result;
            }
        }
        
        //封装结果
        if(cates!=null&&cates.size()>0){
            //查询类别的课程数
            List<Integer> cateIds=new ArrayList<Integer>();
            for (CourseCategoryModel ccm : cates) {
                cateIds.add(ccm.getCocaId());
            }
            List<CourseCategoryModel> temp=null;
            try {
                temp=courseCateDAO.countCateCourseNum(cateIds);
            } catch(Exception e) {
                logger.error("countCateCourseNum error!\n"+e.getLocalizedMessage());
                result=null;
                return result;
            }  
            if(temp!=null&&!temp.isEmpty()){
                for (CourseCategoryModel ccm : cates) {
                    boolean flag=false;
                    for (CourseCategoryModel ccm1 : temp) {
                        if(ccm1.getCocaId()==ccm.getCocaId()){
                            ccm.setCourseNum(ccm1.getCourseNum());
                            flag=true;
                            break;
                        }
                    }
                    if(!flag){
                        ccm.setCourseNum(0);
                    }
                }
            }
            //时间格式化
            for (CourseCategoryModel ccm : cates) {
                ccm.setFormatCreateTime(FormatUtil.formatDate(ccm.getCocaCreateTime()));
            }
            int startIndex,endIndex;
            List<PageData> pageDataList = Lists.newArrayList();  
            for (int k = 0; k < query.getPageArray().length; k++) {
                int page = query.getPageArray()[k];
                if (page <= 0 || page > result.getTotalPage()) {
                    continue;
                }
                startIndex=k*query.getRecordPerPage();
                endIndex=startIndex+query.getRecordPerPage();
                if(startIndex>=cates.size()){
                    continue;
                }
                if(endIndex>cates.size()){
                    endIndex=cates.size();
                }
                List<CourseCategoryModel> pageDatas=cates.subList(startIndex, endIndex);
                pageDataList.add(new PageData(page, pageDatas));
            }
            result.setPageData(pageDataList);
        }
        return result;
    }
    
    /**
     * 创建新的课程类别
     * 
     * @param courseCate 课程类别对象
     * @return           操作结果
     * @see CourseCategory
     */
    public String createCourseCate(CourseCategory courseCate){
        String result="fail";
        int insertNum=0;
        try {
            insertNum=courseCateDAO.createCourseCate(courseCate);
        } catch(Exception e) {
            logger.error("createCourseCate error!\n"+e.getLocalizedMessage());
        }
        if(insertNum==1){
            result="success";
        }
        return result;
    }
    
    /**
     * 批量删除课程类别
     * 
     * @param courseCateIds 课程类别Id,多个用逗号隔开
     * @return           操作结果
     */
    public String delCourseCates(String courseCateIds){
        String result="fail";
        if(courseCateIds!=null&&!courseCateIds.isEmpty()){
            String[] idStrs=courseCateIds.split(",");
            if(idStrs!=null&&idStrs.length>0){
                List<Integer> cateIds=new ArrayList<Integer>();
                try {
                    for (String s : idStrs) {
                        cateIds.add(Integer.parseInt(s));
                    }
                } catch(Exception e) {
                    logger.error("Integer parse error!\n"+e.getLocalizedMessage());
                }
                if(cateIds.size()>0){
                    int deleteNum=0;
                    try {
                        deleteNum=courseCateDAO.delCourseCates(cateIds);
                    } catch(Exception e) {
                        logger.error("delCourseCates error!\n"+e.getLocalizedMessage());
                    }
                    if(deleteNum==cateIds.size()){
                        result="success";
                    }
                }
            }
        }
        return result;
    }
    
    /**
     * 修改课程类别
     * 
     * @param courseCate 新的课程类别对象
     * @return  操作结果
     * @see CourseCategory
     */
    public String modifyCourseCate(CourseCategory courseCate){
        String result="success";
        if(courseCate==null||courseCate.getCocaId()<=0){
            return "fail";
        }
        try {
            courseCateDAO.modifyCourseCate(courseCate);
        } catch(Exception e) {
            logger.error("Integer parse error!\n"+e.getLocalizedMessage());
            result="fail";
        }
        return result;
    }
    
    /**
     * 获取所有课程类别,用于筛选
     * 
     * @param isOpen 1表示开放，0表示关闭
     * @return 类别列表
     */
    public List<CourseCategory> viewAllCourseCates(int isOpen){
        List<CourseCategory> result=null;
        try {
            result=courseCateDAO.viewAllCourseCates(isOpen);
        } catch(Exception e) {
            logger.error("viewAllCourseCates error!\n"+e.getLocalizedMessage());
        }
        return result;
    }
    
}
