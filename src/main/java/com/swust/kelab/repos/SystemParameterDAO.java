package com.swust.kelab.repos;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.swust.kelab.domain.SystemParameter;
import com.swust.kelab.model.SystemParameterModel;

@Repository
public class SystemParameterDAO {

    private SqlSession sqlSession;

    @Autowired
    public void setSqlSession1(SqlSession sqlSession){
        this.sqlSession=sqlSession;
    }
    /**
     * 返回所有参数
     * @author 李晓伟
     * @return List<SystemParameterModel> list
     */
    public List<SystemParameter> selectAllSypa(){
        return sqlSession.selectList("systemParameter.selectAllSypa");
    }

    /**
     * 修改参数
     * @author 李晓伟
     * @param list
     * @throws Exception
     */
    public void updateSypa(List<SystemParameterModel> list) throws Exception{

        for(SystemParameterModel  sypaModel : list){
            sqlSession.update("systemParameter.updateSypa",sypaModel);
        }
    }



    @Autowired
    public void setSqlSession(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    /**
     * 返回所有指定类型参数
     * @param pType 参数类型
     * @author 李晓伟
     * @return List<SystemParameterModel> list
     */
    public List<SystemParameter> selectAllSypa(int pType) {
        return sqlSession.selectList("systemParameter.selectAllSypa", pType);
    }



    /**
     * 删除友情链接
     * @author 李晓伟
     * @param sypa
     * @throws Exception
     */
    public void  updateLink( SystemParameter   sypa) throws Exception {
        sqlSession.update("systemParameter.updateLink", sypa);
    }

    /**
     *添加友情链接
     *@author 李晓伟
     * @param sypa
     * @throws Exception
     */
    public void addLink(SystemParameter sypa) throws Exception{
        sqlSession.insert("systemParameter.insertLink", sypa);
    }

    /**
     * 删除友情链接
     * @author李晓伟
     * @param linkId
     * @throws Exception
     */
    public  void deleteLink(int linkId) throws Exception{
        sqlSession.delete("systemParameter.deleteLink", linkId);
    }
    
    /**
     * 
     * @param enName
     * @return
     */
    public SystemParameter viewOneParameterByEnName(String enName){
        return sqlSession.selectOne("systemParameter.selectOneByEnName",enName);
    }
}
