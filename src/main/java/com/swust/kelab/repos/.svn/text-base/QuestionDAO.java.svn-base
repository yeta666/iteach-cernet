package com.swust.kelab.repos;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.swust.kelab.domain.ExamQuestion;
import com.swust.kelab.domain.Option;
import com.swust.kelab.model.ExamQuestionModel;
import com.swust.kelab.repos.bean.ListQuery;

/**
 * 试题管理相关查询
 * @author yangzq
 * 
 */
@Repository()
public class QuestionDAO {

	private SqlSession sqlSession;

	@Autowired
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	/**
	 * 根据页面查询条件统计试题记录
	 * @author yangzq
	 * @param query
	 * @return
	 */
    public int selectQuesCount(ListQuery query){
    	return (Integer) sqlSession.selectOne("question.selectQuesCount", query);
    }
    
    /**
     * 根据页面条件查询试题信息
     * @author yangzq
     * @param query
     * @return
     */
    public List<ExamQuestionModel> selectQuesDetail(ListQuery query){
    	return sqlSession.selectList("question.selectQuesDetail", query);
    }

    /**
     * 保存新增的试题
     * @author yangzq
     * @param question
     * @return
     */
	public int saveQuestions(ExamQuestion question) {
		sqlSession.insert("question.saveNewQuestion",question);
		return question.getExquId();
	}

	/**
	 * 存入新的选项数据
	 * @author yangzq
	 * @param option
	 */
	public int saveOption(Option option) {
		sqlSession.insert("question.saveOption",option);
		return option.getOptiId();
	}

	
	/**
	 * 根据选项编号删除选项
	 * @author yangzq
	 * @param delQues
	 */
	public void delOptions(String[] delQues) {
		sqlSession.delete("question.delOptions", delQues);
	}
	
	/**
	 * 删除试题
	 * @author yangzq
	 * @param delQuestions
	 */
	public void delQuestion(String[] delQuestions) {
		sqlSession.delete("question.delQuestion", delQuestions);
	}

	/**
	 * 编辑获取试题信息
	 * @author yangzq
	 * @param quesId
	 * @return
	 */
	public ExamQuestionModel getOneQuestion(Integer quesId) {
		// TODO Auto-generated method stub
		return sqlSession.selectOne("question.selectOneQuestion", quesId);
	}

	/**
	 * 获取选中的试题信息
	 * @author yangzq
	 * @param queId
	 * @return
	 */
	public List<ExamQuestionModel> selectedInfos(String[] queId) {
		return sqlSession.selectList("question.selectedInfos", queId);
	}

	/**
	 * 更新试题数据
	 * @author yangzq
	 * @param question
	 */
	public void updateQuestions(ExamQuestion question) {
		sqlSession.update("question.updateQuestion",question);
	}

}
