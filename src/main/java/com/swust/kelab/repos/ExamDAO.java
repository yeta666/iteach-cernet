package com.swust.kelab.repos;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.swust.kelab.domain.ExamPaper;
import com.swust.kelab.domain.ReAutoTest;
import com.swust.kelab.model.ExamCourModel;
import com.swust.kelab.model.ExamInfo;
import com.swust.kelab.model.ExamQuestionModel;
import com.swust.kelab.model.ExamResultModel;
import com.swust.kelab.model.GradeModel;
import com.swust.kelab.model.ResultQuesModel;
import com.swust.kelab.model.ScoreModel;
import com.swust.kelab.model.TestCondition;
import com.swust.kelab.repos.bean.ListQuery;
import com.swust.kelab.repos.bean.Query;

/**
 * 
 * 课程测试相关查询<br>
 * 
 * @author htx yangzq
 * 
 */
@Repository()
public class ExamDAO {
    private SqlSession sqlSession;

    @Autowired
    public void setSqlSession(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public int selectCount(ListQuery query) {
        return (Integer) sqlSession.selectOne("examInfo.selectCount", query);
    }

    public List<ExamInfo> selectList(Query query) {
        List<ExamInfo> results = sqlSession.selectList("examInfo.select", query);
        return results;
    }

    /**
     * 根据考试编号查询一个考试信息
     * 
     * @author yangzq
     * @param examinId
     * @return
     */
    public ExamInfo selectExamInfoById(int examinId) {
        return sqlSession.selectOne("examInfo.selectOne", examinId);
    }

    /**
     * 通过考试信息编号获取试卷信息
     * 
     * @author yangzq
     * @param examinId
     * @return
     */
    public ExamPaper selectExamPaper(int paperId) {
        return sqlSession.selectOne("examInfo.selectExamPaper", paperId);
    }

    /**
     * 查询考试数据
     * 
     * @author yangzq
     * @param examinId
     * @return
     */
    public List<ExamQuestionModel> queryPaperData(int paperId) {
        return sqlSession.selectList("examInfo.queryPaperData", paperId);
    }

    /**
     * 考试完毕插入结果表
     * 
     * @author yangzq
     * @param resultModel
     * @return int 主键返回给solve表
     */
    public int examResult(ExamResultModel resultModel) {
        sqlSession.insert("examInfo.examResult", resultModel);
        return resultModel.getRateId();
    }

    /**
     * 考试完毕结果插入试题表
     * 
     * @author yangzq
     * @param ques
     */
    public void resultQues(ResultQuesModel ques) {
        sqlSession.insert("examInfo.resultQues", ques);
    }

    /**
     * 评卷,将分数保存到数据库
     * 
     * @author yangzq
     * @param update
     */
    public void markScore(Map<String, Object> update) {
        sqlSession.update("examInfo.markScore", update);
    }

    /**
     * 察看我的成绩记录条数
     * 
     * @author yangzq
     * @param query
     * @return
     */
    public int selectAllMyGradesCount(ListQuery query) {
        Integer i = sqlSession.selectOne("examInfo.selectAllMyGradesCount", query);
        return i.intValue();
    }

    /**
     * 察看我的成绩列表信息
     * 
     * @author yangzq
     * @param query
     * @return
     */
    public List<GradeModel> selectAllMyGradesDetail(ListQuery query) {
        return sqlSession.selectList("examInfo.selectAllMyGradesDetail", query);
    }

    /**
     * 查询所有课程测试管理中配置好的测试信息记录数
     * 
     * @author yangzq
     * @param query
     * @return
     */
    public int queryAllExamInfoCount(ListQuery query) {
        return (Integer) sqlSession.selectOne("examInfo.queryAllExamInfoCount", query);
    }

    /**
     * 查询所有课程测试管理中配置好的测试信息
     * 
     * @author yangzq
     * @param query
     * @return
     */
    public List<ExamInfo> queryAllExamInfo(ListQuery query) {
        return sqlSession.selectList("examInfo.queryAllExamInfo", query);
    }

    /**
     * 检验课程测试创建的名字是否有重复
     * 
     * @author yangzq
     * @param name
     * @return
     */
    public int checkTestName(String name) {
        return (Integer) sqlSession.selectOne("examInfo.selectExamInfoCountByName", name);
    }

    /**
     * 创建新的课程测试
     * 
     * @author yangzq
     * @param examInfo
     */
    public void newExaminfo(ExamInfo examInfo) throws Exception {
        sqlSession.insert("examInfo.newExaminfo", examInfo);
    }

    /**
     * 更新课程测试数据
     * 
     * @author yangz
     * @param examInfo
     */
    public void updateExaminfo(ExamInfo examInfo) {
        sqlSession.update("examInfo.updateExaminfo", examInfo);
    }

    /**
     * 删除课程测试数据
     * 
     * @author yangzq
     * @param delExins
     */
    public void delExin(String[] delExins) throws Exception {
        // TODO Auto-generated method stub
        sqlSession.delete("examInfo.delExin", delExins);
    }

    /**
     * 查询所有的考试课程
     * 
     * @author lixw
     * @param
     * @return
     */

    public List<ExamCourModel> getUserCourse() {

        return sqlSession.selectList("examInfo.getExamCourse");

    }

    /**
     * 返回说有学生成绩
     * 
     * @author lixw
     * @param query
     * @return List<ScoreModel>
     */

    public List<ScoreModel> getAllScore(ListQuery query) {

        return sqlSession.selectList("examInfo.getAllScore", query);
    }

    /**
     * 统计分页
     * 
     * @author lixw
     * @param query
     * @return int
     */
    public int countAllScore(ListQuery query) {
        Integer i = sqlSession.selectOne("examInfo.countAllScore", query);
        return i.intValue();
    }

    /**
     * 删除学生成绩
     * 
     * @author lixw
     * @param deleteScoreQuery
     * @throws Exception
     * @return
     */
    public void deleteCourseScore(String[] deleteScoreQuery) throws Exception {

        sqlSession.delete("examInfo.deleteCourseScore", deleteScoreQuery);

    }

    /**
     * 删除与此此考试相关的试题得分
     * 
     * @author lixw
     * @param deleteScoreQuery
     * @throws Exception
     */

    public void deleteSolve(String[] deleteScoreQuery) throws Exception {

        sqlSession.delete("examInfo.deleteSlove", deleteScoreQuery);
    }

    /**
     * 根据考试结果检查一次是否有记录，用于二次提交成绩更新测试成绩
     * 
     * @author yangzq
     * @param resultModel
     * @return
     */
    public List<ReAutoTest> examAutoTest(ExamResultModel resultModel) {
        return sqlSession.selectList("examInfo.examAutoTest", resultModel);
    }

    /**
     * 加载用户的测试数据
     * 
     * @author yangzq
     * @param query,userId,exinId
     * @return
     */
    public ExamResultModel loadExamResult(Map<String, Object> query) {
        return sqlSession.selectOne("examInfo.loadExamResult", query);
    }

    /**
     * 更新考试结果，保存用户最高分
     * 
     * @author yangzq
     * @param resultModel
     */
    public void updateAutoTest(ExamResultModel resultModel) {
        sqlSession.update("examInfo.updateAutoTest", resultModel);
    }

    /**
     * 更新当前测试的结果数据
     * 
     * @author yangzq
     * @param ques
     */
    public void resultQuesForUpdate(ResultQuesModel ques) {
        sqlSession.update("examInfo.resultQuesForUpdate", ques);
    }

    /**
     * 检查当前用户是否已考试
     * 
     * @author yangzq
     * @param userId
     * @param courseId
     * @return
     */
    public int checkCourseIsTest(Map<String, Object> map) {
        return (Integer) sqlSession.selectOne("examInfo.checkCourseIsTest", map);
    }

    /**
     * 加载课程测试条件
     * 
     * @author 何天翔
     * @param query
     * @return
     */
    public List<TestCondition> loadTestCondition(Map<String, Object> query) {
        return sqlSession.selectList("examInfo.courseTestScore", query);
    }

    /**
     * 检验课程测试答案的次數
     * 
     * @author htx 
     * @param rateId
     * @return
     */
    public int checkSolveCount(int rateId) {
        return (Integer) sqlSession.selectOne("examInfo.selectSolveCount", rateId);
    }
    /**
     * 测试对应的课程成绩总和
     * 
     * @author htx 
     * @param rateId
     * @return
     */
    public float courseTestScore(ListQuery query) {
        return (Float) sqlSession.selectOne("examInfo.courseScore", query);
    }
}
