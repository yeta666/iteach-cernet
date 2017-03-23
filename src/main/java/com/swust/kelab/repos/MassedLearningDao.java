package com.swust.kelab.repos;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.swust.kelab.domain.MassedLearning;
import com.swust.kelab.domain.MassedLearningManage;
import com.swust.kelab.domain.StudentMassedLearning;
import com.swust.kelab.model.MassedLearningModel;
import com.swust.kelab.model.StudentMassedLearningInfoModel;

@Repository("massedLearningDao")
public class MassedLearningDao {

	private SqlSession sqlSession;

	@Autowired
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	public Integer createNewMassedLearning(MassedLearning ml) {
		sqlSession.insert("massedLearning.createMassedLearning", ml);
		return ml.getMaleId();
	}

	/**
	 * 根据massedLearning查询详细的集中学习信息
	 * 
	 * @param ml
	 * @return
	 */
	public List<MassedLearning> findMassedLearningInfoByMaLe(MassedLearning ml) {
		return sqlSession.selectList(
				"massedLearning.queryMassedLearningInfoByMaLe", ml);
	}

	/**
	 * 根据集中学习信息的条件查询集中学习信息
	 * 
	 * @param mlm
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<MassedLearningModel> findMassedLearningInfoByQuery(Map query) {
		return sqlSession.selectList(
				"massedLearning.queryMassedLearningInfoByQuery", query);
	}

	/**
	 * 根据集中学习信息的条件计算条数
	 * 
	 * @param query
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Integer countMassedLearning(Map query) {
		return ((Integer) sqlSession.selectOne(
				"massedLearning.countMassedLearning", query)).intValue();
	}

	/**
	 * 给集中学习课程添加学生
	 * 
	 * @param list
	 * @return
	 */
	public Integer addStudentToMassedLearning(List<StudentMassedLearning> list) {
		return sqlSession.insert("massedLearning.addStudentToMassedLearning",
				list);
	}

	/**
	 * 根据课程id查询该课程的集中学习的限制信息
	 * 
	 * @param courseId
	 * @return
	 */
	public MassedLearningManage findMassedLearningMange(int courseId) {
		return sqlSession.selectOne(
				"massedLearning.queryMassedLearningManagerByCourId", courseId);
	}

	/**
	 * 根据学生集中学习某些信息查询学生集中学习详细信息
	 * 
	 * @param sml
	 * @return
	 */
	public List<StudentMassedLearningInfoModel> findStudentMassedLearning(
			StudentMassedLearning sml) {
		return sqlSession.selectList(
				"massedLearning.findStudentFromStuMasLearning", sml);
	}

	/**
	 * 查询所有学生的集中选课信息中时间冲突的
	 * 
	 * @param data
	 * @return
	 */
	public List<StudentMassedLearningInfoModel> findConflictMassedLearning(
			Map<String, Object> data) {
		return sqlSession.selectList(
				"massedLearning.findConflictMassedLearning", data);
	}

	/**
	 * 删除某些集中学习中的学生
	 * 
	 * @param queryData
	 *            包含学生的id的数组，一个是集中学习id
	 * @return
	 */
	public Integer deleteStudentFromMassedLearning(Map<String, Object> queryData) {
		return sqlSession.delete(
				"massedLearning.deleteStudentFromStuMasLearning", queryData);
	}

	/**
	 * 批量删除集中学习
	 * 
	 * @author ZhangXin
	 * @param mlIds
	 * @return
	 */
	public int deleteMl(List<Integer> mlIds) {
		// TODO Auto-generated method stub
		return sqlSession.delete("massedLearning.deleteMasLearning", mlIds);
	}

	/**
	 * 删除
	 * @param mlId
	 * @return
	 */
	public int deleteMlStudent(String mlIds)
	{
		return sqlSession.delete("studentMassedLearning.deleteStudentByMlID",mlIds);
	}
	/**
	 * 根据集中学习id查询该集中学习有多少已选的学生
	 * 
	 * @param data
	 * @return
	 */
	public Integer countStudeNumFromStudentMale(Map<String, Object> data) {
		return sqlSession.selectOne(
				"massedLearning.countStudentInMassedLearning", data);
	}

	/**
	 * 开始集中学习后更改此集中学习的状态
	 * 
	 * @param data
	 * @return
	 */
	public Integer updateMassedLearningState(Map<String, Object> data) {
		return sqlSession.update("massedLearning.updateMassedLearningState", data);
	}

}
