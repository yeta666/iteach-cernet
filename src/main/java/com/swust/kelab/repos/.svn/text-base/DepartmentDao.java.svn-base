package com.swust.kelab.repos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.swust.kelab.domain.Department;
import com.swust.kelab.model.DepartmentModel;
import com.swust.kelab.model.PlatformStatisticModel;
import com.swust.kelab.repos.bean.ListQuery;

@Repository
public class DepartmentDao {

	final Logger logger = LoggerFactory.getLogger(getClass());

	private SqlSession sqlSession;

	@Autowired
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	/**
	 * 通过部门id和部门类型查询部门列表<br>
	 * @param depaType
	 * @param depaIds
	 * @return
	 * @throws Exception
	 */
	public List<Department> viewAllDepaListBelongsToParentDepar(
				Integer depaId,Integer depaType,String deparIds) throws Exception {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("depaId", depaId);
		params.put("depaType", depaType);
		params.put("depaIds", deparIds);
		return sqlSession.selectList("department.viewAllDepaListBelongsToParentDepar",params);
	}

	/**
	 * 根据部门类型查询部门信息
	 * 
	 * @param depa_type
	 * @return
	 */
	public List<Department> queryDepatmentByType(int depa_type) {
		return sqlSession.selectList("department.queryDepartment", depa_type);
	}
	
	/**
	 * 查询上级部门类型的部门信息
	 * 
	 * @param depa_type
	 * @return
	 */
	public List<Department> queryParentDepatmentByType(int depa_type) {
		return sqlSession.selectList("department.queryParentDepartment", depa_type);
	}
	/**
	 * 根据学校统计教师人数（包含学习名称）
	 * 
	 * @return 统计信息
	 * @see PlatformStatisticModel
	 */
	public List<PlatformStatisticModel> staTeacherNumBySchool() {
		try {
			return sqlSession.selectList("department.staSchoolTeaNum");
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e.getLocalizedMessage());
		}
		return null;
	}

	/**
	 * 根据学校统计学生人数
	 * 
	 * @return 统计信息
	 * @see PlatformStatisticModel
	 */
	public List<PlatformStatisticModel> staStudentNumBySchool() {
		try {
			return sqlSession.selectList("department.staSchoolStuNum");
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e.getLocalizedMessage());
		}
		return null;
	}

	/**
	 * 根据部门id查询部门基本信息：id、名称、类型、上级部门id
	 * 
	 * @param departId
	 *            部门id，非正整数则查询所有部门的第一个
	 * @return 部门信息
	 * @see Department
	 */
	public Department queryDepartmentById(int departId) {
		return sqlSession.selectOne("department.queryDepartById", departId);
	}

	/**
	 * 根据部门查询部门
	 * 
	 * @param depart
	 * @return 查询成功返回一个List<Department> <br>
	 *         若数据库中无数据则返回一个size为0的list
	 * @see Department
	 * @author lujoCom
	 */
	public List<Department> findDepartmentsByDepar(Department depart) {
		return sqlSession.selectList("department.findDepartByDepart", depart);
	}

	/**
	 * 添加一个新的部门，添加成功后返回插入后部门的id
	 * 
	 * @param depart
	 * @return 成功插入后返回depart_id
	 * @see Department
	 * @author lujoCom
	 */
	public Integer insertDepartment(Department depart) {
		sqlSession.insert("department.insertDepartment", depart);
		return depart.getDepaId();
	}

   /**
    * 根据上级组织id，和要查询的关键词查询
    * @param parentId 上级组织id
    * @param keyword 查询的关键词
    */
	public List<Department> queryDepartmentByParent(Map parameter) {
		return sqlSession.selectList("department.queryDepartByPidAndKeyword", parameter);
	}
	/**
	    * 根据上级组织id查询其下属所有机构
	    * @param parentId 上级组织id
	    */
	public List<Department> queryDepartmentByParent(int parentId) {
		return sqlSession.selectList("department.queryDepartByPid", parentId);
	}
	/**
	 * 更新一个机构信息
	 * @param parameter
	 * @return 返回更新操作状态
	 */
	public int updateOneDepartment(Map<String, Object> parameter) {
		return ((Integer)sqlSession.update("department.updateOneDepartment", parameter)).intValue();
	}
	/**
	 * 批量删除机构
	 * @param departmentIds 待删除的id字符串 eg: (1,2,3)
	 * @return
	 */
	public int deleteBatchDepartment(List departmentIds) {
		return ((Integer)sqlSession.delete("department.deleteBathDeparment", departmentIds)).intValue();
	}
	/**
	 * 机构筛选查询
	 * @param query 查询对象
	 * @return 查询结果列表
	 */
	public List<DepartmentModel> queryByMutiTerm(ListQuery query) {
		return sqlSession.selectList("department.selectByMutiTerm", query);
	}
	/**
	 * 机构筛选查询结果总记录数
	 * @param query 查询对象
	 * @return 结果总记录数
	 */
	public int countByMutiTerm(ListQuery query){
		return ((Integer)sqlSession.selectOne("department.countByMutiTerm", query)).intValue();
	}
	
	/**
	 * 批量插入部门
	 * 
	 * @param departs 部门列表
	 * @return 实际插入的记录条数
	 */
	public int batchInsertDeparts(List<Department> departs){
	    return ((Integer)sqlSession.insert("department.batchInsertDeparts", departs)).intValue();
	}
	
	/**
     * 机构名称查询
     * @param depaId 机构id，未指定则返回所有机构的信息
     * @return 查询结果列表
     */
    public List<DepartmentModel> queryDepartName(Integer depaId) {
        return sqlSession.selectList("department.queryDepartName", depaId);
    }
	
}
