package com.swust.kelab.repos;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.swust.kelab.model.CourseStatisticModel;
import com.swust.kelab.model.PlatformStatisticModel;

/**
 * 视频学习时间学习记录类
 *
 */
@Repository()
public class LearningProcessRecordDAO {
    final Logger logger = LoggerFactory.getLogger(getClass());
    
    private SqlSession sqlSession;
    
    @Autowired
    public void setSqlSession(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }
    
    /**
     * 查询学生个人中心对应的课程统计
     * @param params  查询参数
     * @return        统计信息
     * @see CourseStatisticModel
     */
    public List<CourseStatisticModel> staCourseLearnByStu(Map params){
        try {
            return sqlSession.selectList("learnProcessRecord.staByCourse", params);
        } catch (Exception e) {
            // TODO: handle exception
            logger.error(e.getLocalizedMessage());
        }
        return null;
    }
    
    /**
     * 按照学校分别统计该学校学生的学习记录
     * 
     * @return 学习记录统计
     * @see PlatformStatisticModel
     */
    public List<PlatformStatisticModel> staCourseLearnBySchool(){
        try {
            return sqlSession.selectList("learnProcessRecord.staBySchool");
        } catch (Exception e) {
            // TODO: handle exception
            logger.error(e.getLocalizedMessage());
        }
        return null;
    }
    
    /**
     * 通过学习记录，统计总的学习次数和学习时间
     * 
     * @return 统计信息
     * @see PlatformStatisticModel
     */
    public PlatformStatisticModel staTotalLearningRecord(){
        return sqlSession.selectOne("learnProcessRecord.staTotalLearningRecord");
    }
}
