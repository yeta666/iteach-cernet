package com.swust.kelab.repos;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.swust.kelab.domain.Clas;
import com.swust.kelab.domain.Grade;
import com.swust.kelab.model.ClasModel;
import com.swust.kelab.model.ClassModel;
import com.swust.kelab.model.GradeClassModel;

/**
 * 年级以及班级
 * 
 * @author lujoCom
 * 
 */
@Repository
public class GradeDao {

	private SqlSession sqlSession;

	@Autowired
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	/**
	 * 根据学校ID查询年级
	 * 
	 * @param depa_id
	 * @return
	 */
	public List<Grade> queryGradeByDepaId(int depa_id) {
		return sqlSession.selectList("grade.queryGrades", depa_id);
	}

	/**
	 * 根据年级的名称以级学校id查询年级id,将年级名称和学校id封装到一个grade对象中
	 * 
	 * @param grade
	 * @return 年级id
	 */
	public Integer findGradIdByGraNamAndSchId(Grade grade) {
		return sqlSession.selectOne("grade.findGradeByGraNamAndSchId", grade);
	}

	/**
	 * 添加年级,将年级的名称以及学校id封装成一个grade对象，然后添加到数据库中，成功后返回年级的id
	 * 
	 * @param grade
	 * @return 添加成功后返回此年级的id
	 */
	public Integer insertGrade(Grade grade) {
		sqlSession.insert("grade.insertGrade", grade);
		return grade.getGradId();
	}

	/**
	 * 根据年级ID查询班级
	 * 
	 * @param gra_id
	 * @return
	 */
	public List<Clas> queryClaByGraId(int gra_id) {
		return sqlSession.selectList("grade.queryClasses", gra_id);
	}

	/**
	 * 根据班级名称以及年级id查询班级id
	 * 
	 * @param clas
	 * @return 返回班级id
	 */
	public Integer findClasIdByClaNamAndGradId(Clas clas) {
		return sqlSession.selectOne("grade.findClassIdByClaNaAndGraId", clas);
	}

	/**
	 * 将班级名称以及年级id封装成一个clas对象，然后添加到数据库中,添加成功后返回改班级的id
	 * 
	 * @param clas
	 * @return 返回添加成功后的班级id
	 */
	public Integer insertClass(Clas clas) {
		sqlSession.insert("grade.insertClass", clas);
		return clas.getClasId();
	}

	/**
	 * 通过学校id,级联查询对应的年级和班级,用做筛选
	 * 
	 * @param depaId
	 *            学校id,若为非正数,则查询所有学校
	 * @return 年级及其对应的班级列表
	 * @see GradeClassModel
	 */
	public List<GradeClassModel> viewGradeClassByDepaId(int depaId) {
		return sqlSession.selectList("grade.viewGradeClass", depaId);
	}

	/**
	 * 通过用户的部门类型查询学校的所有年级所有班级信息
	 * 
	 * @param data
	 *            data中存放用户所在部门id(departId)以及部门类型(departType)
	 * @return
	 * @author lujocom
	 */
	public List<ClasModel> getSchoolAndGraClasInfo(Map<String, Object> data) {
		return sqlSession.selectList("grade.findSchoolAndGraCla", data);
	}
	
	/**
	 * 根据部门id、年级id或者班级id，获取班级（年级）信息
	 * 
	 * @param query 查询信息
	 * @return 结果列表
	 */
	public List<ClasModel> queryClassGrade(Map query) {
	    return sqlSession.selectList("grade.queryClassGrade", query);
	}
	
	/**
	 * 通过年级id获取对应的班级,未指定id则获取全部班级
	 * 
	 * @param gradId 年级id
	 * @return 结果列表
	 */
	public List<ClassModel> queryClassByGradeId(Integer gradId) {
	    return sqlSession.selectList("grade.queryClassByGradeId", gradId);
	}	
	
	/**
	 * 通过id查找年级信息
	 * @param gradeId
	 * @return
	 */
	public Grade findGradeById(int gradeId){
        return sqlSession.selectOne("grade.findGradeById",gradeId);
    }
}
