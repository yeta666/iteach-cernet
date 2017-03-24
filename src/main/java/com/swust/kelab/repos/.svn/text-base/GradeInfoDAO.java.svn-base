package com.swust.kelab.repos;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.swust.kelab.domain.Clas;
import com.swust.kelab.domain.Grade;
import com.swust.kelab.model.ClasModel;
import com.swust.kelab.repos.bean.ListQuery;

@Repository
public class GradeInfoDAO {

    private SqlSession sqlSession;

    @Autowired
    public void setSqlSession(SqlSession sqlSession){
        this.sqlSession = sqlSession;
    }

    /**增加年级
     * @author lancer
     * @param grade
     * @throws Exception
     */
    public int insertGrade(Grade grade) throws Exception{
        sqlSession.insert("classInfo.insertGrade", grade);
        return grade.getGradId();
    }

    /**修改年级
     * @author lancer
     * @param grade
     * @throws Exception
     */
    public void updateGrade(Grade grade) throws Exception{
        sqlSession.update("classInfo.updateGrade", grade);
    }

    /**
     * 插入班级
     * @author lancer
     * @param clas
     * @throws Exception
     */
    public int insertClas(Clas clas) throws Exception{
        return sqlSession.insert("classInfo.insertClass", clas);
    }

    /**
     * 修改班级
     * @author lancer
     * @param clas
     * @throws Exception
     */
    public void updateClas(Clas clas) throws Exception{
        sqlSession.update("classInfo.updateClass", clas);
    }

    /**
     * 删除班级
     * @author lancer
     * @param clasId
     * @throws Exception
     */
    public void deleteClas(String[] clasId) throws Exception{
        sqlSession.delete("classInfo.deleteClass", clasId);
    }
    
    /**
     * 删除多余的年级
     * @author lancer
     * @param clasId
     * @throws Exception
     */
    public void deleteUselessGrade() throws Exception{
        sqlSession.delete("classInfo.deleteUselessGrade");
    }


    /**
     * 查询班级
     * @author lancer
     * @return list<ClasModel>
     */
    public  List<ClasModel>  selectAllClas(Map query){
        return sqlSession.selectList("classInfo.selectAllClass",query);
    }

    /**
     * 班级统计
     * @author lancer
     * @return 
     */
    public int countAllClas(Map query){
        return ((Integer) sqlSession.selectOne("classInfo.countAllClass",query)).intValue();
    }


    public  ClasModel  selectClass(int classId){
        return sqlSession.selectOne("classInfo.selectOneClass",classId );
    }

    public int isGradeExsit(Grade grade){
        return ((Integer)sqlSession.selectOne("classInfo.isGradeExsit",grade)).intValue();
    }

    public int isClassExsit(Clas clas){
        return ((Integer)sqlSession.selectOne("classInfo.isClassExsit",clas));
    }



    public int findClasIdByClaNamAndGradId(Clas clas){
        return ((Integer)sqlSession.selectOne("classInfo.findClasIdByClaNamAndGradId",clas));
    }  
    
}
