package com.swust.kelab.repos;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.swust.kelab.domain.Attachment;
import com.swust.kelab.utils.FileUtil;

/**
 * 处理附件表的dao
 * 
 * @author 吴岘辉
 *
 */
@Repository
public class AttachmentDAO {
    final Logger logger = LoggerFactory.getLogger(getClass());
    
    private SqlSession sqlSession;
    
    @Autowired
    public void setSqlSession(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }
    
    public List<Attachment> getAttachBySourceTypeId(Map query){
        return sqlSession.selectList("attachment.getAttachBySourceTypeId",
                query);
    }
    
    /**
     * 新增附件
     * 
     * @param attach 附件对象
     * @return 新增行的id
     * @see Attachment
     */
    public int createAttachment(Attachment attach){
        return ((Integer)sqlSession.insert("attachment.createAttachment",
                attach)).intValue();
    }
    
    /**
     * 删除附件
     * 
     * @param attachId 附件id
     * @return         影响的数据库行数
     */
    public int deleteAttachment(int attachId){
        return ((Integer)sqlSession.delete("attachment.deleteAttachment",
                attachId)).intValue();
    }
    
    /**
     * 增加附件下载数
     * 
     * @param attachId 附件id
     * @return         影响的数据库行数
     */
    public int addAttachDownNum(int attachId){
        return ((Integer)sqlSession.update("attachment.addAttachDownNum",
                attachId)).intValue();
    }
    
    /**
     * 设置附件的原件id
     * 
     * @param query    包含附件id和原件id
     * @return         影响的数据库行数
     */
    public int changeAttachSourceId(Map query){
        return ((Integer)sqlSession.update("attachment.setAttachSourceId",
                query)).intValue();
    }
    
    public Attachment selectAttachById(int attachId){
        return sqlSession.selectOne("attachment.selectAttachById", attachId);
    }
    /**
     * 根据sourceid以及类型 批量删除记录
     * @param query 删除条件
     */
	public int deleteBatchAttachment(Map<String, Integer> query) {
		return ((Integer)sqlSession.delete("attachment.deleteBatch", query)).intValue();
	}
	/**
	 * 批量删除附件记录和附件文件
	 * @param query 删除查询条件
	 * @param basePath 文件的绝对路径前缀
	 * @return
	 */
    public int deleteBatchAttachmentAndRecored(Map<String, Integer> query,String basePath){
		List<Attachment> attachMents = getAttachBySourceTypeId(query);
		String absolutePath = null;
		//一次删除附件文件
		for(Attachment attach:attachMents){
			if(attach.getAttaFilename()!=null){
			absolutePath = basePath+"/"+attach.getAttaLocation()+"/"+attach.getAttaFilename();
			FileUtil.deleteOneFile(absolutePath);
			}
		}
		//删除附件记录
		return deleteBatchAttachment(query);
    }
    /**
     * 查询某类附件的所有信息
     * @param type
     * @return
     */
	public List<Attachment> findAttachmentsByType(int type) {
		// TODO Auto-generated method stub
		return sqlSession.selectList("attachment.selectAttachByType",type);
	}
	/**
	 * 添加attachment
	 * @param atta
	 * @return
	 */
	public Integer addAttachment(Attachment atta) {
		// TODO Auto-generated method stub
		sqlSession.selectList("attachment.createAttachment",atta);
		return atta.getAttaId();
	}

	public Attachment findAttachmentById(int attaId) {
		// TODO Auto-generated method stub
		return sqlSession.selectOne("attachment.findAttachmentById",attaId);
	}
}
