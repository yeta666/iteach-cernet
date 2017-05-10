package com.swust.kelab.repos;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.swust.kelab.domain.ScoreExportConfirm;
import com.swust.kelab.model.ScoreExportConfirmModel;

@Repository
public class ScoreExportConfirmDAO {
    final Logger logger = LoggerFactory.getLogger(getClass());
    
    private SqlSession sqlSession;
    
    @Autowired
    public void setSqlSession(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }
    
    /**
     * 导出时创建一条成绩导出记录
     * 
     * @param sec
     * @return
     */
    public int createExportConfirm(ScoreExportConfirm sec){
        return ((Integer)sqlSession.insert("scoreExportConfirm.create", sec)).intValue();
    }
    
    /**
     * 统计导出记录的总数
     * 
     * @param sec
     * @return
     */
    public int countTotalExportNum(Map query){
        return ((Integer)sqlSession.selectOne("scoreExportConfirm.countNum", query)).intValue();
    }
    
    /**
     * 根据搜索条件查询导出确认列表
     * 
     * @param query
     * @return
     */
    public List<ScoreExportConfirmModel> viewExportConfirmsByQuery(Map query){
        return sqlSession.selectList("scoreExportConfirm.viewList", query);
    }
    
    /**
     * 确认一条导出记录
     * 
     * @param sec
     * @return
     */
    public int confirmOneExport(ScoreExportConfirm sec){
        return sqlSession.update("scoreExportConfirm.confirm", sec);
    }
    
    /**
     * 根据id获取一条导出记录
     * 
     * @param secoId  记录id
     * @return
     */
    public ScoreExportConfirmModel viewOneExportById(int secoId){
        return sqlSession.selectOne("scoreExportConfirm.viewOne", secoId);
    }
    
    /**
     * 批量删除导出记录
     * 
     * @param sec
     * @return
     */
    public int deleteExport(List<Integer> ids){
        return sqlSession.delete("scoreExportConfirm.delete", ids);
    }  

}
