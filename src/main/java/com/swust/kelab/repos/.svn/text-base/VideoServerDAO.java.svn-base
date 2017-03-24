package com.swust.kelab.repos;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.swust.kelab.domain.VedioServer;
import com.swust.kelab.model.VideoServerModel;
import com.swust.kelab.repos.bean.ListQuery;

@Repository
public class VideoServerDAO {
      
    private SqlSession sqlSession;
    
    @Autowired
    public void setSqlSession( SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }
    
    /**
     * 返回服务器列表
     * @author lancer
     * @return List<VideoServerModel> list
     */
    public List<VideoServerModel> selectAllServer(ListQuery query){
        return sqlSession.selectList("videoServer.selectAllServer",query);
    }
    
    /**
     * 删除服务器数据
     * @author lancer
     * @param veseId
     * @throws Exception
     */
    public void deleteServer(String[]  veseId) throws Exception{
        sqlSession.delete("videoServer.deleteServer",veseId);
    }
    
    /**
     * 更新服务器数据
     * @author lancer
     * @param vedioServer
     * @throws Exception
     */
    public void updateServer(VedioServer vedioServer) throws Exception{
        sqlSession.update("videoServer.updateServer", vedioServer);
    }
    /**
     * 添加服务器数据
     * @author lancer
     * @param vedioServer
     * @throws Exception
     */
    public int addServer(VedioServer vedioServer)throws Exception{
        return ((Integer)sqlSession.insert("videoServer.insertServer", vedioServer)).intValue();
    }
    
    
    /**
     * 统计分页
     * @author  lancer
     * @param query
     * @return int
     */
      public int countAllServer(ListQuery query) {
          Integer i = sqlSession.selectOne("videoServer.countAllServer",query);
          return i.intValue();
      }
      /**
       *单条查询
       *@author lancer
       *@param int veseId
       *@return 
       */
      
      public VideoServerModel selectById(int veseId){
          return sqlSession.selectOne("videoServer.selectById",veseId);
      }
      
      /**
       * 验证服务器是否存在
       * @author lancer
       * @param vedioServer
       * @return
       */
      public int isServerExsit(VedioServer vedioServer){
          return ((Integer)sqlSession.selectOne("videoServer.isServerExsit",vedioServer)).intValue();
      }
      
      public List<VideoServerModel> selectOrderServer(){
          return sqlSession.selectList("videoServer.selectOrderServer");
      }
}
