package com.swust.kelab.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.swust.kelab.domain.Clas;
import com.swust.kelab.domain.Department;
import com.swust.kelab.domain.Grade;
import com.swust.kelab.model.GradeClassModel;
import com.swust.kelab.repos.DepartmentDao;
import com.swust.kelab.repos.GradeDao;

@Service(value = "gradeService")
public class GradeService {
    final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private DepartmentDao departmentDAO;
    
	private GradeDao gradeDao;

	@Autowired
	public void setGradeDao(GradeDao gradeDao) {
		this.gradeDao = gradeDao;
	}

	/**
	 * 根据学校ID查询学校的所有年级
	 * 
	 * @param depa_id
	 * @return
	 */
	@Transactional
	public List<Grade> findGradesByDepaId(int depa_id) {
		return gradeDao.queryGradeByDepaId(depa_id);
	}

	/**
	 * 根据学校id以及年级名称查询年级id
	 * 
	 * @param grade
	 * @return 查询成功存在此年级,返回年级id否则返回0
	 */
	public Integer findGradIdByDepartIdAndGrdeNam(Grade grade) {
		return gradeDao.findGradIdByGraNamAndSchId(grade);
	}

	/**
	 * 添加年级,添加成功后返回id
	 * 
	 * @param grade
	 * @return 返回添加年级成功后此年级的id,添加失败返回0
	 */
	public Integer insertGrade(Grade grade) {
		return gradeDao.insertGrade(grade);
	}

	/**
	 * 根据年级ID查询年级的所有班级
	 * 
	 * @param grade_id
	 * @return
	 */
	@Transactional
	public List<Clas> findClassesByGradId(int grade_id) {
		return gradeDao.queryClaByGraId(grade_id);
	}

	/**
	 * 根据年级id以及班级名称查询班级id,查询成功返回此班级的id,不存在此年级返回0
	 * 
	 * @param clas
	 * @return
	 */
	public Integer findClasIdByGradIdAndClaNam(Clas clas) {
		return gradeDao.findClasIdByClaNamAndGradId(clas);
	}

	/**
	 * 插入班级，插入成功后返回班级id，失败返回0
	 * 
	 * @param clas
	 * @return
	 */
	public Integer insertClass(Clas clas) {
		return gradeDao.insertClass(clas);
	}
	
	/**
	 * 根据部门id获取所有的年级和班级
	 * 若部门类型不是学校,则查询所有学校的年级和班级
	 * 
	 * @param departId 部门id
	 * @return  年级和班级
	 */
	public List<GradeClassModel> viewGradeClassByDepart(int departId){
	    List<GradeClassModel> result=null;
	    //查询部门类型
        Department depart=null;
        try {
            depart=departmentDAO.queryDepartmentById(departId);
        } catch(Exception e) {
            logger.error("queryDepartType error!\n"+e.getLocalizedMessage());
            return result;
        }
        int departType=-1;
        if(depart!=null){                      
            departType=depart.getDepaType();
        }
        //根据部门类型,查询年级和班级
        int depaId=0;
        if(departType==3){
            depaId=departId;
        }
        try {
            result=gradeDao.viewGradeClassByDepaId(depaId);
        } catch(Exception e) {
            logger.error("viewGradeClassByDepaId error!\n"+e.getMessage());
        }
	    return result;
	}

}
