package com.swust.kelab.repos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.swust.kelab.domain.EvaluateMethod;
import com.swust.kelab.model.CourseEvaluateModel;

/**
 * 课程的评分标准数据库DAO层<br>
 * 
 * @author EasonLian
 */
@Repository()
public class EvaluateMethodDAO {

	@Autowired
	private SqlSession sqlSession;
	
	/**
	 * 修改评估方法
	 * @param EvaluateMethod
	 * @author EasonLian
	 */
	public int modeEvaluateMethod(EvaluateMethod evme) {
		return sqlSession.update("evaluateMethod.modeEvaluateMethod", evme);
	}
	
	/**
	 * 添加评估方法
	 * @param EvaluateMethod
	 * @author EasonLian
	 */
	public int addEvaluateMethod(EvaluateMethod evme) throws Exception {
		return sqlSession.insert("evaluateMethod.addEvaluateMethod", evme);
	}

	/**
	 * 批量删除查询考核方式
	 * @param ids
	 * @return 删除的行数
	 * @throws Exception
	 */
	public int delEvaluateMethod(String ids) throws Exception {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("ids", ids);
		return sqlSession.delete("evaluateMethod.delEvaluateMethod",params);
	}
	
	/**
	 * 查询考核方式数量
	 * @param 查询参数
	 */
	public int getEvaluateMethodCount(Map<String,Object> params) throws Exception {
		Integer in = sqlSession.selectOne("evaluateMethod.getEvaluateMethodCount",params);
		return in.intValue();
	}
	
	/**
     * 查询所有考核方式列表
     * @param 参数map
     */
	public List<EvaluateMethod> viewEvaluateMethodList(
			Map<String, Object> params) throws Exception {
		return sqlSession.selectList("evaluateMethod.viewEvaluateMethodList",params);
	}
	
	/**
	 * 通过章节Id，查找该门课程的评分标准
	 * 
	 * @param chapId
	 * @return
	 */
	public EvaluateMethod selectOneByCourseId(Integer courId) {
		return sqlSession.selectOne("evaluateMethod.selectOneByCourId",courId);
	}
	
	/**
	 * 查询课程对应的评价方法
	 * 
	 * @param courseIds 课程id列表
	 * @return          评价方法列表
	 * @see CourseEvaluateModel
	 */
	public List<CourseEvaluateModel> getEvaluateMethodByCourse(List<Integer> courseIds){
	    return sqlSession.selectList("evaluateMethod.getEvaluateMethodByCourse",courseIds);
	}
	
	/**
	 * 查询所有的考核方式
	 * @return 考核方式列表
	 * @see EvaluateMethod
	 */
	public List<EvaluateMethod> viewAllEvaluateMethods(){
	    return sqlSession.selectList("evaluateMethod.viewAll");
	}
}
