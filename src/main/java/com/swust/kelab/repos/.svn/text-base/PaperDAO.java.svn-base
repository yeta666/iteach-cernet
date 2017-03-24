package com.swust.kelab.repos;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.swust.kelab.domain.GroupPaperParameter;
import com.swust.kelab.domain.ReGroupPaper;
import com.swust.kelab.model.ExamPaperModel;
import com.swust.kelab.repos.bean.ListQuery;

/**
 * 试卷管理相关查询
 * 
 * @author yangzq
 * 
 */
@Repository()
public class PaperDAO {
    private SqlSession sqlSession;

    @Autowired
    public void setSqlSession(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    /**
     * 查询试卷数量
     * 
     * @author yangzq
     * @param query
     * @return
     */
    public int selectPapersCount(ListQuery query) {
        // TODO Auto-generated method stub
        return (Integer) sqlSession.selectOne("paper.selectPapersCount", query);
    }

    /**
     * 查询所有试卷详细
     * 
     * @author yangzq
     * @param query
     * @return
     */
    public List<ExamPaperModel> selectPapersDetail(ListQuery query) {
        return sqlSession.selectList("paper.selectPapersDetail", query);
    }

    /**
     * 组卷产生新的试卷
     * 
     * @author yangzq
     * @param paperModel
     * @return
     */
    public int insertNewPaper(ExamPaperModel paperModel) {
        sqlSession.insert("paper.insertNewPaper", paperModel);
        return paperModel.getExpaId();
    }

    /**
     * 组卷生成组卷参数表
     * 
     * @author yangzq
     * @param groupPaper
     */
    public void insertNewGroupPaper(ReGroupPaper groupPaper) {
        sqlSession.insert("paper.insertNewGroupPaper", groupPaper);
    }

    /**
     * 根据试卷名称查询试卷数 用于检验试卷名称是否存在重复
     * 
     * @author yangzq
     * @param name
     * @return
     */
    public int checkExamInfoTitle(String name) {
        return (Integer) sqlSession.selectOne("paper.selectPaperCountByName", name);
    }

    /**
     * 自动组卷返回主键
     * 
     * @author yangzq
     * @param parameter
     * @return
     */
    public int saveGpPaParameter(GroupPaperParameter parameter) {
        sqlSession.insert("paper.saveGpPaParameter", parameter);
        return parameter.getGppaId();
    }

    /**
     * 随机抽取试题，返回试题编号
     * 
     * @author yangzq
     * @param examCourId
     * @param gppaQuestiontype
     * @param userId
     * @param gppaDifficulty
     * @return
     */
    public List<Integer> selectGroupQues(Map<String, Object> query) {
        return sqlSession.selectList("paper.selectGroupQues", query);
    }

    /**
     * 根据条件计数试题数量
     * 
     * @author yangzq
     * @param query
     * @return
     */
    public int countQuesNums(Map<String, Object> query) {
        // TODO Auto-generated method stub
        return (Integer) sqlSession.selectOne("paper.countQuesNums", query);
    }

    /**
     * 根据paperIds获取对应自动组卷参数表的gppanames
     * 
     * @author yangzq
     * @param paperIds
     * @return
     */
    public List<Integer> queryGppaIds(String[] paperIds) {
        // TODO Auto-generated method stub
        return sqlSession.selectList("paper.queryGppaIds", paperIds);
    }

    /**
     * 删除组卷信息表Re_GroupPaper
     * 
     * @author yangzq
     * @param rgpaexpaIds
     */
    public void delReGroupPaper(String[] rgpaExpaIds) {
        sqlSession.delete("paper.delReGroupPaper", rgpaExpaIds);
    }

    /**
     * 删除试卷
     * 
     * @author yangzq
     * @param paperIds
     */
    public void delPapers(String[] paperIds) {
        // TODO Auto-generated method stub
        sqlSession.delete("paper.delPapers", paperIds);
    }

    /**
     * 删除自动组卷参数表
     * 
     * @author yangzq
     * @param gppaNames
     */
    public void delGroupPaperParameter(List<Integer> gppaIds) {
        sqlSession.delete("paper.delGroupPaperParameter", gppaIds);
    }

    /**
     * 查询试卷对应的考试id
     * 
     * @author htx
     * @param paperIds
     */
    public List<Integer> findExamIdsByPaper(String[] paperIds) {
        return sqlSession.selectList("paper.queryExamIdsByPaper", paperIds);
    }

    /**
     * 查询考试对应的测试id
     * 
     * @author htx
     * @param paperIds
     */
    public List<Integer> findRatrIdsByExam(List<Integer> delExinIds) {
        return sqlSession.selectList("paper.queryreTestIdsByExam", delExinIds);
    }

}
