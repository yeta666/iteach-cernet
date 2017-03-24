package com.swust.kelab.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.swust.kelab.domain.Clas;
import com.swust.kelab.domain.Grade;
import com.swust.kelab.model.ClasModel;
import com.swust.kelab.model.CommonQuery;
import com.swust.kelab.model.PageData;
import com.swust.kelab.model.QueryData;
import com.swust.kelab.repos.GradeDao;
import com.swust.kelab.repos.GradeInfoDAO;
import com.swust.kelab.repos.ReSelectCourseDAO;
import com.swust.kelab.repos.bean.ListQuery;

@Service
public class GradeInfoService {
    final Logger logger = LoggerFactory.getLogger(getClass());
    
    private GradeInfoDAO gradeInfoDAO;
    private GradeDao gradeDao;
    
    @Autowired
    private ReSelectCourseDAO reSelectCourseDAO;
    
    @Autowired
    public void setGradeInfoDAO(GradeInfoDAO gradeInfoDAO) {
        this.gradeInfoDAO = gradeInfoDAO;
    }

    @Autowired
    public void setGradeDao(GradeDao gradeDao) {
        this.gradeDao = gradeDao;
    }

    /**
     * 年级增加
     * 
     * @author lancer
     * @param grade
     * @throws Exception
     */
    public int insertGrade(Grade grade) throws Exception {

        if (gradeInfoDAO.isGradeExsit(grade) > 0) {
            return 0;
        } else {
            return gradeInfoDAO.insertGrade(grade);
        }
    }

    /**
     * 年级修改
     * 
     * @author lancer
     * @param grade
     * @throws Exception
     */
    public void updateGrade(Grade grade) throws Exception {
        Grade oriGrade=null;
        gradeInfoDAO.updateGrade(grade);
        try {
            oriGrade=gradeDao.findGradeById(grade.getGradId());
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("findGradeById failed!\n"+e.getLocalizedMessage());
        }
        String newGradeName=grade.getGradName();
        if(oriGrade!=null&&newGradeName!=null
                &&!newGradeName.equals(grade.getGradName())){
            Map query=new HashMap();
            query.put("gradeId", grade.getGradId());
            query.put("gradeName", newGradeName);
            try {
                reSelectCourseDAO.updateUserDepaInfo(query);
            } catch (Exception e) {
                // TODO: handle exception
                logger.error("updateUserDepaInfo failed!\n"+e.getLocalizedMessage());
            }
        } 
        
    }

    /**
     * 添加班级
     * 
     * @author lancer
     * @param clas
     * @throws Exception
     */
    public int insertClass(Clas clas) throws Exception {

        if (gradeInfoDAO.isClassExsit(clas) > 0) {
            return 0;
        } else {
            return gradeInfoDAO.insertClas(clas);
        }
    }

    /**
     * 删除班级
     * 
     * @author lancer
     * @param clasId
     * @throws Exception
     */
    public void deleteClass(String[] clasId) throws Exception {
        gradeInfoDAO.deleteClas(clasId);
        //删除多余年级
        gradeInfoDAO.deleteUselessGrade();
    }

    /**
     * 班级修改
     * 
     * @author lancer
     * @param clas
     * @throws Exception
     */
    public int updateClass(Clas clas) throws Exception {

        if (gradeInfoDAO.findClasIdByClaNamAndGradId(clas) > 0) {
            return 0;
        } else {
            gradeInfoDAO.updateClas(clas);
        }
        ClasModel oriClass=null;
        try {
            Map q=new HashMap();
            q.put("classId", clas.getClasId());
            List<ClasModel> result=gradeDao.queryClassGrade(q);
            if(result!=null&&result.size()>0){
                oriClass=result.get(0);
            }
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("queryClassGrade failed!\n"+e.getLocalizedMessage());
        }
        String newClasName=clas.getClasName();
        if(oriClass!=null&&newClasName!=null
                &&newClasName.equals(oriClass.getClasName())){
            Map query=new HashMap();
            query.put("classId", clas.getClasId());
            query.put("className", newClasName);
            try {
                reSelectCourseDAO.updateUserDepaInfo(query);
            } catch (Exception e) {
                // TODO: handle exception
                logger.error("updateUserDepaInfo failed!\n"+e.getLocalizedMessage());
            }
        }
        return 1;
    }

    /**
     * 查询班级
     * 
     * @author lancer
     */

    public QueryData selectAllClass(Integer depaId, Integer gradId, CommonQuery commonquery) {
        QueryData queryData = new QueryData();
        // 构造查询条件
        ListQuery query = commonquery.format();
        if (depaId > 0 && depaId != null) {
            query.fill("depaId", depaId);
        }
        if (gradId > 0 && gradId != null) {
            query.fill("gradId", gradId);
        }
        int totalCount = gradeInfoDAO.countAllClas(query);
        queryData.setTotalCount(totalCount);
        if (totalCount == 0) {
            return queryData;
        }
        if (commonquery.getRecordPerPage() <= 0) {
            commonquery.setRecordPerPage(10);
        }
        query.fill("maxCount", commonquery.getRecordPerPage());

        int totalPage = QueryData.computeTotalPage(totalCount, commonquery.getRecordPerPage());
        queryData.setTotalPage(totalPage);
        List<PageData> pageDataList = Lists.newArrayList();
        // 未指定页数，则只读取前三页数据
        if (commonquery.getPageArray() == null) {
            commonquery.setPageArray(new int[] { 1, 2, 3 });
        }
        // 分别获取每页的数据
        for (int i = 0; i < commonquery.getPageArray().length; i++) {
            int page = commonquery.getPageArray()[i];
            if (page <= 0 || page > totalPage) {
                continue;
            }
            System.out.println(query.get("maxCount"));
            query.fill("startIndex", QueryData.computeStartIndex(page, commonquery.getRecordPerPage()));
            List<ClasModel> clasModel = gradeInfoDAO.selectAllClas(query);
            pageDataList.add(new PageData(page, clasModel));
        }
        // 装载返回结果
        queryData.setPageData(pageDataList);
        return queryData;

    }

    /**
     * 查询单个班级
     * 
     * @author lancer
     * @param classId
     * @return
     */

    public ClasModel selectClass(int classId) {
        return gradeInfoDAO.selectClass(classId);
    }

}
